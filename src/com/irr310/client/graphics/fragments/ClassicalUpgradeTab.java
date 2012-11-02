package com.irr310.client.graphics.fragments;

import org.fenggui.event.mouse.MouseAdapter;
import org.fenggui.event.mouse.MouseClickedEvent;
import org.fenggui.event.mouse.MouseEnteredEvent;
import org.fenggui.event.mouse.MouseExitedEvent;

import com.irr310.client.graphics.UiEngine;
import com.irr310.client.navigation.LoginManager;
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


public class ClassicalUpgradeTab extends UpgradeTab{

    private V3DContainer root;
    private PopupDescription popup;
    private int width;
    private int height;
    private final UpgradeCategory categorie;
    
    public ClassicalUpgradeTab(UiEngine engine, String name, UpgradeCategory categorie) {
        super(engine, name);
        this.categorie = categorie;
        
        root = null;
        popup = null;
    }

    private void generate() {
//        List<Upgrade> availableUpgrades = Game.getInstance().getWorld().getAvailableUpgrades();
//        
//        int y = 10;
//        int x = 10;
//        int blockWidth = 70;
//        int blockHeight = 70;
//        
//        for(Upgrade upgrade: availableUpgrades) {
//            
//            if(upgrade.getCategory() != categorie) {
//                continue;
//            }
//            V3DGuiComponent upgradePane = generateUpgradePane(upgrade);
//            upgradePane.setPosition(x, y);
//            root.add(upgradePane);
//            x += blockWidth;
//            if(x + blockWidth >  width  ) {
//               x = 10;
//               y += blockHeight;
//            } 
//        }
    }
    
    private V3DGuiComponent generateUpgradePane(final Upgrade upgrade) {
        final V3DContainer pane = new V3DContainer();
        pane.setSize(70, 70);
        
        V3DGuiRectangle upgradeRect = new V3DGuiRectangle();
        upgradeRect.setyAlignment(GuiYAlignment.TOP);
        upgradeRect.setPosition(3, 3);
        upgradeRect.setBorderWidth(2);
        upgradeRect.setSize(64, 64);
        upgradeRect.setFillColor(V3DColor.transparent);
        upgradeRect.setBorderColor(GuiConstants.irrGreen);
        pane.add(upgradeRect);
        
        
        
        UpgradeOwnership ownership  = LoginManager.localPlayer.getUpgradeState(upgrade);
        int currentRank = ownership.getRank();
        
        V3DLabel rankLabel = new V3DLabel(""+currentRank+" / "+upgrade.getMaxRank());
        rankLabel.setFontStyle("Ubuntu", "bold", 24);
        rankLabel.setColor(V3DColor.black, V3DColor.transparent);
        rankLabel.setxAlignment(GuiXAlignment.RIGHT);
        rankLabel.setPosition(5, 5);
        pane.add(rankLabel);

        
        
        final V3DButton upgradeButton = new V3DButton("");
        upgradeButton.setColor(V3DColor.transparent, V3DColor.transparent);
        upgradeButton.setxAlignment(GuiXAlignment.LEFT);
        upgradeButton.setyAlignment(GuiYAlignment.TOP);
        upgradeButton.setPadding(70,70,0,0);
        upgradeButton.setPosition( 0, 0);
        pane.add(upgradeButton);
        
        upgradeButton.getFenGUIWidget().addMouseListener(new MouseAdapter() {
            
            

            @Override
            public void mouseClicked(MouseClickedEvent mouseClickedEvent) {
                
//                if(mouseClickedEvent.getButton() == MouseButton.LEFT) {
//                    Game.getInstance().sendToAll(new BuyUpgradeRequestEvent(upgrade, LoginManager.localPlayer));
//                } else if (mouseClickedEvent.getButton() == MouseButton.RIGHT) {
//                    Game.getInstance().sendToAll(new SellUpgradeRequestEvent(upgrade, LoginManager.localPlayer));    
//                }
            }
            
            @Override
            public void mouseEntered(MouseEnteredEvent mouseEnteredEvent) {
                popup = new PopupDescription(upgrade);
                ClassicalUpgradeTab.this.getEngine().addPopup(popup, pane);
            }
            
            @Override
            public void mouseExited(MouseExitedEvent mouseExited) {
                ClassicalUpgradeTab.this.getEngine().removePopup(popup);
                popup = null;
            }
        });
        
        
        return pane;
    }
    
    

    @Override
    public V3DContainer getContentPane() {
        if(root == null) {
            root = new V3DContainer();
            generate();
        }
        return root;
    }

