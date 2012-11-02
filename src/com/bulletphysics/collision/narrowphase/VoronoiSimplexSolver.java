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

import com.bulletphysics.linearmath.VectorUtil;
import com.bulletphysics.util.ObjectPool;

/**
 * VoronoiSimplexSolver is an implementation of the closest point distance algorithm
 * from a 1-4 points simplex to the origin. Can be used with GJK, as an alternative
 * to Johnson distance algorithm.
 * 
 * @author jezek2
 */
public class VoronoiSimplexSolver extends SimplexSolverInterface {

	//protected final BulletStack stack = BulletStack.get();
	protected final ObjectPool<SubSimplexClosestResult> subsimplexResultsPool = ObjectPool.get(SubSimplexClosestResult.class);
	
	private static final int VORONOI_SIMPLEX_MAX_VERTS = 5;
	
	private static final int VERTA = 0;
	private static final int VERTB = 1;
	private static final int VERTC = 2;

	public int numVertices;

	public final Vector3d[] simplexVectorW = new Vector3d[VORONOI_SIMPLEX_MAX_VERTS];
	public final Vector3d[] simplexPointsP = new Vector3d[VORONOI_SIMPLEX_MAX_VERTS];
	public final Vector3d[] simplexPointsQ = new Vector3d[VORONOI_SIMPLEX_MAX_VERTS];

	public final Vector3d cachedP1 = new Vector3d();
	public final Vector3d cachedP2 = new Vector3d();
	public final Vector3d cachedV = new Vector3d();
	public final Vector3d lastW = new Vector3d();
	public boolean cachedValidClosest;

	public final SubSimplexClosestResult cachedBC = new SubSimplexClosestResult();

	public boolean needsUpdate;
	
	{
		for (int i=0; i<VORONOI_SIMPLEX_MAX_VERTS; i++) {
			simplexVectorW[i] = new Vector3d();
			simplexPointsP[i] = new Vector3d();
			simplexPointsQ[i] = new Vector3d();
		}
	}

	public void removeVertex(int index) {
		assert(numVertices>0);
		numVertices--;
		simplexVectorW[index].set(simplexVectorW[numVertices]);
		simplexPointsP[index].set(simplexPointsP[numVertices]);
		simplexPointsQ[index].set(simplexPointsQ[numVertices]);
	}
	
