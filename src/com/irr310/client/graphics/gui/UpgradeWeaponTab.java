package com.irr310.client.graphics.gui;

import java.util.List;
import java.util.Map;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.UpgradeOwnership;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;


public class UpgradeWeaponTab extends UpgradeTab{

    private V3DContainer root;
    private List<UpgradeOwnership> upgradesOwnership;

    public UpgradeWeaponTab() {
        super("Weapon");
        
        upgradesOwnership = LoginManager.localPlayer.getUpgrades();
        Map<String, Upgrade> availableUpgrades = Game.getInstance().getWorld().getAvailableUpgrades();
        root = new V3DContainer();
        
        int y = 10;
        
        for(Upgrade upgrade: availableUpgrades.values()) {
            V3DGuiComponent upgradePane = generateUpgradePane(upgrade);
            upgradePane.setPosition(10, y);
            root.add(upgradePane);
            y += 110;
        }
        
        
    }
    
    private V3DGuiComponent generateUpgradePane(Upgrade upgrade) {
        V3DContainer pane = new V3DContainer();

        V3DGuiRectangle upgradeRect = new V3DGuiRectangle();
        upgradeRect.setyAlignment(GuiYAlignment.TOP);
        upgradeRect.setPosition(0, 0);
        upgradeRect.setBorderWidth(2);
        upgradeRect.setSize(300, 60);
        upgradeRect.setFillColor(V3DColor.transparent);
        upgradeRect.setBorderColor(GuiConstants.irrGreen);
        pane.add(upgradeRect);
        
        V3DLabel upgradeName = new V3DLabel(upgrade.getName());
        upgradeName.setFontStyle("Ubuntu", "bold", 16);
        upgradeName.setColor(V3DColor.black, V3DColor.transparent);
        upgradeName.setPosition(5, 0);
        pane.add(upgradeName);
        
        V3DLabel upgradeDescription = new V3DLabel(upgrade.getDescription());
        upgradeDescription.setFontStyle("Ubuntu", "", 12);
        upgradeDescription.setColor(V3DColor.darkgrey, V3DColor.transparent);
        upgradeDescription.setPosition(5, 20);
        upgradeDescription.setyAlignment(GuiYAlignment.TOP);
        upgradeDescription.setWordWarping(true, 150);
        pane.add(upgradeDescription);
        
        pane.setSize(200, 100);
        return pane;
    }

    @Override
    public V3DContainer getContentPane() {
        
        
        
        
        return root;
    }

    
}
