package com.irr310.server.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.irr310.client.ClientConfig;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionAvailableProductList;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.item.DeployableItem;
import com.irr310.common.world.item.Item;
import com.irr310.common.world.item.Item.ItemType;
import com.irr310.common.world.item.Item.State;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.server.world.product.Product;

public class GameSerializer {

    private Document mDocument;

    public void save(Game game) {
        
        // Create saves directory
        File savesCacheDir = new File(ClientConfig.getSaveDirectoryPath("/"));
        if(!savesCacheDir.exists()) {
            savesCacheDir.mkdirs();
        }
        
        String saveFilePath = ClientConfig.getSaveDirectoryPath("previous_game.irr310");
        
        File file;
        FileOutputStream fop = null;
        try {
        
        DocumentBuilderFactory documentBuilderFactory =DocumentBuilderFactory.newInstance();

        DocumentBuilder documentBuilder =documentBuilderFactory.newDocumentBuilder();

        mDocument = documentBuilder.newDocument();
        
        Element rootElement = mDocument.createElement("irr310");
        mDocument.appendChild(rootElement);
        
        
        // save global nextId
        // save both time
        // save real date
        
        serializeWorld(game.getWorld(), rootElement);

        
        file = new File(saveFilePath);
        
        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        
        fop = new FileOutputStream(file);

        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(mDocument);

        StreamResult result =  new StreamResult(new StringWriter());

      //t.setParameter(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "5");
        transformer.transform(source, result);
        
        // get the content in bytes
        String xmlString = result.getWriter().toString();
        System.out.println(xmlString);
        byte[] contentInBytes = xmlString.getBytes();

        fop.write(contentInBytes);
        fop.flush();
        fop.close();

        System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }

    private void serializeWorld(World world, Element parentElement) {
        Element worldElement = mDocument.createElement("world");
        parentElement.appendChild(worldElement);

        
        
        // Players
        Element playersElement = mDocument.createElement("players");
        worldElement.appendChild(playersElement);
        
        for(Player player: world.getPlayers()) {
            Element playerElement = mDocument.createElement("player");
            playersElement.appendChild(playerElement);

            playerElement.setAttribute("id", Long.toString(player.getId()));
            playerElement.setAttribute("login", player.getLogin());
            playerElement.setAttribute("faction", Long.toString(player.getFaction().getId()));
            playerElement.setAttribute("human", Boolean.toString(player.isHuman()));
            playerElement.setAttribute("local", Boolean.toString(player.isLocal()));
        }
        
        // Factions
        Element factionsElement = mDocument.createElement("factions");
        worldElement.appendChild(factionsElement);
        
        for(Faction faction: world.getFactions()) {
            Element factionElement = mDocument.createElement("faction");
            factionsElement.appendChild(factionElement);

            factionElement.setAttribute("id", Long.toString(faction.getId()));
            factionElement.setAttribute("color", faction.getColor().toString());
            factionElement.setAttribute("home-system", Long.toString(faction.getHomeSystem().getId()));
            factionElement.setAttribute("root-nexus", Long.toString(faction.getRootNexus().getId()));
            
            factionElement.setAttribute("staters-amount", Long.toString(faction.getStatersAmount()));
            factionElement.setAttribute("ores-amount", Long.toString(faction.getOresAmount()));
            factionElement.setAttribute("kolium-amount", Long.toString(faction.getKoliumAmount()));
            factionElement.setAttribute("neuridium-amount", Long.toString(faction.getNeuridiumAmount()));

            Element knownSystemsElement = mDocument.createElement("known-systems");
            factionElement.appendChild(knownSystemsElement);
            for(WorldSystem system : faction.getKnownSystems()) {
                Element knownSystemElement = mDocument.createElement("system");
                knownSystemElement.setAttribute("id", Long.toString(system.getId()));
                knownSystemsElement.appendChild(knownSystemElement);
            }

        }
        
        
//        private List<Player> players = new ArrayList<Player>();
//        
//        private List<Ship> shipList = new ArrayList<Ship>();
//        
//        private FactionProduction production;
//        private FactionStocks stocks;
//        private FactionAvailableProductList availableProductList;
//        
        
        
        // Item full description
        Element itemsElement = mDocument.createElement("items");
        worldElement.appendChild(itemsElement);
        
        for(Item item : world.getItems()) {
            Element itemElement = mDocument.createElement("item");
            itemsElement.appendChild(itemElement);
            
            itemElement.setAttribute("id", Long.toString(item.getId()));
            itemElement.setAttribute("type", item.getType().toString());
            itemElement.setAttribute("owner", Long.toString(item.getOwner().getId()));
            itemElement.setAttribute("usufruct", Long.toString(item.getUsufruct().getId()));
            itemElement.setAttribute("state", item.getState().toString());
            itemElement.setAttribute("product", item.getProduct().getId());

            if(item.getSubItems().size() > 0) {
                Element subitemsElement = mDocument.createElement("subitems");
                itemElement.appendChild(subitemsElement);
                for(Entry<String, Item> subitem : item.getSubItems().entrySet()) {
                    Element subitemElement = mDocument.createElement("subitem");
                    subitemsElement.appendChild(subitemElement);
                    
                    subitemElement.setAttribute("key", subitem.getKey());
                    subitemElement.setAttribute("item", Long.toString(subitem.getValue().getId()));
                }
            }
            
            if(item instanceof DeployableItem) {
                // No specific stuff yet
            }
            
        }
    }

}
