package com.irr310.client.graphics;

import org.fenggui.event.ActivationEvent;
import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.FocusEvent;
import org.fenggui.event.IActivationListener;
import org.fenggui.event.IButtonPressedListener;
import org.fenggui.event.IFocusListener;
import org.fenggui.event.mouse.IMouseEnteredListener;
import org.fenggui.event.mouse.MouseEnteredEvent;

import com.irr310.client.GameClient;
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

public class LoadingGraphicRenderer implements GraphicRenderer {

    private static final V3DColor irrRed = new V3DColor(108, 0, 0);
    private static final V3DColor irrRedHover = new V3DColor(118, 10, 10);
    private V3DSimple2DCamera camera;
    private final GraphicEngine engine;
    private V3DCameraBinding cameraBinding;
    private V3DLabel logoIRR;
    private V3DLabel logo310;
    private V3DLabel loadingMessage;

    public LoadingGraphicRenderer(GraphicEngine engine) {
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
        
        V3DLabel loadingText = new V3DLabel("Loading");
        loadingText.setFontStyle("Ubuntu", "bold", 45);
        loadingText.setColor(irrRed, V3DColor.transparent);
        loadingText.setPosition(300, 400);
        addGuiComponent(loadingText);
        
        loadingMessage = new V3DLabel("");
        loadingMessage.setFontStyle("Ubuntu", "bold", 25);
        loadingMessage.setColor(V3DColor.darkgrey, V3DColor.transparent);
        loadingMessage.setPosition(300, 450);
        addGuiComponent(loadingMessage);
        

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

    public void setMessage(String message) {
        loadingMessage.setText(message);
    }

}
