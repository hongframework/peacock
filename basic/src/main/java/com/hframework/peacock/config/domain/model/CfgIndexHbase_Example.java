package com.hframework.peacock.config.domain.model;

import java.util.ArrayList;
import java.util.List;

public class CfgIndexHbase_Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CfgIndexHbase_Example() {
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

        public Criteria andHbaseIdIsNull() {
            addCriterion("hbase_id is null");
            return (Criteria) this;
        }

        public Criteria andHbaseIdIsNotNull() {
            addCriterion("hbase_id is not null");
            return (Criteria) this;
        }

        public Criteria andHbaseIdEqualTo(Integer value) {
            addCriterion("hbase_id =", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdNotEqualTo(Integer value) {
            addCriterion("hbase_id <>", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdGreaterThan(Integer value) {
            addCriterion("hbase_id >", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("hbase_id >=", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdLessThan(Integer value) {
            addCriterion("hbase_id <", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdLessThanOrEqualTo(Integer value) {
            addCriterion("hbase_id <=", value, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdIn(List<Integer> values) {
            addCriterion("hbase_id in", values, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdNotIn(List<Integer> values) {
            addCriterion("hbase_id not in", values, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdBetween(Integer value1, Integer value2) {
            addCriterion("hbase_id between", value1, value2, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andHbaseIdNotBetween(Integer value1, Integer value2) {
            addCriterion("hbase_id not between", value1, value2, "hbaseId");
            return (Criteria) this;
        }

        public Criteria andTableIsNull() {
            addCriterion("`table` is null");
            return (Criteria) this;
        }

        public Criteria andTableIsNotNull() {
            addCriterion("`table` is not null");
            return (Criteria) this;
        }

        public Criteria andTableEqualTo(String value) {
            addCriterion("`table` =", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotEqualTo(String value) {
            addCriterion("`table` <>", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableGreaterThan(String value) {
            addCriterion("`table` >", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableGreaterThanOrEqualTo(String value) {
            addCriterion("`table` >=", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLessThan(String value) {
            addCriterion("`table` <", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLessThanOrEqualTo(String value) {
            addCriterion("`table` <=", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableLike(String value) {
            addCriterion("`table` like", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotLike(String value) {
            addCriterion("`table` not like", value, "table");
            return (Criteria) this;
        }

        public Criteria andTableIn(List<String> values) {
            addCriterion("`table` in", values, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotIn(List<String> values) {
            addCriterion("`table` not in", values, "table");
            return (Criteria) this;
        }

        public Criteria andTableBetween(String value1, String value2) {
            addCriterion("`table` between", value1, value2, "table");
            return (Criteria) this;
        }

        public Criteria andTableNotBetween(String value1, String value2) {
            addCriterion("`table` not between", value1, value2, "table");
            return (Criteria) this;
        }

        public Criteria andFamilyIsNull() {
            addCriterion("family is null");
            return (Criteria) this;
        }

        public Criteria andFamilyIsNotNull() {
            addCriterion("family is not null");
            return (Criteria) this;
        }

        public Criteria andFamilyEqualTo(String value) {
            addCriterion("family =", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyNotEqualTo(String value) {
            addCriterion("family <>", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyGreaterThan(String value) {
            addCriterion("family >", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyGreaterThanOrEqualTo(String value) {
            addCriterion("family >=", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyLessThan(String value) {
            addCriterion("family <", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyLessThanOrEqualTo(String value) {
            addCriterion("family <=", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyLike(String value) {
            addCriterion("family like", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyNotLike(String value) {
            addCriterion("family not like", value, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyIn(List<String> values) {
            addCriterion("family in", values, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyNotIn(List<String> values) {
            addCriterion("family not in", values, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyBetween(String value1, String value2) {
            addCriterion("family between", value1, value2, "family");
            return (Criteria) this;
        }

        public Criteria andFamilyNotBetween(String value1, String value2) {
            addCriterion("family not between", value1, value2, "family");
            return (Criteria) this;
        }

        public Criteria andQualifierIsNull() {
            addCriterion("qualifier is null");
            return (Criteria) this;
        }

        public Criteria andQualifierIsNotNull() {
            addCriterion("qualifier is not null");
            return (Criteria) this;
        }

        public Criteria andQualifierEqualTo(String value) {
            addCriterion("qualifier =", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierNotEqualTo(String value) {
            addCriterion("qualifier <>", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierGreaterThan(String value) {
            addCriterion("qualifier >", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierGreaterThanOrEqualTo(String value) {
            addCriterion("qualifier >=", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierLessThan(String value) {
            addCriterion("qualifier <", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierLessThanOrEqualTo(String value) {
            addCriterion("qualifier <=", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierLike(String value) {
            addCriterion("qualifier like", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierNotLike(String value) {
            addCriterion("qualifier not like", value, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierIn(List<String> values) {
            addCriterion("qualifier in", values, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierNotIn(List<String> values) {
            addCriterion("qualifier not in", values, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierBetween(String value1, String value2) {
            addCriterion("qualifier between", value1, value2, "qualifier");
            return (Criteria) this;
        }

        public Criteria andQualifierNotBetween(String value1, String value2) {
            addCriterion("qualifier not between", value1, value2, "qualifier");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterIsNull() {
            addCriterion("rowkey_converter is null");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterIsNotNull() {
            addCriterion("rowkey_converter is not null");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterEqualTo(Byte value) {
            addCriterion("rowkey_converter =", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterNotEqualTo(Byte value) {
            addCriterion("rowkey_converter <>", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterGreaterThan(Byte value) {
            addCriterion("rowkey_converter >", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterGreaterThanOrEqualTo(Byte value) {
            addCriterion("rowkey_converter >=", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterLessThan(Byte value) {
            addCriterion("rowkey_converter <", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterLessThanOrEqualTo(Byte value) {
            addCriterion("rowkey_converter <=", value, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterIn(List<Byte> values) {
            addCriterion("rowkey_converter in", values, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterNotIn(List<Byte> values) {
            addCriterion("rowkey_converter not in", values, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterBetween(Byte value1, Byte value2) {
            addCriterion("rowkey_converter between", value1, value2, "rowkeyConverter");
            return (Criteria) this;
        }

        public Criteria andRowkeyConverterNotBetween(Byte value1, Byte value2) {
            addCriterion("rowkey_converter not between", value1, value2, "rowkeyConverter");
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