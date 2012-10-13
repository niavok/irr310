package com.irr310.i3d;

import java.util.HashMap;
import java.util.Map;

import com.irr310.i3d.fonts.Font;
import com.irr310.i3d.view.Drawable;
import com.irr310.i3d.view.View;

public class RessourceFileCache {

    Map<String, Color> colorCache = new HashMap<String, Color>();
    Map<String, Font> fontCache = new HashMap<String, Font>();
    Map<String, View> viewCache = new HashMap<String, View>();
    Map<String, String> stringCache = new HashMap<String, String>();
    Map<String, Style> styleCache = new HashMap<String, Style>();
    
	private String fileId;

    public RessourceFileCache(String fileId) {
		this.fileId = fileId;
	}
    
    public View getView(String id) {
        View widget = viewCache.get(id);
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
        viewCache.put(id, widget);       
    }

	public String getFileId() {
		return fileId;
	}

    public Font getFont(String id) {
        return  fontCache.get(id);
    }

    public void addFont(String id, Font font) {
        fontCache.put(id,font);
    }

    public void addString(String id, String string) {
        stringCache.put(id,string);
    }
    
    public String getString(String id) {
        return stringCache.get(id);
    }

    public void addStyle(String id, Style style) {
        styleCache.put(id,style);
    }
    
    public Style getStyle(String id) {
        return styleCache.get(id);
    }
}
