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
 * ConeShape implements a cone shape primitive, centered around the origin and
 * aligned with the Y axis. The {@link ConeShapeX} is aligned around the X axis
 * and {@link ConeShapeZ} around the Z axis.
 * 
 * @author jezek2
 */
public class ConeShape extends ConvexInternalShape {

	private double sinAngle;
	private double radius;
	private double height;
	private int[] coneIndices = new int[3];

	public ConeShape(double radius, double height) {
		this.radius = radius;
		this.height = height;
		setConeUpIndex(1);
		sinAngle = (radius / (double)Math.sqrt(this.radius * this.radius + this.height * this.height));
	}

	public double getRadius() {
		return radius;
	}

	public double getHeight() {
		return height;
	}

	private Vector3d coneLocalSupport(Vector3d v, Vector3d out) {
		double halfHeight = height * 0.5f;

		if (VectorUtil.getCoord(v, coneIndices[1]) > v.length() * sinAngle) {
			VectorUtil.setCoord(out, coneIndices[0], 0f);
			VectorUtil.setCoord(out, coneIndices[1], halfHeight);
			VectorUtil.setCoord(out, coneIndices[2], 0f);
			return out;
		}
		else {
			double v0 = VectorUtil.getCoord(v, coneIndices[0]);
			double v2 = VectorUtil.getCoord(v, coneIndices[2]);
			double s = (double)Math.sqrt(v0 * v0 + v2 * v2);
			if (s > BulletGlobals.FLT_EPSILON) {
				double d = radius / s;
				VectorUtil.setCoord(out, coneIndices[0], VectorUtil.getCoord(v, coneIndices[0]) * d);
				VectorUtil.setCoord(out, coneIndices[1], -halfHeight);
				VectorUtil.setCoord(out, coneIndices[2], VectorUtil.getCoord(v, coneIndices[2]) * d);
				return out;
			} else {
				VectorUtil.setCoord(out, coneIndices[0], 0f);
				VectorUtil.setCoord(out, coneIndices[1], -halfHeight);
				VectorUtil.setCoord(out, coneIndices[2], 0f);
				return out;
			}
		}
	}

	@Override
	public Vector3d localGetSupportingVertexWithoutMargin(Vector3d vec, Vector3d out) {
		return coneLocalSupport(vec, out);
	}

	@Override
	public void batchedUnitVectorGetSupportingVertexWithoutMargin(Vector3d[] vectors, Vector3d[] supportVerticesOut, int numVectors) {
		for (int i=0; i<numVectors; i++) {
			Vector3d vec = vectors[i];
			coneLocalSupport(vec, supportVerticesOut[i]);
		}
	}

	@Override
	public Vector3d localGetSupportingVertex(Vector3d vec, Vector3d out) {
		Vector3d supVertex = coneLocalSupport(vec, out);
		if (getMargin() != 0f) {
			Vector3d vecnorm = new Vector3d(vec);
			if (vecnorm.lengthSquared() < (BulletGlobals.FLT_EPSILON * BulletGlobals.FLT_EPSILON)) {
				vecnorm.set(-1f, -1f, -1f);
			}
			vecnorm.normalize();
			supVertex.scaleAdd(getMargin(), vecnorm, supVertex);
		}
		return supVertex;
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.CONE_SHAPE_PROXYTYPE;
	}

	@Override
	public void calculateLocalInertia(double mass, Vector3d inertia) {
		Transform identity = new Transform();
		identity.setIdentity();
		Vector3d aabbMin = new Vector3d(), aabbMax = new Vector3d();
		getAabb(identity, aabbMin, aabbMax);

		Vector3d halfExtents = new Vector3d();
		halfExtents.sub(aabbMax, aabbMin);
		halfExtents.scale(0.5f);

		double margin = getMargin();

		double lx = 2f * (halfExtents.x + margin);
		double ly = 2f * (halfExtents.y + margin);
		double lz = 2f * (halfExtents.z + margin);
		double x2 = lx * lx;
		double y2 = ly * ly;
		double z2 = lz * lz;
		double scaledmass = mass * 0.08333333f;

		inertia.set(y2 + z2, x2 + z2, x2 + y2);
		inertia.scale(scaledmass);

		//inertia.x() = scaledmass * (y2+z2);
		//inertia.y() = scaledmass * (x2+z2);
		//inertia.z() = scaledmass * (x2+y2);
	}

	@Override
	public String getName() {
		return "Cone";
	}

	// choose upAxis index
	protected void setConeUpIndex(int upIndex) {
		switch (upIndex) {
			case 0:
				coneIndices[0] = 1;
				coneIndices[1] = 0;
				coneIndices[2] = 2;
				break;

			case 1:
				coneIndices[0] = 0;
				coneIndices[1] = 1;
				coneIndices[2] = 2;
				break;

			case 2:
				coneIndices[0] = 0;
				coneIndices[1] = 2;
				coneIndices[2] = 1;
				break;

			default:
				assert (false);
		}
	}

	public int getConeUpIndex() {
		return coneIndices[1];
	}
	
}
