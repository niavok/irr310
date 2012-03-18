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

// Dbvt implementation by Nathanael Presson

package com.bulletphysics.collision.broadphase;

import javax.vecmath.Vector3d;

import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

/**
 *
 * @author jezek2
 */
public class DbvtAabbMm {

	private final Vector3d mi = new Vector3d();
	private final Vector3d mx = new Vector3d();

	public DbvtAabbMm() {
	}

	public DbvtAabbMm(DbvtAabbMm o) {
		set(o);
	}
	
	public void set(DbvtAabbMm o) {
		mi.set(o.mi);
		mx.set(o.mx);
	}
	
	public static void swap(DbvtAabbMm p1, DbvtAabbMm p2) {
		Vector3d tmp = new Vector3d();
		
		tmp.set(p1.mi);
		p1.mi.set(p2.mi);
		p2.mi.set(tmp);

		tmp.set(p1.mx);
		p1.mx.set(p2.mx);
		p2.mx.set(tmp);
	}

	public Vector3d Center(Vector3d out) {
		out.add(mi, mx);
		out.scale(0.5f);
		return out;
	}
	
	public Vector3d Lengths(Vector3d out) {
		out.sub(mx, mi);
		return out;
	}
	
	public Vector3d Extents(Vector3d out) {
		out.sub(mx, mi);
		out.scale(0.5f);
		return out;
	}
	
	public Vector3d Mins() {
		return mi;
	}

	public Vector3d Maxs() {
		return mx;
	}
	
	public static DbvtAabbMm FromCE(Vector3d c, Vector3d e, DbvtAabbMm out) {
		DbvtAabbMm box = out;
		box.mi.sub(c, e);
		box.mx.add(c, e);
		return box;
	}

	public static DbvtAabbMm FromCR(Vector3d c, double r, DbvtAabbMm out) {
		Vector3d tmp = new Vector3d();
		tmp.set(r, r, r);
		return FromCE(c, tmp, out);
	}

	public static DbvtAabbMm FromMM(Vector3d mi, Vector3d mx, DbvtAabbMm out) {
		DbvtAabbMm box = out;
		box.mi.set(mi);
		box.mx.set(mx);
		return box;
	}
	
	//public static  DbvtAabbMm	FromPoints( btVector3* pts,int n);
	//public static  DbvtAabbMm	FromPoints( btVector3** ppts,int n);
	
	public void Expand(Vector3d e) {
		mi.sub(e);
		mx.add(e);
	}

	public void SignedExpand(Vector3d e) {
		if (e.x > 0) {
			mx.x += e.x;
		}
		else {
			mi.x += e.x;
		}
		
		if (e.y > 0) {
			mx.y += e.y;
		}
		else {
			mi.y += e.y;
		}
		
		if (e.z > 0) {
			mx.z += e.z;
		}
		else {
			mi.z += e.z;
		}
	}

	public boolean Contain(DbvtAabbMm a) {
		return ((mi.x <= a.mi.x) &&
		        (mi.y <= a.mi.y) &&
		        (mi.z <= a.mi.z) &&
		        (mx.x >= a.mx.x) &&
		        (mx.y >= a.mx.y) &&
		        (mx.z >= a.mx.z));
	}

	public int Classify(Vector3d n, double o, int s) {
		Vector3d pi = new Vector3d();
		Vector3d px = new Vector3d();

		switch (s) {
			case (0 + 0 + 0):
				px.set(mi.x, mi.y, mi.z);
				pi.set(mx.x, mx.y, mx.z);
				break;
			case (1 + 0 + 0):
				px.set(mx.x, mi.y, mi.z);
				pi.set(mi.x, mx.y, mx.z);
				break;
			case (0 + 2 + 0):
				px.set(mi.x, mx.y, mi.z);
				pi.set(mx.x, mi.y, mx.z);
				break;
			case (1 + 2 + 0):
				px.set(mx.x, mx.y, mi.z);
				pi.set(mi.x, mi.y, mx.z);
				break;
			case (0 + 0 + 4):
				px.set(mi.x, mi.y, mx.z);
				pi.set(mx.x, mx.y, mi.z);
				break;
			case (1 + 0 + 4):
				px.set(mx.x, mi.y, mx.z);
				pi.set(mi.x, mx.y, mi.z);
				break;
			case (0 + 2 + 4):
				px.set(mi.x, mx.y, mx.z);
				pi.set(mx.x, mi.y, mi.z);
				break;
			case (1 + 2 + 4):
				px.set(mx.x, mx.y, mx.z);
				pi.set(mi.x, mi.y, mi.z);
				break;
		}
		
		if ((n.dot(px) + o) < 0) {
			return -1;
		}
		if ((n.dot(pi) + o) >= 0) {
			return +1;
		}
		return 0;
	}

	public double ProjectMinimum(Vector3d v, int signs) {
		Vector3d[] b = new Vector3d[] { mx, mi };
		Vector3d p = new Vector3d();
		p.set(b[(signs >> 0) & 1].x,
		      b[(signs >> 1) & 1].y,
		      b[(signs >> 2) & 1].z);
		return p.dot(v);
	}
	 
