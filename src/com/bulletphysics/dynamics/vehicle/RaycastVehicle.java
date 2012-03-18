/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * Bullet Continuous Collision Detection and Physics Library
 * Copyright (c) 2003-2008 Erwin Coumans  http://www.bulletphysics.com/
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 * 
 * Permission is granted to anyone to use this software for any purpose, 
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 
 * 1. The origin of this software must not be misrepresented; you must not
 *    claim that you wrote the original software. If you use this software
 *    in a product, an acknowledgment in the product documentation would be
 *    appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 *    misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.bulletphysics.dynamics.vehicle;

import javax.vecmath.Matrix3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.ContactConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;
import com.bulletphysics.dynamics.constraintsolver.TypedConstraintType;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.MiscUtil;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ArrayPool;
import com.bulletphysics.util.FloatArrayList;
import com.bulletphysics.util.ObjectArrayList;

/**
 * Raycast vehicle, very special constraint that turn a rigidbody into a vehicle.
 * 
 * @author jezek2
 */
public class RaycastVehicle extends TypedConstraint {
	
	private final ArrayPool<double[]> doubleArrays = ArrayPool.get(double.class);

	private static RigidBody s_fixedObject = new RigidBody(0, null, null);
	private static final double sideFrictionStiffness2 = 1.0f;
	
	protected ObjectArrayList<Vector3d> forwardWS = new ObjectArrayList<Vector3d>();
	protected ObjectArrayList<Vector3d> axle = new ObjectArrayList<Vector3d>();
	protected FloatArrayList forwardImpulse = new FloatArrayList();
	protected FloatArrayList sideImpulse = new FloatArrayList();

	private double tau;
	private double damping;
	private VehicleRaycaster vehicleRaycaster;
	private double pitchControl = 0f;
	private double steeringValue; 
	private double currentVehicleSpeedKmHour;

	private RigidBody chassisBody;

	private int indexRightAxis = 0;
	private int indexUpAxis = 2;
	private int indexForwardAxis = 1;
	
	public ObjectArrayList<WheelInfo> wheelInfo = new ObjectArrayList<WheelInfo>();

	// constructor to create a car from an existing rigidbody
	public RaycastVehicle(VehicleTuning tuning, RigidBody chassis, VehicleRaycaster raycaster) {
		super(TypedConstraintType.VEHICLE_CONSTRAINT_TYPE);
		this.vehicleRaycaster = raycaster;
		this.chassisBody = chassis;
		defaultInit(tuning);
	}
	
	private void defaultInit(VehicleTuning tuning) {
		currentVehicleSpeedKmHour = 0f;
		steeringValue = 0f;
	}

	/**
	 * Basically most of the code is general for 2 or 4 wheel vehicles, but some of it needs to be reviewed.
	 */
	public WheelInfo addWheel(Vector3d connectionPointCS, Vector3d wheelDirectionCS0, Vector3d wheelAxleCS, double suspensionRestLength, double wheelRadius, VehicleTuning tuning, boolean isFrontWheel) {
		WheelInfoConstructionInfo ci = new WheelInfoConstructionInfo();

		ci.chassisConnectionCS.set(connectionPointCS);
		ci.wheelDirectionCS.set(wheelDirectionCS0);
		ci.wheelAxleCS.set(wheelAxleCS);
		ci.suspensionRestLength = suspensionRestLength;
		ci.wheelRadius = wheelRadius;
		ci.suspensionStiffness = tuning.suspensionStiffness;
		ci.wheelsDampingCompression = tuning.suspensionCompression;
		ci.wheelsDampingRelaxation = tuning.suspensionDamping;
		ci.frictionSlip = tuning.frictionSlip;
		ci.bIsFrontWheel = isFrontWheel;
		ci.maxSuspensionTravelCm = tuning.maxSuspensionTravelCm;

		wheelInfo.add(new WheelInfo(ci));

		WheelInfo wheel = wheelInfo.getQuick(getNumWheels() - 1);

		updateWheelTransformsWS(wheel, false);
		updateWheelTransform(getNumWheels() - 1, false);
		return wheel;
	}

