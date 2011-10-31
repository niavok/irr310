package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class DoubleMessageFieldGenerator extends MessageFieldGenerator<Double> {

    @Override
    public int getSize(Double value) {
        return 8;
    }

    @Override
    public int write(Double value, byte[] buffer, int offset) {
        
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value), buffer, offset);
        return 8;
    }

    @Override
    public Pair<Integer, Double> load(byte[] buffer, int offset) {
        long bits = TypeConversion.longFromByteArray(buffer, offset);
        return new ImmutablePair<Integer, Double>(8, Double.longBitsToDouble(bits));

    }

}
