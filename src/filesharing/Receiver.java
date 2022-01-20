package filesharing;

import filesharing.Constraints.ByteAddress;
import filesharing.Constraints.Configuration;
import filesharing.models.AckResponse;
import filesharing.models.DataResponse;
import filesharing.models.GetFileRequest;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

class Receiver {
    private HashMap<Integer, byte[]> receivedBytes = new HashMap<Integer, byte[]>();
    private DatagramSocket receiverSocket;
    private String fileName;
    private static Receiver receiverSingleton;
    private static Boolean flagSingleton = false;

    private Receiver() throws SocketException {
        receiverSocket = new DatagramSocket(Configuration.RECEIVER_PORT);
    }

    static Receiver getReceiver() throws SocketException {
        if (!flagSingleton) {
            receiverSingleton = new Receiver();
            flagSingleton = true;
        }
        return receiverSingleton;
    }

    void sendGetFileRequest(String fileName) throws IOException {
        this.fileName = fileName;
        GetFileRequest getFileRequest = new GetFileRequest(fileName);
        DatagramPacket datagramPacket = new DatagramPacket(getFileRequest.getPacket(), 0, getFileRequest.getPacket().length, InetAddress.getByName(Configuration.BROADCAST_ADDRESS), Configuration.SENDER_PORT);
        receiverSocket.send(datagramPacket);
    }

    void startToListen() throws IOException {
        System.out.println("client start to listening...");
        AckResponse ackResponse = new AckResponse(receive());
        int numberOfPackets = ackResponse.getNumberOfPackets();
        System.out.println("download started : number of packets " + numberOfPackets);
        DataResponse dataResponse;
        do {
            dataResponse = new DataResponse(receive());
            receivedBytes.putIfAbsent(dataResponse.getOffset(), dataResponse.getData());
            if ((numberOfPackets > 100 && dataResponse.getOffset() % (numberOfPackets / 100) == 0) || numberOfPackets < 100)
                System.out.print("|");
        } while (dataResponse.getOffset() != numberOfPackets);
        System.out.println("\nWriting file");
        Helper.writeArrayListOfArrayByteToFile(fileName, receivedBytes);
        System.out.println("file received (" + fileName + ")");
    }

    private byte[] receive() throws IOException {
        byte[] receive = new byte[ByteAddress.PACKET_SIZE];
        DatagramPacket DpReceive = new DatagramPacket(receive, ByteAddress.PACKET_SIZE);
        receiverSocket.receive(DpReceive);
        return receive;
    }
}
