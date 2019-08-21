package com.hframework.smartweb.examples;

import com.google.common.collect.Lists;
import com.hframework.smartweb.annotation.*;
import com.hframework.smartweb.bean.SmartPattern;
import com.hframework.smartweb.bean.checker.EmailChecker;
import com.hframework.smartweb.bean.checker.GenericAuthChecker;
import com.hframework.smartweb.bean.checker.MoneyChecker;
import com.hframework.smartweb.bean.formatter.DateFormatter;
import com.hframework.smartweb.bean.formatter.MoneyFormatter;
import com.hframework.smartweb.bean.parser.DateSmartParser;
import com.hframework.smartweb.bean.parser.P2pIdSmartParser;
import com.hframework.common.util.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/24.
 */
@Controller
@SmartApi("/examples")
public class SmartDemoController {



    /**
     * http请求校验
     * 请求示例：http://localhost:8080/smart/parameter?token=123&beginDate=20131212&endDate=20131212&bankNo=1211111111111111&money=12.12&email=zqhget1@163.com&gender=1&age=10&cycle=day
     * @return
     */
    @SmartApi(path = "parameter", version = "1.0.1", name = "请求参数示例", owners = "3")
    public String parameterCheckDemo(
            @SmartParameter(name="token", required = true, description = "TOKEN", parser = P2pIdSmartParser.class) Integer p2pId,
            @SmartParameter(required = true, parser = DateSmartParser.class, pattern = SmartPattern.YYYYMMDD) Date beginDate,
            @SmartParameter(required = true, pattern = SmartPattern.YYYYMMDD) Date endDate,
            @SmartParameter(required = true, regex = "^(\\d{16}|\\d{19})$") String bankNo,
            @SmartParameter(required = true, checker = MoneyChecker.class, pattern = SmartPattern.IsNormalMoney) BigDecimal money,
            @SmartParameter(required = true, checker = EmailChecker.class) String email,
            @SmartParameter(options ={"0","1"}) String gender,
            @SmartParameter( min = 0, max = 300) int age,
            @SmartParameter(defaultValue = "today", enums = Cycle.class) String cycle,
            @SmartParameter(defaultValue = "1", min = 1) Integer pageNo ,
            @SmartParameter(defaultValue = "20", min = 1) Integer pageSize,
            @SmartParameter() String lastRequestTime){
        return "SUCCESS";
    }
    /**
     * 请求示例：http://localhost:8080/smart/response/exclude
     * */
    @SmartApi(path = "response/exclude", version = "1.0.1", name = "请求结果实例（exclude)", owners = "3")
    @SmartResult(value = {
            @Result(attr = "id", alias = "userId"),
            @Result(attr = "name", alias = "userName"),
            @Result(attr = "age"),
            @Result(attr = "address"),
            @Result(attr = "createTime", formatter = DateFormatter.class, pattern = SmartPattern.YYYY_MM_DD),
            @Result(attr = "balance", alias = "userBalance", formatter = MoneyFormatter.class),
            @Result(attr = "tags", alias = "tags"),
            @Result(attr = "investList", alias = "invests", values = {
                    @SubResult(attr = "id", alias = "investId"),
                    @SubResult(attr = "name", alias = "investName"),
            }),
    }, expand = {
            @Expand(attr = "id", newAttr = "avatar", handler = QueryUserBaseInfoHandler.class),
            @Expand(attr = "id", newAttr = {"realName","idno","bankNo","bank","bankIcon","money","uid"}, handler = QueryUserBaseInfoHandler.class)
    }, exclude = {"ext1","ext2"})
    public User responseResultIgnoreDemo() throws ParseException {
        return getDemoUser();
    }

   /**
     * 请求示例：http://localhost:8080/smart/response/include
     * */
    @SmartApi(path = "response/include", version = "1.0.1", name = "请求结果实例（include)", owners = "3")
    @SmartResult(value = {
            @Result(attr = "id", alias = "userId"),
            @Result(attr = "name", alias = "userName"),
            @Result(attr = "age"),
            @Result(attr = "address"),
            @Result(attr = "createTime", formatter = DateFormatter.class, pattern = SmartPattern.YYYY_MM_DD),
            @Result(attr = "balance", alias = "userBalance", formatter = MoneyFormatter.class),
            @Result(attr = "tags", alias = "tags"),
            @Result(attr = "investList", alias = "invests", values = {
                    @SubResult(attr = "id", alias = "investId"),
                    @SubResult(attr = "name", alias = "investName"),
            }),
    }, expand = {
            @Expand(attr = "id", newAttr = "avatar", handler = QueryUserBaseInfoHandler.class),
            @Expand(attr = "id", newAttr = {"realName","idno","bankNo","bank","bankIcon","money","uid"}, handler = QueryUserBaseInfoHandler.class),
//            @Expand(attr = {"id","name","age","address"}, newAttr = "overview", handler = MessageFormatHandler.class, patternString="id={0},name={1},age={2},address={3}"),
    }, include = {"ext1","ext2"})
    public User responseResultExtractDemo() throws ParseException {
        return getDemoUser();
    }

