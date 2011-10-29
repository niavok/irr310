package com.irr310.common.network.protocol;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.tools.TypeConversion;

public class NetworkMessage {

    public byte MAJOR_PROTOCOL_VERSION_KEY = 42; 
    
    private long responseIndex;

    public NetworkMessage() {
        responseIndex = 0;
    }

    public void setResponseIndex(long responseIndex) {
        this.responseIndex = responseIndex;
    }

    public byte[] getBytes() {

        // List fields to send (with @NetworkParam)
        MessageDataDescription description = MessageDataDescription.getMessageDataDescription((Class<NetworkMessage>) this.getClass());

        byte[] buffer = new byte[17 + description.computeSize(this)];
        int offset = 0; 
                
        // Major protocol version, 1 byte
        buffer[offset] = MAJOR_PROTOCOL_VERSION_KEY;
        offset++;

        // Type 4 bytes
        TypeConversion.writeIntToByteArray(0, buffer, offset+=1);
        offset+=4;
        
        // Response id 8 bytes
        TypeConversion.writeLongToByteArray(responseIndex, buffer, offset);
        offset+=8;
        
        // Length in bytes, 4 bytes
        int size = description.computeSize(this);
        TypeConversion.writeIntToByteArray(size, buffer, offset);
        offset+=4;
        
        // Fields
        description.writeFields(this, buffer, offset);
        
        return buffer;
    }
    
    

    private static class MessageDataDescription {

        private static Map<Class<NetworkMessage>, MessageDataDescription> fieldsCache = new HashMap<Class<NetworkMessage>, MessageDataDescription>();

        private final List<MessageFieldDescription> fields;

        public MessageDataDescription(Class<NetworkMessage> messageClass) {
            Field[] allFields = messageClass.getFields();
            fields = new ArrayList<MessageFieldDescription>();
            for (Field field : allFields) {
                
                if (field.getAnnotation(NetworkParam.class) != null) {
                    
                    if(field.getType().equals(String.class)) {
                        fields.add(new StringMessageFieldDescription(field));    
                    } else {
                        System.out.println("Field type not supported for network: "+ field.getDeclaringClass());
                    }
                }
            }

        }

        

        public int computeSize(NetworkMessage networkMessage) {
            int size = 0;
            
            for(MessageFieldDescription field: fields) {
                size += field.getSize(networkMessage);
            }
            
            return size;
        }

        public void writeFields(NetworkMessage networkMessage, byte[] buffer, int offset) {
            int localOffset = offset;
            
            for(MessageFieldDescription field: fields) {
                offset += field.write(networkMessage, buffer, localOffset);
            }
            
        }
        
        public static MessageDataDescription getMessageDataDescription(Class<NetworkMessage> messageClass) {
            if (fieldsCache.containsKey(messageClass)) {
                return fieldsCache.get(messageClass);
            } else {
                MessageDataDescription description = new MessageDataDescription(messageClass);
                fieldsCache.put((Class<NetworkMessage>) messageClass, description);
                return description;
            }
        }

    }
    
    private static abstract class MessageFieldDescription {
        protected final Field field;
        
        private MessageFieldDescription(Field field) {
            this.field = field;
        }

        public abstract int getSize(NetworkMessage networkMessage);
        
        public abstract int write(NetworkMessage networkMessage, byte[] buffer, int offset);
    }

    private static class StringMessageFieldDescription extends MessageFieldDescription {
        
        private StringMessageFieldDescription(Field field) {
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
                return string.length;
                
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network package creation");
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network package creation");
            }
            
            return 0;
            
        }
        
    }
    
}
