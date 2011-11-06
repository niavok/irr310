package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldObject extends GameEntity {

    private String name;
    protected List<Part> parts;
    private Part firstPart;

    public WorldObject(long id, String name) {
        super(id);
        this.name = name;
        firstPart = null;
        parts = new ArrayList<Part>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Part> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        parts.add(part);
        if (firstPart == null) {
            firstPart = part;
        }
    }

    public Part getFirstPart() {
        return firstPart;
    }

}
