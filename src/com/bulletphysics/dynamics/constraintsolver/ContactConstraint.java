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

package com.bulletphysics.dynamics.constraintsolver;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectPool;

/**
 * Functions for resolving contacts.
 * 
 * @author jezek2
 */
public class ContactConstraint {
	
	public static final ContactSolverFunc resolveSingleCollision = new ContactSolverFunc() {
		public double resolveContact(RigidBody body1, RigidBody body2, ManifoldPoint contactPoint, ContactSolverInfo info) {
			return resolveSingleCollision(body1, body2, contactPoint, info);
		}
	};

	public static final ContactSolverFunc resolveSingleFriction = new ContactSolverFunc() {
		public double resolveContact(RigidBody body1, RigidBody body2, ManifoldPoint contactPoint, ContactSolverInfo info) {
			return resolveSingleFriction(body1, body2, contactPoint, info);
		}
	};

	public static final ContactSolverFunc resolveSingleCollisionCombined = new ContactSolverFunc() {
		public double resolveContact(RigidBody body1, RigidBody body2, ManifoldPoint contactPoint, ContactSolverInfo info) {
			return resolveSingleCollisionCombined(body1, body2, contactPoint, info);
		}
	};
	
	/**
	 * Bilateral constraint between two dynamic objects.
	 */
	public static void resolveSingleBilateral(RigidBody body1, Vector3d pos1,
			RigidBody body2, Vector3d pos2,
			double distance, Vector3d normal, double[] impulse, double timeStep) {
		double normalLenSqr = normal.lengthSquared();
		assert (Math.abs(normalLenSqr) < 1.1f);
		if (normalLenSqr > 1.1f) {
			impulse[0] = 0f;
			return;
		}

		ObjectPool<JacobianEntry> jacobiansPool = ObjectPool.get(JacobianEntry.class);
		Vector3d tmp = new Vector3d();
		
		Vector3d rel_pos1 = new Vector3d();
		rel_pos1.sub(pos1, body1.getCenterOfMassPosition(tmp));

		Vector3d rel_pos2 = new Vector3d();
		rel_pos2.sub(pos2, body2.getCenterOfMassPosition(tmp));

		//this jacobian entry could be re-used for all iterations

		Vector3d vel1 = new Vector3d();
		body1.getVelocityInLocalPoint(rel_pos1, vel1);

		Vector3d vel2 = new Vector3d();
		body2.getVelocityInLocalPoint(rel_pos2, vel2);

		Vector3d vel = new Vector3d();
		vel.sub(vel1, vel2);

		Matrix3d mat1 = body1.getCenterOfMassTransform(new Transform()).basis;
		mat1.transpose();

		Matrix3d mat2 = body2.getCenterOfMassTransform(new Transform()).basis;
		mat2.transpose();

		JacobianEntry jac = jacobiansPool.get();
		jac.init(mat1, mat2,
				rel_pos1, rel_pos2, normal,
				body1.getInvInertiaDiagLocal(new Vector3d()), body1.getInvMass(),
				body2.getInvInertiaDiagLocal(new Vector3d()), body2.getInvMass());

		double jacDiagAB = jac.getDiagonal();
		double jacDiagABInv = 1f / jacDiagAB;

		Vector3d tmp1 = body1.getAngularVelocity(new Vector3d());
		mat1.transform(tmp1);

		Vector3d tmp2 = body2.getAngularVelocity(new Vector3d());
		mat2.transform(tmp2);

		double rel_vel = jac.getRelativeVelocity(
				body1.getLinearVelocity(new Vector3d()),
				tmp1,
				body2.getLinearVelocity(new Vector3d()),
				tmp2);

		jacobiansPool.release(jac);

		rel_vel = normal.dot(vel);

		// todo: move this into proper structure
		double contactDamping = 0.2f;

		//#ifdef ONLY_USE_LINEAR_MASS
		//	btScalar massTerm = btScalar(1.) / (body1.getInvMass() + body2.getInvMass());
		//	impulse = - contactDamping * rel_vel * massTerm;
		//#else	
		double velocityImpulse = -contactDamping * rel_vel * jacDiagABInv;
		impulse[0] = velocityImpulse;
		//#endif
	}

