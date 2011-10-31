package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class OptionalMessageFieldGenerator extends MessageFieldGenerator<Object> {

    private final MessageFieldGenerator<?> parentGenerator;

    public OptionalMessageFieldGenerator(MessageFieldGenerator<?> parentGenerator) {
        this.parentGenerator = parentGenerator;
    }

    @Override
    public int getSize(Object value) {
        if (value == null) {
            return 1;
        }
        return 1 + parentGenerator.genericGetSize(value);
    }

    @Override
    public int write(Object value, byte[] buffer, int offset) {

        if (value == null) {
            buffer[offset] = 0;
            return 1;
        }
        buffer[offset] = 1;
        return 1 + parentGenerator.genericWrite(value, buffer, offset + 1);
    }

    @Override
    public Pair<Integer, Object> load(byte[] buffer, int offset) {

        if (buffer[offset] == 0) {
            return new ImmutablePair<Integer, Object>(1, null);
        } else {
            Pair<Integer, ?> tempLoad = parentGenerator.load(buffer, offset + 1);
            return new ImmutablePair<Integer, Object>(1 + tempLoad.getLeft(), tempLoad.getRight());
        }

    }

}
