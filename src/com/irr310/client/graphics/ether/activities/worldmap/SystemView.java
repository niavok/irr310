package com.irr310.client.graphics.ether.activities.worldmap;

import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Graphics;
import com.irr310.i3d.view.View;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class SystemView extends View {

    private final WorldSystem system;

    public SystemView(WorldSystem system) {
        this.system = system;
    }

    @Override
    public void onDraw(Graphics g) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLayout(float l, float t, float r, float b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onMeasure() {
        // TODO Auto-generated method stub

    }

    @Override
    public View duplicate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
        // TODO Auto-generated method stub
        return false;
    }

}
