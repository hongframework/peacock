package com.hframework.peacock.config.domain.model;

public class CfgIndexRedis {
    private Integer id;

    private Integer indexId;

    private Integer redisId;

    private Byte dataType;

    private Byte keyConverter;

    private String keyParts;

    private Byte method;

    private Integer ctime;

    private Integer mtime;

    public CfgIndexRedis(Integer id, Integer indexId, Integer redisId, Byte dataType, Byte keyConverter, String keyParts, Byte method, Integer ctime, Integer mtime) {
        this.id = id;
        this.indexId = indexId;
        this.redisId = redisId;
        this.dataType = dataType;
        this.keyConverter = keyConverter;
        this.keyParts = keyParts;
        this.method = method;
        this.ctime = ctime;
        this.mtime = mtime;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIndexId() {
        return indexId;
    }

    public Integer getRedisId() {
        return redisId;
    }

    public Byte getDataType() {
        return dataType;
    }

    public Byte getKeyConverter() {
        return keyConverter;
    }

    public String getKeyParts() {
        return keyParts;
    }

    public Byte getMethod() {
        return method;
    }

    public Integer getCtime() {
        return ctime;
    }

    public Integer getMtime() {
        return mtime;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public void setIndexId(Integer indexId) {
        this.indexId=indexId;
    }

    public void setRedisId(Integer redisId) {
        this.redisId=redisId;
    }

    public void setDataType(Byte dataType) {
        this.dataType=dataType;
    }

    public void setKeyConverter(Byte keyConverter) {
        this.keyConverter=keyConverter;
    }

    public void setKeyParts(String keyParts) {
        this.keyParts=keyParts;
    }

    public void setMethod(Byte method) {
        this.method=method;
    }

    public void setCtime(Integer ctime) {
        this.ctime=ctime;
    }

    public void setMtime(Integer mtime) {
        this.mtime=mtime;
    }

    public CfgIndexRedis() {
        super();
    }
}