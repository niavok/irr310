package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;
import com.irr310.common.tools.Vec3;

public class Vect3MessageFieldGenerator extends MessageFieldGenerator<Vec3> {

    public Vect3MessageFieldGenerator() {
    }

    @Override
    public int getSize(Vec3 value) {
        // 3 doubles = 3*8 = 24
        return 24;
    }

    @Override
    public int write(Vec3 value, byte[] buffer, int offset) {
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.x), buffer, offset + 0);
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.y), buffer, offset + 8);
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value.z), buffer, offset + 16);
        return 24;
    }

    @Override
    public Pair<Integer, Vec3> load(byte[] buffer, int offset) {
        Vec3 v = Vec3.origin();
        v.x = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 0));
        v.y = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 8));
        v.z = Double.longBitsToDouble(TypeConversion.longFromByteArray(buffer, offset + 16));

        return new ImmutablePair<Integer, Vec3>(24, v);
    }

}
