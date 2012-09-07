package com.irr310.i3d.view;

import com.irr310.client.graphics.ether.activities.MainMenuActivity;
import com.irr310.common.tools.Log;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Surface;
import com.irr310.server.Time;

public abstract class Activity implements ViewParent {

    private View mview;
    private Layout mLayout;
    private boolean mLayoutUpdated;
	private Surface parentSurface;
    private Intent intent;

    public abstract void onCreate(Object objectBundle);
    public abstract void onResume();
    public abstract void onPause();
    public abstract void onDestroy();
    
    public Activity() {
        mLayoutUpdated = false;
    }
    
    public void assignSurface(Surface parentSurface) {
        this.parentSurface = parentSurface;
		mLayout = new Layout();
        forceLayout();
    }
    
    protected void setContentView(String string) {
        mview = I3dRessourceManager.loadView(string);
        mview.assignParent(this);
    }
    
    public void draw() {
        mview.draw();
    }
    
    public void update(Time absTime, Time gameTime) {
        if(!mLayoutUpdated) {
            mLayoutUpdated = true;
            mview.layout(mLayout);
        }
        onUpdate(absTime, gameTime);
    }

    protected void onUpdate(Time absTime, Time gameTime) {
    }
    
    @Override
    public ViewParent getParent() {
        return null;
    }
    
    @Override
    public void requestLayout() {
        
    }
  
    @Override
    public void addChild(View view) {
        // TODO Auto-generated method stub
        
    }
    
    protected View findViewById(String id) {
		View view = mview.findViewById(id);
		if(view == null) {
			Log.error("Fail to find View with '"+id+"' as id.");
		}
		return view;
	}
	public void forceLayout() {
		mLayoutUpdated = false;
		mLayout.setWidth(parentSurface.width);
        mLayout.setHeight(parentSurface.height);
        mLayout.setOffsetX(0);
        mLayout.setOffsetY(0);
        mLayout.setWidthDefined(true);
        mLayout.setHeightDefined(true);
	}

	/**
	 * Call by the activity to leave the hand to another activity
	 * @param mainMenuActivity
	 */
	protected void startActivity(Intent intent) {
        parentSurface.startActivity(intent);
    }
	
    public void setIntent(Intent intent) {
        this.intent = intent;
    }


}
