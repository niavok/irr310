package com.irr310.common.network.protocol;

import java.util.List;

import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.view.PartStateView;

public class PartStateUpdateListMessage extends NetworkMessage {

    @NetworkListField(PartStateView.class)
    public List<PartStateView> partStateList;

    public PartStateUpdateListMessage() {
        super(NetworkMessageType.PART_STATE_UPDATE_LIST);
    }
    
    public PartStateUpdateListMessage( List<PartStateView> partStateList) {
        super(NetworkMessageType.PART_STATE_UPDATE_LIST);
        this.partStateList = partStateList;
    }

}
