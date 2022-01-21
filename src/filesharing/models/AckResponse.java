package filesharing.models;

import filesharing.Constraints.ByteAddress;
import filesharing.Helper;

import java.nio.ByteBuffer;

public class AckResponse {
    private byte[] packet;

    public AckResponse(byte[] packet) {
        this.packet = packet;
    }

    public AckResponse(int numberOfPackets) {
        setNumberOfPackets(numberOfPackets);
    }

    public byte[] getPacket() {
        return packet;
    }

    public int getNumberOfPackets() {
        int dataSize = Helper.getTrueNumber((int) packet[0]);
        return ByteBuffer.wrap(Helper.spliter(packet, 1, dataSize)).getInt() - 1;
    }

    private void setNumberOfPackets(int sizeOfFile) {
        resetPacket();
        byte[] numberOfPacket_bytes = ByteBuffer.allocate(ByteAddress.DATA_SIZE).putInt(sizeOfFile).array();
        packet[0] = (byte) numberOfPacket_bytes.length;
        System.arraycopy(numberOfPacket_bytes, 0, packet, 1, ByteAddress.DATA_SIZE);
    }

    private void resetPacket() {
        this.packet = new byte[ByteAddress.PACKET_SIZE];
    }
}