	public Transform getWheelTransformWS(int wheelIndex, Transform out) {
		assert (wheelIndex < getNumWheels());
		WheelInfo wheel = wheelInfo.getQuick(wheelIndex);
		out.set(wheel.worldTransform);
		return out;
	}

	public void updateWheelTransform(int wheelIndex) {
		updateWheelTransform(wheelIndex, true);
	}
	
	public void updateWheelTransform(int wheelIndex, boolean interpolatedTransform) {
		WheelInfo wheel = wheelInfo.getQuick(wheelIndex);
		updateWheelTransformsWS(wheel, interpolatedTransform);
		Vector3d up = new Vector3d();
		up.negate(wheel.raycastInfo.wheelDirectionWS);
		Vector3d right = wheel.raycastInfo.wheelAxleWS;
		Vector3d fwd = new Vector3d();
		fwd.cross(up, right);
		fwd.normalize();
		// up = right.cross(fwd);
		// up.normalize();

		// rotate around steering over de wheelAxleWS
		double steering = wheel.steering;

		Quat4d steeringOrn = new Quat4d();
		QuaternionUtil.setRotation(steeringOrn, up, steering); //wheel.m_steering);
		Matrix3d steeringMat = new Matrix3d();
		MatrixUtil.setRotation(steeringMat, steeringOrn);

		Quat4d rotatingOrn = new Quat4d();
		QuaternionUtil.setRotation(rotatingOrn, right, -wheel.rotation);
		Matrix3d rotatingMat = new Matrix3d();
		MatrixUtil.setRotation(rotatingMat, rotatingOrn);

		Matrix3d basis2 = new Matrix3d();
		basis2.setRow(0, right.x, fwd.x, up.x);
		basis2.setRow(1, right.y, fwd.y, up.y);
		basis2.setRow(2, right.z, fwd.z, up.z);

		Matrix3d wheelBasis = wheel.worldTransform.basis;
		wheelBasis.mul(steeringMat, rotatingMat);
		wheelBasis.mul(basis2);

		wheel.worldTransform.origin.scaleAdd(wheel.raycastInfo.suspensionLength, wheel.raycastInfo.wheelDirectionWS, wheel.raycastInfo.hardPointWS);
	}
	
	public void resetSuspension() {
		int i;
		for (i = 0; i < wheelInfo.size(); i++) {
			WheelInfo wheel = wheelInfo.getQuick(i);
			wheel.raycastInfo.suspensionLength = wheel.getSuspensionRestLength();
			wheel.suspensionRelativeVelocity = 0f;

			wheel.raycastInfo.contactNormalWS.negate(wheel.raycastInfo.wheelDirectionWS);
			//wheel_info.setContactFriction(btScalar(0.0));
			wheel.clippedInvContactDotSuspension = 1f;
		}
	}

	public void updateWheelTransformsWS(WheelInfo wheel) {
		updateWheelTransformsWS(wheel, true);
	}
	
	public void updateWheelTransformsWS(WheelInfo wheel, boolean interpolatedTransform) {
		wheel.raycastInfo.isInContact = false;

		Transform chassisTrans = getChassisWorldTransform(new Transform());
		if (interpolatedTransform && (getRigidBody().getMotionState() != null)) {
			getRigidBody().getMotionState().getWorldTransform(chassisTrans);
		}

		wheel.raycastInfo.hardPointWS.set(wheel.chassisConnectionPointCS);
		chassisTrans.transform(wheel.raycastInfo.hardPointWS);

		wheel.raycastInfo.wheelDirectionWS.set(wheel.wheelDirectionCS);
		chassisTrans.basis.transform(wheel.raycastInfo.wheelDirectionWS);

		wheel.raycastInfo.wheelAxleWS.set(wheel.wheelAxleCS);
		chassisTrans.basis.transform(wheel.raycastInfo.wheelAxleWS);
	}

