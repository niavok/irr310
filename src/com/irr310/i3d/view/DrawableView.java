package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.drawable.Drawable;

public class DrawableView extends View {


    private Drawable drawable;

    public DrawableView() {
    }

    @Override
    public void onDraw(Graphics g) {
        drawable.setGraphics(g);
        drawable.setBounds(0, 0, layoutParams.getWidth(), layoutParams.getHeight());
        drawable.draw();
    }

    @Override
    public View duplicate() {
        DrawableView view = new DrawableView();
        view.setLayout(getLayoutParams());
        view.setBorder(getBorderParams().duplicate());
        view.setDrawable(drawable);
        return view;
    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
    }

    @Override
    public void onMeasure() {
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
