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

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.linearmath.VectorUtil;

/**
 *
 * @author jezek2
 */
class PlaneShape {

	public static void get_plane_equation(StaticPlaneShape shape, Vector4d equation) {
		Vector3d tmp = new Vector3d();
		equation.set(shape.getPlaneNormal(tmp));
		equation.w = shape.getPlaneConstant();
	}
	
	public static void get_plane_equation_transformed(StaticPlaneShape shape, Transform trans, Vector4d equation) {
		get_plane_equation(shape, equation);

		Vector3d tmp = new Vector3d();

		trans.basis.getRow(0, tmp);
		double x = VectorUtil.dot3(tmp, equation);
		trans.basis.getRow(1, tmp);
		double y = VectorUtil.dot3(tmp, equation);
		trans.basis.getRow(2, tmp);
		double z = VectorUtil.dot3(tmp, equation);

		double w = VectorUtil.dot3(trans.origin, equation) + equation.w;

		equation.set(x, y, z, w);
	}
	
}
