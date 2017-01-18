package CoreJava.CompletableFutures.RestClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class RishRestClient {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String args[]) {

        long start = System.nanoTime();

//        newRunnableCalls();
        newCallableCalls();
//        sychronousCalls();
//        futureCalls();
//        completatbleFutureCalls();
//        parallelStreamsCall();

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    private static void sychronousCalls() {
        LongStream.rangeClosed(1, 32).
                mapToObj(RishRestClient::networkCall).
                forEach(System.out::println);
    }

    private static void newRunnableCalls() {
        LongStream.rangeClosed(1, 32)
                .mapToObj(l -> new Thread(() -> System.out.println(networkCall(l))))
                .forEach(l -> l.start());
    }

    private static void newCallableCalls() {
        LongStream.rangeClosed(1, 32)
                .mapToObj(l -> executor.submit(() -> networkCall(l)))
                .collect(Collectors.toList())
                .stream()
                .forEach(l -> {
                    try {
                        System.out.println(l.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        executor.shutdown();
    }

    private static void completatbleFutureCalls() {
        LongStream.rangeClosed(1, 32)
                .mapToObj(l -> CompletableFuture.supplyAsync(() -> networkCall(l), executor))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .forEach(System.out::println);
    }

    private static void parallelStreamsCall() {
        LongStream.rangeClosed(1, 32)
                .parallel()
                .mapToObj(l -> networkCall(l))
                .forEach(System.out::println);
    }

    private static String networkCall(long l) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Iteration : " + l;
    }

    private static String realNetworkCall(long l) {
        StringBuffer response = new StringBuffer();
        try {

            URL url = new URL("http://localhost:8080/getString/");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response + " : " + l;
    }

}
