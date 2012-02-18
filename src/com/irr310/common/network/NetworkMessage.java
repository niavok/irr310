package com.irr310.common.network;

import com.irr310.common.network.protocol.NetworkMessageType;
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
    
    public long getResponseIndex() {
        return responseIndex;
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

    public void load(byte[] dataBuffer) {
        // List fields to send (with @NetworkParam)
        MessageDataDescription description = MessageDataDescription.getMessageDataDescription((Class<NetworkMessage>) this.getClass());

        // Fields
        description.loadFields(this, dataBuffer, 0);
        
        
    }

    

    
}
