package CoreJava.CompletableFutures.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class RishRestClient {

    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    public static void main(String args[]) {

        long start = System.nanoTime();

//        sychronousCalls();
//        futureCalls();
//        completatbleFutureCalls();
        parallelStreamsCall();

        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

    private static void sychronousCalls() {
        LongStream.rangeClosed(1, 32).
                mapToObj(RishRestClient::networkCall).
                forEach(System.out::println);
    }

    private static void futureCalls() {

        List<Future<String>> list = new ArrayList<>();

        LongStream.rangeClosed(1, 32).
                mapToObj(l -> executor.submit(() -> networkCall(l))).
                forEach(f -> {
                    try {
                        System.out.println(f.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });

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

}
