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

/*
Added by Roman Ponomarev (rponom@gmail.com)
April 04, 2008

TODO:
 - add clamping od accumulated impulse to improve stability
 - add conversion for ODE constraint solver
*/

package com.bulletphysics.dynamics.constraintsolver;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

// JAVA NOTE: SliderConstraint from 2.71

/**
 *
 * @author jezek2
 */
public class SliderConstraint extends TypedConstraint {
	
	public static final double SLIDER_CONSTRAINT_DEF_SOFTNESS    = 1.0f;
	public static final double SLIDER_CONSTRAINT_DEF_DAMPING     = 1.0f;
	public static final double SLIDER_CONSTRAINT_DEF_RESTITUTION = 0.7f;
	
	protected final Transform frameInA = new Transform();
	protected final Transform frameInB = new Transform();
	// use frameA fo define limits, if true
	protected boolean useLinearReferenceFrameA;
	// linear limits
	protected double lowerLinLimit;
	protected double upperLinLimit;
	// angular limits
	protected double lowerAngLimit;
	protected double upperAngLimit;
	// softness, restitution and damping for different cases
	// DirLin - moving inside linear limits
	// LimLin - hitting linear limit
	// DirAng - moving inside angular limits
	// LimAng - hitting angular limit
	// OrthoLin, OrthoAng - against constraint axis
	protected double softnessDirLin;
	protected double restitutionDirLin;
	protected double dampingDirLin;
	protected double softnessDirAng;
	protected double restitutionDirAng;
	protected double dampingDirAng;
	protected double softnessLimLin;
	protected double restitutionLimLin;
	protected double dampingLimLin;
	protected double softnessLimAng;
	protected double restitutionLimAng;
	protected double dampingLimAng;
	protected double softnessOrthoLin;
	protected double restitutionOrthoLin;
	protected double dampingOrthoLin;
	protected double softnessOrthoAng;
	protected double restitutionOrthoAng;
	protected double dampingOrthoAng;
	
	// for interlal use
	protected boolean solveLinLim;
	protected boolean solveAngLim;

