package com.irr310.common.network.field;


public abstract class MessageFieldGenerator<T> {
    protected final T value;
    
    MessageFieldGenerator(T value) {
        this.value = value;
    }

    public abstract int load(byte[] buffer, int localOffset);

    public abstract int getSize();
    
    public abstract int write( byte[] buffer, int offset);
}