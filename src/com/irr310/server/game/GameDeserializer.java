package com.irr310.server.game;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.common.tools.Vec2;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.system.Nexus;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Color;
import com.irr310.server.world.product.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by fred on 04/05/14.
 */
public class GameDeserializer {

    private final Game mGame;
    private final File mSaveFilePath;
    private final DocumentBuilderFactory docBuilderFactory;
    private final World mWorld;
    private Map<Long, Player> mPlayerMap = new HashMap<Long, Player>();
    private Map<Long, Faction> mFactionMap = new HashMap<Long, Faction>();
    private Map<Long, WorldSystem> mSystemMap = new HashMap<Long, WorldSystem>();
    private Map<Long, Nexus> mNexusMap = new HashMap<Long, Nexus>();

    private enum Pass {
        LINK_PASS, OBJECT_PASS

    }

    public GameDeserializer(Game game, String saveFilePath) {
        mGame = game;
        mWorld = game.getWorld();
        mSaveFilePath = new File(saveFilePath);
        docBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    public void load() {
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

            Document doc;
            doc = docBuilder.parse(mSaveFilePath);

            Element root = doc.getDocumentElement();

            if (!root.getNodeName().equals("irr310")) {
                Log.warn("The file '" + mSaveFilePath.getAbsolutePath() + "' is not a irr310 save file");
                return;
            }

            parseRoot(root, Pass.OBJECT_PASS);
            parseRoot(root, Pass.LINK_PASS);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            Log.error("Failed to parse file '" + mSaveFilePath.getAbsolutePath(), e);
        } catch (IOException e) {
            Log.error("Failed to load file '" + mSaveFilePath.getAbsolutePath(), e);
        }


    }

