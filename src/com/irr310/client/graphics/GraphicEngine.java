package com.irr310.client.graphics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import com.irr310.client.graphics.gui.GuiFpsIndicator;
import com.irr310.client.graphics.gui.GuiSpeedIndicator;
import com.irr310.common.Game;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.KeyPressedEvent;
import com.irr310.common.event.MinimizeWindowEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldObjectAddedEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TransformMatrix.TransformMatrixChangeListener;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.server.Duration;

import fr.def.iss.vd2.lib_v3d.V3DCanvas;
import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.V3DContext;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DScene;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DEye3DCamera;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple3DCamera;
import fr.def.iss.vd2.lib_v3d.controller.V3DFollow3DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DCameraController;
import fr.def.iss.vd2.lib_v3d.controller.V3DSimple3DRotateOnlyCameraController;
import fr.def.iss.vd2.lib_v3d.element.V3DBox;
import fr.def.iss.vd2.lib_v3d.element.V3DBox.RenderMode;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DElement;
import fr.def.iss.vd2.lib_v3d.element.V3DGroupElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

public class GraphicEngine extends FramerateEngine {

    final V3DContext context = new V3DContext();
    V3DCameraBinding fullscreenBinding;
    V3DEye3DCamera activeCamera;
    V3DCanvas canvas;
    private V3DScene scene;
    private V3DGroupElement fitOrder;
    private List<Pair<LinearEngineCapacity, V3DLine>> thrustLines;
    private List<Animated> animatedList = new ArrayList<Animated>();
    private V3DFollow3DCameraController cameraController;

    public GraphicEngine() {
        framerate = new Duration(16666666);
        //framerate = new Duration(1666666);
        thrustLines = new ArrayList<Pair<LinearEngineCapacity, V3DLine>>();

    }

    @Override
    protected void init() {
        canvas = new V3DCanvas(context, 1920, 1200);
        
        fitOrder = null;

        activeCamera = new V3DEye3DCamera(context);

        cameraController = new V3DFollow3DCameraController(activeCamera);
        animatedList.add(cameraController);
        
        
        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);
        activeCamera.setBackgroundColor(V3DColor.white);

        // Add zoom and pane camera controlleur
        // cameraController.setLimitBound(false);

        scene = new V3DScene(context);
        activeCamera.setScene(scene);

        /*V3DBox sky = new V3DBox(context);
        sky.setSize(new V3DVect3(1024, 768, 1));
        sky.setPosition(1024 / 2, 768 / 2, 0);*/

        // activeCamera.getBackgroundScene().add(new V3DColorElement(sky,
        // V3DColor.pink));
        //activeCamera.getBackgroundScene().add(new V3DColorElement(new Sky(context), V3DColor.pink));

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

        // activeCamera.setShowCenter(true);

        activeCamera.fitAll();

        // activeCamera.fit(new V3DVect3(0, 0, 0), new V3DVect3(5, 5, 5));

        canvas.addCamera(fullscreenBinding);
        canvas.setEnabled(true);

