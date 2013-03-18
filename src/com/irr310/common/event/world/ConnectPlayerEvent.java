package com.irr310.common.event.world;


public class ConnectPlayerEvent extends WorldEvent {

    
    final private String playerLogin;
    private final boolean local;

    public ConnectPlayerEvent(String playerLogin, boolean local) {
        this.playerLogin = playerLogin;
        this.local = local;
    }
    
    public String getPlayerLogin() {
        return playerLogin;
    }
    
    public boolean isLocal() {
        return local;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