	public void	reduceVertices(UsageBitfield usedVerts) {
		if ((numVertices() >= 4) && (!usedVerts.usedVertexD))
			removeVertex(3);

		if ((numVertices() >= 3) && (!usedVerts.usedVertexC))
			removeVertex(2);

		if ((numVertices() >= 2) && (!usedVerts.usedVertexB))
			removeVertex(1);

		if ((numVertices() >= 1) && (!usedVerts.usedVertexA))
			removeVertex(0);
	}
	
	
	public boolean updateClosestVectorAndPoints() {
		if (needsUpdate)
		{
			cachedBC.reset();

			needsUpdate = false;

			switch (numVertices())
			{
			case 0:
					cachedValidClosest = false;
					break;
			case 1:
				{
					cachedP1.set(simplexPointsP[0]);
					cachedP2.set(simplexPointsQ[0]);
					cachedV.sub(cachedP1, cachedP2); //== m_simplexVectorW[0]
					cachedBC.reset();
					cachedBC.setBarycentricCoordinates(1f, 0f, 0f, 0f);
					cachedValidClosest = cachedBC.isValid();
					break;
				}
			case 2:
				{
					Vector3d tmp = new Vector3d();
					
					//closest point origin from line segment
					Vector3d from = simplexVectorW[0];
					Vector3d to = simplexVectorW[1];
					Vector3d nearest = new Vector3d();

					Vector3d p = new Vector3d();
					p.set(0f, 0f, 0f);
					Vector3d diff = new Vector3d();
					diff.sub(p, from);

					Vector3d v = new Vector3d();
					v.sub(to, from);

					double t = v.dot(diff);

					if (t > 0) {
						double dotVV = v.dot(v);
						if (t < dotVV) {
							t /= dotVV;
							tmp.scale(t, v);
							diff.sub(tmp);
							cachedBC.usedVertices.usedVertexA = true;
							cachedBC.usedVertices.usedVertexB = true;
						} else {
							t = 1;
							diff.sub(v);
							// reduce to 1 point
							cachedBC.usedVertices.usedVertexB = true;
						}
					} else
					{
						t = 0;
						//reduce to 1 point
						cachedBC.usedVertices.usedVertexA = true;
					}
					cachedBC.setBarycentricCoordinates(1f-t, t, 0f, 0f);
					
					tmp.scale(t, v);
					nearest.add(from, tmp);

					tmp.sub(simplexPointsP[1], simplexPointsP[0]);
					tmp.scale(t);
					cachedP1.add(simplexPointsP[0], tmp);

					tmp.sub(simplexPointsQ[1], simplexPointsQ[0]);
					tmp.scale(t);
					cachedP2.add(simplexPointsQ[0], tmp);

					cachedV.sub(cachedP1, cachedP2);

					reduceVertices(cachedBC.usedVertices);

					cachedValidClosest = cachedBC.isValid();
					break;
				}
			case 3: 
				{ 
					Vector3d tmp1 = new Vector3d();
					Vector3d tmp2 = new Vector3d();
					Vector3d tmp3 = new Vector3d();
					
					// closest point origin from triangle 
					Vector3d p = new Vector3d();
					p.set(0f, 0f, 0f);

					Vector3d a = simplexVectorW[0]; 
					Vector3d b = simplexVectorW[1]; 
					Vector3d c = simplexVectorW[2]; 

					closestPtPointTriangle(p,a,b,c,cachedBC);

					tmp1.scale(cachedBC.barycentricCoords[0], simplexPointsP[0]);
					tmp2.scale(cachedBC.barycentricCoords[1], simplexPointsP[1]);
					tmp3.scale(cachedBC.barycentricCoords[2], simplexPointsP[2]);
					VectorUtil.add(cachedP1, tmp1, tmp2, tmp3);

					tmp1.scale(cachedBC.barycentricCoords[0], simplexPointsQ[0]);
					tmp2.scale(cachedBC.barycentricCoords[1], simplexPointsQ[1]);
					tmp3.scale(cachedBC.barycentricCoords[2], simplexPointsQ[2]);
					VectorUtil.add(cachedP2, tmp1, tmp2, tmp3);

					cachedV.sub(cachedP1, cachedP2);

					reduceVertices(cachedBC.usedVertices);
					cachedValidClosest = cachedBC.isValid(); 

					break; 
				}
			case 4:
				{
					Vector3d tmp1 = new Vector3d();
					Vector3d tmp2 = new Vector3d();
					Vector3d tmp3 = new Vector3d();
					Vector3d tmp4 = new Vector3d();
					
					Vector3d p = new Vector3d();
					p.set(0f, 0f, 0f);

					Vector3d a = simplexVectorW[0];
					Vector3d b = simplexVectorW[1];
					Vector3d c = simplexVectorW[2];
					Vector3d d = simplexVectorW[3];

					boolean hasSeperation = closestPtPointTetrahedron(p,a,b,c,d,cachedBC);

					if (hasSeperation)
					{
						tmp1.scale(cachedBC.barycentricCoords[0], simplexPointsP[0]);
						tmp2.scale(cachedBC.barycentricCoords[1], simplexPointsP[1]);
						tmp3.scale(cachedBC.barycentricCoords[2], simplexPointsP[2]);
						tmp4.scale(cachedBC.barycentricCoords[3], simplexPointsP[3]);
						VectorUtil.add(cachedP1, tmp1, tmp2, tmp3, tmp4);

						tmp1.scale(cachedBC.barycentricCoords[0], simplexPointsQ[0]);
						tmp2.scale(cachedBC.barycentricCoords[1], simplexPointsQ[1]);
						tmp3.scale(cachedBC.barycentricCoords[2], simplexPointsQ[2]);
						tmp4.scale(cachedBC.barycentricCoords[3], simplexPointsQ[3]);
						VectorUtil.add(cachedP2, tmp1, tmp2, tmp3, tmp4);

						cachedV.sub(cachedP1, cachedP2);
						reduceVertices (cachedBC.usedVertices);
					} else
					{
	//					printf("sub distance got penetration\n");

						if (cachedBC.degenerate)
						{
							cachedValidClosest = false;
						} else
						{
							cachedValidClosest = true;
							//degenerate case == false, penetration = true + zero
							cachedV.set(0f, 0f, 0f);
						}
						break;
					}

					cachedValidClosest = cachedBC.isValid();

					//closest point origin from tetrahedron
					break;
				}
			default:
				{
					cachedValidClosest = false;
				}
			}
		}

		return cachedValidClosest;
	}

	
	public boolean closestPtPointTriangle(Vector3d p, Vector3d a, Vector3d b, Vector3d c, SubSimplexClosestResult result) {
		result.usedVertices.reset();

		// Check if P in vertex region outside A
		Vector3d ab = new Vector3d();
		ab.sub(b, a);

		Vector3d ac = new Vector3d();
		ac.sub(c, a);

		Vector3d ap = new Vector3d();
		ap.sub(p, a);

		double d1 = ab.dot(ap);
		double d2 = ac.dot(ap);

		if (d1 <= 0f && d2 <= 0f) 
		{
			result.closestPointOnSimplex.set(a);
			result.usedVertices.usedVertexA = true;
			result.setBarycentricCoordinates(1f, 0f, 0f, 0f);
			return true; // a; // barycentric coordinates (1,0,0)
		}

		// Check if P in vertex region outside B
		Vector3d bp = new Vector3d();
		bp.sub(p, b);

		double d3 = ab.dot(bp);
		double d4 = ac.dot(bp);

		if (d3 >= 0f && d4 <= d3) 
		{
			result.closestPointOnSimplex.set(b);
			result.usedVertices.usedVertexB = true;
			result.setBarycentricCoordinates(0, 1f, 0f, 0f);

			return true; // b; // barycentric coordinates (0,1,0)
		}

		// Check if P in edge region of AB, if so return projection of P onto AB
		double vc = d1*d4 - d3*d2;
		if (vc <= 0f && d1 >= 0f && d3 <= 0f) {
			double v = d1 / (d1 - d3);
			result.closestPointOnSimplex.scaleAdd(v, ab, a);
			result.usedVertices.usedVertexA = true;
			result.usedVertices.usedVertexB = true;
			result.setBarycentricCoordinates(1f-v, v, 0f, 0f);
			return true;
			//return a + v * ab; // barycentric coordinates (1-v,v,0)
		}

		// Check if P in vertex region outside C
		Vector3d cp = new Vector3d();
		cp.sub(p, c);

		double d5 = ab.dot(cp);
		double d6 = ac.dot(cp);

		if (d6 >= 0f && d5 <= d6) 
		{
			result.closestPointOnSimplex.set(c);
			result.usedVertices.usedVertexC = true;
			result.setBarycentricCoordinates(0f, 0f, 1f, 0f);
			return true;//c; // barycentric coordinates (0,0,1)
		}

		// Check if P in edge region of AC, if so return projection of P onto AC
		double vb = d5*d2 - d1*d6;
		if (vb <= 0f && d2 >= 0f && d6 <= 0f) {
			double w = d2 / (d2 - d6);
			result.closestPointOnSimplex.scaleAdd(w, ac, a);
			result.usedVertices.usedVertexA = true;
			result.usedVertices.usedVertexC = true;
			result.setBarycentricCoordinates(1f-w, 0f, w, 0f);
			return true;
			//return a + w * ac; // barycentric coordinates (1-w,0,w)
		}

		// Check if P in edge region of BC, if so return projection of P onto BC
		double va = d3*d6 - d5*d4;
		if (va <= 0f && (d4 - d3) >= 0f && (d5 - d6) >= 0f) {
			double w = (d4 - d3) / ((d4 - d3) + (d5 - d6));

			Vector3d tmp = new Vector3d();
			tmp.sub(c, b);
			result.closestPointOnSimplex.scaleAdd(w, tmp, b);

			result.usedVertices.usedVertexB = true;
			result.usedVertices.usedVertexC = true;
			result.setBarycentricCoordinates(0, 1f-w, w, 0f);
			return true;		
		   // return b + w * (c - b); // barycentric coordinates (0,1-w,w)
		}

		// P inside face region. Compute Q through its barycentric coordinates (u,v,w)
		double denom = 1f / (va + vb + vc);
		double v = vb * denom;
		double w = vc * denom;

		Vector3d tmp1 = new Vector3d();
		Vector3d tmp2 = new Vector3d();

		tmp1.scale(v, ab);
		tmp2.scale(w, ac);
		VectorUtil.add(result.closestPointOnSimplex, a, tmp1, tmp2);
		result.usedVertices.usedVertexA = true;
		result.usedVertices.usedVertexB = true;
		result.usedVertices.usedVertexC = true;
		result.setBarycentricCoordinates(1f-v-w, v, w, 0f);

		return true;
		//	return a + ab * v + ac * w; // = u*a + v*b + w*c, u = va * denom = btScalar(1.0) - v - w
	}
	
