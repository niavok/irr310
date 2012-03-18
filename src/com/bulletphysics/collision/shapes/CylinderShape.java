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

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

/**
 * CylinderShape class implements a cylinder shape primitive, centered around
 * the origin. Its central axis aligned with the Y axis. {@link CylinderShapeX}
 * is aligned with the X axis and {@link CylinderShapeZ} around the Z axis.
 * 
 * @author jezek2
 */
public class CylinderShape extends BoxShape {

	protected int upAxis;

	public CylinderShape(Vector3d halfExtents) {
		super(halfExtents);
		upAxis = 1;
		recalcLocalAabb();
	}

	protected CylinderShape(Vector3d halfExtents, boolean unused) {
		super(halfExtents);
	}

	@Override
	public void getAabb(Transform t, Vector3d aabbMin, Vector3d aabbMax) {
		_PolyhedralConvexShape_getAabb(t, aabbMin, aabbMax);
	}

	protected Vector3d cylinderLocalSupportX(Vector3d halfExtents, Vector3d v, Vector3d out) {
		return cylinderLocalSupport(halfExtents, v, 0, 1, 0, 2, out);
	}

	protected Vector3d cylinderLocalSupportY(Vector3d halfExtents, Vector3d v, Vector3d out) {
		return cylinderLocalSupport(halfExtents, v, 1, 0, 1, 2, out);
	}

	protected Vector3d cylinderLocalSupportZ(Vector3d halfExtents, Vector3d v, Vector3d out) {
		return cylinderLocalSupport(halfExtents, v, 2, 0, 2, 1, out);
	}
	
	private Vector3d cylinderLocalSupport(Vector3d halfExtents, Vector3d v, int cylinderUpAxis, int XX, int YY, int ZZ, Vector3d out) {
		//mapping depends on how cylinder local orientation is
		// extents of the cylinder is: X,Y is for radius, and Z for height

		double radius = VectorUtil.getCoord(halfExtents, XX);
		double halfHeight = VectorUtil.getCoord(halfExtents, cylinderUpAxis);

		double d;

		double s = (double) Math.sqrt(VectorUtil.getCoord(v, XX) * VectorUtil.getCoord(v, XX) + VectorUtil.getCoord(v, ZZ) * VectorUtil.getCoord(v, ZZ));
		if (s != 0f) {
			d = radius / s;
			VectorUtil.setCoord(out, XX, VectorUtil.getCoord(v, XX) * d);
			VectorUtil.setCoord(out, YY, VectorUtil.getCoord(v, YY) < 0f ? -halfHeight : halfHeight);
			VectorUtil.setCoord(out, ZZ, VectorUtil.getCoord(v, ZZ) * d);
			return out;
		}
		else {
			VectorUtil.setCoord(out, XX, radius);
			VectorUtil.setCoord(out, YY, VectorUtil.getCoord(v, YY) < 0f ? -halfHeight : halfHeight);
			VectorUtil.setCoord(out, ZZ, 0f);
			return out;
		}
	}

	@Override
	public Vector3d localGetSupportingVertexWithoutMargin(Vector3d vec, Vector3d out) {
		return cylinderLocalSupportY(getHalfExtentsWithoutMargin(new Vector3d()), vec, out);
	}

	@Override
	public void batchedUnitVectorGetSupportingVertexWithoutMargin(Vector3d[] vectors, Vector3d[] supportVerticesOut, int numVectors) {
		for (int i = 0; i < numVectors; i++) {
			cylinderLocalSupportY(getHalfExtentsWithoutMargin(new Vector3d()), vectors[i], supportVerticesOut[i]);
		}
	}

	@Override
	public Vector3d localGetSupportingVertex(Vector3d vec, Vector3d out) {
		Vector3d supVertex = out;
		localGetSupportingVertexWithoutMargin(vec, supVertex);

		if (getMargin() != 0f) {
			Vector3d vecnorm = new Vector3d(vec);
			if (vecnorm.lengthSquared() < (BulletGlobals.SIMD_EPSILON * BulletGlobals.SIMD_EPSILON)) {
				vecnorm.set(-1f, -1f, -1f);
			}
			vecnorm.normalize();
			supVertex.scaleAdd(getMargin(), vecnorm, supVertex);
		}
		return out;
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.CYLINDER_SHAPE_PROXYTYPE;
	}

	public int getUpAxis() {
		return upAxis;
	}
	
	public double getRadius() {
		return getHalfExtentsWithMargin(new Vector3d()).x;
	}

	@Override
	public String getName() {
		return "CylinderY";
	}
	
}
