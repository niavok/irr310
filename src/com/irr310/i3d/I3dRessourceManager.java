package com.irr310.i3d;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.fonts.FontFactory;
import com.irr310.i3d.view.BorderParams.CornerStyle;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.DrawableView;
import com.irr310.i3d.view.LayoutParams.LayoutGravity;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.LinearLayout.LayoutOrientation;
import com.irr310.i3d.view.Rect;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.ScrollView.ScrollAxis;
import com.irr310.i3d.view.ScrollView.ScrollLimits;
import com.irr310.i3d.view.TextView.Gravity;
import com.irr310.i3d.view.drawable.BitmapDrawable;
import com.irr310.i3d.view.drawable.BitmapFactory;
import com.irr310.i3d.view.drawable.CircleDrawable;
import com.irr310.i3d.view.drawable.ColoredDrawable;
import com.irr310.i3d.view.drawable.Drawable;
import com.irr310.i3d.view.drawable.GradientDrawable;
import com.irr310.i3d.view.drawable.InsetDrawable;
import com.irr310.i3d.view.drawable.SolidDrawable;
import com.irr310.i3d.view.Triangle;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.ViewParent;
import com.irr310.i3d.view.Waiter;

public class I3dRessourceManager {

    private static I3dRessourceManager instance = new I3dRessourceManager();
    private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private final Map<String, RessourceFileCache> fileCache = new HashMap<String, RessourceFileCache>();
    Map<String, Drawable> drawableCache = new HashMap<String, Drawable>();

    Graphics g;
    private FontFactory fontFactory;

    public I3dRessourceManager() {
        fontFactory = new FontFactory();
    }

    public static I3dRessourceManager getInstance() {
        return instance;
    }

    public void setGraphics(Graphics g) {
        this.g = g;
    }

    public static View loadView(String viewId) {
        return instance.doLoadView(viewId);
    }

    public View doLoadView(String viewId) {

        String[] layoutParts = viewId.split("@");
        String localId = layoutParts[0];
        String fileId = layoutParts[1];

        View view = null;

        if (fileCache.containsKey(fileId)) {

        } else {
            parseFile(fileId);
        }

        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        view = ressourceFileCache.getView(viewId);

        if (view == null) {
            Log.error("Unknown view '" + viewId);
        }

        return view;
    }

    private void parseFile(String fileId) {
        RessourceFileCache ressourceFileCache = new RessourceFileCache(fileId);
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = docBuilder.parse(new File("res/" + fileId + ".xml"));

            Element root = doc.getDocumentElement();

            if (root.getNodeName().contains("Layout")) {
                ParseView(root, ressourceFileCache);
            } else if (root.getNodeName().equals("resources")) {
                parseResources(root, ressourceFileCache);
            }

        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            throw new RessourceLoadingException("Failed to parse file '" + fileId, e);
        } catch (IOException e) {
            throw new RessourceLoadingException("Failed to load file '" + fileId, e);
        }