    private void parseRoot(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        // First pass create items
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("world")) {
                parseWorld(subElement, pass);
            } else {
                Log.error("Unknown tag: " + subElement.getNodeName());
            }
        }
    }

    private void parseWorld(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        // First pass create items
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // TODO error
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("players")) {
                parsePlayers(subElement, pass);
            } else if (subElement.getNodeName().equals("factions")) {
                parseFactions(subElement, pass);
            } else if (subElement.getNodeName().equals("items")) {
                parseItems(subElement, pass);
            } else if (subElement.getNodeName().equals("map")) {
                parseMap(subElement, pass);
            } else {
                Log.error("Unknown tag: " + subElement.getNodeName());
            }
        }
    }

    private void parsePlayers(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("player")) {
                parsePlayer(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseFactions(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("faction")) {
                parseFaction(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseFaction(Element element, Pass pass) {
        Faction faction = null;
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String login = element.getAttribute("login");
                Color color = getColorAttribute(element,"color");
                long koliumAmount = getLongAttribute(element, "kolium-amount");
                long neuridiumAmount = getLongAttribute(element, "neuridium-amount");
                long oresAmount = getLongAttribute(element, "ores-amount");
                long statersAmount = getLongAttribute(element, "staters-amount");

                faction = new Faction(mWorld, id);
                faction.getAvailableProductList().setProductManager(mWorld.getProductManager());
                faction.setColor(color);
                faction.setKoliumAmount(koliumAmount);
                faction.setNeuridiumAmount(neuridiumAmount);
                faction.setOresAmount(oresAmount);
                faction.setStatersAmount(statersAmount);

                mFactionMap.put(id, faction);
                mWorld.addFaction(faction);
            }
            break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long systemId = getLongAttribute(element, "home-system");
                long nexusId = getLongAttribute(element, "root-nexus");


                faction = mFactionMap.get(id);
                WorldSystem system = mSystemMap.get(systemId);
                Nexus nexus = mNexusMap.get(nexusId);

                faction.setHomeSystem(system);
                faction.setRootNexus(nexus);
            }
            break;
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("players")) {
                parseFactionPlayers(subElement, pass, faction);
            } else if (subElement.getNodeName().equals("known-systems")) {
                 parseFactionKnownSystems(subElement, pass, faction);
            } else if (subElement.getNodeName().equals("ships")) {
                // TODO parse ships
            } else if (subElement.getNodeName().equals("production")) {
                // TODO parse production
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
            }
        }
    }

    private void parseFactionPlayers(Element element, Pass pass, Faction faction) {
        if(pass == Pass.LINK_PASS) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // White space
                    continue;
                }
                Element subElement = (Element) node;
                if (subElement.getNodeName().equals("player")) {
                        long playerId = getLongAttribute(subElement, "id");
                        Player player = mPlayerMap.get(playerId);
                        faction.assignPlayer(player);
                } else {
                    throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
                }
            }
        }
    }

    private void parseFactionKnownSystems(Element element, Pass pass, Faction faction) {
        if(pass == Pass.LINK_PASS) {
            NodeList childNodes = element.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    // White space
                    continue;
                }
                Element subElement = (Element) node;
                if (subElement.getNodeName().equals("system")) {
                    long playerId = getLongAttribute(subElement, "id");
                    WorldSystem system = mSystemMap.get(playerId);
                    faction.discoverSystem(system);
                } else {
                    throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag faction element");
                }
            }
        }
    }

    private void parsePlayer(Element element, Pass pass) {
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String login = element.getAttribute("login");
                boolean local = getBooleanAttribute(element, "local");
                boolean human = getBooleanAttribute(element, "human");

                Player player = new Player(mWorld, id, login);
                player.setLocal(local);
                player.setHuman(human);

                mPlayerMap.put(id, player);
                mWorld.addPlayer(player);
            }
                break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long factionId = getLongAttribute(element, "faction");

                Player player = mPlayerMap.get(id);
                Faction faction = mFactionMap.get(factionId);

                player.setFaction(faction);
            }
                break;
        }
    }

    private void parseItems(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("item")) {
                //TODO
                //parseItem(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }


    private void parseMap(Element element, Pass pass) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("system")) {
                parseWorldSystem(subElement, pass);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag players element");
            }
        }
    }

    private void parseWorldSystem(Element element, Pass pass) {
        WorldSystem system = null;

        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                String name = element.getAttribute("name");
                boolean homeSystem = getBooleanAttribute(element, "home-system");
                double radius = getDoubleAttribute(element, "radius");
                Vec2 location = getVec2Attribute(element, "location");

                system = new WorldSystem(mWorld, id, location);

                system.setName(name);
                system.setHomeSystem(homeSystem);
                system.setRadius(radius);

                mSystemMap.put(id, system);
                mWorld.getMap().addZone(system);
            }
            break;
            case LINK_PASS: {
                if(element.hasAttribute("owner")) {
                    long id = getLongAttribute(element, "id");
                    long factionId = getLongAttribute(element, "owner");

                    system = mSystemMap.get(id);
                    Faction faction = mFactionMap.get(factionId);

                    system.setOwner(faction);
                }
            }
            break;
        }


        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("nexuses")) {
                parseNexuses(subElement, pass, system);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag system element");
            }
        }

    }

    private void parseNexuses(Element element, Pass pass, WorldSystem system) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = childNodes.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                // White space
                continue;
            }
            Element subElement = (Element) node;
            if (subElement.getNodeName().equals("nexus")) {
                parseNexus(subElement, pass, system);
            } else {
                throw new RessourceLoadingException("Unknown tag '"+subElement.getNodeName()+"'for tag nexuses element");
            }
        }
    }

    private void parseNexus(Element element, Pass pass, WorldSystem system) {
        switch(pass) {
            case OBJECT_PASS: {
                long id = getLongAttribute(element, "id");
                Vec3 location = getVec3Attribute(element, "location");
                double radius = getDoubleAttribute(element, "radius");

                Nexus nexus = new Nexus(system, id);
                nexus.setLocation(location);
                nexus.setRadius(radius);

                mNexusMap.put(id, nexus);
                system.addNexus(nexus);
            }
            break;
            case LINK_PASS: {
                long id = getLongAttribute(element, "id");
                long factionId = getLongAttribute(element, "owner");

                Nexus nexus = mNexusMap.get(id);
                Faction faction = mFactionMap.get(factionId);

                nexus.setOwner(faction);
            }
            break;
        }
    }

    private long getLongAttribute(Element element, String attribute) {
        return Long.parseLong(element.getAttribute(attribute));
    }

    private boolean getBooleanAttribute(Element element, String attribute) {
        return Boolean.parseBoolean(element.getAttribute(attribute));
    }

    private double getDoubleAttribute(Element element, String attribute) {
        return Double.parseDouble(element.getAttribute(attribute));
    }

    private Color getColorAttribute(Element element, String attribute) {
        return Color.parseColor(element.getAttribute(attribute));
    }

    private Vec2 getVec2Attribute(Element element, String attribute) {
        return Vec2.parseVec2(element.getAttribute(attribute));
    }

    private Vec3 getVec3Attribute(Element element, String attribute) {
        return Vec3.parseVec3(element.getAttribute(attribute));
    }
}
