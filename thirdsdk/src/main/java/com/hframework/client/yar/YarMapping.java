package com.hframework.client.yar;

public enum YarMapping {
    INVEST_RECORDS(YarConfig.INVEST_RECORDS, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getInvestRecord",
            "\\NCFGroup\\Protos\\Ptp\\RequestInvestRecord"),

    DEAL_DETAIL(YarConfig.DEAL_DETAIL, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getDealDetailByDealId",
            "\\NCFGroup\\Protos\\Ptp\\RequestDealInfo"),

    USEFULL_DEALS(YarConfig.USEFULL_DEALS, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getUsefulDeals",
            "\\NCFGroup\\Common\\Extensions\\Base\\SimpleRequestBase"),

    ACHIEVE_CLIENT(YarConfig.ACHIEVE_CLIENT, "\\NCFGroup\\Ptp\\services\\PtpPerformance", "getDaysInvestStat",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),

    ACHIEVE_HOME_PAGE(YarConfig.ACHIEVE_HOME_PAGE, "\\NCFGroup\\Ptp\\services\\PtpPerformance", "getSummary",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),

    FP_LOGIN(YarConfig.FP_LOGIN, "\\NCFGroup\\Ptp\\services\\PtpUser", "login",
            "\\NCFGroup\\Protos\\Ptp\\RequestUserLogin"),

    P2P_BONUS(YarConfig.P2P_BONUS, "\\NCFGroup\\Ptp\\services\\PtpBonus", "buy",
            "\\NCFGroup\\Protos\\Ptp\\RequestBonusBuy"),
    P2P_BONUS_AND_GIVE(YarConfig.P2P_BONUS_AND_GIVE, "\\NCFGroup\\Ptp\\services\\PtpBonus", "buyDirectPush",
            "\\NCFGroup\\Protos\\Ptp\\RequestBonusBuyDirectPush"),
    P2P_GET_BONUS_USE_INFO(YarConfig.P2P_GET_BONUS_USE_INFO, "\\NCFGroup\\Ptp\\services\\PtpBonus", "getUsedByOrder",
            "\\NCFGroup\\Protos\\Ptp\\RequestBonusGetByOrder"),

    P2P_GET_BONUS_USER_INFO(YarConfig.P2P_GET_BONUS_USER_INFO, "\\NCFGroup\\Bonus\\Services\\BonusRPC", "getBonusUserInfo",
            "xxxx"),

    P2P_GET_BONUS_USER_INFOLIST(YarConfig.P2P_GET_BONUS_USER_INFOLIST, "\\NCFGroup\\Bonus\\Services\\BonusRPC", "getBonusLog",
            "xxxx"),
    P2P_GET_BONUS_USE_DETAIL(YarConfig.P2P_GET_BONUS_USE_DETAIL, "\\NCFGroup\\Ptp\\services\\PtpBonus", "getUsedDetailByOrder",
            "\\NCFGroup\\Protos\\Ptp\\RequestBonusGetByOrder"),

    UPLOAD_USER_AVATAR(YarConfig.UPLOAD_USER_AVATAR, "\\NCFGroup\\Ptp\\services\\PtpImg", "uploadUserAavatar",
            "xxxx"),
    UPLOAD_USER_PIC(YarConfig.UPLOAD_USER_PIC, "\\NCFGroup\\Ptp\\services\\PtpImg", "uploadViaBin",
            "xxxx"),

