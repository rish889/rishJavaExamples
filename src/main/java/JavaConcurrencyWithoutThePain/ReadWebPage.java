package JavaConcurrencyWithoutThePain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


//mvn clean package
//java -cp target/rish-java-examples-1.0-SNAPSHOT.jar JavaConcurrencyWithoutThePain.ReadWebPage http://www.google.com


public class ReadWebPage {
    public static void main(final String[] args) {

        if (args.length != 1) {
            System.err.println("usage: java ReadWebPage url");
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<List<String>> future = executor.submit(() -> {
            List<String> lines = new ArrayList<>();
            URL url = new URL(args[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStreamReader isr = new InputStreamReader(con.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
                lines.add(line);
            return lines;
        });


        try {
            future.get(5, TimeUnit.SECONDS)
                    .stream()
                    .forEach(System.out::println);
        } catch (ExecutionException ee) {
            System.err.println("Callable through exception: " + ee.getMessage());
        } catch (InterruptedException | TimeoutException eite) {
            System.err.println("URL not responding");
        }
        executor.shutdown();
    }
}