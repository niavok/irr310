package com.irr310.server.game;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
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
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.*;
import com.irr310.server.GameServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.irr310.client.ClientConfig;
import com.irr310.common.world.item.DeployableItem;
import com.irr310.common.world.item.Item;

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

        worldElement.setAttribute("next-item-id", Long.toString(GameServer.getNextId()));

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

                    productionTaskElement.setAttribute("id", Long.toString(productionTask.getId()));

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
            if(item.getOwner() != null) {
                itemElement.setAttribute("owner", Long.toString(item.getOwner().getId()));
            }
            if(item.getUsufruct() != null) {
                itemElement.setAttribute("usufruct", Long.toString(item.getUsufruct().getId()));
            }
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


            Element shipsElement = mDocument.createElement("ships");
            systemElement.appendChild(shipsElement);
            for (Ship ship : worldSystem.getShips()) {
                Element shipElement = mDocument.createElement("ship");
                shipsElement.appendChild(shipElement);

                shipElement.setAttribute("id", Long.toString(ship.getId()));
                shipElement.setAttribute("owner", Long.toString(ship.getOwner().getId()));
                shipElement.setAttribute("destructible", Boolean.toString(ship.isDestructible()));

                Element shipComponentsElement = mDocument.createElement("components");
                shipElement.appendChild(shipComponentsElement);
                for (Entry<String, Component> entry : ship.getComponentKeysMap().entrySet()) {
                    Element shipComponentElement = mDocument.createElement("component");
                    shipComponentsElement.appendChild(shipComponentElement);

                    shipComponentElement.setAttribute("key", entry.getKey());
                    shipComponentElement.setAttribute("id", Long.toString(entry.getValue().getId()));
                }


                Element shipLinksElement = mDocument.createElement("links");
                shipElement.appendChild(shipLinksElement);
                for (Link link : ship.getLinks()) {
                    Element shipLinkElement = mDocument.createElement("link");
                    shipLinksElement.appendChild(shipLinkElement);
                    shipLinkElement.setAttribute("slot1", Long.toString(link.getSlot1().getId()));
                    shipLinkElement.setAttribute("slot2", Long.toString(link.getSlot2().getId()));
                }
            }

            Element partsElement = mDocument.createElement("parts");
            systemElement.appendChild(partsElement);
            for (Part part : worldSystem.getParts()) {
                Element partElement = mDocument.createElement("part");
                partsElement.appendChild(partElement);
                partElement.setAttribute("id", Long.toString(part.getId()));
                if(part.getOwner() != null) {
                    partElement.setAttribute("owner", Long.toString(part.getOwner().getId()));
                }
                partElement.setAttribute("mass", Double.toString(part.getMass()));
                partElement.setAttribute("linear-dumping", Double.toString(part.getLinearDamping()));
                partElement.setAttribute("angular-dumping", Double.toString(part.getAngularDamping()));
                partElement.setAttribute("angular-speed", part.getAngularSpeed().toString());
                partElement.setAttribute("linear-speed", part.getLinearSpeed().toString());
                partElement.setAttribute("transform", part.getTransform().toString());
                partElement.setAttribute("shape", part.getShape().toString());
                partElement.setAttribute("parent", Long.toString(part.getParentObject().getId()));
                partElement.setAttribute("collision-shape", part.getCollisionShape().toString());

                if(part.getCollisionExcludeList() != null) {
                    Element collisionExcludeListElement = mDocument.createElement("collision-exclude-list");
                    partElement.appendChild(collisionExcludeListElement);
                    for (Part collisionPart : part.getCollisionExcludeList()) {
                        Element collisionExcludePartElement = mDocument.createElement("part");
                        collisionExcludeListElement.appendChild(collisionExcludePartElement);
                        partElement.setAttribute("id", Long.toString(collisionPart.getId()));
                    }
                }
            }

            Element slotsElement = mDocument.createElement("slots");
            systemElement.appendChild(slotsElement);
            for (Slot slot : worldSystem.getSlotIdMap().values()) {
                Element slotElement = mDocument.createElement("slot");
                slotsElement.appendChild(slotElement);

                generateSlot(slotElement, slot);
            }

            Element celestialObjectsElement = mDocument.createElement("celestial-objects");
            systemElement.appendChild(celestialObjectsElement);
            for (CelestialObject celestialObject : worldSystem.getCelestialObjects()) {
                Element celestialObjectElement = mDocument.createElement("celestial-object");
                celestialObjectsElement.appendChild(celestialObjectElement);

                celestialObjectElement.setAttribute("id", Long.toString(celestialObject.getId()));

                generateSystemObjectProperties(celestialObjectElement, celestialObject);
            }

            Element componentsElement = mDocument.createElement("components");
            systemElement.appendChild(componentsElement);
            for (Component component : worldSystem.getComponentIdMap().values()) {
                Element componentElement = mDocument.createElement("component");
                componentsElement.appendChild(componentElement);

                componentElement.setAttribute("id", Long.toString(component.getId()));




                componentElement.setAttribute("key", component.getKey());
                componentElement.setAttribute("efficiency", Double.toString(component.getEfficiency()));
                componentElement.setAttribute("quality", Double.toString(component.getQuality()));
                componentElement.setAttribute("ship", Long.toString(component.getShip().getId()));
                componentElement.setAttribute("location-in-ship", component.getLocationInShip().toString());
                componentElement.setAttribute("ship-rotation", component.getShipRotation().toString());
                componentElement.setAttribute("attached", Boolean.toString(component.isAttached()));



                Element componentSlotsElement = mDocument.createElement("slots");
                componentElement.appendChild(componentSlotsElement);
                for (Slot slot : component.getSlots()) {
                    Element componentSlotElement = mDocument.createElement("slot");
                    componentSlotsElement.appendChild(componentSlotElement);
                    componentSlotElement.setAttribute("id", Long.toString(slot.getId()));
                }

                Element componentCapacitiesElement = mDocument.createElement("capacities");
                componentElement.appendChild(componentCapacitiesElement);
                for (Capacity capacity : component.getCapacities()) {
                    Element componentCapacityElement = mDocument.createElement("capacity");
                    componentCapacitiesElement.appendChild(componentCapacityElement);
                    componentCapacityElement.setAttribute("id", Long.toString(capacity.getId()));
                }

                //SystemObject properties
                generateSystemObjectProperties(componentElement, component);


            }

            Element capacitiesElement = mDocument.createElement("capacities");
            systemElement.appendChild(capacitiesElement);
            for (Capacity capacity : worldSystem.getCapacityIdMap().values()) {
                Element capacityElement = mDocument.createElement("capacity");
                capacitiesElement.appendChild(capacityElement);

                capacityElement.setAttribute("id", Long.toString(capacity.getId()));
                capacityElement.setAttribute("name", capacity.getName());
                capacityElement.setAttribute("component", Long.toString(capacity.getComponent().getId()));

                if(capacity instanceof LinearEngineCapacity) {
                    LinearEngineCapacity linearEngineCapacity = (LinearEngineCapacity) capacity;
                    capacityElement.setAttribute("type", "linear-engine");
                    capacityElement.setAttribute("current-thrust", Double.toString(linearEngineCapacity.getCurrentThrust()));
                    capacityElement.setAttribute("target-thrust", Double.toString(linearEngineCapacity.getTargetThrust()));
                    capacityElement.setAttribute("base-max-thrust", Double.toString(linearEngineCapacity.getTheoricalMaxThrust()));
                    capacityElement.setAttribute("base-min-thrust", Double.toString(linearEngineCapacity.getTheoricalMinThrust()));
                    capacityElement.setAttribute("base-variation-speed", Double.toString(linearEngineCapacity.getTheoricalVariationSpeed()));
                    capacityElement.setAttribute("max-thrust", Double.toString(linearEngineCapacity.getMaxThrust()));
                    capacityElement.setAttribute("min-thrust", Double.toString(linearEngineCapacity.getMinThrust()));
                    capacityElement.setAttribute("variation-speed", Double.toString(linearEngineCapacity.getVariationSpeed()));
                    capacityElement.setAttribute("target-thrust-input", Double.toString(linearEngineCapacity.getTargetThrustInput()));
                }


                //TODO custom properties
            }
        }


    }

    private void generateSystemObjectProperties(Element element, SystemObject systemObject) {
        element.setAttribute("name", systemObject.getName());
        element.setAttribute("skin", systemObject.getSkin());
        element.setAttribute("durability-max", Double.toString(systemObject.getDurabilityMax()));
        element.setAttribute("durability", Double.toString(systemObject.getDurability()));
        element.setAttribute("physical-resistance", Double.toString(systemObject.getPhysicalResistance()));
        element.setAttribute("heat-resistance", Double.toString(systemObject.getHeatResistance()));


        Element componentPartsElement = mDocument.createElement("parts");
        element.appendChild(componentPartsElement);
        for (Part part : systemObject.getParts()) {
            Element componentPartElement = mDocument.createElement("part");
            componentPartsElement.appendChild(componentPartElement);
            componentPartElement.setAttribute("id", Long.toString(part.getId()));
        }
    }

    private void generateSlot(Element element, Slot slot) {
        element.setAttribute("id", Long.toString(slot.getId()));
        element.setAttribute("position", slot.getPosition().toString());
        element.setAttribute("part", Long.toString(slot.getPart().getId()));
        element.setAttribute("component", Long.toString(slot.getComponent().getId()));
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
