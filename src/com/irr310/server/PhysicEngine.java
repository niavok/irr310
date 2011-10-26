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
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
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

public class PhysicEngine extends Engine {

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
	private Map<Part, RigidBody> bodyMap;

	
	public PhysicEngine() {
		framerate = new Duration(10000000);
		
		initPhysics();
	}

	@Override
	protected void frame() {
		// simple dynamics world doesn't handle fixed-time-stepping
		float ms = getDeltaTimeMicroseconds();

		// step the simulation
		if (dynamicsWorld != null) {
			dynamicsWorld.stepSimulation(ms / 1000000f);
		}
		
	}
	
	
	public float getDeltaTimeMicroseconds() {
		float dt = clock.getTimeMicroseconds();
		clock.reset();
		return dt;
	}
	
	public void initPhysics() {

		bodyMap = new HashMap<Part, RigidBody>();
		
		// collision configuration contains default setup for memory, collision setup
		collisionConfiguration = new DefaultCollisionConfiguration();

		// use the default collision dispatcher. For parallel processing you can use a diffent dispatcher (see Extras/BulletMultiThreaded)
		dispatcher = new CollisionDispatcher(collisionConfiguration);

		broadphase = new DbvtBroadphase();

		// the default constraint solver. For parallel processing you can use a different solver (see Extras/BulletMultiThreaded)
		SequentialImpulseConstraintSolver sol = new SequentialImpulseConstraintSolver();
		solver = sol;
		
		// TODO: needed for SimpleDynamicsWorld
		//sol.setSolverMode(sol.getSolverMode() & ~SolverMode.SOLVER_CACHE_FRIENDLY.getMask());
		
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);

		// No gravity
		dynamicsWorld.setGravity(new Vector3f(0f, 0f, 0f));

		
		/*dynamicsWorld.getPairCache().setOverlapFilterCallback(new OverlapFilterCallback() {
			
			@Override
			public boolean needBroadphaseCollision(BroadphaseProxy proxy0,
					BroadphaseProxy proxy1) {
				
				System.out.println("proxy0 "+proxy0.clientObject.getClass().getSimpleName());
				System.out.println("proxy1 "+proxy1.clientObject.getClass().getSimpleName());
				
				return true;
			}
		});*/

		

	}
	
	
	
	protected void addObject(WorldObject object) {
		for(final Part part: object.getParts()) {
			addPart(part);
		}
	}
	
	protected void addShip(Ship ship) {
		
		
		
		for(Component component:  ship.getComponents()) {
			for(final Part part: component.getParts()) {
				bodyMap.put(part, addPart(part));
			}
		}
		
		for(Link link: ship.getLinks()) {
			addLink(link);
		}
	}
	
	protected  void addLink(Link link) {
		Slot slot1 = link.getSlot1();
		Slot slot2 = link.getSlot2();
		RigidBody body1 = bodyMap.get(slot1.getPart());
		RigidBody body2 = bodyMap.get(link.getSlot2().getPart());
		
		
		{
			Transform localA = new Transform(), localB = new Transform();
			localA.setIdentity();
			localB.setIdentity();
			MatrixUtil.setEulerZYX(localA.basis, 0, PI_2, 0);
			localA.origin.set(slot1.getPosition().x.floatValue(),slot1.getPosition().y.floatValue(), slot1.getPosition().z.floatValue());
			MatrixUtil.setEulerZYX(localB.basis, 0, PI_2, 0);
			localB.origin.set(slot2.getPosition().x.floatValue(),slot2.getPosition().y.floatValue(), slot2.getPosition().z.floatValue());
			HingeConstraint constraint = new HingeConstraint(body1, body2, localA, localB);
			dynamicsWorld.addConstraint(constraint, true);
		}
		
		{
			Transform localA = new Transform(), localB = new Transform();
			localA.setIdentity();
			localB.setIdentity();
			MatrixUtil.setEulerZYX(localA.basis, PI_2, 0, 0);
			localA.origin.set(slot1.getPosition().x.floatValue(),slot1.getPosition().y.floatValue(), slot1.getPosition().z.floatValue());
			MatrixUtil.setEulerZYX(localB.basis, PI_2, 0, 0);
			localB.origin.set(slot2.getPosition().x.floatValue(),slot2.getPosition().y.floatValue(), slot2.getPosition().z.floatValue());
			HingeConstraint constraint = new HingeConstraint(body1, body2, localA, localB);
			dynamicsWorld.addConstraint(constraint, true);
		}
		
		
	}
	
	protected RigidBody addPart(Part part) {
		// create a few dynamic rigidbodies
		// Re-using the same collision is better for memory usage and performance

		
		
		CollisionShape colShape = new BoxShape(part.getShape().divide(2).toVector3f());
		//CollisionShape colShape = new SphereShape(1f);
		collisionShapes.add(colShape);

		// Create Dynamic Objects
		

		float mass = part.getMass().floatValue();

		// rigidbody is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0, 0, 0);
		if (isDynamic) {
			colShape.calculateLocalInertia(mass, localInertia);
		}

		
		//TODO rotation

		// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
		PartMotionState myMotionState = new PartMotionState(part);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);
		
		myMotionState.setBody(body);
		
		body.setActivationState(RigidBody.ISLAND_SLEEPING);
		body.setDamping(0.001f, 0.1f);
		body.setSleepingThresholds(0.001f, 0.001f);
		//body.setDeactivationTime(deactivationTime)

		dynamicsWorld.addRigidBody(body);
		
		body.setLinearVelocity(part.getLinearSpeed().toVector3f());
		body.setAngularVelocity(part.getRotationSpeed().toVector3f());
		body.setActivationState(RigidBody.ACTIVE_TAG);
		
		return body;
		
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
			out.origin.set(part.getTransform().getTranslation().toVector3d());
			return out;
		}

		@Override
		public void setWorldTransform(Transform worldTrans) {
			Vector3f origin = worldTrans.origin;
			part.getTransform().setTranslation(origin.x, origin.y, origin.z);
			//Vector3f linearVelocity = body.getLinearVelocity(new Vector3f());
			worldTrans.getOpenGLMatrix(part.getTransform().getData());
			part.getTransform().fireChanged();
			Quat4f rotation = worldTrans.getRotation(new Quat4f());
			
			//System.out.println("w="+rotation.w+" x="+rotation.x+" y="+rotation.y+" z="+rotation.z);
			
			//System.out.println("x="+origin.x+" y="+origin.y+" z="+origin.z+" vx="+linearVelocity.x+" vy="+linearVelocity.y+" vz="+linearVelocity.z+ " desactivation_time="+body.getDeactivationTime()+""+body.getLinearSleepingThreshold());
		}		
	}
	
	/*class ShipFilterCallback : public btOverlapFilterCallback
	{
		// return true when pairs need collision
		virtual bool	needBroadphaseCollision(btBroadphaseProxy* proxy0,btBroadphaseProxy* proxy1) const
		{
			bool collides = (proxy0->m_collisionFilterGroup & proxy1->m_collisionFilterMask) != 0;
			collides = collides && (proxy1->m_collisionFilterGroup & proxy0->m_collisionFilterMask);
			
			//add some additional logic here that modified 'collides'
			return collides;
		}
	};*/
	

	@Override
	protected void processEvent(EngineEvent e) {
		e.accept(new PhysicEngineEventVisitor());
	}

	private final class PhysicEngineEventVisitor extends DefaultEngineEventVisitor {
		@Override
		public void visit(QuitGameEvent event) {
			System.out.println("stopping physic engine");
			isRunning = false;
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
			addShip(event.getShip());
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