	/// Test if point p and d lie on opposite sides of plane through abc
	
	public static int pointOutsideOfPlane(Vector3d p, Vector3d a, Vector3d b, Vector3d c, Vector3d d)
	{
		Vector3d tmp = new Vector3d();

		Vector3d normal = new Vector3d();
		normal.sub(b, a);
		tmp.sub(c, a);
		normal.cross(normal, tmp);

		tmp.sub(p, a);
		double signp = tmp.dot(normal); // [AP AB AC]

		tmp.sub(d, a);
		double signd = tmp.dot(normal); // [AD AB AC]

	//#ifdef CATCH_DEGENERATE_TETRAHEDRON
//	#ifdef BT_USE_DOUBLE_PRECISION
//	if (signd * signd < (btScalar(1e-8) * btScalar(1e-8)))
//		{
//			return -1;
//		}
//	#else
		if (signd * signd < ((1e-4f) * (1e-4f)))
		{
	//		printf("affine dependent/degenerate\n");//
			return -1;
		}
	//#endif

	//#endif
		// Points on opposite sides if expression signs are opposite
		return (signp * signd < 0f)? 1 : 0;
	}
	
	
	public boolean closestPtPointTetrahedron(Vector3d p, Vector3d a, Vector3d b, Vector3d c, Vector3d d, SubSimplexClosestResult finalResult) {
		SubSimplexClosestResult tempResult = subsimplexResultsPool.get();
		tempResult.reset();
		try {
			Vector3d tmp = new Vector3d();
			Vector3d q = new Vector3d();

			// Start out assuming point inside all halfspaces, so closest to itself
			finalResult.closestPointOnSimplex.set(p);
			finalResult.usedVertices.reset();
			finalResult.usedVertices.usedVertexA = true;
			finalResult.usedVertices.usedVertexB = true;
			finalResult.usedVertices.usedVertexC = true;
			finalResult.usedVertices.usedVertexD = true;

			int pointOutsideABC = pointOutsideOfPlane(p, a, b, c, d);
			int pointOutsideACD = pointOutsideOfPlane(p, a, c, d, b);
			int	pointOutsideADB = pointOutsideOfPlane(p, a, d, b, c);
			int	pointOutsideBDC = pointOutsideOfPlane(p, b, d, c, a);

		   if (pointOutsideABC < 0 || pointOutsideACD < 0 || pointOutsideADB < 0 || pointOutsideBDC < 0)
		   {
			   finalResult.degenerate = true;
			   return false;
		   }

		   if (pointOutsideABC == 0 && pointOutsideACD == 0 && pointOutsideADB == 0 && pointOutsideBDC == 0)
			 {
				 return false;
			 }


			double bestSqDist = Float.MAX_VALUE;
			// If point outside face abc then compute closest point on abc
			if (pointOutsideABC != 0) 
			{
				closestPtPointTriangle(p, a, b, c,tempResult);
				q.set(tempResult.closestPointOnSimplex);

				tmp.sub(q, p);
				double sqDist = tmp.dot(tmp);
				// Update best closest point if (squared) distance is less than current best
				if (sqDist < bestSqDist) {
					bestSqDist = sqDist;
					finalResult.closestPointOnSimplex.set(q);
					//convert result bitmask!
					finalResult.usedVertices.reset();
					finalResult.usedVertices.usedVertexA = tempResult.usedVertices.usedVertexA;
					finalResult.usedVertices.usedVertexB = tempResult.usedVertices.usedVertexB;
					finalResult.usedVertices.usedVertexC = tempResult.usedVertices.usedVertexC;
					finalResult.setBarycentricCoordinates(
							tempResult.barycentricCoords[VERTA],
							tempResult.barycentricCoords[VERTB],
							tempResult.barycentricCoords[VERTC],
							0
					);

				}
			}


			// Repeat test for face acd
			if (pointOutsideACD != 0) 
			{
				closestPtPointTriangle(p, a, c, d,tempResult);
				q.set(tempResult.closestPointOnSimplex);
				//convert result bitmask!

				tmp.sub(q, p);
				double sqDist = tmp.dot(tmp);
				if (sqDist < bestSqDist) 
				{
					bestSqDist = sqDist;
					finalResult.closestPointOnSimplex.set(q);
					finalResult.usedVertices.reset();
					finalResult.usedVertices.usedVertexA = tempResult.usedVertices.usedVertexA;

					finalResult.usedVertices.usedVertexC = tempResult.usedVertices.usedVertexB;
					finalResult.usedVertices.usedVertexD = tempResult.usedVertices.usedVertexC;
					finalResult.setBarycentricCoordinates(
							tempResult.barycentricCoords[VERTA],
							0,
							tempResult.barycentricCoords[VERTB],
							tempResult.barycentricCoords[VERTC]
					);

				}
			}
			// Repeat test for face adb


			if (pointOutsideADB != 0)
			{
				closestPtPointTriangle(p, a, d, b,tempResult);
				q.set(tempResult.closestPointOnSimplex);
				//convert result bitmask!

				tmp.sub(q, p);
				double sqDist = tmp.dot(tmp);
				if (sqDist < bestSqDist) 
				{
					bestSqDist = sqDist;
					finalResult.closestPointOnSimplex.set(q);
					finalResult.usedVertices.reset();
					finalResult.usedVertices.usedVertexA = tempResult.usedVertices.usedVertexA;
					finalResult.usedVertices.usedVertexB = tempResult.usedVertices.usedVertexC;

					finalResult.usedVertices.usedVertexD = tempResult.usedVertices.usedVertexB;
					finalResult.setBarycentricCoordinates(
							tempResult.barycentricCoords[VERTA],
							tempResult.barycentricCoords[VERTC],
							0,
							tempResult.barycentricCoords[VERTB]
					);

				}
			}
			// Repeat test for face bdc


			if (pointOutsideBDC != 0)
			{
				closestPtPointTriangle(p, b, d, c,tempResult);
				q.set(tempResult.closestPointOnSimplex);
				//convert result bitmask!
				tmp.sub(q, p);
				double sqDist = tmp.dot(tmp);
				if (sqDist < bestSqDist) 
				{
					bestSqDist = sqDist;
					finalResult.closestPointOnSimplex.set(q);
					finalResult.usedVertices.reset();
					//
					finalResult.usedVertices.usedVertexB = tempResult.usedVertices.usedVertexA;
					finalResult.usedVertices.usedVertexC = tempResult.usedVertices.usedVertexC;
					finalResult.usedVertices.usedVertexD = tempResult.usedVertices.usedVertexB;

					finalResult.setBarycentricCoordinates(
							0,
							tempResult.barycentricCoords[VERTA],
							tempResult.barycentricCoords[VERTC],
							tempResult.barycentricCoords[VERTB]
					);

				}
			}

			//help! we ended up full !

			if (finalResult.usedVertices.usedVertexA &&
				finalResult.usedVertices.usedVertexB &&
				finalResult.usedVertices.usedVertexC &&
				finalResult.usedVertices.usedVertexD) 
			{
				return true;
			}

			return true;
		}
		finally {
			subsimplexResultsPool.release(tempResult);
		}
	}
	
