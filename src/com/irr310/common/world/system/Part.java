package com.irr310.common.world.system;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;

public class Part extends WorldSystemEntity {

    // private final Vect3 position;
    private Double mass;
    private Double linearDamping;
    private Double angularDamping;
    private Vec3 angularSpeed;
    private Vec3 linearSpeed;
    private final TransformMatrix transform;
    private Vec3 shape;
    private Faction owner;
    private SystemObject parentObject;
    private CollisionShape collisionShape;

    // TODO : save this
    private List<Part> collisionExcludeList;
    
    
    public enum CollisionShape {
        BOX, SPHERE,
    }

    public Part(WorldSystem system, long id) {
        super(system, id);
        angularSpeed = Vec3.origin();
        linearSpeed = Vec3.origin();
        transform = TransformMatrix.identity();
        mass = 0.;
        shape = Vec3.one();
        owner = null;
        collisionShape = CollisionShape.BOX;
        linearDamping = 0.1;
        angularDamping = 0.9;
        
        collisionExcludeList = null;
    }

    public void setLinearDamping(Double linearDamping) {
        this.linearDamping = linearDamping;
    }

    public void setAngularDamping(Double angularDamping) {
        this.angularDamping = angularDamping;
    }

    public void setParentObject(SystemObject parentObject) {
        this.parentObject = parentObject;
    }

    public void setAngularSpeed(Vec3 angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public void setLinearSpeed(Vec3 linearSpeed) {
        this.linearSpeed = linearSpeed;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getMass() {
        return mass;
    }

    public Vec3 getAngularSpeed() {
        return angularSpeed;
    }

    public Vec3 getLinearSpeed() {
        return linearSpeed;
    }
    
    public Double getAngularDamping() {
        return angularDamping;
    }
    
    public Double getLinearDamping() {
        return linearDamping;
    }
    
    public void setAngularDamping(double angularDamping) {
        this.angularDamping = angularDamping;
    }
    
    public void setLinearDamping(double linearDamping) {
        this.linearDamping = linearDamping;
    }

    public TransformMatrix getTransform() {
        return transform;
    }

    public Vec3 getShape() {
        return shape;
    }

    public void setShape(Vec3 shape) {
        this.shape = shape;
    }

    public Faction getOwner() {
        return owner;
    }

    public void setOwner(Faction owner) {
        this.owner = owner;
    }

    public SystemObject getParentObject() {
        return parentObject;
    }

    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    public void addCollisionExclusion(Part part) {
        if(collisionExcludeList == null) {
            collisionExcludeList = new ArrayList<Part>();
        }
        if(!collisionExcludeList.contains(part)) {
            collisionExcludeList.add(part);
            part.addCollisionExclusion(this);
        }
    }
    
    public List<Part> getCollisionExcludeList() {
        return collisionExcludeList;
    }



}
