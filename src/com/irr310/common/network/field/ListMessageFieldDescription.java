package com.irr310.common.network.field;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.tools.TypeConversion;

public class ListMessageFieldDescription extends MessageFieldDescription {
    
    public ListMessageFieldDescription(Field field) {
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
            Type genericType = field.getGenericType();
            
            List list = (List) field.get(networkMessage);
            if(list.size() == 0) {
                TypeConversion.writeIntToByteArray(0, buffer, offset);
                return 4;
            } 
            
            
            TypeConversion.writeIntToByteArray(list.size(), buffer, offset);
            int localOffset = offset+4;
            
            
            
            
            
            
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