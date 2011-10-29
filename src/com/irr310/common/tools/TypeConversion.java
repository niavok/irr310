package com.irr310.common.tools;

public class TypeConversion {

    public static byte[] intToByteArray(int value) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (4 - 1 - i) * 8;
            b[i] = (byte) ((value >>> offset) & 0xFF);
        }
        return b;
    }
    
    
    public static byte[] writeIntToByteArray(int value, byte[] b, int offset) {
        for (int i = offset; i < 4+offset; i++) {
            int o = (4 - 1 - i) * 8;
            b[i] = (byte) ((value >>> o) & 0xFF);
        }
        return b;
    }
    
    public static byte[] writeLongToByteArray(long value, byte[] b, int offset) {
        for (int i = offset; i < 8+offset; i++) {
            int o = (8 - 1 - i) * 8;
            b[i] = (byte) ((value >>> o) & 0xFF);
        }
        System.err.println("WARNING: writeLongToByteArray - verify the result !");
        return b;
    }
    
}
