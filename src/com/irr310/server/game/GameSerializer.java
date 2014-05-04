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

import com.irr310.common.tools.Log;
import com.irr310.common.world.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.irr310.client.ClientConfig;
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

    private final String mSaveFilePath;
    private Document mDocument;

    public GameSerializer(String saveFilePath) {

        mSaveFilePath = saveFilePath;
    }

    public void save(Game game) {
        
        // Create saves directory
        File savesCacheDir = new File(ClientConfig.getSaveDirectoryPath("/"));
        if(!savesCacheDir.exists()) {
            savesCacheDir.mkdirs();
        }

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

        
        file = new File(mSaveFilePath);

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

            Element factionPlayersElement = mDocument.createElement("players");
            factionElement.appendChild(factionPlayersElement);
            for (Player player : faction.getPlayers()) {
                Element playerElement = mDocument.createElement("player");
                playerElement.setAttribute("id", Long.toString(player.getId()));
                factionPlayersElement.appendChild(playerElement);
            }

            Element shipsElement = mDocument.createElement("ships");
            factionElement.appendChild(shipsElement);
            for (Ship ship: faction.getShipList()) {
                Element shipElement = mDocument.createElement("ship");
                shipElement.setAttribute("id", Long.toString(ship.getId()));
                shipsElement.appendChild(shipElement);
            }

            Element productionElement = mDocument.createElement("production");
            factionElement.appendChild(productionElement);
            {
                FactionProduction production = faction.getProduction();
                productionElement.setAttribute("factory-capacity", Long.toString(production.getFactoryCapacity()));
                productionElement.setAttribute("next-factory-capacity-increase-round", Long.toString(production.getNextFactoryCapacityIncreaseRounds()));
                if(production.getActiveProductionTask() != null) {
                    productionElement.setAttribute("active-task", Long.toString(production.getActiveProductionTask().getId()));
                }

                if(production.getNextFactoryCapacityOrder() != null) {
                    Element nextFactoryCapacityOrderElement = mDocument.createElement("next-factory-capacity-order");
                    productionElement.appendChild(nextFactoryCapacityOrderElement);
                    generateFactoryCapacityOrder(nextFactoryCapacityOrderElement, production.getNextFactoryCapacityOrder());
                }

                Element factoryCapacityOrderListElement = mDocument.createElement("factory-capacity-orders");
                productionElement.appendChild(factoryCapacityOrderListElement);
                for (FactionProduction.FactoryCapacityOrder factoryCapacityOrder : production.getFactoryCapacityOrderList()) {
                    Element factoryCapacityOrderElement = mDocument.createElement("factory-capacity");
                    factoryCapacityOrderListElement.appendChild(factoryCapacityOrderElement);
                    generateFactoryCapacityOrder(factoryCapacityOrderElement, factoryCapacityOrder);
                }

                Element factoryCapacityActiveOrderListElement = mDocument.createElement("factory-capacity-active-orders");
                productionElement.appendChild(factoryCapacityActiveOrderListElement);
                for (FactionProduction.FactoryCapacityOrder factoryCapacityOrder : production.getFactoryCapacityActiveList()) {
                    Element factoryCapacityOrderElement = mDocument.createElement("factory-capacity");
                    factoryCapacityActiveOrderListElement.appendChild(factoryCapacityOrderElement);
                    generateFactoryCapacityOrder(factoryCapacityOrderElement, factoryCapacityOrder);
                }

                Element productionTasksElement = mDocument.createElement("production-tasks");
                productionElement.appendChild(productionTasksElement);
                for (ProductionTask productionTask : production.getProductionTaskQueue()) {
                    Element productionTaskElement = mDocument.createElement("production-task");
                    productionTasksElement.appendChild(productionTaskElement);

                    Element workUnitElement = mDocument.createElement("root-work-unit");
                    productionTaskElement.appendChild(workUnitElement);
                    generateWorkUnit(workUnitElement, productionTask.getRootWorkUnit());

                }
            }
        }

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


        //
        Element mapElement = mDocument.createElement("map");
        worldElement.appendChild(mapElement);
        for (WorldSystem worldSystem : world.getMap().getSystems()) {
            Element systemElement = mDocument.createElement("system");
            mapElement.appendChild(systemElement);

            systemElement.setAttribute("id", Long.toString(worldSystem.getId()));
            systemElement.setAttribute("location", worldSystem.getLocation().toString());
            systemElement.setAttribute("radius", Double.toString(worldSystem.getRadius()));
            systemElement.setAttribute("name", worldSystem.getName());
            if(worldSystem.getOwner() != null) {
                systemElement.setAttribute("owner", Long.toString(worldSystem.getOwner().getId()));
            }
            systemElement.setAttribute("home-system", Boolean.toString(worldSystem.isHomeSystem()));

            Element nexusesElement = mDocument.createElement("nexuses");
            systemElement.appendChild(nexusesElement);
            for (Nexus nexus : worldSystem.getNexuses()) {
                Element nexusElement = mDocument.createElement("nexus");
                nexusesElement.appendChild(nexusElement);

                nexusElement.setAttribute("id", Long.toString(nexus.getId()));
                nexusElement.setAttribute("location", nexus.getLocation().toString());
                nexusElement.setAttribute("radius", Double.toString(nexus.getRadius()));
                nexusElement.setAttribute("owner", Long.toString(nexus.getOwner().getId()));
            }


            //            private final List<Nexus> nexuses;

//            private final List<CelestialObject> celestialObjects;

//            private final List<Ship> ships;
//            private final List<Part> parts;
//            private final List<Part> myParts;
//            private final Map<Long, CelestialObject> celestialObjectIdMap;
//            private final Map<Long, Capacity> capacityIdMap;
//            private final Map<Long, Component> componentIdMap;
//            private final Map<Long, Slot> slotIdMap;
//            private final Map<Long, Part> partIdMap;
//            private final Map<Long, Ship> shipIdMap;
//            private SystemEngine systemEngine;


        }


    }

    private void generateWorkUnit(Element workUnitElement, ProductionTask.WorkUnit workUnit) {

        if(workUnit instanceof ProductionTask.BatchWorkUnit) {
            ProductionTask.BatchWorkUnit batchWorkUnit = (ProductionTask.BatchWorkUnit) workUnit;

            workUnitElement.setAttribute("type", "batch");
            workUnitElement.setAttribute("product", batchWorkUnit.getProduct().getId());
            workUnitElement.setAttribute("requested-quantity", Long.toString(batchWorkUnit.getRequestedQuantity()));
            workUnitElement.setAttribute("done-quantity", Long.toString(batchWorkUnit.getDoneQuantity()));

            if(batchWorkUnit.getCurrentWorkUnit() != null) {
                Element subItemElement = mDocument.createElement("current-work-unit");
                workUnitElement.appendChild(subItemElement);
                generateWorkUnit(subItemElement, batchWorkUnit.getCurrentWorkUnit());
            }


        } else if(workUnit instanceof ProductionTask.BuildWorkUnit) {
            ProductionTask.BuildWorkUnit buildWorkUnit = (ProductionTask.BuildWorkUnit) workUnit;

            workUnitElement.setAttribute("type", "build");
            workUnitElement.setAttribute("product", buildWorkUnit.getProduct().getId());
            workUnitElement.setAttribute("work-state", buildWorkUnit.getWorkState().toString());
            workUnitElement.setAttribute("pending-ores", Long.toString(buildWorkUnit.getPendingOres()));
            workUnitElement.setAttribute("accumulated-production-capacity", Long.toString(buildWorkUnit.getAccumulatedProductionCapacity()));

            Element reservedItemsElement = mDocument.createElement("reserved-items");
            workUnitElement.appendChild(reservedItemsElement);
            for (Entry<String, Item> stringItemEntry : buildWorkUnit.getReservedItems().entrySet()) {
                Element reservedItemElement = mDocument.createElement("reserved-item");
                reservedItemsElement.appendChild(reservedItemElement);
                reservedItemElement.setAttribute("key", stringItemEntry.getKey());
                reservedItemElement.setAttribute("item", Long.toString(stringItemEntry.getValue().getId()));
            }

            if(buildWorkUnit.getSubItemWorkUnit() != null) {
                Element subItemElement = mDocument.createElement("subitem-work-unit");
                workUnitElement.appendChild(subItemElement);
                generateWorkUnit(subItemElement, buildWorkUnit.getSubItemWorkUnit());
            }
        } else {
            Log.warn("Not implemented WorkUnit type '"+workUnit.getClass().getSimpleName()+"'");
        }



    }

    private void generateFactoryCapacityOrder(Element factoryCapacityOrderElement, FactionProduction.FactoryCapacityOrder factoryCapacityOrder) {
        factoryCapacityOrderElement.setAttribute("sell-price-per-count", Long.toString(factoryCapacityOrder.sellPricePerCount));
        factoryCapacityOrderElement.setAttribute("count", Long.toString(factoryCapacityOrder.count));
    }

}
