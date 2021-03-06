package com.irr310.client.graphics.ether.activities.shipcamera;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.client.graphics.skin.GenericCelestialObjectSkin;
import com.irr310.client.graphics.skin.GenericComponentSkin;
import com.irr310.common.world.system.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.irr310.client.graphics.GraphicalElement;
import com.irr310.client.graphics.skin.Skin;
import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.input.I3dMouseEvent.Action;
import com.irr310.i3d.scene.I3dEye3DCamera;
import com.irr310.i3d.scene.I3dScene;
import com.irr310.i3d.scene.controller.I3dFollow3DCameraController;
import com.irr310.i3d.scene.element.I3dElement;
import com.irr310.i3d.scene.element.I3dGroupElement;
import com.irr310.i3d.utils.I3dColor;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.View;
import com.irr310.server.engine.system.SystemEngine;

import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent.KeyAction;
import fr.def.iss.vd2.lib_v3d.V3DShader;
import fr.def.iss.vd2.lib_v3d.V3DVect3;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.element.V3DColorElement;
import fr.def.iss.vd2.lib_v3d.element.V3DLine;
import fr.def.iss.vd2.lib_v3d.element.V3DShaderElement;
import fr.def.iss.vd2.lib_v3d.element.V3DrawElement;

public class ShipCameraView extends View {

    private Ship focusedShip;
    private I3dEye3DCamera activeCamera;
    private I3dFollow3DCameraController mCameraController;
    private List<GraphicalElement> animatedList = new CopyOnWriteArrayList<GraphicalElement>();
    private V3DCameraBinding fullscreenBinding;
    private I3dScene scene;
    private V3DCameraBinding binding;
    private Map<Component, List<GraphicalElement>> componentToV3DElementMap = new HashMap<Component, List<GraphicalElement>>();
    private Map<CelestialObject, List<GraphicalElement>> celestialObjectToV3DElementMap = new HashMap<CelestialObject, List<GraphicalElement>>();
    private WorldSystem mSystem;
    private ControlMode mControlMode = ControlMode.PILOT;
    

    private enum ControlMode {
        PILOT,
        CAMERA,
    }
    
    public ShipCameraView(Ship ship, SystemEngine systemEngine) {
        this.focusedShip = ship;
        this.mSystem = systemEngine.getSystem();
        
        init();
        binding = new V3DCameraBinding();
        reshape();
        
        setOnKeyListener(new OnKeyEventListener() {

            @Override
            public boolean onKeyEvent(V3DKeyEvent keyEvent) {
                
                if(keyEvent.getAction() == KeyAction.KEY_PRESSED) {
                    if( keyEvent.getKeyCode() == Keyboard.KEY_C) {
                        mControlMode = ControlMode.CAMERA; 
                    } else if( keyEvent.getKeyCode() == Keyboard.KEY_V) {
                        mControlMode = ControlMode.PILOT; 
                    } 
                }
                Log.log("key char="+keyEvent.getCharacter()+ " code="+keyEvent.getKeyCode());
                
                return false;
            }
        });
        
        setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
                Log.log("camera action="+mouseEvent.getAction());
                if(mControlMode == ControlMode.CAMERA) {
                    mCameraController.onEvent(mouseEvent);
                    return true;
                } else if(mControlMode == ControlMode.PILOT) {
                    if(mouseEvent.getAction() == Action.MOUSE_CLICKED && mouseEvent.getButton() == 0) {
//                        mBaseMouseX = mouseEvent.getX();
//                        mBaseMouseY = mouseEvent.getY();
//                        mSteering = false;
                    }
                }
                return false;
            }
        });
        
    }

    private void reshape() {
        
        mLayoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        mLayoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
    }
        
        

    @Override
    public void onDraw(Graphics g) {
        
        
     // amination
        for (GraphicalElement animated : animatedList) {
            animated.update(g.getTime());
        }
        
        mCameraController.update(g.getTime());
        
        GL11.glColor3f(1, 0, 0);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(0, 0, 0);
        GL11.glVertex3d(100, 100, 100);
        GL11.glEnd();
        
        
        GL11.glViewport(binding.x, binding.y, binding.width, binding.height);

        //Clean Background
        I3dColor color = activeCamera.getBackgroundColor();
        color = I3dColor.azure;
        GL11.glClearColor(color.r, color.g, color.b, color.a);

        GL11.glScissor(binding.x, binding.y, binding.width, binding.height);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        if (color.a == 1.0f) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        } else {
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glOrtho(0, binding.width, 0, binding.height, -2000.0, 2000.0);

            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(color.r, color.g, color.b, color.a);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex3f(0, 0, 0);
            GL11.glVertex3f(binding.width, 0, 0);
            GL11.glVertex3f(binding.width, binding.height, 0);
            GL11.glVertex3f(0, binding.height, 0);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        activeCamera.display( binding.width, binding.height);


//        if (select && binding == focusCamera) {
//            GL11.glMatrixMode(GL11.GL_PROJECTION);
//            GL11.glLoadIdentity();
//            //glu.gluPerspective(45.0f, h, 0.1, 2000.0);
//            GL11.glMatrixMode(GL11.GL_MODELVIEW);
//            GL11.glLoadIdentity();
//            binding.camera.select( mouseX, mouseY);
//            context.setMouseOverCameraBinding(binding);
//        }
//        
        
        
        
        
        
        
//        activeCamera.display(1000,1000);
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        binding.x = (int) l;
        binding.y = (int) t;
        binding.width = (int) (r-t);
        binding.height = (int) (b-t);
    }

    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
        // TODO Auto-generated method stub

    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