	/**
	 * Clear the simplex, remove all the vertices.
	 */
	public void reset() {
		cachedValidClosest = false;
		numVertices = 0;
		needsUpdate = true;
		lastW.set(1e30f, 1e30f, 1e30f);
		cachedBC.reset();
	}

	public void addVertex(Vector3d w, Vector3d p, Vector3d q) {
		lastW.set(w);
		needsUpdate = true;

		simplexVectorW[numVertices].set(w);
		simplexPointsP[numVertices].set(p);
		simplexPointsQ[numVertices].set(q);

		numVertices++;
	}

	/**
	 * Return/calculate the closest vertex.
	 */
	public boolean closest(Vector3d v) {
		boolean succes = updateClosestVectorAndPoints();
		v.set(cachedV);
		return succes;
	}

	public double maxVertex() {
		int i, numverts = numVertices();
		double maxV = 0f;
		for (i = 0; i < numverts; i++) {
			double curLen2 = simplexVectorW[i].lengthSquared();
			if (maxV < curLen2) {
				maxV = curLen2;
			}
		}
		return maxV;
	}

	public boolean fullSimplex() {
		return (numVertices == 4);
	}

	public int getSimplex(Vector3d[] pBuf, Vector3d[] qBuf, Vector3d[] yBuf) {
		for (int i = 0; i < numVertices(); i++) {
			yBuf[i].set(simplexVectorW[i]);
			pBuf[i].set(simplexPointsP[i]);
			qBuf[i].set(simplexPointsQ[i]);
		}
		return numVertices();
	}