	/**
	 * Response between two dynamic objects with friction.
	 */
	public static double resolveSingleCollision(
			RigidBody body1,
			RigidBody body2,
			ManifoldPoint contactPoint,
			ContactSolverInfo solverInfo) {
		
		Vector3d tmpVec = new Vector3d();

		Vector3d pos1_ = contactPoint.getPositionWorldOnA(new Vector3d());
		Vector3d pos2_ = contactPoint.getPositionWorldOnB(new Vector3d());
		Vector3d normal = contactPoint.normalWorldOnB;

		// constant over all iterations
		Vector3d rel_pos1 = new Vector3d();
		rel_pos1.sub(pos1_, body1.getCenterOfMassPosition(tmpVec));

		Vector3d rel_pos2 = new Vector3d();
		rel_pos2.sub(pos2_, body2.getCenterOfMassPosition(tmpVec));

		Vector3d vel1 = body1.getVelocityInLocalPoint(rel_pos1, new Vector3d());
		Vector3d vel2 = body2.getVelocityInLocalPoint(rel_pos2, new Vector3d());
		Vector3d vel = new Vector3d();
		vel.sub(vel1, vel2);

		double rel_vel;
		rel_vel = normal.dot(vel);

		double Kfps = 1f / solverInfo.timeStep;

		// btScalar damping = solverInfo.m_damping ;
		double Kerp = solverInfo.erp;
		double Kcor = Kerp * Kfps;

		ConstraintPersistentData cpd = (ConstraintPersistentData) contactPoint.userPersistentData;
		assert (cpd != null);
		double distance = cpd.penetration;
		double positionalError = Kcor * -distance;
		double velocityError = cpd.restitution - rel_vel; // * damping;

		double penetrationImpulse = positionalError * cpd.jacDiagABInv;

		double velocityImpulse = velocityError * cpd.jacDiagABInv;

		double normalImpulse = penetrationImpulse + velocityImpulse;

		// See Erin Catto's GDC 2006 paper: Clamp the accumulated impulse
		double oldNormalImpulse = cpd.appliedImpulse;
		double sum = oldNormalImpulse + normalImpulse;
		cpd.appliedImpulse = 0f > sum ? 0f : sum;

		normalImpulse = cpd.appliedImpulse - oldNormalImpulse;

		//#ifdef USE_INTERNAL_APPLY_IMPULSE
		Vector3d tmp = new Vector3d();
		if (body1.getInvMass() != 0f) {
			tmp.scale(body1.getInvMass(), contactPoint.normalWorldOnB);
			body1.internalApplyImpulse(tmp, cpd.angularComponentA, normalImpulse);
		}
		if (body2.getInvMass() != 0f) {
			tmp.scale(body2.getInvMass(), contactPoint.normalWorldOnB);
			body2.internalApplyImpulse(tmp, cpd.angularComponentB, -normalImpulse);
		}
		//#else //USE_INTERNAL_APPLY_IMPULSE
		//	body1.applyImpulse(normal*(normalImpulse), rel_pos1);
		//	body2.applyImpulse(-normal*(normalImpulse), rel_pos2);
		//#endif //USE_INTERNAL_APPLY_IMPULSE

		return normalImpulse;
	}
	
