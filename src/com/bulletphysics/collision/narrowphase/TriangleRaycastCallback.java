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

package com.bulletphysics.collision.narrowphase;

import javax.vecmath.Vector3d;

import com.bulletphysics.collision.shapes.TriangleCallback;
import com.bulletphysics.linearmath.VectorUtil;

/**
 *
 * @author jezek2
 */
public abstract class TriangleRaycastCallback extends TriangleCallback {
	
	//protected final BulletStack stack = BulletStack.get();

	public final Vector3d from = new Vector3d();
	public final Vector3d to = new Vector3d();

	public double hitFraction;

	public TriangleRaycastCallback(Vector3d from, Vector3d to) {
		this.from.set(from);
		this.to.set(to);
		this.hitFraction = 1f;
	}
	
	public void processTriangle(Vector3d[] triangle, int partId, int triangleIndex) {
		Vector3d vert0 = triangle[0];
		Vector3d vert1 = triangle[1];
		Vector3d vert2 = triangle[2];

		Vector3d v10 = new Vector3d();
		v10.sub(vert1, vert0);

		Vector3d v20 = new Vector3d();
		v20.sub(vert2, vert0);

		Vector3d triangleNormal = new Vector3d();
		triangleNormal.cross(v10, v20);

		double dist = vert0.dot(triangleNormal);
		double dist_a = triangleNormal.dot(from);
		dist_a -= dist;
		double dist_b = triangleNormal.dot(to);
		dist_b -= dist;

		if (dist_a * dist_b >= 0f) {
			return; // same sign
		}

		double proj_length = dist_a - dist_b;
		double distance = (dist_a) / (proj_length);
		// Now we have the intersection point on the plane, we'll see if it's inside the triangle
		// Add an epsilon as a tolerance for the raycast,
		// in case the ray hits exacly on the edge of the triangle.
		// It must be scaled for the triangle size.

		if (distance < hitFraction) {
			double edge_tolerance = triangleNormal.lengthSquared();
			edge_tolerance *= -0.0001f;
			Vector3d point = new Vector3d();
			VectorUtil.setInterpolate3(point, from, to, distance);
			{
				Vector3d v0p = new Vector3d();
				v0p.sub(vert0, point);
				Vector3d v1p = new Vector3d();
				v1p.sub(vert1, point);
				Vector3d cp0 = new Vector3d();
				cp0.cross(v0p, v1p);

				if (cp0.dot(triangleNormal) >= edge_tolerance) {
					Vector3d v2p = new Vector3d();
					v2p.sub(vert2, point);
					Vector3d cp1 = new Vector3d();
					cp1.cross(v1p, v2p);
					if (cp1.dot(triangleNormal) >= edge_tolerance) {
						Vector3d cp2 = new Vector3d();
						cp2.cross(v2p, v0p);

						if (cp2.dot(triangleNormal) >= edge_tolerance) {

							if (dist_a > 0f) {
								hitFraction = reportHit(triangleNormal, distance, partId, triangleIndex);
							}
							else {
								Vector3d tmp = new Vector3d();
								tmp.negate(triangleNormal);
								hitFraction = reportHit(tmp, distance, partId, triangleIndex);
							}
						}
					}
				}
			}
		}
	}

	public abstract double reportHit(Vector3d hitNormalLocal, double hitFraction, int partId, int triangleIndex );

}
