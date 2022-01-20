package filesharing;

import filesharing.Constraints.Commands;
import java.util.Scanner;

/**
 * args[0]      p2p
 * args[1]      -receive        -serve
 * args[2]      -path
 * args[3]      path_to_Directory
 * examples:
 * p2p -serve -path ./shared/ ( server side )
 * p2p -receive music.mp3 ( client side )
 */

class Main {
    public static void main(String[] args) {
        do {
            System.out.print(">");
            String[] commands = new Scanner(System.in).nextLine().split(" ");
            try {
                if (commands[0].equals(Commands.PtoP)) {
                    if (commands[1].equals(Commands.RECEIVE)) {
                        Receiver receiver = Receiver.getReceiver();
                        receiver.sendGetFileRequest(commands[2]);
                        receiver.startToListen();
                    } else if(commands[1].equals(Commands.SERVE)) {
                        Sender sender = new Sender(commands[3]);
                        sender.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (true);
    }
}