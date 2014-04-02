package com.irr310.i3d;

import java.util.EnumMap;

import com.irr310.common.tools.RessourceLoadingException;
import com.irr310.i3d.view.View.ViewState;

public class StyleSelector<T extends Duplicable<T>> {
    
    EnumMap<ViewState, T> data = new EnumMap<ViewState, T>(ViewState.class);
    
    
    public StyleSelector() {
    }
    
    public StyleSelector(T idleValue) {
        data.put(ViewState.IDLE, idleValue);
    }

    public T get(ViewState state) {
         T value = data.get(state);
         if(value == null) {
             switch(state) {
                case ACTIVE:
                case DISABLED:
                case FOCUSED:
                case OVER:
                case SELECTED:
                    value = get(ViewState.IDLE);
                    break;
                case FOCUSED_OVER:
                    value = data.get(ViewState.FOCUSED);
                    if(value == null) {
                        value = get(ViewState.OVER);
                    }
                case SELECTED_OVER:
                    value = data.get(ViewState.SELECTED);
                    if(value == null) {
                        value = get(ViewState.OVER);
                    }
                    break;
                case IDLE:
                default:
                    throw new RessourceLoadingException("No value in style selector");
             }
         }
         return value;
    }
    
    private T getRaw(ViewState state) {
        return data.get(state);
    }
    
    
    public void set(ViewState state, T value) {
        data.put(state, value);
    }

    public StyleSelector<T> squashIn(StyleSelector<T> target) {
        if(target == null) {
            return this;
        }
        
        StyleSelector<T> result = new StyleSelector<T>();
        
        // Merge both selectors
        for(ViewState state: ViewState.values()) {
            T raw = this.getRaw(state);
            if(raw != null) {
                result.set(state, raw);
            } else {
                raw = target.getRaw(state);
                if(raw != null) {
                    result.set(state, raw);
                }
            }
        }
        
        return result;
    }

    public StyleSelector<T> duplicate() {
        StyleSelector<T> result = new StyleSelector<T>();
        for(ViewState state: ViewState.values()) {
            T raw = this.getRaw(state);
            if(raw != null) {
                result.set(state, raw.duplicate());
            }
        }
        return result;
    }


   
}
