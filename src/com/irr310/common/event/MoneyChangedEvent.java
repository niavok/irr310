package com.irr310.common.event;

import com.irr310.common.world.Player;

public class MoneyChangedEvent extends EngineEvent {

    private final int amount;
    private final Player player;
    private final boolean embedded;
    private final boolean positive;

    public MoneyChangedEvent(int amount, Player player, boolean embedded, boolean positive) {
        this.amount = amount;
        this.player = player;
        this.embedded = embedded;
        this.positive = positive;
    }

    @Override
    public void accept(EngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public int getAmount() {
        return amount;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isEmbedded() {
        return embedded;
    }

    public boolean isPositive() {
        return positive;
    }

}
