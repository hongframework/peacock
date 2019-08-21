package com.hframework.peacock.config.domain.model;

import java.util.ArrayList;
import java.util.List;

public class CfgIndexMysql_Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CfgIndexMysql_Example() {
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

        public Criteria andMysqlIdIsNull() {
            addCriterion("mysql_id is null");
            return (Criteria) this;
        }

        public Criteria andMysqlIdIsNotNull() {
            addCriterion("mysql_id is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlIdEqualTo(Integer value) {
            addCriterion("mysql_id =", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdNotEqualTo(Integer value) {
            addCriterion("mysql_id <>", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdGreaterThan(Integer value) {
            addCriterion("mysql_id >", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mysql_id >=", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdLessThan(Integer value) {
            addCriterion("mysql_id <", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdLessThanOrEqualTo(Integer value) {
            addCriterion("mysql_id <=", value, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdIn(List<Integer> values) {
            addCriterion("mysql_id in", values, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdNotIn(List<Integer> values) {
            addCriterion("mysql_id not in", values, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdBetween(Integer value1, Integer value2) {
            addCriterion("mysql_id between", value1, value2, "mysqlId");
            return (Criteria) this;
        }

        public Criteria andMysqlIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mysql_id not between", value1, value2, "mysqlId");
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

        public Criteria andPrimaryKeyIsNull() {
            addCriterion("primary_key is null");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyIsNotNull() {
            addCriterion("primary_key is not null");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyEqualTo(String value) {
            addCriterion("primary_key =", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyNotEqualTo(String value) {
            addCriterion("primary_key <>", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyGreaterThan(String value) {
            addCriterion("primary_key >", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyGreaterThanOrEqualTo(String value) {
            addCriterion("primary_key >=", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyLessThan(String value) {
            addCriterion("primary_key <", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyLessThanOrEqualTo(String value) {
            addCriterion("primary_key <=", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyLike(String value) {
            addCriterion("primary_key like", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyNotLike(String value) {
            addCriterion("primary_key not like", value, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyIn(List<String> values) {
            addCriterion("primary_key in", values, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyNotIn(List<String> values) {
            addCriterion("primary_key not in", values, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyBetween(String value1, String value2) {
            addCriterion("primary_key between", value1, value2, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andPrimaryKeyNotBetween(String value1, String value2) {
            addCriterion("primary_key not between", value1, value2, "primaryKey");
            return (Criteria) this;
        }

        public Criteria andColumnIsNull() {
            addCriterion("`column` is null");
            return (Criteria) this;
        }

        public Criteria andColumnIsNotNull() {
            addCriterion("`column` is not null");
            return (Criteria) this;
        }

        public Criteria andColumnEqualTo(String value) {
            addCriterion("`column` =", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnNotEqualTo(String value) {
            addCriterion("`column` <>", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnGreaterThan(String value) {
            addCriterion("`column` >", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnGreaterThanOrEqualTo(String value) {
            addCriterion("`column` >=", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnLessThan(String value) {
            addCriterion("`column` <", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnLessThanOrEqualTo(String value) {
            addCriterion("`column` <=", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnLike(String value) {
            addCriterion("`column` like", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnNotLike(String value) {
            addCriterion("`column` not like", value, "column");
            return (Criteria) this;
        }

        public Criteria andColumnIn(List<String> values) {
            addCriterion("`column` in", values, "column");
            return (Criteria) this;
        }

        public Criteria andColumnNotIn(List<String> values) {
            addCriterion("`column` not in", values, "column");
            return (Criteria) this;
        }

        public Criteria andColumnBetween(String value1, String value2) {
            addCriterion("`column` between", value1, value2, "column");
            return (Criteria) this;
        }

        public Criteria andColumnNotBetween(String value1, String value2) {
            addCriterion("`column` not between", value1, value2, "column");
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