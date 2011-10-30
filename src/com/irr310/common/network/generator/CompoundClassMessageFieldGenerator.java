package com.irr310.common.network.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class CompoundClassMessageFieldGenerator<T> extends MessageFieldGenerator<T> {

    private final List<Pair<Field, MessageFieldGenerator<?>>> generators;
    private final Class<T> type;

    public CompoundClassMessageFieldGenerator(Class<T> type) {

        this.type = type;
        Field[] allFields = type.getFields();
        generators = new ArrayList<Pair<Field, MessageFieldGenerator<?>>>();
        for (Field field : allFields) {

            MessageFieldGenerator<?> generator = MessageFieldGenerator.getFromField(field);
            if (generator != null) {
                generators.add(new ImmutablePair<Field, MessageFieldGenerator<?>>(field, generator));
            }

        }

    }

    @Override
    public int getSize(T object) {
        int size = 0;

        for (Pair<Field, MessageFieldGenerator<?>> pair : generators) {
            try {
                size += pair.getRight().genericGetSize(pair.getLeft().get(object));
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network computeSize");
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network computeSize");
            }
        }

        return size;
    }

    @Override
    public int write(T object, byte[] buffer, int offset) {
        int relativeOffset = 0;

        for (Pair<Field, MessageFieldGenerator<?>> pair : generators) {
            try {
                relativeOffset += pair.getRight().genericWrite(pair.getLeft().get(object), buffer, offset + relativeOffset);
            } catch (IllegalArgumentException e) {
                System.err.println("IllegalArgumentException on network computeSize");
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccessException on network computeSize");
            }
        }
        return relativeOffset;
    }

    @Override
    public Pair<Integer, T> load(byte[] buffer, int offset) {
        int relativeOffset = 0;

        try {
            Constructor<T> constructor;

            constructor = type.getConstructor();

            T instance;

            instance = constructor.newInstance();

            for (Pair<Field, MessageFieldGenerator<?>> pair : generators) {

                Pair<Integer, ?> load = pair.getRight().load(buffer, relativeOffset + offset);
                relativeOffset += load.getLeft();

                try {
                    pair.getLeft().set(instance, load.getRight());
                } catch (IllegalArgumentException e) {
                    System.err.println("IllegalArgumentException on network package load");
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    System.err.println("IllegalAccessException on network package load");
                    e.printStackTrace();
                }
            }

            return new ImmutablePair<Integer, T>(relativeOffset, instance);

        } catch (SecurityException e1) {
            e1.printStackTrace();
        } catch (NoSuchMethodException e1) {
            System.err.println("No empty constructor");
            e1.printStackTrace();
        } catch (IllegalArgumentException e1) {
            e1.printStackTrace();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        }

        return null;
    }

}
