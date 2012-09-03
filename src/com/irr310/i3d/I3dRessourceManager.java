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
import com.irr310.i3d.view.AbsoluteLayout;
import com.irr310.i3d.view.Layer;
import com.irr310.i3d.view.Rect;
import com.irr310.i3d.view.Triangle;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.ViewParent;
import com.irr310.i3d.view.Waiter;

public class I3dRessourceManager {

    private static I3dRessourceManager instance = new I3dRessourceManager();
    private final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

    private final Map<String, RessourceFileCache> fileCache = new HashMap<String, RessourceFileCache>();
    
    Graphics g;

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
        
        if(fileCache.containsKey(fileId)) {
            
        } else {
            parseFile(fileId);
        }
        
        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        view = ressourceFileCache.getView(localId);
        
        if(view == null) {
            Log.error("Unknown view '" + viewId);
        }
        
        return view;
    }

    private void parseFile(String fileId) {
        RessourceFileCache ressourceFileCache = new RessourceFileCache();
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = docBuilder.parse(new File("res/" + fileId + ".xml"));

            Element root = doc.getDocumentElement();
            
            
            if(root.getNodeName().contains("Layout")) {
                ParseView(root, ressourceFileCache);
            } else if(root.getNodeName().equals("resources")) {
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
        
    }

    private View ParseView(Element element, RessourceFileCache ressourceFileCache) {
        String nodeName = element.getNodeName();
        Log.trace("nodeName=" + nodeName);

        View view = null;
        if (nodeName.equals("AbsoluteLayout")) {
            view = NewAbsoluteLayout(element);
        } else if (nodeName.equals("Layer")) {
            view = NewLayer(element);
        } else if (nodeName.equals("Rect")) {
            view = NewRect(element);
        } else if (nodeName.equals("Triangle")) {
            view = NewTriangle(element);
        } else if (nodeName.equals("Waiter")) {
            view = NewWaiter(element);
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

        if(view.getId() != null) {
            ressourceFileCache.addWidget(view.getId(), view);
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
            if(subElement.getNodeName().equals("color")) {
                String colorName = subElement.getAttribute("name");
                Color color = new Color(subElement.getTextContent());
                ressourceFileCache.addColor(colorName, color);
            } else {
                Log.error("Unknonw resources: "+subElement.getNodeName());
            }
        }
    }
    
    private Waiter NewWaiter(Element element) {
        return new Waiter(g);
    }

    private Rect NewRect(Element element) {
        Rect rect = new Rect(g);
        NamedNodeMap attributes = element.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue= attr.getValue();
            
            if(attrName.equals("i3d:id")) {
                rect.setId(attrValue);
            } else if(attrName.equals("i3d:backgroundColor")) {
                rect.setBackgroundColor(loadColor(attrValue));
            } else if(attrName.equals("i3d:layout_width")) {
                
            } else if(attrName.equals("i3d:layout_height")) {
                
            } else {
                Log.error("Unknown attrib '" + attrName+"="+attrValue+"' for Rect");
            }
        }
        
        return rect;
    }
    
    private Triangle NewTriangle(Element element) {
        Triangle triangle = new Triangle(g);
        NamedNodeMap attributes = element.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue= attr.getValue();
            
            if(attrName.equals("i3d:id")) {
                triangle.setId(attrValue);
            } else if(attrName.equals("i3d:backgroundColor")) {
                triangle.setBackgroundColor(loadColor(attrValue));
            } else if(attrName.equals("i3d:points")) {
                String[] pointsStrings = attrValue.split("\\|");
                if(pointsStrings.length != 3) {
                    Log.error("A triangle must have 3 points and not "+pointsStrings.length+ "("+attrValue+")");
                    break;
                }
                int index = 0;
                I3dMesure[] points = new I3dMesure[6]; 
                for(String pointString: pointsStrings) {
                    String[] mesuresString = pointString.split(",");
                    if(mesuresString.length != 2) {
                        Log.error("A point must have 2 points and not "+mesuresString.length+ "("+pointString+")");
                        break;
                    }
                    
                    points[index++] =  parseMesure(mesuresString[0],mesuresString[1]);
                }
                
                triangle.setPoints(points);
                
            } else {
                Log.error("Unknown attrib '" + attrName+"="+attrValue+"' for Rect");
                break;
            }
        }
        
        return triangle;
    }

    private I3dMesure parseMesure(String mesureStringX, String mesureStringY) {
        boolean relativeX = false;
        boolean relativeY = false;
        String stringValueX = "0";
        String stringValueY = "0";
        if(mesureStringX.endsWith("px")) {
            relativeX = false;
            stringValueX = mesureStringX.substring(0, mesureStringX.length()-2);
        } else if(mesureStringX.endsWith("%")) {
            relativeX = false;
            stringValueX = mesureStringX.substring(0, mesureStringX.length()-1);
        } else {
            Log.error("Unknown unit for mesure '"+mesureStringX+"'");
        }
        
        if(mesureStringY.endsWith("px")) {
            relativeY = false;
            stringValueY = mesureStringY.substring(0, mesureStringY.length()-2);
        } else if(mesureStringY.endsWith("%")) {
            relativeY = false;
            stringValueY = mesureStringY.substring(0, mesureStringY.length()-1);
        } else {
            Log.error("Unknown unit for mesure '"+mesureStringY+"'");
        }
        
        int valueX = Integer.parseInt(stringValueX);
        int valueY = Integer.parseInt(stringValueY);
                
        return new I3dMesure(valueX, valueY, relativeX, relativeY);
    }


    private Layer NewLayer(Element element) {
        return new Layer(g);
    }

    private AbsoluteLayout NewAbsoluteLayout(Element element) {
        AbsoluteLayout absoluteLayout = new AbsoluteLayout(g);

        NamedNodeMap attributes = element.getAttributes();
        for(int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue= attr.getValue();
            
            if(attrName.equals("i3d:id")) {
                absoluteLayout.setId(attrValue);
            } else if(attrName.equals("i3d:layout_width")) {
                //TODO
            } else if(attrName.equals("i3d:layout_height")) {
                //TODO
            } else if(attrName.equals("i3d:i3d:align_x")) {
                //TODO                
            } else if(attrName.equals("i3d:i3d:align_y")) {
                //TODO
            } else {
                Log.error("Unknown attrib '" + attrName+"="+attrValue+"' for AbsoluteLayout");
            }
        }
        
        return absoluteLayout;
    }

    
    private Color loadColor(String colorId) {
        
        String[] colorParts = colorId.split("@");
        String localId = colorParts[0];
        String fileId = colorParts[1];

        Color color = null;

        if(fileCache.containsKey(fileId)) {
            
        } else {
            parseFile(fileId);
        }
        
        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
        color = ressourceFileCache.getColor(localId);

        if(color == null) {
            Log.error("Unknown color '" + colorId);
        }

        return color;
    }
    
}
