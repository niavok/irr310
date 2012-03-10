package com.irr310.common.world.upgrade;

import java.util.ArrayList;
import java.util.List;

public class Upgrade {
    public enum UpgradeCategory {
        WEAPON_UPGRADE,
        WEAPONS,
        SHIP_UPGRADE,
        DEFENSE,
        SYSTEM,
    }
    
    int maxRank;
    int initialRank;

    private UpgradeCategory category;
    private String tag;
    private String name;
    private String globalDescription;
    private List<Upgrade> dependencies;
    private List<String> rankDescriptions;
    private List<Integer> prices;
    
    public Upgrade() {
        dependencies = new ArrayList<Upgrade>();
        prices = new ArrayList<Integer>();
        rankDescriptions = new ArrayList<String>();
        maxRank = 0;
        initialRank = 0;
    }
    
    public UpgradeCategory getCategory() {
        return category;
    }
    public List<Upgrade> getDependencies() {
        return dependencies;
    }
    
    public String getGlobalDescription() {
        return globalDescription;
    }
    public int getMaxRank() {
        return maxRank;
    }
    public String getName() {
        return name;
    }
    
    public List<Integer> getPrices() {
        return prices;
    }
    
    public String getTag() {
        return tag;
    }
    
    
    public void setCategory(UpgradeCategory category) {
        this.category = category;
    }
    
    public void setGlobalDescription(String description) {
        this.globalDescription = description;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public void addDependency(Upgrade dependency) {
        dependencies.add(dependency);
    }
    
    public void addRank(int price, String rankDescription) {
        prices.add(price);
        rankDescriptions.add(rankDescription);
        maxRank ++;
    }
    
    public List<String> getRankDescriptions() {
        return rankDescriptions;
    }
    
    public int getInitialRank() {
        return initialRank;
    }
    
    public void setInitialRank(int initialRank) {
        this.initialRank = initialRank;
    }
    
}
