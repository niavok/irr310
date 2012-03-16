package com.irr310.client.graphics.gui;

import java.util.List;

import org.fenggui.event.ButtonPressedEvent;
import org.fenggui.event.IButtonPressedListener;

import com.irr310.client.graphics.GraphicEngine;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.Game;
import com.irr310.common.event.BuyUpgradeRequestEvent;
import com.irr310.common.event.SellUpgradeRequestEvent;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

import fr.def.iss.vd2.lib_v3d.V3DColor;
import fr.def.iss.vd2.lib_v3d.gui.V3DButton;
import fr.def.iss.vd2.lib_v3d.gui.V3DContainer;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiXAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiComponent.GuiYAlignment;
import fr.def.iss.vd2.lib_v3d.gui.V3DGuiRectangle;
import fr.def.iss.vd2.lib_v3d.gui.V3DLabel;


public class UpgradeWeaponsTab extends UpgradeTab{

    private V3DContainer root;

    public UpgradeWeaponsTab(GraphicEngine engine) {
        super(engine, "Weapons");
        
        root = new V3DContainer();
        
        generate();
        
    }

    private void generate() {
        List<Upgrade> availableUpgrades = Game.getInstance().getWorld().getAvailableUpgrades();
        
        int y = 10;
        
        for(Upgrade upgrade: availableUpgrades) {
            if(upgrade.getCategory() != UpgradeCategory.WEAPONS) {
                continue;
            }
            V3DGuiComponent upgradePane = generateUpgradePane(upgrade);
            upgradePane.setPosition(10, y);
            root.add(upgradePane);
            y += 110;
        }
    }
    
    private V3DGuiComponent generateUpgradePane(final Upgrade upgrade) {
        V3DContainer pane = new V3DContainer();
        pane.setSize(320, 100);
        
        V3DGuiRectangle upgradeRect = new V3DGuiRectangle();
        upgradeRect.setyAlignment(GuiYAlignment.TOP);
        upgradeRect.setPosition(0, 0);
        upgradeRect.setBorderWidth(2);
        upgradeRect.setSize(320, 100);
        upgradeRect.setFillColor(V3DColor.transparent);
        upgradeRect.setBorderColor(GuiConstants.irrGreen);
        pane.add(upgradeRect);
        
        V3DLabel upgradeName = new V3DLabel(upgrade.getName());
        upgradeName.setFontStyle("Ubuntu", "bold", 24);
        upgradeName.setColor(V3DColor.black, V3DColor.transparent);
        upgradeName.setPosition(5, 0);
        pane.add(upgradeName);
        
        int yPos = 25;
        
        V3DLabel upgradeDescription = new V3DLabel(upgrade.getGlobalDescription());
        upgradeDescription.setFontStyle("Ubuntu", "", 12);
        upgradeDescription.setColor(V3DColor.darkgrey, V3DColor.transparent);
        upgradeDescription.setPosition(5, yPos);
        upgradeDescription.setyAlignment(GuiYAlignment.TOP);
        upgradeDescription.setWordWarping(true, 150);
        pane.add(upgradeDescription);
        
        yPos += upgradeDescription.getSize().getY();
        
        
        UpgradeOwnership ownership  = LoginManager.localPlayer.getUpgradeState(upgrade);
        int currentRank = ownership.getRank();
        
        V3DLabel rankLabel = new V3DLabel(""+currentRank+" / "+upgrade.getMaxRank());
        rankLabel.setFontStyle("Ubuntu", "bold", 24);
        rankLabel.setColor(V3DColor.black, V3DColor.transparent);
        rankLabel.setxAlignment(GuiXAlignment.RIGHT);
        rankLabel.setPosition(5, 5);
        pane.add(rankLabel);
        
        
        if(currentRank > 0) {
            
            final V3DButton sellButton = new V3DButton("Sell");
            sellButton.setFontStyle("Ubuntu", "bold", 16);
            sellButton.setColor(V3DColor.white, GuiConstants.irrRed);
            sellButton.setxAlignment(GuiXAlignment.RIGHT);
            sellButton.setyAlignment(GuiYAlignment.BOTTOM);
            sellButton.setPadding(5,20,20,5);
            sellButton.setPosition( 145,30);
            sellButton.getFenGUIWidget().addButtonPressedListener(new IButtonPressedListener() {
                
                @Override
                public void buttonPressed(ButtonPressedEvent e) {
                    Game.getInstance().sendToAll(new SellUpgradeRequestEvent(upgrade, LoginManager.localPlayer));
                }
            });
            pane.add(sellButton);
            
            
            V3DLabel sellPrice = new V3DLabel(upgrade.getPrices().get(currentRank-1) +" $");
            sellPrice.setFontStyle("Ubuntu", "bold", 16);
            sellPrice.setColor(GuiConstants.irrRed, V3DColor.transparent);
            sellPrice.setxAlignment(GuiXAlignment.RIGHT);
            sellPrice.setyAlignment(GuiYAlignment.BOTTOM);
            sellPrice.setPosition( 145,10);
            pane.add(sellPrice);
            
        }
        
        
        if(currentRank < upgrade.getMaxRank()) {
            
            
            
            final V3DButton buyButton = new V3DButton("Buy");
            buyButton.setFontStyle("Ubuntu", "bold", 16);
            buyButton.setColor(V3DColor.white, GuiConstants.irrGreen);
            buyButton.setxAlignment(GuiXAlignment.RIGHT);
            buyButton.setyAlignment(GuiYAlignment.BOTTOM);
            buyButton.setPadding(5,30,30,5);
            buyButton.setPosition( 20,30);
            buyButton.getFenGUIWidget().addButtonPressedListener(new IButtonPressedListener() {
                
                @Override
                public void buttonPressed(ButtonPressedEvent e) {
                    Game.getInstance().sendToAll(new BuyUpgradeRequestEvent(upgrade, LoginManager.localPlayer));
                }
            });
            pane.add(buyButton);
            
            
            V3DLabel buyPrice = new V3DLabel(upgrade.getPrices().get(currentRank) +" $");
            buyPrice.setFontStyle("Ubuntu", "bold", 16);
            buyPrice.setColor(GuiConstants.irrGreen, V3DColor.transparent);
            buyPrice.setxAlignment(GuiXAlignment.RIGHT);
            buyPrice.setyAlignment(GuiYAlignment.BOTTOM);
            buyPrice.setPosition( 20,10);
            pane.add(buyPrice);
        }
        
        return pane;
    }

    @Override
    public V3DContainer getContentPane() {
        
        return root;
    }

    @Override
    public void refresh() {
        System.err.println("refresh");
        root.removeAll();
        generate();
        
    }

    
}
