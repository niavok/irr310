package com.irr310.common.binder;

public interface BinderListener<T> {
    void onChange(BindVariable<T> variable);
}