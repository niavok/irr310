package com.irr310.common.network.field;

import java.lang.reflect.Field;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.tools.TypeConversion;

public class BooleanMessageFieldDescription extends MessageFieldDescription {

    public BooleanMessageFieldDescription(Field field) {
        super(field);
    }

    @Override
    public int getSize(NetworkMessage networkMessage) {
        return 1;

    }

    @Override
    public int write(NetworkMessage networkMessage, byte[] buffer, int offset) {
        try {
            boolean value = (Boolean) field.get(networkMessage);

            if (value) {
                buffer[offset] = 1;
            } else {
                buffer[offset] = 0;
            }

            return 1;

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

        try {
            if (buffer[offset] == 0) {
                field.set(networkMessage, false);
            } else if (buffer[offset] == 1) {
                field.set(networkMessage, true);
            } else {
                System.err.println("Invalid value " + buffer[offset] + " for boolean");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("IllegalArgumentException on network package load");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.err.println("IllegalAccessException on network package load");
            e.printStackTrace();
        }

        return 1;
    }

}
