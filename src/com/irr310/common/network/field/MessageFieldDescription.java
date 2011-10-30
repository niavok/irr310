package com.irr310.common.network.field;

import java.lang.reflect.Field;

import com.irr310.common.network.NetworkMessage;

public abstract class MessageFieldDescription {
    protected final Field field;
    
    MessageFieldDescription(Field field) {
        this.field = field;
    }

    public abstract int load(NetworkMessage networkMessage, byte[] buffer, int localOffset);

    public abstract int getSize(NetworkMessage networkMessage);
    
    public abstract int write(NetworkMessage networkMessage, byte[] buffer, int offset);
}