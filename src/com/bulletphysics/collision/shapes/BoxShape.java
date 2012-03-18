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
import javax.vecmath.Vector4d;

import com.bulletphysics.collision.broadphase.BroadphaseNativeType;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.AabbUtil2;
import com.bulletphysics.linearmath.ScalarUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

/**
 * BoxShape is a box primitive around the origin, its sides axis aligned with length
 * specified by half extents, in local shape coordinates. When used as part of a
 * {@link CollisionObject} or {@link RigidBody} it will be an oriented box in world space.
 *
 * @author jezek2
 */
public class BoxShape extends PolyhedralConvexShape {

	public BoxShape(Vector3d boxHalfExtents) {
		Vector3d margin = new Vector3d(getMargin(), getMargin(), getMargin());
		VectorUtil.mul(implicitShapeDimensions, boxHalfExtents, localScaling);
		implicitShapeDimensions.sub(margin);
	}

	public Vector3d getHalfExtentsWithMargin(Vector3d out) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(out);
		Vector3d margin = new Vector3d();
		margin.set(getMargin(), getMargin(), getMargin());
		halfExtents.add(margin);
		return out;
	}

	public Vector3d getHalfExtentsWithoutMargin(Vector3d out) {
		out.set(implicitShapeDimensions); // changed in Bullet 2.63: assume the scaling and margin are included
		return out;
	}

	@Override
	public BroadphaseNativeType getShapeType() {
		return BroadphaseNativeType.BOX_SHAPE_PROXYTYPE;
	}

	@Override
	public Vector3d localGetSupportingVertex(Vector3d vec, Vector3d out) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(out);
		
		double margin = getMargin();
		halfExtents.x += margin;
		halfExtents.y += margin;
		halfExtents.z += margin;

		out.set(
				ScalarUtil.fsel(vec.x, halfExtents.x, -halfExtents.x),
				ScalarUtil.fsel(vec.y, halfExtents.y, -halfExtents.y),
				ScalarUtil.fsel(vec.z, halfExtents.z, -halfExtents.z));
		return out;
	}

	@Override
	public Vector3d localGetSupportingVertexWithoutMargin(Vector3d vec, Vector3d out) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(out);

		out.set(
				ScalarUtil.fsel(vec.x, halfExtents.x, -halfExtents.x),
				ScalarUtil.fsel(vec.y, halfExtents.y, -halfExtents.y),
				ScalarUtil.fsel(vec.z, halfExtents.z, -halfExtents.z));
		return out;
	}

	@Override
	public void batchedUnitVectorGetSupportingVertexWithoutMargin(Vector3d[] vectors, Vector3d[] supportVerticesOut, int numVectors) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(new Vector3d());

		for (int i = 0; i < numVectors; i++) {
			Vector3d vec = vectors[i];
			supportVerticesOut[i].set(ScalarUtil.fsel(vec.x, halfExtents.x, -halfExtents.x),
					ScalarUtil.fsel(vec.y, halfExtents.y, -halfExtents.y),
					ScalarUtil.fsel(vec.z, halfExtents.z, -halfExtents.z));
		}
	}

	@Override
	public void setMargin(double margin) {
		// correct the implicitShapeDimensions for the margin
		Vector3d oldMargin = new Vector3d();
		oldMargin.set(getMargin(), getMargin(), getMargin());
		Vector3d implicitShapeDimensionsWithMargin = new Vector3d();
		implicitShapeDimensionsWithMargin.add(implicitShapeDimensions, oldMargin);

		super.setMargin(margin);
		Vector3d newMargin = new Vector3d();
		newMargin.set(getMargin(), getMargin(), getMargin());
		implicitShapeDimensions.sub(implicitShapeDimensionsWithMargin, newMargin);
	}

	@Override
	public void setLocalScaling(Vector3d scaling) {
		Vector3d oldMargin = new Vector3d();
		oldMargin.set(getMargin(), getMargin(), getMargin());
		Vector3d implicitShapeDimensionsWithMargin = new Vector3d();
		implicitShapeDimensionsWithMargin.add(implicitShapeDimensions, oldMargin);
		Vector3d unScaledImplicitShapeDimensionsWithMargin = new Vector3d();
		VectorUtil.div(unScaledImplicitShapeDimensionsWithMargin, implicitShapeDimensionsWithMargin, localScaling);

		super.setLocalScaling(scaling);

		VectorUtil.mul(implicitShapeDimensions, unScaledImplicitShapeDimensionsWithMargin, localScaling);
		implicitShapeDimensions.sub(oldMargin);
	}

	@Override
	public void getAabb(Transform t, Vector3d aabbMin, Vector3d aabbMax) {
		AabbUtil2.transformAabb(getHalfExtentsWithoutMargin(new Vector3d()), getMargin(), t, aabbMin, aabbMax);
	}

	@Override
	public void calculateLocalInertia(double mass, Vector3d inertia) {
		//btScalar margin = btScalar(0.);
		Vector3d halfExtents = getHalfExtentsWithMargin(new Vector3d());

		double lx = 2f * halfExtents.x;
		double ly = 2f * halfExtents.y;
		double lz = 2f * halfExtents.z;

		inertia.set(mass / 12f * (ly * ly + lz * lz),
				mass / 12f * (lx * lx + lz * lz),
				mass / 12f * (lx * lx + ly * ly));
	}

	@Override
	public void getPlane(Vector3d planeNormal, Vector3d planeSupport, int i) {
		// this plane might not be aligned...
		Vector4d plane = new Vector4d();
		getPlaneEquation(plane, i);
		planeNormal.set(plane.x, plane.y, plane.z);
		Vector3d tmp = new Vector3d();
		tmp.negate(planeNormal);
		localGetSupportingVertex(tmp, planeSupport);
	}

	@Override
	public int getNumPlanes() {
		return 6;
	}

	@Override
	public int getNumVertices() {
		return 8;
	}

	@Override
	public int getNumEdges() {
		return 12;
	}

	@Override
	public void getVertex(int i, Vector3d vtx) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(new Vector3d());

		vtx.set(halfExtents.x * (1 - (i & 1)) - halfExtents.x * (i & 1),
				halfExtents.y * (1 - ((i & 2) >> 1)) - halfExtents.y * ((i & 2) >> 1),
				halfExtents.z * (1 - ((i & 4) >> 2)) - halfExtents.z * ((i & 4) >> 2));
	}
	
	public void getPlaneEquation(Vector4d plane, int i) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(new Vector3d());

		switch (i) {
			case 0:
				plane.set(1f, 0f, 0f, -halfExtents.x);
				break;
			case 1:
				plane.set(-1f, 0f, 0f, -halfExtents.x);
				break;
			case 2:
				plane.set(0f, 1f, 0f, -halfExtents.y);
				break;
			case 3:
				plane.set(0f, -1f, 0f, -halfExtents.y);
				break;
			case 4:
				plane.set(0f, 0f, 1f, -halfExtents.z);
				break;
			case 5:
				plane.set(0f, 0f, -1f, -halfExtents.z);
				break;
			default:
				assert (false);
		}
	}

	@Override
	public void getEdge(int i, Vector3d pa, Vector3d pb) {
		int edgeVert0 = 0;
		int edgeVert1 = 0;

		switch (i) {
			case 0:
				edgeVert0 = 0;
				edgeVert1 = 1;
				break;
			case 1:
				edgeVert0 = 0;
				edgeVert1 = 2;
				break;
			case 2:
				edgeVert0 = 1;
				edgeVert1 = 3;

				break;
			case 3:
				edgeVert0 = 2;
				edgeVert1 = 3;
				break;
			case 4:
				edgeVert0 = 0;
				edgeVert1 = 4;
				break;
			case 5:
				edgeVert0 = 1;
				edgeVert1 = 5;

				break;
			case 6:
				edgeVert0 = 2;
				edgeVert1 = 6;
				break;
			case 7:
				edgeVert0 = 3;
				edgeVert1 = 7;
				break;
			case 8:
				edgeVert0 = 4;
				edgeVert1 = 5;
				break;
			case 9:
				edgeVert0 = 4;
				edgeVert1 = 6;
				break;
			case 10:
				edgeVert0 = 5;
				edgeVert1 = 7;
				break;
			case 11:
				edgeVert0 = 6;
				edgeVert1 = 7;
				break;
			default:
				assert (false);
		}

		getVertex(edgeVert0, pa);
		getVertex(edgeVert1, pb);
	}

	@Override
	public boolean isInside(Vector3d pt, double tolerance) {
		Vector3d halfExtents = getHalfExtentsWithoutMargin(new Vector3d());

		//btScalar minDist = 2*tolerance;

		boolean result =
				(pt.x <= (halfExtents.x + tolerance)) &&
				(pt.x >= (-halfExtents.x - tolerance)) &&
				(pt.y <= (halfExtents.y + tolerance)) &&
				(pt.y >= (-halfExtents.y - tolerance)) &&
				(pt.z <= (halfExtents.z + tolerance)) &&
				(pt.z >= (-halfExtents.z - tolerance));

		return result;
	}

	@Override
	public String getName() {
		return "Box";
	}

	@Override
	public int getNumPreferredPenetrationDirections() {
		return 6;
	}

	@Override
	public void getPreferredPenetrationDirection(int index, Vector3d penetrationVector) {
		switch (index) {
			case 0:
				penetrationVector.set(1f, 0f, 0f);
				break;
			case 1:
				penetrationVector.set(-1f, 0f, 0f);
				break;
			case 2:
				penetrationVector.set(0f, 1f, 0f);
				break;
			case 3:
				penetrationVector.set(0f, -1f, 0f);
				break;
			case 4:
				penetrationVector.set(0f, 0f, 1f);
				break;
			case 5:
				penetrationVector.set(0f, 0f, -1f);
				break;
			default:
				assert (false);
		}
	}

}
