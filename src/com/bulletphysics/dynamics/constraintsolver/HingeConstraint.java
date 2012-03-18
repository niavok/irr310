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

/* Hinge Constraint by Dirk Gregorius. Limits added by Marcus Hennix at Starbreeze Studios */

package com.bulletphysics.dynamics.constraintsolver;

import javax.vecmath.Matrix3d;
import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.ScalarUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.TransformUtil;

/**
 * Hinge constraint between two rigid bodies each with a pivot point that descibes
 * the axis location in local space. Axis defines the orientation of the hinge axis.
 * 
 * @author jezek2
 */
public class HingeConstraint extends TypedConstraint {

	private JacobianEntry[] jac/*[3]*/ = new JacobianEntry[] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() }; // 3 orthogonal linear constraints
	private JacobianEntry[] jacAng/*[3]*/ = new JacobianEntry[] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() }; // 2 orthogonal angular constraints+ 1 for limit/motor

	private final Transform rbAFrame = new Transform(); // constraint axii. Assumes z is hinge axis.
	private final Transform rbBFrame = new Transform();

	private double motorTargetVelocity;
	private double maxMotorImpulse;

	private double limitSoftness; 
	private double biasFactor; 
	private double relaxationFactor; 

	private double lowerLimit;	
	private double upperLimit;	
	
	private double kHinge;

	private double limitSign;
	private double correction;

	private double accLimitImpulse;

	private boolean angularOnly;
	private boolean enableAngularMotor;
	private boolean solveLimit;

	public HingeConstraint() {
		super(TypedConstraintType.HINGE_CONSTRAINT_TYPE);
		enableAngularMotor = false;
	}

	public HingeConstraint(RigidBody rbA, RigidBody rbB, Vector3d pivotInA, Vector3d pivotInB, Vector3d axisInA, Vector3d axisInB) {
		super(TypedConstraintType.HINGE_CONSTRAINT_TYPE, rbA, rbB);
		angularOnly = false;
		enableAngularMotor = false;

		rbAFrame.origin.set(pivotInA);

		// since no frame is given, assume this to be zero angle and just pick rb transform axis
		Vector3d rbAxisA1 = new Vector3d();
		Vector3d rbAxisA2 = new Vector3d();
		
		Transform centerOfMassA = rbA.getCenterOfMassTransform(new Transform());
		centerOfMassA.basis.getColumn(0, rbAxisA1);
		double projection = axisInA.dot(rbAxisA1);

		if (projection >= 1.0f - BulletGlobals.SIMD_EPSILON) {
			centerOfMassA.basis.getColumn(2, rbAxisA1);
			rbAxisA1.negate();
			centerOfMassA.basis.getColumn(1, rbAxisA2);
		} else if (projection <= -1.0f + BulletGlobals.SIMD_EPSILON) {           
			centerOfMassA.basis.getColumn(2, rbAxisA1);                            
			centerOfMassA.basis.getColumn(1, rbAxisA2);
		} else {
			rbAxisA2.cross(axisInA, rbAxisA1);                                                                
			rbAxisA1.cross(rbAxisA2, axisInA);                                                                                            
		}

		rbAFrame.basis.setRow(0, rbAxisA1.x, rbAxisA2.x, axisInA.x);
		rbAFrame.basis.setRow(1, rbAxisA1.y, rbAxisA2.y, axisInA.y);
		rbAFrame.basis.setRow(2, rbAxisA1.z, rbAxisA2.z, axisInA.z);

		Quat4d rotationArc = QuaternionUtil.shortestArcQuat(axisInA, axisInB, new Quat4d());
		Vector3d rbAxisB1 = QuaternionUtil.quatRotate(rotationArc, rbAxisA1, new Vector3d());
		Vector3d rbAxisB2 = new Vector3d();
		rbAxisB2.cross(axisInB, rbAxisB1);

		rbBFrame.origin.set(pivotInB);
		rbBFrame.basis.setRow(0, rbAxisB1.x, rbAxisB2.x, -axisInB.x);
		rbBFrame.basis.setRow(1, rbAxisB1.y, rbAxisB2.y, -axisInB.y);
		rbBFrame.basis.setRow(2, rbAxisB1.z, rbAxisB2.z, -axisInB.z);			

		// start with free
		lowerLimit = 1e30f;
		upperLimit = -1e30f;
		biasFactor = 0.3f;
		relaxationFactor = 1.0f;
		limitSoftness = 0.9f;
		solveLimit = false;
	}

	public HingeConstraint(RigidBody rbA, Vector3d pivotInA, Vector3d axisInA) {
		super(TypedConstraintType.HINGE_CONSTRAINT_TYPE, rbA);
		angularOnly = false;
		enableAngularMotor = false;

		// since no frame is given, assume this to be zero angle and just pick rb transform axis
		// fixed axis in worldspace
		Vector3d rbAxisA1 = new Vector3d();
		Transform centerOfMassA = rbA.getCenterOfMassTransform(new Transform());
		centerOfMassA.basis.getColumn(0, rbAxisA1);

		double projection = rbAxisA1.dot(axisInA);
		if (projection > BulletGlobals.FLT_EPSILON) {
			rbAxisA1.scale(projection);
			rbAxisA1.sub(axisInA);
		}
		else {
			centerOfMassA.basis.getColumn(1, rbAxisA1);
		}

		Vector3d rbAxisA2 = new Vector3d();
		rbAxisA2.cross(axisInA, rbAxisA1);

		rbAFrame.origin.set(pivotInA);
		rbAFrame.basis.setRow(0, rbAxisA1.x, rbAxisA2.x, axisInA.x);
		rbAFrame.basis.setRow(1, rbAxisA1.y, rbAxisA2.y, axisInA.y);
		rbAFrame.basis.setRow(2, rbAxisA1.z, rbAxisA2.z, axisInA.z);

		Vector3d axisInB = new Vector3d();
		axisInB.negate(axisInA);
		centerOfMassA.basis.transform(axisInB);

		Quat4d rotationArc = QuaternionUtil.shortestArcQuat(axisInA, axisInB, new Quat4d());
		Vector3d rbAxisB1 = QuaternionUtil.quatRotate(rotationArc, rbAxisA1, new Vector3d());
		Vector3d rbAxisB2 = new Vector3d();
		rbAxisB2.cross(axisInB, rbAxisB1);

		rbBFrame.origin.set(pivotInA);
		centerOfMassA.transform(rbBFrame.origin);
		rbBFrame.basis.setRow(0, rbAxisB1.x, rbAxisB2.x, axisInB.x);
		rbBFrame.basis.setRow(1, rbAxisB1.y, rbAxisB2.y, axisInB.y);
		rbBFrame.basis.setRow(2, rbAxisB1.z, rbAxisB2.z, axisInB.z);

		// start with free
		lowerLimit = 1e30f;
		upperLimit = -1e30f;
		biasFactor = 0.3f;
		relaxationFactor = 1.0f;
		limitSoftness = 0.9f;
		solveLimit = false;
	}

	public HingeConstraint(RigidBody rbA, RigidBody rbB, Transform rbAFrame, Transform rbBFrame) {
		super(TypedConstraintType.HINGE_CONSTRAINT_TYPE, rbA, rbB);
		this.rbAFrame.set(rbAFrame);
		this.rbBFrame.set(rbBFrame);
		angularOnly = false;
		enableAngularMotor = false;

		// flip axis
		this.rbBFrame.basis.m02 *= -1f;
		this.rbBFrame.basis.m12 *= -1f;
		this.rbBFrame.basis.m22 *= -1f;

		// start with free
		lowerLimit = 1e30f;
		upperLimit = -1e30f;
		biasFactor = 0.3f;
		relaxationFactor = 1.0f;
		limitSoftness = 0.9f;
		solveLimit = false;
	}

	public HingeConstraint(RigidBody rbA, Transform rbAFrame) {
		super(TypedConstraintType.HINGE_CONSTRAINT_TYPE, rbA);
		this.rbAFrame.set(rbAFrame);
		this.rbBFrame.set(rbAFrame);
		angularOnly = false;
		enableAngularMotor = false;

		// not providing rigidbody B means implicitly using worldspace for body B

		// flip axis
		this.rbBFrame.basis.m02 *= -1f;
		this.rbBFrame.basis.m12 *= -1f;
		this.rbBFrame.basis.m22 *= -1f;

		this.rbBFrame.origin.set(this.rbAFrame.origin);
		rbA.getCenterOfMassTransform(new Transform()).transform(this.rbBFrame.origin);

		// start with free
		lowerLimit = 1e30f;
		upperLimit = -1e30f;
		biasFactor = 0.3f;
		relaxationFactor = 1.0f;
		limitSoftness = 0.9f;
		solveLimit = false;
	}
	
	@Override
	public void buildJacobian() {
		Vector3d tmp = new Vector3d();
		Vector3d tmp1 = new Vector3d();
		Vector3d tmp2 = new Vector3d();
		Vector3d tmpVec = new Vector3d();
		Matrix3d mat1 = new Matrix3d();
		Matrix3d mat2 = new Matrix3d();
		
		Transform centerOfMassA = rbA.getCenterOfMassTransform(new Transform());
		Transform centerOfMassB = rbB.getCenterOfMassTransform(new Transform());

		appliedImpulse = 0f;

		if (!angularOnly) {
			Vector3d pivotAInW = new Vector3d(rbAFrame.origin);
			centerOfMassA.transform(pivotAInW);

			Vector3d pivotBInW = new Vector3d(rbBFrame.origin);
			centerOfMassB.transform(pivotBInW);

			Vector3d relPos = new Vector3d();
			relPos.sub(pivotBInW, pivotAInW);

			Vector3d[] normal/*[3]*/ = new Vector3d[]{new Vector3d(), new Vector3d(), new Vector3d()};
			if (relPos.lengthSquared() > BulletGlobals.FLT_EPSILON) {
				normal[0].set(relPos);
				normal[0].normalize();
			}
			else {
				normal[0].set(1f, 0f, 0f);
			}

			TransformUtil.planeSpace1(normal[0], normal[1], normal[2]);

			for (int i = 0; i < 3; i++) {
				mat1.transpose(centerOfMassA.basis);
				mat2.transpose(centerOfMassB.basis);

				tmp1.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));
				tmp2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));

				jac[i].init(
						mat1,
						mat2,
						tmp1,
						tmp2,
						normal[i],
						rbA.getInvInertiaDiagLocal(new Vector3d()),
						rbA.getInvMass(),
						rbB.getInvInertiaDiagLocal(new Vector3d()),
						rbB.getInvMass());
			}
		}

		// calculate two perpendicular jointAxis, orthogonal to hingeAxis
		// these two jointAxis require equal angular velocities for both bodies

		// this is unused for now, it's a todo
		Vector3d jointAxis0local = new Vector3d();
		Vector3d jointAxis1local = new Vector3d();

		rbAFrame.basis.getColumn(2, tmp);
		TransformUtil.planeSpace1(tmp, jointAxis0local, jointAxis1local);

		// TODO: check this
		//getRigidBodyA().getCenterOfMassTransform().getBasis() * m_rbAFrame.getBasis().getColumn(2);

		Vector3d jointAxis0 = new Vector3d(jointAxis0local);
		centerOfMassA.basis.transform(jointAxis0);

		Vector3d jointAxis1 = new Vector3d(jointAxis1local);
		centerOfMassA.basis.transform(jointAxis1);

		Vector3d hingeAxisWorld = new Vector3d();
		rbAFrame.basis.getColumn(2, hingeAxisWorld);
		centerOfMassA.basis.transform(hingeAxisWorld);

		mat1.transpose(centerOfMassA.basis);
		mat2.transpose(centerOfMassB.basis);
		jacAng[0].init(jointAxis0,
				mat1,
				mat2,
				rbA.getInvInertiaDiagLocal(new Vector3d()),
				rbB.getInvInertiaDiagLocal(new Vector3d()));

		// JAVA NOTE: reused mat1 and mat2, as recomputation is not needed
		jacAng[1].init(jointAxis1,
				mat1,
				mat2,
				rbA.getInvInertiaDiagLocal(new Vector3d()),
				rbB.getInvInertiaDiagLocal(new Vector3d()));

		// JAVA NOTE: reused mat1 and mat2, as recomputation is not needed
		jacAng[2].init(hingeAxisWorld,
				mat1,
				mat2,
				rbA.getInvInertiaDiagLocal(new Vector3d()),
				rbB.getInvInertiaDiagLocal(new Vector3d()));

		// Compute limit information
		double hingeAngle = getHingeAngle();

		//set bias, sign, clear accumulator
		correction = 0f;
		limitSign = 0f;
		solveLimit = false;
		accLimitImpulse = 0f;

		if (lowerLimit < upperLimit) {
			if (hingeAngle <= lowerLimit * limitSoftness) {
				correction = (lowerLimit - hingeAngle);
				limitSign = 1.0f;
				solveLimit = true;
			}
			else if (hingeAngle >= upperLimit * limitSoftness) {
				correction = upperLimit - hingeAngle;
				limitSign = -1.0f;
				solveLimit = true;
			}
		}

		// Compute K = J*W*J' for hinge axis
		Vector3d axisA = new Vector3d();
		rbAFrame.basis.getColumn(2, axisA);
		centerOfMassA.basis.transform(axisA);

		kHinge = 1.0f / (getRigidBodyA().computeAngularImpulseDenominator(axisA) +
				getRigidBodyB().computeAngularImpulseDenominator(axisA));
	}

	@Override
	public void solveConstraint(double timeStep) {
		Vector3d tmp = new Vector3d();
		Vector3d tmp2 = new Vector3d();
		Vector3d tmpVec = new Vector3d();

		Transform centerOfMassA = rbA.getCenterOfMassTransform(new Transform());
		Transform centerOfMassB = rbB.getCenterOfMassTransform(new Transform());
		
		Vector3d pivotAInW = new Vector3d(rbAFrame.origin);
		centerOfMassA.transform(pivotAInW);

		Vector3d pivotBInW = new Vector3d(rbBFrame.origin);
		centerOfMassB.transform(pivotBInW);

		double tau = 0.3f;

		// linear part
		if (!angularOnly) {
			Vector3d rel_pos1 = new Vector3d();
			rel_pos1.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));

			Vector3d rel_pos2 = new Vector3d();
			rel_pos2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));

			Vector3d vel1 = rbA.getVelocityInLocalPoint(rel_pos1, new Vector3d());
			Vector3d vel2 = rbB.getVelocityInLocalPoint(rel_pos2, new Vector3d());
			Vector3d vel = new Vector3d();
			vel.sub(vel1, vel2);

			for (int i = 0; i < 3; i++) {
				Vector3d normal = jac[i].linearJointAxis;
				double jacDiagABInv = 1f / jac[i].getDiagonal();

				double rel_vel;
				rel_vel = normal.dot(vel);
				// positional error (zeroth order error)
				tmp.sub(pivotAInW, pivotBInW);
				double depth = -(tmp).dot(normal); // this is the error projected on the normal
				double impulse = depth * tau / timeStep * jacDiagABInv - rel_vel * jacDiagABInv;
				appliedImpulse += impulse;
				Vector3d impulse_vector = new Vector3d();
				impulse_vector.scale(impulse, normal);

				tmp.sub(pivotAInW, rbA.getCenterOfMassPosition(tmpVec));
				rbA.applyImpulse(impulse_vector, tmp);

				tmp.negate(impulse_vector);
				tmp2.sub(pivotBInW, rbB.getCenterOfMassPosition(tmpVec));
				rbB.applyImpulse(tmp, tmp2);
			}
		}


		{
			// solve angular part

			// get axes in world space
			Vector3d axisA = new Vector3d();
			rbAFrame.basis.getColumn(2, axisA);
			centerOfMassA.basis.transform(axisA);

			Vector3d axisB = new Vector3d();
			rbBFrame.basis.getColumn(2, axisB);
			centerOfMassB.basis.transform(axisB);

			Vector3d angVelA = getRigidBodyA().getAngularVelocity(new Vector3d());
			Vector3d angVelB = getRigidBodyB().getAngularVelocity(new Vector3d());

			Vector3d angVelAroundHingeAxisA = new Vector3d();
			angVelAroundHingeAxisA.scale(axisA.dot(angVelA), axisA);

			Vector3d angVelAroundHingeAxisB = new Vector3d();
			angVelAroundHingeAxisB.scale(axisB.dot(angVelB), axisB);

			Vector3d angAorthog = new Vector3d();
			angAorthog.sub(angVelA, angVelAroundHingeAxisA);

			Vector3d angBorthog = new Vector3d();
			angBorthog.sub(angVelB, angVelAroundHingeAxisB);

			Vector3d velrelOrthog = new Vector3d();
			velrelOrthog.sub(angAorthog, angBorthog);

			{
				// solve orthogonal angular velocity correction
				double relaxation = 1f;
				double len = velrelOrthog.length();
				if (len > 0.00001f) {
					Vector3d normal = new Vector3d();
					normal.normalize(velrelOrthog);

					double denom = getRigidBodyA().computeAngularImpulseDenominator(normal) +
							getRigidBodyB().computeAngularImpulseDenominator(normal);
					// scale for mass and relaxation
					// todo:  expose this 0.9 factor to developer
					velrelOrthog.scale((1f / denom) * relaxationFactor);
				}

				// solve angular positional correction
				// TODO: check
				//Vector3d angularError = -axisA.cross(axisB) *(btScalar(1.)/timeStep);
				Vector3d angularError = new Vector3d();
				angularError.cross(axisA, axisB);
				angularError.negate();
				angularError.scale(1f / timeStep);
				double len2 = angularError.length();
				if (len2 > 0.00001f) {
					Vector3d normal2 = new Vector3d();
					normal2.normalize(angularError);

					double denom2 = getRigidBodyA().computeAngularImpulseDenominator(normal2) +
							getRigidBodyB().computeAngularImpulseDenominator(normal2);
					angularError.scale((1f / denom2) * relaxation);
				}

				tmp.negate(velrelOrthog);
				tmp.add(angularError);
				rbA.applyTorqueImpulse(tmp);

				tmp.sub(velrelOrthog, angularError);
				rbB.applyTorqueImpulse(tmp);

				// solve limit
				if (solveLimit) {
					tmp.sub(angVelB, angVelA);
					double amplitude = ((tmp).dot(axisA) * relaxationFactor + correction * (1f / timeStep) * biasFactor) * limitSign;

					double impulseMag = amplitude * kHinge;

					// Clamp the accumulated impulse
					double temp = accLimitImpulse;
					accLimitImpulse = Math.max(accLimitImpulse + impulseMag, 0f);
					impulseMag = accLimitImpulse - temp;

					Vector3d impulse = new Vector3d();
					impulse.scale(impulseMag * limitSign, axisA);

					rbA.applyTorqueImpulse(impulse);

					tmp.negate(impulse);
					rbB.applyTorqueImpulse(tmp);
				}
			}

			// apply motor
			if (enableAngularMotor) {
				// todo: add limits too
				Vector3d angularLimit = new Vector3d();
				angularLimit.set(0f, 0f, 0f);

				Vector3d velrel = new Vector3d();
				velrel.sub(angVelAroundHingeAxisA, angVelAroundHingeAxisB);
				double projRelVel = velrel.dot(axisA);

				double desiredMotorVel = motorTargetVelocity;
				double motor_relvel = desiredMotorVel - projRelVel;

				double unclippedMotorImpulse = kHinge * motor_relvel;
				// todo: should clip against accumulated impulse
				double clippedMotorImpulse = unclippedMotorImpulse > maxMotorImpulse ? maxMotorImpulse : unclippedMotorImpulse;
				clippedMotorImpulse = clippedMotorImpulse < -maxMotorImpulse ? -maxMotorImpulse : clippedMotorImpulse;
				Vector3d motorImp = new Vector3d();
				motorImp.scale(clippedMotorImpulse, axisA);

				tmp.add(motorImp, angularLimit);
				rbA.applyTorqueImpulse(tmp);

				tmp.negate(motorImp);
				tmp.sub(angularLimit);
				rbB.applyTorqueImpulse(tmp);
			}
		}
	}

	public void updateRHS(double timeStep) {
	}

	public double getHingeAngle() {
		Transform centerOfMassA = rbA.getCenterOfMassTransform(new Transform());
		Transform centerOfMassB = rbB.getCenterOfMassTransform(new Transform());
		
		Vector3d refAxis0 = new Vector3d();
		rbAFrame.basis.getColumn(0, refAxis0);
		centerOfMassA.basis.transform(refAxis0);

		Vector3d refAxis1 = new Vector3d();
		rbAFrame.basis.getColumn(1, refAxis1);
		centerOfMassA.basis.transform(refAxis1);

		Vector3d swingAxis = new Vector3d();
		rbBFrame.basis.getColumn(1, swingAxis);
		centerOfMassB.basis.transform(swingAxis);

		return ScalarUtil.atan2Fast(swingAxis.dot(refAxis0), swingAxis.dot(refAxis1));
	}
	
	public void setAngularOnly(boolean angularOnly) {
		this.angularOnly = angularOnly;
	}

	public void enableAngularMotor(boolean enableMotor, double targetVelocity, double maxMotorImpulse) {
		this.enableAngularMotor = enableMotor;
		this.motorTargetVelocity = targetVelocity;
		this.maxMotorImpulse = maxMotorImpulse;
	}

	public void setLimit(double low, double high) {
		setLimit(low, high, 0.9f, 0.3f, 1.0f);
	}

	public void setLimit(double low, double high, double _softness, double _biasFactor, double _relaxationFactor) {
		lowerLimit = low;
		upperLimit = high;

		limitSoftness = _softness;
		biasFactor = _biasFactor;
		relaxationFactor = _relaxationFactor;
	}

	public double getLowerLimit() {
		return lowerLimit;
	}

	public double getUpperLimit() {
		return upperLimit;
	}

	public Transform getAFrame(Transform out) {
		out.set(rbAFrame);
		return out;
	}

	public Transform getBFrame(Transform out) {
		out.set(rbBFrame);
		return out;
	}

	public boolean getSolveLimit() {
		return solveLimit;
	}

	public double getLimitSign() {
		return limitSign;
	}

	public boolean getAngularOnly() {
		return angularOnly;
	}

	public boolean getEnableAngularMotor() {
		return enableAngularMotor;
	}

	public double getMotorTargetVelosity() {
		return motorTargetVelocity;
	}

	public double getMaxMotorImpulse() {
		return maxMotorImpulse;
	}
	
}