	protected JacobianEntry[] jacLin = new JacobianEntry[/*3*/] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() };
	protected double[] jacLinDiagABInv = new double[3];

	protected JacobianEntry[] jacAng = new JacobianEntry[/*3*/] { new JacobianEntry(), new JacobianEntry(), new JacobianEntry() };

	protected double timeStep;
	protected final Transform calculatedTransformA = new Transform();
	protected final Transform calculatedTransformB = new Transform();

	protected final Vector3d sliderAxis = new Vector3d();
	protected final Vector3d realPivotAInW = new Vector3d();
	protected final Vector3d realPivotBInW = new Vector3d();
	protected final Vector3d projPivotInW = new Vector3d();
	protected final Vector3d delta = new Vector3d();
	protected final Vector3d depth = new Vector3d();
	protected final Vector3d relPosA = new Vector3d();
	protected final Vector3d relPosB = new Vector3d();

	protected double linPos;

	protected double angDepth;
	protected double kAngle;

	protected boolean poweredLinMotor;
	protected double targetLinMotorVelocity;
	protected double maxLinMotorForce;
	protected double accumulatedLinMotorImpulse;
	
	protected boolean poweredAngMotor;
	protected double targetAngMotorVelocity;
	protected double maxAngMotorForce;
	protected double accumulatedAngMotorImpulse;

    public SliderConstraint() {
		super(TypedConstraintType.SLIDER_CONSTRAINT_TYPE);
		useLinearReferenceFrameA = true;
		initParams();
	}

    public SliderConstraint(RigidBody rbA, RigidBody rbB, Transform frameInA, Transform frameInB ,boolean useLinearReferenceFrameA) {
		super(TypedConstraintType.SLIDER_CONSTRAINT_TYPE, rbA, rbB);
        this.frameInA.set(frameInA);
        this.frameInB.set(frameInB);
		this.useLinearReferenceFrameA = useLinearReferenceFrameA;
		initParams();
	}

	protected void initParams() {
		lowerLinLimit = 1f;
		upperLinLimit = -1f;
		lowerAngLimit = 0f;
		upperAngLimit = 0f;
		softnessDirLin = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionDirLin = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingDirLin = 0f;
		softnessDirAng = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionDirAng = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingDirAng = 0f;
		softnessOrthoLin = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionOrthoLin = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingOrthoLin = SLIDER_CONSTRAINT_DEF_DAMPING;
		softnessOrthoAng = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionOrthoAng = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingOrthoAng = SLIDER_CONSTRAINT_DEF_DAMPING;
		softnessLimLin = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionLimLin = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingLimLin = SLIDER_CONSTRAINT_DEF_DAMPING;
		softnessLimAng = SLIDER_CONSTRAINT_DEF_SOFTNESS;
		restitutionLimAng = SLIDER_CONSTRAINT_DEF_RESTITUTION;
		dampingLimAng = SLIDER_CONSTRAINT_DEF_DAMPING;

		poweredLinMotor = false;
		targetLinMotorVelocity = 0f;
		maxLinMotorForce = 0f;
		accumulatedLinMotorImpulse = 0f;

		poweredAngMotor = false;
		targetAngMotorVelocity = 0f;
		maxAngMotorForce = 0f;
		accumulatedAngMotorImpulse = 0f;
	}

	@Override
	public void buildJacobian() {
		if (useLinearReferenceFrameA) {
			buildJacobianInt(rbA, rbB, frameInA, frameInB);
		}
		else {
			buildJacobianInt(rbB, rbA, frameInB, frameInA);
		}
	}

	@Override
	public void solveConstraint(double timeStep) {
		this.timeStep = timeStep;
		if (useLinearReferenceFrameA) {
			solveConstraintInt(rbA, rbB);
		}
		else {
			solveConstraintInt(rbB, rbA);
		}
	}
	
	public Transform getCalculatedTransformA(Transform out) {
		out.set(calculatedTransformA);
		return out;
	}
	
	public Transform getCalculatedTransformB(Transform out) {
		out.set(calculatedTransformB);
		return out;
	}
	
	public Transform getFrameOffsetA(Transform out) {
		out.set(frameInA);
		return out;
	}

	public Transform getFrameOffsetB(Transform out) {
		out.set(frameInB);
		return out;
	}
	
	public double getLowerLinLimit() {
		return lowerLinLimit;
	}

	public void setLowerLinLimit(double lowerLimit) {
		this.lowerLinLimit = lowerLimit;
	}

	public double getUpperLinLimit() {
		return upperLinLimit;
	}

	public void setUpperLinLimit(double upperLimit) {
		this.upperLinLimit = upperLimit;
	}

	public double getLowerAngLimit() {
		return lowerAngLimit;
	}

	public void setLowerAngLimit(double lowerLimit) {
		this.lowerAngLimit = lowerLimit;
	}

	public double getUpperAngLimit() {
		return upperAngLimit;
	}

	public void setUpperAngLimit(double upperLimit) {
		this.upperAngLimit = upperLimit;
	}

	public boolean getUseLinearReferenceFrameA() {
		return useLinearReferenceFrameA;
	}
	
	public double getSoftnessDirLin() {
		return softnessDirLin;
	}

	public double getRestitutionDirLin() {
		return restitutionDirLin;
	}

	public double getDampingDirLin() {
		return dampingDirLin;
	}

	public double getSoftnessDirAng() {
		return softnessDirAng;
	}

	public double getRestitutionDirAng() {
		return restitutionDirAng;
	}

	public double getDampingDirAng() {
		return dampingDirAng;
	}

	public double getSoftnessLimLin() {
		return softnessLimLin;
	}

	public double getRestitutionLimLin() {
		return restitutionLimLin;
	}

	public double getDampingLimLin() {
		return dampingLimLin;
	}

	public double getSoftnessLimAng() {
		return softnessLimAng;
	}

	public double getRestitutionLimAng() {
		return restitutionLimAng;
	}

	public double getDampingLimAng() {
		return dampingLimAng;
	}

	public double getSoftnessOrthoLin() {
		return softnessOrthoLin;
	}

	public double getRestitutionOrthoLin() {
		return restitutionOrthoLin;
	}

	public double getDampingOrthoLin() {
		return dampingOrthoLin;
	}

	public double getSoftnessOrthoAng() {
		return softnessOrthoAng;
	}

	public double getRestitutionOrthoAng() {
		return restitutionOrthoAng;
	}

	public double getDampingOrthoAng() {
		return dampingOrthoAng;
	}
	
	public void setSoftnessDirLin(double softnessDirLin) {
		this.softnessDirLin = softnessDirLin;
	}

	public void setRestitutionDirLin(double restitutionDirLin) {
		this.restitutionDirLin = restitutionDirLin;
	}

	public void setDampingDirLin(double dampingDirLin) {
		this.dampingDirLin = dampingDirLin;
	}

	public void setSoftnessDirAng(double softnessDirAng) {
		this.softnessDirAng = softnessDirAng;
	}

	public void setRestitutionDirAng(double restitutionDirAng) {
		this.restitutionDirAng = restitutionDirAng;
	}

	public void setDampingDirAng(double dampingDirAng) {
		this.dampingDirAng = dampingDirAng;
	}

	public void setSoftnessLimLin(double softnessLimLin) {
		this.softnessLimLin = softnessLimLin;
	}

	public void setRestitutionLimLin(double restitutionLimLin) {
		this.restitutionLimLin = restitutionLimLin;
	}

	public void setDampingLimLin(double dampingLimLin) {
		this.dampingLimLin = dampingLimLin;
	}

	public void setSoftnessLimAng(double softnessLimAng) {
		this.softnessLimAng = softnessLimAng;
	}

	public void setRestitutionLimAng(double restitutionLimAng) {
		this.restitutionLimAng = restitutionLimAng;
	}

	public void setDampingLimAng(double dampingLimAng) {
		this.dampingLimAng = dampingLimAng;
	}

	public void setSoftnessOrthoLin(double softnessOrthoLin) {
		this.softnessOrthoLin = softnessOrthoLin;
	}

	public void setRestitutionOrthoLin(double restitutionOrthoLin) {
		this.restitutionOrthoLin = restitutionOrthoLin;
	}

	public void setDampingOrthoLin(double dampingOrthoLin) {
		this.dampingOrthoLin = dampingOrthoLin;
	}

	public void setSoftnessOrthoAng(double softnessOrthoAng) {
		this.softnessOrthoAng = softnessOrthoAng;
	}

	public void setRestitutionOrthoAng(double restitutionOrthoAng) {
		this.restitutionOrthoAng = restitutionOrthoAng;
	}

	public void setDampingOrthoAng(double dampingOrthoAng) {
		this.dampingOrthoAng = dampingOrthoAng;
	}

	public void setPoweredLinMotor(boolean onOff) {
		this.poweredLinMotor = onOff;
	}

	public boolean getPoweredLinMotor() {
		return poweredLinMotor;
	}

	public void setTargetLinMotorVelocity(double targetLinMotorVelocity) {
		this.targetLinMotorVelocity = targetLinMotorVelocity;
	}

	public double getTargetLinMotorVelocity() {
		return targetLinMotorVelocity;
	}

	public void setMaxLinMotorForce(double maxLinMotorForce) {
		this.maxLinMotorForce = maxLinMotorForce;
	}

	public double getMaxLinMotorForce() {
		return maxLinMotorForce;
	}

	public void setPoweredAngMotor(boolean onOff) {
		this.poweredAngMotor = onOff;
	}

	public boolean getPoweredAngMotor() {
		return poweredAngMotor;
	}

	public void setTargetAngMotorVelocity(double targetAngMotorVelocity) {
		this.targetAngMotorVelocity = targetAngMotorVelocity;
	}

	public double getTargetAngMotorVelocity() {
		return targetAngMotorVelocity;
	}

	public void setMaxAngMotorForce(double maxAngMotorForce) {
		this.maxAngMotorForce = maxAngMotorForce;
	}

	public double getMaxAngMotorForce() {
		return this.maxAngMotorForce;
	}

	public double getLinearPos() {
		return this.linPos;
	}

	// access for ODE solver

	public boolean getSolveLinLimit() {
		return solveLinLim;
	}

	public double getLinDepth() {
		return depth.x;
	}

	public boolean getSolveAngLimit() {
		return solveAngLim;
	}

	public double getAngDepth() {
		return angDepth;
	}
	
	// internal
	
	public void buildJacobianInt(RigidBody rbA, RigidBody rbB, Transform frameInA, Transform frameInB) {
		Transform tmpTrans = new Transform();
		Transform tmpTrans1 = new Transform();
		Transform tmpTrans2 = new Transform();
		Vector3d tmp = new Vector3d();
		Vector3d tmp2 = new Vector3d();

		// calculate transforms
		calculatedTransformA.mul(rbA.getCenterOfMassTransform(tmpTrans), frameInA);
		calculatedTransformB.mul(rbB.getCenterOfMassTransform(tmpTrans), frameInB);
		realPivotAInW.set(calculatedTransformA.origin);
		realPivotBInW.set(calculatedTransformB.origin);
		calculatedTransformA.basis.getColumn(0, tmp);
		sliderAxis.set(tmp); // along X
		delta.sub(realPivotBInW, realPivotAInW);
		projPivotInW.scaleAdd(sliderAxis.dot(delta), sliderAxis, realPivotAInW);
		relPosA.sub(projPivotInW, rbA.getCenterOfMassPosition(tmp));
		relPosB.sub(realPivotBInW, rbB.getCenterOfMassPosition(tmp));
		Vector3d normalWorld = new Vector3d();

		// linear part
		for (int i=0; i<3; i++) {
			calculatedTransformA.basis.getColumn(i, normalWorld);

			Matrix3d mat1 = rbA.getCenterOfMassTransform(tmpTrans1).basis;
			mat1.transpose();

			Matrix3d mat2 = rbB.getCenterOfMassTransform(tmpTrans2).basis;
			mat2.transpose();

			jacLin[i].init(
					mat1,
					mat2,
					relPosA,
					relPosB,
					normalWorld,
					rbA.getInvInertiaDiagLocal(tmp),
					rbA.getInvMass(),
					rbB.getInvInertiaDiagLocal(tmp2),
					rbB.getInvMass());
			jacLinDiagABInv[i] = 1f / jacLin[i].getDiagonal();
			VectorUtil.setCoord(depth, i, delta.dot(normalWorld));
		}
		testLinLimits();

		// angular part
		for (int i=0; i<3; i++) {
			calculatedTransformA.basis.getColumn(i, normalWorld);

			Matrix3d mat1 = rbA.getCenterOfMassTransform(tmpTrans1).basis;
			mat1.transpose();

			Matrix3d mat2 = rbB.getCenterOfMassTransform(tmpTrans2).basis;
			mat2.transpose();

			jacAng[i].init(
					normalWorld,
					mat1,
					mat2,
					rbA.getInvInertiaDiagLocal(tmp),
					rbB.getInvInertiaDiagLocal(tmp2));
		}
		testAngLimits();

		Vector3d axisA = new Vector3d();
		calculatedTransformA.basis.getColumn(0, axisA);
		kAngle = 1f / (rbA.computeAngularImpulseDenominator(axisA) + rbB.computeAngularImpulseDenominator(axisA));
		// clear accumulator for motors
		accumulatedLinMotorImpulse = 0f;
		accumulatedAngMotorImpulse = 0f;
	}
	
	public void solveConstraintInt(RigidBody rbA, RigidBody rbB) {
		Vector3d tmp = new Vector3d();

		// linear
		Vector3d velA = rbA.getVelocityInLocalPoint(relPosA, new Vector3d());
		Vector3d velB = rbB.getVelocityInLocalPoint(relPosB, new Vector3d());
		Vector3d vel = new Vector3d();
		vel.sub(velA, velB);

		Vector3d impulse_vector = new Vector3d();

		for (int i=0; i<3; i++) {
			Vector3d normal = jacLin[i].linearJointAxis;
			double rel_vel = normal.dot(vel);
			// calculate positional error
			double depth = VectorUtil.getCoord(this.depth, i);
			// get parameters
			double softness = (i != 0)? softnessOrthoLin : (solveLinLim? softnessLimLin : softnessDirLin);
			double restitution = (i != 0)? restitutionOrthoLin : (solveLinLim? restitutionLimLin : restitutionDirLin);
			double damping = (i != 0)? dampingOrthoLin : (solveLinLim? dampingLimLin : dampingDirLin);
			// calcutate and apply impulse
			double normalImpulse = softness * (restitution * depth / timeStep - damping * rel_vel) * jacLinDiagABInv[i];
			impulse_vector.scale(normalImpulse, normal);
			rbA.applyImpulse(impulse_vector, relPosA);
			tmp.negate(impulse_vector);
			rbB.applyImpulse(tmp, relPosB);

			if (poweredLinMotor && (i == 0)) {
				// apply linear motor
				if (accumulatedLinMotorImpulse < maxLinMotorForce) {
					double desiredMotorVel = targetLinMotorVelocity;
					double motor_relvel = desiredMotorVel + rel_vel;
					normalImpulse = -motor_relvel * jacLinDiagABInv[i];
					// clamp accumulated impulse
					double new_acc = accumulatedLinMotorImpulse + Math.abs(normalImpulse);
					if (new_acc > maxLinMotorForce) {
						new_acc = maxLinMotorForce;
					}
					double del = new_acc - accumulatedLinMotorImpulse;
					if (normalImpulse < 0f) {
						normalImpulse = -del;
					}
					else {
						normalImpulse = del;
					}
					accumulatedLinMotorImpulse = new_acc;
					// apply clamped impulse
					impulse_vector.scale(normalImpulse, normal);
					rbA.applyImpulse(impulse_vector, relPosA);
					tmp.negate(impulse_vector);
					rbB.applyImpulse(tmp, relPosB);
				}
			}
		}

		// angular
		// get axes in world space
		Vector3d axisA = new Vector3d();
		calculatedTransformA.basis.getColumn(0, axisA);
		Vector3d axisB = new Vector3d();
		calculatedTransformB.basis.getColumn(0, axisB);

		Vector3d angVelA = rbA.getAngularVelocity(new Vector3d());
		Vector3d angVelB = rbB.getAngularVelocity(new Vector3d());

		Vector3d angVelAroundAxisA = new Vector3d();
		angVelAroundAxisA.scale(axisA.dot(angVelA), axisA);
		Vector3d angVelAroundAxisB = new Vector3d();
		angVelAroundAxisB.scale(axisB.dot(angVelB), axisB);

		Vector3d angAorthog = new Vector3d();
		angAorthog.sub(angVelA, angVelAroundAxisA);
		Vector3d angBorthog = new Vector3d();
		angBorthog.sub(angVelB, angVelAroundAxisB);
		Vector3d velrelOrthog = new Vector3d();
		velrelOrthog.sub(angAorthog, angBorthog);

		// solve orthogonal angular velocity correction
		double len = velrelOrthog.length();
		if (len > 0.00001f) {
			Vector3d normal = new Vector3d();
			normal.normalize(velrelOrthog);
			double denom = rbA.computeAngularImpulseDenominator(normal) + rbB.computeAngularImpulseDenominator(normal);
			velrelOrthog.scale((1f / denom) * dampingOrthoAng * softnessOrthoAng);
		}

		// solve angular positional correction
		Vector3d angularError = new Vector3d();
		angularError.cross(axisA, axisB);
		angularError.scale(1f / timeStep);
		double len2 = angularError.length();
		if (len2 > 0.00001f) {
			Vector3d normal2 = new Vector3d();
			normal2.normalize(angularError);
			double denom2 = rbA.computeAngularImpulseDenominator(normal2) + rbB.computeAngularImpulseDenominator(normal2);
			angularError.scale((1f / denom2) * restitutionOrthoAng * softnessOrthoAng);
		}

		// apply impulse
		tmp.negate(velrelOrthog);
		tmp.add(angularError);
		rbA.applyTorqueImpulse(tmp);
		tmp.sub(velrelOrthog, angularError);
		rbB.applyTorqueImpulse(tmp);
		double impulseMag;

		// solve angular limits
		if (solveAngLim) {
			tmp.sub(angVelB, angVelA);
			impulseMag = tmp.dot(axisA) * dampingLimAng + angDepth * restitutionLimAng / timeStep;
			impulseMag *= kAngle * softnessLimAng;
		}
		else {
			tmp.sub(angVelB, angVelA);
			impulseMag = tmp.dot(axisA) * dampingDirAng + angDepth * restitutionDirAng / timeStep;
			impulseMag *= kAngle * softnessDirAng;
		}
		Vector3d impulse = new Vector3d();
		impulse.scale(impulseMag, axisA);
		rbA.applyTorqueImpulse(impulse);
		tmp.negate(impulse);
		rbB.applyTorqueImpulse(tmp);

		// apply angular motor
		if (poweredAngMotor) {
			if (accumulatedAngMotorImpulse < maxAngMotorForce) {
				Vector3d velrel = new Vector3d();
				velrel.sub(angVelAroundAxisA, angVelAroundAxisB);
				double projRelVel = velrel.dot(axisA);

				double desiredMotorVel = targetAngMotorVelocity;
				double motor_relvel = desiredMotorVel - projRelVel;

				double angImpulse = kAngle * motor_relvel;
				// clamp accumulated impulse
				double new_acc = accumulatedAngMotorImpulse + Math.abs(angImpulse);
				if (new_acc > maxAngMotorForce) {
					new_acc = maxAngMotorForce;
				}
				double del = new_acc - accumulatedAngMotorImpulse;
				if (angImpulse < 0f) {
					angImpulse = -del;
				} else {
					angImpulse = del;
				}
				accumulatedAngMotorImpulse = new_acc;

				// apply clamped impulse
				Vector3d motorImp = new Vector3d();
				motorImp.scale(angImpulse, axisA);
				rbA.applyTorqueImpulse(motorImp);
				tmp.negate(motorImp);
				rbB.applyTorqueImpulse(tmp);
			}
		}
	}
	
	// shared code used by ODE solver
	
	public void calculateTransforms() {
		Transform tmpTrans = new Transform();

		if (useLinearReferenceFrameA) {
			calculatedTransformA.mul(rbA.getCenterOfMassTransform(tmpTrans), frameInA);
			calculatedTransformB.mul(rbB.getCenterOfMassTransform(tmpTrans), frameInB);
		}
		else {
			calculatedTransformA.mul(rbB.getCenterOfMassTransform(tmpTrans), frameInB);
			calculatedTransformB.mul(rbA.getCenterOfMassTransform(tmpTrans), frameInA);
		}
		realPivotAInW.set(calculatedTransformA.origin);
		realPivotBInW.set(calculatedTransformB.origin);
		calculatedTransformA.basis.getColumn(0, sliderAxis); // along X
		delta.sub(realPivotBInW, realPivotAInW);
		projPivotInW.scaleAdd(sliderAxis.dot(delta), sliderAxis, realPivotAInW);
		Vector3d normalWorld = new Vector3d();
		// linear part
		for (int i=0; i<3; i++) {
			calculatedTransformA.basis.getColumn(i, normalWorld);
			VectorUtil.setCoord(depth, i, delta.dot(normalWorld));
		}
	}

	public void testLinLimits() {
		solveLinLim = false;
		linPos = depth.x;
		if (lowerLinLimit <= upperLinLimit) {
			if (depth.x > upperLinLimit) {
				depth.x -= upperLinLimit;
				solveLinLim = true;
			}
			else if (depth.x < lowerLinLimit) {
				depth.x -= lowerLinLimit;
				solveLinLim = true;
			}
			else {
				depth.x = 0f;
			}
		}
		else {
			depth.x = 0f;
		}
	}
	
	public void testAngLimits() {
		angDepth = 0f;
		solveAngLim = false;
		if (lowerAngLimit <= upperAngLimit) {
			Vector3d axisA0 = new Vector3d();
			calculatedTransformA.basis.getColumn(1, axisA0);
			Vector3d axisA1 = new Vector3d();
			calculatedTransformA.basis.getColumn(2, axisA1);
			Vector3d axisB0 = new Vector3d();
			calculatedTransformB.basis.getColumn(1, axisB0);

			double rot = (double) Math.atan2(axisB0.dot(axisA1), axisB0.dot(axisA0));
			if (rot < lowerAngLimit) {
				angDepth = rot - lowerAngLimit;
				solveAngLim = true;
			}
			else if (rot > upperAngLimit) {
				angDepth = rot - upperAngLimit;
				solveAngLim = true;
			}
		}
	}
	
	// access for PE Solver
	
	public Vector3d getAncorInA(Vector3d out) {
		Transform tmpTrans = new Transform();

		Vector3d ancorInA = out;
		ancorInA.scaleAdd((lowerLinLimit + upperLinLimit) * 0.5f, sliderAxis, realPivotAInW);
		rbA.getCenterOfMassTransform(tmpTrans);
		tmpTrans.inverse();
		tmpTrans.transform(ancorInA);
		return ancorInA;
	}

	public Vector3d getAncorInB(Vector3d out) {
		Vector3d ancorInB = out;
		ancorInB.set(frameInB.origin);
		return ancorInB;
	}

}
