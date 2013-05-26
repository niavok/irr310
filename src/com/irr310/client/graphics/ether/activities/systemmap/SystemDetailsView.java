package com.irr310.client.graphics.ether.activities.systemmap;


import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.TextView.Gravity;

public class SystemDetailsView extends RelativeLayout {

    private final WorldSystemState system;
    private SystemDetailCircleView systemCircleView;
    private TextView textView;

    public SystemDetailsView(final WorldSystemState system) {
        this.system = system;
        
        systemCircleView = new SystemDetailCircleView(system);
        
        textView = new TextView();
        textView.setText(system.name);
        textView.setFont(I3dRessourceManager.getInstance().loadFont("systemNameWorldMap@fonts"));
        textView.setTextColor(I3dRessourceManager.getInstance().loadColor("systemNameWorldMap@color"));
        
        addViewInLayout(systemCircleView);
        addViewInLayout(textView);
        
        reshape();
    }
    
   

    public WorldSystemState getSystem() {
        return system;
    }
    
    private void reshape() {
        
        systemCircleView.reshape();
        
        
        layoutParams.setLayoutWidthMeasure(LayoutMeasure.MATCH_PARENT);
        layoutParams.setLayoutHeightMeasure(LayoutMeasure.MATCH_PARENT);
        
        
        
        
        textView.getLayoutParams().setMarginTopMeasure(new Measure(15, false, Axis.VERTICAL));
        textView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.WRAP_CONTENT);
        textView.setGravity(Gravity.CENTER);
        
        
    }

}
