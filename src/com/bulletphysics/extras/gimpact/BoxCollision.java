/*
 * Java port of Bullet (c) 2008 Martin Dvorak <jezek2@advel.cz>
 *
 * This source file is part of GIMPACT Library.
 *
 * For the latest info, see http://gimpact.sourceforge.net/
 *
 * Copyright (c) 2007 Francisco Leon Najera. C.C. 80087371.
 * email: projectileman@yahoo.com
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

package com.bulletphysics.extras.gimpact;

import javax.vecmath.Matrix3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

/**
 *
 * @author jezek2
 */
class BoxCollision {
	
	public static final double BOX_PLANE_EPSILON = 0.000001f;

	public static boolean BT_GREATER(double x, double y) {
		return Math.abs(x) > y;
	}

	public static double BT_MAX3(double a, double b, double c) {
		return Math.max(a, Math.max(b, c));
	}

	public static double BT_MIN3(double a, double b, double c) {
		return Math.min(a, Math.min(b, c));
	}
	
	public static boolean TEST_CROSS_EDGE_BOX_MCR(Vector3d edge, Vector3d absolute_edge, Vector3d pointa, Vector3d pointb, Vector3d _extend, int i_dir_0, int i_dir_1, int i_comp_0, int i_comp_1) {
		double dir0 = -VectorUtil.getCoord(edge, i_dir_0);
		double dir1 = VectorUtil.getCoord(edge, i_dir_1);
		double pmin = VectorUtil.getCoord(pointa, i_comp_0) * dir0 + VectorUtil.getCoord(pointa, i_comp_1) * dir1;
		double pmax = VectorUtil.getCoord(pointb, i_comp_0) * dir0 + VectorUtil.getCoord(pointb, i_comp_1) * dir1;
		if (pmin > pmax) {
			//BT_SWAP_NUMBERS(pmin,pmax);
			pmin = pmin + pmax;
			pmax = pmin - pmax;
			pmin = pmin - pmax;
		}
		double abs_dir0 = VectorUtil.getCoord(absolute_edge, i_dir_0);
		double abs_dir1 = VectorUtil.getCoord(absolute_edge, i_dir_1);
		double rad = VectorUtil.getCoord(_extend, i_comp_0) * abs_dir0 + VectorUtil.getCoord(_extend, i_comp_1) * abs_dir1;
		if (pmin > rad || -rad > pmax) {
			return false;
		}
		return true;
	}

	public static boolean TEST_CROSS_EDGE_BOX_X_AXIS_MCR(Vector3d edge, Vector3d absolute_edge, Vector3d pointa, Vector3d pointb, Vector3d _extend) {
		return TEST_CROSS_EDGE_BOX_MCR(edge, absolute_edge, pointa, pointb, _extend, 2, 1, 1, 2);
	}

	public static boolean TEST_CROSS_EDGE_BOX_Y_AXIS_MCR(Vector3d edge, Vector3d absolute_edge, Vector3d pointa, Vector3d pointb, Vector3d _extend) {
		return TEST_CROSS_EDGE_BOX_MCR(edge, absolute_edge, pointa, pointb, _extend, 0, 2, 2, 0);
	}

	public static boolean TEST_CROSS_EDGE_BOX_Z_AXIS_MCR(Vector3d edge, Vector3d absolute_edge, Vector3d pointa, Vector3d pointb, Vector3d _extend) {
		return TEST_CROSS_EDGE_BOX_MCR(edge, absolute_edge, pointa, pointb, _extend, 1, 0, 0, 1);
	}
	
	/**
	 * Returns the dot product between a vec3f and the col of a matrix.
	 */
	public static double bt_mat3_dot_col(Matrix3d mat, Vector3d vec3, int colindex) {
		return vec3.x*mat.getElement(0, colindex) + vec3.y*mat.getElement(1, colindex) + vec3.z*mat.getElement(2, colindex);
	}

	/**
	 * Compairison of transformation objects.
	 */
	public static boolean compareTransformsEqual(Transform t1, Transform t2) {
		return t1.equals(t2);
	}
	
	////////////////////////////////////////////////////////////////////////////

