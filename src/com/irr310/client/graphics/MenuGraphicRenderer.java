package com.irr310.client.graphics;

import org.fenggui.event.ActivationEvent;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IActivationListener;
import org.fenggui.event.IButtonPressedListener;

import com.irr310.common.Game;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEventVisitor;
import com.irr310.common.event.QuitGameEvent;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.camera.V3DCameraBinding;
import fr.def.iss.vd2.lib_v3d.camera.V3DSimple2DCamera;
import fr.def.iss.vd2.lib_v3d.gui.V3DButton;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiXAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;

public class MenuGraphicRenderer implements GraphicRenderer {

    private static final V3DColor irrRed = new V3DColor(108, 0, 0);
    private V3DSimple2DCamera camera;
    private final GraphicEngine engine;
    private V3DCameraBinding cameraBinding;
    private V3DLabel logoIRR;
    private V3DLabel logo310;

    public MenuGraphicRenderer(GraphicEngine engine) {
        this.engine = engine;
    }

    @Override
    public void init() {
        camera = new V3DSimple2DCamera(engine.getV3DContext());
        camera.setBackgroundColor(V3DColor.white);
        cameraBinding = V3DCameraBinding.buildFullscreenCamera(camera);

        
        logoIRR = new V3DLabel("IRR");
        logoIRR.setFontStyle("Ubuntu", "bold", 55);
        logoIRR.setColor(irrRed, V3DColor.transparent);
        logoIRR.setPosition(150, 150);
        addGuiComponent(logoIRR);
        
        
        logo310 = new V3DLabel("310");
        logo310.setFontStyle("Ubuntu", "bold", 55);
        logo310.setColor(V3DColor.black, V3DColor.transparent);
        logo310.setPosition(238, 150);
        addGuiComponent(logo310);
        
        
        
        V3DButton exitButton = new V3DButton("Exit");
        exitButton.setFontStyle("Ubuntu", "bold", 16);
        exitButton.setColor(V3DColor.white, irrRed);
        //exitButton.setPosition( engine.getViewportSize().x.intValue() - 10,(int) engine.getViewportSize().y.intValue());
        exitButton.setxAlignment(GuiXAlignment.RIGHT);
        exitButton.setyAlignment(GuiYAlignment.BOTTOM);
        exitButton.setPadding(5,40,40,5);
        exitButton.setPosition( 20,20);
        exitButton.getFenGUIWidget().addButtonPressedListener(new IButtonPressedListener() {
            
            @Override
            public void buttonPressed(ButtonPressedEvent e) {
                Game.getInstance().sendToAll(new QuitGameEvent());
            }
        });
        exitButton.getFenGUIWidget().addActivationListener(new IActivationListener() {
            
            @Override
            public void widgetActivationChanged(ActivationEvent activationEvent) {
                System.err.println("activated");
            }
        });
        addGuiComponent(exitButton);
        

    }

    @Override
    public V3DCameraBinding getCameraBinding() {
        return cameraBinding;
    }

    @Override
    public void frame() {
    }

    @Override
    public EngineEventVisitor getEventVisitor() {
        return new DefaultEngineEventVisitor();
    }

    @Override
    public void addGuiComponent(V3DGuiComponent component) {
        cameraBinding.getGui().add(component);
    }

    @Override
    public void removeGuiComponent(V3DGuiComponent component) {
        cameraBinding.getGui().remove(component);
    }

    @Override
    public void resetGui() {
        cameraBinding.getGui().clear();
    }

}
