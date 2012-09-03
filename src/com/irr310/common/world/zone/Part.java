package com.irr310.common.world.zone;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Player;
import com.irr310.common.world.view.PartStateView;
import com.irr310.common.world.view.PartView;

public class Part extends GameEntity {

    // private final Vect3 position;
    private Double mass;
    private Double linearDamping;
    private Double angularDamping;
    private final Vec3 rotationSpeed;
    private final Vec3 linearSpeed;
    private final TransformMatrix transform;
    private Vec3 shape;
    private Player owner;
    private final WorldObject parentObject;
    private CollisionShape collisionShape;
    private List<Part> collisionExcludeList;
    
    
    public enum CollisionShape {
        BOX, SPHERE,
    }

    public Part(long id, WorldObject parentObject) {
        super(id);
        this.parentObject = parentObject;
        rotationSpeed = Vec3.origin();
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

    public void setMass(Double mass) {
        this.mass = mass;
    }

    public Double getMass() {
        return mass;
    }

    public Vec3 getRotationSpeed() {
        return rotationSpeed;
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

    public PartView toView() {
        PartView partView = new PartView();
        partView.id = getId();
        partView.linearSpeed = linearSpeed;
        partView.mass = mass;
        partView.linearDamping = linearDamping;
        partView.angularDamping = angularDamping;
        partView.rotationSpeed = rotationSpeed;
        partView.shape = shape;
        partView.collisionShape = collisionShape.ordinal();
        partView.transform = transform;
        partView.ownerId = (owner == null ? -1 : owner.getId());
        return partView;
    }

    public void fromView(PartView partView) {
        linearSpeed.set(partView.linearSpeed);
        rotationSpeed.set(partView.rotationSpeed);
        mass = partView.mass;
        linearDamping = partView.linearDamping;
        angularDamping = partView.angularDamping;
        shape = partView.shape;
        collisionShape = CollisionShape.values()[partView.collisionShape];
        transform.set(partView.transform.getData());
        owner = (partView.ownerId == -1 ? null : Game.getInstance().getWorld().getPlayerById(partView.ownerId));
    }

    public PartStateView toStateView() {
        PartStateView partStateView = new PartStateView();
        partStateView.id = getId();
        partStateView.linearSpeed = linearSpeed;
        partStateView.rotationSpeed = rotationSpeed;
        partStateView.transform = transform;
        return partStateView;
    }

    public void fromStateView(PartStateView partStateView) {

        // if (Game.getInstance() instanceof GameClient) {
        //
        // if (linearSpeed.distanceTo(partStateView.linearSpeed) > 0) {
        // System.err.println("fix speed from: " + linearSpeed.toString() +
        // " to " + partStateView.linearSpeed);
        // }
        // if
        // (transform.getTranslation().distanceTo(partStateView.transform.getTranslation())
        // > 0) {
        // System.err.println("fix position from: " +
        // transform.getTranslation().toString() + " to "
        // + partStateView.transform.getTranslation().toString());
        // }
        //
        // }
        linearSpeed.set(partStateView.linearSpeed);
        rotationSpeed.set(partStateView.rotationSpeed);
        transform.set(partStateView.transform.getData());
        // if (Game.getInstance() instanceof GameClient) {
        //
        //
        // System.err.println("new position : " +
        // transform.getTranslation().toString() + " asked "
        // + partStateView.transform.getTranslation().toString());
        // }

    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public WorldObject getParentObject() {
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