	public double rayCast(WheelInfo wheel) {
		updateWheelTransformsWS(wheel, false);

		double depth = -1f;

		double raylen = wheel.getSuspensionRestLength() + wheel.wheelsRadius;

		Vector3d rayvector = new Vector3d();
		rayvector.scale(raylen, wheel.raycastInfo.wheelDirectionWS);
		Vector3d source = wheel.raycastInfo.hardPointWS;
		wheel.raycastInfo.contactPointWS.add(source, rayvector);
		Vector3d target = wheel.raycastInfo.contactPointWS;

		double param = 0f;

		VehicleRaycasterResult rayResults = new VehicleRaycasterResult();

		assert (vehicleRaycaster != null);

		Object object = vehicleRaycaster.castRay(source, target, rayResults);

		wheel.raycastInfo.groundObject = null;

		if (object != null) {
			param = rayResults.distFraction;
			depth = raylen * rayResults.distFraction;
			wheel.raycastInfo.contactNormalWS.set(rayResults.hitNormalInWorld);
			wheel.raycastInfo.isInContact = true;

			wheel.raycastInfo.groundObject = s_fixedObject; // todo for driving on dynamic/movable objects!;
			//wheel.m_raycastInfo.m_groundObject = object;

			double hitDistance = param * raylen;
			wheel.raycastInfo.suspensionLength = hitDistance - wheel.wheelsRadius;
			// clamp on max suspension travel

			double minSuspensionLength = wheel.getSuspensionRestLength() - wheel.maxSuspensionTravelCm * 0.01f;
			double maxSuspensionLength = wheel.getSuspensionRestLength() + wheel.maxSuspensionTravelCm * 0.01f;
			if (wheel.raycastInfo.suspensionLength < minSuspensionLength) {
				wheel.raycastInfo.suspensionLength = minSuspensionLength;
			}
			if (wheel.raycastInfo.suspensionLength > maxSuspensionLength) {
				wheel.raycastInfo.suspensionLength = maxSuspensionLength;
			}

			wheel.raycastInfo.contactPointWS.set(rayResults.hitPointInWorld);

			double denominator = wheel.raycastInfo.contactNormalWS.dot(wheel.raycastInfo.wheelDirectionWS);

			Vector3d chassis_velocity_at_contactPoint = new Vector3d();
			Vector3d relpos = new Vector3d();
			relpos.sub(wheel.raycastInfo.contactPointWS, getRigidBody().getCenterOfMassPosition(new Vector3d()));

			getRigidBody().getVelocityInLocalPoint(relpos, chassis_velocity_at_contactPoint);

			double projVel = wheel.raycastInfo.contactNormalWS.dot(chassis_velocity_at_contactPoint);

			if (denominator >= -0.1f) {
				wheel.suspensionRelativeVelocity = 0f;
				wheel.clippedInvContactDotSuspension = 1f / 0.1f;
			}
			else {
				double inv = -1f / denominator;
				wheel.suspensionRelativeVelocity = projVel * inv;
				wheel.clippedInvContactDotSuspension = inv;
			}

		}
		else {
			// put wheel info as in rest position
			wheel.raycastInfo.suspensionLength = wheel.getSuspensionRestLength();
			wheel.suspensionRelativeVelocity = 0f;
			wheel.raycastInfo.contactNormalWS.negate(wheel.raycastInfo.wheelDirectionWS);
			wheel.clippedInvContactDotSuspension = 1f;
		}

		return depth;
	}
	
	public Transform getChassisWorldTransform(Transform out) {
		/*
		if (getRigidBody()->getMotionState())
		{
			btTransform chassisWorldTrans;
			getRigidBody()->getMotionState()->getWorldTransform(chassisWorldTrans);
			return chassisWorldTrans;
		}
		*/

		return getRigidBody().getCenterOfMassTransform(out);
	}
	