private void init() {
        
        
        activeCamera = new I3dEye3DCamera();

        mCameraController = new I3dFollow3DCameraController(activeCamera);
        configureDefaultCamera();
        animatedList.add(mCameraController);

        fullscreenBinding = V3DCameraBinding.buildFullscreenCamera(activeCamera);
        activeCamera.setBackgroundColor(I3dColor.white);

        // Add zoom and pane camera controlleur
        // cameraController.setLimitBound(false);

        scene = new I3dScene();
        activeCamera.setScene(scene);
        
        Component kernel = focusedShip.getComponentByKey("kernel");
        mCameraController.setFollowed(new FollowablePart(kernel.getFirstPart()));

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
        I3dElement ref0 = generateReference();
        ref0.setPosition(0, 0, 0);
        I3dElement ref1 = generateReference();
        ref1.setPosition(1, 0, 0);
        I3dElement ref2 = generateReference();
        ref2.setPosition(2, 0, 0);

        scene.add(ref0);
        scene.add(ref1);
        scene.add(ref2);
        
        addShip(focusedShip);

        for (CelestialObject celestialObject : mSystem.getCelestialObjects()) {
            addCelestialObject(celestialObject);
        }


//        generateGuiStructure();

//        loadCurrentWorld();

        // activeCamera.setShowCenter(true);

        activeCamera.fitAll();        
    }
    
    private void configureDefaultCamera() {
        mCameraController.configure(10,-2,-10, 2);
    }
    
    private void createBubble() {

      
        
      File v3drawFileStructure = new File("graphics/output/bubble.v3draw");
      V3DrawElement bubbleElement = V3DrawElement.LoadFromFile(v3drawFileStructure);
      // elementStructure.setShader("bubble");
      bubbleElement.setScale((float) mSystem.getRadius());

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
              ARBShaderObjects.glUniform2fARB(resolution, mCameraController.getCamera().getCurrentWidth(), mCameraController.getCamera()
                                                                                                                          .getCurrentHeight());
              float time2 = ((float) (new Date().getTime() - startTime)) / 10000.0f;
              ARBShaderObjects.glUniform1fARB(time, time2);

          }
      };

      scene.add(new V3DColorElement(new V3DShaderElement(bubbleElement, shader), new I3dColor(255, 255, 255)));
  }
    
    private I3dElement generateReference() {
        V3DLine xAxis = new V3DLine();
        xAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(10000, 0, 0));

        V3DLine yAxis = new V3DLine();
        yAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 10000, 0));

        V3DLine zAxis = new V3DLine();
        zAxis.setLocation(new V3DVect3(0, 0, 0), new V3DVect3(0, 0, 10000));

        I3dGroupElement group = new I3dGroupElement();

        group.add(new V3DColorElement(xAxis, I3dColor.red));
        group.add(new V3DColorElement(yAxis, I3dColor.green));
        group.add(new V3DColorElement(zAxis, I3dColor.blue));
        return group;
    }
    
    protected void addShip(final Ship ship) {

        for (Component component : ship.getComponents()) {
            addComponent(component);
        }

//        if(LoginManager.getLocalPlayer().equals(ship.getOwner())) {
//            activeCamera.fitAll();
//        } else {
//            Component kernel = ship.getComponentByName("kernel");
//            GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, kernel.getFirstPart());
//            guiTrackingArrow.setColor(V3DColor.fromI3d(ship.getOwner().getColor().copy().setAlpha(0.8f)));
//            addPersistantGuiElement(guiTrackingArrow);
//            worldObjectToV3DElementMap.get(kernel).add(guiTrackingArrow);    
//        }
    }
    
    private void addComponent(Component component) {
        if(componentToV3DElementMap.get(component) != null) {
            return;
        }
        GraphicalElement graphicalElement = addObject(component);
        componentToV3DElementMap.put(component, new ArrayList<GraphicalElement>());
        componentToV3DElementMap.get(component).add(graphicalElement);
    }

    private void addCelestialObject(CelestialObject celestialObject) {
        if(componentToV3DElementMap.get(celestialObject) != null) {
            return;
        }
        GraphicalElement graphicalElement = addObject(celestialObject);
        celestialObjectToV3DElementMap.put(celestialObject, new ArrayList<GraphicalElement>());
        celestialObjectToV3DElementMap.get(celestialObject).add(graphicalElement);
    }

    protected GraphicalElement addObject(final CelestialObject object) {
        Skin skin = null;
        skin = new GenericCelestialObjectSkin(object);

        addElement(skin);
        return skin;
    }
    
    protected GraphicalElement addObject(final Component object) {

        Skin skin = null;

//        if (object.getSkin().isEmpty()) {
            System.err.println("generic skin");
            skin = new GenericComponentSkin(object);
//        } else {
//            if (object.getSkin().equals("big_propeller")) {
//                skin = new PropellerSkin(this, (Component) object);
//            } else if (object.getSkin().equals("pvcell")) {
//                skin = new PvCellSkin(this, (Component) object);
//            } else if (object.getSkin().equals("camera")) {
//                skin = new CameraSkin(this, (Component) object);
//            } else if (object.getSkin().equals("reactor")) {
//                skin = new ReactorSkin(this, (Component) object);
//            } else if (object.getSkin().equals("tank")) {
//                skin = new TankSkin(this, (Component) object);
//            } else if (object.getSkin().equals("factory")) {
//                skin = new FactorySkin(this, (Component) object);
//            } else if (object.getSkin().equals("hangar")) {
//                skin = new FactorySkin(this, (Component) object);
//            } else if (object.getSkin().equals("harvester")) {
//                skin = new FactorySkin(this, (Component) object);
//            } else if (object.getSkin().equals("refinery")) {
//                skin = new FactorySkin(this, (Component) object);
//            } else if (object.getSkin().equals("kernel")) {
//                skin = new CameraSkin(this, (Component) object);
//            } else if (object.getSkin().equals("wing")) {
//                skin = new WingSkin(this, (Component) object);
//            } else if (object.getSkin().equals("hull")) {
//                skin = new HullSkin(this, (Component) object);
//            } else if (object.getSkin().equals("thrusterBlock")) {
//                skin = new ThrusterBlockSkin(this, (Component) object);
//            } else if (object.getSkin().equals("gun")) {
//                skin = new WeaponSkin(this, (Component) object);
//            } else if (object.getSkin().equals("rocket_hull")) {
//                skin = new RocketSkin(this, (Component) object);
//            } else if (object.getSkin().equals("asteroid")) {
//                skin = new AsteroidSkin(this, (CelestialObject) object);
//
//                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
//                guiTrackingArrow.setColor(new V3DColor(255, 0, 0, 0.8f));
//                addPersistantGuiElement(guiTrackingArrow);
//                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);
//
//            } else if (object.getSkin().equals("monolith")) {
//                skin = new MonolithSkin(this, (CelestialObject) object);
//
//                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
//                guiTrackingArrow.setColor(new V3DColor(88, 9, 168, 0.8f));
//                addPersistantGuiElement(guiTrackingArrow);
//                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);
//            } else if (object.getSkin().equals("loot")) {
//                skin = new LootSkin(this, (CelestialObject) object);
//
//                GuiTrackingArrow guiTrackingArrow = new GuiTrackingArrow(this, cameraController, object.getFirstPart());
//                guiTrackingArrow.setColor(new V3DColor(32, 200, 32, 0.8f));
//                addPersistantGuiElement(guiTrackingArrow);
//                worldObjectToV3DElementMap.get(object).add(guiTrackingArrow);
//            } else {
//                System.err.println("No skin found for: " + object.getSkin());
//                skin = new GenericSkin(this, object);
//            }
//        }
//
        addElement(skin);
        return skin;
    }
    
    public void addElement(GraphicalElement graphicalElement) {
//        elementList.add(graphicalElement);
        if (graphicalElement.isAnimated()) {
            animatedList.add(graphicalElement);
        }
        if (graphicalElement.isDisplayable()) {
            scene.add(graphicalElement.getV3DElement());
        }
    }
    
}
