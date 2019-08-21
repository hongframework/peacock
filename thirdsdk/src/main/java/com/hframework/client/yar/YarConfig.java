package com.hframework.client.yar;

import com.hframework.common.resource.ResourceWrapper;
import com.hframework.common.resource.annotation.Key;
import com.hframework.common.resource.annotation.Source;
import com.hframework.client.decrypt.DecryptConfig;

import java.lang.reflect.InvocationTargetException;

@Source("third/yar.properties")
public class YarConfig {

    @Key( "third.yar.test_model")
    private String testModel;

    @Key( "third.yar.p2p.domain")
    private String p2pDomain;

    @Key( "third.yar.o2o.domain")
    private String o2oDomain;

    @Key( "third.yar.bonus.domain")
    private String bonusDomain;



    //	public static final String RPC_HOST = "http://wangfei5.p2pbackend.firstb2blocal.com";
    public static final String RPC_HOST = "";
    //	public static final String RPC_HOST = "http://openapi.firstb2b.com";
    public static final String RPC_SECRET = "d4eb3d27b2aab394edabe01e92fefb21";
    public static final String RPC_CLIENTID = "01e592bb43c6b9510ee0951f";

    public static final String REDIRECT_URI = "http://lcstest.firstb2b.com/oauth";
    public static final String LOGIN = RPC_HOST + "/user/login";
    public static final String GETTOKEN = RPC_HOST + "/oauth/getToken";

    public static final String FP_LOGIN = "/fp/login";

    public static final String P2P_BONUS = "P2P_BONUS";
    public static final String P2P_BONUS_AND_GIVE = "P2P_BONUS_AND_GIVE";
    public static final String UPLOAD_USER_AVATAR = "UPLOAD_USER_AVATAR";
    public static final String UPLOAD_USER_PIC= "UPLOAD_USER_PIC";

    public static final String P2P_GET_BONUS_USE_INFO = "P2P_GET_BONUS_USE_INFO";
    public static final String P2P_GET_BONUS_USER_INFOLIST = "P2P_GET_BONUS_USER_INFOLIST";
    public static final String P2P_GET_BONUS_USER_INFO = "P2P_GET_BONUS_USER_INFO";
    public static final String P2P_GET_BONUS_USE_DETAIL = "P2P_GET_BONUS_USE_DETAIL";


    public static final String CFPINFO = RPC_HOST + "/cfp/cfpInfo";
    public static final String CFPINFOBYID = RPC_HOST + "/cfp/cfpInfoById";
    public static final String CFPUSERBYP2P= RPC_HOST + "/cfp/cfpUserByP2P";

    public static final String WITHDRAW = RPC_HOST + "/cfp/withdraw";

    public static final String CUSTOMERINFO = RPC_HOST + "/cfp/customerInfo";
    public static final String CUSTOMERS_LIST = RPC_HOST + "/cfp/latestCustomers";
    public static final String CUSTOMER_ANALYSE = RPC_HOST + "/cfp/customerInfo";
    public static final String EMPTY_CUSTOMER_LIST = RPC_HOST + "/cfp/customers";
    public static final String INVEST_ANALYSE = RPC_HOST + "/cfp/investAnalyse";
    public static final String GETCUSTOMERSINFO = RPC_HOST + "/cfp/getCustomersInfo";
    public static final String ACHIEVE_HOME_PAGE = "/cfp/performance";
    public static final String ACHIEVE_CLIENT = "/cfp/daysInvestStat";
    public static final String USEFULL_DEALS = "/cfp/getUsefulDeals";
    public static final String DEAL_DETAIL = "/cfp/getDealDetailByDealId";
    public static final String INVEST_RECORDS = "/cfp/getInvestRecord";
    public static final String SEARCH_CUSTOMERS = RPC_HOST + "/cfp/searchCustomers";

    public static final String CUSTOMER_COMMISSION = RPC_HOST + "/cfp/commissions";

    public static final String PUSH_SIGNIN=  "/push/signIn";
    public static final String PUSH_SIGNOUT=  "/push/signOut";
    public static final String PUSH_TO_SINGLE=  "/cfp/toSingle";
    public static final String PUSH_TO_ALL=  "/cfp/toAll";

    public static final String CUSTOMERS_LIST_BY_CONDITION= "/fp/customers/list";
    public static final String REPAY_CALENDAR_YEAR= "/fp/repay/calendar/year";
    public static final String REPAY_CALENDAR_MONTH= "/fp/repay/calendar/month";
    public static final String REPAY_CALENDAR_DAY= "/fp/repay/calendar/day";
    public static final String TICKET_RECEIVE= "/fp/ticket/receive";
    public static final String TICKET_GIVE= "/fp/ticket/give";
    public static final String TICKET_AMOUNT= "/fp/ticket/amount";
    public static final String TICKET_AMOUNT_UNUSED= "/fp/ticket/amountUnused";
    public static final String TICKET_LIST= "/fp/ticket/list";
    public static final String TICKET_LIST_GIVE= "/fp/ticket/list/give";
    public static final String TICKET_HISTORY= "/fp/ticket/history";
    public static final String TICKET_USEDLIST= "/fp/ticket/usedlist";
    public static final String TICKET_TIMELIST= "/fp/ticket/timelist";
    public static final String VIP_INFO= "/fp/vip/getVipInfo";
    public static final String VIP_INFOLSIT= "/fp/vip/getVipInfoList";

    public static final String TICKET__DISCOUNTHEXIDS= "/fp/ticket/getDiscountHexIds";
    public static final String TICKET_CONDISCOUNTHEXIDS= "/fp/ticket/convertSnToIds";

    public String getTestModel() {
        return testModel;
    }

    public void setTestModel(String testModel) {
        this.testModel = testModel;
    }

    public String getP2pDomain() {
        return p2pDomain;
    }

    public void setP2pDomain(String p2pDomain) {
        this.p2pDomain = p2pDomain;
    }

    public String getO2oDomain() {
        return o2oDomain;
    }

    public void setO2oDomain(String o2oDomain) {
        this.o2oDomain = o2oDomain;
    }

    public String getBonusDomain() {
        return bonusDomain;
    }

    public void setBonusDomain(String bonusDomain) {
        this.bonusDomain = bonusDomain;
    }

    private static YarConfig instance;

    private YarConfig() {
        super();
    }

    public  static YarConfig getInstance(){
        if(instance == null) {
            synchronized (DecryptConfig.class) {
                if(instance == null) {
                    try {
                        return instance = ResourceWrapper.getResourceBean(YarConfig.class);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    return instance = new YarConfig();
                }
            }
        }
        return instance;
    }

}
