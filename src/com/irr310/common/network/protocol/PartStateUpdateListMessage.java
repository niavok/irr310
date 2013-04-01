package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.state.MovementState;

public class PartStateUpdateListMessage extends NetworkMessage {

    @NetworkListField(MovementState.class)
    public List<MovementState> partStateList;

    public PartStateUpdateListMessage() {
        super(NetworkMessageType.PART_STATE_UPDATE_LIST);
    }
    
    public PartStateUpdateListMessage( List<MovementState> partStateList) {
        super(NetworkMessageType.PART_STATE_UPDATE_LIST);
        this.partStateList = partStateList;
    }

}
