package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.List;

import org.lwjgl.input.Mouse;

import com.irr310.common.tools.Log;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.LayoutParams.LayoutMeasure;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.TextView.Gravity;
import com.irr310.i3d.view.View.ViewState;
import com.irr310.i3d.view.View;

public class SystemView extends RelativeLayout {

    private final WorldSystem system;
    private float size;
    private float zoom;
    private float zoomedSize;
    private SystemCircleView systemCircleView;
    private TextView textView;

    public SystemView(final WorldMapActivity activity, final WorldSystem system, final SelectionManager<WorldSystem>selectionManager) {
        this.system = system;
        
        size = 30;
        zoom = 1;

        
        systemCircleView = new SystemCircleView(system);
        
        textView = new TextView();
        textView.setText(system.getName());
        textView.setFont(I3dRessourceManager.getInstance().loadFont("systemNameWorldMap@fonts"));
        textView.getTextColor().set(ViewState.IDLE, I3dRessourceManager.getInstance().loadColor("systemNameWorldMap@color"));
        
        addViewInLayout(systemCircleView);
        addViewInLayout(textView);
        
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                Log.trace("click on system count="+mouseEvent.getClickCount());
                if(mouseEvent.getClickCount() == 2) {
                    activity.inspectSystemAction(system);
                } else {
                    selectionManager.select(system);
                }
            }

        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<WorldSystem>() {
            
            public void onSelectionChange(List<WorldSystem> selection) {
                if(selection.contains(SystemView.this.system)) {
                    systemCircleView.setState(ViewState.SELECTED);
                } else {
                    systemCircleView.setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(SystemView.class));
            }
        }
        );
        
        if(selectionManager.getSelection().contains(system)) {
            systemCircleView.setState(ViewState.SELECTED);
        }
        
        reshape();
    }
    
    public WorldSystem getSystem() {
        return system;
    }
    
    private void reshape() {
        
        systemCircleView.reshape();
        
        zoomedSize = size * zoom;
        
        mLayoutParams.setLayoutWidthMeasure(LayoutMeasure.FIXED);
        mLayoutParams.setWidthMeasure(new Measure(zoomedSize, false, Axis.HORIZONTAL));
        mLayoutParams.setLayoutHeightMeasure(LayoutMeasure.FIXED);
        mLayoutParams.setHeightMeasure(new Measure(zoomedSize, false, Axis.VERTICAL));
        
        mLayoutParams.setMarginLeftMeasure(new Measure((float) (system.getLocation().x * zoom) - zoomedSize/2, false, Axis.HORIZONTAL));
        mLayoutParams.setMarginTopMeasure(new Measure((float) (system.getLocation().y * zoom) - zoomedSize/2, false, Axis.VERTICAL));
        
        textView.getLayoutParams().setMarginTopMeasure(new Measure(-15 - zoomedSize/20 , false, Axis.VERTICAL));
        textView.getLayoutParams().setLayoutWidthMeasure(LayoutMeasure.FIXED);
        textView.getLayoutParams().setWidthMeasure(new Measure(zoomedSize, false, Axis.HORIZONTAL));
        textView.setGravity(Gravity.CENTER);
        
        
        
    }
    
    
    public void setZoom(float zoom) {
        systemCircleView.setZoom(zoom);
        this.zoom = zoom;
        
        
        reshape();
        if(getParent() != null) {
            getParent().requestLayout();
        }
    }
}
