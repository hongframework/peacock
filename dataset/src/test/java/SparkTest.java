import com.google.common.collect.Lists;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.Array;
import scala.Array$;
import scala.Tuple2;
import scala.collection.JavaConversions;
import scala.collection.Seq$;
import scala.collection.immutable.List$;
import scala.reflect.ClassTag$;
import scala.reflect.ClassTag$class;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class SparkTest {

    private transient JavaSparkContext sc;

    @Before
    public void setUp() {
        sc = new JavaSparkContext("local", "JavaAPISuite");
    }

    @After
    public void tearDown() {
        sc.stop();
        sc = null;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void reduceByKey() {
        List<Tuple2<Integer, Integer>> pairs = Arrays.asList(
                new Tuple2<>(2, 1),
                new Tuple2<>(2, 1),
                new Tuple2<>(1, 1),
                new Tuple2<>(3, 2),
                new Tuple2<>(3, 1)
        );
//        JavaPairRDD<Integer, Integer> rdd = sc.parallelizePairs(pairs);
//        JavaPairRDD<Integer, Integer> counts = rdd.reduceByKey((a, b) -> a + b);
//        assertEquals(1, counts.lookup(1).get(0).intValue());
//        assertEquals(2, counts.lookup(2).get(0).intValue());
//        assertEquals(3, counts.lookup(3).get(0).intValue());
//
//        Map<Integer, Integer> localCounts = counts.collectAsMap();
//        assertEquals(1, localCounts.get(1).intValue());
//        assertEquals(2, localCounts.get(2).intValue());
//        assertEquals(3, localCounts.get(3).intValue());
//
//        localCounts = rdd.reduceByKeyLocally((a, b) -> a + b);
//        assertEquals(1, localCounts.get(1).intValue());
//        assertEquals(2, localCounts.get(2).intValue());
//        assertEquals(3, localCounts.get(3).intValue());
    }


    @SuppressWarnings("unchecked")
    @Test
    public void reduceByKey2() {
        List<Row> rows = Arrays.asList(
                RowFactory.create(1.0f,2.0f,3.0f,4.0f,5.0f),
                RowFactory.create(1.0f,2.0f,3.0f,4.0f,5.0f),
                RowFactory.create(1.0f,2.0f,3.0f,4.0f,5.0f),
                RowFactory.create(1.0f,2.0f,3.0f,4.0f,5.0f)
        );

        JavaRDD<Row> javaRDD = sc.parallelize(rows);


//
//        javaRDD.map(x -> x.toSeq()).map(x -> JavaConversions.seqAsJavaList(x)).flatMap(x -> x.iterator());
//        javaRDD.map(x -> x.toSeq().toArray(ClassTag$.MODULE$.apply(String.class)));
//


    }
}
