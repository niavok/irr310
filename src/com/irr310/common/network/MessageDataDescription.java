package com.irr310.common.network;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.irr310.common.network.field.BooleanMessageFieldDescription;
import com.irr310.common.network.field.ListMessageFieldDescription;
import com.irr310.common.network.field.MessageFieldDescription;
import com.irr310.common.network.field.StringMessageFieldDescription;

class MessageDataDescription {

    private static Map<Class<NetworkMessage>, MessageDataDescription> fieldsCache = new HashMap<Class<NetworkMessage>, MessageDataDescription>();

    private final List<MessageFieldDescription> fields;

    public MessageDataDescription(Class<NetworkMessage> messageClass) {
        Field[] allFields = messageClass.getFields();
        fields = new ArrayList<MessageFieldDescription>();
        for (Field field : allFields) {
            
            if (field.getAnnotation(NetworkParam.class) != null) {
                
                if(field.getType().equals(String.class)) {
                    fields.add(new StringMessageFieldDescription(field));    
                }
                else if(field.getType().equals(boolean.class)) {
                    fields.add(new BooleanMessageFieldDescription(field));    
                }
                else if(field.getType().isAssignableFrom(List.class)) {
                    fields.add(new ListMessageFieldDescription(field));    
                }
                else {
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