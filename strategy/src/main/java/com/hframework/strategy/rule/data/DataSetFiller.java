package com.hframework.strategy.rule.data;

import com.hframework.strategy.index.IndexRepository;
import com.hframework.strategy.rule.ExpressionEngine;
import com.hframework.strategy.rule.exceptions.DataFetchException;
import com.hframework.strategy.rule.fetch.Fetcher;
import com.hframework.strategy.rule.fetch.DemoFetcher;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/6/27.
 */
public class DataSetFiller {

    public static void autoFullDataSet(EDataSet dataSet, String varString, Set<Integer> ignoreRowIndex) {
        autoFullDataSet(dataSet, new String[]{varString}, ignoreRowIndex);
    }

    public static void setFetcherOverview(EDataSet dataSet, String[] varStrings){
        Map<Fetcher, List<String>> fetcherInfos = new HashMap<>();
        for (String varString : varStrings) {
            String fieldCode = varString.replaceAll("[${}]+", "");
            if(!dataSet.containDataFieldCode(fieldCode)) {
                if(!IndexRepository.getDefaultInstance().getFetchers().containsKey(fieldCode)) {
                    throw new DataFetchException("field " + fieldCode + "'s fetcher is not exists !");
                }

                Fetcher fetcher = IndexRepository.getDefaultInstance().getFetchers().getMiddle(fieldCode);
                if(!fetcherInfos.containsKey(fetcher)) fetcherInfos.put(fetcher, new ArrayList<String>());
                fetcherInfos.get(fetcher).add(fieldCode);
            }
        }
        dataSet.setFetcherOverview(fetcherInfos);
    }

    public static void autoFullDataSet(EDataSet dataSet, String[] varStrings, Set<Integer> rowIndexScope){
        Set<Fetcher> fetchers = new HashSet<>();
        for (String varString : varStrings) {
            String fieldCode = varString.replaceAll("[${}]+", "");
            if(!dataSet.containDataFieldCode(fieldCode)) {
                fetchers.add(IndexRepository.getDefaultInstance().getFetchers().getMiddle(fieldCode));
            }
        }

        for (Fetcher fetcher : fetchers) {
            List<String> fieldCodeList = dataSet.getFetcherOverview().get(fetcher);
            List<EDataField> eDataFields = new ArrayList();
            for (String fieldCode : fieldCodeList) {
                if(!dataSet.containDataFieldCode(fieldCode)) {
                    EDataField eDataField = dataSet.addDataField(fieldCode, false);
                    eDataField.setIsFetch(true);
                    eDataField.setFetcher(IndexRepository.getDefaultInstance().getFetchers().getMiddle(fieldCode));
                    eDataField.setFetchAttr(IndexRepository.getDefaultInstance().getFetchers().getRight(fieldCode));
                    eDataFields.add(eDataField);
                }
            }
            dataSet.fetch(fetcher, eDataFields, rowIndexScope);

        }
    }



}