        fileCache.put(fileId, ressourceFileCache);
        fontFactory.free();
    }

    private View ParseView(Element element, RessourceFileCache ressourceFileCache) {
        String nodeName = element.getNodeName();
        // Log.trace("nodeName=" + nodeName);

        View view = null;
        if (nodeName.equals("View")) {
            view = linkView(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("RelativeLayout")) {
            view = NewRelativeLayout(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("LinearLayout")) {
            view = NewLinearLayout(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Rect")) {
            view = NewRect(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Button")) {
            view = NewButton(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Triangle")) {
            view = NewTriangle(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Waiter")) {
            view = NewWaiter(element);
        } else if (nodeName.equals("TextView")) {
            view = NewTextView(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("ScrollView")) {
            view = NewScrollView(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Drawable")) {
            view = NewDrawableView(element, ressourceFileCache.getFileId());
        } else {
            throw new RessourceLoadingException("Unknown nodeName=" + nodeName);
        }

        if (view != null && view instanceof ViewParent) {
            ViewParent viewParent = (ViewParent) view;
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // TODO error
                    continue;
                }
                Element subElement = (Element) node;
                viewParent.addChild(ParseView(subElement, ressourceFileCache));
            }
        }

        if (view.getId() != null) {
            ressourceFileCache.addWidget(view.getId(), view);
        }

        return view;
    }

    private View linkView(Element element, String fileId) {
        String ref = element.getAttribute("i3d:ref");
        View view = loadView(ref).duplicate();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(view);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(view);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (attrName.equals("i3d:ref")) {
                // Already processed
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for View");
            }
        }

        return view;
    }

    private void parseDrawables(Element element, String fileId) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("gradient")) {
                parseGradientDrawable(subElement, fileId);
            } else if (subElement.getNodeName().equals("solid")) {
                parseSolidDrawable(subElement, fileId);
            } else if (subElement.getNodeName().equals("circle")) {
                parseCircleDrawable(subElement, fileId);
            } else if (subElement.getNodeName().equals("inset")) {
                parseInsetDrawable(subElement, fileId);
            } else if (subElement.getNodeName().equals("colored")) {
                parseColoredDrawable(subElement, fileId);
            } else {
                throw new RessourceLoadingException("Unknown drawable: " + subElement.getNodeName());
            }
        }
    }

    private void parseResources(Element element, RessourceFileCache ressourceFileCache) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("color")) {
                String colorName = subElement.getAttribute("name");
                Color color = new Color(subElement.getTextContent());
                ressourceFileCache.addColor(colorName, color);
            } else if (subElement.getNodeName().equals("string")) {
                String stringName = subElement.getAttribute("name");
                String string = subElement.getTextContent();
                ressourceFileCache.addString(stringName, string);
            } else if (subElement.getNodeName().equals("font")) {
                String fontName = subElement.getAttribute("name");
                String fontCode = subElement.getAttribute("font");
                String fontStyle = subElement.getAttribute("style");
                int fontSize = Integer.parseInt(subElement.getAttribute("size"));
                Font font = fontFactory.generateFont(fontCode, fontSize, fontStyle);
                ressourceFileCache.addFont(fontName, font);
            } else if (subElement.getNodeName().equals("style")) {
                parseStyle(subElement, ressourceFileCache);
            } else {
                throw new RessourceLoadingException("Unknonw resources: " + subElement.getNodeName());
            }
        }
    }

    private void parseStyle(Element element, RessourceFileCache ressourceFileCache) {
        String name = element.getAttribute("name");

        Style style = new Style();
        style.setParent(element.getAttribute("parent"));

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (!subElement.getNodeName().equals("item")) {
                continue;
            }

            String attrName = subElement.getAttribute("name");
            String attrValue = subElement.getTextContent();

            StyleFactory styleFactory = new StyleFactory(style);

            if (checkViewAttrs(attrName, attrValue, null, styleFactory)) {
            } else if (checkColor(attrName, attrValue, styleFactory)) {
            } else if (checkFont(attrName, attrValue, styleFactory)) {
            } else if (checkGravity(attrName, attrValue, styleFactory)) {
            } else if (checkBorderColor(attrName, attrValue, styleFactory)) {
            } else if (checkBorderSize(attrName, attrValue, styleFactory)) {
            } else if (checkBackground(attrName, attrValue, styleFactory)) {
            } else if (checkCornerLeftTopStyle(attrName, attrValue, styleFactory)) {
            } else if (checkCornerRightTopStyle(attrName, attrValue, styleFactory)) {
            } else if (checkCornerLeftBottomStyle(attrName, attrValue, styleFactory)) {
            } else if (checkCornerRightBottomStyle(attrName, attrValue, styleFactory)) {
            } else if (checkCornerLeftTopSize(attrName, attrValue, styleFactory)) {
            } else if (checkCornerRightTopSize(attrName, attrValue, styleFactory)) {
            } else if (checkCornerLeftBottomSize(attrName, attrValue, styleFactory)) {
            } else if (checkCornerRightBottomSize(attrName, attrValue, styleFactory)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for Style");
            }

        }
        ressourceFileCache.addStyle(name, style);
    }

    private Waiter NewWaiter(Element element) {
        return new Waiter();
    }

    private Rect NewRect(Element element, String fileId) {
        Rect rect = new Rect();
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(rect);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (attrName.equals("i3d:backgroundColor")) {
                rect.setBackgroundColor(loadColor(attrValue));
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for Rect");
            }
        }

        return rect;
    }

    private Triangle NewTriangle(Element element, String fileId) {
        Triangle triangle = new Triangle();
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(triangle);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (attrName.equals("i3d:backgroundColor")) {
                triangle.setBackgroundColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:points")) {
                String[] pointsStrings = attrValue.split("\\|");
                if (pointsStrings.length != 3) {
                    throw new RessourceLoadingException("A triangle must have 3 points and not " + pointsStrings.length + "(" + attrValue + ")");
                }
                int index = 0;
                MeasurePoint[] points = new MeasurePoint[3];
                for (String pointString : pointsStrings) {
                    String[] mesuresString = pointString.split(",");
                    if (mesuresString.length != 2) {
                        throw new RessourceLoadingException("A point must have 2 points and not " + mesuresString.length + "(" + pointString + ")");
                    }

                    MeasurePoint point = new MeasurePoint();
                    point.setX(parseMeasure(mesuresString[0], Axis.HORIZONTAL));
                    point.setY(parseMeasure(mesuresString[1], Axis.VERTICAL));

                    points[index++] = point;
                }

                triangle.setPoints(points);

            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for Triangle");
            }
        }

        return triangle;
    }

    private Measure parseMeasure(String mesureString, Axis axis) {
        boolean relative = false;
        String stringValue = "0";
        if (mesureString.endsWith("px")) {
            relative = false;
            stringValue = mesureString.substring(0, mesureString.length() - 2);
        } else if (mesureString.endsWith("%")) {
            relative = true;
            stringValue = mesureString.substring(0, mesureString.length() - 1);
        } else {
            Log.error("Unknown unit for mesure '" + mesureString + "'");
            return null;
        }

        
        float value = 0;
        try {
            value = Float.parseFloat(stringValue);
        } catch(NumberFormatException e) {
            throw new RessourceLoadingException("Bad format number for measure '" + mesureString+"'", e);
        }

        return new Measure(value, relative, axis);
    }

    private RelativeLayout NewRelativeLayout(Element element, String fileId) {
        RelativeLayout relativeLayout = new RelativeLayout();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(relativeLayout);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(relativeLayout);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for RelativeLayout");
            }
        }

        return relativeLayout;
    }
    
    private DrawableView NewDrawableView(Element element, String fileId) {
        DrawableView drawableView = new DrawableView();
        String ref = element.getAttribute("i3d:ref");
        drawableView.setDrawable(loadDrawable(ref));
        
        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(drawableView);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(drawableView);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (attrName.equals("i3d:ref")) {
                // Already processed
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for DrawableView");
            }
        }

        return drawableView;
    }

    private LinearLayout NewLinearLayout(Element element, String fileId) {
        LinearLayout linearLayout = new LinearLayout();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(linearLayout);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(linearLayout);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (checkOrientation(attrName, attrValue, linearLayout)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for LinearLayout");
            }
        }

        return linearLayout;
    }

    private TextView NewTextView(Element element, String fileId) {
        TextView textView = new TextView();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(textView);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            TextViewFactory textViewFactory = new TextViewFactory(textView);

            if (checkViewAttrs(attrName, attrValue, fileId, textViewFactory)) {
            } else if (checkColor(attrName, attrValue, textViewFactory)) {
            } else if (checkText(attrName, attrValue, textViewFactory)) {
            } else if (checkFont(attrName, attrValue, textViewFactory)) {
            } else if (checkGravity(attrName, attrValue, textViewFactory)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for TextView");
            }
        }

        return textView;
    }

    private ScrollView NewScrollView(Element element, String fileId) {
        ScrollView view = new ScrollView();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(view);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            ViewFactory viewFactory = new ViewFactory(view);

            if (checkViewAttrs(attrName, attrValue, fileId, viewFactory)) {
            } else if (attrName.equals("i3d:scroll_axis")) {
                if(attrValue.equals("vertical")) {
                    view.setScrollAxis(ScrollAxis.VERTICAL);
                } else if(attrValue.equals("horizontal")) {
                    view.setScrollAxis(ScrollAxis.HORIZONTAL);
                } else  if(attrValue.equals("both")) {
                    view.setScrollAxis(ScrollAxis.BOTH);
                } else {
                    Log.warn("Unknown value '" + attrValue + " for attribute '" + attrName + "' for ScrollView. Correct values are 'vertical', 'horizontal', or 'both'");
                }
            } else if (attrName.equals("i3d:scroll_limits")) {
                if(attrValue.equals("strict")) {
                    view.setScrollLimits(ScrollLimits.STRICT);
                } else if(attrValue.equals("soft")) {
                    view.setScrollLimits(ScrollLimits.SOFT);
                } else  if(attrValue.equals("free")) {
                    view.setScrollLimits(ScrollLimits.FREE);
                } else {
                    Log.warn("Unknown value '" + attrValue + " for attribute '" + attrName + "' for ScrollView. Correct values are 'stric', 'soft', or 'free'");
                }
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ScrollView");
            }
        }

        return view;
    }

    private Button NewButton(Element element, String fileId) {
        Button button = new Button();

        Style style = loadStyle(element.getAttribute("i3d:style"));
        style.apply(button);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            TextViewFactory textViewFactory = new TextViewFactory(button);

            if (checkViewAttrs(attrName, attrValue, fileId, textViewFactory)) {
            } else if (checkColor(attrName, attrValue, textViewFactory)) {
            } else if (checkText(attrName, attrValue, textViewFactory)) {
            } else if (checkFont(attrName, attrValue, textViewFactory)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for Button");
            }
        }

        return button;
    }

    private boolean checkViewAttrs(String attrName, String attrValue, String fileId, LayoutFactory view) {
        boolean used = true;
        if (checkId(attrName, attrValue, fileId, view)) {
        } else if (attrName.equals("i3d:style")) {
            // Already checked
        } else if (checkHelp(attrName, attrValue, view)) {
        } else if (checkLayoutWidth(attrName, attrValue, view)) {
        } else if (checkLayoutHeight(attrName, attrValue, view)) {
        } else if (checkGravityX(attrName, attrValue, view)) {
        } else if (checkGravityY(attrName, attrValue, view)) {
        } else if (checkLayoutMarginTop(attrName, attrValue, view)) {
        } else if (checkLayoutMarginBottom(attrName, attrValue, view)) {
        } else if (checkLayoutMarginLeft(attrName, attrValue, view)) {
        } else if (checkLayoutMarginRight(attrName, attrValue, view)) {
        } else if (checkLayoutPaddingTop(attrName, attrValue, view)) {
        } else if (checkLayoutPaddingBottom(attrName, attrValue, view)) {
        } else if (checkLayoutPaddingLeft(attrName, attrValue, view)) {
        } else if (checkLayoutPaddingRight(attrName, attrValue, view)) {
        } else {
            used = false;
        }
        return used;
    }

    private boolean checkId(String attrName, String attrValue, String fileId, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:id")) {
            view.setId(attrValue + "@" + fileId);
            used = true;
        }
        return used;
    }

    private boolean checkHelp(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:help")) {
            view.setHelp(loadString(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkColor(String attrName, String attrValue, TextFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:color")) {
            view.setTextColor(loadColor(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkText(String attrName, String attrValue, TextFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:text")) {
            view.setText(loadString(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkFont(String attrName, String attrValue, TextFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:font")) {
            view.setFont(loadFont(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkLayoutWidth(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;

        if (attrName.equals("i3d:layout_width")) {
            Measure measure = null;
            if (attrValue.equals("match_parent")) {
                view.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
                used = true;
            } else if (attrValue.equals("wrap_content")) {
                view.setLayoutWidthMeasure(LayoutMeasure.WRAP_CONTENT);
                used = true;
            } else if ((measure = parseMeasure(attrValue, Axis.HORIZONTAL)) != null) {
                view.setLayoutWidthMeasure(LayoutMeasure.FIXED);
                view.setWidthMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_width attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutHeight(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_height")) {
            Measure measure = null;
            if (attrValue.equals("match_parent")) {
                view.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
                used = true;
            } else if (attrValue.equals("wrap_content")) {
                view.setLayoutHeightMeasure(LayoutMeasure.WRAP_CONTENT);
                used = true;
            } else if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setLayoutHeightMeasure(LayoutMeasure.FIXED);
                view.setHeightMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_height attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutPaddingTop(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_paddingTop")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setPaddingTopMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_paddingTop attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutPaddingBottom(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_paddingBottom")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setPaddingBottomMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_paddingBottom attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutPaddingLeft(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_paddingLeft")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.HORIZONTAL)) != null) {
                view.setPaddingLeftMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_paddingLeft attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutPaddingRight(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_paddingRight")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.HORIZONTAL)) != null) {
                view.setPaddingRightMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_paddingRight attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutMarginTop(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginTop")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setMarginTopMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_marginTop attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutMarginBottom(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginBottom")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setMarginBottomMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_marginBottom attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutMarginLeft(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginLeft")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.HORIZONTAL)) != null) {
                view.setMarginLeftMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_marginLeft attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutMarginRight(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginRight")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.HORIZONTAL)) != null) {
                view.setMarginRightMeasure(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_marginRight attribute");
            }
        }
        return used;
    }

    private boolean checkGravityX(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_gravity_x")) {
            if (attrValue.equals("center")) {
                view.setLayoutGravityX(LayoutGravity.CENTER);
                used = true;
            } else if (attrValue.equals("left")) {
                view.setLayoutGravityX(LayoutGravity.LEFT);
                used = true;
            } else if (attrValue.equals("right")) {
                view.setLayoutGravityX(LayoutGravity.RIGHT);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_gravity_x attribute");
            }
        }
        return used;
    }

    private boolean checkGravityY(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_gravity_y")) {
            if (attrValue.equals("center")) {
                view.setLayoutGravityY(LayoutGravity.CENTER);
                used = true;
            } else if (attrValue.equals("top")) {
                view.setLayoutGravityY(LayoutGravity.TOP);
                used = true;
            } else if (attrValue.equals("bottom")) {
                view.setLayoutGravityY(LayoutGravity.BOTTOM);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:layout_gravity_y attribute");
            }
        }
        return used;
    }

    private boolean checkOrientation(String attrName, String attrValue, LinearLayout view) {
        boolean used = false;
        if (attrName.equals("i3d:orientation")) {
            if (attrValue.equals("vertical")) {
                view.setLayoutOrientation(LayoutOrientation.VERTICAL);
                used = true;
            } else if (attrValue.equals("horizontal")) {
                view.setLayoutOrientation(LayoutOrientation.HORIZONTAL);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:orientation attribute");
            }
        }
        return used;
    }

    private boolean checkGravity(String attrName, String attrValue, TextFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:gravity")) {
            if (attrValue.equals("top|left")) {
                view.setGravity(Gravity.TOP_LEFT);
                used = true;
            } else if (attrValue.equals("top|center")) {
                view.setGravity(Gravity.TOP_CENTER);
                used = true;
            } else if (attrValue.equals("top|right")) {
                view.setGravity(Gravity.TOP_RIGHT);
                used = true;
            } else if (attrValue.equals("center|left")) {
                view.setGravity(Gravity.CENTER_LEFT);
                used = true;
            } else if (attrValue.equals("center")) {
                view.setGravity(Gravity.CENTER);
                used = true;
            } else if (attrValue.equals("center|right")) {
                view.setGravity(Gravity.CENTER_RIGHT);
                used = true;
            } else if (attrValue.equals("bottom|left")) {
                view.setGravity(Gravity.BOTTOM_LEFT);
                used = true;
            } else if (attrValue.equals("bottom|center")) {
                view.setGravity(Gravity.BOTTOM_CENTER);
                used = true;
            } else if (attrValue.equals("bottom|right")) {
                view.setGravity(Gravity.BOTTOM_RIGHT);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:gravity attribute");
            }
        }
        return used;
    }

    private boolean checkBorderColor(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:borderColor")) {
            view.setBorderColor(loadColor(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkBorderSize(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:borderSize")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setBorderSize(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:borderSize attribute");
            }
        }
        return used;
    }

    private boolean checkCornerLeftTopSize(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerLeftTopSize")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setCornerLeftTopSize(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerLeftTopSize attribute");
            }
        }
        return used;
    }

    private boolean checkCornerRightTopSize(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerRightTopSize")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setCornerRightTopSize(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerRightTopSize attribute");
            }
        }
        return used;
    }

    private boolean checkCornerLeftBottomSize(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerLeftBottomSize")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setCornerLeftBottomSize(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerLeftBottomSize attribute");
            }
        }
        return used;
    }

    private boolean checkCornerRightBottomSize(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerRightBottomSize")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue, Axis.VERTICAL)) != null) {
                view.setCornerRightBottomSize(measure);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerRightBottomSize attribute");
            }
        }
        return used;
    }

    private boolean checkCornerLeftTopStyle(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerLeftTopStyle")) {
            if (attrValue.equals("square")) {
                view.setCornerLeftTopStyle(CornerStyle.SQUARE);
                used = true;
            } else if (attrValue.equals("bevel")) {
                view.setCornerLeftTopStyle(CornerStyle.BEVEL);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerLeftTopStyle attribute");
            }
        }
        return used;
    }

    private boolean checkCornerRightTopStyle(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerRightTopStyle")) {
            if (attrValue.equals("square")) {
                view.setCornerRightTopStyle(CornerStyle.SQUARE);
                used = true;
            } else if (attrValue.equals("bevel")) {
                view.setCornerRightTopStyle(CornerStyle.BEVEL);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerRightTopStyle attribute");
            }
        }
        return used;
    }

    private boolean checkCornerLeftBottomStyle(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerLeftBottomStyle")) {
            if (attrValue.equals("square")) {
                view.setCornerLeftBottomStyle(CornerStyle.SQUARE);
                used = true;
            } else if (attrValue.equals("bevel")) {
                view.setCornerLeftBottomStyle(CornerStyle.BEVEL);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerLeftBottomStyle attribute");
            }
        }
        return used;
    }

    private boolean checkCornerRightBottomStyle(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:cornerRightBottomStyle")) {
            if (attrValue.equals("square")) {
                view.setCornerRightBottomStyle(CornerStyle.SQUARE);
                used = true;
            } else if (attrValue.equals("bevel")) {
                view.setCornerRightBottomStyle(CornerStyle.BEVEL);
                used = true;
            } else {
                throw new RessourceLoadingException("Unsupported value '" + attrValue + "' for i3d:cornerRightBottomStyle attribute");
            }
        }
        return used;
    }

    private boolean checkBackground(String attrName, String attrValue, LayoutFactory view) {
        boolean used = false;
        if (attrName.equals("i3d:background")) {
            view.setBackground(loadDrawable(attrValue));
            used = true;
        }
        return used;
    }

    public Color loadColor(String colorId) {

        String[] colorParts = colorId.split("@");
        if (colorParts.length != 2) {
            throw new RessourceLoadingException("Fail to load color '" + colorId + "' : no @ found");
        }

        String localId = colorParts[0];
        String fileId = colorParts[1];

        Color color = null;

        if (fileCache.containsKey(fileId)) {

        } else {
            parseFile(fileId);
        }

        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        color = ressourceFileCache.getColor(localId);

        if (color == null) {
            throw new RessourceLoadingException("Unknown color '" + colorId);
        }

        return color;
    }

    public Drawable loadDrawable(String drawableId) {
        Drawable drawable = null;

        drawable = getDrawable(drawableId);
        if (drawable == null) {

            String[] drawableParts = drawableId.split("@");
            String fileId = drawableParts[1];

            parseDrawableFile(fileId);

            drawable = getDrawable(drawableId);
        }

        if (drawable == null) {
            Log.warn("Unknown drawable '" + drawableId);
            SolidDrawable mockdrawable = new SolidDrawable();
            mockdrawable.setColor(Color.pink);
            drawable = mockdrawable;
        }

        return drawable;
    }

    private String loadString(String stringId) {

        String[] stringParts = stringId.split("@");
        String localId = stringParts[0];
        String fileId = stringParts[1];

        String string = null;

        if (fileCache.containsKey(fileId)) {

        } else {
            parseFile(fileId);
        }

        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        string = ressourceFileCache.getString(localId);

        if (string == null) {
            Log.warn("Unknown string '" + stringId);
            string = stringId;
        }

        return string;
    }

    public Font loadFont(String fontId) {
        String[] fontParts = fontId.split("@");
        if (fontParts.length != 2) {
            throw new RessourceLoadingException("Fail to load font '" + fontId + "' : no @ found");
        }

        String localId = fontParts[0];
        String fileId = fontParts[1];

        Font font = null;

        if (fileCache.containsKey(fileId)) {

        } else {
            parseFile(fileId);
        }

        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        font = ressourceFileCache.getFont(localId);

        if (font == null) {
            throw new RessourceLoadingException("Unknown font '" + fontId);
        }

        return font;
    }

    Style loadStyle(String styleId) {

        if (styleId == null || styleId.isEmpty()) {
            return new Style();
        }

        String[] styleParts = styleId.split("@");
        if (styleParts.length != 2) {
            throw new RessourceLoadingException("Fail to load style '" + styleId + "' : no @ found");
        }

        String localId = styleParts[0];
        String fileId = styleParts[1];

        Style style = null;

        if (fileCache.containsKey(fileId)) {

        } else {
            parseFile(fileId);
        }

        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        style = ressourceFileCache.getStyle(localId);

        if (style == null) {
            throw new RessourceLoadingException("Unknown style '" + styleId);
        }

        return style;
    }

    private void parseDrawableFile(String fileId) {

        boolean found = false;
        // The file id can represent a xml file or a directory with images

        // Check the file
        File xmlFile = new File("res/drawable/" + fileId + ".xml");
        if (xmlFile.isFile()) {

            try {
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

                Document doc;
                doc = docBuilder.parse(xmlFile);

                Element root = doc.getDocumentElement();

                if (root.getNodeName().contains("drawables")) {
                    parseDrawables(root, fileId);
                }
                found = true;

            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // Check the directory
        File dirFile = new File("res/drawable/" + fileId);
        if (dirFile.isDirectory()) {
            File[] listFiles = dirFile.listFiles();
            for (File file : listFiles) {
                String extension = ".png";
                String fileName = file.getName();
                if (fileName.endsWith(extension)) {
                    loadPngImage(file.getName().substring(0, fileName.length() - extension.length()), fileId, file);
                }
            }
        }
    }

    private void parseGradientDrawable(Element element, String fileId) {
        String name = element.getAttribute("name");

        GradientDrawable drawable = new GradientDrawable();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("name")) {
                // Already processed
            } else if (attrName.equals("i3d:startColor")) {
                drawable.setStartColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:stopColor")) {
                drawable.setStopColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:angle")) {
                drawable.setAngle(Integer.parseInt(attrValue));
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for GradientDrawable");
            }
        }

        addDrawable(name + "@" + fileId, drawable);
    }

    private void parseSolidDrawable(Element element, String fileId) {
        String name = element.getAttribute("name");

        SolidDrawable drawable = new SolidDrawable();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("name")) {
                // Already processed
            } else if (attrName.equals("i3d:color")) {
                drawable.setColor(loadColor(attrValue));
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for SolidDrawable");
            }
        }

        addDrawable(name + "@" + fileId, drawable);
    }

    private void parseCircleDrawable(Element element, String fileId) {
        String name = element.getAttribute("name");

        CircleDrawable drawable = new CircleDrawable();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("name")) {
                // Already processed
            } else if (attrName.equals("i3d:color")) {
                drawable.setColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:quality")) {
                drawable.setQuality(Integer.parseInt(attrValue));
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for CircleDrawable");
            }
        }

        addDrawable(name + "@" + fileId, drawable);
    }

    private void parseInsetDrawable(Element element, String fileId) {
        String name = element.getAttribute("name");

        InsetDrawable drawable = new InsetDrawable();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("name")) {
                // Already processed
            } else if (attrName.equals("i3d:drawable")) {
                drawable.setDrawableName(attrValue);
            } else if (attrName.equals("i3d:insetTop")) {
                drawable.setInsetTop(Integer.parseInt(attrValue));
            } else if (attrName.equals("i3d:insetLeft")) {
                drawable.setInsetLeft(Integer.parseInt(attrValue));
            } else if (attrName.equals("i3d:insetBottom")) {
                drawable.setInsetBottom(Integer.parseInt(attrValue));
            } else if (attrName.equals("i3d:insetRight")) {
                drawable.setInsetRight(Integer.parseInt(attrValue));
            } else if (attrName.equals("i3d:width")) {
                drawable.setWidth(Integer.parseInt(attrValue));
            } else if (attrName.equals("i3d:height")) {
                drawable.setHeight(Integer.parseInt(attrValue));
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for InsetDrawable");
            }
        }

        addDrawable(name + "@" + fileId, drawable);
    }

    private void parseColoredDrawable(Element element, String fileId) {
        String name = element.getAttribute("name");

        ColoredDrawable drawable = new ColoredDrawable();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("name")) {
                // Already processed
            } else if (attrName.equals("i3d:drawable")) {
                drawable.setDrawableName(attrValue);
            } else if (attrName.equals("i3d:color")) {
                drawable.setColor(loadColor(attrValue));
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for ColoredDrawable");
            }
        }

        addDrawable(name + "@" + fileId, drawable);
    }
    
    private void loadPngImage(String name, String fileId, File file) {

        BitmapDrawable drawable = new BitmapFactory().loadPngDrawable(file);

        addDrawable(name + "@" + fileId, drawable);
    }

    public void addDrawable(String id, Drawable drawable) {
        drawableCache.put(id, drawable);
    }

    public Drawable getDrawable(String id) {
        return drawableCache.get(id);
    }

    private interface LayoutFactory {

        void setId(String attrValue);

        void setHelp(String help);

        void setLayoutGravityY(LayoutGravity left);

        void setLayoutGravityX(LayoutGravity center);

        void setMarginRightMeasure(Measure measure);

        void setMarginLeftMeasure(Measure measure);

        void setMarginBottomMeasure(Measure measure);

        void setMarginTopMeasure(Measure measure);

        void setPaddingRightMeasure(Measure measure);

        void setPaddingLeftMeasure(Measure measure);

        void setPaddingBottomMeasure(Measure measure);

        void setPaddingTopMeasure(Measure measure);

        void setHeightMeasure(Measure measure);

        void setLayoutHeightMeasure(LayoutMeasure fixed);

        void setWidthMeasure(Measure measure);

        void setLayoutWidthMeasure(LayoutMeasure matchParent);

        void setBackground(Drawable loadDrawable);

        void setCornerRightBottomStyle(CornerStyle square);

        void setCornerLeftBottomStyle(CornerStyle bevel);

        void setCornerRightTopStyle(CornerStyle bevel);

        void setCornerLeftTopStyle(CornerStyle bevel);

        void setCornerRightBottomSize(Measure measure);

        void setCornerLeftBottomSize(Measure measure);

        void setCornerRightTopSize(Measure measure);

        void setCornerLeftTopSize(Measure measure);

        void setBorderSize(Measure measure);

        void setBorderColor(Color loadColor);
    }

    private interface TextFactory {

        void setTextColor(Color loadColor);

        void setGravity(Gravity topLeft);

        void setFont(Font loadFont);

        void setText(String loadString);

    }

    private static class ViewFactory implements LayoutFactory {

        private final View view;

        public ViewFactory(View view) {
            this.view = view;
        }

        @Override
        public void setId(String id) {
            view.setId(id);
        }

        @Override
        public void setBackground(Drawable drawable) {
            view.getBorderParams().setBackground(drawable);
        }

        @Override
        public void setCornerRightBottomStyle(CornerStyle style) {
            view.getBorderParams().setCornerRightBottomStyle(style);
        }

        @Override
        public void setCornerLeftBottomStyle(CornerStyle style) {
            view.getBorderParams().setCornerLeftBottomStyle(style);
        }

        @Override
        public void setCornerRightTopStyle(CornerStyle style) {
            view.getBorderParams().setCornerRightTopStyle(style);
        }

        @Override
        public void setCornerLeftTopStyle(CornerStyle style) {
            view.getBorderParams().setCornerLeftTopStyle(style);
        }

        @Override
        public void setCornerRightBottomSize(Measure measure) {
            view.getBorderParams().setCornerRightBottomSize(measure);
        }

        @Override
        public void setCornerLeftBottomSize(Measure measure) {
            view.getBorderParams().setCornerLeftBottomSize(measure);
        }

        @Override
        public void setCornerRightTopSize(Measure measure) {
            view.getBorderParams().setCornerRightTopSize(measure);
        }

        @Override
        public void setCornerLeftTopSize(Measure measure) {
            view.getBorderParams().setCornerLeftTopSize(measure);
        }

        @Override
        public void setBorderSize(Measure measure) {
            view.getBorderParams().setBorderSize(measure);
        }

        @Override
        public void setBorderColor(Color color) {
            view.getBorderParams().setBorderColor(color);
        }

        @Override
        public void setLayoutGravityY(LayoutGravity align) {
            view.getLayoutParams().setLayoutGravityY(align);
        }

        @Override
        public void setLayoutGravityX(LayoutGravity align) {
            view.getLayoutParams().setLayoutGravityX(align);
        }

        @Override
        public void setMarginRightMeasure(Measure measure) {
            view.getLayoutParams().setMarginRightMeasure(measure);
        }

        @Override
        public void setMarginLeftMeasure(Measure measure) {
            view.getLayoutParams().setMarginLeftMeasure(measure);
        }

        @Override
        public void setMarginBottomMeasure(Measure measure) {
            view.getLayoutParams().setMarginBottomMeasure(measure);
        }

        @Override
        public void setMarginTopMeasure(Measure measure) {
            view.getLayoutParams().setMarginTopMeasure(measure);
        }

        @Override
        public void setPaddingRightMeasure(Measure measure) {
            view.getLayoutParams().setPaddingRightMeasure(measure);
        }

        @Override
        public void setPaddingLeftMeasure(Measure measure) {
            view.getLayoutParams().setPaddingLeftMeasure(measure);
        }

        @Override
        public void setPaddingBottomMeasure(Measure measure) {
            view.getLayoutParams().setPaddingBottomMeasure(measure);
        }

        @Override
        public void setPaddingTopMeasure(Measure measure) {
            view.getLayoutParams().setPaddingTopMeasure(measure);
        }

        @Override
        public void setHeightMeasure(Measure measure) {
            view.getLayoutParams().setHeightMeasure(measure);
        }

        @Override
        public void setLayoutHeightMeasure(LayoutMeasure measure) {
            view.getLayoutParams().setLayoutHeightMeasure(measure);
        }

        @Override
        public void setWidthMeasure(Measure measure) {
            view.getLayoutParams().setWidthMeasure(measure);
        }

        @Override
        public void setLayoutWidthMeasure(LayoutMeasure measure) {
            view.getLayoutParams().setLayoutWidthMeasure(measure);
        }

        @Override
        public void setHelp(String help) {
            view.setHelp(help);
        }

    }

    private static class TextViewFactory extends ViewFactory implements TextFactory {

        private final TextView textView;

        public TextViewFactory(TextView textView) {
            super(textView);
            this.textView = textView;
        }

        @Override
        public void setTextColor(Color color) {
            textView.setTextColor(color);
        }

        @Override
        public void setFont(Font font) {
            textView.setFont(font);
        }

        @Override
        public void setText(String text) {
            textView.setText(text);
        }

        @Override
        public void setGravity(Gravity gravity) {
            textView.setGravity(gravity);
        }

    }

    private static class StyleFactory implements LayoutFactory, TextFactory {

        private final Style style;

        public StyleFactory(Style style) {
            this.style = style;
        }

        @Override
        public void setId(String attrValue) {
            // Style cannot define id
        }

        @Override
        public void setBackground(Drawable drawable) {
            style.setBackground(drawable);
        }

        @Override
        public void setCornerRightBottomStyle(CornerStyle style) {
            this.style.setCornerRightBottomStyle(style);
        }

        @Override
        public void setCornerLeftBottomStyle(CornerStyle style) {
            this.style.setCornerLeftBottomStyle(style);
        }

        @Override
        public void setCornerRightTopStyle(CornerStyle style) {
            this.style.setCornerRightTopStyle(style);
        }

        @Override
        public void setCornerLeftTopStyle(CornerStyle style) {
            this.style.setCornerLeftTopStyle(style);
        }

        @Override
        public void setCornerRightBottomSize(Measure measure) {
            style.setCornerRightBottomSize(measure);
        }

        @Override
        public void setCornerLeftBottomSize(Measure measure) {
            style.setCornerLeftBottomSize(measure);
        }

        @Override
        public void setCornerRightTopSize(Measure measure) {
            style.setCornerRightTopSize(measure);
        }

        @Override
        public void setCornerLeftTopSize(Measure measure) {
            style.setCornerLeftTopSize(measure);
        }

        @Override
        public void setBorderSize(Measure measure) {
            style.setBorderSize(measure);
        }

        @Override
        public void setBorderColor(Color color) {
            style.setBorderColor(color);
        }

        @Override
        public void setTextColor(Color color) {
            style.setTextColor(color);
        }

        @Override
        public void setFont(Font font) {
            style.setFont(font);
        }

        @Override
        public void setText(String text) {
            // Style cannot define text
        }

        @Override
        public void setLayoutGravityY(LayoutGravity align) {
            style.setLayoutGravityY(align);
        }

        @Override
        public void setLayoutGravityX(LayoutGravity align) {
            style.setLayoutGravityX(align);
        }

        @Override
        public void setMarginRightMeasure(Measure measure) {
            style.setMarginRightMeasure(measure);
        }

        @Override
        public void setMarginLeftMeasure(Measure measure) {
            style.setMarginLeftMeasure(measure);
        }

        @Override
        public void setMarginBottomMeasure(Measure measure) {
            style.setMarginBottomMeasure(measure);
        }

        @Override
        public void setMarginTopMeasure(Measure measure) {
            style.setMarginTopMeasure(measure);
        }

        @Override
        public void setPaddingRightMeasure(Measure measure) {
            style.setPaddingRightMeasure(measure);
        }

        @Override
        public void setPaddingLeftMeasure(Measure measure) {
            style.setPaddingLeftMeasure(measure);
        }

        @Override
        public void setPaddingBottomMeasure(Measure measure) {
            style.setPaddingBottomMeasure(measure);
        }

        @Override
        public void setPaddingTopMeasure(Measure measure) {
            style.setPaddingTopMeasure(measure);
        }

        @Override
        public void setHeightMeasure(Measure measure) {
            style.setHeightMeasure(measure);
        }

        @Override
        public void setLayoutHeightMeasure(LayoutMeasure layoutMeasure) {
            style.setLayoutHeightMeasure(layoutMeasure);
        }

        @Override
        public void setWidthMeasure(Measure measure) {
            style.setWidthMeasure(measure);
        }

        @Override
        public void setLayoutWidthMeasure(LayoutMeasure layoutMeasure) {
            style.setLayoutWidthMeasure(layoutMeasure);
        }

        @Override
        public void setGravity(Gravity gravity) {
            style.setGravity(gravity);
        }

        @Override
        public void setHelp(String help) {
            // Style cannot define help
        }

    }

    public void clearCache() {
        fileCache.clear();
        drawableCache.clear();
    }
}
