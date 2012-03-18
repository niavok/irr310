package com.irr310.common.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3d;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.BroadphasePair;
import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.DispatcherInfo;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionWorld.LocalRayResult;
import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.DefaultNearCallback;
import com.bulletphysics.collision.dispatch.NearCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.collision.narrowphase.PersistentManifold;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.InternalTickCallback;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.irr310.common.Game;
import com.irr310.common.event.CelestialObjectAddedEvent;
import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.event.CollisionEvent;
import com.irr310.common.event.ComponentAddedEvent;
import com.irr310.common.event.ComponentRemovedEvent;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.PauseEngineEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.event.StartEngineEvent;
import com.irr310.common.event.WorldShipAddedEvent;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Link;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.Slot;
import com.irr310.common.world.WorldObject;
import com.irr310.common.world.capacity.Capacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.WingCapacity;
import com.irr310.server.Duration;

public class PhysicEngine extends FramerateEngine {

    public static final float PI_2 = 1.57079632679489661923f;
    public static final float PHYSICAL_SCALE = 1f;

    // keep the collision shapes, for deletion/cleanup
    private ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
    private BroadphaseInterface broadphase;
    private CollisionDispatcher dispatcher;
    private ConstraintSolver solver;
    private DefaultCollisionConfiguration collisionConfiguration;
    protected Clock clock = new Clock();

    // this is the most important class
    protected DynamicsWorld dynamicsWorld = null;
    private Map<Part, RigidBody> partToBodyMap;

    private List<Pair<LinearEngineCapacity, RigidBody>> linearEngines;
    private List<Pair<WingCapacity, RigidBody>> wings;
    private List<Component> components;
    private List<Ship> ships;
    private List<Link> links;

    private PhysicEngineEventVisitor eventVisitor;

    public PhysicEngine() {
        framerate = new Duration(10000000); // 10 ms

        linearEngines = new ArrayList<Pair<LinearEngineCapacity, RigidBody>>();
        wings = new ArrayList<Pair<WingCapacity, RigidBody>>();
        eventVisitor = new PhysicEngineEventVisitor();
        components = new ArrayList<Component>();
        ships = new ArrayList<Ship>();
        links = new ArrayList<Link>();
        initPhysics();
    }

    @Override
    protected void frame() {
        // Apply forces
        // Linear Engines
        for (Pair<LinearEngineCapacity, RigidBody> linearEngine : linearEngines) {
            
            RigidBody body = linearEngine.getRight();
            Transform t = new Transform();
            body.getWorldTransform(t);

            TransformMatrix force = TransformMatrix.identity();
            force.translate(new Vec3(0, linearEngine.getLeft().getCurrentThrust() * PHYSICAL_SCALE * 1, 0));

            TransformMatrix rotation = new TransformMatrix();
            t.getOpenGLMatrix(rotation.getData());
            rotation.setTranslation(0, 0, 0);
            force.preMultiply(rotation);
            body.applyCentralForce(force.getTranslation().toVector3d());
            body.setActivationState(RigidBody.ACTIVE_TAG);
        }

        // wings
        for (Pair<WingCapacity, RigidBody> wing : wings) {
            RigidBody body = wing.getRight();
            WingCapacity wingCapacity = wing.getLeft();

            // Get Transform
            Transform t = new Transform();
            body.getWorldTransform(t);
            TransformMatrix bodyTransform = TransformMatrix.identity();
            t.getOpenGLMatrix(bodyTransform.getData());

            Vec3 breakAxis = wingCapacity.getBreakAxis();
            Vec3 absoluteBreakAxis = breakAxis.rotate(bodyTransform);

            Vec3 thrustAxis = wingCapacity.getThrustAxis();
            Vec3 absoluteThrustAxis = thrustAxis.rotate(bodyTransform);

            Vector3d lv = new Vector3d();
            body.getLinearVelocity(lv);
            Vec3 velocity = new Vec3(lv);

            if (velocity.length() > 0) {

                // Resistance
                double opposition = velocity.dot(absoluteBreakAxis);
                
                Vec3 oppositionVector = absoluteBreakAxis.multiply(opposition * wingCapacity.getFriction() * -1);

                // Conversion
                double conversion = velocity.dot(absoluteThrustAxis) / velocity.length();
                
                double force = Math.pow(conversion, 2) * Math.abs(opposition)
                        * wingCapacity.getYield();
                Vec3 conversionVector = absoluteThrustAxis.multiply(force);
                
                body.applyCentralForce(conversionVector.plus(oppositionVector).toVector3d());
            }
            body.setActivationState(RigidBody.ACTIVE_TAG);

        }

        Game.getInstance().getWorld().lock();

        // step the simulation
        if (dynamicsWorld != null) {
            dynamicsWorld.stepSimulation(framerate.getSeconds());
        }

        Game.getInstance().getWorld().unlock();

    }

