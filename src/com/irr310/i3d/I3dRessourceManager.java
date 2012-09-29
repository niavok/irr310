package com.irr310.i3d;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.fonts.FontFactory;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.LayoutParams.LayoutAlign;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.LinearLayout.LayoutOrientation;
import com.irr310.i3d.view.Rect;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.Triangle;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.ViewParent;
import com.irr310.i3d.view.Waiter;

public class I3dRessourceManager {

    private static I3dRessourceManager instance = new I3dRessourceManager();
    private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private final Map<String, RessourceFileCache> fileCache = new HashMap<String, RessourceFileCache>();

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
        view = ressourceFileCache.getView(localId);

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
                ParseResources(root, ressourceFileCache);
            }

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

        fileCache.put(fileId, ressourceFileCache);
        fontFactory.free();
    }

    private View ParseView(Element element, RessourceFileCache ressourceFileCache) {
        String nodeName = element.getNodeName();
        // Log.trace("nodeName=" + nodeName);

        View view = null;
        if (nodeName.equals("View")) {
            view = LinkView(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("RelativeLayout")) {
            view = NewRelativeLayout(element);
        } else if (nodeName.equals("LinearLayout")) {
            view = NewLinearLayout(element);
        } else if (nodeName.equals("Rect")) {
            view = NewRect(element);
        } else if (nodeName.equals("Button")) {
            view = NewButton(element);
        } else if (nodeName.equals("Triangle")) {
            view = NewTriangle(element, ressourceFileCache.getFileId());
        } else if (nodeName.equals("Waiter")) {
            view = NewWaiter(element);
        } else if (nodeName.equals("TextView")) {
            view = NewTextView(element);
        } else {
            // TODO error
            Log.trace("ERROR unknown nodeName=" + nodeName);
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

    private View LinkView(Element element, String fileId) {
        String ref = element.getAttribute("i3d:ref");
        View view = loadView(ref).duplicate();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkViewAttrs(attrName, attrValue, view)) {
            } else if (attrName.equals("i3d:ref")) {
                // Already processed
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for View");
            }
        }

        return view;
    }

    private void ParseResources(Element element, RessourceFileCache ressourceFileCache) {
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
            } else {
                Log.error("Unknonw resources: " + subElement.getNodeName());
            }
        }
    }

    private Waiter NewWaiter(Element element) {
        return new Waiter(g);
    }

    private Rect NewRect(Element element) {
        Rect rect = new Rect(g);
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("i3d:id")) {
                rect.setId(attrValue);
            } else if (attrName.equals("i3d:backgroundColor")) {
                rect.setBackgroundColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:layout_width")) {

            } else if (attrName.equals("i3d:layout_height")) {

            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for Rect");
            }
        }

        return rect;
    }

    private Triangle NewTriangle(Element element, String fileId) {
        Triangle triangle = new Triangle(g);
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("i3d:id")) {
                triangle.setId(attrValue + "@" + fileId);
            } else if (attrName.equals("i3d:backgroundColor")) {
                triangle.setBackgroundColor(loadColor(attrValue));
            } else if (attrName.equals("i3d:layout_width")) {
                if (attrValue.equals("match_parent")) {
                    triangle.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
                } else {
                    Log.error("Unsupported value '" + attrValue + "' for i3d:layout_width attribute");
                }
            } else if (attrName.equals("i3d:layout_height")) {
                if (attrValue.equals("match_parent")) {
                    triangle.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
                } else {
                    Log.error("Unsupported value '" + attrValue + "' for i3d:layout_height attribute");
                }
            } else if (attrName.equals("i3d:align_x")) {
            } else if (attrName.equals("i3d:points")) {
                String[] pointsStrings = attrValue.split("\\|");
                if (pointsStrings.length != 3) {
                    Log.error("A triangle must have 3 points and not " + pointsStrings.length + "(" + attrValue + ")");
                    break;
                }
                int index = 0;
                MeasurePoint[] points = new MeasurePoint[3];
                for (String pointString : pointsStrings) {
                    String[] mesuresString = pointString.split(",");
                    if (mesuresString.length != 2) {
                        Log.error("A point must have 2 points and not " + mesuresString.length + "(" + pointString + ")");
                        break;
                    }

                    MeasurePoint point = new MeasurePoint();
                    point.setX(parseMeasure(mesuresString[0]));
                    point.setY(parseMeasure(mesuresString[1]));

                    points[index++] = point;
                }

                triangle.setPoints(points);

            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for Triangle");
                break;
            }
        }

        return triangle;
    }
  
    private Measure parseMeasure(String mesureString) {
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

        int value = Integer.parseInt(stringValue);

        return new Measure(value, relative);
    }

    private RelativeLayout NewRelativeLayout(Element element) {
        RelativeLayout relativeLayout = new RelativeLayout(g);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkViewAttrs(attrName, attrValue, relativeLayout)) {
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for RelativeLayout");
            }
        }

        return relativeLayout;
    }

    private LinearLayout NewLinearLayout(Element element) {
        LinearLayout linearLayout = new LinearLayout(g);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkViewAttrs(attrName, attrValue, linearLayout)) {
            } else if (checkOrientation(attrName, attrValue, linearLayout)) {
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for LinearLayout");
            }
        }

        return linearLayout;
    }

    private TextView NewTextView(Element element) {
        TextView textView = new TextView(g);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkViewAttrs(attrName, attrValue, textView)) {
            } else if (checkColor(attrName, attrValue, textView)) {
            } else if (checkText(attrName, attrValue, textView)) {
            } else if (checkFont(attrName, attrValue, textView)) {
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for TextView");
            }
        }

        return textView;
    }

    private Button NewButton(Element element) {
        Button button = new Button(g);

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkViewAttrs(attrName, attrValue, button)) {
            } else if (checkColor(attrName, attrValue, button)) {
            } else if (checkText(attrName, attrValue, button)) {
            } else if (checkFont(attrName, attrValue, button)) {
            } else {
                Log.error("Unknown attrib '" + attrName + "=" + attrValue + "' for Button");
            }
        }

        return button;
    }

    private boolean checkViewAttrs(String attrName, String attrValue, View view) {
        boolean used = true;
        if (checkId(attrName, attrValue, view)) {
        } else if (checkLayoutWidth(attrName, attrValue, view)) {
        } else if (checkLayoutHeight(attrName, attrValue, view)) {
        } else if (checkGravityX(attrName, attrValue, view)) {
        } else if (checkGravityY(attrName, attrValue, view)) {
        } else if (checkLayoutMarginTop(attrName, attrValue, view)) {
        } else if (checkLayoutMarginBottom(attrName, attrValue, view)) {
        } else if (checkLayoutMarginLeft(attrName, attrValue, view)) {
        } else if (checkLayoutMarginRight(attrName, attrValue, view)) {
        } else {
            used = false;
        }
        return used;
    }

    private boolean checkId(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:id")) {
            view.setId(attrValue);
            used = true;
        }
        return used;
    }

    private boolean checkColor(String attrName, String attrValue, TextView view) {
        boolean used = false;
        if (attrName.equals("i3d:color")) {
            view.setTextColor(loadColor(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkText(String attrName, String attrValue, TextView view) {
        boolean used = false;
        if (attrName.equals("i3d:text")) {
            view.setText(loadString(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkFont(String attrName, String attrValue, TextView view) {
        boolean used = false;
        if (attrName.equals("i3d:font")) {
            view.setFont(loadFont(attrValue));
            used = true;
        }
        return used;
    }

    private boolean checkLayoutWidth(String attrName, String attrValue, View view) {
        boolean used = false;

        if (attrName.equals("i3d:layout_width")) {
            Measure measure = null;
            if (attrValue.equals("match_parent")) {
                view.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
                used = true;
            } else if (attrValue.equals("wrap_content")) {
                view.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.WRAP_CONTENT);
                used = true;
            } else if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
                view.getLayoutParams().setWidthMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_width attribute");
            }
        }
        return used;
    }

    private boolean checkLayoutHeight(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_height")) {
            Measure measure = null;
            if (attrValue.equals("match_parent")) {
                view.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
                used = true;
            } else if (attrValue.equals("wrap_content")) {
                view.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.WRAP_CONTENT);
                used = true;
            } else if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setLayoutHeightMeasure(LayoutMeasure.FIXED);
                view.getLayoutParams().setHeightMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_height attribute");
            }
        }
        return used;
    }
    
    private boolean checkLayoutMarginTop(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginTop")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setMarginTopMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_marginTop attribute");
            }
        }
        return used;
    }
    
    private boolean checkLayoutMarginBottom(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginBottom")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setMarginBottomMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_marginBottom attribute");
            }
        }
        return used;
    }
    
    private boolean checkLayoutMarginLeft(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginLeft")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setMarginLeftMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_marginLeft attribute");
            }
        }
        return used;
    }
    
    private boolean checkLayoutMarginRight(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_marginRight")) {
            Measure measure = null;
            if ((measure = parseMeasure(attrValue)) != null) {
                view.getLayoutParams().setMarginRightMeasure(measure);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_marginRight attribute");
            }
        }
        return used;
    }

    private boolean checkGravityX(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_gravity_x")) {
            if (attrValue.equals("center")) {
                view.getLayoutParams().setLayoutAlignX(LayoutAlign.CENTER);
                used = true;
            } else if (attrValue.equals("left")) {
                view.getLayoutParams().setLayoutAlignY(LayoutAlign.LEFT);
                used = true;
            } else if (attrValue.equals("right")) {
                view.getLayoutParams().setLayoutAlignY(LayoutAlign.RIGHT);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_gravity_x attribute");
            }
        }
        return used;
    }

    private boolean checkGravityY(String attrName, String attrValue, View view) {
        boolean used = false;
        if (attrName.equals("i3d:layout_gravity_y")) {
            if (attrValue.equals("center")) {
                view.getLayoutParams().setLayoutAlignY(LayoutAlign.CENTER);
                used = true;
            } else if (attrValue.equals("top")) {
                view.getLayoutParams().setLayoutAlignY(LayoutAlign.TOP);
                used = true;
            } else if (attrValue.equals("bottom")) {
                view.getLayoutParams().setLayoutAlignY(LayoutAlign.BOTTOM);
                used = true;
            } else {
                Log.error("Unsupported value '" + attrValue + "' for i3d:layout_gravity_y attribute");
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
                Log.error("Unsupported value '" + attrValue + "' for i3d:orientation attribute");
            }
        }
        return used;
    }

    private Color loadColor(String colorId) {

        String[] colorParts = colorId.split("@");
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
            Log.error("Unknown color '" + colorId);
        }

        return color;
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
            Log.error("Unknown string '" + stringId);
        }

        return string;
    }

    Font loadFont(String fontId) {
        String[] fontParts = fontId.split("@");
        if (fontParts.length != 2) {
            Log.error("Fail to load font '" + fontId + "' : no @ found");
            return null;
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
            Log.error("Unknown font '" + fontId);
        }

        return font;
    }
}
