package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;
import com.irr310.common.tools.Vect3;

public class Vect3MessageFieldGenerator extends MessageFieldGenerator<Vect3> {

    public Vect3MessageFieldGenerator() {
    }

    @Override
    public int getSize(Vect3 value) {
        // 3 doubles = 3*8 = 24
        return 24;
    }

    @Override
    public int write(Vect3 value, byte[] buffer, int offset) {
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.x), buffer, offset + 0);
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.y), buffer, offset + 8);
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.z), buffer, offset + 16);
        return 24;
    }

    @Override
    public Pair<Integer, Vect3> load(byte[] buffer, int offset) {
        Vect3 v = Vect3.origin();
        v.x = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 0));
        v.y = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 8));
        v.z = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 16));

        return new ImmutablePair<Integer, Vect3>(24, v);
    }

}
