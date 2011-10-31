package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TypeConversion;
import com.irr310.common.tools.Vect3;

public class TransformMatrixMessageFieldGenerator extends MessageFieldGenerator<TransformMatrix> {

    public TransformMatrixMessageFieldGenerator() {
    }

    @Override
    public int getSize(TransformMatrix value) {
        // 16 float = 4*16 = 64
        return 64;
    }

    @Override
    public int write(TransformMatrix value, byte[] buffer, int offset) {
        float[] data = value.getData();
        for(int i = 0; i < 16; i++) {
            TypeConversion.writeIntToByteArray(Float.floatToIntBits(data[i]), buffer, offset + i*4);
        }
        return 64;
    }

    @Override
    public Pair<Integer, TransformMatrix> load(byte[] buffer, int offset) {
        TransformMatrix t = new TransformMatrix();
        float[] data = new float[16];
        for(int i = 0; i < 16; i++) {
            data[i] = Float.intBitsToFloat(TypeConversion.intFromByteArray(buffer, offset + i*4));
        }
        t.set(data);
        
        return new ImmutablePair<Integer, TransformMatrix>(64, t);
    }

}
