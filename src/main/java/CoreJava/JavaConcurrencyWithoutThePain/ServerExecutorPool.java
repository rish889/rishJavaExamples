package CoreJava.JavaConcurrencyWithoutThePain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by rishabhg on 12/27/16.
 */

public class ServerExecutorPool {
    static Executor pool = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(9000);
        while (true) {
            final Socket s = socket.accept();
            pool.execute(() -> {
                System.out.println("========Thread Name : ========" + Thread.currentThread().getName());
                System.out.println("========Socket.toString : ========" + s.toString());
            });
        }
    }
}