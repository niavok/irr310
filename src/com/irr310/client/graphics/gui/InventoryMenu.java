package com.irr310.client.graphics.gui;

import java.io.IOException;
import java.util.List;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.graphics.MenuContainer;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.item.ItemSlot;
import com.irr310.common.world.item.ShipSchema;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.element.V3DCircle;
import fr.def.iss.vd2.lib_v3d.element.V3DSprite;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiCircle;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiSprite;

public class InventoryMenu extends MenuContainer {

    private final GraphicEngine engine;

    public InventoryMenu(GraphicEngine engine) {
        this.engine = engine;
        setPosition(-2, 123);
        setSize(500, 600);
        setyAlignment(GuiYAlignment.BOTTOM);
        setxAlignment(GuiXAlignment.RIGHT);

        V3DGuiRectangle inventoryBase = new V3DGuiRectangle();
        inventoryBase.setyAlignment(GuiYAlignment.BOTTOM);
        inventoryBase.setPosition(0, 0);
        inventoryBase.setSize(500, 600);
        inventoryBase.setBorderWidth(4);
        inventoryBase.setFillColor(GuiConstants.irrFill);
        inventoryBase.setBorderColor(GuiConstants.irrRed);
        add(inventoryBase);

        V3DGuiRectangle inventoryTop = new V3DGuiRectangle();
        inventoryTop.setyAlignment(GuiYAlignment.BOTTOM);
        inventoryTop.setPosition(0, 0);
        inventoryTop.setBorderWidth(4);
        inventoryTop.setSize(500, 30);
        inventoryTop.setFillColor(GuiConstants.irrRed);
        inventoryTop.setBorderColor(GuiConstants.irrRed);
        add(inventoryTop);

        generateInventory();

    }

    private void generateInventory() {
        ShipSchema shipSchema = LoginManager.getLocalPlayer().getShipSchema();

        // top wing
        V3DGuiRectangle topWing = new V3DGuiRectangle();
        topWing.setBorderColor(GuiConstants.irrBlue);
        topWing.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        topWing.setPosition(248, 100);
        topWing.setSize(5, 100);
        topWing.setBorderWidth(3);
        add(topWing);

        // left wing
        V3DGuiRectangle leftWing = new V3DGuiRectangle();
        leftWing.setBorderColor(GuiConstants.irrBlue);
        leftWing.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        leftWing.setPosition(100, 250);
        leftWing.setSize(100, 5);
        leftWing.setBorderWidth(3);
        add(leftWing);

        // right wing
        V3DGuiRectangle rightWing = new V3DGuiRectangle();
        rightWing.setBorderColor(GuiConstants.irrBlue);
        rightWing.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        rightWing.setPosition(300, 250);
        rightWing.setSize(100, 5);
        rightWing.setBorderWidth(3);
        add(rightWing);

        // bottom wing
        V3DGuiRectangle bottomWing = new V3DGuiRectangle();
        bottomWing.setBorderColor(GuiConstants.irrBlue);
        bottomWing.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        bottomWing.setPosition(248, 300);
        bottomWing.setSize(5, 100);
        bottomWing.setBorderWidth(3);
        add(bottomWing);

        // Hull
        V3DGuiCircle hull = new V3DGuiCircle();
        hull.setBorderColor(GuiConstants.irrBlue);
        hull.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        hull.setPosition(250, 250);
        hull.setSize(80, 80);
        hull.setQuality(32);
        hull.setBorderWidth(3);
        add(hull);

        // Top engine
        V3DGuiCircle topEngine = new V3DGuiCircle();
        topEngine.setBorderColor(GuiConstants.irrBlue);
        topEngine.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        topEngine.setPosition(250, 70);
        topEngine.setSize(40, 40);
        topEngine.setQuality(32);
        topEngine.setBorderWidth(3);
        add(topEngine);

        // Bottom engine
        V3DGuiCircle bottomEngine = new V3DGuiCircle();
        bottomEngine.setBorderColor(GuiConstants.irrBlue);
        bottomEngine.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        bottomEngine.setPosition(250, 430);
        bottomEngine.setSize(40, 40);
        bottomEngine.setQuality(32);
        bottomEngine.setBorderWidth(3);
        add(bottomEngine);

        // Left engine
        V3DGuiCircle leftEngine = new V3DGuiCircle();
        leftEngine.setBorderColor(GuiConstants.irrBlue);
        leftEngine.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        leftEngine.setPosition(70, 250);
        leftEngine.setSize(40, 40);
        leftEngine.setQuality(32);
        leftEngine.setBorderWidth(3);
        add(leftEngine);

        // Right engine
        V3DGuiCircle rightEngine = new V3DGuiCircle();
        rightEngine.setBorderColor(GuiConstants.irrBlue);
        rightEngine.setFillColor(GuiConstants.irrBlue.copy().setAlpha(0.5f));
        rightEngine.setPosition(430, 250);
        rightEngine.setSize(40, 40);
        rightEngine.setQuality(32);
        rightEngine.setBorderWidth(3);
        add(rightEngine);
        
        
        // Add slots
        for (ItemSlot itemSlot : shipSchema.getItemSlots()) {
            addSlot(itemSlot);
        }
    }

    private void addSlot(ItemSlot itemSlot) {
        double scale = 41;
         
        V3DGuiRectangle slot = new V3DGuiRectangle();
        slot.setBorderColor(V3DColor.grey);
        slot.setFillColor(V3DColor.grey.copy().setAlpha(0.5f));
        int x2 = (int) (250-(int) (scale/2)+scale*itemSlot.getPosition().x.doubleValue());
        int y2 = 250-(int) (scale/2)+ (int)(scale *itemSlot.getPosition().z.doubleValue());
        slot.setPosition(x2, y2);
        slot.setSize((int)scale-3,(int)scale-3);
        slot.setBorderWidth(3);
        add(slot);
        
        
        try {
            V3DGuiSprite icon = new V3DGuiSprite(engine.getV3DContext(),"graphics/icons/gun_item_icon_64.png");
            icon.setPosition(x2, y2);
            icon.setSize((int)scale-4,(int)scale-4);
            add(icon);
            
        } catch (IOException e) {
            System.err.println("Fail to load sprite: graphics/icons/gun_item_icon_64.png");
        }
        
    }

    @Override
    public void refresh() {
    }

}
