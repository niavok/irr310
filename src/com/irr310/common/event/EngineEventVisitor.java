package com.irr310.common.event;


public interface EngineEventVisitor {

	public abstract void visit(QuitGameEvent event);

	public abstract void visit(StartEngineEvent event);
	
	public abstract void visit(InitEngineEvent event);
	
	public abstract void visit(PauseEngineEvent event);

	public abstract void visit(UseScriptEvent event);

	public abstract void visit(AddWorldObjectEvent event);

	public abstract void visit(CelestialObjectAddedEvent event);

	public abstract void visit(WorldShipAddedEvent event);

    public abstract void visit(NetworkEvent event);

    public abstract void visit(PlayerAddedEvent event);

    public abstract void visit(KeyPressedEvent event);
    
    public abstract void visit(KeyReleasedEvent event);

    public abstract void visit(PlayerLoggedEvent event);

    public abstract void visit(MinimizeWindowEvent event);

    public abstract void visit(CollisionEvent event);

    public abstract void visit(DamageEvent event);

    public abstract void visit(MouseEvent event);

    public abstract void visit(BulletFiredEvent event);

    public abstract void visit(CelestialObjectRemovedEvent event);

    public abstract void visit(LoadingGameEvent event);

    public abstract void visit(WorldReadyEvent event);

    public abstract void visit(GameOverEvent event);

    public abstract void visit(AddGuiComponentEvent event);

    public abstract void visit(RemoveGuiComponentEvent event);

    public abstract void visit(NextWaveEvent event);

    public abstract void visit(MoneyChangedEvent event);

    public abstract void visit(ReloadUiEvent event);

    public abstract void visit(BuyUpgradeRequestEvent event);

    public abstract void visit(UpgradeStateChanged event);

    public abstract void visit(SellUpgradeRequestEvent event);

    public abstract void visit(InventoryChangedEvent event);

    public abstract void visit(ComponentRemovedEvent event);

    public abstract void visit(ComponentAddedEvent event);
}
