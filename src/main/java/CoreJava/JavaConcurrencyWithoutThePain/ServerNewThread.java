package CoreJava.JavaConcurrencyWithoutThePain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class ServerNewThread {

    private final static Logger LOGGER = Logger.getLogger(ServerNewThread.class.getName());

    public static void main(String[] args) throws IOException {

        LOGGER.addHandler(new StreamHandler(System.out, new SimpleFormatter()));

        ServerSocket socket = new ServerSocket(9000);

        while (true) {
            final Socket s = socket.accept();

            new Thread(() -> {
                doWork(s);
            }).start();
        }

    }

    static void doWork(Socket s) {
        LOGGER.info("========doWork Thread. Socket.toString : ========" + s.toString());
    }

}
