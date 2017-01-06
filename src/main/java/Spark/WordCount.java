package Spark;

import org.apache.commons.io.FileUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

//        <dependency>
//        <groupId>org.apache.spark</groupId>
//        <artifactId>spark-core_2.10</artifactId>
//        <version>1.2.0</version>
//        </dependency>

public class WordCount {
    private static final FlatMapFunction<String, String> WORDS_EXTRACTOR =
            new FlatMapFunction<String, String>() {
                @Override
                public Iterable<String> call(String s) throws Exception {
                    return Arrays.asList(s.split(" "));
                }
            };

    private static final PairFunction<String, String, Integer> WORDS_MAPPER =
            new PairFunction<String, String, Integer>() {
                @Override
                public Tuple2<String, Integer> call(String s) throws Exception {
                    return new Tuple2<String, Integer>(s, 1);
                }
            };

    private static final Function2<Integer, Integer, Integer> WORDS_REDUCER =
            new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(Integer a, Integer b) throws Exception {
                    return a + b;
                }
            };

    public static void main(String[] args) throws IOException {
        SparkConf conf = new SparkConf().setAppName("org.sparkexample.WordCount").setMaster("local");
        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> file = context.textFile("/Volumes/dev/workspace/personal/rishJavaExamples/input/wordCount.txt");
        JavaRDD<String> words = file.flatMap(WORDS_EXTRACTOR);
        JavaPairRDD<String, Integer> pairs = words.mapToPair(WORDS_MAPPER);
        JavaPairRDD<String, Integer> counter = pairs.reduceByKey(WORDS_REDUCER);

        File outputFile = new File("/Volumes/dev/workspace/personal/rishJavaExamples/output/wordCount");
        FileUtils.deleteDirectory(outputFile);

        counter.saveAsTextFile(outputFile.getAbsolutePath());
    }
}
