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

import java.nio.ByteBuffer;

import javax.vecmath.Tuple3d;

/**
 *
 * @author jezek2
 */
public class ByteBufferVertexData extends VertexData {

	public ByteBuffer vertexData;
	public int vertexCount;
	public int vertexStride;
	public ScalarType vertexType;

	public ByteBuffer indexData;
	public int indexCount;
	public int indexStride;
	public ScalarType indexType;

	@Override
	public int getVertexCount() {
		return vertexCount;
	}

	@Override
	public int getIndexCount() {
		return indexCount;
	}

	@Override
	public <T extends Tuple3d> T getVertex(int idx, T out) {
		int off = idx*vertexStride;
		out.x = vertexData.getDouble(off+4*0);
		out.y = vertexData.getDouble(off+4*1);
		out.z = vertexData.getDouble(off+4*2);
		return out;
	}

	@Override
	public void setVertex(int idx, double x, double y, double z) {
		int off = idx*vertexStride;
		vertexData.putDouble(off+4*0, x);
		vertexData.putDouble(off+4*1, y);
		vertexData.putDouble(off+4*2, z);
	}

	@Override
	public int getIndex(int idx) {
		if (indexType == ScalarType.SHORT) {
			return indexData.getShort(idx*indexStride) & 0xFFFF;
		}
		else if (indexType == ScalarType.INTEGER) {
			return indexData.getInt(idx*indexStride);
		}
		else {
			throw new IllegalStateException("indicies type must be short or integer");
		}
	}

}
