package com.irr310.i3d;

import java.util.HashMap;
import java.util.Map;

import com.irr310.i3d.view.View;

public class RessourceFileCache {

    Map<String, Color> colorCache = new HashMap<String, Color>();
    Map<String, View> widgetCache = new HashMap<String, View>();
	private String fileId;

    public RessourceFileCache(String fileId) {
		this.fileId = fileId;
	}
    
    public View getView(String id) {
        View widget = widgetCache.get(id);
        if(widget != null) {
            return widget.duplicate();
        }
        return null;
    }
    
    public Color getColor(String id) {
        return  colorCache.get(id);
    }

    public void addColor(String id, Color color) {
        colorCache.put(id, color);
    }

    public void addWidget(String id, View widget) {
        widgetCache.put(id, widget);       
    }

	public String getFileId() {
		return fileId;
	}

   

}
