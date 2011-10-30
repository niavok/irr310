package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class LongMessageFieldGenerator extends MessageFieldGenerator<Long> {

    @Override
    public int getSize(Long value) {
        return 8;
    }

    @Override
    public int write(Long value, byte[] buffer, int offset) {
        TypeConversion.writeLongToByteArray(value, buffer, offset);
        return 8;
    }

    @Override
    public Pair<Integer, Long> load(byte[] buffer, int offset) {
        long value = TypeConversion.longFromByteArray(buffer, offset);
        return new ImmutablePair<Integer, Long>(8, value);

    }

}
