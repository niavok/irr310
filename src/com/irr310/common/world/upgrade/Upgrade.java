package com.irr310.common.world.upgrade;

import java.util.ArrayList;
import java.util.List;

public class Upgrade {
    public enum UpgradeCategory {
        WEAPON,
        SHIP,
        DEFENSE,
        SYSTEM,
    }
    
    int maxRank;

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
    }
    
    public UpgradeCategory getCategory() {
        return category;
    }
    public List<Upgrade> getDependencies() {
        return dependencies;
    }
    
    public String getDescription() {
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
    
    public void setMaxRank(int maxRank) {
        this.maxRank = maxRank;
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
    }
    
}
