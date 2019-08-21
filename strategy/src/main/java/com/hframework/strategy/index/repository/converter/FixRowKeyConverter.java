package com.hframework.strategy.index.repository.converter;

import com.google.common.base.Joiner;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public class FixRowKeyConverter implements RowKeyConverter<Integer>{

    private String[] parts ;

    public String getKey(){
        return convert(-1);
    }

    public FixRowKeyConverter(String[] parts){
        this.parts =  parts;
    }

    @Override
    public String convert(Integer keyNumber) {
        return Joiner.on("_").join(parts);
    }

    @Override
    public Integer reverse(String key) {
        return -1;
    }

}

