package com.irr310.common.network.field;

import java.lang.reflect.Field;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.tools.TypeConversion;

public class StringMessageFieldDescription extends MessageFieldDescription {
    
    public StringMessageFieldDescription(Field field) {
        super(field);
    }

    @Override
    public int getSize(NetworkMessage networkMessage) {
        try {
            String string = (String) field.get(networkMessage);
            // 4 bytes used to indicate the size
            
            //If you have a NullPointerException on the next line, this could be because you don't initialyse a message attribute
            return string.getBytes().length+4;
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException on network package creation");
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException on network package creation");
        }
        return 0;
        
    }
    @Override
    public int write(NetworkMessage networkMessage, byte[] buffer, int offset) {
        try {
            byte[] string = ((String) field.get(networkMessage)).getBytes();
            TypeConversion.writeIntToByteArray(string.length, buffer, offset);
            System.arraycopy(string, 0, buffer, offset+4, string.length);
            return string.length+4;
            
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException on network package creation");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException on network package creation");
            e.printStackTrace();
        }
        
        return 0;
        
    }

    @Override
    public int load(NetworkMessage networkMessage, byte[] buffer, int offset) {
        int length = TypeConversion.intFromByteArray(buffer, offset);
        String string = new String(buffer, offset+4, length);
        
        try {
            field.set(networkMessage, string);
        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException on network package load");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException on network package load");
            e.printStackTrace();
        }
        
        return length+4;
    }
    
}