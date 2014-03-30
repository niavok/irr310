package com.irr310.i3d.view;


public class ImageButton extends DrawableView {

    public ImageButton() {
    }
   
    @Override
    public void onMeasure() {
        super.onMeasure();
    }
    
    @Override
    public View duplicate() {
        ImageButton view = new ImageButton();
        duplicateTo(view);
        return view;
    }
    
}
