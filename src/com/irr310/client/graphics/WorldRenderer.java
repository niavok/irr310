package com.irr310.client.graphics;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBShaderObjects;

import com.irr310.client.graphics.effects.BulletEffect;
import com.irr310.client.graphics.gui.GuiConstants;
import com.irr310.client.graphics.gui.InventoryMenu;
import com.irr310.client.graphics.gui.UpgradeMenu;
import com.irr310.client.graphics.skin.AsteroidSkin;
import com.irr310.client.graphics.skin.CameraSkin;
import com.irr310.client.graphics.skin.FactorySkin;
import com.irr310.client.graphics.skin.GenericSkin;
import com.irr310.client.graphics.skin.GunSkin;
import com.irr310.client.graphics.skin.HullSkin;
import com.irr310.client.graphics.skin.LootSkin;
import com.irr310.client.graphics.skin.MonolithSkin;
import com.irr310.client.graphics.skin.PropellerSkin;
import com.irr310.client.graphics.skin.PvCellSkin;
import com.irr310.client.graphics.skin.ReactorSkin;
import com.irr310.client.graphics.skin.Skin;
import com.irr310.client.graphics.skin.TankSkin;
import com.irr310.client.graphics.skin.ThrusterBlockSkin;
import com.irr310.client.graphics.skin.WingSkin;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.GameTime;
import com.irr310.common.event.AddGuiComponentEvent;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.DamageEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.MoneyChangedEvent;
import com.irr310.common.event.NextWaveEvent;
import com.irr310.common.event.RemoveGuiComponentEvent;
import com.irr310.common.event.UpgradeStateChanged;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;
import com.irr310.common.world.Monolith;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.World;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DShader;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DEye3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DFollow3DCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DPoint;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;
import fr.def.iss.vd2.lib_v3d.gui.V3DButton;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGui;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiXAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiLayer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

public class WorldRenderer implements GraphicRenderer {
    V3DCameraBinding fullscreenBinding;
    private V3DScene scene;
    private List<Pair<LinearEngineCapacity, V3DLine>> thrustLines;
    private List<GraphicalElement> animatedList = new CopyOnWriteArrayList<GraphicalElement>();
    private List<GraphicalElement> guiAnimatedList = new CopyOnWriteArrayList<GraphicalElement>();
    private List<GraphicalElement> elementList = new CopyOnWriteArrayList<GraphicalElement>();
    private List<GuiAnimatedElement> persistantGuiElementList = new CopyOnWriteArrayList<GuiAnimatedElement>();

    private V3DFollow3DCameraController cameraController;
    private Map<WorldObject, List<GraphicalElement>> worldObjectToV3DElementMap = new HashMap<WorldObject, List<GraphicalElement>>();

    V3DEye3DCamera activeCamera;
    private final GraphicEngine engine;
    private V3DGuiLayer interfaceLayer;
    private V3DGuiLayer hudLayer;
    private V3DGuiLayer mainMenuLayer;

    public enum GuiLayer {
        INTEFACE, HUD, MAIN_MENU,
    }

    
    // Game
    private V3DLabel waveCountText;
    private NextWaveEvent lastWaveEvent = null;
    private Monolith monolith;
    private V3DGuiRectangle monolithStatus;
    private V3DLabel monolithStatusText;
    private V3DLabel moneyText;
    private MenuContainer upgradeMenu;
    private MenuContainer inventoryMenu;
    boolean upgradeMenuEnabled = false;
    boolean inventoryMenuEnabled = false;

    public WorldRenderer(GraphicEngine engine) {
        this.engine = engine;
        thrustLines = new ArrayList<Pair<LinearEngineCapacity, V3DLine>>();
    }

    @Override
    public void init() {
        activeCamera = new V3DEye3DCamera(engine.getV3DContext());

        cameraController = new V3DFollow3DCameraController(this, activeCamera);
        animatedList.add(cameraController);

        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);
        activeCamera.setBackgroundColor(V3DColor.white);

