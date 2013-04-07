package com.irr310.i3d;

import java.util.ArrayList;
import java.util.List;


public class SelectionManager<T> {

    List<OnSelectionChangeListener<T>> listeners = new ArrayList<OnSelectionChangeListener<T>>();
    private List<T> selection = new ArrayList<T>();
    
    
    public void addOnSelectionChangeListener(OnSelectionChangeListener<T> onSelectionChangeListener) {
        listeners.add(onSelectionChangeListener);
    }
    
    public void clear() {
        listeners.clear();
    }

    public interface OnSelectionChangeListener<T> {
        void onSelectionChange(List<T> selection);
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
}
