package com.irr310.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.Sys;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.BroadphaseProxy;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.OverlapFilterCallback;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.Generic6DofConstraint;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.irr310.common.engine.FramerateEngine;
import com.irr310.server.event.DefaultServerEngineEventVisitor;
import com.irr310.server.event.ServerEngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.event.WorldObjectAddedEvent;
import com.irr310.server.event.WorldShipAddedEvent;
import com.irr310.server.game.world.Component;
import com.irr310.server.game.world.Link;
import com.irr310.server.game.world.Part;
import com.irr310.server.game.world.Ship;
import com.irr310.server.game.world.Slot;
import com.irr310.server.game.world.WorldObject;

public class PhysicEngine extends FramerateEngine<ServerEngineEvent> {

	public static final float PI_2 = 1.57079632679489661923f;

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

	public PhysicEngine() {
		framerate = new Duration(10000000); // 10 ms

		initPhysics();
	}

	@Override
	protected void frame() {

		// step the simulation
		if (dynamicsWorld != null) {
			dynamicsWorld.stepSimulation(framerate.getSeconds());
		}

	}

	public float getDeltaTimeMicroseconds() {
		float dt = clock.getTimeMicroseconds();
		clock.reset();
		return dt;
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

		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase,
				solver, collisionConfiguration);

		// No gravity
		dynamicsWorld.setGravity(new Vector3f(0f, 0f, 0f));