	public boolean inSimplex(Vector3d w) {
		boolean found = false;
		int i, numverts = numVertices();
		//btScalar maxV = btScalar(0.);

		//w is in the current (reduced) simplex
		for (i = 0; i < numverts; i++) {
			if (simplexVectorW[i].equals(w)) {
				found = true;
			}
		}

		//check in case lastW is already removed
		if (w.equals(lastW)) {
			return true;
		}

		return found;
	}

	public void backup_closest(Vector3d v) {
		v.set(cachedV);
	}

	public boolean emptySimplex() {
		return (numVertices() == 0);
	}

	public void compute_points(Vector3d p1, Vector3d p2) {
		updateClosestVectorAndPoints();
		p1.set(cachedP1);
		p2.set(cachedP2);
	}

	public int numVertices() {
		return numVertices;
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class UsageBitfield {
		public boolean usedVertexA;
		public boolean usedVertexB;
		public boolean usedVertexC;
		public boolean usedVertexD;
		
		public void reset() {
			usedVertexA = false;
			usedVertexB = false;
			usedVertexC = false;
			usedVertexD = false;
		}
	}
	
	public static class SubSimplexClosestResult {
		public final Vector3d closestPointOnSimplex = new Vector3d();
		//MASK for m_usedVertices
		//stores the simplex vertex-usage, using the MASK, 
		// if m_usedVertices & MASK then the related vertex is used
		public final UsageBitfield usedVertices = new UsageBitfield();
		public final double[] barycentricCoords = new double[4];
		public boolean degenerate;
		
		public void reset() {
			degenerate = false;
			setBarycentricCoordinates(0f, 0f, 0f, 0f);
			usedVertices.reset();
		}

		public boolean isValid() {
			boolean valid = (barycentricCoords[0] >= 0f) &&
					(barycentricCoords[1] >= 0f) &&
					(barycentricCoords[2] >= 0f) &&
					(barycentricCoords[3] >= 0f);
			return valid;
		}

		public void setBarycentricCoordinates(double a, double b, double c, double d) {
			barycentricCoords[0] = a;
			barycentricCoords[1] = b;
			barycentricCoords[2] = c;
			barycentricCoords[3] = d;
		}
	}
	
}