    /**
     * 请求示例：http://localhost:8080/smart/precheck?clientId=50003&timestamp=32433234&requestSerialNo=234234323454&sign=FDC5B6FCE24B73136B692B6D2CCEE83A&token=123&beginDate=20131212&endDate=20131212&bankNo=1211111111111111&money=12.12&email=zqhget1@163.com&gender=1&age=10&cycle=day
     * */
    @SmartApi(path = "precheck",checker = GenericAuthChecker.class, version = "1.0.1", name = "前置检查", owners = "3")
    public User checkHeaderDemo(
            @SmartParameter(name="token", required = true, parser = P2pIdSmartParser.class) Integer p2pId,
            @SmartParameter(required = true, parser = DateSmartParser.class, pattern = SmartPattern.YYYYMMDD) Date beginDate,
            @SmartParameter(required = true, pattern = SmartPattern.YYYYMMDD) Date endDate,
            @SmartParameter(required = true, regex = "^(\\d{16}|\\d{19})$") String bankNo,
            @SmartParameter(required = true, checker = MoneyChecker.class, pattern = SmartPattern.IsNormalMoney) BigDecimal money,
            @SmartParameter(required = true, checker = EmailChecker.class) String email,
            @SmartParameter(options ={"0","1"}) String gender,
            @SmartParameter( min = 0, max = 300) int age,
            @SmartParameter(defaultValue = "today", enums = Cycle.class) String cycle,
            @SmartParameter(defaultValue = "1", min = 1) Integer pageNo ,
            @SmartParameter(defaultValue = "20", min = 1) Integer pageSize,
            @SmartParameter() String lastRequestTime) throws ParseException {
        return getDemoUser();
    }


    /**
     * 请求示例：http://localhost:8080/smart/complete?token=123&beginDate=20131212&endDate=20131212&bankNo=1211111111111111&money=12.12&email=zqhget1@163.com&gender=1&age=10&cycle=day
     * */
    @SmartApi(path = "complete",version = "1.0.1", name = "完整实例(One)", owners = "3")
    @SmartResult(value = {
            @Result(attr = "id", alias = "userId"),
            @Result(attr = "name", alias = "userName"),
            @Result(attr = "age"),
            @Result(attr = "address"),
            @Result(attr = "createTime", formatter = DateFormatter.class, pattern = SmartPattern.YYYY_MM_DD),
            @Result(attr = "balance", alias = "userBalance", formatter = MoneyFormatter.class),
            @Result(attr = "tags", alias = "tags"),
            @Result(attr = "investList", alias = "invests", values = {
                    @SubResult(attr = "id", alias = "investId"),
                    @SubResult(attr = "name", alias = "investName"),
            }),
    }, expand = {
            @Expand(attr = {"p2pId","p2pId"}, newAttr = "avatar", handler = QueryUserBaseInfoHandler.class),
            @Expand(attr = {"p2pId","p2pId"}, newAttr = {"realName","idno","bankNo","bank","bankIcon","money","uid"}, handler = QueryUserBaseInfoHandler.class)
    }, exclude = {"ext1","ext2"})
    public User completeDemo(
            @SmartParameter(name="token", required = true, parser = P2pIdSmartParser.class) Integer p2pId,
            @SmartParameter(required = true, parser = DateSmartParser.class, pattern = SmartPattern.YYYYMMDD) Date beginDate,
            @SmartParameter(required = true, pattern = SmartPattern.YYYYMMDD) Date endDate,
            @SmartParameter(required = true, regex = "^(\\d{16}|\\d{19})$") String bankNo,
            @SmartParameter(required = true, checker = MoneyChecker.class, pattern = SmartPattern.IsNormalMoney) BigDecimal money,
            @SmartParameter(required = true, checker = EmailChecker.class) String email,
            @SmartParameter(options ={"0","1"}) String gender,
            @SmartParameter( min = 0, max = 300) int age,
            @SmartParameter(defaultValue = "today", enums = Cycle.class) String cycle,
            @SmartParameter(defaultValue = "1", min = 1) Integer pageNo ,
            @SmartParameter(defaultValue = "20", min = 1) Integer pageSize,
            @SmartParameter() String lastRequestTime) throws ParseException {
        return getDemoUser();
    }