		dynamicsWorld.getPairCache().setOverlapFilterCallback(
				new OverlapFilterCallback() {

					@Override
					public boolean needBroadphaseCollision(
							BroadphaseProxy proxy0, BroadphaseProxy proxy1) {

						UserData data0 = (UserData) ((RigidBody) proxy0.clientObject)
								.getUserPointer();
						UserData data1 = (UserData) ((RigidBody) proxy1.clientObject)
								.getUserPointer();

						// System.out.println("test collision");

						if (data0 != null && data1 != null
								&& data0.ship == data1.ship) {
							// System.out.println("cancel collision for "+data0.ship);
							return false;
						}

						// System.out.println("valid collision");
						return true;
					}
				});

	}

	protected void addObject(WorldObject object) {
		for (final Part part : object.getParts()) {
			addPart(part);
		}
	}

	protected void addShip(Ship ship, Vect3 position) {

		for (Component component : ship.getComponents()) {
			for (final Part part : component.getParts()) {

				part.getTransform().rotate(component.getShipRotation());

				part.getTransform().setTranslation(
						position.plus(component.getShipPosition()));

				RigidBody rigidBody = addPart(part);
				partToBodyMap.put(part, rigidBody);
				UserData userData = new UserData();
				userData.ship = ship;
				rigidBody.setUserPointer(userData);
			}
		}

		for (Link link : ship.getLinks()) {
			addLink(link);
		}
	}

	protected void addLink(Link link) {
		Slot slot1 = link.getSlot1();
		Slot slot2 = link.getSlot2();
		RigidBody body1 = partToBodyMap.get(slot1.getPart());
		RigidBody body2 = partToBodyMap.get(slot2.getPart());

		Vect3 shipRotation1 = slot1.getComponent().getShipRotation();
		Vect3 shipRotation2 = slot2.getComponent().getShipRotation();

		Transform localA = new Transform(), localB = new Transform();
		localA.setIdentity();
		localA.origin.set(slot1.getPosition().x.floatValue(),
				slot1.getPosition().y.floatValue(),
				slot1.getPosition().z.floatValue());

		MatrixUtil.setEulerZYX(localA.basis,
				(float) -(Math.toRadians(shipRotation1.x)),
				(float) -Math.toRadians(shipRotation1.y),
				(float) -Math.toRadians(shipRotation1.z));

		localB.setIdentity();
		localB.origin.set(slot2.getPosition().x.floatValue(),
				slot2.getPosition().y.floatValue(),
				slot2.getPosition().z.floatValue());

		MatrixUtil.setEulerZYX(localB.basis,
				(float) -(Math.toRadians(shipRotation2.x)),
				(float) -Math.toRadians(shipRotation2.y),
				(float) -Math.toRadians(shipRotation2.z));

		Generic6DofConstraint constraint = new Generic6DofConstraint(body1,
				body2, localA, localB, false);
		constraint.setLinearLowerLimit(new Vector3f());
		constraint.setLinearUpperLimit(new Vector3f());

		constraint.setAngularLowerLimit(new Vector3f(0, 0, 0));
		constraint.setAngularUpperLimit(new Vector3f(0, 0, 0));

		dynamicsWorld.addConstraint(constraint, true);

	}

	protected RigidBody addPart(Part part) {
		// create a few dynamic rigidbodies
		// Re-using the same collision is better for memory usage and
		// performance

		CollisionShape colShape = new BoxShape(part.getShape().divide(2)
				.toVector3f());
		// CollisionShape colShape = new SphereShape(1f);
		collisionShapes.add(colShape);

		// Create Dynamic Objects

		float mass = part.getMass().floatValue();

		// rigidbody is dynamic if and only if mass is non zero, otherwise
		// static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0, 0, 0);
		if (isDynamic) {
			colShape.calculateLocalInertia(mass, localInertia);
		}

		// TODO rotation

		// using motionstate is recommended, it provides interpolation
		// capabilities, and only synchronizes 'active' objects
		PartMotionState myMotionState = new PartMotionState(part);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass,
				myMotionState, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);

		myMotionState.setBody(body);

		body.setActivationState(RigidBody.ISLAND_SLEEPING);
		body.setDamping(0.001f, 0.1f);
		body.setSleepingThresholds(0.001f, 0.001f);
		// body.setDeactivationTime(deactivationTime)

		dynamicsWorld.addRigidBody(body);

		body.setLinearVelocity(part.getLinearSpeed().toVector3f());
		body.setAngularVelocity(part.getRotationSpeed().toVector3f());
		body.setActivationState(RigidBody.ACTIVE_TAG);
		body.setCcdMotionThreshold(1f);
		body.setCcdSweptSphereRadius(0.2f);
		return body;

	}

	public class UserData {

		public Ship ship = null;

	}

	public class PartMotionState extends MotionState {

		private final Part part;
		private RigidBody body;

		PartMotionState(Part part) {
			this.part = part;

		}

		public void setBody(RigidBody body) {
			this.body = body;
		}

		@Override
		public Transform getWorldTransform(Transform out) {
			out.setIdentity();
			// out.origin.set(part.getTransform().getTranslation().toVector3d());
			out.setFromOpenGLMatrix(part.getTransform().getData());
			return out;
		}

		@Override
		public void setWorldTransform(Transform worldTrans) {
			Vector3f origin = worldTrans.origin;
			part.getTransform().setTranslation(origin.x, origin.y, origin.z);
			// Vector3f linearVelocity = body.getLinearVelocity(new Vector3f());
			worldTrans.getOpenGLMatrix(part.getTransform().getData());
			part.getTransform().fireChanged();
		}
	}

	/*
	 * class ShipFilterCallback : public btOverlapFilterCallback { // return
	 * true when pairs need collision virtual bool
	 * needBroadphaseCollision(btBroadphaseProxy* proxy0,btBroadphaseProxy*
	 * proxy1) const { bool collides = (proxy0->m_collisionFilterGroup &
	 * proxy1->m_collisionFilterMask) != 0; collides = collides &&
	 * (proxy1->m_collisionFilterGroup & proxy0->m_collisionFilterMask);
	 * 
	 * //add some additional logic here that modified 'collides' return
	 * collides; } };
	 */

	@Override
	protected void processEvent(ServerEngineEvent e) {
		e.accept(new PhysicEngineEventVisitor());
	}

	private final class PhysicEngineEventVisitor extends
			DefaultServerEngineEventVisitor {
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
		public void visit(WorldObjectAddedEvent event) {
			addObject(event.getObject());
		}

		@Override
		public void visit(WorldShipAddedEvent event) {
			addShip(event.getShip(), event.getPosition());
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