	public static class BoxBoxTransformCache {
		public final Vector3d T1to0 = new Vector3d(); // Transforms translation of model1 to model 0
		public final Matrix3d R1to0 = new Matrix3d(); // Transforms Rotation of model1 to model 0, equal  to R0' * R1
		public final Matrix3d AR = new Matrix3d();    // Absolute value of m_R1to0
		
		public void set(BoxBoxTransformCache cache) {
			throw new UnsupportedOperationException();
		}
		
		public void calc_absolute_matrix() {
			//static const btVector3 vepsi(1e-6f,1e-6f,1e-6f);
			//m_AR[0] = vepsi + m_R1to0[0].absolute();
			//m_AR[1] = vepsi + m_R1to0[1].absolute();
			//m_AR[2] = vepsi + m_R1to0[2].absolute();

			for (int i=0; i<3; i++) {
				for (int j=0; j<3; j++) {
					AR.setElement(i, j, 1e-6f + Math.abs(R1to0.getElement(i, j)));
				}
			}
		}

		/**
		 * Calc the transformation relative  1 to 0. Inverts matrics by transposing.
		 */
		public void calc_from_homogenic(Transform trans0, Transform trans1) {
			Transform temp_trans = new Transform();
			temp_trans.inverse(trans0);
			temp_trans.mul(trans1);

			T1to0.set(temp_trans.origin);
			R1to0.set(temp_trans.basis);

			calc_absolute_matrix();
		}
		
		/**
		 * Calcs the full invertion of the matrices. Useful for scaling matrices.
		 */
		public void calc_from_full_invert(Transform trans0, Transform trans1) {
			R1to0.invert(trans0.basis);
			T1to0.negate(trans0.origin);
			R1to0.transform(T1to0);

			Vector3d tmp = new Vector3d();
			tmp.set(trans1.origin);
			R1to0.transform(tmp);
			T1to0.add(tmp);

			R1to0.mul(trans1.basis);

			calc_absolute_matrix();
		}
		