        //GUI
        GuiFpsIndicator fpsIndicator = new GuiFpsIndicator(context);
        fullscreenBinding.getGui().add(fpsIndicator);
        fpsIndicator.setPosition(10, 10);
        animatedList.add(fpsIndicator);
    }

    private void createBubble() {
        
        File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
        final V3DrawElement elementStructure = V3DrawElement.LoadFromFile(v3drawFileStructure, context);
        //elementStructure.setShader("bubble");
        elementStructure.setScale(1000);
        scene.add(new V3DColorElement(new V3DShaderElement(elementStructure, "bubble"), new V3DColor(255, 255, 255)));
    }

    private V3DElement generateReference() {
        V3DLine xAxis = new V3DLine(context);
        xAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(1, 0, 0));

        V3DLine yAxis = new V3DLine(context);
        yAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 1, 0));

        V3DLine zAxis = new V3DLine(context);
        zAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 1));

        V3DGroupElement group = new V3DGroupElement(context);

        group.add(new V3DColorElement(xAxis, V3DColor.red));
        group.add(new V3DColorElement(yAxis, V3DColor.green));
        group.add(new V3DColorElement(zAxis, V3DColor.blue));
        return group;
    }

    @Override
    protected void end() {
        Display.destroy();
    }

    protected void addShip(final Ship ship) {

        V3DGroupElement shipElements = new V3DGroupElement(context);
        for (Component component : ship.getComponents()) {
            shipElements.add(addObject(component, true));

            for (Capacity capacity : component.getCapacities()) {
                if (capacity instanceof LinearEngineCapacity) {

                    V3DLine thrustLine = new V3DLine(context);
                    thrustLine.setThickness(3);
                    thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 0));

                    final V3DColorElement group = new V3DColorElement(thrustLine, V3DColor.fushia);

                    scene.add(group);

                    final Part part = component.getFirstPart();
                    part.getTransform().addListener(new TransformMatrixChangeListener() {

                        @Override
                        public void valueChanged() {
                            group.setTransformMatrix(part.getTransform().toFloatBuffer());
                        }
                    });

                    thrustLines.add(new ImmutablePair<LinearEngineCapacity, V3DLine>((LinearEngineCapacity) capacity, thrustLine));
                }

            }

        }
        
        

        fitOrder = shipElements;
        cameraController.setFollowed(ship.getComponentByName("hull").getFirstPart());
        scene.add(shipElements);
        activeCamera.fitAll();

        GuiSpeedIndicator speedIndicator = new GuiSpeedIndicator(context, ship.getComponentByName("kernel").getFirstPart());
        fullscreenBinding.getGui().add(speedIndicator);
        speedIndicator.setPosition(10, 30);
        animatedList.add(speedIndicator);
        
    }

    protected V3DElement addObject(final WorldObject object, boolean inShip) {

        
        Skin skin = null;
        
        if(object.getSkin().isEmpty()) {
            skin = new GenericSkin(context, object);
        } else {
            if(object.getSkin().equals("big_propeller")) {
                skin = new PropellerSkin(context, (Component) object);
            } else if(object.getSkin().equals("pvcell")) {
                skin = new PvCellSkin(context, (Component) object);
            } else if(object.getSkin().equals("camera")) {
                skin = new CameraSkin(context, (Component) object);
            } else if(object.getSkin().equals("tank")) {
                skin = new TankSkin(context, (Component) object);
            } else if(object.getSkin().equals("factory")) {
                skin = new FactorySkin(context, (Component) object);
            } else if(object.getSkin().equals("hangar")) {
                skin = new FactorySkin(context, (Component) object);
            } else if(object.getSkin().equals("harvester")) {
                skin = new FactorySkin(context, (Component) object);
            } else if(object.getSkin().equals("refinery")) {
                skin = new FactorySkin(context, (Component) object);
            } else if(object.getSkin().equals("kernel")) {
                skin = new CameraSkin(context, (Component) object);
            } else {
                System.err.println("No skin found for: "+object.getSkin());
                skin = new GenericSkin(context, object);
            }
        }
        
        skin.bind(scene);
        if(skin.isAnimated()) {
            animatedList.add(skin);
            skin.setFramerate(framerate);
        }
        
        return skin.getElement();
    }

    @Override
    protected void frame() {

        Log.perfBegin("Frame");
        Game.getInstance().getWorld().lock();
        
        Log.perfBegin("amination");
        // amination
        for(Animated animated : animatedList) {
            animated.animate();
        }
        Log.perfEnd();
        
        
        /*Log.perfBegin("fit");
        if (fitOrder == null) {
            activeCamera.fitAll();
        } else {
            activeCamera.fit(fitOrder.getBoundingBox());
        }
        Log.perfEnd();*/
        
        
        Log.perfBegin("Apply forces");
        // Apply forces
        for (Pair<LinearEngineCapacity, V3DLine> thrustLinePair : thrustLines) {
            V3DLine thrustLine = thrustLinePair.getRight();
            LinearEngineCapacity engine = thrustLinePair.getLeft();

            thrustLine.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, -(float) engine.getCurrentThrust(), 0));
        }
        Log.perfEnd();

        Log.perfBegin("draw");
        
        canvas.frame();
        Game.getInstance().getWorld().unlock();
        
        Log.perfEnd();
        
        Log.perfEnd();
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new DebugGraphicEngineEventVisitor());
    }

    private final class DebugGraphicEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping graphic engine");
            canvas.destroy();
            setRunning(false);
        }

        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);
        }

        @Override
        public void visit(WorldObjectAddedEvent event) {
            addObject(event.getObject(), false);
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            addShip(event.getShip());
        }
        
        
        @Override
        public void visit(MinimizeWindowEvent event) {
            canvas.hide();
        }
        
        @Override
        public void visit(KeyPressedEvent event) {
            if(event.getKeyCode() == Keyboard.KEY_F11 && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                System.out.println("Reload shaders");
                context.reloadShader();
                return;
            }
        }

    }

    public void onMouseEvent(V3DMouseEvent mouseEvent) {
        if (canvas != null) {
            canvas.onMouseEvent(mouseEvent);
        }
    }

}
