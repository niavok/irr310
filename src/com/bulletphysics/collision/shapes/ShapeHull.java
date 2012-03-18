/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 * 
 * ShapeHull implemented by John McCutchan.
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

import com.bulletphysics.linearmath.MiscUtil;
import com.bulletphysics.linearmath.convexhull.HullDesc;
import com.bulletphysics.linearmath.convexhull.HullFlags;
import com.bulletphysics.linearmath.convexhull.HullLibrary;
import com.bulletphysics.linearmath.convexhull.HullResult;
import com.bulletphysics.util.IntArrayList;
import com.bulletphysics.util.ObjectArrayList;

/**
 * ShapeHull takes a {@link ConvexShape}, builds the convex hull using {@link HullLibrary}
 * and provides triangle indices and vertices.
 * 
 * @author jezek2
 */
public class ShapeHull {

	protected ObjectArrayList<Vector3d> vertices = new ObjectArrayList<Vector3d>();
	protected IntArrayList indices = new IntArrayList();
	protected int numIndices;
	protected ConvexShape shape;

	protected ObjectArrayList<Vector3d> unitSpherePoints = new ObjectArrayList<Vector3d>();

	public ShapeHull(ConvexShape shape) {
		this.shape = shape;
		this.vertices.clear();
		this.indices.clear();
		this.numIndices = 0;

		MiscUtil.resize(unitSpherePoints, NUM_UNITSPHERE_POINTS+ConvexShape.MAX_PREFERRED_PENETRATION_DIRECTIONS*2, Vector3d.class);
		for (int i=0; i<constUnitSpherePoints.size(); i++) {
			unitSpherePoints.getQuick(i).set(constUnitSpherePoints.getQuick(i));
		}
	}

	public boolean buildHull(double margin) {
		Vector3d norm = new Vector3d();

		int numSampleDirections = NUM_UNITSPHERE_POINTS;
		{
			int numPDA = shape.getNumPreferredPenetrationDirections();
			if (numPDA != 0) {
				for (int i=0; i<numPDA; i++) {
					shape.getPreferredPenetrationDirection(i, norm);
					unitSpherePoints.getQuick(numSampleDirections).set(norm);
					numSampleDirections++;
				}
			}
		}

		ObjectArrayList<Vector3d> supportPoints = new ObjectArrayList<Vector3d>();
		MiscUtil.resize(supportPoints, NUM_UNITSPHERE_POINTS + ConvexShape.MAX_PREFERRED_PENETRATION_DIRECTIONS * 2, Vector3d.class);

		for (int i=0; i<numSampleDirections; i++) {
			shape.localGetSupportingVertex(unitSpherePoints.getQuick(i), supportPoints.getQuick(i));
		}

		HullDesc hd = new HullDesc();
		hd.flags = HullFlags.TRIANGLES;
		hd.vcount = numSampleDirections;

		//#ifdef BT_USE_DOUBLE_PRECISION
		//hd.mVertices = &supportPoints[0];
		//hd.mVertexStride = sizeof(btVector3);
		//#else
		hd.vertices = supportPoints;
		//hd.vertexStride = 3 * 4;
		//#endif

		HullLibrary hl = new HullLibrary();
		HullResult hr = new HullResult();
		if (!hl.createConvexHull(hd, hr)) {
			return false;
		}

		MiscUtil.resize(vertices, hr.numOutputVertices, Vector3d.class);

		for (int i=0; i<hr.numOutputVertices; i++) {
			vertices.getQuick(i).set(hr.outputVertices.getQuick(i));
		}
		numIndices = hr.numIndices;
		MiscUtil.resize(indices, numIndices, 0);
		for (int i=0; i<numIndices; i++) {
			indices.set(i, hr.indices.get(i));
		}

		// free temporary hull result that we just copied
		hl.releaseResult(hr);

		return true;
	}

	public int numTriangles() {
		return numIndices / 3;
	}

	public int numVertices() {
		return vertices.size();
	}

	public int numIndices() {
		return numIndices;
	}

	public ObjectArrayList<Vector3d> getVertexPointer() {
		return vertices;
	}

	public IntArrayList getIndexPointer() {
		return indices;
	}

	////////////////////////////////////////////////////////////////////////////
	
	private static int NUM_UNITSPHERE_POINTS = 42;
	
	private static ObjectArrayList<Vector3d> constUnitSpherePoints = new ObjectArrayList<Vector3d>();
	