    public float getDeltaTimeMicroseconds() {
        float dt = clock.getTimeMicroseconds();
        clock.reset();
        return dt;
    }

    public List<RayResultDescriptor> rayTest(final Vec3 from, final Vec3 to) {

        final List<RayResultDescriptor> rayResultDescriptorList = new ArrayList<RayResultDescriptor>();

        dynamicsWorld.rayTest(from.toVector3d(), to.toVector3d(), new RayResultCallback() {

            @Override
            public double addSingleResult(LocalRayResult rayResult, boolean normalInWorldSpace) {

                UserData data = (UserData) ((RigidBody) rayResult.collisionObject).getUserPointer();

                Vec3 globalPosition = from.plus(to.minus(from).multiply(rayResult.hitFraction));

                Vec3 localPosition = globalPosition.transform(data.part.getTransform().inverse());

                RayResultDescriptor descriptor = new RayResultDescriptor();
                descriptor.setHitFraction(rayResult.hitFraction);
                descriptor.setGlobalPosition(globalPosition);
                descriptor.setPart(data.part);
                descriptor.setLocalPosition(localPosition);

                rayResultDescriptorList.add(descriptor);

                return 0;
            }
        });
        
        if(rayResultDescriptorList.size() > 1) {
            Collections.sort(rayResultDescriptorList);
        }
        
        return rayResultDescriptorList;
    }

    public void initPhysics() {

        partToBodyMap = new HashMap<Part, RigidBody>();

        // collision configuration contains default setup for memory, collision
        // setup
        collisionConfiguration = new DefaultCollisionConfiguration();

        // use the default collision dispatcher. For parallel processing you can
        // use a diffent dispatcher (see Extras/BulletMultiThreaded)
        dispatcher = new CollisionDispatcher(collisionConfiguration);

        broadphase = new DbvtBroadphase();

        // the default constraint solver. For parallel processing you can use a
        // different solver (see Extras/BulletMultiThreaded)
        SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
        solver = sol;

        // TODO: needed for SimpleDynamicsWorld
        // sol.setSolverMode(sol.getSolverMode() &
        // ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());

        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);

        dynamicsWorld.setInternalTickCallback(new CollisionDetectionCallback(), null);

        // No gravity
        dynamicsWorld.setGravity(new Vector3d(0f, 0f, 0f));

        dynamicsWorld.getPairCache().setOverlapFilterCallback(new OverlapFilterCallback() {

            @Override
            public boolean needBroadphaseCollision(BroadphaseProxy proxy0, BroadphaseProxy proxy1) {

                UserData data0 = (UserData) ((RigidBody) proxy0.clientObject).getUserPointer();
                UserData data1 = (UserData) ((RigidBody) proxy1.clientObject).getUserPointer();

                if (data0 != null && data1 != null) {
                    if (data0.ship != null && data0.ship == data1.ship) {
                        return false;
                    }
                }

                // System.out.println("valid collision");
                return true;
            }
        });

