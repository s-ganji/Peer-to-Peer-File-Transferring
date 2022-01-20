package filesharing;

import filesharing.Constraints.ByteAddress;
import filesharing.Constraints.Configuration;
import filesharing.models.AckResponse;
import filesharing.models.DataResponse;
import filesharing.models.GetFileRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Sender {
    private HashMap<Integer, byte[]> arrayListOfBufferedFile = new HashMap<Integer, byte[]>();
    private DatagramSocket datagramSocket;
    private String PATH_OF_SHARED;
    private String[] list;

    Sender(String path) throws SocketException {
        PATH_OF_SHARED = path;
        startSocket();
        System.out.println("server started");
        File file = new File(PATH_OF_SHARED);
        list = file.list();
        assert list != null;
    }

    void start() throws IOException, InterruptedException {
        do {
            System.out.println("listening...");
            GetFileRequest getFileRequest = listenForRequest();
            if (Arrays.asList(list).contains(getFileRequest.getFileName())) {
                System.out.println("TRANSFER STARTED!");
                fileToBuffer(getFileRequest.getFileName());
                sendAckResponse(getFileRequest);
                SendFile(getFileRequest);
                System.out.println("TRANSFER DONE!");
            } else
                System.out.println("file not found! (" + getFileRequest.getFileName() + ")");
        } while (true);
    }

    private void startSocket() throws SocketException {
        datagramSocket = new DatagramSocket(Configuration.SENDER_PORT);
    }

    private GetFileRequest listenForRequest() throws IOException {
        byte[] receive = new byte[ByteAddress.PACKET_SIZE];
        DatagramPacket dpReceive = new DatagramPacket(receive, ByteAddress.PACKET_SIZE);
        datagramSocket.receive(dpReceive);
        return new GetFileRequest(receive, dpReceive.getAddress(), dpReceive.getPort());
    }

    private void sendAckResponse(GetFileRequest getFileRequest) throws IOException {
        AckResponse ackResponse = new AckResponse(arrayListOfBufferedFile.size());
        DatagramPacket datagramPacket = new DatagramPacket(ackResponse.getPacket(), 0, ackResponse.getPacket().length, getFileRequest.getInetAddress(), getFileRequest.getPort());
        datagramSocket.send(datagramPacket);
    }

    private void SendFile(GetFileRequest getFileRequest) throws IOException, InterruptedException {
        int counter = 0;
        for (Map.Entry m : arrayListOfBufferedFile.entrySet())
            sendPacket(counter++, (byte[]) m.getValue(), getFileRequest);
    }

    private void sendPacket(int counter, byte[] data, GetFileRequest getFileRequest) throws IOException, InterruptedException {
        DataResponse dataResponse = new DataResponse(counter, data);
        DatagramPacket datagramPacket = new DatagramPacket(dataResponse.getPacket(), 0, dataResponse.getPacket().length, getFileRequest.getInetAddress(), getFileRequest.getPort());
        datagramSocket.send(datagramPacket);
        Thread.sleep(1);
    }

    private void fileToBuffer(String filePath) throws IOException {
        arrayListOfBufferedFile = new HashMap<Integer, byte[]>();
        FileInputStream fileInput = new FileInputStream(PATH_OF_SHARED + filePath);
        int dataSize = ByteAddress.DATA_SIZE;
        byte[] data = new byte[dataSize];
        byte[] tempByte = new byte[1];
        int counter = 0;
        int keyMap = 0;
        while ((fileInput.read(tempByte)) != -1) {
            data[counter++] = tempByte[0];
            if (counter == dataSize) {
                counter = 0;
                arrayListOfBufferedFile.put(keyMap++, data);
                data = new byte[dataSize];
            }
        }
        if (counter > 0) {
            byte[] lastArray = new byte[counter];
            System.arraycopy(data, 0, lastArray, 0, counter);
            arrayListOfBufferedFile.put(keyMap, lastArray);
        }
    }
}
