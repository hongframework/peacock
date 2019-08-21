package com.hframework.strategy.index.repository.converter;

import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by zhangquanhong on 2017/7/17.
 */
public  class SHA256RowKeyConverter implements RowKeyConverter<Integer>{

    @Override
    public String convert(Integer keyNumber) {
        String encode = sha256Encode(String.valueOf(keyNumber));
        String prefix = StringUtils.substring(encode, 0, 2);
        return prefix + "_" + keyNumber;
    }

    @Override
    public Integer reverse(String key) {
        return Integer.valueOf(key.substring(3));
    }

    public static String sha256Encode(String v){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        md.update(v.getBytes());
        return byteToHex(md.digest());
    }

    public static String byteToHex(final byte[] hash) {
        try{

            final Formatter formatter = new Formatter();
            for (byte b : hash)
                formatter.format("%02x", b);
            return formatter.toString();
        }catch(Exception e){
            return "";
        }
    }
}

