package com.irr310.client.graphics.ether.activities.production;

import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.world.state.ProductState;
import com.irr310.common.world.state.ProductionTaskState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class ProductionTaskDetailsView extends ProxyView {

    private final ProductionTaskState productionTask;
    private TextView productNameTextView;
    private Button cancelProductButton;
    private final FactoryActivity productionActivity;

    public ProductionTaskDetailsView(FactoryActivity productionActivity, ProductionTaskState productionState) {
        super(I3dRessourceManager.loadView("main@layout/production/production_task_details"));
        this.productionActivity = productionActivity;
        this.productionTask = productionState;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/production_task_details");
        
        cancelProductButton = (Button) findViewById("cancelProductButton@layout/production/production_task_details");
        
        productNameTextView.setText(productionState.product.name +" "+ productionState.product.code);
        
        
        cancelProductButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
            }
        });
    }

}
