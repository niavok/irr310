package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.common.world.ProductionTask;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class ProductionTaskView extends ProxyView {

    private final ProductionTask productionTask;
    private TextView productNameTextView;

    public ProductionTaskView(ProductionTask productionTask, final SelectionManager<ProductionTask> selectionManager) {
        super(I3dRessourceManager.loadView("main@layout/production/production_task"));
        this.productionTask = productionTask;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/production_task");
        
        productNameTextView.setText(productionTask.getProduct().getName() +" "+ productionTask.getProduct().getCode());
        
        this.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                selectionManager.select(ProductionTaskView.this.productionTask);
            }
        });
        
        selectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<ProductionTask>() {
          
            public void onSelectionChange(List<ProductionTask> selection) {
                if(selection.contains(ProductionTaskView.this.productionTask)) {
                    setState(ViewState.SELECTED);
                } else {
                    setState(ViewState.IDLE);
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                return (clearKey.equals(ProductionTaskView.class));
            }
        }
        );
        
        if(selectionManager.getSelection().contains(productionTask)) {
            setState(ViewState.SELECTED);
        }
    }

}
