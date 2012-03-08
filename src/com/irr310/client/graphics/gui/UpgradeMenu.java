package com.irr310.client.graphics.gui;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;

public class UpgradeMenu extends V3DContainer{

    int tabOffset = 40;
    
    public UpgradeMenu() {
        setPosition(-2, 123);
        setSize(500, 600);
        setyAlignment(GuiYAlignment.BOTTOM);

        V3DGuiRectangle upgradeBase = new V3DGuiRectangle();
        upgradeBase.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeBase.setPosition(0, 0);
        upgradeBase.setSize(500, 600);
        upgradeBase.setBorderWidth(4);
        upgradeBase.setFillColor(GuiConstants.irrFill);
        upgradeBase.setBorderColor(GuiConstants.irrGreen);
        add(upgradeBase);

        V3DGuiRectangle upgradeTop = new V3DGuiRectangle();
        upgradeTop.setyAlignment(GuiYAlignment.BOTTOM);
        upgradeTop.setPosition(0, 0);
        upgradeTop.setBorderWidth(4);
        upgradeTop.setSize(500, 30);
        upgradeTop.setFillColor(GuiConstants.irrGreen);
        upgradeTop.setBorderColor(GuiConstants.irrGreen);
        
        
        //Tabs
        
        
        addTab("Weapon");
        addTab("Defenses");
        addTab("Ship");
        addTab("Monolith");
        addTab("Tools");
        
        //Tab content
        V3DGuiRectangle tabContent= new V3DGuiRectangle();
        tabContent.setyAlignment(GuiYAlignment.TOP);
        tabContent.setPosition(140, 20);
        tabContent.setSize(340, 530);
        tabContent.setBorderWidth(2);
        tabContent.setFillColor(GuiConstants.irrFill);
        tabContent.setBorderColor(GuiConstants.irrGreen);
        add(tabContent);
        
        
        add(upgradeTop);
    }

    private void addTab(String string) {
        V3DGuiRectangle weaponTabBox = new V3DGuiRectangle();
        weaponTabBox.setyAlignment(GuiYAlignment.TOP);
        weaponTabBox.setPosition(10, tabOffset+2);
        weaponTabBox.setSize(130, 40);
        weaponTabBox.setBorderWidth(2);
        weaponTabBox.setFillColor(GuiConstants.irrFill);
        weaponTabBox.setBorderColor(GuiConstants.irrGreen);
        add(weaponTabBox);
        
        V3DLabel weaponTabText = new V3DLabel(string);
        weaponTabText.setyAlignment(GuiYAlignment.TOP);
        weaponTabText.setPosition(25, tabOffset+12);
        weaponTabText.setFontStyle("Ubuntu", "bold", 18);
        weaponTabText.setColor(GuiConstants.irrGreen, V3DColor.transparent);
        add(weaponTabText);
        
        V3DLabel tabTextCount = new V3DLabel("12");
        tabTextCount.setyAlignment(GuiYAlignment.TOP);
        tabTextCount.setxAlignment(GuiXAlignment.LEFT);
        tabTextCount.setPosition(120, tabOffset+22);
        tabTextCount.setFontStyle("Ubuntu", "", 10);
        tabTextCount.setColor(V3DColor.darkgrey, V3DColor.transparent);
        add(tabTextCount);
        
        tabOffset+= 50;
    }
    
    
    
}
