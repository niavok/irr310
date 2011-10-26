package com.irr310.server;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;
import com.irr310.server.event.DefaultEngineEventVisitor;
import com.irr310.server.event.EngineEvent;
import com.irr310.server.event.InitEngineEvent;
import com.irr310.server.event.PauseEngineEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.StartEngineEvent;
import com.irr310.server.event.WorldObjectAddedEvent;
import com.irr310.server.game.world.WorldObject;

public class PhysicEngine extends Engine {

	// keep the collision shapes, for deletion/cleanup
	private ObjectArrayList<CollisionShape> collisionShapes = new ObjectArrayList<CollisionShape>();
	private BroadphaseInterface broadphase;
	private CollisionDispatcher dispatcher;
	private ConstraintSolver solver;
	private DefaultCollisionConfiguration collisionConfiguration;
	protected Clock clock = new Clock();
	
	// this is the most important class
	protected DynamicsWorld dynamicsWorld = null;

	
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

		// create a few basic rigid bodies
		CollisionShape groundShape = new BoxShape(new Vector3f(50f, 50f, 50f));
		//CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 50);

		collisionShapes.add(groundShape);

		Transform groundTransform = new Transform();
		groundTransform.setIdentity();
		groundTransform.origin.set(0, -56, 0);

		// We can also use DemoApplication::localCreateRigidBody, but for clarity it is provided here:
		{
			float mass = 0f;

			// rigidbody is dynamic if and only if mass is non zero, otherwise static
			boolean isDynamic = (mass != 0f);

			Vector3f localInertia = new Vector3f(0, 0, 0);
			if (isDynamic) {
				groundShape.calculateLocalInertia(mass, localInertia);
			}

			// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
			DefaultMotionState myMotionState = new DefaultMotionState(groundTransform);
			RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, groundShape, localInertia);
			RigidBody body = new RigidBody(rbInfo);

			// add the body to the dynamics world
			//dynamicsWorld.addRigidBody(body);
		}

		

	}
	
	
	
	protected void addObject(WorldObject object) {
	
		// create a few dynamic rigidbodies
		// Re-using the same collision is better for memory usage and performance

		
		
		CollisionShape colShape = new BoxShape(object.getShape().divide(2).toVector3f());
		//CollisionShape colShape = new SphereShape(1f);
		collisionShapes.add(colShape);

		// Create Dynamic Objects
		

		float mass = object.getMass().floatValue();

		// rigidbody is dynamic if and only if mass is non zero, otherwise static
		boolean isDynamic = (mass != 0f);

		Vector3f localInertia = new Vector3f(0, 0, 0);
		if (isDynamic) {
			colShape.calculateLocalInertia(mass, localInertia);
		}

		
		//TODO rotation

		// using motionstate is recommended, it provides interpolation capabilities, and only synchronizes 'active' objects
		WorldObjectMotionState myMotionState = new WorldObjectMotionState(object);
		RigidBodyConstructionInfo rbInfo = new RigidBodyConstructionInfo(mass, myMotionState, colShape, localInertia);
		RigidBody body = new RigidBody(rbInfo);
		
		myMotionState.setBody(body);
		
		body.setActivationState(RigidBody.ISLAND_SLEEPING);
		body.setDamping(0.001f, 0.1f);
		body.setSleepingThresholds(0.001f, 0.001f);
		//body.setDeactivationTime(deactivationTime)

		dynamicsWorld.addRigidBody(body);
		
		body.setLinearVelocity(object.getLinearSpeed().toVector3f());
		body.setAngularVelocity(object.getRotationSpeed().toVector3f());
		body.setActivationState(RigidBody.ACTIVE_TAG);
	}
	
	public class WorldObjectMotionState extends MotionState {

		private final WorldObject object;
		private RigidBody body;

		WorldObjectMotionState(WorldObject object) {
			this.object = object;
			
		}
		
		public void setBody(RigidBody body) {
			this.body = body;
		}

		@Override
		public Transform getWorldTransform(Transform out) {
			out.setIdentity();
			out.origin.set(object.getTransform().getTranslation().toVector3d());
			return out;
		}

		@Override
		public void setWorldTransform(Transform worldTrans) {
			Vector3f origin = worldTrans.origin;
			object.getTransform().setTranslation(origin.x, origin.y, origin.z);
			//Vector3f linearVelocity = body.getLinearVelocity(new Vector3f());
			worldTrans.getOpenGLMatrix(object.getTransform().getData());
			object.getTransform().fireChanged();
			Quat4f rotation = worldTrans.getRotation(new Quat4f());
			
			//System.out.println("w="+rotation.w+" x="+rotation.x+" y="+rotation.y+" z="+rotation.z);
			
			//System.out.println("x="+origin.x+" y="+origin.y+" z="+origin.z+" vx="+linearVelocity.x+" vy="+linearVelocity.y+" vz="+linearVelocity.z+ " desactivation_time="+body.getDeactivationTime()+""+body.getLinearSleepingThreshold());
		}		
	}
	

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
