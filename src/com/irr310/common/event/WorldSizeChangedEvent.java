package com.irr310.common.event;


public class WorldSizeChangedEvent extends EngineEvent {

    private final double oldSize;
    private final double newSize;

    public WorldSizeChangedEvent(double oldSize, double newSize) {
        this.oldSize = oldSize;
        this.newSize = newSize;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public double getNewSize() {
        return newSize;
    }

    public double getOldSize() {
        return oldSize;
    }
}
