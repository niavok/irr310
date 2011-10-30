package com.irr310.common.network;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.network.generator.MessageFieldGenerator;

class MessageDataDescription {

    private static Map<Class<NetworkMessage>, MessageDataDescription> fieldsCache = new HashMap<Class<NetworkMessage>, MessageDataDescription>();
    
    private final List<Pair<Field, MessageFieldGenerator<?>>> generators;

    public MessageDataDescription(Class<NetworkMessage> messageClass) {
        Field[] allFields = messageClass.getFields();
        generators = new ArrayList<Pair<Field, MessageFieldGenerator<?>>>();
        for (Field field : allFields) {
            
            MessageFieldGenerator<?> generator = MessageFieldGenerator.getFromField(field);
            if(generator != null) {
                generators.add(new ImmutablePair<Field, MessageFieldGenerator<?>>(field, generator));
            }
            
            /*
            if (field.getAnnotation(NetworkParam.class) != null) {
                
                Class<?> type = field.getType();
                
                if(type.equals(String.class)) {
                    fields.add(new StringMessageFieldDescription(field));    
                }
                else if(type.equals(boolean.class)) {
                    fields.add(new BooleanMessageFieldDescription(field));    
                }
                else {
                    System.out.println("Field type not supported for network: "+ field.getDeclaringClass());
                }
            } else if (field.getAnnotation(NetworkListParam.class) != null) {
                
                NetworkListParam annotation = field.getAnnotation(NetworkListParam.class);
                Class<?> value = annotation.value();
                fields.add(new ListMessageFieldDescription(field, value));    
                
            }*/
        }

    }
    
    

    

    public int computeSize(NetworkMessage networkMessage) {
        int size = 0;
        
        
        
        for(Pair<Field, MessageFieldGenerator<?>> pair: generators) {
            try {
                size += pair.getRight().genericGetSize(pair.getLeft().get(networkMessage));
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network computeSize");
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network computeSize");
            }
        }
        
        return size;
    }

    public void writeFields(NetworkMessage networkMessage, byte[] buffer, int offset) {
        int localOffset = offset;
        
        for(Pair<Field, MessageFieldGenerator<?>> pair: generators) {
            try {
                localOffset += pair.getRight().genericWrite(pair.getLeft().get(networkMessage), buffer, localOffset );
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network computeSize");
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network computeSize");
            }
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
        
        for(Pair<Field, MessageFieldGenerator<?>> pair: generators) {
            
            Pair<Integer,?> load = pair.getRight().load(buffer, localOffset );
            
            localOffset += load.getLeft();
            try {
            pair.getLeft().set(networkMessage, load.getRight());
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network package load");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network package load");
                e.printStackTrace();
            } 
        }
        
        
        
    }

}