    @SmartApi(path = "completeList",version = "1.0.1", name = "完整实例(List)", owners = "3")
    @SmartResult(value = {
            @Result(attr = "id", alias = "userId"),
            @Result(attr = "name", alias = "userName"),
            @Result(attr = "age"),
            @Result(attr = "address"),
            @Result(attr = "createTime", formatter = DateFormatter.class, pattern = SmartPattern.YYYY_MM_DD),
            @Result(attr = "balance", alias = "userBalance", formatter = MoneyFormatter.class),
            @Result(attr = "tags", alias = "tags"),
            @Result(attr = "investList", alias = "invests", values = {
                    @SubResult(attr = "id", alias = "investId"),
                    @SubResult(attr = "name", alias = "investName"),
            }),
    }, expand = {
            @Expand(attr = {"p2pId","id"}, newAttr = "avatar", handler = QueryUserBaseInfoHandler.class),
            @Expand(attr = {"p2pId","id"}, newAttr = {"realName","idno","bankNo","bank","bankIcon","money","uid"}, handler = QueryUserBaseInfoHandler.class)
    }, exclude = {"ext1","ext2"})
    public List<User> completeListDemo(
            @SmartParameter(name="token", required = true, parser = P2pIdSmartParser.class) Integer p2pId,
            @SmartParameter(required = true, parser = DateSmartParser.class, pattern = SmartPattern.YYYYMMDD) Date beginDate,
            @SmartParameter(required = true, pattern = SmartPattern.YYYYMMDD) Date endDate,
            @SmartParameter(required = true, regex = "^(\\d{16}|\\d{19})$") String bankNo,
            @SmartParameter(required = true, checker = MoneyChecker.class, pattern = SmartPattern.IsNormalMoney) BigDecimal money,
            @SmartParameter(required = true, checker = EmailChecker.class) String email,
            @SmartParameter(options ={"0","1"}) String gender,
            @SmartParameter( min = 0, max = 300) int age,
            @SmartParameter(defaultValue = "today", enums = Cycle.class) String cycle,
            @SmartParameter(defaultValue = "1", min = 1) Integer pageNo ,
            @SmartParameter(defaultValue = "20", min = 1) Integer pageSize,
            @SmartParameter() String lastRequestTime) throws ParseException {
        User demoUser = getDemoUser();
        demoUser.setId(2);
        return Lists.newArrayList(getDemoUser(),demoUser , getDemoUser());
    }


    public User getDemoUser() throws ParseException {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        user.setAge((short) 12);
        user.setAddress("北京市朝阳区霄云路网信大厦B座");
        user.setCreateTime(DateUtils.parseYYYYMMDDHHMMSS("2017-01-23 12:24:56"));
        user.setBalance(new BigDecimal("124612.23"));
        user.setTags(new String[]{"信用良好","高净值","活跃用户","VIP"});
        return user;
    }

    public static class Invest{
        private Long id ;
        private String name;
        private String createTime;
        private BigDecimal amount;
        private Long productId;
        private String productName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
    }

    public static class User{
        private long id;
        private String name;
        private Short age;
        private String address;
        private Date createTime;
        private BigDecimal balance;
        private String[] tags;
        private List<Invest> investList;

        private Date lastLoginTime;
        private String ext1;
        private String ext2;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Short getAge() {
            return age;
        }

        public void setAge(Short age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }

        public String[] getTags() {
            return tags;
        }

        public void setTags(String[] tags) {
            this.tags = tags;
        }

        public String getExt1() {
            return ext1;
        }

        public void setExt1(String ext1) {
            this.ext1 = ext1;
        }

        public String getExt2() {
            return ext2;
        }

        public void setExt2(String ext2) {
            this.ext2 = ext2;
        }

        public Date getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(Date lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public List<Invest> getInvestList() {
            return investList;
        }

        public void setInvestList(List<Invest> investList) {
            this.investList = investList;
        }
    }

    public static enum Cycle{
        day, yesterday, week, month, year
    }
}