	public void updateVehicle(double step) {
		for (int i = 0; i < getNumWheels(); i++) {
			updateWheelTransform(i, false);
		}
		
		Vector3d tmp = new Vector3d();

		currentVehicleSpeedKmHour = 3.6f * getRigidBody().getLinearVelocity(tmp).length();

		Transform chassisTrans = getChassisWorldTransform(new Transform());

		Vector3d forwardW = new Vector3d();
		forwardW.set(
				chassisTrans.basis.getElement(0, indexForwardAxis),
				chassisTrans.basis.getElement(1, indexForwardAxis),
				chassisTrans.basis.getElement(2, indexForwardAxis));

		if (forwardW.dot(getRigidBody().getLinearVelocity(tmp)) < 0f) {
			currentVehicleSpeedKmHour *= -1f;
		}

		//
		// simulate suspension
		//

		int i = 0;
		for (i = 0; i < wheelInfo.size(); i++) {
			double depth;
			depth = rayCast(wheelInfo.getQuick(i));
		}

		updateSuspension(step);

		for (i = 0; i < wheelInfo.size(); i++) {
			// apply suspension force
			WheelInfo wheel = wheelInfo.getQuick(i);

			double suspensionForce = wheel.wheelsSuspensionForce;

			double gMaxSuspensionForce = 6000f;
			if (suspensionForce > gMaxSuspensionForce) {
				suspensionForce = gMaxSuspensionForce;
			}
			Vector3d impulse = new Vector3d();
			impulse.scale(suspensionForce * step, wheel.raycastInfo.contactNormalWS);
			Vector3d relpos = new Vector3d();
			relpos.sub(wheel.raycastInfo.contactPointWS, getRigidBody().getCenterOfMassPosition(tmp));

			getRigidBody().applyImpulse(impulse, relpos);
		}

		updateFriction(step);

		for (i = 0; i < wheelInfo.size(); i++) {
			WheelInfo wheel = wheelInfo.getQuick(i);
			Vector3d relpos = new Vector3d();
			relpos.sub(wheel.raycastInfo.hardPointWS, getRigidBody().getCenterOfMassPosition(tmp));
			Vector3d vel = getRigidBody().getVelocityInLocalPoint(relpos, new Vector3d());

			if (wheel.raycastInfo.isInContact) {
				Transform chassisWorldTransform = getChassisWorldTransform(new Transform());

				Vector3d fwd = new Vector3d();
				fwd.set(
						chassisWorldTransform.basis.getElement(0, indexForwardAxis),
						chassisWorldTransform.basis.getElement(1, indexForwardAxis),
						chassisWorldTransform.basis.getElement(2, indexForwardAxis));

				double proj = fwd.dot(wheel.raycastInfo.contactNormalWS);
				tmp.scale(proj, wheel.raycastInfo.contactNormalWS);
				fwd.sub(tmp);

				double proj2 = fwd.dot(vel);

				wheel.deltaRotation = (proj2 * step) / (wheel.wheelsRadius);
				wheel.rotation += wheel.deltaRotation;

			}
			else {
				wheel.rotation += wheel.deltaRotation;
			}

			wheel.deltaRotation *= 0.99f; // damping of rotation when not in contact
		}
	}

	public void setSteeringValue(double steering, int wheel) {
		assert (wheel >= 0 && wheel < getNumWheels());

		WheelInfo wheel_info = getWheelInfo(wheel);
		wheel_info.steering = steering;
	}
	
	public double getSteeringValue(int wheel) {
		return getWheelInfo(wheel).steering;
	}

	public void applyEngineForce(double force, int wheel) {
		assert (wheel >= 0 && wheel < getNumWheels());
		WheelInfo wheel_info = getWheelInfo(wheel);
		wheel_info.engineForce = force;
	}

	public WheelInfo getWheelInfo(int index) {
		assert ((index >= 0) && (index < getNumWheels()));

		return wheelInfo.getQuick(index);
	}

