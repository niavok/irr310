package com.irr310.client.graphics.ether.activities.shipcamera;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.state.PartState;
import com.irr310.i3d.scene.controller.I3dFollow3DCameraController.Followable;

public class FollowablePart implements Followable{

    private PartState part;

    public FollowablePart(PartState part) {
        this.part = part;
    }

    @Override
    public TransformMatrix getTransform() {
        return part.transform;
    }

}
