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

package com.bulletphysics.collision.shapes;

import javax.vecmath.Vector3d;

import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.linearmath.Transform;

/**
 * CollisionShape class provides an interface for collision shapes that can be
 * shared among {@link CollisionObject}s.
 * 
 * @author jezek2
 */
public abstract class CollisionShape {

	//protected final BulletStack stack = BulletStack.get();

	protected Object userPointer;
	
	///getAabb returns the axis aligned bounding box in the coordinate frame of the given transform t.
	public abstract void getAabb(Transform t, Vector3d aabbMin, Vector3d aabbMax);

	public void getBoundingSphere(Vector3d center, double[] radius) {
		Vector3d tmp = new Vector3d();

		Transform tr = new Transform();
		tr.setIdentity();
		Vector3d aabbMin = new Vector3d(), aabbMax = new Vector3d();

		getAabb(tr, aabbMin, aabbMax);

		tmp.sub(aabbMax, aabbMin);
		radius[0] = tmp.length() * 0.5f;

		tmp.add(aabbMin, aabbMax);
		center.scale(0.5f, tmp);
	}

	///getAngularMotionDisc returns the maximus radius needed for Conservative Advancement to handle time-of-impact with rotations.
	public double getAngularMotionDisc() {
		Vector3d center = new Vector3d();
		double[] disc = new double[1]; // TODO: stack
		getBoundingSphere(center, disc);
		disc[0] += center.length();
		return disc[0];
	}

	///calculateTemporalAabb calculates the enclosing aabb for the moving object over interval [0..timeStep)
	///result is conservative
	public void calculateTemporalAabb(Transform curTrans, Vector3d linvel, Vector3d angvel, double timeStep, Vector3d temporalAabbMin, Vector3d temporalAabbMax) {
		//start with static aabb
		getAabb(curTrans, temporalAabbMin, temporalAabbMax);

		double temporalAabbMaxx = temporalAabbMax.x;
		double temporalAabbMaxy = temporalAabbMax.y;
		double temporalAabbMaxz = temporalAabbMax.z;
		double temporalAabbMinx = temporalAabbMin.x;
		double temporalAabbMiny = temporalAabbMin.y;
		double temporalAabbMinz = temporalAabbMin.z;

		// add linear motion
		Vector3d linMotion = new Vector3d(linvel);
		linMotion.scale(timeStep);

		//todo: simd would have a vector max/min operation, instead of per-element access
		if (linMotion.x > 0f) {
			temporalAabbMaxx += linMotion.x;
		}
		else {
			temporalAabbMinx += linMotion.x;
		}
		if (linMotion.y > 0f) {
			temporalAabbMaxy += linMotion.y;
		}
		else {
			temporalAabbMiny += linMotion.y;
		}
		if (linMotion.z > 0f) {
			temporalAabbMaxz += linMotion.z;
		}
		else {
			temporalAabbMinz += linMotion.z;
		}

		//add conservative angular motion
		double angularMotion = angvel.length() * getAngularMotionDisc() * timeStep;
		Vector3d angularMotion3d = new Vector3d();
		angularMotion3d.set(angularMotion, angularMotion, angularMotion);
		temporalAabbMin.set(temporalAabbMinx, temporalAabbMiny, temporalAabbMinz);
		temporalAabbMax.set(temporalAabbMaxx, temporalAabbMaxy, temporalAabbMaxz);

		temporalAabbMin.sub(angularMotion3d);
		temporalAabbMax.add(angularMotion3d);
	}

//#ifndef __SPU__
	public boolean isPolyhedral() {
		return getShapeType().isPolyhedral();
	}

	public boolean isConvex() {
		return getShapeType().isConvex();
	}

	public boolean isConcave() {
		return getShapeType().isConcave();
	}

	public boolean isCompound() {
		return getShapeType().isCompound();
	}

	///isInfinite is used to catch simulation error (aabb check)
	public boolean isInfinite() {
		return getShapeType().isInfinite();
	}

	public abstract BroadphaseNativeType getShapeType();

	public abstract void setLocalScaling(Vector3d scaling);
	
	// TODO: returns const
	public abstract Vector3d getLocalScaling(Vector3d out);

	public abstract void calculateLocalInertia(double mass, Vector3d inertia);


//debugging support
	public abstract String getName();
//#endif //__SPU__
	public abstract void setMargin(double margin);

	public abstract double getMargin();
	
	// optional user data pointer
	public void setUserPointer(Object userPtr) {
		userPointer = userPtr;
	}

	public Object getUserPointer() {
		return userPointer;
	}
	
}