	public void setBrake(double brake, int wheelIndex) {
		assert ((wheelIndex >= 0) && (wheelIndex < getNumWheels()));
		getWheelInfo(wheelIndex).brake = brake;
	}

	public void updateSuspension(double deltaTime) {
		double chassisMass = 1f / chassisBody.getInvMass();

		for (int w_it = 0; w_it < getNumWheels(); w_it++) {
			WheelInfo wheel_info = wheelInfo.getQuick(w_it);

			if (wheel_info.raycastInfo.isInContact) {
				double force;
				//	Spring
				{
					double susp_length = wheel_info.getSuspensionRestLength();
					double current_length = wheel_info.raycastInfo.suspensionLength;

					double length_diff = (susp_length - current_length);

					force = wheel_info.suspensionStiffness * length_diff * wheel_info.clippedInvContactDotSuspension;
				}

				// Damper
				{
					double projected_rel_vel = wheel_info.suspensionRelativeVelocity;
					{
						double susp_damping;
						if (projected_rel_vel < 0f) {
							susp_damping = wheel_info.wheelsDampingCompression;
						}
						else {
							susp_damping = wheel_info.wheelsDampingRelaxation;
						}
						force -= susp_damping * projected_rel_vel;
					}
				}

				// RESULT
				wheel_info.wheelsSuspensionForce = force * chassisMass;
				if (wheel_info.wheelsSuspensionForce < 0f) {
					wheel_info.wheelsSuspensionForce = 0f;
				}
			}
			else {
				wheel_info.wheelsSuspensionForce = 0f;
			}
		}
	}
	
	private double calcRollingFriction(WheelContactPoint contactPoint) {
		Vector3d tmp = new Vector3d();
		
		double j1 = 0f;
		
		Vector3d contactPosWorld = contactPoint.frictionPositionWorld;

		Vector3d rel_pos1 = new Vector3d();
		rel_pos1.sub(contactPosWorld, contactPoint.body0.getCenterOfMassPosition(tmp));
		Vector3d rel_pos2 = new Vector3d();
		rel_pos2.sub(contactPosWorld, contactPoint.body1.getCenterOfMassPosition(tmp));

		double maxImpulse = contactPoint.maxImpulse;

		Vector3d vel1 = contactPoint.body0.getVelocityInLocalPoint(rel_pos1, new Vector3d());
		Vector3d vel2 = contactPoint.body1.getVelocityInLocalPoint(rel_pos2, new Vector3d());
		Vector3d vel = new Vector3d();
		vel.sub(vel1, vel2);

		double vrel = contactPoint.frictionDirectionWorld.dot(vel);

		// calculate j that moves us to zero relative velocity
		j1 = -vrel * contactPoint.jacDiagABInv;
		j1 = Math.min(j1, maxImpulse);
		j1 = Math.max(j1, -maxImpulse);

		return j1;
	}
	
