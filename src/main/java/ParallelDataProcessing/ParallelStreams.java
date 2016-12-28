package ParallelDataProcessing;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Warmup;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Created by rishabhg on 12/27/16.
 */
public class ParallelStreams {
//    @Benchmark
//    @Fork(value = 1)
//    @Warmup(iterations = 10)
//    public static long iterativeSum() {
//        long n = 10_000_000l;
//        long result = 0l;
//        for (long i = 1l; i <= n; i++) {
//            result += i;
//        }
//        return result;
//    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long sequentialSum() {
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
        return LongStream.range(1L, 10_000_000L)
                .reduce(0l,Long::sum);
    }

    @Benchmark
    @Fork(value = 1)
    @Warmup(iterations = 5)
    public static long parallelSum() {
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "8");
        return LongStream.range(1L, 10_000_000L)
                .parallel()
                .reduce(0l,Long::sum);
    }

//    @Benchmark
//    @Fork(value = 1)
//    @Warmup(iterations = 10)
//    public static long sequentialNotLongStreamSum() {
//        return LongStream.iterate(1L, i->i+1)
//                .limit(10_000_000L)
//                .reduce(0l,Long::sum);
//    }
}
