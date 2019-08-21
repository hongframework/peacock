import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import scala.Array;
import scala.Array$;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class ScalaTest {
    public static void main(String[] args) {
        Array<String> arr = new Array<String>(2);
        System.out.println(arr.toString());

        double[] ds1 = new double[]{1d,3d,5d};
        System.out.println(Joiner.on("-").join(Arrays.asList(ds1)));
        System.out.println(Joiner.on("-").join(Lists.newArrayList(ds1)));
        System.out.println("----------------------------------");


        //        Array$.MODULE$.apply(1.0d, scala.collection.mutable.Seq$.MODULE$.)
        JavaConversions.iterableAsScalaIterable(Lists.newArrayList(1,2,4)).toSeq();

        double[] ds = {1,2,3,4,5};
//        Double[] ds2  = Arrays.stream(Array$.MODULE$.range(0, ds.length - 1))
//                .mapToObj(index -> new Double(ds[index]))
//                .toArray(Double[]::new);
//        System.out.println(Joiner.on("-").join(Arrays.asList(ds2)));
//        System.out.println("----------------------------------");
//
//        double[] ds3 = {1,2,3,4,5};
//        Double[] ds4  = Arrays.stream(ds3)
//                .mapToObj(x -> new Double(x)).toArray(Double[]::new);
//        System.out.println(Joiner.on("-").join(Arrays.asList(ds4)));
//        System.out.println("----------------------------------");
//
//        List list = new ArrayList(3);
//        list.stream().map(x -> ds[list.indexOf(x)]);



    }
}