	public void updateFriction(double timeStep) {
		// calculate the impulse, so that the wheels don't move sidewards
		int numWheel = getNumWheels();
		if (numWheel == 0) {
			return;
		}

		MiscUtil.resize(forwardWS, numWheel, Vector3d.class);
		MiscUtil.resize(axle, numWheel, Vector3d.class);
		MiscUtil.resize(forwardImpulse, numWheel, 0f);
		MiscUtil.resize(sideImpulse, numWheel, 0f);

		Vector3d tmp = new Vector3d();

		int numWheelsOnGround = 0;

		// collapse all those loops into one!
		for (int i = 0; i < getNumWheels(); i++) {
			WheelInfo wheel_info = wheelInfo.getQuick(i);
			RigidBody groundObject = (RigidBody) wheel_info.raycastInfo.groundObject;
			if (groundObject != null) {
				numWheelsOnGround++;
			}
			sideImpulse.set(i, 0f);
			forwardImpulse.set(i, 0f);
		}

		{
			Transform wheelTrans = new Transform();
			for (int i = 0; i < getNumWheels(); i++) {

				WheelInfo wheel_info = wheelInfo.getQuick(i);

				RigidBody groundObject = (RigidBody) wheel_info.raycastInfo.groundObject;

				if (groundObject != null) {
					getWheelTransformWS(i, wheelTrans);

					Matrix3d wheelBasis0 = new Matrix3d(wheelTrans.basis);
					axle.getQuick(i).set(
							wheelBasis0.getElement(0, indexRightAxis),
							wheelBasis0.getElement(1, indexRightAxis),
							wheelBasis0.getElement(2, indexRightAxis));

					Vector3d surfNormalWS = wheel_info.raycastInfo.contactNormalWS;
					double proj = axle.getQuick(i).dot(surfNormalWS);
					tmp.scale(proj, surfNormalWS);
					axle.getQuick(i).sub(tmp);
					axle.getQuick(i).normalize();

					forwardWS.getQuick(i).cross(surfNormalWS, axle.getQuick(i));
					forwardWS.getQuick(i).normalize();

					double[] doublePtr = doubleArrays.getFixed(1);
					ContactConstraint.resolveSingleBilateral(chassisBody, wheel_info.raycastInfo.contactPointWS,
							groundObject, wheel_info.raycastInfo.contactPointWS,
							0f, axle.getQuick(i), doublePtr, timeStep);
					sideImpulse.set(i, doublePtr[0]);
					doubleArrays.release(doublePtr);

					sideImpulse.set(i, sideImpulse.get(i) * sideFrictionStiffness2);
				}
			}
		}

		double sideFactor = 1f;
		double fwdFactor = 0.5f;

		boolean sliding = false;
		{
			for (int wheel = 0; wheel < getNumWheels(); wheel++) {
				WheelInfo wheel_info = wheelInfo.getQuick(wheel);
				RigidBody groundObject = (RigidBody) wheel_info.raycastInfo.groundObject;

				double rollingFriction = 0f;

				if (groundObject != null) {
					if (wheel_info.engineForce != 0f) {
						rollingFriction = wheel_info.engineForce * timeStep;
					}
					else {
						double defaultRollingFrictionImpulse = 0f;
						double maxImpulse = wheel_info.brake != 0f ? wheel_info.brake : defaultRollingFrictionImpulse;
						WheelContactPoint contactPt = new WheelContactPoint(chassisBody, groundObject, wheel_info.raycastInfo.contactPointWS, forwardWS.getQuick(wheel), maxImpulse);
						rollingFriction = calcRollingFriction(contactPt);
					}
				}

				// switch between active rolling (throttle), braking and non-active rolling friction (no throttle/break)

				forwardImpulse.set(wheel, 0f);
				wheelInfo.getQuick(wheel).skidInfo = 1f;

				if (groundObject != null) {
					wheelInfo.getQuick(wheel).skidInfo = 1f;

					double maximp = wheel_info.wheelsSuspensionForce * timeStep * wheel_info.frictionSlip;
					double maximpSide = maximp;

					double maximpSquared = maximp * maximpSide;

					forwardImpulse.set(wheel, rollingFriction); //wheelInfo.m_engineForce* timeStep;

					double x = (forwardImpulse.get(wheel)) * fwdFactor;
					double y = (sideImpulse.get(wheel)) * sideFactor;

					double impulseSquared = (x * x + y * y);

					if (impulseSquared > maximpSquared) {
						sliding = true;

						double factor = maximp / (double) Math.sqrt(impulseSquared);

						wheelInfo.getQuick(wheel).skidInfo *= factor;
					}
				}

			}
		}

		if (sliding) {
			for (int wheel = 0; wheel < getNumWheels(); wheel++) {
				if (sideImpulse.get(wheel) != 0f) {
					if (wheelInfo.getQuick(wheel).skidInfo < 1f) {
						forwardImpulse.set(wheel, forwardImpulse.get(wheel) * wheelInfo.getQuick(wheel).skidInfo);
						sideImpulse.set(wheel, sideImpulse.get(wheel) * wheelInfo.getQuick(wheel).skidInfo);
					}
				}
			}
		}

		// apply the impulses
		{
			for (int wheel = 0; wheel < getNumWheels(); wheel++) {
				WheelInfo wheel_info = wheelInfo.getQuick(wheel);

				Vector3d rel_pos = new Vector3d();
				rel_pos.sub(wheel_info.raycastInfo.contactPointWS, chassisBody.getCenterOfMassPosition(tmp));

				if (forwardImpulse.get(wheel) != 0f) {
					tmp.scale(forwardImpulse.get(wheel), forwardWS.getQuick(wheel));
					chassisBody.applyImpulse(tmp, rel_pos);
				}
				if (sideImpulse.get(wheel) != 0f) {
					RigidBody groundObject = (RigidBody) wheelInfo.getQuick(wheel).raycastInfo.groundObject;

					Vector3d rel_pos2 = new Vector3d();
					rel_pos2.sub(wheel_info.raycastInfo.contactPointWS, groundObject.getCenterOfMassPosition(tmp));

					Vector3d sideImp = new Vector3d();
					sideImp.scale(sideImpulse.get(wheel), axle.getQuick(wheel));

					rel_pos.z *= wheel_info.rollInfluence;
					chassisBody.applyImpulse(sideImp, rel_pos);

					// apply friction impulse on the ground
					tmp.negate(sideImp);
					groundObject.applyImpulse(tmp, rel_pos2);
				}
			}
		}
	}
	
