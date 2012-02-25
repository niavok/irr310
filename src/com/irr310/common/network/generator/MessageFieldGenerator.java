package com.irr310.common.network.generator;

import java.lang.reflect.Field;

import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkListField;
import com.irr310.common.network.NetworkOptionalField;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;

@SuppressWarnings("unchecked")
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

    
    @SuppressWarnings("rawtypes")
    public static <V> MessageFieldGenerator<V> getFromType(Class<V> type) {

        if (type.equals(String.class)) {
            return (MessageFieldGenerator<V>) new StringMessageFieldGenerator();
        } else if (type.equals(boolean.class)) {
            return (MessageFieldGenerator<V>) new BooleanMessageFieldGenerator();
        } else if (type.equals(int.class)) {
            return (MessageFieldGenerator<V>) new IntegerMessageFieldGenerator();
        } else if (type.equals(long.class)) {
            return (MessageFieldGenerator<V>) new LongMessageFieldGenerator();
        } else if (type.equals(double.class)) {
            return (MessageFieldGenerator<V>) new DoubleMessageFieldGenerator();
        } else if (type.equals(Double.class)) {
            return (MessageFieldGenerator<V>) new DoubleMessageFieldGenerator();
        } else if (type.equals(Integer.class)) {
            return (MessageFieldGenerator<V>) new IntegerMessageFieldGenerator();
        } else if (type.equals(Boolean.class)) {
            return (MessageFieldGenerator<V>) new BooleanMessageFieldGenerator();
        } else if (type.equals(Vec3.class)) {
            return (MessageFieldGenerator<V>) new Vect3MessageFieldGenerator();
        } else if (type.equals(TransformMatrix.class)) {
            return (MessageFieldGenerator<V>) new TransformMatrixMessageFieldGenerator();
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
    }

    @SuppressWarnings("rawtypes")
    public static MessageFieldGenerator<?> getFromField(Field field) {

        if (field.getAnnotation(NetworkField.class) != null) {
            return getFromType(field.getType());
        }

        if (field.getAnnotation(NetworkListField.class) != null) {

            NetworkListField annotation = field.getAnnotation(NetworkListField.class);

            Class<?> type = annotation.value();

            return new ListMessageFieldGenerator(type);
        }

        if (field.getAnnotation(NetworkOptionalField.class) != null) {
            return new OptionalMessageFieldGenerator(getFromType(field.getType()));
        }

        return null;
    }

}