        dispatcher.setNearCallback(new NearCallback() {

            DefaultNearCallback defaultNearCallback = new DefaultNearCallback();

            @Override
            public void handleCollision(BroadphasePair collisionPair, CollisionDispatcher dispatcher, DispatcherInfo dispatchInfo) {

                UserData data0 = (UserData) ((RigidBody) collisionPair.pProxy0.clientObject).getUserPointer();
                UserData data1 = (UserData) ((RigidBody) collisionPair.pProxy1.clientObject).getUserPointer();

                if (data0.part.getParentObject().isBroken()) {
                    return;
                }

                if (data1.part.getParentObject().isBroken()) {
                    return;
                }

                defaultNearCallback.handleCollision(collisionPair, dispatcher, dispatchInfo);
            }
        });

    }

    protected void addObject(WorldObject object) {
        for (final Part part : object.getParts()) {
            addPart(part, new UserData());
        }
    }

    protected void removeObject(WorldObject object) {
        for (final Part part : object.getParts()) {
            RigidBody body = partToBodyMap.remove(part);

            int numConstraint = dynamicsWorld.getNumConstraints();
            List<TypedConstraint> contraintToRemove = new ArrayList<TypedConstraint>();
            for (int i = 0; i < numConstraint; i++) {
                TypedConstraint constraint = dynamicsWorld.getConstraint(i);
                if (constraint.getRigidBodyA() == body || constraint.getRigidBodyB() == body) {
                    contraintToRemove.add(constraint);
                }
            }

            for (TypedConstraint typedConstraint : contraintToRemove) {
                dynamicsWorld.removeConstraint(typedConstraint);
            }

            dynamicsWorld.removeRigidBody(body);
        }
    }

    protected void removeComponent(Component component) {
        components.remove(component);
        removeObject(component);
    }

    public void reloadStates() {
        for (Entry<Part, RigidBody> partEntry : partToBodyMap.entrySet()) {
            // TODO: fix concurrent modification on the map
            RigidBody body = partEntry.getValue();
            PartMotionState motionState = (PartMotionState) body.getMotionState();
            motionState.reload();
        }
    }

    public void reloadStates(Part part) {
        RigidBody body = partToBodyMap.get(part);
        if (body != null) {
            PartMotionState motionState = (PartMotionState) body.getMotionState();
            motionState.reload();
        }
    }

    protected void addShip(Ship ship, TransformMatrix transform) {
        if (ships.contains(ship)) {
            return;
        }

        ships.add(ship);

        for (Component component : ship.getComponents()) {
            addComponent(transform, component);
        }

        for (Link link : ship.getLinks()) {
            addLink(link);
        }

    }

    private void addComponent(TransformMatrix transform, Component component) {
        if (components.contains(component)) {
            return;
        }
        if (!ships.contains(component.getShip())) {
            // Wait the parent ship is added
            return;
        }

        for (final Part part : component.getParts()) {

            if (transform != null) {
                part.getTransform().rotate(component.getShipRotation());

                part.getTransform().translate(component.getShipPosition());
                part.getTransform().preMultiply(transform);
            }

            UserData userData = new UserData();
            userData.ship = component.getShip();
            RigidBody rigidBody = addPart(part, userData);

            rigidBody.setUserPointer(userData);
        }

        for (Capacity capacity : component.getCapacities()) {
            if (capacity instanceof LinearEngineCapacity) {
                linearEngines.add(new ImmutablePair<LinearEngineCapacity, RigidBody>((LinearEngineCapacity) capacity,
                                                                                     partToBodyMap.get(component.getFirstPart())));
            }

            if (capacity instanceof WingCapacity) {
                wings.add(new ImmutablePair<WingCapacity, RigidBody>((WingCapacity) capacity, partToBodyMap.get(component.getFirstPart())));
            }

        }
        components.add(component);
    }

    protected void addLink(Link link) {
        if (links.contains(link)) {
            return;
        }

        links.add(link);

        Slot slot1 = link.getSlot1();
        Slot slot2 = link.getSlot2();
        RigidBody body1 = partToBodyMap.get(slot1.getPart());
        RigidBody body2 = partToBodyMap.get(slot2.getPart());

        Vec3 shipRotation1 = slot1.getComponent().getShipRotation();
        Vec3 shipRotation2 = slot2.getComponent().getShipRotation();

        Transform localA = new Transform(), localB = new Transform();
        localA.setIdentity();
        Vec3 position1 = slot1.getPosition();

        localA.origin.set(position1.x* PHYSICAL_SCALE, position1.y * PHYSICAL_SCALE, position1.z * PHYSICAL_SCALE);

        MatrixUtil.setEulerZYX(localA.basis,
                               (float) -Math.toRadians(shipRotation1.z),
                               (float) -Math.toRadians(shipRotation1.y),
                               (float) -Math.toRadians(shipRotation1.x));

        localB.setIdentity();
        Vec3 position2 = slot2.getPosition();
        localB.origin.set(position2.x * PHYSICAL_SCALE, position2.y * PHYSICAL_SCALE, position2.z * PHYSICAL_SCALE);

        MatrixUtil.setEulerZYX(localB.basis,
                               (float) -Math.toRadians(shipRotation2.z),
                               (float) -Math.toRadians(shipRotation2.y),
                               (float) -Math.toRadians(shipRotation2.x));

        Generic6DofConstraint constraint = new Generic6DofConstraint(body1, body2, localA, localB, false);
        constraint.setLinearLowerLimit(new Vector3d());
        constraint.setLinearUpperLimit(new Vector3d());

        constraint.setAngularLowerLimit(new Vector3d(0, 0, 0));
        constraint.setAngularUpperLimit(new Vector3d(0, 0, 0));

        dynamicsWorld.addConstraint(constraint, true);

    }

    protected RigidBody addPart(Part part, UserData userData) {
        // create a few dynamic rigidbodies
        // Re-using the same collision is better for memory usage and
        // performance
        if (partToBodyMap.get(part) != null) {
            return partToBodyMap.get(part);
        }

        com.irr310.common.world.Part.CollisionShape collisionShape = part.getCollisionShape();
        CollisionShape colShape = null;

        switch (collisionShape) {
            case BOX:
                colShape = new BoxShape(part.getShape().divide(2).multiply(PHYSICAL_SCALE).toVector3d());
                break;
            case SPHERE:
                colShape = new SphereShape(part.getShape().x*PHYSICAL_SCALE / 2);
                break;

            default:
                colShape = new BoxShape(part.getShape().divide(2).multiply(PHYSICAL_SCALE).toVector3d());
                break;
        }

        // CollisionShape colShape = new SphereShape(1f);
        collisionShapes.add(colShape);

        // Create Dynamic Objects

        float mass = (part.getMass().floatValue()) * PHYSICAL_SCALE / 10;

        // rigidbody is dynamic if and only if mass is non zero, otherwise
        // static
        boolean isDynamic = (mass != 0f);

        Vector3d localInertia = new Vector3d(0, 0, 0);
        if (isDynamic) {
            colShape.calculateLocalInertia(mass  * 2, localInertia);
        }

        // TODO rotation

        // using motionstate is recommended, it provides interpolation
        // capabilities, and only synchronizes 'active' objects
        PartMotionState myMotionState = new PartMotionState(part);
        RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
        RigidBody body = new RigidBody(rbInfo);
        userData.part = part;
        body.setUserPointer(userData);

        myMotionState.setBody(body);

        body.setActivationState(RigidBody.ISLAND_SLEEPING);
        body.setDamping(part.getLinearDamping().floatValue(), part.getAngularDamping().floatValue());

        body.setSleepingThresholds(0.001f, 0.001f);
        //body.setDeactivationTime(30);

        dynamicsWorld.addRigidBody(body);
        if(mass > 0) {
            body.setLinearVelocity(part.getLinearSpeed().multiply(PHYSICAL_SCALE).toVector3d());
            body.setAngularVelocity(part.getRotationSpeed().toVector3d());
        }
        body.setActivationState(RigidBody.ACTIVE_TAG);
        body.setCcdMotionThreshold(10000f*PHYSICAL_SCALE);
        body.setCcdSweptSphereRadius(0.2f*PHYSICAL_SCALE);
        partToBodyMap.put(part, body);

        return body;

    }

    private final class CollisionDetectionCallback extends InternalTickCallback {
        @Override
        public void internalTick(DynamicsWorld world, double timeStep) {
            int numManifolds = world.getDispatcher().getNumManifolds();
            for (int i = 0; i < numManifolds; i++) {
                PersistentManifold contactManifold = world.getDispatcher().getManifoldByIndexInternal(i);
                RigidBody obA = (RigidBody) contactManifold.getBody0();
                RigidBody obB = (RigidBody) contactManifold.getBody1();

                int numContacts = contactManifold.getNumContacts();
                for (int j = 0; j < numContacts; j++) {
                    ManifoldPoint pt = contactManifold.getContactPoint(j);
                    if (pt.getDistance() < 0.f) {
                        Vector3d ptA = new Vector3d();
                        Vector3d ptB = new Vector3d();

                        Vector3d vA = new Vector3d();
                        Vector3d vB = new Vector3d();

                        pt.getPositionWorldOnA(ptA);
                        pt.getPositionWorldOnB(ptB);
                        obA.getLinearVelocity(vA);
                        obB.getLinearVelocity(vB);

                        CollisionDescriptor collisionDescriptor = new CollisionDescriptor();
                        Part partA = ((UserData) obA.getUserPointer()).part;
                        Part partB = ((UserData) obB.getUserPointer()).part;

                        collisionDescriptor.setPartA(partA);
                        collisionDescriptor.setPartB(partB);

                        Vec3 localPositionA = new Vec3(pt.localPointA);
                        Vec3 globalPositionA = new Vec3(ptA);
                        Vec3 localPositionB = new Vec3(pt.localPointB);
                        Vec3 globalPositionB = new Vec3(ptB);

                        Vec3 globalPosition = globalPositionA.plus(globalPositionB).divide(2);

                        collisionDescriptor.setLocalPositionOnA(localPositionA);
                        collisionDescriptor.setLocalPositionOnB(localPositionB);
                        collisionDescriptor.setGlobalPosition(globalPosition);

                        collisionDescriptor.setImpulse(pt.appliedImpulse);

                        Game.getInstance().sendToAll(new CollisionEvent(collisionDescriptor));

                    }
                }
            }

        }
    }

    public class UserData {

        public Part part = null;
        public Ship ship = null;

    }

    public class PartMotionState extends MotionState {

        private final Part part;
        private RigidBody body;
        

        PartMotionState(Part part) {
            this.part = part;

        }

        public void reload() {
            Transform transform = new Transform();
            getWorldTransform(transform);
            body.setWorldTransform(transform);
            body.setLinearVelocity(part.getLinearSpeed().multiply(PHYSICAL_SCALE).toVector3d());
            body.setAngularVelocity(part.getRotationSpeed().toVector3d());
            body.activate(true);
        }

        public void setBody(RigidBody body) {
            this.body = body;
        }

        @Override
        public Transform getWorldTransform(Transform out) {
            out.setIdentity();
            // out.origin.set(part.getTransform().getTranslation().toVector3d());
            out.setFromOpenGLMatrix(part.getTransform().scale(PHYSICAL_SCALE).getData());
            return out;
        }

        @Override
        public void setWorldTransform(Transform worldTrans) {
            Vector3d origin = worldTrans.origin;
            part.getTransform().translate(origin.x, origin.y, origin.z);
            worldTrans.getOpenGLMatrix(part.getTransform().getData());
            part.getTransform().scale(1.0f/PHYSICAL_SCALE);

            Vector3d lv = new Vector3d();
            
            body.getLinearVelocity(lv);
            part.getLinearSpeed().set(new Vec3(lv).divide(PHYSICAL_SCALE));

            Vector3d av = new Vector3d();
            body.getAngularVelocity(av);
            part.getRotationSpeed().set(av);
            
        }
    }

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(eventVisitor);
    }

    private final class PhysicEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping physic engine");
            setRunning(false);
        }

        @Override
        public void visit(StartEngineEvent event) {
            pause(false);
        }

        @Override
        public void visit(PauseEngineEvent event) {
            pause(true);

        }

        @Override
        public void visit(CelestialObjectAddedEvent event) {
            addObject(event.getObject());
        }

        @Override
        public void visit(CelestialObjectRemovedEvent event) {
            removeObject(event.getObject());
        }

        @Override
        public void visit(ComponentAddedEvent event) {

            Component component = event.getComponent();
            Component kernel = component.getShip().getComponentByName("kernel");
            addComponent(TransformMatrix.identity().translate(kernel.getShipPosition().negative()).preMultiply(kernel.getFirstPart().getTransform()),
                         component);
            if (components.contains(event.getComponent())) {
                for (Link link : component.getShip().getLinks()) {
                    addLink(link);
                }
            }
        }

        @Override
        public void visit(ComponentRemovedEvent event) {
            removeObject(event.getComponent());

            for (Iterator<Link> iterator = links.iterator(); iterator.hasNext();) {
                Link link = iterator.next();
                if (link.getSlot1().getComponent() == event.getComponent() || link.getSlot2().getComponent() == event.getComponent()) {
                    iterator.remove();
                }
            }
        }

        @Override
        public void visit(WorldShipAddedEvent event) {
            addShip(event.getShip(), event.getTransform());
        }

    }

    @Override
    protected void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub

    }

}