        // Add zoom and pane camera controlleur
        // cameraController.setLimitBound(false);

        scene = new V3DScene(engine.getV3DContext());
        activeCamera.setScene(scene);

        /*
         * V3DBox sky = new V3DBox(context); sky.setSize(new V3DVect3(1024, 768,
         * 1)); sky.setPosition(1024 / 2, 768 / 2, 0);
         */

        // activeCamera.getBackgroundScene().add(new V3DColorElement(sky,
        // V3DColor.pink));
        // activeCamera.getBackgroundScene().add(new V3DColorElement(new
        // Sky(context, cameraController), V3DColor.pink));

        createBubble();

        // Add reference
        V3DElement ref0 = generateReference();
        ref0.setPosition(0, 0, 0);
        V3DElement ref1 = generateReference();
        ref1.setPosition(1, 0, 0);
        V3DElement ref2 = generateReference();
        ref2.setPosition(2, 0, 0);

        scene.add(ref0);
        scene.add(ref1);
        scene.add(ref2);

        generateGuiStructure();

        loadCurrentWorld();

        // activeCamera.setShowCenter(true);

        activeCamera.fitAll();

        // activeCamera.fit(new V3DVect3(0, 0, 0), new V3DVect3(5, 5, 5));

    }

    private void reloadGui() {

        upgradeMenu = null;
        inventoryMenu = null;
        upgradeMenuEnabled = false;
        inventoryMenuEnabled = false;
        
        // Generate logo
        V3DLabel logoIRR = new V3DLabel("IRR");
        logoIRR.setFontStyle("Ubuntu", "bold", 24);
        logoIRR.setColor(GuiConstants.irrRed, V3DColor.transparent);
        logoIRR.setPosition(10, 10);
        mainMenuLayer.add(logoIRR);

        V3DLabel logo310 = new V3DLabel("310");
        logo310.setFontStyle("Ubuntu", "bold", 24);
        logo310.setColor(V3DColor.black, V3DColor.transparent);
        logo310.setPosition(50, 10);
        mainMenuLayer.add(logo310);

        // Generate stats box
        V3DGuiRectangle indicatorBorder = new V3DGuiRectangle();
        indicatorBorder.setyAlignment(GuiYAlignment.TOP);
        indicatorBorder.setPosition(120, 10);
        indicatorBorder.setSize(300, 30);
        indicatorBorder.setFillColor(GuiConstants.irrFill);
        indicatorBorder.setBorderColor(GuiConstants.irrRed);
        mainMenuLayer.add(indicatorBorder);

        final V3DLabel resolutionIndicator = new V3DLabel("" + (int) engine.getViewportSize().x + "x" + (int) engine.getViewportSize().y + " px");
        resolutionIndicator.setPosition(300, 17);
        resolutionIndicator.setFontStyle("Ubuntu", "bold", 16);
        resolutionIndicator.setColor(V3DColor.black, V3DColor.transparent);
        mainMenuLayer.add(resolutionIndicator);

        generateUpgradeBox();
        generateReputationBox();
        generateWaveBox();
        generateDamageBox();

        for (GuiAnimatedElement element : persistantGuiElementList) {
            addGuiComponent(element.getGuiElement(), element.getLayer());
        }
    }

    private void generateGuiStructure() {
        V3DGui gui = fullscreenBinding.getGui();

        hudLayer = new V3DGuiLayer(gui);
        hudLayer.getFenGUIWidget().setBlockClick(false);
        interfaceLayer = new V3DGuiLayer(gui);
        interfaceLayer.getFenGUIWidget().setBlockClick(false);
        mainMenuLayer = new V3DGuiLayer(gui);
        mainMenuLayer.getFenGUIWidget().setBlockClick(false);

        V3DGuiLayer menuLayer = new V3DGuiLayer(gui);
        menuLayer.getFenGUIWidget().setBlockClick(false);
        V3DGuiLayer pauseLayer = new V3DGuiLayer(gui);
        pauseLayer.getFenGUIWidget().setBlockClick(false);
        // pauseLayer.setColor(new V3DColor(0,0,0,0.5f));
        V3DGuiLayer popUpLayer = new V3DGuiLayer(gui);
        popUpLayer.getFenGUIWidget().setBlockClick(false);

        gui.add(hudLayer);
        gui.add(interfaceLayer);
        gui.add(menuLayer);
        gui.add(pauseLayer);
        gui.add(mainMenuLayer);
        gui.add(popUpLayer);

        addPersistantGuiElement(new GuiAnimatedElement(this) {
            final V3DLabel clockIndicator = new V3DLabel("Time: --");
            DecimalFormat format = new DecimalFormat("0");
            {
                clockIndicator.setPosition(128, 17);
                clockIndicator.setFontStyle("Ubuntu", "bold", 16);
                clockIndicator.setColor(V3DColor.black, V3DColor.transparent);
            }

            @Override
            public void update() {
                clockIndicator.setText("Time: " + format.format(GameTime.getGameTime().getSeconds()) + " s");

            }

            @Override
            public V3DGuiComponent getGuiElement() {
                return clockIndicator;
            }

            @Override
            public GuiLayer getLayer() {
                return GuiLayer.MAIN_MENU;
            }

        });

        addPersistantGuiElement(new GuiAnimatedElement(this) {
            DecimalFormat format = new DecimalFormat("0");
            final V3DLabel fpsIndicator = new V3DLabel("-- fps");

            {
                fpsIndicator.setPosition(235, 17);
                fpsIndicator.setFontStyle("Ubuntu", "bold", 16);
                fpsIndicator.setColor(V3DColor.black, V3DColor.transparent);
            }

            @Override
            public void update() {
                fpsIndicator.setText("" + format.format(engine.getFps()) + " fps");

            }

            @Override
            public V3DGuiComponent getGuiElement() {
                return fpsIndicator;
            }

            @Override
            public GuiLayer getLayer() {
                return GuiLayer.MAIN_MENU;
            }

        });

    }

    private void generateUpgradeBox() {

        V3DContainer container = new V3DContainer();
        container.setPosition(10, 10);
        container.setSize(200, 110);
        container.setyAlignment(GuiYAlignment.BOTTOM);
        interfaceLayer.add(container);

        V3DGuiRectangle upgradeBase = new V3DGuiRectangle();
        upgradeBase.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeBase.setPosition(0, 0);
        upgradeBase.setSize(200, 80);
        upgradeBase.setBorderWidth(4);
        upgradeBase.setFillColor(GuiConstants.irrFill);
        upgradeBase.setBorderColor(GuiConstants.irrGreen);
        container.add(upgradeBase);

        V3DGuiRectangle upgradeTop = new V3DGuiRectangle();
        upgradeTop.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeTop.setPosition(0, 80);
        upgradeTop.setBorderWidth(4);
        upgradeTop.setSize(200, 30);
        upgradeTop.setFillColor(GuiConstants.irrGreen);
        upgradeTop.setBorderColor(GuiConstants.irrGreen);
        container.add(upgradeTop);

        final V3DLabel upgradeText = new V3DLabel("Upgrades");
        upgradeText.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeText.setPosition(40, 80);
        upgradeText.setFontStyle("Ubuntu", "bold", 24);
        upgradeText.setColor(V3DColor.white, V3DColor.transparent);
        container.add(upgradeText);

        moneyText = new V3DLabel(LoginManager.localPlayer.getMoney() + " $");
        moneyText.setyAlignment(GuiYAlignment.BOTTOM);
        moneyText.setxAlignment(GuiXAlignment.RIGHT);
        moneyText.setPosition(15, 20);
        moneyText.setFontStyle("Ubuntu", "bold", 40);
        moneyText.setColor(GuiConstants.irrGreen, V3DColor.transparent);
        container.add(moneyText);
        
        V3DButton button  = new V3DButton("");
        button.setPosition(0, 0);
        button.setPadding(140,200,0,0);
        button.getFenGUIWidget().addButtonPressedListener(new IButtonPressedListener() {
            
            @Override
            public void buttonPressed(ButtonPressedEvent e) {
                toogleUpgradeMenu();
            }

            
        });
        container.add(button);
    }

    private void generateReputationBox() {

        V3DContainer container = new V3DContainer();
        container.setPosition(250, 10);
        container.setSize(200, 110);
        container.setyAlignment(GuiYAlignment.BOTTOM);
        interfaceLayer.add(container);

        V3DGuiRectangle upgradeBase = new V3DGuiRectangle();
        upgradeBase.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeBase.setPosition(0, 0);
        upgradeBase.setSize(200, 80);
        upgradeBase.setBorderWidth(4);
        upgradeBase.setFillColor(GuiConstants.irrFill);
        upgradeBase.setBorderColor(GuiConstants.irrBlue);
        container.add(upgradeBase);

        V3DGuiRectangle upgradeTop = new V3DGuiRectangle();
        upgradeTop.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeTop.setPosition(0, 80);
        upgradeTop.setBorderWidth(4);
        upgradeTop.setSize(200, 30);
        upgradeTop.setFillColor(GuiConstants.irrBlue);
        upgradeTop.setBorderColor(GuiConstants.irrBlue);
        container.add(upgradeTop);

        final V3DLabel upgradeText = new V3DLabel("Reputation");
        upgradeText.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeText.setPosition(40, 80);
        upgradeText.setFontStyle("Ubuntu", "bold", 24);
        upgradeText.setColor(V3DColor.white, V3DColor.transparent);
        container.add(upgradeText);

        final V3DLabel moneyText = new V3DLabel("0");
        moneyText.setxAlignment(GuiXAlignment.RIGHT);
        moneyText.setyAlignment(GuiYAlignment.BOTTOM);
        moneyText.setPosition(40, 15);
        moneyText.setFontStyle("Ubuntu", "bold", 45);
        moneyText.setColor(GuiConstants.irrBlue, V3DColor.transparent);
        container.add(moneyText);

    }

    private void generateWaveBox() {

        V3DContainer container = new V3DContainer();
        container.setxAlignment(GuiXAlignment.RIGHT);
        container.setSize(240, 130);
        container.setPosition(10, 10);
        container.setyAlignment(GuiYAlignment.BOTTOM);
        interfaceLayer.add(container);

        V3DGuiRectangle upgradeBase = new V3DGuiRectangle();
        upgradeBase.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeBase.setPosition(0, 0);
        upgradeBase.setSize(240, 110);
        upgradeBase.setBorderWidth(4);
        upgradeBase.setFillColor(GuiConstants.irrFill);
        upgradeBase.setBorderColor(GuiConstants.irrRed);
        container.add(upgradeBase);

        waveCountText = new V3DLabel("Wave " + (lastWaveEvent == null ? "--" : lastWaveEvent.getWaveId()));
        waveCountText.setyAlignment(GuiYAlignment.BOTTOM);
        waveCountText.setPosition(25, 32);
        waveCountText.setFontStyle("Ubuntu", "bold", 45);
        waveCountText.setColor(GuiConstants.irrRed, V3DColor.transparent);
        container.add(waveCountText);

    }

    private void generateDamageBox() {

        V3DContainer container = new V3DContainer();
        container.setxAlignment(GuiXAlignment.RIGHT);
        container.setSize(120, 200);
        container.setPosition(10, 10);
        container.setyAlignment(GuiYAlignment.TOP);
        interfaceLayer.add(container);

        V3DGuiRectangle upgradeBase = new V3DGuiRectangle();
        upgradeBase.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeBase.setPosition(0, 0);
        upgradeBase.setSize(120, 200);
        upgradeBase.setBorderWidth(4);
        upgradeBase.setFillColor(GuiConstants.irrFill);
        upgradeBase.setBorderColor(GuiConstants.irrRed);
        container.add(upgradeBase);

        monolithStatus = new V3DGuiRectangle();
        monolithStatus.setyAlignment(GuiYAlignment.TOP);
        monolithStatus.setPosition(10, 30);
        monolithStatus.setSize(10, 20);
        monolithStatus.setBorderWidth(2);
        monolithStatus.setFillColor(new V3DColor(0, 150, 0, 0.5f));
        monolithStatus.setBorderColor(new V3DColor(0, 150, 0));
        container.add(monolithStatus);

        monolithStatusText = new V3DLabel("");
        monolithStatusText.setyAlignment(GuiYAlignment.TOP);
        monolithStatusText.setPosition(25, 32);
        monolithStatusText.setFontStyle("Ubuntu", "", 16);
        monolithStatusText.setColor(V3DColor.black, V3DColor.transparent);
        container.add(monolithStatusText);
        updateMonolithStatus();

    }
    
    private void toogleUpgradeMenu() {
        if(upgradeMenu == null) {
            upgradeMenu = new UpgradeMenu();
        }
        
        if(!upgradeMenuEnabled) {
            interfaceLayer.add(upgradeMenu);
            upgradeMenuEnabled = true;
        } else {
            interfaceLayer.remove(upgradeMenu);
            upgradeMenuEnabled = false;
        }
        
    }
    
    private void enabledUpgradeMenu() {
        if(upgradeMenu == null) {
            upgradeMenu = new UpgradeMenu();
        }
        
        if(!upgradeMenuEnabled) {
            interfaceLayer.add(upgradeMenu);
            upgradeMenuEnabled = true;
        }
    }
    
    private void disableUpgradeMenu() {
        interfaceLayer.remove(upgradeMenu);
        upgradeMenuEnabled = false;
    }
    
    private void enabledInventoryMenu() {
        if(inventoryMenu == null) {
            inventoryMenu = new InventoryMenu();
        }
        
        if(!inventoryMenuEnabled) {
            interfaceLayer.add(inventoryMenu);
            inventoryMenuEnabled = true;
        }
    }
    
    private void disableInventoryMenu() {
        interfaceLayer.remove(inventoryMenu);
        inventoryMenuEnabled = false;
    }
    
    
    private void toogleInventoryMenu() {
        if(inventoryMenu == null) {
            inventoryMenu = new InventoryMenu();
        }
        
        if(!inventoryMenuEnabled) {
            interfaceLayer.add(inventoryMenu);
            inventoryMenuEnabled = true;
        } else {
            interfaceLayer.remove(inventoryMenu);
            inventoryMenuEnabled = false;
        }
        
    }

    private void updateMonolithStatus() {
        if (monolithStatusText != null && monolith != null) {
            int color = (int) (150 * monolith.getDurability() / monolith.getDurabilityMax());

            monolithStatus.setFillColor(new V3DColor(150 - color, color, 0, 0.5f));
            monolithStatus.setBorderColor(new V3DColor(150 - color, color, 0));
            monolithStatusText.setText("" + (int) monolith.getDurability() + "/" + (int) monolith.getDurabilityMax());
        }
    }

    private void loadCurrentWorld() {
        System.err.println("add current world");
        World world = Game.getInstance().getWorld();
        for (CelestialObject celestialObject : world.getCelestialsObjects()) {
            addCelestialObject(celestialObject);
        }
    }

    public void frame() {

        Game.getInstance().getWorld().lock();

        Log.perfBegin("amination");

        // amination
        for (GraphicalElement animated : animatedList) {
            animated.update();
        }
        Log.perfEnd();

        /*
         * Log.perfBegin("fit"); if (fitOrder == null) { activeCamera.fitAll();
         * } else { activeCamera.fit(fitOrder.getBoundingBox()); }
         * Log.perfEnd();
         */

        Log.perfBegin("Apply forces");
        // Apply forces
        for (Pair<LinearEngineCapacity, V3DLine> thrustLinePair : thrustLines) {
            V3DLine thrustLine = thrustLinePair.getRight();
            LinearEngineCapacity engine = thrustLinePair.getLeft();

            thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, -(float) engine.getCurrentThrust(), 0));
        }
        Log.perfEnd();

        Game.getInstance().getWorld().unlock();
    }

    private void createBubble() {

        File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, engine.getV3DContext());
        // elementStructure.setShader("bubble");
        elementStructure.setScale(1000);

        V3DShader shader = new V3DShader("bubble") {
            private int resolution;
            private int time;
            private long startTime;

            protected void loadUniforms() {
                resolution = ARBShaderObjects.glGetUniformLocationARB(shader, "resolution");
                time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");

                startTime = new Date().getTime();
            };

            protected void setUniforms() {
                // V3DVect3 rotation = camera.getRotation();
                // ARBShaderObjects.glUniform3fARB(inputRotation, rotation.x,
                // rotation.y, rotation.z);
                ARBShaderObjects.glUniform2fARB(resolution, cameraController.getCamera().getCurrentWidth(), cameraController.getCamera()
                                                                                                                            .getCurrentHeight());
                float time2 = ((float) (new Date().getTime() - startTime)) / 10000.0f;
                ARBShaderObjects.glUniform1fARB(time, time2);

            }
        };

        scene.add(new V3DColorElement(new V3DShaderElement(elementStructure, shader), new V3DColor(255, 255, 255)));
    }

    private V3DElement generateReference() {
        V3DLine xAxis = new V3DLine(engine.getV3DContext());
        xAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(1, 0, 0));

        V3DLine yAxis = new V3DLine(engine.getV3DContext());
        yAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 1, 0));

        V3DLine zAxis = new V3DLine(engine.getV3DContext());
        zAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 1));

        V3DGroupElement group = new V3DGroupElement(engine.getV3DContext());

        group.add(new V3DColorElement(xAxis, V3DColor.red));
        group.add(new V3DColorElement(yAxis, V3DColor.green));
        group.add(new V3DColorElement(zAxis, V3DColor.blue));
        return group;
    }

    protected void addCelestialObject(final CelestialObject object) {

        worldObjectToV3DElementMap.put(object, new ArrayList<GraphicalElement>());

        GraphicalElement element = addObject(object);
        worldObjectToV3DElementMap.get(object).add(element);

        if (object instanceof Monolith) {
            monolith = (Monolith) object;
            updateMonolithStatus();
        }
    }

    protected void removeCelestialObject(final CelestialObject object) {
        List<GraphicalElement> elements = worldObjectToV3DElementMap.get(object);
        for (GraphicalElement element : elements) {
            if (element != null) {
                element.destroy();
            }
        }

    }

    protected void addShip(final Ship ship) {

        for (Component component : ship.getComponents()) {
            addObject(component);

            for (Capacity capacity : component.getCapacities()) {
                if (capacity instanceof LinearEngineCapacity) {

                    V3DLine thrustLine = new V3DLine(engine.getV3DContext());
                    thrustLine.setThickness(3);
                    thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));

                    final V3DColorElement group = new V3DColorElement(thrustLine, V3DColor.fushia);

                    final Part part = component.getFirstPart();

                    addElement(new AnimatedElement(this) {

                        @Override
                        public void update() {
                            group.setTransformMatrix(part.getTransform().toFloatBuffer());
                        }

                        @Override
                        public V3DElement getV3DElement() {
                            return group;
                        }

                    });

                    thrustLines.add(new ImmutablePair<LinearEngineCapacity, V3DLine>((LinearEngineCapacity) capacity, thrustLine));
                }

            }

        }

        cameraController.setFollowed(ship.getComponentByName("kernel").getFirstPart());

        activeCamera.fitAll();
    }

    protected GraphicalElement addObject(final WorldObject object) {

        Skin skin = null;

        if (object.getSkin().isEmpty()) {
            System.err.println("generic skin");
            skin = new GenericSkin(this, object);
        } else {
            if (object.getSkin().equals("big_propeller")) {
                skin = new PropellerSkin(this, (Component) object);
            } else if (object.getSkin().equals("pvcell")) {
                skin = new PvCellSkin(this, (Component) object);
            } else if (object.getSkin().equals("camera")) {
                skin = new CameraSkin(this, (Component) object);
            } else if (object.getSkin().equals("reactor")) {
                skin = new ReactorSkin(this, (Component) object);
            } else if (object.getSkin().equals("tank")) {
                skin = new TankSkin(this, (Component) object);
            } else if (object.getSkin().equals("factory")) {
                skin = new FactorySkin(this, (Component) object);
            } else if (object.getSkin().equals("hangar")) {
                skin = new FactorySkin(this, (Component) object);
            } else if (object.getSkin().equals("harvester")) {
                skin = new FactorySkin(this, (Component) object);
            } else if (object.getSkin().equals("refinery")) {
                skin = new FactorySkin(this, (Component) object);
            } else if (object.getSkin().equals("kernel")) {
                skin = new CameraSkin(this, (Component) object);
            } else if (object.getSkin().equals("wing")) {
                skin = new WingSkin(this, (Component) object);
            } else if (object.getSkin().equals("hull")) {
                skin = new HullSkin(this, (Component) object);
            } else if (object.getSkin().equals("thrusterBlock")) {
                skin = new ThrusterBlockSkin(this, (Component) object);
            } else if (object.getSkin().equals("gun")) {
                skin = new GunSkin(this, (Component) object);
            } else if (object.getSkin().equals("asteroid")) {
                skin = new AsteroidSkin(this, (CelestialObject) object);

                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
                guiTrackingArrow.setColor(new V3DColor(255, 0, 0, 0.8f));
                addPersistantGuiElement(guiTrackingArrow);
                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);

            } else if (object.getSkin().equals("monolith")) {
                skin = new MonolithSkin(this, (CelestialObject) object);

                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
                guiTrackingArrow.setColor(new V3DColor(88, 9, 168, 0.8f));
                addPersistantGuiElement(guiTrackingArrow);
                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);
            } else if (object.getSkin().equals("loot")) {
                skin = new LootSkin(this, (CelestialObject) object);

                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
                guiTrackingArrow.setColor(new V3DColor(32, 200, 32, 0.8f));
                addPersistantGuiElement(guiTrackingArrow);
                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);
            }else {
                System.err.println("No skin found for: " + object.getSkin());
                skin = new GenericSkin(this, object);
            }
        }

        addElement(skin);
        return skin;
    }

    private void addBullet(Vec3 from, Vec3 to) {
        BulletEffect bulletEffect = new BulletEffect(this, from, to);
        addElement(bulletEffect);
    }

    public void addElement(GraphicalElement graphicalElement) {
        elementList.add(graphicalElement);
        if (graphicalElement.isAnimated()) {
            animatedList.add(graphicalElement);
        }
        if (graphicalElement.isDisplayable()) {
            scene.add(graphicalElement.getV3DElement());
        }
    }

    public void destroyElement(GraphicalElement graphicalElement) {
        elementList.remove(graphicalElement);
        if (graphicalElement.isAnimated()) {
            animatedList.remove(graphicalElement);
        }
        if (graphicalElement.isDisplayable()) {
            scene.remove(graphicalElement.getV3DElement());
        }
        if (graphicalElement instanceof GuiAnimatedElement) {
            GuiAnimatedElement guiElement = (GuiAnimatedElement) graphicalElement;
            persistantGuiElementList.remove(guiElement);
            removeGuiComponent(guiElement.getGuiElement(), guiElement.getLayer());
        }

    }

    public void addPersistantGuiElement(GuiAnimatedElement graphicalElement) {
        elementList.add(graphicalElement);
        animatedList.add(graphicalElement);
        persistantGuiElementList.add(graphicalElement);
        addGuiComponent(graphicalElement.getGuiElement(), graphicalElement.getLayer());
    }

    public void addGuiComponent(V3DGuiComponent component) {
        interfaceLayer.add(component);
    }

    public void addGuiComponent(V3DGuiComponent component, GuiLayer layer) {
        switch (layer) {
            case HUD:
                hudLayer.add(component);
                break;
            case INTEFACE:
                interfaceLayer.add(component);
                break;
            case MAIN_MENU:
                mainMenuLayer.add(component);
                break;
            default:
                break;
        }

    }

    public void removeGuiComponent(V3DGuiComponent component) {
        interfaceLayer.remove(component);
    }

    public void removeGuiComponent(V3DGuiComponent component, GuiLayer layer) {
        switch (layer) {
            case HUD:
                hudLayer.remove(component);
                break;
            case INTEFACE:
                interfaceLayer.remove(component);
                break;
            case MAIN_MENU:
                mainMenuLayer.remove(component);
                break;
            default:
                break;
        }
    }

    public void resetGui() {
        System.err.println("reset GUI");
        interfaceLayer.removeAll();
        hudLayer.removeAll();
        mainMenuLayer.removeAll();

        for (GraphicalElement element : guiAnimatedList) {
            element.destroy();
        }

        reloadGui();
    }

    private final class WorldRendererEventVisitor extends DefaultEngineEventVisitor {

        @Override
        public void visit(CelestialObjectAddedEvent event) {

            addCelestialObject(event.getObject());
        }

        @Override
        public void visit(CelestialObjectRemovedEvent event) {
            removeCelestialObject(event.getObject());
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            addShip(event.getShip());
        }

        @Override
        public void visit(CollisionEvent event) {
            V3DPoint point = new V3DPoint(engine.getV3DContext());
            point.setPosition(event.getCollisionDescriptor().getGlobalPosition().toV3DVect3());
            point.setSize(5f);
            scene.add(new V3DColorElement(point, new V3DColor((int) event.getCollisionDescriptor().getImpulse() * 10,
                                                              50,
                                                              255 - (int) event.getCollisionDescriptor().getImpulse() * 10)));
        }

        @Override
        public void visit(BulletFiredEvent event) {
            addBullet(event.getFrom(), event.getTo());
        }

        @Override
        public void visit(AddGuiComponentEvent event) {
            hudLayer.add(event.getComponent());
        }

        @Override
        public void visit(RemoveGuiComponentEvent event) {
            hudLayer.remove(event.getComponent());
        }

        @Override
        public void visit(NextWaveEvent event) {
            lastWaveEvent = event;
            if (waveCountText != null) {
                waveCountText.setText("Wave " + event.getWaveId());
            }
        }

        @Override
        public void visit(DamageEvent event) {

            if (event.getTarget().getParentObject() instanceof Monolith) {
                updateMonolithStatus();
            }

        }
        
        @Override
        public void visit(MoneyChangedEvent event) {
            if(moneyText != null) {
                moneyText.setText(LoginManager.localPlayer.getMoney()+" $");
            }
        }
        
        @Override
        public void visit(UpgradeStateChanged event) {
            upgradeMenu.refresh();
            inventoryMenu.refresh();
        }

        
        @Override
        public void visit(KeyPressedEvent event) {
            if(event.getKeyCode() == Keyboard.KEY_ESCAPE) {
                disableInventoryMenu();
                disableUpgradeMenu();
            } else if(event.getKeyCode() == Keyboard.KEY_TAB) {
                if(upgradeMenuEnabled || inventoryMenuEnabled) {
                    disableInventoryMenu();
                    disableUpgradeMenu();
                } else {
                    enabledInventoryMenu();
                    enabledUpgradeMenu();
                }
                    
            } else if(event.getKeyCode() == Keyboard.KEY_I) {
                toogleInventoryMenu();
            } else if(event.getKeyCode() == Keyboard.KEY_U) {
                toogleUpgradeMenu();
            } 
            
        }
    }

    @Override
    public V3DCameraBinding getCameraBinding() {
        return fullscreenBinding;
    }

    @Override
    public EngineEventVisitor getEventVisitor() {
        return new WorldRendererEventVisitor();
    }

    public GraphicEngine getEngine() {
        return engine;
    }

}
