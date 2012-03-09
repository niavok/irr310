package com.irr310.client.graphics.gui;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiXAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;

public abstract class UpgradeTab implements GuiTab{

    private V3DContainer labelPane;
    private V3DLabel weaponTabText;
    private V3DGuiRectangle weaponTabBox;
    private V3DLabel tabTextCount;

    public UpgradeTab(String name) {

    
        labelPane = new V3DContainer();
        labelPane.setSize(130, 40);
        
        
        weaponTabBox = new V3DGuiRectangle();
        weaponTabBox.setyAlignment(GuiYAlignment.TOP);
        weaponTabBox.setPosition(0, 2);
        weaponTabBox.setSize(130, 40);
        weaponTabBox.setBorderWidth(2);
        weaponTabBox.setFillColor(GuiConstants.irrFill);
        weaponTabBox.setBorderColor(GuiConstants.irrGreen);
        labelPane.add(weaponTabBox);
        
        weaponTabText = new V3DLabel(name);
        weaponTabText.setyAlignment(GuiYAlignment.TOP);
        weaponTabText.setPosition(25, 12);
        weaponTabText.setFontStyle("Ubuntu", "bold", 18);
        weaponTabText.setColor(GuiConstants.irrGreen, V3DColor.transparent);
        labelPane.add(weaponTabText);
        
        tabTextCount = new V3DLabel("12");
        tabTextCount.setyAlignment(GuiYAlignment.TOP);
        tabTextCount.setxAlignment(GuiXAlignment.RIGHT);
        tabTextCount.setPosition(10, 22);
        tabTextCount.setFontStyle("Ubuntu", "", 10);
        tabTextCount.setColor(V3DColor.darkgrey, V3DColor.transparent);
        labelPane.add(tabTextCount);
    }
    
    @Override
    public V3DContainer getLabelPane() {
        return labelPane;
    }

    @Override
    public void setActive(boolean active) {
        if(active) {
            weaponTabBox.setFillColor(GuiConstants.irrGreen);
            weaponTabText.setColor(V3DColor.white, V3DColor.transparent);
            tabTextCount.setColor(V3DColor.lightgrey, V3DColor.transparent);
        } else {
            weaponTabBox.setFillColor(GuiConstants.irrFill);
            weaponTabText.setColor(GuiConstants.irrGreen, V3DColor.transparent);
            tabTextCount.setColor(V3DColor.darkgrey, V3DColor.transparent);
        }
    }

}
