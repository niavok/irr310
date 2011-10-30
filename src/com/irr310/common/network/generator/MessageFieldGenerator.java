package com.irr310.common.network.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkField;

public abstract class MessageFieldGenerator<T> {

    public abstract int getSize(T value);

    public abstract int write(T value, byte[] buffer, int offset);

    public abstract Pair<Integer, T> load(byte[] buffer, int offset);

    public int genericWrite(Object object, byte[] buffer, int offset) {
        return write((T) object, buffer, offset);
    }

    public int genericGetSize(Object object) {
        return getSize((T) object);
    }

    public static <V> MessageFieldGenerator<V> getFromType(Class<V> type) {

        if (type.equals(String.class)) {
            return (MessageFieldGenerator<V>) new StringMessageFieldGenerator();
        } else if (type.equals(boolean.class)) {
            return (MessageFieldGenerator<V>) new BooleanMessageFieldGenerator();
        } else if (type.equals(int.class)) {
            return (MessageFieldGenerator<V>) new IntegerMessageFieldGenerator();
        } else if (type.equals(long.class)) {
            return (MessageFieldGenerator<V>) new LongMessageFieldGenerator();
        } else {
            // Look for NetworkClass annotation

            if (type.getAnnotation(NetworkClass.class) != null) {
                // Cool, the class is serializable
                return new CompoundClassMessageFieldGenerator(type);
            } else {
                System.out.println("Field type not supported for network: " + type);
                return null;
            }

        }

        /*
         * } else if (field.getAnnotation(NetworkListParam.class) != null) {
         * NetworkListParam annotation =
         * field.getAnnotation(NetworkListParam.class); Class<?> value =
         * annotation.value(); fields.add(new ListMessageFieldDescription(field,
         * value)); return null;
         */
    }

    public static MessageFieldGenerator<?> getFromField(Field field) {

        if (field.getAnnotation(NetworkField.class) != null) {
            return getFromType(field.getType());
        }

        if (field.getAnnotation(NetworkListField.class) != null) {

            NetworkListField annotation = field.getAnnotation(NetworkListField.class);

            Class<?> type = annotation.value();

            return new ListMessageFieldGenerator(type);
        }

        return null;
        /*
         * if (field.getAnnotation(NetworkParam.class) != null) { Class<?> type
         * = field.getType(); if(type.equals(String.class)) { fields.add(new
         * StringMessageFieldDescription(field)); } else
         * if(type.equals(boolean.class)) { fields.add(new
         * BooleanMessageFieldDescription(field)); } else {
         * System.out.println("Field type not supported for network: "+
         * field.getDeclaringClass()); } } else if
         * (field.getAnnotation(NetworkListParam.class) != null) {
         * NetworkListParam annotation =
         * field.getAnnotation(NetworkListParam.class); Class<?> value =
         * annotation.value(); fields.add(new ListMessageFieldDescription(field,
         * value)); return null;
         */

    }

}
