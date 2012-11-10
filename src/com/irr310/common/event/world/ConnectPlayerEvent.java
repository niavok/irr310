package com.irr310.common.event.world;


public class ConnectPlayerEvent extends WorldEvent {

    
    final private String playerLogin;

    public ConnectPlayerEvent(String playerLogin) {
        this.playerLogin = playerLogin;
    }
    
    public String getPlayerLogin() {
        return playerLogin;
    }
    
    @Override
    public void accept(WorldEventVisitor visitor) {
        visitor.visit(this);
    }

}
