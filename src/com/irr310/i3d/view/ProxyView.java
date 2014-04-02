package com.irr310.i3d.view;

import com.irr310.i3d.Graphics;
import com.irr310.i3d.Style;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;


public class ProxyView extends View {

	private final View client;

    public ProxyView(View client) {
        this.client = client;
	}
    
    public void draw(Graphics g) {
        client.draw(g);
    }

    public void onDraw(Graphics g) {
        client.onDraw(g);
    }

    public void measure() {
        client.measure();
    }

    public int hashCode() {
        return client.hashCode();
    }

    public void layout(float l, float t, float r, float b) {
        client.layout(l, t, r, b);
    }

    public void onLayout(float l, float t, float r, float b) {
        client.onLayout(l, t, r, b);
    }

    public void onMeasure() {
        client.onMeasure();
    }

    public void setId(String id) {
        client.setId(id);
    }

    public String getId() {
        return client.getId();
    }

    public View duplicate() {
        return client.duplicate();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public View doFindViewById(String id) {
        return client.doFindViewById(id);
    }

    public BorderParams getBorderParams() {
        return client.getBorderParams();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        client.setOnClickListener(onClickListener);
    }

    public void setOnMouseListener(OnMouseEventListener onMouseEventListener) {
        client.setOnMouseListener(onMouseEventListener);
    }
    
    @Override
    public void setOnKeyListener(OnKeyEventListener onKeyEventListener) {
        client.setOnKeyListener(onKeyEventListener);
    }

    public boolean performClick(V3DMouseEvent mouseEvent) {
        return client.performClick(mouseEvent);
    }

    public void setHelp(String help) {
        client.setHelp(help);
    }

    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        return client.onMouseEvent(mouseEvent);
    }

    public boolean isVisible() {
        return client.isVisible();
    }

    public void setVisible(boolean visible) {
        client.setVisible(visible);
    }

    public String toString() {
        return client.toString();
    }

    public LayoutParams getLayoutParams() {
        return client.getLayoutParams();
    }

    public ViewParent getParent() {
        return client.getParent();
    }

    public void setState(ViewState state) {
        client.setState(state);
    }

    public void setIdleStyle(Style style) {
        client.setIdleStyle(style);
    }

    public Style getIdleStyle() {
        return client.getIdleStyle();
    }
}
