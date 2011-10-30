package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class StringMessageFieldGenerator extends MessageFieldGenerator<String> {

    public StringMessageFieldGenerator() {
    }

    @Override
    public int getSize(String value) {
        // 4 bytes used to indicate the size

        // If you have a NullPointerException on the next line, this could be
        // because you don't initialyse a message attribute
        return value.getBytes().length + 4;

    }

    @Override
    public int write(String value, byte[] buffer, int offset) {
        byte[] string = value.getBytes();
        TypeConversion.writeIntToByteArray(string.length, buffer, offset);
        System.arraycopy(string, 0, buffer, offset + 4, string.length);
        return string.length + 4;
    }

    @Override
    public Pair<Integer, String> load(byte[] buffer, int offset) {
        int length = TypeConversion.intFromByteArray(buffer, offset);
        String string = new String(buffer, offset + 4, length);
        
        return new ImmutablePair<Integer, String>(length + 4, string);
    }

}
