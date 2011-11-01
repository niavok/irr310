package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class IntegerMessageFieldGenerator extends MessageFieldGenerator<Integer> {

    @Override
    public int getSize(Integer value) {
        return 4;
    }

    @Override
    public int write(Integer value, byte[] buffer, int offset) {
        TypeConversion.writeIntToByteArray(value, buffer, offset);
        return 4;
    }

    @Override
    public Pair<Integer, Integer> load(byte[] buffer, int offset) {
        int value = TypeConversion.intFromByteArray(buffer, offset);
        return new ImmutablePair<Integer, Integer>(4, value);
    }

}
