package com.spaceagencies.i3d.view;


public class ImageButton extends DrawableView {

    public ImageButton() {
    }
   
    @Override
    public void onMeasure(float widthMeasureSpec, float heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    @Override
    public View duplicate() {
        ImageButton view = new ImageButton();
        duplicateTo(view);
        return view;
    }
    
}
