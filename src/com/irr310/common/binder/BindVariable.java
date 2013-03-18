package com.irr310.common.binder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BindVariable<T> {
    
    T publicValue;
    T nextValue;
    private final BinderServer server;
    List<Bound<?>> bounds = new CopyOnWriteArrayList<Bound<?>>();
    
    public BindVariable(BinderServer server, T initialValue) {
        this.server = server;
        publicValue = initialValue;
        nextValue = initialValue;
    }
    
    public T get() {
        return publicValue;
    }

    public void set(T value) {
        if(value != nextValue) {
            nextValue = value;
            server.notifyChange(this);
        }
    }

    void sync() {
        publicValue = nextValue;
    }

    public T getCurrent() {
        return nextValue;
    }

    public BinderServer getServer() {
        return server;
    }

    public List<Bound<?>> getBounds() {
        return bounds;
    }
    
    public void bind(Bound<T> bound) {
        bounds.add(bound);
    }
    
    public void unbind(Bound<T> bound) {
        bounds.remove(bound);
    }
}