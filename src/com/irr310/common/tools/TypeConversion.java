package com.irr310.common.tools;

import java.nio.ByteBuffer;

public class TypeConversion {

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        ByteBuffer.wrap(b, 0, 4).putInt(value);
        return b;
    }

    public static byte[] writeIntToByteArray(int value, byte[] b, int offset) {
        ByteBuffer.wrap(b, offset, 4).putInt(value);
        return b;
    }

    public static byte[] writeLongToByteArray(long value, byte[] b, int offset) {
        ByteBuffer.wrap(b, offset, 8).putLong(value);
        return b;
    }

    public static int intFromByteArray(byte[] b, int offset) {
        return ByteBuffer.wrap(b, offset, 4).getInt();

    }

    public static long longFromByteArray(byte[] b, int offset) {
        return ByteBuffer.wrap(b, offset, 8).getLong();
    }
}
