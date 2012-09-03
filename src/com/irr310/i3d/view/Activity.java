package com.irr310.i3d.view;

import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Surface;
import com.irr310.server.Time;

public abstract class Activity implements ViewParent {

    private View view;
    private Layout mLayout;
    private boolean mLayoutUpdated;

    public abstract void onCreate(Object objectBundle);
    public abstract void onStart();
    public abstract void onPause();
    
    public Activity() {
        mLayoutUpdated = false;
    }
    
    public void assignSurface(Surface parentSurface) {
        mLayout = new Layout(parentSurface.width, parentSurface.height);
    }
    
    protected void setContentView(String string) {
        view = I3dRessourceManager.loadView(string);
        view.assignParent(this);
    }
    
    public void draw() {
        view.draw();
    }
    
    public void update(Time absTime, Time gameTime) {
        if(!mLayoutUpdated) {
            mLayoutUpdated = true;
            view.computeSize();
        }
    }
    
    public void onResume() {
    }
    
    
    @Override
    public ViewParent getParent() {
        return null;
    }
    
    @Override
    public Layout getLayout() {
        return mLayout;
    }
    
    @Override
    public void requestLayout() {
        
    }
  
    @Override
    public void addChild(View view) {
        // TODO Auto-generated method stub
        
    }
    
}
