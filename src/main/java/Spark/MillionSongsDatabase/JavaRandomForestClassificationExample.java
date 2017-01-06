package Spark.MillionSongsDatabase;

import java.util.HashMap;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import org.apache.spark.mllib.util.MLUtils;
import scala.Tuple2;


//                <dependency>
//                <groupId>org.apache.spark</groupId>
//                <artifactId>spark-core_2.11</artifactId>
//                <version>2.0.0</version>
//                </dependency>
//                <dependency>
//                <groupId>org.apache.spark</groupId>
//                <artifactId>spark-mllib_2.11</artifactId>
//                <version>2.0.0</version>
//                </dependency>

public class JavaRandomForestClassificationExample {
    //static SparkSession spark = UtilityForSparkSession.mySession();
    static JavaSparkContext spark = new JavaSparkContext(new SparkConf().setAppName("org.sparkexample.WordCount").setMaster("local"));


    public static void main(String[] args) {
        // Load and parse the data file.
        String datapath = "/Volumes/dev/workspace/personal/rishJavaExamples/input/MillionSongsDatabase/Letterdata_libsvm.data";
        JavaRDD<LabeledPoint> data = MLUtils.loadLibSVMFile(spark.sc(), datapath).toJavaRDD();

        JavaRDD<LabeledPoint>[] splits = data.randomSplit(new double[]{0.7, 0.3}, 12345);
        JavaRDD<LabeledPoint> trainingData = splits[0];
        JavaRDD<LabeledPoint> testData = splits[1];

        // Train a RandomForest model. Empty categoricalFeaturesInfo indicates all features are continuous.
        Integer numClasses = 26; // 26 alphabets means 26 classes
        HashMap<Integer, Integer> categoricalFeaturesInfo = new HashMap<>(); // Empty categoricalFeaturesInfo indicates all features are continuous.
        Integer numTrees = 10; // Deafult is 5 but it is better practice to have more trees. If >1 then it is considered as a forest.
        String featureSubsetStrategy = "auto"; // Let the algorithm choose feature subset strategy.
        String impurity = "gini"; // For information gain
        Integer maxDepth = 20; //Maximum depth of the tree
        Integer maxBins = 40; // Number of maximum beans to be used
        Integer seed = 12345; // Random seed

        final RandomForestModel model = RandomForest.trainClassifier(
                trainingData,
                numClasses,
                categoricalFeaturesInfo,
                numTrees,
                featureSubsetStrategy,
                impurity,
                maxDepth,
                maxBins,
                seed
        );

        // Evaluation-1: evaluate the model on test instances and compute test error
        JavaPairRDD<Double, Double> predictionAndLabel =
                testData.mapToPair(new PairFunction<LabeledPoint, Double, Double>() {
                    @Override
                    public Tuple2<Double, Double> call(LabeledPoint p) {
                        return new Tuple2<>(model.predict(p.features()), p.label());
                    }
                });

        Double testErr =
                1.0 * predictionAndLabel.filter(new Function<Tuple2<Double, Double>, Boolean>() {
                    @Override
                    public Boolean call(Tuple2<Double, Double> pl) {
                        return !pl._1().equals(pl._2());
                    }
                }).count() / testData.count();
        System.out.println("Test Error: " + testErr);
        System.out.println("Learned classification forest model:\n" + model.toDebugString());

        // Evaluation-2: evaluate the model on test instances and compute the related performance measure statistics
        JavaRDD<Tuple2<Object, Object>> predictionAndLabels = testData.map(
                new Function<LabeledPoint, Tuple2<Object, Object>>() {
                    public Tuple2<Object, Object> call(LabeledPoint p) {
                        Double prediction = model.predict(p.features());
                        return new Tuple2<Object, Object>(prediction, p.label());
                    }
                }
        );

        // Get evaluation metrics.
        MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
        System.out.println(metrics.confusionMatrix());
        System.out.println(metrics.confusionMatrix());
        double precision = metrics.precision(metrics.labels()[0]);
        double recall = metrics.recall(metrics.labels()[0]);
        double f_measure = metrics.fMeasure();
        double query_label = 8.0;
        double TP = metrics.truePositiveRate(query_label);
        double FP = metrics.falsePositiveRate(query_label);
        double WTP = metrics.weightedTruePositiveRate();
        double WFP =  metrics.weightedFalsePositiveRate();
        System.out.println("Precision = " + precision);
        System.out.println("Recall = " + recall);
        System.out.println("F-measure = " + f_measure);
        System.out.println("True Positive Rate = " + TP);
        System.out.println("False Positive Rate = " + FP);
        System.out.println("Weighted True Positive Rate = " + WTP);
        System.out.println("Weighted False Positive Rate = " + WFP);


        spark.stop();

    }
}