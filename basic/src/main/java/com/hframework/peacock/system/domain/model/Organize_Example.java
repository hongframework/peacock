package com.hframework.peacock.system.domain.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Organize_Example {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public Organize_Example() {
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

        public Criteria andOrganizeIdIsNull() {
            addCriterion("organize_id is null");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdIsNotNull() {
            addCriterion("organize_id is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdEqualTo(Long value) {
            addCriterion("organize_id =", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdNotEqualTo(Long value) {
            addCriterion("organize_id <>", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdGreaterThan(Long value) {
            addCriterion("organize_id >", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("organize_id >=", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdLessThan(Long value) {
            addCriterion("organize_id <", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdLessThanOrEqualTo(Long value) {
            addCriterion("organize_id <=", value, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdIn(List<Long> values) {
            addCriterion("organize_id in", values, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdNotIn(List<Long> values) {
            addCriterion("organize_id not in", values, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdBetween(Long value1, Long value2) {
            addCriterion("organize_id between", value1, value2, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeIdNotBetween(Long value1, Long value2) {
            addCriterion("organize_id not between", value1, value2, "organizeId");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeIsNull() {
            addCriterion("organize_code is null");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeIsNotNull() {
            addCriterion("organize_code is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeEqualTo(String value) {
            addCriterion("organize_code =", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeNotEqualTo(String value) {
            addCriterion("organize_code <>", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeGreaterThan(String value) {
            addCriterion("organize_code >", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeGreaterThanOrEqualTo(String value) {
            addCriterion("organize_code >=", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeLessThan(String value) {
            addCriterion("organize_code <", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeLessThanOrEqualTo(String value) {
            addCriterion("organize_code <=", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeLike(String value) {
            addCriterion("organize_code like", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeNotLike(String value) {
            addCriterion("organize_code not like", value, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeIn(List<String> values) {
            addCriterion("organize_code in", values, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeNotIn(List<String> values) {
            addCriterion("organize_code not in", values, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeBetween(String value1, String value2) {
            addCriterion("organize_code between", value1, value2, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeCodeNotBetween(String value1, String value2) {
            addCriterion("organize_code not between", value1, value2, "organizeCode");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameIsNull() {
            addCriterion("organize_name is null");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameIsNotNull() {
            addCriterion("organize_name is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameEqualTo(String value) {
            addCriterion("organize_name =", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameNotEqualTo(String value) {
            addCriterion("organize_name <>", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameGreaterThan(String value) {
            addCriterion("organize_name >", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameGreaterThanOrEqualTo(String value) {
            addCriterion("organize_name >=", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameLessThan(String value) {
            addCriterion("organize_name <", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameLessThanOrEqualTo(String value) {
            addCriterion("organize_name <=", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameLike(String value) {
            addCriterion("organize_name like", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameNotLike(String value) {
            addCriterion("organize_name not like", value, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameIn(List<String> values) {
            addCriterion("organize_name in", values, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameNotIn(List<String> values) {
            addCriterion("organize_name not in", values, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameBetween(String value1, String value2) {
            addCriterion("organize_name between", value1, value2, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeNameNotBetween(String value1, String value2) {
            addCriterion("organize_name not between", value1, value2, "organizeName");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeIsNull() {
            addCriterion("organize_type is null");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeIsNotNull() {
            addCriterion("organize_type is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeEqualTo(Byte value) {
            addCriterion("organize_type =", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeNotEqualTo(Byte value) {
            addCriterion("organize_type <>", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeGreaterThan(Byte value) {
            addCriterion("organize_type >", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("organize_type >=", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeLessThan(Byte value) {
            addCriterion("organize_type <", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeLessThanOrEqualTo(Byte value) {
            addCriterion("organize_type <=", value, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeIn(List<Byte> values) {
            addCriterion("organize_type in", values, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeNotIn(List<Byte> values) {
            addCriterion("organize_type not in", values, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeBetween(Byte value1, Byte value2) {
            addCriterion("organize_type between", value1, value2, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("organize_type not between", value1, value2, "organizeType");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelIsNull() {
            addCriterion("organize_level is null");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelIsNotNull() {
            addCriterion("organize_level is not null");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelEqualTo(Byte value) {
            addCriterion("organize_level =", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelNotEqualTo(Byte value) {
            addCriterion("organize_level <>", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelGreaterThan(Byte value) {
            addCriterion("organize_level >", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelGreaterThanOrEqualTo(Byte value) {
            addCriterion("organize_level >=", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelLessThan(Byte value) {
            addCriterion("organize_level <", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelLessThanOrEqualTo(Byte value) {
            addCriterion("organize_level <=", value, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelIn(List<Byte> values) {
            addCriterion("organize_level in", values, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelNotIn(List<Byte> values) {
            addCriterion("organize_level not in", values, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelBetween(Byte value1, Byte value2) {
            addCriterion("organize_level between", value1, value2, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andOrganizeLevelNotBetween(Byte value1, Byte value2) {
            addCriterion("organize_level not between", value1, value2, "organizeLevel");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdIsNull() {
            addCriterion("parent_organize_id is null");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdIsNotNull() {
            addCriterion("parent_organize_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdEqualTo(Long value) {
            addCriterion("parent_organize_id =", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdNotEqualTo(Long value) {
            addCriterion("parent_organize_id <>", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdGreaterThan(Long value) {
            addCriterion("parent_organize_id >", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdGreaterThanOrEqualTo(Long value) {
            addCriterion("parent_organize_id >=", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdLessThan(Long value) {
            addCriterion("parent_organize_id <", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdLessThanOrEqualTo(Long value) {
            addCriterion("parent_organize_id <=", value, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdIn(List<Long> values) {
            addCriterion("parent_organize_id in", values, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdNotIn(List<Long> values) {
            addCriterion("parent_organize_id not in", values, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdBetween(Long value1, Long value2) {
            addCriterion("parent_organize_id between", value1, value2, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andParentOrganizeIdNotBetween(Long value1, Long value2) {
            addCriterion("parent_organize_id not between", value1, value2, "parentOrganizeId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("`status` is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("`status` is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("`status` =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("`status` <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("`status` >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("`status` >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("`status` <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("`status` <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("`status` in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("`status` not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("`status` between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("`status` not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNull() {
            addCriterion("creator_id is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIsNotNull() {
            addCriterion("creator_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorIdEqualTo(Long value) {
            addCriterion("creator_id =", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotEqualTo(Long value) {
            addCriterion("creator_id <>", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThan(Long value) {
            addCriterion("creator_id >", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdGreaterThanOrEqualTo(Long value) {
            addCriterion("creator_id >=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThan(Long value) {
            addCriterion("creator_id <", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdLessThanOrEqualTo(Long value) {
            addCriterion("creator_id <=", value, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdIn(List<Long> values) {
            addCriterion("creator_id in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotIn(List<Long> values) {
            addCriterion("creator_id not in", values, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdBetween(Long value1, Long value2) {
            addCriterion("creator_id between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreatorIdNotBetween(Long value1, Long value2) {
            addCriterion("creator_id not between", value1, value2, "creatorId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andModifierIdIsNull() {
            addCriterion("modifier_id is null");
            return (Criteria) this;
        }

        public Criteria andModifierIdIsNotNull() {
            addCriterion("modifier_id is not null");
            return (Criteria) this;
        }

        public Criteria andModifierIdEqualTo(Long value) {
            addCriterion("modifier_id =", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotEqualTo(Long value) {
            addCriterion("modifier_id <>", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdGreaterThan(Long value) {
            addCriterion("modifier_id >", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdGreaterThanOrEqualTo(Long value) {
            addCriterion("modifier_id >=", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdLessThan(Long value) {
            addCriterion("modifier_id <", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdLessThanOrEqualTo(Long value) {
            addCriterion("modifier_id <=", value, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdIn(List<Long> values) {
            addCriterion("modifier_id in", values, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotIn(List<Long> values) {
            addCriterion("modifier_id not in", values, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdBetween(Long value1, Long value2) {
            addCriterion("modifier_id between", value1, value2, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifierIdNotBetween(Long value1, Long value2) {
            addCriterion("modifier_id not between", value1, value2, "modifierId");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNull() {
            addCriterion("modify_time is null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIsNotNull() {
            addCriterion("modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifyTimeEqualTo(Date value) {
            addCriterion("modify_time =", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotEqualTo(Date value) {
            addCriterion("modify_time <>", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThan(Date value) {
            addCriterion("modify_time >", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("modify_time >=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThan(Date value) {
            addCriterion("modify_time <", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeLessThanOrEqualTo(Date value) {
            addCriterion("modify_time <=", value, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeIn(List<Date> values) {
            addCriterion("modify_time in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotIn(List<Date> values) {
            addCriterion("modify_time not in", values, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeBetween(Date value1, Date value2) {
            addCriterion("modify_time between", value1, value2, "modifyTime");
            return (Criteria) this;
        }

        public Criteria andModifyTimeNotBetween(Date value1, Date value2) {
            addCriterion("modify_time not between", value1, value2, "modifyTime");
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