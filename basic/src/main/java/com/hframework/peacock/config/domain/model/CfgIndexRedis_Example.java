package com.hframework.peacock.config.domain.model;

import java.util.ArrayList;
import java.util.List;

public class CfgIndexRedis_Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CfgIndexRedis_Example() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart=limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd=limitEnd;
    }

    public Integer getLimitEnd() {
        return limitEnd;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIndexIdIsNull() {
            addCriterion("index_id is null");
            return (Criteria) this;
        }

        public Criteria andIndexIdIsNotNull() {
            addCriterion("index_id is not null");
            return (Criteria) this;
        }

        public Criteria andIndexIdEqualTo(Integer value) {
            addCriterion("index_id =", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotEqualTo(Integer value) {
            addCriterion("index_id <>", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdGreaterThan(Integer value) {
            addCriterion("index_id >", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("index_id >=", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdLessThan(Integer value) {
            addCriterion("index_id <", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdLessThanOrEqualTo(Integer value) {
            addCriterion("index_id <=", value, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdIn(List<Integer> values) {
            addCriterion("index_id in", values, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotIn(List<Integer> values) {
            addCriterion("index_id not in", values, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdBetween(Integer value1, Integer value2) {
            addCriterion("index_id between", value1, value2, "indexId");
            return (Criteria) this;
        }

        public Criteria andIndexIdNotBetween(Integer value1, Integer value2) {
            addCriterion("index_id not between", value1, value2, "indexId");
            return (Criteria) this;
        }

        public Criteria andRedisIdIsNull() {
            addCriterion("redis_id is null");
            return (Criteria) this;
        }

        public Criteria andRedisIdIsNotNull() {
            addCriterion("redis_id is not null");
            return (Criteria) this;
        }

        public Criteria andRedisIdEqualTo(Integer value) {
            addCriterion("redis_id =", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdNotEqualTo(Integer value) {
            addCriterion("redis_id <>", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdGreaterThan(Integer value) {
            addCriterion("redis_id >", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("redis_id >=", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdLessThan(Integer value) {
            addCriterion("redis_id <", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdLessThanOrEqualTo(Integer value) {
            addCriterion("redis_id <=", value, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdIn(List<Integer> values) {
            addCriterion("redis_id in", values, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdNotIn(List<Integer> values) {
            addCriterion("redis_id not in", values, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdBetween(Integer value1, Integer value2) {
            addCriterion("redis_id between", value1, value2, "redisId");
            return (Criteria) this;
        }

        public Criteria andRedisIdNotBetween(Integer value1, Integer value2) {
            addCriterion("redis_id not between", value1, value2, "redisId");
            return (Criteria) this;
        }

        public Criteria andDataTypeIsNull() {
            addCriterion("data_type is null");
            return (Criteria) this;
        }

        public Criteria andDataTypeIsNotNull() {
            addCriterion("data_type is not null");
            return (Criteria) this;
        }

        public Criteria andDataTypeEqualTo(Byte value) {
            addCriterion("data_type =", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeNotEqualTo(Byte value) {
            addCriterion("data_type <>", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeGreaterThan(Byte value) {
            addCriterion("data_type >", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("data_type >=", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeLessThan(Byte value) {
            addCriterion("data_type <", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeLessThanOrEqualTo(Byte value) {
            addCriterion("data_type <=", value, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeIn(List<Byte> values) {
            addCriterion("data_type in", values, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeNotIn(List<Byte> values) {
            addCriterion("data_type not in", values, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeBetween(Byte value1, Byte value2) {
            addCriterion("data_type between", value1, value2, "dataType");
            return (Criteria) this;
        }

        public Criteria andDataTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("data_type not between", value1, value2, "dataType");
            return (Criteria) this;
        }

        public Criteria andKeyConverterIsNull() {
            addCriterion("key_converter is null");
            return (Criteria) this;
        }

        public Criteria andKeyConverterIsNotNull() {
            addCriterion("key_converter is not null");
            return (Criteria) this;
        }

        public Criteria andKeyConverterEqualTo(Byte value) {
            addCriterion("key_converter =", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterNotEqualTo(Byte value) {
            addCriterion("key_converter <>", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterGreaterThan(Byte value) {
            addCriterion("key_converter >", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterGreaterThanOrEqualTo(Byte value) {
            addCriterion("key_converter >=", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterLessThan(Byte value) {
            addCriterion("key_converter <", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterLessThanOrEqualTo(Byte value) {
            addCriterion("key_converter <=", value, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterIn(List<Byte> values) {
            addCriterion("key_converter in", values, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterNotIn(List<Byte> values) {
            addCriterion("key_converter not in", values, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterBetween(Byte value1, Byte value2) {
            addCriterion("key_converter between", value1, value2, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyConverterNotBetween(Byte value1, Byte value2) {
            addCriterion("key_converter not between", value1, value2, "keyConverter");
            return (Criteria) this;
        }

        public Criteria andKeyPartsIsNull() {
            addCriterion("key_parts is null");
            return (Criteria) this;
        }

        public Criteria andKeyPartsIsNotNull() {
            addCriterion("key_parts is not null");
            return (Criteria) this;
        }

        public Criteria andKeyPartsEqualTo(String value) {
            addCriterion("key_parts =", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsNotEqualTo(String value) {
            addCriterion("key_parts <>", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsGreaterThan(String value) {
            addCriterion("key_parts >", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsGreaterThanOrEqualTo(String value) {
            addCriterion("key_parts >=", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsLessThan(String value) {
            addCriterion("key_parts <", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsLessThanOrEqualTo(String value) {
            addCriterion("key_parts <=", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsLike(String value) {
            addCriterion("key_parts like", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsNotLike(String value) {
            addCriterion("key_parts not like", value, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsIn(List<String> values) {
            addCriterion("key_parts in", values, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsNotIn(List<String> values) {
            addCriterion("key_parts not in", values, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsBetween(String value1, String value2) {
            addCriterion("key_parts between", value1, value2, "keyParts");
            return (Criteria) this;
        }

        public Criteria andKeyPartsNotBetween(String value1, String value2) {
            addCriterion("key_parts not between", value1, value2, "keyParts");
            return (Criteria) this;
        }

        public Criteria andMethodIsNull() {
            addCriterion("`method` is null");
            return (Criteria) this;
        }

        public Criteria andMethodIsNotNull() {
            addCriterion("`method` is not null");
            return (Criteria) this;
        }

        public Criteria andMethodEqualTo(Byte value) {
            addCriterion("`method` =", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotEqualTo(Byte value) {
            addCriterion("`method` <>", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThan(Byte value) {
            addCriterion("`method` >", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodGreaterThanOrEqualTo(Byte value) {
            addCriterion("`method` >=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThan(Byte value) {
            addCriterion("`method` <", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodLessThanOrEqualTo(Byte value) {
            addCriterion("`method` <=", value, "method");
            return (Criteria) this;
        }

        public Criteria andMethodIn(List<Byte> values) {
            addCriterion("`method` in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotIn(List<Byte> values) {
            addCriterion("`method` not in", values, "method");
            return (Criteria) this;
        }

        public Criteria andMethodBetween(Byte value1, Byte value2) {
            addCriterion("`method` between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andMethodNotBetween(Byte value1, Byte value2) {
            addCriterion("`method` not between", value1, value2, "method");
            return (Criteria) this;
        }

        public Criteria andCtimeIsNull() {
            addCriterion("ctime is null");
            return (Criteria) this;
        }

        public Criteria andCtimeIsNotNull() {
            addCriterion("ctime is not null");
            return (Criteria) this;
        }

        public Criteria andCtimeEqualTo(Integer value) {
            addCriterion("ctime =", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotEqualTo(Integer value) {
            addCriterion("ctime <>", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeGreaterThan(Integer value) {
            addCriterion("ctime >", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("ctime >=", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeLessThan(Integer value) {
            addCriterion("ctime <", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeLessThanOrEqualTo(Integer value) {
            addCriterion("ctime <=", value, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeIn(List<Integer> values) {
            addCriterion("ctime in", values, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotIn(List<Integer> values) {
            addCriterion("ctime not in", values, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeBetween(Integer value1, Integer value2) {
            addCriterion("ctime between", value1, value2, "ctime");
            return (Criteria) this;
        }

        public Criteria andCtimeNotBetween(Integer value1, Integer value2) {
            addCriterion("ctime not between", value1, value2, "ctime");
            return (Criteria) this;
        }

        public Criteria andMtimeIsNull() {
            addCriterion("mtime is null");
            return (Criteria) this;
        }

        public Criteria andMtimeIsNotNull() {
            addCriterion("mtime is not null");
            return (Criteria) this;
        }

        public Criteria andMtimeEqualTo(Integer value) {
            addCriterion("mtime =", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeNotEqualTo(Integer value) {
            addCriterion("mtime <>", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeGreaterThan(Integer value) {
            addCriterion("mtime >", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("mtime >=", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeLessThan(Integer value) {
            addCriterion("mtime <", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeLessThanOrEqualTo(Integer value) {
            addCriterion("mtime <=", value, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeIn(List<Integer> values) {
            addCriterion("mtime in", values, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeNotIn(List<Integer> values) {
            addCriterion("mtime not in", values, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeBetween(Integer value1, Integer value2) {
            addCriterion("mtime between", value1, value2, "mtime");
            return (Criteria) this;
        }

        public Criteria andMtimeNotBetween(Integer value1, Integer value2) {
            addCriterion("mtime not between", value1, value2, "mtime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}