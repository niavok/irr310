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

/**
 * Cylinder shape around the Z axis.
 * 
 * @author jezek2
 */
public class CylinderShapeZ extends CylinderShape {

	public CylinderShapeZ(Vector3d halfExtents) {
		super(halfExtents, false);
		upAxis = 2;
		recalcLocalAabb();
	}

	@Override
	public Vector3d localGetSupportingVertexWithoutMargin(Vector3d vec, Vector3d out) {
		return cylinderLocalSupportZ(getHalfExtentsWithoutMargin(new Vector3d()), vec, out);
	}

	@Override
	public void batchedUnitVectorGetSupportingVertexWithoutMargin(Vector3d[] vectors, Vector3d[] supportVerticesOut, int numVectors) {
		for (int i = 0; i < numVectors; i++) {
			cylinderLocalSupportZ(getHalfExtentsWithoutMargin(new Vector3d()), vectors[i], supportVerticesOut[i]);
		}
	}

	@Override
	public double getRadius() {
		return getHalfExtentsWithMargin(new Vector3d()).x;
	}

	@Override
	public String getName() {
		return "CylinderZ";
	}
	
}
