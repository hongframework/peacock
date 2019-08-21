package com.hframework.smartsql.client2;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hframework.smartsql.SmartSqlClient;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.Row$;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.catalyst.expressions.GenericRow;
import scala.Array;
import scala.Array$;
import scala.Int;
import scala.collection.immutable.HashMap;
import scala.runtime.RichDouble;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Demo {
    public static void main(String[] args) {

        double d = 123d;
        double sd = new RichDouble(1.57d).toDouble();
        System.out.println(sd);
        Array<Double> doubleArray = new Array<>(5);
//        for (int i = 0; i < 5; i++) {
//            doubleArray.update(0,sd + 1);
//        }

        Row row = RowFactory.create(1,2,3,4,5);
        System.out.println(doubleArray);
        System.out.println(row.getInt(1));

    }
}
