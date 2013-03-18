package com.irr310.common.binder;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BinderClient {

    List<Bound<?>> boundList = new CopyOnWriteArrayList<Bound<?>>();
    
    public void forceProcess() {
        for(Bound<?> bound: boundList) {
            bound.onChange();
        }
    }

    public void process() {
        for(Bound<?> bound: boundList) {
            if(bound.pickChanged()) {
                bound.onChange();
            }
        }
    }
    
    public void clear() {
        for(Bound<?> bound: boundList) {
            bound.destroy();
        }
    }

    public <T> void bind(BindVariable<T> bindVariable, BinderListener<T> bindListener) {
        Bound<T> bound = new Bound<T>(this, bindVariable, bindListener);
        boundList.add(bound);
        bindVariable.bind(bound);
    }
    
    public void unbind(Bound bound) {
        boundList.remove(bound);
    }
}
