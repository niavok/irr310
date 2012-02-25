package com.irr310.client.graphics;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.ARBShaderObjects;

import com.irr310.client.graphics.effects.BulletEffect;
import com.irr310.client.graphics.skin.AsteroidSkin;
import com.irr310.client.graphics.skin.CameraSkin;
import com.irr310.client.graphics.skin.FactorySkin;
import com.irr310.client.graphics.skin.GenericSkin;
import com.irr310.client.graphics.skin.GunSkin;
import com.irr310.client.graphics.skin.HullSkin;
import com.irr310.client.graphics.skin.MonolithSkin;
import com.irr310.client.graphics.skin.PropellerSkin;
import com.irr310.client.graphics.skin.PvCellSkin;
import com.irr310.client.graphics.skin.ReactorSkin;
import com.irr310.client.graphics.skin.Skin;
import com.irr310.client.graphics.skin.TankSkin;
import com.irr310.client.graphics.skin.ThrusterBlockSkin;
import com.irr310.client.graphics.skin.WingSkin;
import com.irr310.common.Game;
import com.irr310.common.event.BulletFiredEvent;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.CelestialObject;
import com.irr310.common.world.Component;
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
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;

public class WorldRenderer implements GraphicRenderer {
    V3DCameraBinding fullscreenBinding;
    private V3DScene scene;
    private List<Pair<LinearEngineCapacity, V3DLine>> thrustLines;
    private List<GraphicalElement> animatedList = new CopyOnWriteArrayList<GraphicalElement>();
    private List<GraphicalElement> elementList = new CopyOnWriteArrayList<GraphicalElement>();

    private V3DFollow3DCameraController cameraController;
    private Map<WorldObject, GraphicalElement> worldObjectToV3DElementMap = new HashMap<WorldObject, GraphicalElement>();

    V3DEye3DCamera activeCamera;
    private final GraphicEngine engine;

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

        loadCurrentWorld();

        // activeCamera.setShowCenter(true);

        activeCamera.fitAll();

        // activeCamera.fit(new V3DVect3(0, 0, 0), new V3DVect3(5, 5, 5));

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
            private int inputRotation;
            private int resolution;
            private int time;
            private long startTime;

            protected void loadUniforms() {
                inputRotation = ARBShaderObjects.glGetUniformLocationARB(shader, "inputRotation");
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

        GraphicalElement element = addObject(object);
        worldObjectToV3DElementMap.put(object, element);
    }

    protected void removeCelestialObject(final CelestialObject object) {
        GraphicalElement element = worldObjectToV3DElementMap.get(object);
        if (element != null) {
            element.destroy();
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
            } else if (object.getSkin().equals("monolith")) {
                skin = new MonolithSkin(this, (CelestialObject) object);
            } else {
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

    }

    public void addGuiComponent(V3DGuiComponent component) {
        fullscreenBinding.getGui().add(component);
    }

    public void removeGuiComponent(V3DGuiComponent component) {
        fullscreenBinding.getGui().remove(component);
    }

    public void resetGui() {
        fullscreenBinding.getGui().clear();
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
