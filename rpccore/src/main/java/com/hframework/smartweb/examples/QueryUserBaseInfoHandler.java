package com.hframework.smartweb.examples;

import com.google.common.collect.Lists;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/demo/uerCenter/p2pUser/queryUserBaseInfo",owners = "2")
public class QueryUserBaseInfoHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version ="1.0.0", description = "根据规则查询一条用户")
    public User queryOneUserV100(
            @SmartParameter(required = true) Long p2pId,
            @SmartParameter(required = true) Long userId) {
        return new User(1L,"zhangsan", "张三","52212313213214072", "622621342342432432432","招商银行","xx.png",new BigDecimal("12.234"),1000L,"http://xx.../avatar.jpg");
    }

    @Handler( version ="1.0.0", description = "根据规则查询一条用户", batch = true)
    public List<User> queryOneUserV100(Long[] p2pId, Long[] userId) {
        List result = new ArrayList();
        for (int i = 0; i < p2pId.length; i++) {
            result.add(new User(1L,"zhangsan", "张三","52212313213214072", "622621342342432432432","招商银行","xx.png",new BigDecimal("12.234"),1000L,"http://xx.../avatar.jpg"));
        }
        return result;
    };

    @Handler(version ="1.0.1", description = "根据规则查询一条用户")
    public User queryOneUserV101(@SmartParameter(required = true) Long p2pId, @SmartParameter(required = true) Long userId) {
        return new User(1L,"zhangsan", "张三","52212313213214072", "622621342342432432432","招商银行","xx.png",new BigDecimal("12.234"),1000L,"http://xx.../avatar.jpg");
    }

    @Handler(version ="1.0.1", description = "根据规则查询一条用户", batch = true)
    public List<User> queryOneUserV101(Long[] p2pId, Long[] userId) {
        return Lists.newArrayList( new User(1L,"zhangsan", "张三","52212313213214072", "622621342342432432432","招商银行","xx.png",new BigDecimal("12.234"),1000L,"http://xx.../avatar.jpg"),
                new User(2L,"zhangsan2", "张三2","522123132132140722", "6226213423424324324322","招商银行2","xx.png2",new BigDecimal("12.234"),1000L,"http://xx.../avatar.jpg2"));
    };


    public class User{

        private Long id;
        private String userName;

        private String realName;
        private String idno;
        private String bankNo;
        private String bank;
        private String bankIcon;
        private BigDecimal money;
        private Long uid;

        private String avatar;

        public User() {
        }

        public User(Long id, String userName, String realName, String idno, String bankNo, String bank, String bankIcon, BigDecimal money, Long uid, String avatar) {
            this.id = id;
            this.userName = userName;
            this.realName = realName;
            this.idno = idno;
            this.bankNo = bankNo;
            this.bank = bank;
            this.bankIcon = bankIcon;
            this.money = money;
            this.uid = uid;
            this.avatar = avatar;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getIdno() {
            return idno;
        }

        public void setIdno(String idno) {
            this.idno = idno;
        }

        public String getBankNo() {
            return bankNo;
        }

        public void setBankNo(String bankNo) {
            this.bankNo = bankNo;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankIcon() {
            return bankIcon;
        }

        public void setBankIcon(String bankIcon) {
            this.bankIcon = bankIcon;
        }

        public BigDecimal getMoney() {
            return money;
        }

        public void setMoney(BigDecimal money) {
            this.money = money;
        }

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }


}
