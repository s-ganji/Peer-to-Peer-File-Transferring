package filesharing;

import filesharing.Constraints.Configuration;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Helper {
    public static byte[] spliter(byte[] data, int offset, int length) {
        byte[] result = new byte[length];
        System.arraycopy(data, offset, result, 0, length);
        return result;
    }

    public static int getTrueNumber(int num) {
        if (num < 0) return num + 256;
        else return num;
    }

    static void writeArrayListOfArrayByteToFile(String path, HashMap<Integer, byte[]> array) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Configuration.RECEIVED_FILE_ADDRESS + path);
            for (Map.Entry m : array.entrySet())
                for (byte b : (byte[]) m.getValue())
                    fileOutputStream.write(b);
            fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