		public Vector3d transform(Vector3d point, Vector3d out) {
			if (point == out) {
				point = new Vector3d(point);
			}
			
			Vector3d tmp = new Vector3d();
			R1to0.getRow(0, tmp);
			out.x = tmp.dot(point) + T1to0.x;
			R1to0.getRow(1, tmp);
			out.y = tmp.dot(point) + T1to0.y;
			R1to0.getRow(2, tmp);
			out.z = tmp.dot(point) + T1to0.z;
			return out;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////
	
	public static class AABB {
		public final Vector3d min = new Vector3d();
		public final Vector3d max = new Vector3d();
		
		public AABB() {
		}

		public AABB(Vector3d V1, Vector3d V2, Vector3d V3) {
			calc_from_triangle(V1, V2, V3);
		}

		public AABB(Vector3d V1, Vector3d V2, Vector3d V3, double margin) {
			calc_from_triangle_margin(V1, V2, V3, margin);
		}

		public AABB(AABB other) {
			set(other);
		}

		public AABB(AABB other, double margin) {
			this(other);
			min.x -= margin;
			min.y -= margin;
			min.z -= margin;
			max.x += margin;
			max.y += margin;
			max.z += margin;
		}

		public void init(Vector3d V1, Vector3d V2, Vector3d V3, double margin) {
			calc_from_triangle_margin(V1, V2, V3, margin);
		}

		public void set(AABB other) {
			min.set(other.min);
			max.set(other.max);
		}

		public void invalidate() {
			min.set(BulletGlobals.SIMD_INFINITY, BulletGlobals.SIMD_INFINITY, BulletGlobals.SIMD_INFINITY);
			max.set(-BulletGlobals.SIMD_INFINITY, -BulletGlobals.SIMD_INFINITY, -BulletGlobals.SIMD_INFINITY);
		}

		public void increment_margin(double margin) {
			min.x -= margin;
			min.y -= margin;
			min.z -= margin;
			max.x += margin;
			max.y += margin;
			max.z += margin;
		}

		public void copy_with_margin(AABB other, double margin) {
			min.x = other.min.x - margin;
			min.y = other.min.y - margin;
			min.z = other.min.z - margin;

			max.x = other.max.x + margin;
			max.y = other.max.y + margin;
			max.z = other.max.z + margin;
		}
		
		public void calc_from_triangle(Vector3d V1, Vector3d V2, Vector3d V3) {
			min.x = BT_MIN3(V1.x, V2.x, V3.x);
			min.y = BT_MIN3(V1.y, V2.y, V3.y);
			min.z = BT_MIN3(V1.z, V2.z, V3.z);

			max.x = BT_MAX3(V1.x, V2.x, V3.x);
			max.y = BT_MAX3(V1.y, V2.y, V3.y);
			max.z = BT_MAX3(V1.z, V2.z, V3.z);
		}

		public void calc_from_triangle_margin(Vector3d V1, Vector3d V2, Vector3d V3, double margin) {
			calc_from_triangle(V1, V2, V3);
			min.x -= margin;
			min.y -= margin;
			min.z -= margin;
			max.x += margin;
			max.y += margin;
			max.z += margin;
		}
		
		/**
		 * Apply a transform to an AABB.
		 */
		public void appy_transform(Transform trans) {
			Vector3d tmp = new Vector3d();

			Vector3d center = new Vector3d();
			center.add(max, min);
			center.scale(0.5f);

			Vector3d extends_ = new Vector3d();
			extends_.sub(max, center);

			// Compute new center
			trans.transform(center);

			Vector3d textends = new Vector3d();

			trans.basis.getRow(0, tmp);
			tmp.absolute();
			textends.x = extends_.dot(tmp);

			trans.basis.getRow(1, tmp);
			tmp.absolute();
			textends.y = extends_.dot(tmp);

			trans.basis.getRow(2, tmp);
			tmp.absolute();
			textends.z = extends_.dot(tmp);

			min.sub(center, textends);
			max.add(center, textends);
		}

		/**
		 * Apply a transform to an AABB.
		 */
		public void appy_transform_trans_cache(BoxBoxTransformCache trans) {
			Vector3d tmp = new Vector3d();

			Vector3d center = new Vector3d();
			center.add(max, min);
			center.scale(0.5f);

			Vector3d extends_ = new Vector3d();
			extends_.sub(max, center);

			// Compute new center
			trans.transform(center, center);

			Vector3d textends = new Vector3d();

			trans.R1to0.getRow(0, tmp);
			tmp.absolute();
			textends.x = extends_.dot(tmp);

			trans.R1to0.getRow(1, tmp);
			tmp.absolute();
			textends.y = extends_.dot(tmp);

			trans.R1to0.getRow(2, tmp);
			tmp.absolute();
			textends.z = extends_.dot(tmp);

			min.sub(center, textends);
			max.add(center, textends);
		}
		
		/**
		 * Merges a Box.
		 */
		public void merge(AABB box) {
			min.x = Math.min(min.x, box.min.x);
			min.y = Math.min(min.y, box.min.y);
			min.z = Math.min(min.z, box.min.z);

			max.x = Math.max(max.x, box.max.x);
			max.y = Math.max(max.y, box.max.y);
			max.z = Math.max(max.z, box.max.z);
		}

		/**
		 * Merges a point.
		 */
		public void merge_point(Vector3d point) {
			min.x = Math.min(min.x, point.x);
			min.y = Math.min(min.y, point.y);
			min.z = Math.min(min.z, point.z);

			max.x = Math.max(max.x, point.x);
			max.y = Math.max(max.y, point.y);
			max.z = Math.max(max.z, point.z);
		}
		
		/**
		 * Gets the extend and center.
		 */
		public void get_center_extend(Vector3d center, Vector3d extend) {
			center.add(max, min);
			center.scale(0.5f);

			extend.sub(max, center);
		}
		
		/**
		 * Finds the intersecting box between this box and the other.
		 */
		public void find_intersection(AABB other, AABB intersection) {
			intersection.min.x = Math.max(other.min.x, min.x);
			intersection.min.y = Math.max(other.min.y, min.y);
			intersection.min.z = Math.max(other.min.z, min.z);

			intersection.max.x = Math.min(other.max.x, max.x);
			intersection.max.y = Math.min(other.max.y, max.y);
			intersection.max.z = Math.min(other.max.z, max.z);
		}

		public boolean has_collision(AABB other) {
			if (min.x > other.max.x ||
			    max.x < other.min.x ||
			    min.y > other.max.y ||
			    max.y < other.min.y ||
			    min.z > other.max.z ||
			    max.z < other.min.z) {
				return false;
			}
			return true;
		}
		
		/**
		 * Finds the Ray intersection parameter.
		 * 
		 * @param aabb     aligned box
		 * @param vorigin  a vec3f with the origin of the ray
		 * @param vdir     a vec3f with the direction of the ray
		 */
		public boolean collide_ray(Vector3d vorigin, Vector3d vdir) {
			Vector3d extents = new Vector3d(), center = new Vector3d();
			get_center_extend(center, extents);

			double Dx = vorigin.x - center.x;
			if (BT_GREATER(Dx, extents.x) && Dx * vdir.x >= 0.0f) return false;
			
			double Dy = vorigin.y - center.y;
			if (BT_GREATER(Dy, extents.y) && Dy * vdir.y >= 0.0f) return false;
			
			double Dz = vorigin.z - center.z;
			if (BT_GREATER(Dz, extents.z) && Dz * vdir.z >= 0.0f) return false;
			
			double f = vdir.y * Dz - vdir.z * Dy;
			if (Math.abs(f) > extents.y * Math.abs(vdir.z) + extents.z * Math.abs(vdir.y)) return false;
			
			f = vdir.z * Dx - vdir.x * Dz;
			if (Math.abs(f) > extents.x * Math.abs(vdir.z) + extents.z * Math.abs(vdir.x)) return false;
			
			f = vdir.x * Dy - vdir.y * Dx;
			if (Math.abs(f) > extents.x * Math.abs(vdir.y) + extents.y * Math.abs(vdir.x)) return false;
			
			return true;
		}
	
		public void projection_interval(Vector3d direction, double[] vmin, double[] vmax) {
			Vector3d tmp = new Vector3d();

			Vector3d center = new Vector3d();
			Vector3d extend = new Vector3d();
			get_center_extend(center, extend);

			double _fOrigin = direction.dot(center);
			tmp.absolute(direction);
			double _fMaximumExtent = extend.dot(tmp);
			vmin[0] = _fOrigin - _fMaximumExtent;
			vmax[0] = _fOrigin + _fMaximumExtent;
		}

		public PlaneIntersectionType plane_classify(Vector4d plane) {
			Vector3d tmp = new Vector3d();

			double[] _fmin = new double[1], _fmax = new double[1];
			tmp.set(plane.x, plane.y, plane.z);
			projection_interval(tmp, _fmin, _fmax);

			if (plane.w > _fmax[0] + BOX_PLANE_EPSILON) {
				return PlaneIntersectionType.BACK_PLANE; // 0
			}

			if (plane.w + BOX_PLANE_EPSILON >= _fmin[0]) {
				return PlaneIntersectionType.COLLIDE_PLANE; //1
			}
			
			return PlaneIntersectionType.FRONT_PLANE; //2
		}
		
		public boolean overlapping_trans_conservative(AABB box, Transform trans1_to_0) {
			AABB tbox =new AABB(box);
			tbox.appy_transform(trans1_to_0);
			return has_collision(tbox);
		}

		public boolean overlapping_trans_conservative2(AABB box, BoxBoxTransformCache trans1_to_0) {
			AABB tbox = new AABB(box);
			tbox.appy_transform_trans_cache(trans1_to_0);
			return has_collision(tbox);
		}

		/**
		 * transcache is the transformation cache from box to this AABB.
		 */
		public boolean overlapping_trans_cache(AABB box, BoxBoxTransformCache transcache, boolean fulltest) {
			Vector3d tmp = new Vector3d();

			// Taken from OPCODE
			Vector3d ea = new Vector3d(), eb = new Vector3d(); //extends
			Vector3d ca = new Vector3d(), cb = new Vector3d(); //extends
			get_center_extend(ca, ea);
			box.get_center_extend(cb, eb);

			Vector3d T = new Vector3d();
			double t, t2;

			// Class I : A's basis vectors
			for (int i=0; i<3; i++) {
				transcache.R1to0.getRow(i, tmp);
				VectorUtil.setCoord(T, i, tmp.dot(cb) + VectorUtil.getCoord(transcache.T1to0, i) - VectorUtil.getCoord(ca, i));

				transcache.AR.getRow(i, tmp);
				t = tmp.dot(eb) + VectorUtil.getCoord(ea, i);
				if (BT_GREATER(VectorUtil.getCoord(T, i), t)) {
					return false;
				}
			}
			// Class II : B's basis vectors
			for (int i=0; i<3; i++) {
				t = bt_mat3_dot_col(transcache.R1to0, T, i);
				t2 = bt_mat3_dot_col(transcache.AR, ea, i) + VectorUtil.getCoord(eb, i);
				if (BT_GREATER(t, t2)) {
					return false;
				}
			}
			// Class III : 9 cross products
			if (fulltest) {
				int m, n, o, p, q, r;
				for (int i = 0; i < 3; i++) {
					m = (i+1) % 3;
					n = (i+2) % 3;
					o = (i == 0)? 1:0;
					p = (i == 2)? 1:2;
					for (int j=0; j<3; j++) {
						q = j == 2 ? 1 : 2;
						r = j == 0 ? 1 : 0;
						t = VectorUtil.getCoord(T, n) * transcache.R1to0.getElement(m, j) - VectorUtil.getCoord(T, m) * transcache.R1to0.getElement(n, j);
						t2 = VectorUtil.getCoord(ea, o) * transcache.AR.getElement(p, j) + VectorUtil.getCoord(ea, p) * transcache.AR.getElement(o, j) +
								VectorUtil.getCoord(eb, r) * transcache.AR.getElement(i, q) + VectorUtil.getCoord(eb, q) * transcache.AR.getElement(i, r);
						if (BT_GREATER(t, t2)) {
							return false;
						}
					}
				}
			}
			return true;
		}
		
		/**
		 * Simple test for planes.
		 */
		public boolean collide_plane(Vector4d plane) {
			PlaneIntersectionType classify = plane_classify(plane);
			return (classify == PlaneIntersectionType.COLLIDE_PLANE);
		}
		
		/**
		 * Test for a triangle, with edges.
		 */
		public boolean collide_triangle_exact(Vector3d p1, Vector3d p2, Vector3d p3, Vector4d triangle_plane) {
			if (!collide_plane(triangle_plane)) {
				return false;
			}
			Vector3d center = new Vector3d(), extends_ = new Vector3d();
			get_center_extend(center, extends_);

			Vector3d v1 = new Vector3d();
			v1.sub(p1, center);
			Vector3d v2 = new Vector3d();
			v2.sub(p2, center);
			Vector3d v3 = new Vector3d();
			v3.sub(p3, center);

			// First axis
			Vector3d diff = new Vector3d();
			diff.sub(v2, v1);
			Vector3d abs_diff = new Vector3d();
			abs_diff.absolute(diff);

			// Test With X axis
			TEST_CROSS_EDGE_BOX_X_AXIS_MCR(diff, abs_diff, v1, v3, extends_);
			// Test With Y axis
			TEST_CROSS_EDGE_BOX_Y_AXIS_MCR(diff, abs_diff, v1, v3, extends_);
			// Test With Z axis
			TEST_CROSS_EDGE_BOX_Z_AXIS_MCR(diff, abs_diff, v1, v3, extends_);

			diff.sub(v3, v2);
			abs_diff.absolute(diff);

			// Test With X axis
			TEST_CROSS_EDGE_BOX_X_AXIS_MCR(diff, abs_diff, v2, v1, extends_);
			// Test With Y axis
			TEST_CROSS_EDGE_BOX_Y_AXIS_MCR(diff, abs_diff, v2, v1, extends_);
			// Test With Z axis
			TEST_CROSS_EDGE_BOX_Z_AXIS_MCR(diff, abs_diff, v2, v1, extends_);

			diff.sub(v1, v3);
			abs_diff.absolute(diff);

			// Test With X axis
			TEST_CROSS_EDGE_BOX_X_AXIS_MCR(diff, abs_diff, v3, v2, extends_);
			// Test With Y axis
			TEST_CROSS_EDGE_BOX_Y_AXIS_MCR(diff, abs_diff, v3, v2, extends_);
			// Test With Z axis
			TEST_CROSS_EDGE_BOX_Z_AXIS_MCR(diff, abs_diff, v3, v2, extends_);

			return true;
		}
	}
	
}
