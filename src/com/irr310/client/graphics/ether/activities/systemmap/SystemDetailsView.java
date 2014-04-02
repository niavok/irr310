package com.irr310.client.graphics.ether.activities.systemmap;


import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.TextView.Gravity;

public class SystemDetailsView extends RelativeLayout {

    private final WorldSystem mSystem;
    private SystemDetailCircleView systemCircleView;
    private TextView textView;

    public SystemDetailsView(SystemMapActivity activity, final WorldSystem system) {
        this.mSystem = system;
        
        systemCircleView = new SystemDetailCircleView(activity, system);
        
        textView = new TextView();
        textView.setText(system.getName());
        textView.setFont(I3dRessourceManager.getInstance().loadFont("systemNameWorldMap@fonts"));
        
        textView.getTextColor().set(ViewState.IDLE, I3dRessourceManager.getInstance().loadColor("systemNameWorldMap@color"));
        
        addViewInLayout(systemCircleView);
        addViewInLayout(textView);
        
        reshape();
    }
    
   

    public WorldSystem getSystem() {
        return mSystem;
    }
    
    private void reshape() {
        
        systemCircleView.reshape();
        
        
        mLayoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        mLayoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
        
        
        
        
        textView.getLayoutParams().setMarginTopMeasure(new Measure(15, false, Axis.VERTICAL));
        textView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER);
        
        
    }

}
