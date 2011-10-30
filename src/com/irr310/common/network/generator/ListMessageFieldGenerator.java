package com.irr310.common.network.generator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class ListMessageFieldGenerator<T> extends MessageFieldGenerator<List<T>> {

    MessageFieldGenerator<T> generator;

    public ListMessageFieldGenerator(Class<T> type) {

        generator = MessageFieldGenerator.getFromType(type);

    }

  
    @Override
    public int getSize(List<T> list) {
        // Size of the size + the size of each element

        int size = 4;

        for (T value : list) {
            size += generator.getSize(value);
        }

        return size;
    }

    @Override
    public int write(List<T> list, byte[] buffer, int offset) {
        int relativeOffset = 0;
        TypeConversion.writeIntToByteArray(list.size(), buffer, offset + relativeOffset);
        relativeOffset += 4;

        for (T value : list) {
            relativeOffset += generator.write(value, buffer, offset + relativeOffset);
        }

        return relativeOffset;
    }

    
    @Override
    public Pair<Integer, List<T>> load(byte[] buffer, int offset) {
        int relativeOffset = 0;
        
        int length = TypeConversion.intFromByteArray(buffer, offset + relativeOffset);
        
        relativeOffset += 4;
        
        List<T> outputList = new ArrayList<T>(length);
        
        for(int i = 0; i < length; i++) {
            Pair<Integer,T> load = generator.load(buffer, offset+relativeOffset);
            outputList.add(load.getRight());
            relativeOffset += load.getLeft();
        }
        
        return new ImmutablePair<Integer, List<T>>(relativeOffset, outputList);
    }

    
}
