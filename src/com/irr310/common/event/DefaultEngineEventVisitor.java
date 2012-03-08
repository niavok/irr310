package com.irr310.common.event;

public class DefaultEngineEventVisitor implements EngineEventVisitor {

    public void visit(QuitGameEvent event) {
    }

    public void visit(StartEngineEvent event) {
    }

    public void visit(InitEngineEvent event) {
    }

    public void visit(PauseEngineEvent event) {
    }

    public void visit(UseScriptEvent event) {
    }

    public void visit(AddWorldObjectEvent event) {
    }

    public void visit(CelestialObjectAddedEvent event) {
    }

    @Override
    public void visit(AddShipEvent event) {
    }

    @Override
    public void visit(WorldShipAddedEvent event) {
    }

    @Override
    public void visit(NetworkEvent event) {
    }

    @Override
    public void visit(PlayerAddedEvent event) {
    }

    @Override
    public void visit(KeyPressedEvent event) {
    }

    @Override
    public void visit(KeyReleasedEvent event) {
    }

    @Override
    public void visit(PlayerLoggedEvent event) {
    }

    @Override
    public void visit(MinimizeWindowEvent event) {
    }

    @Override
    public void visit(CollisionEvent event) {
    }

    @Override
    public void visit(DamageEvent event) {
    }

    @Override
    public void visit(MouseEvent event) {
    }

    public void visit(BulletFiredEvent event) {
    };
    
    @Override
    public void visit(CelestialObjectRemovedEvent event) {
    }
    
    @Override
    public void visit(LoadingGameEvent event) {
    }
    
    @Override
    public void visit(WorldReadyEvent event) {
    }
    
    @Override
    public void visit(GameOverEvent event) {
    }
    
    @Override
    public void visit(AddGuiComponentEvent event) {
    }
    
    @Override
    public void visit(RemoveGuiComponentEvent event) {
    }
    
    @Override
    public void visit(NextWaveEvent event) {
    }
    
    @Override
    public void visit(MoneyChangedEvent event) {
    }
}