	public static double resolveSingleFriction(
			RigidBody body1,
			RigidBody body2,
			ManifoldPoint contactPoint,
			ContactSolverInfo solverInfo) {
		
		Vector3d tmpVec = new Vector3d();
		
		Vector3d pos1 = contactPoint.getPositionWorldOnA(new Vector3d());
		Vector3d pos2 = contactPoint.getPositionWorldOnB(new Vector3d());

		Vector3d rel_pos1 = new Vector3d();
		rel_pos1.sub(pos1, body1.getCenterOfMassPosition(tmpVec));

		Vector3d rel_pos2 = new Vector3d();
		rel_pos2.sub(pos2, body2.getCenterOfMassPosition(tmpVec));

		ConstraintPersistentData cpd = (ConstraintPersistentData) contactPoint.userPersistentData;
		assert (cpd != null);

		double combinedFriction = cpd.friction;

		double limit = cpd.appliedImpulse * combinedFriction;

		if (cpd.appliedImpulse > 0f) //friction
		{
			//apply friction in the 2 tangential directions

			// 1st tangent
			Vector3d vel1 = new Vector3d();
			body1.getVelocityInLocalPoint(rel_pos1, vel1);

			Vector3d vel2 = new Vector3d();
			body2.getVelocityInLocalPoint(rel_pos2, vel2);

			Vector3d vel = new Vector3d();
			vel.sub(vel1, vel2);

			double j1, j2;

			{
				double vrel = cpd.frictionWorldTangential0.dot(vel);

				// calculate j that moves us to zero relative velocity
				j1 = -vrel * cpd.jacDiagABInvTangent0;
				double oldTangentImpulse = cpd.accumulatedTangentImpulse0;
				cpd.accumulatedTangentImpulse0 = oldTangentImpulse + j1;

				cpd.accumulatedTangentImpulse0 = Math.min(cpd.accumulatedTangentImpulse0, limit);
				cpd.accumulatedTangentImpulse0 = Math.max(cpd.accumulatedTangentImpulse0, -limit);
				j1 = cpd.accumulatedTangentImpulse0 - oldTangentImpulse;
			}
			{
				// 2nd tangent

				double vrel = cpd.frictionWorldTangential1.dot(vel);

				// calculate j that moves us to zero relative velocity
				j2 = -vrel * cpd.jacDiagABInvTangent1;
				double oldTangentImpulse = cpd.accumulatedTangentImpulse1;
				cpd.accumulatedTangentImpulse1 = oldTangentImpulse + j2;

				cpd.accumulatedTangentImpulse1 = Math.min(cpd.accumulatedTangentImpulse1, limit);
				cpd.accumulatedTangentImpulse1 = Math.max(cpd.accumulatedTangentImpulse1, -limit);
				j2 = cpd.accumulatedTangentImpulse1 - oldTangentImpulse;
			}

			//#ifdef USE_INTERNAL_APPLY_IMPULSE
			Vector3d tmp = new Vector3d();

			if (body1.getInvMass() != 0f) {
				tmp.scale(body1.getInvMass(), cpd.frictionWorldTangential0);
				body1.internalApplyImpulse(tmp, cpd.frictionAngularComponent0A, j1);

				tmp.scale(body1.getInvMass(), cpd.frictionWorldTangential1);
				body1.internalApplyImpulse(tmp, cpd.frictionAngularComponent1A, j2);
			}
			if (body2.getInvMass() != 0f) {
				tmp.scale(body2.getInvMass(), cpd.frictionWorldTangential0);
				body2.internalApplyImpulse(tmp, cpd.frictionAngularComponent0B, -j1);

				tmp.scale(body2.getInvMass(), cpd.frictionWorldTangential1);
				body2.internalApplyImpulse(tmp, cpd.frictionAngularComponent1B, -j2);
			}
			//#else //USE_INTERNAL_APPLY_IMPULSE
			//	body1.applyImpulse((j1 * cpd->m_frictionWorldTangential0)+(j2 * cpd->m_frictionWorldTangential1), rel_pos1);
			//	body2.applyImpulse((j1 * -cpd->m_frictionWorldTangential0)+(j2 * -cpd->m_frictionWorldTangential1), rel_pos2);
			//#endif //USE_INTERNAL_APPLY_IMPULSE
		}
		return cpd.appliedImpulse;
	}
	
