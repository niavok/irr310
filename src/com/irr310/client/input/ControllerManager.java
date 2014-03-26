package com.irr310.client.input;

import org.lwjgl.input.Controller;

import com.irr310.common.tools.Log;

import fr.def.iss.vd2.lib_v3d.V3DControllerEvent;
import fr.def.iss.vd2.lib_v3d.V3DControllerEvent.ControllerAction;
import fr.def.iss.vd2.lib_v3d.V3DControllerEvent.ControllerState;

public class ControllerManager {

    private Controller mController;
    private ControllerEventObserver mObserver;
    
    ControllerState mState;
    
    public ControllerManager(Controller controller, ControllerEventObserver observer) {
        mController = controller;
        mObserver = observer;
        
        
        init();
    }

    private void init() {
        
        mState = new ControllerState(mController.getAxisCount(), mController.getButtonCount());
        
        
        for(int j = 0; j < mController.getAxisCount(); j++) {
            mState.mAxisValues[j] = mController.getAxisValue(j);
            mState.mDeadZoneValues[j] = mController.getDeadZone(j);
        }
        
        
        for(int j = 0; j < mController.getButtonCount(); j++) {
            mState.mButtonValues[j] = mController.isButtonPressed(j);
        }
    }

    public void dump() {
        
      Log.log("Controller "+mController.getIndex()+" detected : "+ mController.getName());
      
      for(int j = 0; j < mController.getAxisCount(); j++) {
          Log.log("    - Axis "+j+": "+ mController.getAxisName(j)+" (dead zone:"+mController.getDeadZone(j)+") -> "+ mController.getAxisValue(j));
      }
      
      for(int j = 0; j < mController.getButtonCount(); j++) {
          Log.log("    - Button "+j+": "+ mController.getButtonName(j)+" ->"+ mController.isButtonPressed(j));
      }
      
      for(int j = 0; j < mController.getRumblerCount(); j++) {
          Log.log("    - Rumbler "+j+": "+ mController.getRumblerName(j));
      }
              
    }

    public void update(int mouseX, int mouseY) {
        mController.poll();
        
        for(int j = 0; j < mController.getAxisCount(); j++) {
            float newValue = mController.getAxisValue(j);
            if(newValue != mState.mAxisValues[j]) {
                mState.mAxisValues[j] = newValue;    
                Log.log("- Axis "+j+": "+ mController.getAxisName(j)+" (dead zone:"+mController.getDeadZone(j)+") -> "+ mController.getAxisValue(j));
                mObserver.onControllerEvent(new V3DControllerEvent(ControllerAction.AXIS_MOVED, mController.getAxisName(j),  j, mState, mouseX, mouseY));
            }
            
        }
        
        for(int j = 0; j < mController.getButtonCount(); j++) {
            
            boolean newValue = mController.isButtonPressed(j);
            if(newValue != mState.mButtonValues[j]) {
                mState.mButtonValues[j] = newValue;
                Log.log("    - Button "+j+": "+ mController.getButtonName(j)+" ->"+ mController.isButtonPressed(j));
                if(newValue) {
                    mObserver.onControllerEvent(new V3DControllerEvent(ControllerAction.BUTTON_PRESSED, mController.getButtonName(j),  j, mState, mouseX, mouseY));
                } else {
                    mObserver.onControllerEvent(new V3DControllerEvent(ControllerAction.BUTTON_RELEASED, mController.getButtonName(j),  j, mState, mouseX, mouseY));
                }
                
            }
            

        }
    }
    
    public interface ControllerEventObserver {
        void onControllerEvent(V3DControllerEvent event);
    }

}
