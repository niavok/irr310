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

package com.bulletphysics.linearmath;

import javax.vecmath.Quat4d;
import javax.vecmath.Vector3d;

import com.bulletphysics.BulletGlobals;

/**
 * Utility functions for quaternions.
 * 
 * @author jezek2
 */
public class QuaternionUtil {

	public static double getAngle(Quat4d q) {
		double s = 2f * (double) Math.acos(q.w);
		return s;
	}
	
	public static void setRotation(Quat4d q, Vector3d axis, double angle) {
		double d = axis.length();
		assert (d != 0f);
		double s = (double)Math.sin(angle * 0.5f) / d;
		q.set(axis.x * s, axis.y * s, axis.z * s, (double) Math.cos(angle * 0.5f));
	}
	
	// Game Programming Gems 2.10. make sure v0,v1 are normalized
	public static Quat4d shortestArcQuat(Vector3d v0, Vector3d v1, Quat4d out) {
		Vector3d c = new Vector3d();
		c.cross(v0, v1);
		double d = v0.dot(v1);

		if (d < -1.0 + BulletGlobals.FLT_EPSILON) {
			// just pick any vector
			out.set(0.0f, 1.0f, 0.0f, 0.0f);
			return out;
		}

		double s = (double) Math.sqrt((1.0f + d) * 2.0f);
		double rs = 1.0f / s;

		out.set(c.x * rs, c.y * rs, c.z * rs, s * 0.5f);
		return out;
	}
	
	public static void mul(Quat4d q, Vector3d w) {
		double rx = q.w * w.x + q.y * w.z - q.z * w.y;
		double ry = q.w * w.y + q.z * w.x - q.x * w.z;
		double rz = q.w * w.z + q.x * w.y - q.y * w.x;
		double rw = -q.x * w.x - q.y * w.y - q.z * w.z;
		q.set(rx, ry, rz, rw);
	}
	
	public static Vector3d quatRotate(Quat4d rotation, Vector3d v, Vector3d out) {
		Quat4d q = new Quat4d(rotation);
		QuaternionUtil.mul(q, v);

		Quat4d tmp = new Quat4d();
		inverse(tmp, rotation);
		q.mul(tmp);
		
		out.set(q.x, q.y, q.z);
		return out;
	}
	
	public static void inverse(Quat4d q) {
		q.x = -q.x;
		q.y = -q.y;
		q.z = -q.z;
	}
	
	public static void inverse(Quat4d q, Quat4d src) {
		q.x = -src.x;
		q.y = -src.y;
		q.z = -src.z;
		q.w = src.w;
	}

	public static void setEuler(Quat4d q, double yaw, double pitch, double roll) {
		double halfYaw = yaw * 0.5f;
		double halfPitch = pitch * 0.5f;
		double halfRoll = roll * 0.5f;
		double cosYaw = (double)Math.cos(halfYaw);
		double sinYaw = (double)Math.sin(halfYaw);
		double cosPitch = (double)Math.cos(halfPitch);
		double sinPitch = (double)Math.sin(halfPitch);
		double cosRoll = (double)Math.cos(halfRoll);
		double sinRoll = (double)Math.sin(halfRoll);
		q.x = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
		q.y = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;
		q.z = sinRoll * cosPitch * cosYaw - cosRoll * sinPitch * sinYaw;
		q.w = cosRoll * cosPitch * cosYaw + sinRoll * sinPitch * sinYaw;
	}

}