	public static boolean Intersect(DbvtAabbMm a, DbvtAabbMm b) {
		return ((a.mi.x <= b.mx.x) &&
		        (a.mx.x >= b.mi.x) &&
		        (a.mi.y <= b.mx.y) &&
		        (a.mx.y >= b.mi.y) &&
		        (a.mi.z <= b.mx.z) &&
		        (a.mx.z >= b.mi.z));
	}

	public static boolean Intersect(DbvtAabbMm a, DbvtAabbMm b, Transform xform) {
		Vector3d d0 = new Vector3d();
		Vector3d d1 = new Vector3d();
		Vector3d tmp = new Vector3d();

		// JAVA NOTE: check
		b.Center(d0);
		xform.transform(d0);
		d0.sub(a.Center(tmp));

		MatrixUtil.transposeTransform(d1, d0, xform.basis);

		double[] s0 = new double[] { 0, 0 };
		double[] s1 = new double[2];
		s1[0] = xform.origin.dot(d0);
		s1[1] = s1[0];

		a.AddSpan(d0, s0, 0, s0, 1);
		b.AddSpan(d1, s1, 0, s1, 1);
		if (s0[0] > (s1[1])) {
			return false;
		}
		if (s0[1] < (s1[0])) {
			return false;
		}
		return true;
	}

	public static boolean Intersect(DbvtAabbMm a, Vector3d b) {
		return ((b.x >= a.mi.x) &&
		        (b.y >= a.mi.y) &&
		        (b.z >= a.mi.z) &&
		        (b.x <= a.mx.x) &&
		        (b.y <= a.mx.y) &&
		        (b.z <= a.mx.z));
	}

	public static boolean Intersect(DbvtAabbMm a, Vector3d org, Vector3d invdir, int[] signs) {
		Vector3d[] bounds = new Vector3d[]{a.mi, a.mx};
		double txmin = (bounds[signs[0]].x - org.x) * invdir.x;
		double txmax = (bounds[1 - signs[0]].x - org.x) * invdir.x;
		double tymin = (bounds[signs[1]].y - org.y) * invdir.y;
		double tymax = (bounds[1 - signs[1]].y - org.y) * invdir.y;
		if ((txmin > tymax) || (tymin > txmax)) {
			return false;
		}
		
		if (tymin > txmin) {
			txmin = tymin;
		}
		if (tymax < txmax) {
			txmax = tymax;
		}
		double tzmin = (bounds[signs[2]].z - org.z) * invdir.z;
		double tzmax = (bounds[1 - signs[2]].z - org.z) * invdir.z;
		if ((txmin > tzmax) || (tzmin > txmax)) {
			return false;
		}
		
		if (tzmin > txmin) {
			txmin = tzmin;
		}
		if (tzmax < txmax) {
			txmax = tzmax;
		}
		return (txmax > 0);
	}

	public static double Proximity(DbvtAabbMm a, DbvtAabbMm b) {
		Vector3d d = new Vector3d();
		Vector3d tmp = new Vector3d();

		d.add(a.mi, a.mx);
		tmp.add(b.mi, b.mx);
		d.sub(tmp);
		return Math.abs(d.x) + Math.abs(d.y) + Math.abs(d.z);
	}

	public static void Merge(DbvtAabbMm a, DbvtAabbMm b, DbvtAabbMm r) {
		for (int i=0; i<3; i++) {
			if (VectorUtil.getCoord(a.mi, i) < VectorUtil.getCoord(b.mi, i)) {
				VectorUtil.setCoord(r.mi, i, VectorUtil.getCoord(a.mi, i));
			}
			else {
				VectorUtil.setCoord(r.mi, i, VectorUtil.getCoord(b.mi, i));
			}
			
			if (VectorUtil.getCoord(a.mx, i) > VectorUtil.getCoord(b.mx, i)) {
				VectorUtil.setCoord(r.mx, i, VectorUtil.getCoord(a.mx, i));
			}
			else {
				VectorUtil.setCoord(r.mx, i, VectorUtil.getCoord(b.mx, i));
			}
		}
	}

	public static boolean NotEqual(DbvtAabbMm a, DbvtAabbMm b) {
		return ((a.mi.x != b.mi.x) ||
		        (a.mi.y != b.mi.y) ||
		        (a.mi.z != b.mi.z) ||
		        (a.mx.x != b.mx.x) ||
		        (a.mx.y != b.mx.y) ||
		        (a.mx.z != b.mx.z));
	}
	
	private void AddSpan(Vector3d d, double[] smi, int smi_idx, double[] smx, int smx_idx) {
		for (int i=0; i<3; i++) {
			if (VectorUtil.getCoord(d, i) < 0) {
				smi[smi_idx] += VectorUtil.getCoord(mx, i) * VectorUtil.getCoord(d, i);
				smx[smx_idx] += VectorUtil.getCoord(mi, i) * VectorUtil.getCoord(d, i);
			}
			else {
				smi[smi_idx] += VectorUtil.getCoord(mi, i) * VectorUtil.getCoord(d, i);
				smx[smx_idx] += VectorUtil.getCoord(mx, i) * VectorUtil.getCoord(d, i);
			}
		}
	}
	
}