	/**
	 * velocity + friction<br>
	 * response between two dynamic objects with friction
	 */
	public static double resolveSingleCollisionCombined(
			RigidBody body1,
			RigidBody body2,
			ManifoldPoint contactPoint,
			ContactSolverInfo solverInfo) {
		
		Vector3d tmpVec = new Vector3d();
		
		Vector3d pos1 = contactPoint.getPositionWorldOnA(new Vector3d());
		Vector3d pos2 = contactPoint.getPositionWorldOnB(new Vector3d());
		Vector3d normal = contactPoint.normalWorldOnB;

		Vector3d rel_pos1 = new Vector3d();
		rel_pos1.sub(pos1, body1.getCenterOfMassPosition(tmpVec));

		Vector3d rel_pos2 = new Vector3d();
		rel_pos2.sub(pos2, body2.getCenterOfMassPosition(tmpVec));

		Vector3d vel1 = body1.getVelocityInLocalPoint(rel_pos1, new Vector3d());
		Vector3d vel2 = body2.getVelocityInLocalPoint(rel_pos2, new Vector3d());
		Vector3d vel = new Vector3d();
		vel.sub(vel1, vel2);

		double rel_vel;
		rel_vel = normal.dot(vel);

		double Kfps = 1f / solverInfo.timeStep;

		//btScalar damping = solverInfo.m_damping ;
		double Kerp = solverInfo.erp;
		double Kcor = Kerp * Kfps;

		ConstraintPersistentData cpd = (ConstraintPersistentData) contactPoint.userPersistentData;
		assert (cpd != null);
		double distance = cpd.penetration;
		double positionalError = Kcor * -distance;
		double velocityError = cpd.restitution - rel_vel;// * damping;

		double penetrationImpulse = positionalError * cpd.jacDiagABInv;

		double velocityImpulse = velocityError * cpd.jacDiagABInv;

		double normalImpulse = penetrationImpulse + velocityImpulse;

		// See Erin Catto's GDC 2006 paper: Clamp the accumulated impulse
		double oldNormalImpulse = cpd.appliedImpulse;
		double sum = oldNormalImpulse + normalImpulse;
		cpd.appliedImpulse = 0f > sum ? 0f : sum;

		normalImpulse = cpd.appliedImpulse - oldNormalImpulse;


		//#ifdef USE_INTERNAL_APPLY_IMPULSE
		Vector3d tmp = new Vector3d();
		if (body1.getInvMass() != 0f) {
			tmp.scale(body1.getInvMass(), contactPoint.normalWorldOnB);
			body1.internalApplyImpulse(tmp, cpd.angularComponentA, normalImpulse);
		}
		if (body2.getInvMass() != 0f) {
			tmp.scale(body2.getInvMass(), contactPoint.normalWorldOnB);
			body2.internalApplyImpulse(tmp, cpd.angularComponentB, -normalImpulse);
		}
		//#else //USE_INTERNAL_APPLY_IMPULSE
		//	body1.applyImpulse(normal*(normalImpulse), rel_pos1);
		//	body2.applyImpulse(-normal*(normalImpulse), rel_pos2);
		//#endif //USE_INTERNAL_APPLY_IMPULSE

		{
			//friction
			body1.getVelocityInLocalPoint(rel_pos1, vel1);
			body2.getVelocityInLocalPoint(rel_pos2, vel2);
			vel.sub(vel1, vel2);

			rel_vel = normal.dot(vel);

			tmp.scale(rel_vel, normal);
			Vector3d lat_vel = new Vector3d();
			lat_vel.sub(vel, tmp);
			double lat_rel_vel = lat_vel.length();

			double combinedFriction = cpd.friction;

			if (cpd.appliedImpulse > 0) {
				if (lat_rel_vel > BulletGlobals.FLT_EPSILON) {
					lat_vel.scale(1f / lat_rel_vel);

					Vector3d temp1 = new Vector3d();
					temp1.cross(rel_pos1, lat_vel);
					body1.getInvInertiaTensorWorld(new Matrix3d()).transform(temp1);

					Vector3d temp2 = new Vector3d();
					temp2.cross(rel_pos2, lat_vel);
					body2.getInvInertiaTensorWorld(new Matrix3d()).transform(temp2);

					Vector3d java_tmp1 = new Vector3d();
					java_tmp1.cross(temp1, rel_pos1);

					Vector3d java_tmp2 = new Vector3d();
					java_tmp2.cross(temp2, rel_pos2);

					tmp.add(java_tmp1, java_tmp2);

					double friction_impulse = lat_rel_vel /
							(body1.getInvMass() + body2.getInvMass() + lat_vel.dot(tmp));
					double normal_impulse = cpd.appliedImpulse * combinedFriction;

					friction_impulse = Math.min(friction_impulse, normal_impulse);
					friction_impulse = Math.max(friction_impulse, -normal_impulse);

					tmp.scale(-friction_impulse, lat_vel);
					body1.applyImpulse(tmp, rel_pos1);

					tmp.scale(friction_impulse, lat_vel);
					body2.applyImpulse(tmp, rel_pos2);
				}
			}
		}

		return normalImpulse;
	}

	public static double resolveSingleFrictionEmpty(
			RigidBody body1,
			RigidBody body2,
			ManifoldPoint contactPoint,
			ContactSolverInfo solverInfo) {
		return 0f;
	}
	
}
