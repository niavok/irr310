package com.irr310.client.graphics.ether.activities.production;

import com.irr310.common.world.ProductionTask;
import com.irr310.i3d.I3dRessourceManager;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.ProxyView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;

public class ProductionTaskDetailsView extends ProxyView {

    private final ProductionTask productionTask;
    private TextView productNameTextView;
    private Button cancelProductButton;
    private final FactoryActivity productionActivity;

    public ProductionTaskDetailsView(FactoryActivity productionActivity, ProductionTask productionTask) {
        super(I3dRessourceManager.loadView("main@layout/production/production_task_details"));
        this.productionActivity = productionActivity;
        this.productionTask = productionTask;
        productNameTextView = (TextView) findViewById("productNameTextView@layout/production/production_task_details");
        
        cancelProductButton = (Button) findViewById("cancelProductButton@layout/production/production_task_details");
        
        productNameTextView.setText(productionTask.getProduct().getName() +" "+ productionTask.getProduct().getCode());
        
        
        cancelProductButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
            }
        });
    }

}
