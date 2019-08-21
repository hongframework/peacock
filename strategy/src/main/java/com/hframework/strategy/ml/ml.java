package com.hframework.strategy.ml;


import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.evaluator.tree.NodeScoreDistribution;
import org.jpmml.model.PMMLUtil;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by chencheng on 2017/6/23.
 */





public class ml {

    private ml(){
    }


    public static class Table extends ArrayList<List<String>> {



        private String separator = null;


        public Table(){
            super(1024);
        }

        public String getSeparator(){
            return this.separator;
        }

        public void setSeparator(String separator){
            this.separator = separator;
        }
    }




    public static Table readTable(String is, String separator_line,String separator) throws IOException {
        Table table = new Table();

        Splitter splitter = null;

        if (splitter == null) {
            splitter = Splitter.on(separator);
        }


        Splitter splitter_line = null;

        if (splitter_line == null) {
            splitter_line = Splitter.on(separator_line);
        }

         // End if


        if(is.contains(";")) {




            List<String> row_line = Lists.newArrayList(splitter_line.split(is));




            for (int i = 0; i < row_line.size(); i++) {


                String line = row_line.get(i);

                if (line == null || (line.trim()).equals("")) {
                    break;
                } // End if




                List<String> row = Lists.newArrayList(splitter.split(line.trim()));

                table.add(row);
            }
        }

        else if (is != null && !is.trim().equals("")) {

            // End if
            table.add(Lists.newArrayList(splitter.split(is.trim())));
        }

        table.setSeparator(separator);



        return table;
    }

//    static
//    public CsvUtil.Table readTable(File file, String separator) throws IOException {
//
//        try(InputStream is = new FileInputStream(file)){
//            return CsvUtil.readTable(is, separator);
//        }
//    }


    public static String predict(String data,String model,String schema) throws JAXBException, SAXException, IOException {


        String separator = ",";
        String separator_line = ";";
        String header = schema;
                //"f1,f2,f3,f4,f5,f6";

        Splitter on = Splitter.on(separator);

        List<String> strings = Lists.newArrayList(on.split(header));

      //  File file = new File("C:\\Users\\Administrator\\Desktop\\kk");

       // InputStream is = new FileInputStream(file);


        ByteArrayInputStream is= new ByteArrayInputStream(model.getBytes());

        PMML unmarshal = PMMLUtil.unmarshal(is);





        //File file = new File("C:\\Users\\Administrator\\Desktop\\iris");

        Table inputTable = readTable(data,separator_line,separator);

        List<Map<FieldName, String>> records = new ArrayList<>(inputTable.size());

        for(int row = 0; row < inputTable.size(); row++){
            List<String> bodyRow = inputTable.get(row);



            Map<FieldName, String> record = new LinkedHashMap<>();

            for(int column = 0; column < strings.size(); column++){
                FieldName name = FieldName.create(strings.get(column));
                String value = bodyRow.get(column);

                record.put(name, value);
            }

            records.add(record);
        }




        ModelEvaluatorFactory modelEvaluatorFactory = ModelEvaluatorFactory.newInstance();
        Evaluator evaluator = modelEvaluatorFactory.newModelEvaluator(unmarshal);

        List<InputField> inputFields = evaluator.getInputFields();

        List<TargetField> targetFields = evaluator.getTargetFields();

        FieldName targetFieldName = targetFields.get(0).getName();


        Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

//        List<Map<FieldName, ?>> outputRecords = new ArrayList<>(records.size());

        StringBuilder stringBuilder = new StringBuilder();

        for(Map<FieldName, ?> inputRecord : records){
            arguments.clear();

            for(InputField inputField : inputFields){
                FieldName name = inputField.getName();

                FieldValue value = EvaluatorUtil.prepare(inputField, inputRecord.get(name));

                arguments.put(name, value);
            }


            Map<FieldName, ?> result = evaluator.evaluate(arguments);


            Object targetFieldValue = result.get(targetFieldName);

            Object predict = ((NodeScoreDistribution) targetFieldValue).getResult();

            stringBuilder.append(predict+",");
//            outputRecords.add(result);
//


        }


        return stringBuilder.deleteCharAt(stringBuilder.length()-1).toString();





//        SparkConf sparkConf = new SparkConf();
//        sparkConf.setAppName("pmml").setMaster("local");
//
//        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
//        SQLContext sqlContext = new SQLContext(sparkContext);
//
//
//        List<Row> rows = Arrays.asList(
//                RowFactory.create(0.0, 3.1, 3.4, 3.9, 2.4,4.1,3.2),
//                RowFactory.create(0.0, 2.3, 1.4, 4.2, 2.2,4.1,3.2),
//                RowFactory.create(0.0, 2.3, 1.4, 4.2, 2.2,4.1,3.2),
//                RowFactory.create(0.0, 2.5, 1.5, 3.2, 2.2,4.4,5.4),
//                RowFactory.create(1.0, 2.4, 1.4, 4.2, 2.2,4.4,3.4),
//                RowFactory.create(1.0, 2.5, 1.4, 2.2, 2.7,4.4,3.4),
//                RowFactory.create(1.0, 2.4, 1.5, 4.2, 2.6,4.4,3.4)
//2.3,1.4,4.2,2.2,4.1,3.2;2.3,1.4,4.2,2.2,4.1,3.2
//
//        );
//
//        StructType structType = new StructType(new StructField[]{
//                new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f1", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f2", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f3", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f4", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f5", DataTypes.DoubleType, false, Metadata.empty()),
//                new StructField("f6", DataTypes.DoubleType, false, Metadata.empty())
//
//
//        });
//        DataFrame dataFrame = sqlContext.createDataFrame(rows, structType);
//
//        dataFrame.show();
//
//        DataFrame va = new VectorAssembler().setInputCols(new String[]{"f1", "f2", "f3", "f4", "f5", "f6"})
//                .setOutputCol("feature").transform(dataFrame);
//        VectorAssembler vec = new VectorAssembler().setInputCols(new String[]{"f1", "f2", "f3", "f4", "f5", "f6"}).setOutputCol("feature");
////
////
//        StringIndexerModel fit1 = new StringIndexer().setInputCol("label").setOutputCol("indexLabel").fit(va);
////
//
//
//
//
//        IndexToString indexToString = new IndexToString().setInputCol("prediction").setOutputCol("predictionLabel").setLabels(fit1.labels());
//
//
//
//        DecisionTreeClassifier decisionTreeClassifier = new DecisionTreeClassifier().setLabelCol("indexLabel").setFeaturesCol("feature");
//
//        Pipeline pipeline = new Pipeline().setStages(new PipelineStage[]{vec, fit1, decisionTreeClassifier, indexToString});
//
//        PipelineModel fit = pipeline.fit(dataFrame);
//
////
//        PMML pmml = ConverterUtil.toPMML(dataFrame.schema(), fit);
//
//
//            FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\kk");
//            MetroJAXBUtil.marshalPMML(pmml,fileOutputStream);
//
//
//
//
//        dataFrame.printSchema();
//
//        va.show();

    }


}
