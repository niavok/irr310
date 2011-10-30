package com.irr310.client.event;


public interface ClientEventVisitor {

    void visit(ShipAddEvent event);

}
