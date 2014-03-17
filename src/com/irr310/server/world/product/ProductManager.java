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
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec3;
import com.irr310.server.world.product.ComponentProduct.ComponentElectricStorageCapacityProduct;
import com.irr310.server.world.product.ComponentProduct.ComponentKernelCapacityProduct;
import com.irr310.server.world.product.ComponentProduct.ComponentLinearEngineCapacityProduct;
import com.irr310.server.world.product.ComponentProduct.ComponentPartProduct;
import com.irr310.server.world.product.ComponentProduct.PartShapeProduct;
import com.irr310.server.world.product.ComponentProduct.PartShapeProduct.ShapeType;

public class ProductManager {

    List<Product> products = new ArrayList<Product>();
    Map<String, Product> productIds = new HashMap<String, Product>();
    
    private DocumentBuilderFactory docBuilderFactory;

    public static final int DEBUG_COEF = 10000; 
    
    
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
    
    public List<Product> getProducts() {
        return products;
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
        ComponentProduct component = new ComponentProduct();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (checkProductCommonAttrs(attrName, attrValue, component)) {
            } else if (checkComponentAttrs(attrName, attrValue, component)) {
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentProduct");
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
            if (subElement.getNodeName().equals("parts")) {
                parseComponentProductParts(subElement, component);
            } else if (subElement.getNodeName().equals("specifications")) {
                Log.warn("TODO : parse specifications");
            } else if (subElement.getNodeName().equals("production")) {
                parseComponentProductProduction(subElement, component);
            } else if (subElement.getNodeName().equals("capacities")) {
                parseComponentProductCapacities(subElement, component);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag component for component '"+component.getId()+"'");
            }
        }
        
        return component;
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
                parseShipProductLinks(subElement, ship);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag ship for ship '"+ship.getId()+"'");
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
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag ship/components for ship '"+ship.getId()+"'");
            }
        }        
    }

    private void parseShipProductComponent(Element element, ShipProduct ship) {

        String key = element.getAttribute("key");
        String ref = element.getAttribute("ref");
        String locationString = element.getAttribute("location");
        String rotationString = element.getAttribute("rotation");
        if(locationString == null || locationString.isEmpty()) {
            throw new RessourceLoadingException("No valid location for component '"+key+"'for ship '"+ship.getId()+"'");
        }
        if(rotationString == null || rotationString.isEmpty()) {
            rotationString = "vec3(0,0,0)";
        }
        Vec3 location = parseVec3(locationString);
        Vec3 rotation = parseVec3(rotationString);
        ship.addComponent(key, ref, location, rotation);
    }
    
    private void parseShipProductLinks(Element element, ShipProduct ship) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("link")) {
                parseShipProductLink(subElement, ship);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag ship/components for ship '"+ship.getId()+"'");
            }
        }        
    }
    
    private void parseComponentProductParts(Element element, ComponentProduct component) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {           
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("part")) {
                parseComponentProductPart(subElement, component);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag component/parts for component '"+component.getId()+"'");
            }
        }        
    }
    
    private void parseComponentProductCapacities(Element element, ComponentProduct component) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {           
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("linearEngineCapacity")) {
                parseComponentProductLinearEngineCapacity(subElement, component);
            } else if (subElement.getNodeName().equals("kernelCapacity")) {
                    parseComponentProductKernelCapacity(subElement, component);
            } else if (subElement.getNodeName().equals("electricStorageCapacity")) {
                parseComponentProductElectricStorageCapacity(subElement, component);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag component/parts for component '"+component.getId()+"'");
            }
        }        
    }

    private void parseComponentProductLinearEngineCapacity(Element element, ComponentProduct component) {
        ComponentLinearEngineCapacityProduct capacity = new ComponentLinearEngineCapacityProduct(component);
        
        NamedNodeMap attributes = element.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++) {
            Node item = attributes.item(j);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("airFriction")) {
                capacity.setAirFriction(Double.parseDouble(attrValue));
            } else if (attrName.equals("theoricalMaxThrust")) {
                capacity.setTheoricalMaxThrust(Double.parseDouble(attrValue));
            } else if (attrName.equals("theoricalMinThrust")) {
                capacity.setTheoricalMinThrust(Double.parseDouble(attrValue));
            } else if (attrName.equals("theoricalVariationSpeed")) {
                capacity.setTheoricalVariationSpeed(Double.parseDouble(attrValue));
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentLinearEngineCapacityProduct");
            }
        }
        
        component.addCapacity(capacity);
    }
    
    private void parseComponentProductElectricStorageCapacity(Element element, ComponentProduct component) {
        ComponentElectricStorageCapacityProduct capacity = new ComponentElectricStorageCapacityProduct(component);
        
        NamedNodeMap attributes = element.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++) {
            Node item = attributes.item(j);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("capacity")) {
                capacity.setCapacity(Double.parseDouble(attrValue));
            } else if (attrName.equals("yield")) {
                capacity.setYield(Double.parseDouble(attrValue));
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentElectricStorageCapacityProduct");
            }
        }
        
        component.addCapacity(capacity);
    }
    
    private void parseComponentProductKernelCapacity(Element element, ComponentProduct component) {
        ComponentKernelCapacityProduct capacity = new ComponentKernelCapacityProduct(component);
        
        NamedNodeMap attributes = element.getAttributes();
        for (int j = 0; j < attributes.getLength(); j++) {
            Node item = attributes.item(j);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("electricConsumption")) {
                capacity.setElectricConsumption(Double.parseDouble(attrValue));
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentElectricStorageCapacityProduct");
            }
        }
        
        component.addCapacity(capacity);
    }
    
    private void parseComponentProductProduction(Element element, ComponentProduct component) {
        
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("factoryCost")) {
                component.setFactoryCost(Long.parseLong(attrValue)/ DEBUG_COEF);
            } else if (attrName.equals("oreCost")) {
                component.setOreCost(Long.parseLong(attrValue) / DEBUG_COEF);
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentPartProduct");
            }
        }
    }
    
    private void parseComponentProductPart(Element element, ComponentProduct component) {
        ComponentPartProduct part = new ComponentPartProduct(component);
        
        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            Attr attr = (Attr) item;
            String attrName = attr.getName();
            String attrValue = attr.getValue();

            if (attrName.equals("mass")) {
                part.setMass(Double.parseDouble(attrValue));
            } else if (attrName.equals("shape")) {
                part.setShape(parseShape(attrValue));
            } else {
                throw new RessourceLoadingException("Unknown attrib '" + attrName + "=" + attrValue + "' for ComponentPartProduct");
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
            if (subElement.getNodeName().equals("slots")) {
                parseComponentProductPartSlots(subElement, part);
            }else if (subElement.getNodeName().equals("slot")) {
                parseComponentProductPartSlot(subElement, part);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag component/parts/part for component '"+component.getId()+"'");
            }
        }
        component.addPart(part);
    }

    private void parseComponentProductPartSlots(Element subElement, ComponentPartProduct part) {
        String disposition = subElement.getAttribute("disposition");
        
        if(disposition == null || disposition.isEmpty()) {
            throw new RessourceLoadingException("Fail to parse slots for components '" + part.getComponent().getId()  + "' : slots tags must have a valid disposition");
        } else if(disposition.equals("box")) {
            Vec3 shapeSize = part.getShape().getSize();
            part.addSlot("right", new Vec3(shapeSize.x / 2, 0, 0));
            part.addSlot("left",new Vec3(-shapeSize.x / 2, 0, 0));
            part.addSlot("front",new Vec3(0, shapeSize.y / 2, 0));
            part.addSlot("back",new Vec3(0, -shapeSize.y / 2, 0));
            part.addSlot("top",new Vec3(0, 0, shapeSize.z / 2));
            part.addSlot("bottom", new Vec3(0, 0, -shapeSize.z / 2));
        } else {
            throw new RessourceLoadingException("Fail to parse slots for component '" + part.getComponent().getId()  + "' : unknown disposition '"+disposition+"'");
        }
    }
    
    private void parseComponentProductPartSlot(Element subElement, ComponentPartProduct part) {
        String locationString= subElement.getAttribute("location");
        String key= subElement.getAttribute("key");
        
        if(locationString == null || locationString.isEmpty()) {
            throw new RessourceLoadingException("Fail to parse slot for component '" + part.getComponent().getId()  + "' : slot tags must have a valid position");
        } else if(key == null || key.isEmpty()) {
                throw new RessourceLoadingException("Fail to parse slot for component '" + part.getComponent().getId()  + "' : slot tags must have a valid key");
        } else {
            Vec3 location = parseVec3(locationString);
            part.addSlot(key, location);
        }
    }

    private Vec3 parseVec3(String vec3String) {
        if(!vec3String.matches("^vec3\\(.*\\)$")) {
            throw new RessourceLoadingException("Fail to parse vec3 '" + vec3String  + "'");
        }
        
        String vec3ValuesString = vec3String.substring(5, vec3String.length()-1);
        String[] vec3ValuesSplit = vec3ValuesString.split(",");
        
        if(vec3ValuesSplit.length != 3) {
            throw new RessourceLoadingException("Fail to parse vec3 '" + vec3String  + "' : the vector size must have 3 components");
        }
        
        try {
            double x = Double.parseDouble(vec3ValuesSplit[0]);
            double y = Double.parseDouble(vec3ValuesSplit[1]);
            double z = Double.parseDouble(vec3ValuesSplit[2]);
            
            return new Vec3(x, y, z);
        } catch(NumberFormatException e) {
            throw new RessourceLoadingException("Fail to parse vec3 '" + vec3String  + "' : double parsing failed", e);
        }
        
        
    }


    private PartShapeProduct parseShape(String shapeString) {
        if(!shapeString.matches("^box\\(.*\\)$")) {
            throw new RessourceLoadingException("Fail to parse shape '" + shapeString  + "'");
        }
        
        String boxSizeString = shapeString.substring(4, shapeString.length()-1);
        String[] boxSizeStringSplit = boxSizeString.split(",");
        
        if(boxSizeStringSplit.length != 3) {
            throw new RessourceLoadingException("Fail to parse shape '" + shapeString  + "' : the box size must have 3 components");
        }
        
        double sizeX = Double.parseDouble(boxSizeStringSplit[0]);
        double sizeY = Double.parseDouble(boxSizeStringSplit[1]);
        double sizeZ = Double.parseDouble(boxSizeStringSplit[2]);
        
        return new PartShapeProduct(ShapeType.BOX, new Vec3(sizeX, sizeY, sizeZ));
    }

    private void parseShipProductLink(Element element, ShipProduct ship) {

        String refA = element.getAttribute("a");
        String refB = element.getAttribute("b");
        ship.addLink(refA, refB);
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

    public Product getProductById(String id) {
        return productIds.get(id);
    }

}
