package com.irr310.common.network;

import com.irr310.common.event.CelestialObjectRemovedEvent;
import com.irr310.common.network.protocol.CameraViewObjectListRequestMessage;
import com.irr310.common.network.protocol.CapacityUpdateMessage;
import com.irr310.common.network.protocol.CelestialObjectRemovedNotificationMessage;
import com.irr310.common.network.protocol.DamageNotificationMessage;
import com.irr310.common.network.protocol.HelloMessage;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.NetworkMessageType;
import com.irr310.common.network.protocol.PartStateUpdateListMessage;
import com.irr310.common.network.protocol.ShipListMessage;
import com.irr310.common.network.protocol.ShipListRequestMessage;
import com.irr310.common.network.protocol.SignupRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;
import com.irr310.common.network.protocol.WorldObjectListMessage;
import com.irr310.common.tools.TypeConversion;

public abstract class MessageParser {

    private byte[] headerBuffer;
    private byte[] dataBuffer;
    private int dataSize;
    private int dataBufferOffset;
    private int headerBufferOffset;
    private int messageTypeId;
    private long messageResponseId;

    public MessageParser() {
        headerBuffer = new byte[NetworkMessage.HEADER_SIZE];
        headerBufferOffset = 0;
        dataBuffer = null;
        dataSize = 0;
        dataBufferOffset = 0;
        messageTypeId = 0;
    }

    public void parseData(byte[] data) {
        parseData(data, 0);
    }

    public void parseData(byte[] data, int offset) {

        if (headerBufferOffset < NetworkMessage.HEADER_SIZE) {
            if (data.length == offset) {
                // No more data to parse
                return;
            }

            // No complete header

            int missingInHeader = NetworkMessage.HEADER_SIZE - headerBufferOffset;
            int availableInBuffer = data.length - offset;

            int sizeToConsume = Math.min(missingInHeader, availableInBuffer);

            System.arraycopy(data, offset, headerBuffer, headerBufferOffset, sizeToConsume);
            headerBufferOffset += sizeToConsume;

            parseData(data, offset + sizeToConsume);
        } else {
            if (dataBuffer == null) {
                // New data buffer
                dataSize = TypeConversion.intFromByteArray(headerBuffer, 13);
                messageTypeId = TypeConversion.intFromByteArray(headerBuffer, 1);
                messageResponseId = TypeConversion.longFromByteArray(headerBuffer, 5);
                dataBuffer = new byte[dataSize];
                dataBufferOffset = 0;
            }

            int missingInData = dataSize - dataBufferOffset;

            if (data.length == offset && missingInData > 0) {
                // No more data to parse
                return;
            }

            int availableInBuffer = data.length - offset;

            int sizeToConsume = Math.min(missingInData, availableInBuffer);

            System.arraycopy(data, offset, dataBuffer, dataBufferOffset, sizeToConsume);
            dataBufferOffset += sizeToConsume;

            if (dataSize - dataBufferOffset == 0) {
                // The message is complete. Parse it.
                parseMessage();

                // Clean all buffers
                headerBufferOffset = 0;
                dataBufferOffset = 0;
                dataBuffer = null;
            }

            parseData(data, offset + sizeToConsume);

        }

    }

    private void parseMessage() {

        NetworkMessageType messageType = NetworkMessageType.values()[messageTypeId];

        NetworkMessage message = null;

        switch (messageType) {
            case HELLO:
                message = new HelloMessage();
                break;
            case LOGIN_REQUEST:
                message = new LoginRequestMessage();
                break;
            case LOGIN_RESPONSE:
                message = new LoginResponseMessage();
                break;
            case SIGNUP_REQUEST:
                message = new SignupRequestMessage();
                break;
            case SIGNUP_RESPONSE:
                message = new SignupResponseMessage();
                break;
            case SHIP_LIST:
                message = new ShipListMessage();
                break;
            case SHIP_LIST_REQUEST:
                message = new ShipListRequestMessage();
                break;
            case CAPACITY_UPDATE:
                message = new CapacityUpdateMessage();
                break;
            case PART_STATE_UPDATE_LIST:
                message = new PartStateUpdateListMessage();
                break;
            case CAMERA_VIEW_OBJECT_LIST_REQUEST:
                message = new CameraViewObjectListRequestMessage();
                break;
            case WORLD_OBJECT_LIST:
                message = new WorldObjectListMessage();
                break;
            case DAMAGE_NOTIFICATION:
                message = new DamageNotificationMessage();
                break;
            case CELESTIAL_OBJECT_REMOVED_NOTIFICATION:
                message = new CelestialObjectRemovedNotificationMessage();
                break;
            default:
                System.err.println("Not implemented message type: " + messageType.toString());
        }

        if (message != null) {
            message.setResponseIndex(messageResponseId);
            message.load(dataBuffer);

            processMessage(message);

        }

    }

    public abstract void processMessage(NetworkMessage message);

}