	static {
		constUnitSpherePoints.add(new Vector3d(0.000000f, -0.000000f, -1.000000f));
		constUnitSpherePoints.add(new Vector3d(0.723608f, -0.525725f, -0.447219f));
		constUnitSpherePoints.add(new Vector3d(-0.276388f, -0.850649f, -0.447219f));
		constUnitSpherePoints.add(new Vector3d(-0.894426f, -0.000000f, -0.447216f));
		constUnitSpherePoints.add(new Vector3d(-0.276388f, 0.850649f, -0.447220f));
		constUnitSpherePoints.add(new Vector3d(0.723608f, 0.525725f, -0.447219f));
		constUnitSpherePoints.add(new Vector3d(0.276388f, -0.850649f, 0.447220f));
		constUnitSpherePoints.add(new Vector3d(-0.723608f, -0.525725f, 0.447219f));
		constUnitSpherePoints.add(new Vector3d(-0.723608f, 0.525725f, 0.447219f));
		constUnitSpherePoints.add(new Vector3d(0.276388f, 0.850649f, 0.447219f));
		constUnitSpherePoints.add(new Vector3d(0.894426f, 0.000000f, 0.447216f));
		constUnitSpherePoints.add(new Vector3d(-0.000000f, 0.000000f, 1.000000f));
		constUnitSpherePoints.add(new Vector3d(0.425323f, -0.309011f, -0.850654f));
		constUnitSpherePoints.add(new Vector3d(-0.162456f, -0.499995f, -0.850654f));
		constUnitSpherePoints.add(new Vector3d(0.262869f, -0.809012f, -0.525738f));
		constUnitSpherePoints.add(new Vector3d(0.425323f, 0.309011f, -0.850654f));
		constUnitSpherePoints.add(new Vector3d(0.850648f, -0.000000f, -0.525736f));
		constUnitSpherePoints.add(new Vector3d(-0.525730f, -0.000000f, -0.850652f));
		constUnitSpherePoints.add(new Vector3d(-0.688190f, -0.499997f, -0.525736f));
		constUnitSpherePoints.add(new Vector3d(-0.162456f, 0.499995f, -0.850654f));
		constUnitSpherePoints.add(new Vector3d(-0.688190f, 0.499997f, -0.525736f));
		constUnitSpherePoints.add(new Vector3d(0.262869f, 0.809012f, -0.525738f));
		constUnitSpherePoints.add(new Vector3d(0.951058f, 0.309013f, 0.000000f));
		constUnitSpherePoints.add(new Vector3d(0.951058f, -0.309013f, 0.000000f));
		constUnitSpherePoints.add(new Vector3d(0.587786f, -0.809017f, 0.000000f));
		constUnitSpherePoints.add(new Vector3d(0.000000f, -1.000000f, 0.000000f));
		constUnitSpherePoints.add(new Vector3d(-0.587786f, -0.809017f, 0.000000f));
		constUnitSpherePoints.add(new Vector3d(-0.951058f, -0.309013f, -0.000000f));
		constUnitSpherePoints.add(new Vector3d(-0.951058f, 0.309013f, -0.000000f));
		constUnitSpherePoints.add(new Vector3d(-0.587786f, 0.809017f, -0.000000f));
		constUnitSpherePoints.add(new Vector3d(-0.000000f, 1.000000f, -0.000000f));
		constUnitSpherePoints.add(new Vector3d(0.587786f, 0.809017f, -0.000000f));
		constUnitSpherePoints.add(new Vector3d(0.688190f, -0.499997f, 0.525736f));
		constUnitSpherePoints.add(new Vector3d(-0.262869f, -0.809012f, 0.525738f));
		constUnitSpherePoints.add(new Vector3d(-0.850648f, 0.000000f, 0.525736f));
		constUnitSpherePoints.add(new Vector3d(-0.262869f, 0.809012f, 0.525738f));
		constUnitSpherePoints.add(new Vector3d(0.688190f, 0.499997f, 0.525736f));
		constUnitSpherePoints.add(new Vector3d(0.525730f, 0.000000f, 0.850652f));
		constUnitSpherePoints.add(new Vector3d(0.162456f, -0.499995f, 0.850654f));
		constUnitSpherePoints.add(new Vector3d(-0.425323f, -0.309011f, 0.850654f));
		constUnitSpherePoints.add(new Vector3d(-0.425323f, 0.309011f, 0.850654f));
		constUnitSpherePoints.add(new Vector3d(0.162456f, 0.499995f, 0.850654f));
	}
	
}