    CFPINFO(YarConfig.CFPINFO, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCfpInfo",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),
    CFPINFOBYID(YarConfig.CFPINFOBYID, "\\NCFGroup\\Ptp\\services\\PtpUser", "getUserInfoById",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),

    CFPUSERBYP2P(YarConfig.CFPUSERBYP2P, "\\NCFGroup\\Ptp\\services\\PtpUser", "getUidByToken","xxxx"),

    WITHDRAW(YarConfig.WITHDRAW, "\\NCFGroup\\Ptp\\services\\PtpPayment", "cashOutV2",
            "XXXX"),

    CUSTOMERINFO(YarConfig.CUSTOMERINFO, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCustomerInfo",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),

    EMPTY_CUSTOMER_LIST(YarConfig.EMPTY_CUSTOMER_LIST, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCustomers",
            "\\NCFGroup\\Protos\\Ptp\\RequestCustomers"),

    CUSTOMERS_LIST(YarConfig.CUSTOMERS_LIST, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getLatestRepayCustomers",
            "\\NCFGroup\\Protos\\Ptp\\RequestCustomers"),

    CUSTOMER_ANALYSE(YarConfig.CUSTOMER_ANALYSE, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCustomerInfo",
            "\\NCFGroup\\Protos\\Ptp\\RequestUser"),

    INVEST_ANALYSE(YarConfig.INVEST_ANALYSE, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getInvestAnalyse",
            "\\NCFGroup\\Protos\\Ptp\\RequestInvestAnalyse"),

    GETCUSTOMERSINFO(YarConfig.GETCUSTOMERSINFO, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCustomersInfo",
            ""),


    SEARCH_CUSTOMERS(YarConfig.SEARCH_CUSTOMERS, "\\NCFGroup\\Ptp\\services\\PtpCfp", "searchCfpCustomers",
            "\\NCFGroup\\Protos\\Ptp\\RequestSearchCustomers"),

    CUSTOMER_COMMISSION(YarConfig.CUSTOMER_COMMISSION, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCommissions",
            "\\NCFGroup\\Protos\\Ptp\\RequestCommissions"),


    PUSH_SIGNIN(YarConfig.PUSH_SIGNIN, "\\NCFGroup\\Ptp\\services\\PtpPush", "signIn",
            "\\NCFGroup\\Protos\\Ptp\\RequestPushSignIn"),

    PUSH_SIGNOUT(YarConfig.PUSH_SIGNOUT, "\\NCFGroup\\Ptp\\services\\PtpPush", "signOut",
            "\\NCFGroup\\Protos\\Ptp\\RequestPushSignOut"),

    PUSH_TO_SINGLE(YarConfig.PUSH_TO_SINGLE, "\\NCFGroup\\Ptp\\services\\PtpPush", "toSingle",
            "\\NCFGroup\\Protos\\Ptp\\RequestPushToSingle"),

    PUSH_TO_ALL(YarConfig.PUSH_TO_ALL, "\\NCFGroup\\Ptp\\services\\PtpPush", "toAll",
            "\\NCFGroup\\Protos\\Ptp\\RequestPushToAll"),
    REPAY_CALENDAR_YEAR(YarConfig.REPAY_CALENDAR_YEAR, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getRepayCalendarByYear","xxxx"),
    REPAY_CALENDAR_MONTH(YarConfig.REPAY_CALENDAR_MONTH, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getRepayCalendarByMonth","xxxx"),
    REPAY_CALENDAR_DAY(YarConfig.REPAY_CALENDAR_DAY, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getRepayCalendarByDay","xxxx"),
    TICKET_RECEIVE(YarConfig.TICKET_RECEIVE, "\\NCFGroup\\O2O\\Services\\Discount", "acquireDiscount","xxxx"),
    TICKET_GIVE(YarConfig.TICKET_GIVE, "\\NCFGroup\\O2O\\Services\\Discount", "giveDiscount","xxxx"),
    TICKET_AMOUNT(YarConfig.TICKET_AMOUNT, "\\NCFGroup\\O2O\\Services\\Discount", "getUserGivenDiscountCount","xxxx"),
    TICKET_AMOUNT_UNUSED(YarConfig.TICKET_AMOUNT_UNUSED, "\\NCFGroup\\O2O\\Services\\Discount", "getUserUnusedDiscountCountForCFP","xxxx"),
    TICKET_LIST(YarConfig.TICKET_LIST, "\\NCFGroup\\O2O\\Services\\Discount", "getUserGivenDiscountList","xxxx"),
    TICKET_LIST_GIVE(YarConfig.TICKET_LIST_GIVE, "\\NCFGroup\\Ptp\\services\\PtpO2O", "giveDiscounts","xxxx"),
    TICKET_HISTORY(YarConfig.TICKET_HISTORY, "\\NCFGroup\\O2O\\Services\\Discount", "getUserGivenLogList","xxxx"),
    TICKET_USEDLIST(YarConfig.TICKET_USEDLIST, "\\NCFGroup\\O2O\\Services\\Discount", "getUserDiscountListForCFP","xxxx"),
    TICKET_TIMELIST(YarConfig.TICKET_TIMELIST, "\\NCFGroup\\O2O\\Services\\Discount", "getUserGivenTimeList","xxxx"),
    VIP_INFO(YarConfig.VIP_INFO, "\\NCFGroup\\Ptp\\services\\PtpVip", "getVipInfo","xxxx"),
    VIP_INFOLSIT(YarConfig.VIP_INFOLSIT, "\\NCFGroup\\Ptp\\services\\PtpVip", "getVipUserList","xxxx"),

    TICKET__DISCOUNTHEXIDS(YarConfig.TICKET__DISCOUNTHEXIDS, "\\NCFGroup\\Ptp\\services\\PtpMarketing", "getDiscountHexIds","xxxx"),
    TICKET_CONDISCOUNTHEXIDS(YarConfig.TICKET_CONDISCOUNTHEXIDS, "\\NCFGroup\\Ptp\\services\\PtpMarketing", "convertSnToIds","xxxx"),
    CUSTOMERS_BY_CONDITION(YarConfig.CUSTOMERS_LIST_BY_CONDITION, "\\NCFGroup\\Ptp\\services\\PtpCfp", "getCustomersByCondition","xxxx");



    public String url;
    public String service;
    public String method;
    public String proto;

    YarMapping(String url, String service, String method, String proto) {
        this.url = url;
        this.service = service;
        this.method = method;
        this.proto = proto;
    }

    public static YarMapping getMapping(String url) {
        YarMapping[] values = YarMapping.values();
        for (YarMapping mapping : values) {
            if (mapping.url.equals(url)) {
                return mapping;
            }
        }
        return null;
    }
}
