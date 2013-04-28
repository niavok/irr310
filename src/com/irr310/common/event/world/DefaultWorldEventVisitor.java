package com.irr310.common.event.world;


public class DefaultWorldEventVisitor implements WorldEventVisitor {

    @Override
    public void visit(PlayerConnectedEvent event) {
    }

    @Override
    public void visit(ConnectPlayerEvent event) {
    }

    @Override
    public void visit(FactionStateEvent event) {
    }
    
    @Override
    public void visit(QueryFactionStateEvent event) {
    }

    @Override
    public void visit(QueryWorldMapStateEvent event) {
    }
    
    @Override
    public void visit(WorldMapStateEvent event) {
    }
    
    @Override
    public void visit(QueryFactionProductionStateEvent event) {
    }
    
    @Override
    public void visit(FactionProductionStateEvent event) {
    }
    
    @Override
    public void visit(ActionBuyFactionFactoryCapacityEvent event) {
    }
    
    @Override
    public void visit(ActionSellFactionFactoryCapacityEvent event) {
    }

    @Override
    public void visit(QueryFactionAvailableProductListEvent event) {
    }
    
    @Override
    public void visit(FactionAvailableProductListEvent event) {
    }
    
    @Override
    public void visit(ActionBuyProductEvent event) {
    }
    
    @Override
    public void visit(FactionStocksStateEvent event) {
    }
    
    @Override
    public void visit(QueryFactionStocksStateEvent event) {
    }
    
    @Override
    public void visit(ActionDeployShipEvent event) {
    }
    
    @Override
    public void visit(ShipDeployedWorldEvent event) {
    }
}
