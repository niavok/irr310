package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;

@NetworkClass
public class PartView {

    @NetworkField
    public long id;
    
    @NetworkField
    public long ownerId;
    
    @NetworkField
    public double mass;
    
    @NetworkField
    public double angularDamping;
    
    @NetworkField
    public double linearDamping;
    
    @NetworkField
    public Vec3 rotationSpeed;
    
    @NetworkField
    public Vec3 linearSpeed;
    
    @NetworkField
    public TransformMatrix transform;
    
    @NetworkField
    public Vec3 shape;

    @NetworkField
    public int collisionShape;
    
}
