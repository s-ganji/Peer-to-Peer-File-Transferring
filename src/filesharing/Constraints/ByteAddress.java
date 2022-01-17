package filesharing.Constraints;

public class ByteAddress {
    public static final int PACKET_SIZE = 2000;
    public static int NUMBER_OF_OFFSET = 32;
    public static int NUMBER_OF_DATA_SIZE = 16;
    public static int DATA_START_ADDRESS = NUMBER_OF_DATA_SIZE + NUMBER_OF_OFFSET + 1;
    public static int DATA_SIZE = PACKET_SIZE - DATA_START_ADDRESS;

}
