package com.irr310.common.binder;

import java.util.ArrayList;
import java.util.List;

public class BinderServer {

    List<BindVariable<?>> changes = new ArrayList<BindVariable<?>>();
    
    public void flush() {
        for(BindVariable<?> change:changes) {
            change.sync();
            for(Bound<?> bound:change.getBounds()) {
                bound.setChanged();
            }
        }
        changes.clear();
    }

    public void notifyChange(BindVariable<?> bindVariable) {
        changes.add(bindVariable);
    }
    
}
