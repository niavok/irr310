package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.TypeConversion;

public class TransformMatrixMessageFieldGenerator extends MessageFieldGenerator<TransformMatrix> {

    public TransformMatrixMessageFieldGenerator() {
    }

    @Override
    public int getSize(TransformMatrix value) {
        // 16 double = 8*16 = 64
        return 128;
    }

    @Override
    public int write(TransformMatrix value, byte[] buffer, int offset) {
        double[] data = value.getData();
        for(int i = 0; i < 16; i++) {
            TypeConversion.writeLongToByteArray(Double.doubleToLongBits(data[i]), buffer, offset + i*8);
        }
        return 128;
    }

    @Override
    public Pair<Integer, TransformMatrix> load(byte[] buffer, int offset) {
        TransformMatrix t = new TransformMatrix();
        double[] data = new double[16];
        for(int i = 0; i < 16; i++) {
            data[i] = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + i*8));
        }
        t.set(data);
        
        return new ImmutablePair<Integer, TransformMatrix>(64, t);
    }

}
