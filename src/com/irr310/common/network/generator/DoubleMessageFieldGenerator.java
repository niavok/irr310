package com.irr310.common.network.generator;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.irr310.common.tools.TypeConversion;

public class DoubleMessageFieldGenerator extends MessageFieldGenerator<Double> {

    @Override
    public int getSize(Double value) {
        return 8;
    }

    @Override
    public int write(Double value, byte[] buffer, int offset) {
        
        TypeConversion.writeLongToByteArray(Double.doubleToLongBits(value), buffer, offset);
        return 8;
    }

    @Override
    public Pair<Integer, Double> load(byte[] buffer, int offset) {
        long bits = TypeConversion.longFromByteArray(buffer, offset);
        return new ImmutablePair<Integer, Double>(8, Double.longBitsToDouble(bits));

    }
    
    public static void main(String[] args) {
        DoubleMessageFieldGenerator generator = new DoubleMessageFieldGenerator();
        
        double d = 48;
        
        
        
        byte[] b = new byte[8];
        
        generator.write(d, b, 0);
        double d2 = generator.load(b, 0).getRight();
        
        long l = Double.doubleToLongBits(d);
        double d3 = Double.longBitsToDouble(l);
        
        System.out.println(d2);
        System.out.println(l);
        System.out.println(d3);
        
        LongMessageFieldGenerator lGenerator = new LongMessageFieldGenerator();
        
        byte[] b2 = new byte[8];
        
        long l2 = 48;
        lGenerator.write(l2, b2, 0);
        long l3 = lGenerator.load(b2, 0).getRight();
        
        
        lGenerator.write(l, b2, 0);
        long l4 = lGenerator.load(b2, 0).getRight();
        
        System.out.println(l3);
        System.out.println(l4);
    }

}
