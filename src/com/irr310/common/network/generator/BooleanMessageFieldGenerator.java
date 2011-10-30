package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class BooleanMessageFieldGenerator extends MessageFieldGenerator<Boolean> {

    @Override
    public int getSize(Boolean value) {
        return 1;
    }

    @Override
    public int write(Boolean value, byte[] buffer, int offset) {

        if (value) {
            buffer[offset] = 1;
        } else {
            buffer[offset] = 0;
        }

        return 1;
    }

    @Override
    public Pair<Integer, Boolean> load(byte[] buffer, int offset) {
        if (buffer[offset] == 0) {
            return new ImmutablePair<Integer, Boolean>(1, false);
        } else if (buffer[offset] == 1) {
            return new ImmutablePair<Integer, Boolean>(1, true);
        } else {
            System.err.println("Invalid value " + buffer[offset] + " for boolean");
            return null;
        }

    }

}
