package com.irr310.server.world.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.irr310.i3d.RessourceLoadingException;

public class ProductManager {

    List<Product> products = new ArrayList<Product>();
    Map<String, Product> productIds = new HashMap<String, Product>();
    
    private DocumentBuilderFactory docBuilderFactory;
    
    public void init() {

        products.clear();
        productIds.clear();
        
        File dirFile = new File("assets/products/");
        if (!dirFile.isDirectory()) {
            Log.error("Fail to locate products directory at '" + dirFile.getAbsolutePath() + "i");
        }

        docBuilderFactory = DocumentBuilderFactory.newInstance();
        
        File[] listFiles = dirFile.listFiles();
        String extension = ".xml";
        for (File file : listFiles) {
            String fileName = file.getName();
            if (fileName.endsWith(extension)) {
                loadProductFile(file);
            }
        }
        // free memory
        docBuilderFactory = null;
        
        performLinks();
    }

   
    private void loadProductFile(File file) {
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = docBuilder.parse(file);

            Element root = doc.getDocumentElement();

            if (!root.getNodeName().equals("products")) {
                Log.warn("The file '"+file.getAbsolutePath()+"' is not a product description file");
                return;
            }
            
            Product product = null;
            
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // TODO error
                    continue;
                }
                Element subElement = (Element) node;
                if (subElement.getNodeName().equals("component")) {
                    product = parseComponentProduct(subElement);
                } else if (subElement.getNodeName().equals("ship")) {
                    product = parseShipProduct(subElement);
                } else {
                    Log.error("Unknonw product: " + subElement.getNodeName());
                }
                
                if(product != null) {
                    if(!productIds.containsKey(product.getId())) {
                        products.add(product);
                        productIds.put(product.getId(), product);
                    } else {
                        Log.warn("Product with id '"+product.getId()+"' already exist. Drop this one.");
                    }
                }
                product = null;
            }
            
            

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            Log.error("Failed to parse file '" + file.getAbsolutePath(), e);
        } catch (IOException e) {
            Log.error("Failed to load file '" + file.getAbsolutePath(), e);
        }

    }

    
    private ComponentProduct parseComponentProduct(Element element) {
        ComponentProduct product = new ComponentProduct();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkProductCommonAttrs(attrName, attrValue, product)) {
            } else if (checkComponentAttrs(attrName, attrValue, product)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentProduct");
            }
        }
        
        return product;
    }
    
    private ShipProduct parseShipProduct(Element element) {
        ShipProduct ship = new ShipProduct();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkProductCommonAttrs(attrName, attrValue, ship)) {
            } else if (checkShipAttrs(attrName, attrValue, ship)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ShipProduct");
            }
        }
        
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("components")) {
                parseShipProductComponents(subElement, ship);
            } else if (subElement.getNodeName().equals("links")) {
                //parseShipProductLinks(subElement, ship);
                Log.warn("TODO : parse links");
            } else {
                Log.error("Unknown tag for tag ship: " + subElement.getNodeName());
            }
        }
        
        
        
        return ship;
    }

    private void parseShipProductComponents(Element element, ShipProduct ship) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("component")) {
                parseShipProductComponent(subElement, ship);
            } else {
                Log.error("Unknown tag for tag ship/components: " + subElement.getNodeName());
            }
        }        
    }


    private void parseShipProductComponent(Element element, ShipProduct ship) {

        String key = element.getAttribute("key");
        String ref = element.getAttribute("ref");
        ship.addComponent(key, ref);
    }


    private boolean checkProductCommonAttrs(String attrName, String attrValue, Product product) {
        boolean used = true;
        if (checkProductId(attrName, attrValue, product)) {
        } else if (checkProductName(attrName, attrValue, product)) {
        } else if (checkProductCode(attrName, attrValue, product)) {
        } else if (checkProductDescription(attrName, attrValue, product)) {
        } else {
            used = false;
        }
        return used;
    }

    private boolean checkProductId(String attrName, String attrValue, Product product) {
        boolean used = false;
        if (attrName.equals("id")) {
            product.setId(attrValue);
            used = true;
        }
        return used;
    }
    
    private boolean checkProductCode(String attrName, String attrValue, Product product) {
        boolean used = false;
        if (attrName.equals("code")) {
            product.setCode(attrValue);
            used = true;
        }
        return used;
    }
    
    private boolean checkProductName(String attrName, String attrValue, Product product) {
        boolean used = false;
        if (attrName.equals("name")) {
            product.setName(loadString(attrValue));
            used = true;
        }
        return used;
    }
    
    private boolean checkProductDescription(String attrName, String attrValue, Product product) {
        boolean used = false;
        if (attrName.equals("description")) {
            product.setDescription(loadString(attrValue));
            used = true;
        }
        return used;
    }
    
    private boolean checkComponentAttrs(String attrName, String attrValue, ComponentProduct product) {
        boolean used = true;
//        if (checkProductId(attrName, attrValue, product)) {
//        } else if (checkProductName(attrName, attrValue, product)) {
            used = false;
//        }
        return used;
    }
    
    private boolean checkShipAttrs(String attrName, String attrValue, ShipProduct product) {
        boolean used = true;
        if (checkKernelKey(attrName, attrValue, product)) {
        } else {
            used = false;
        }
        return used;
    }
    
    private boolean checkKernelKey(String attrName, String attrValue, ShipProduct product) {
        boolean used = false;
        if (attrName.equals("kernel")) {
            product.setKernelKey(attrValue);
            used = true;
        }
        return used;
    }


    private String loadString(String stringId) {
//TODO Translation
//        String[] stringParts = stringId.split("@");
//        String localId = stringParts[0];
//        String fileId = stringParts[1];
//
//        String string = null;
//
//        if (fileCache.containsKey(fileId)) {
//
//        } else {
//            parseFile(fileId);
//        }
//
//        RessourceFileCache ressourceFileCache = fileCache.get(fileId);
//        string = ressourceFileCache.getString(localId);
//
//        if (string == null) {
//            Log.warn("Unknown string '" + stringId);
//            string = stringId;
//        }
//
//        return string;
        
        return stringId;
    }

    private void performLinks() {
        List<Product> validProducts = new ArrayList<Product>();
        
        for(Product product:products) {
            if(product.performLinks(productIds)) {
                validProducts.add(product);
            } else {
                Log.warn("Invalid product '"+product.getId()+"'. Drop it !");
            }
        }
        
        products = validProducts;
    }

}