    @Override
    public void refresh() {
        System.err.println("refresh");
        root.removeAll();
        generate();
        if(popup != null) {
            popup.refresh();
        }
        
    }
    
    
    private V3DGuiComponent generatePopupPane(final Upgrade upgrade) {
        V3DContainer pane = new V3DContainer();
        pane.setSize(320, 150);
        
        V3DGuiRectangle upgradeRect = new V3DGuiRectangle();
        upgradeRect.setyAlignment(GuiYAlignment.TOP);
        upgradeRect.setPosition(0, 0);
        upgradeRect.setBorderWidth(0);
        upgradeRect.setSize(320, 150);
        upgradeRect.setFillColor(GuiConstants.irrDarkFill);
        pane.add(upgradeRect);
        
        V3DLabel upgradeName = new V3DLabel(upgrade.getName());
        upgradeName.setFontStyle("Ubuntu", "bold", 16);
        upgradeName.setColor(V3DColor.white, V3DColor.transparent);
        upgradeName.setPosition(5, 0);
        pane.add(upgradeName);
        
        int yPos = 20;
        
        V3DLabel upgradeDescription = new V3DLabel(upgrade.getGlobalDescription());
        upgradeDescription.setFontStyle("Ubuntu", "", 12);
        upgradeDescription.setColor(V3DColor.lightgrey, V3DColor.transparent);
        upgradeDescription.setPosition(5, yPos);
        upgradeDescription.setyAlignment(GuiYAlignment.TOP);
        upgradeDescription.setWordWarping(true, 150);
        pane.add(upgradeDescription);
        
        yPos += upgradeDescription.getSize().getHeight();
        
        
        UpgradeOwnership ownership  = LoginManager.localPlayer.getUpgradeState(upgrade);
        int currentRank = ownership.getRank();
        
        V3DLabel rankLabel = new V3DLabel(""+currentRank+" / "+upgrade.getMaxRank());
        rankLabel.setFontStyle("Ubuntu", "bold", 24);
        rankLabel.setColor(V3DColor.white, V3DColor.transparent);
        rankLabel.setxAlignment(GuiXAlignment.RIGHT);
        rankLabel.setPosition(5, 5);
        pane.add(rankLabel);
        
        
        if(currentRank > 0) {
            V3DLabel currentRankLabel = new V3DLabel("Current rank:");
            currentRankLabel.setFontStyle("Ubuntu", "bold", 12);
            currentRankLabel.setColor(V3DColor.white, V3DColor.transparent);
            currentRankLabel.setWordWarping(true, 150);
            currentRankLabel.setPosition(5, yPos);
            pane.add(currentRankLabel);
            
            yPos += currentRankLabel.getSize().getHeight();
            
            V3DLabel currentRankDescription = new V3DLabel(upgrade.getRankDescriptions().get(currentRank-1));
            currentRankDescription.setFontStyle("Ubuntu", "", 12);
            currentRankDescription.setColor(V3DColor.lightgrey, V3DColor.transparent);
            currentRankDescription.setWordWarping(true, 150);
            currentRankDescription.setPosition(5, yPos);
            pane.add(currentRankDescription);
            
            yPos += currentRankDescription.getSize().getHeight();
                      
           
            
            V3DLabel sellPrice = new V3DLabel(upgrade.getPrices().get(currentRank-1) +" $");
            sellPrice.setFontStyle("Ubuntu", "bold", 16);
            sellPrice.setColor(GuiConstants.irrRed, V3DColor.transparent);
            sellPrice.setxAlignment(GuiXAlignment.RIGHT);
            sellPrice.setyAlignment(GuiYAlignment.BOTTOM);
            sellPrice.setPosition( 20,60);
            pane.add(sellPrice);
            
        }
        
        
        if(currentRank < upgrade.getMaxRank()) {
            V3DLabel nextRankLabel = new V3DLabel("Next rank:");
            nextRankLabel.setFontStyle("Ubuntu", "bold", 12);
            nextRankLabel.setColor(V3DColor.white, V3DColor.transparent);
            nextRankLabel.setWordWarping(true, 150);
            nextRankLabel.setPosition(5, yPos);
            pane.add(nextRankLabel);
            
            yPos += nextRankLabel.getSize().getHeight();
            
            V3DLabel nextRankDescription = new V3DLabel(upgrade.getRankDescriptions().get(currentRank));
            nextRankDescription.setFontStyle("Ubuntu", "", 12);
            nextRankDescription.setColor(V3DColor.lightgrey, V3DColor.transparent);
            nextRankDescription.setWordWarping(true, 150);
            nextRankDescription.setPosition(5, yPos);
            pane.add(nextRankDescription);
            
            yPos += nextRankDescription.getSize().getHeight();
            
           
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
    
    private class PopupDescription extends V3DContainer{
     

        private final Upgrade upgrade;

        public PopupDescription(Upgrade upgrade) {
            this.upgrade = upgrade;
            V3DGuiComponent generatePopupPane = generatePopupPane(upgrade);
            add(generatePopupPane);
            setPosition(20, 20);
            setSize(generatePopupPane.getSize().getWidth(), generatePopupPane.getSize().getHeight());
        }

        public void refresh() {
            removeAll();
            V3DGuiComponent generatePopupPane = generatePopupPane(upgrade);
            add(generatePopupPane);
        }
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        
    }

    
}
