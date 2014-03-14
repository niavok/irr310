package com.irr310.common.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Observable<T> {

    private List<T> mObservers = new ArrayList<T>();
    private Map<Object,T> mRemoveMap = new HashMap<Object,T>();
    
    public void register(Object owner, T observer) {
        mObservers.add(observer);
        mRemoveMap.put(owner, observer);
    }
    
    public void unregister(Object owner) {
        T observer = mRemoveMap.remove(owner);
        mObservers.remove(observer);
    }

    public List<T> getObservers() {
        return mObservers;
    }
}
