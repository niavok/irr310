package com.irr310.i3d;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.irr310.client.graphics.ether.activities.production.AvailableProductView;


public class SelectionManager<T> {

    List<OnSelectionChangeListener<T>> listeners = new CopyOnWriteArrayList<OnSelectionChangeListener<T>>();
    private List<T> selection = new ArrayList<T>();
    
    
    public void addOnSelectionChangeListener(OnSelectionChangeListener<T> onSelectionChangeListener) {
        listeners.add(onSelectionChangeListener);
    }
    
    public void clear() {
        listeners.clear();
    }

    public interface OnSelectionChangeListener<T> {
        void onSelectionChange(List<T> selection);

        boolean mustClear(Object clearKey);
    }

    public void select(T selectionItem) {
        selection.clear();
        selection.add(selectionItem);
        for(OnSelectionChangeListener<T> listener:listeners) {
            listener.onSelectionChange(selection);
        }
    }

    public List<T> getSelection() {
        return selection;
    }

    public void clear(Object clearKey) {
        for(OnSelectionChangeListener<T> listener:listeners) {
            if(listener.mustClear(clearKey)) {
                listeners.remove(listener);
            }
        }
    }

    public void clearSelection() {
        selection.clear();
        for(OnSelectionChangeListener<T> listener:listeners) {
            listener.onSelectionChange(selection);
        }
    }
}
