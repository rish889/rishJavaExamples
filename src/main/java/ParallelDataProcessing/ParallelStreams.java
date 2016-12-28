package ParallelDataProcessing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.LongStream;

/**
 * Created by rishabhg on 12/27/16.
 */
public class ParallelStreams {

    //    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long iterativeSum() {
        long n = 10_000_000l;
        long result = 0l;
        for (long i = 1l; i <= n; i++) {
            result += i;
        }
        return result;
    }

    //@Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long sequentialSum() {
        return LongStream.rangeClosed(1L, 10_000_000L)
                .reduce(0l, Long::sum);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long parallelSum() {
        return LongStream.rangeClosed(1L, 10_000_000L)
                .parallel()
                .reduce(0l, Long::sum);
    }

    //@Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long sequentialNotLongStreamSum() {
        return LongStream.iterate(1L, i -> i + 1)
                .limit(10_000_000L)
                .reduce(0l, Long::sum);
    }

    //@Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static void parallelSumForkJoin() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(8);
        forkJoinPool.submit(() -> {
            return LongStream.rangeClosed(1L, 10_000_000L)
                    .parallel()
                    .reduce(0L, Long::sum);
        });
    }

    public static void main(String args[]) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "2");
        System.out.println("Common Thread Pool Size : " + System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism"));
    }
}
