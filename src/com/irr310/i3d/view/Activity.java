package com.irr310.i3d.view;

import com.irr310.i3d.Bundle;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.Handler;
import com.irr310.i3d.I3dContext;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.I3dVec2;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.Surface;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DKeyEvent;

public abstract class Activity implements ViewParent {

    private View mview = null;
    private LayoutParams mLayout= null;
    private boolean mLayoutUpdated = false;
	private Surface parentSurface = null;
    private Intent intent = null;
    private I3dContext context = null;
    private boolean stackable = true;
    private State state = State.STOPPED;
    private Handler handler = new Handler();
    private I3dVec2 mPreferredPosition;

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
    
    public void update(Timestamp time) {
        updateLayout();
        
        
        executeHandler();
        onUpdate(time);
        //The update may have changed the layout. Check that
        updateLayout();
    }

    private void executeHandler() {
        while(handler.hasMessages()) {
            Message message = handler.getMessage();
            onMessage(message);
        }        
    }
    protected void onMessage(Message message) {
    }
    private void updateLayout() {
        if(!mLayoutUpdated) {
            mLayoutUpdated = true;
            mview.measure(mLayout.getContentWidth(), mLayout.getContentHeight());
            
            float left  = mLayout.mLeft;
            float top  = mLayout.mTop;
            float right  = mLayout.mRight;
            float bottom  = mLayout.mBottom;
            
            // This is the case for popup
            if(mview.getLayoutParams().mMeasuredContentWidth < right - left && mview.getLayoutParams().getLayoutWidthMeasure() != LayoutParams.LayoutMeasure.MATCH_PARENT) {
                left = mPreferredPosition.getWidth();
                right = left + mview.getLayoutParams().mMeasuredContentWidth;
                if(right > mLayout.mRight) {
                    right = mLayout.mRight;
                    left = right - mview.getLayoutParams().mMeasuredContentWidth;
                }
            }
            
            if(mview.getLayoutParams().mMeasuredContentHeight < bottom - top && mview.getLayoutParams().getLayoutHeightMeasure() != LayoutParams.LayoutMeasure.MATCH_PARENT) {
                top = mPreferredPosition.getHeight();
                bottom = top + mview.getLayoutParams().mMeasuredContentHeight;
                
                if(bottom > mLayout.mBottom) {
                    bottom = mLayout.mBottom;
                    top = bottom - mview.getLayoutParams().mMeasuredContentHeight;
                }
            }
            
            mview.layout(left, top, right, bottom);
        }
    }

    protected void onUpdate(Timestamp time) {
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
		return mview.findViewById(id);
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
    
    public boolean onMouseEvent(I3dMouseEvent mouseEvent) {
        return mview.onMouseEvent(mouseEvent.relativeTo((int) mview.getLayoutParams().mLeft,(int)  mview.getLayoutParams().mTop));
    }
    
    public boolean onKeyEvent(V3DKeyEvent keyEvent) {
        return mview.onKeyEvent(keyEvent);
    }
    public void onControllerEvent(V3DControllerEvent keyEvent) {
        mview.onControllerEvent(keyEvent);
    }
    
    public Handler getHandler() {
        return handler;
    }
    public void setPreferredPosition(I3dVec2 preferredPosition) {
        mPreferredPosition = preferredPosition;
    }

    

}
