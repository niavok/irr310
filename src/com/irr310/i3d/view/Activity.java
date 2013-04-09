package com.irr310.i3d.view;

import com.irr310.common.tools.Log;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Surface;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public abstract class Activity implements ViewParent {

    private View mview = null;
    private LayoutParams mLayout= null;
    private boolean mLayoutUpdated = false;
	private Surface parentSurface = null;
    private Intent intent = null;
    private I3dContext context = null;
    private boolean stackable = true;
    private State state = State.STOPPED;

    protected abstract void onCreate(Bundle bundle);
    protected abstract void onResume();
    protected abstract void onPause();
    protected abstract void onDestroy();
    
    private enum State {
        STOPPED,
        PAUSED,
        STARTED,
    }
    
    
    public Activity() {
    }
    
    public void resume() {
        if(state == State.STOPPED) {
            create();
        }
        if(state == State.PAUSED) {
            state = State.STARTED;
            onResume();
        }
    }
    
    public void create() {
        state = State.PAUSED;
        onCreate(intent.getBundle());
    }
    
    public void pause() {
        if(state == State.STARTED) {
            onPause();
            state = State.PAUSED;
        }
    }
    
    public void destroy() {
        if(state == State.STARTED) {
            pause();
        }
        if(state == State.PAUSED) {
            onDestroy();
            mview = null;
            mLayoutUpdated = false;
            stackable = true;
            state = State.STOPPED;
        }
    }
    
    public void assignSurface(Surface parentSurface) {
        this.parentSurface = parentSurface;
		mLayout = new LayoutParams();
        forceLayout();
    }
    
    public void setContext(I3dContext context) {
        this.context = context;
    }
    
    protected void setContentView(String string) {
        mview = I3dRessourceManager.loadView(string);
        mview.assignParent(this);
    }
    
    public void draw(Graphics g) {
        mview.draw(g);
    }
    
    public void update(Time absTime, Time gameTime) {
        updateLayout();
        onUpdate(absTime, gameTime);
        //The update may have changed the layout. Check that
        updateLayout();
    }

    private void updateLayout() {
        if(!mLayoutUpdated) {
            mLayoutUpdated = true;
            mview.measure();
            mview.layout(mLayout.mLeft, mLayout.mTop, mLayout.mRight,mLayout.mBottom);
        }
    }

    protected void onUpdate(Time absTime, Time gameTime) {
    }
    
    @Override
    public ViewParent getParent() {
        return null;
    }
    
    @Override
    public void requestLayout() {
        mLayoutUpdated = false;
    }
  
    @Override
    public void addViewInLayout(View view) {
        // TODO Auto-generated method stub
        
    }
    
    public void setStackable(boolean stackable) {
        this.stackable = stackable;
        
    }
    
    public boolean isStackable() {
        return stackable;
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
		mLayout.setFrame(0, 0, parentSurface.width, parentSurface.height);
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
    
    public Intent getIntent() {
        return intent;
    }

    public I3dContext getContext() {
        return context;
    }
    
    public void onMouseEvent(V3DMouseEvent mouseEvent) {
        mview.onMouseEvent(mouseEvent);
    }
   


}