	@Override
	public void buildJacobian() {
		// not yet
	}

	@Override
	public void solveConstraint(double timeStep) {
		// not yet
	}
	
	public int getNumWheels() {
		return wheelInfo.size();
	}

	public void setPitchControl(double pitch) {
		this.pitchControl = pitch;
	}

	public RigidBody getRigidBody() {
		return chassisBody;
	}

	public int getRightAxis() {
		return indexRightAxis;
	}

	public int getUpAxis() {
		return indexUpAxis;
	}

	public int getForwardAxis() {
		return indexForwardAxis;
	}

	/**
	 * Worldspace forward vector.
	 */
	public Vector3d getForwardVector(Vector3d out) {
		Transform chassisTrans = getChassisWorldTransform(new Transform());

		out.set(
				chassisTrans.basis.getElement(0, indexForwardAxis),
				chassisTrans.basis.getElement(1, indexForwardAxis),
				chassisTrans.basis.getElement(2, indexForwardAxis));

		return out;
	}

	/**
	 * Velocity of vehicle (positive if velocity vector has same direction as foward vector).
	 */
	public double getCurrentSpeedKmHour() {
		return currentVehicleSpeedKmHour;
	}

	public void setCoordinateSystem(int rightIndex, int upIndex, int forwardIndex) {
		this.indexRightAxis = rightIndex;
		this.indexUpAxis = upIndex;
		this.indexForwardAxis = forwardIndex;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	private static class WheelContactPoint {
		public RigidBody body0;
		public RigidBody body1;
		public final Vector3d frictionPositionWorld = new Vector3d();
		public final Vector3d frictionDirectionWorld = new Vector3d();
		public double jacDiagABInv;
		public double maxImpulse;

		public WheelContactPoint(RigidBody body0, RigidBody body1, Vector3d frictionPosWorld, Vector3d frictionDirectionWorld, double maxImpulse) {
			this.body0 = body0;
			this.body1 = body1;
			this.frictionPositionWorld.set(frictionPosWorld);
			this.frictionDirectionWorld.set(frictionDirectionWorld);
			this.maxImpulse = maxImpulse;

			double denom0 = body0.computeImpulseDenominator(frictionPosWorld, frictionDirectionWorld);
			double denom1 = body1.computeImpulseDenominator(frictionPosWorld, frictionDirectionWorld);
			double relaxation = 1f;
			jacDiagABInv = relaxation / (denom0 + denom1);
		}
	}

}
