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
        for (int i = 0; i < 4; i++) {
            int o = (4 - 1 - i) * 8;
            b[i+offset] = (byte) ((value >>> o) & 0xFF);
        }
        return b;
    }
    
    public static byte[] writeLongToByteArray(long value, byte[] b, int offset) {
        for (int i = 0; i < 8; i++) {
            int o = (8 - 1 - i) * 8;
            b[i+offset] = (byte) ((value >>> o) & 0xFF);
        }
        return b;
    }


    public static int intFromByteArray(byte[] b, int offset) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
   
    public static long longFromByteArray(byte[] b, int offset) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            long shift = (8 - 1 - i) * 8;
            value += (b[i + offset] & 0x000000FF) << shift;
        }
        return value;
    }
}
