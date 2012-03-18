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
import com.bulletphysics.linearmath.VectorUtil;

//notes:
// Another memory optimization would be to store m_1MinvJt in the remaining 3 w components
// which makes the btJacobianEntry memory layout 16 bytes
// if you only are interested in angular part, just feed massInvA and massInvB zero

/**
 * Jacobian entry is an abstraction that allows to describe constraints.
 * It can be used in combination with a constraint solver.
 * Can be used to relate the effect of an impulse to the constraint error.
 * 
 * @author jezek2
 */
public class JacobianEntry {
	
	//protected final BulletStack stack = BulletStack.get();
	
	public final Vector3d linearJointAxis = new Vector3d();
	public final Vector3d aJ = new Vector3d();
	public final Vector3d bJ = new Vector3d();
	public final Vector3d m_0MinvJt = new Vector3d();
	public final Vector3d m_1MinvJt = new Vector3d();
	// Optimization: can be stored in the w/last component of one of the vectors
	public double Adiag;

	public JacobianEntry() {
	}

	/**
	 * Constraint between two different rigidbodies.
	 */
	public void init(Matrix3d world2A,
			Matrix3d world2B,
			Vector3d rel_pos1, Vector3d rel_pos2,
			Vector3d jointAxis,
			Vector3d inertiaInvA,
			double massInvA,
			Vector3d inertiaInvB,
			double massInvB)
	{
		linearJointAxis.set(jointAxis);

		aJ.cross(rel_pos1, linearJointAxis);
		world2A.transform(aJ);

		bJ.set(linearJointAxis);
		bJ.negate();
		bJ.cross(rel_pos2, bJ);
		world2B.transform(bJ);

		VectorUtil.mul(m_0MinvJt, inertiaInvA, aJ);
		VectorUtil.mul(m_1MinvJt, inertiaInvB, bJ);
		Adiag = massInvA + m_0MinvJt.dot(aJ) + massInvB + m_1MinvJt.dot(bJ);

		assert (Adiag > 0f);
	}

	/**
	 * Angular constraint between two different rigidbodies.
	 */
	public void init(Vector3d jointAxis,
		Matrix3d world2A,
		Matrix3d world2B,
		Vector3d inertiaInvA,
		Vector3d inertiaInvB)
	{
		linearJointAxis.set(0f, 0f, 0f);

		aJ.set(jointAxis);
		world2A.transform(aJ);

		bJ.set(jointAxis);
		bJ.negate();
		world2B.transform(bJ);

		VectorUtil.mul(m_0MinvJt, inertiaInvA, aJ);
		VectorUtil.mul(m_1MinvJt, inertiaInvB, bJ);
		Adiag = m_0MinvJt.dot(aJ) + m_1MinvJt.dot(bJ);

		assert (Adiag > 0f);
	}

	/**
	 * Angular constraint between two different rigidbodies.
	 */
	public void init(Vector3d axisInA,
		Vector3d axisInB,
		Vector3d inertiaInvA,
		Vector3d inertiaInvB)
	{
		linearJointAxis.set(0f, 0f, 0f);
		aJ.set(axisInA);

		bJ.set(axisInB);
		bJ.negate();

		VectorUtil.mul(m_0MinvJt, inertiaInvA, aJ);
		VectorUtil.mul(m_1MinvJt, inertiaInvB, bJ);
		Adiag = m_0MinvJt.dot(aJ) + m_1MinvJt.dot(bJ);

		assert (Adiag > 0f);
	}

	/**
	 * Constraint on one rigidbody.
	 */
	public void init(
		Matrix3d world2A,
		Vector3d rel_pos1, Vector3d rel_pos2,
		Vector3d jointAxis,
		Vector3d inertiaInvA, 
		double massInvA)
	{
		linearJointAxis.set(jointAxis);

		aJ.cross(rel_pos1, jointAxis);
		world2A.transform(aJ);

		bJ.set(jointAxis);
		bJ.negate();
		bJ.cross(rel_pos2, bJ);
		world2A.transform(bJ);

		VectorUtil.mul(m_0MinvJt, inertiaInvA, aJ);
		m_1MinvJt.set(0f, 0f, 0f);
		Adiag = massInvA + m_0MinvJt.dot(aJ);

		assert (Adiag > 0f);
	}

	public double getDiagonal() { return Adiag; }

	/**
	 * For two constraints on the same rigidbody (for example vehicle friction).
	 */
	public double getNonDiagonal(JacobianEntry jacB, double massInvA) {
		JacobianEntry jacA = this;
		double lin = massInvA * jacA.linearJointAxis.dot(jacB.linearJointAxis);
		double ang = jacA.m_0MinvJt.dot(jacB.aJ);
		return lin + ang;
	}

	/**
	 * For two constraints on sharing two same rigidbodies (for example two contact points between two rigidbodies).
	 */
	public double getNonDiagonal(JacobianEntry jacB, double massInvA, double massInvB) {
		JacobianEntry jacA = this;

		Vector3d lin = new Vector3d();
		VectorUtil.mul(lin, jacA.linearJointAxis, jacB.linearJointAxis);

		Vector3d ang0 = new Vector3d();
		VectorUtil.mul(ang0, jacA.m_0MinvJt, jacB.aJ);

		Vector3d ang1 = new Vector3d();
		VectorUtil.mul(ang1, jacA.m_1MinvJt, jacB.bJ);

		Vector3d lin0 = new Vector3d();
		lin0.scale(massInvA, lin);

		Vector3d lin1 = new Vector3d();
		lin1.scale(massInvB, lin);

		Vector3d sum = new Vector3d();
		VectorUtil.add(sum, ang0, ang1, lin0, lin1);

		return sum.x + sum.y + sum.z;
	}

	public double getRelativeVelocity(Vector3d linvelA, Vector3d angvelA, Vector3d linvelB, Vector3d angvelB) {
		Vector3d linrel = new Vector3d();
		linrel.sub(linvelA, linvelB);

		Vector3d angvela = new Vector3d();
		VectorUtil.mul(angvela, angvelA, aJ);

		Vector3d angvelb = new Vector3d();
		VectorUtil.mul(angvelb, angvelB, bJ);

		VectorUtil.mul(linrel, linrel, linearJointAxis);

		angvela.add(angvelb);
		angvela.add(linrel);

		double rel_vel2 = angvela.x + angvela.y + angvela.z;
		return rel_vel2 + BulletGlobals.FLT_EPSILON;
	}
	
}
