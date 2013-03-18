package com.irr310.common.binder;

class Bound<T> {

    final BindVariable<T> bindVariable;
    private final BinderListener<T> bindListener;
    private boolean changed;
    private final BinderClient binderClient;

    public Bound(BinderClient binderClient, BindVariable<T> bindVariable, BinderListener<T> bindListener) {
        this.binderClient = binderClient;
        this.bindVariable = bindVariable;
        this.bindListener = bindListener;
        changed = false;
    }

    public void onChange() {
        bindListener.onChange(bindVariable);
    }

    public boolean pickChanged() {
        if(changed) {
            changed = false;
            return true;
        }
        return false;
    }

    public void setChanged() {
        changed = true;
    }

    public void destroy() {
        binderClient.unbind(this);
        bindVariable.unbind(this);
    }
    
    public BinderClient getBinderClient() {
        return binderClient;
    }
}