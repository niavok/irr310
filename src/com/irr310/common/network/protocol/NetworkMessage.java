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

    public static final int HEADER_SIZE = 17;

    public byte MAJOR_PROTOCOL_VERSION_KEY = 42; 
    
    private long responseIndex;
    private final NetworkMessageType type; 
    
    
    public NetworkMessage(NetworkMessageType type) {
        this.type = type;
        responseIndex = 0;
    }

    public void setResponseIndex(long responseIndex) {
        this.responseIndex = responseIndex;
    }
    
    public NetworkMessageType getType() {
        return type;
    }
    

    public byte[] getBytes() {

        // List fields to send (with @NetworkParam)
        MessageDataDescription description = MessageDataDescription.getMessageDataDescription((Class<NetworkMessage>) this.getClass());

        int dataSize = description.computeSize(this);
        byte[] buffer = new byte[HEADER_SIZE + dataSize];
        int offset = 0; 
                
        // Major protocol version, 1 byte
        buffer[offset] = MAJOR_PROTOCOL_VERSION_KEY;
        offset++;

        // Type 4 bytes
        TypeConversion.writeIntToByteArray(type.ordinal(), buffer, offset);
        offset+=4;
        
        // Response id 8 bytes
        TypeConversion.writeLongToByteArray(responseIndex, buffer, offset);
        offset+=8;
        
        // Length in bytes, 4 bytes
        TypeConversion.writeIntToByteArray(dataSize, buffer, offset);
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
                localOffset += field.write(networkMessage, buffer, localOffset);
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



        public void loadFields(NetworkMessage networkMessage, byte[] buffer, int offset) {
            int localOffset = offset;
            
            for(MessageFieldDescription field: fields) {
                localOffset += field.load(networkMessage, buffer, localOffset);
            }
        }

    }
    
    private static abstract class MessageFieldDescription {
        protected final Field field;
        
        private MessageFieldDescription(Field field) {
            this.field = field;
        }

        public abstract int load(NetworkMessage networkMessage, byte[] buffer, int localOffset);

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

    public void load(byte[] dataBuffer) {
        // List fields to send (with @NetworkParam)
        MessageDataDescription description = MessageDataDescription.getMessageDataDescription((Class<NetworkMessage>) this.getClass());

        // Fields
        description.loadFields(this, dataBuffer, 0);
        
        
    }

    
}
