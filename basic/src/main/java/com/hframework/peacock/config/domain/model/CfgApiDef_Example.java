package com.hframework.peacock.config.domain.model;

import java.util.ArrayList;
import java.util.List;

public class CfgApiDef_Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CfgApiDef_Example() {
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

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andPathIsNull() {
            addCriterion("`path` is null");
            return (Criteria) this;
        }

        public Criteria andPathIsNotNull() {
            addCriterion("`path` is not null");
            return (Criteria) this;
        }

        public Criteria andPathEqualTo(String value) {
            addCriterion("`path` =", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotEqualTo(String value) {
            addCriterion("`path` <>", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThan(String value) {
            addCriterion("`path` >", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathGreaterThanOrEqualTo(String value) {
            addCriterion("`path` >=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThan(String value) {
            addCriterion("`path` <", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLessThanOrEqualTo(String value) {
            addCriterion("`path` <=", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathLike(String value) {
            addCriterion("`path` like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotLike(String value) {
            addCriterion("`path` not like", value, "path");
            return (Criteria) this;
        }

        public Criteria andPathIn(List<String> values) {
            addCriterion("`path` in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotIn(List<String> values) {
            addCriterion("`path` not in", values, "path");
            return (Criteria) this;
        }

        public Criteria andPathBetween(String value1, String value2) {
            addCriterion("`path` between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andPathNotBetween(String value1, String value2) {
            addCriterion("`path` not between", value1, value2, "path");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("`name` is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("`name` is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("`name` =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("`name` <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("`name` >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("`name` >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("`name` <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("`name` <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("`name` like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("`name` not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("`name` in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("`name` not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("`name` between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("`name` not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("version like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("version not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andPropKeyIsNull() {
            addCriterion("prop_key is null");
            return (Criteria) this;
        }

        public Criteria andPropKeyIsNotNull() {
            addCriterion("prop_key is not null");
            return (Criteria) this;
        }

        public Criteria andPropKeyEqualTo(String value) {
            addCriterion("prop_key =", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyNotEqualTo(String value) {
            addCriterion("prop_key <>", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyGreaterThan(String value) {
            addCriterion("prop_key >", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyGreaterThanOrEqualTo(String value) {
            addCriterion("prop_key >=", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyLessThan(String value) {
            addCriterion("prop_key <", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyLessThanOrEqualTo(String value) {
            addCriterion("prop_key <=", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyLike(String value) {
            addCriterion("prop_key like", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyNotLike(String value) {
            addCriterion("prop_key not like", value, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyIn(List<String> values) {
            addCriterion("prop_key in", values, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyNotIn(List<String> values) {
            addCriterion("prop_key not in", values, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyBetween(String value1, String value2) {
            addCriterion("prop_key between", value1, value2, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropKeyNotBetween(String value1, String value2) {
            addCriterion("prop_key not between", value1, value2, "propKey");
            return (Criteria) this;
        }

        public Criteria andPropTypeIsNull() {
            addCriterion("prop_type is null");
            return (Criteria) this;
        }

        public Criteria andPropTypeIsNotNull() {
            addCriterion("prop_type is not null");
            return (Criteria) this;
        }

        public Criteria andPropTypeEqualTo(String value) {
            addCriterion("prop_type =", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeNotEqualTo(String value) {
            addCriterion("prop_type <>", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeGreaterThan(String value) {
            addCriterion("prop_type >", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeGreaterThanOrEqualTo(String value) {
            addCriterion("prop_type >=", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeLessThan(String value) {
            addCriterion("prop_type <", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeLessThanOrEqualTo(String value) {
            addCriterion("prop_type <=", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeLike(String value) {
            addCriterion("prop_type like", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeNotLike(String value) {
            addCriterion("prop_type not like", value, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeIn(List<String> values) {
            addCriterion("prop_type in", values, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeNotIn(List<String> values) {
            addCriterion("prop_type not in", values, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeBetween(String value1, String value2) {
            addCriterion("prop_type between", value1, value2, "propType");
            return (Criteria) this;
        }

        public Criteria andPropTypeNotBetween(String value1, String value2) {
            addCriterion("prop_type not between", value1, value2, "propType");
            return (Criteria) this;
        }

        public Criteria andPropOptionsIsNull() {
            addCriterion("prop_options is null");
            return (Criteria) this;
        }

        public Criteria andPropOptionsIsNotNull() {
            addCriterion("prop_options is not null");
            return (Criteria) this;
        }

        public Criteria andPropOptionsEqualTo(String value) {
            addCriterion("prop_options =", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsNotEqualTo(String value) {
            addCriterion("prop_options <>", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsGreaterThan(String value) {
            addCriterion("prop_options >", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsGreaterThanOrEqualTo(String value) {
            addCriterion("prop_options >=", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsLessThan(String value) {
            addCriterion("prop_options <", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsLessThanOrEqualTo(String value) {
            addCriterion("prop_options <=", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsLike(String value) {
            addCriterion("prop_options like", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsNotLike(String value) {
            addCriterion("prop_options not like", value, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsIn(List<String> values) {
            addCriterion("prop_options in", values, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsNotIn(List<String> values) {
            addCriterion("prop_options not in", values, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsBetween(String value1, String value2) {
            addCriterion("prop_options between", value1, value2, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropOptionsNotBetween(String value1, String value2) {
            addCriterion("prop_options not between", value1, value2, "propOptions");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionIsNull() {
            addCriterion("prop_description is null");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionIsNotNull() {
            addCriterion("prop_description is not null");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionEqualTo(String value) {
            addCriterion("prop_description =", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionNotEqualTo(String value) {
            addCriterion("prop_description <>", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionGreaterThan(String value) {
            addCriterion("prop_description >", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("prop_description >=", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionLessThan(String value) {
            addCriterion("prop_description <", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionLessThanOrEqualTo(String value) {
            addCriterion("prop_description <=", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionLike(String value) {
            addCriterion("prop_description like", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionNotLike(String value) {
            addCriterion("prop_description not like", value, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionIn(List<String> values) {
            addCriterion("prop_description in", values, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionNotIn(List<String> values) {
            addCriterion("prop_description not in", values, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionBetween(String value1, String value2) {
            addCriterion("prop_description between", value1, value2, "propDescription");
            return (Criteria) this;
        }

        public Criteria andPropDescriptionNotBetween(String value1, String value2) {
            addCriterion("prop_description not between", value1, value2, "propDescription");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNull() {
            addCriterion("remark is null");
            return (Criteria) this;
        }

        public Criteria andRemarkIsNotNull() {
            addCriterion("remark is not null");
            return (Criteria) this;
        }

        public Criteria andRemarkEqualTo(String value) {
            addCriterion("remark =", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotEqualTo(String value) {
            addCriterion("remark <>", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThan(String value) {
            addCriterion("remark >", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkGreaterThanOrEqualTo(String value) {
            addCriterion("remark >=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThan(String value) {
            addCriterion("remark <", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLessThanOrEqualTo(String value) {
            addCriterion("remark <=", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkLike(String value) {
            addCriterion("remark like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotLike(String value) {
            addCriterion("remark not like", value, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkIn(List<String> values) {
            addCriterion("remark in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotIn(List<String> values) {
            addCriterion("remark not in", values, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkBetween(String value1, String value2) {
            addCriterion("remark between", value1, value2, "remark");
            return (Criteria) this;
        }

        public Criteria andRemarkNotBetween(String value1, String value2) {
            addCriterion("remark not between", value1, value2, "remark");
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