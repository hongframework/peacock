package com.hframework.peacock.handler.base;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.hframework.common.springext.properties.PropertyConfigurerUtils;
import com.hframework.smartsql.client2.DBClient;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/user_avatar", owners = {"1"})
public class UserAvatarHandler extends AbstractSmartHandler implements SmartHandler {

        private String p2pStaticDomain = PropertyConfigurerUtils.getProperty("domain.p2p.static");
        private String lcsDomain= PropertyConfigurerUtils.getProperty("domain.lcs");

        private static final Logger logger = LoggerFactory.getLogger(UserAvatarHandler.class);

        private static final String SQL_QUERY_USER_IMAGE = "SELECT\n"+
                                                                "  usr.mobile       AS mobile,\n"+
                                                                "  image.attachment AS attachment\n"+
                                                                "FROM firstb2b_user AS usr\n"+
                                                                "  INNER JOIN firstb2b_user_image image\n"+
                                                                "    ON usr.id = image.user_id\n"+
                                                                "WHERE usr.mobile IN('?');";
        private static final String SQL_QUERY_WEIXIN_INFO = "SELECT\n" +
                                                                "  bind.mobile    AS mobile,\n" +
                                                                "  info.openid,\n" +
                                                                "  info.user_info\n" +
                                                                "FROM firstb2b_bonus_bind AS bind\n" +
                                                                "  INNER JOIN firstb2b_weixin_info info\n" +
                                                                "    ON bind.openid = info.openid\n" +
                                                                "WHERE bind.mobile IN('?')\n" +
                                                                "ORDER BY info.update_time;";

        @Handler(version = "1.0.0", description = "获取用户头像")
        public String getAvatar(
                @SmartParameter(required = true, description = "密文手机号") String mobile) {
                return getAvatar(new String[]{mobile}).get(0);
        }

        @Handler(version ="1.0.0", description = "获取用户头像", batch = true)
        public List<String> getAvatar(String[] mobiles) {
                Set<String> distinctMobiles = Sets.newHashSet(mobiles);
                Map<String, String> imageMap = new LinkedHashMap<>();
                DBClient.setCurrentDatabaseKey("firstb2b");
                List<Map<String, Object>> maps = DBClient.executeQueryMaps(SQL_QUERY_USER_IMAGE.replaceAll("\\?", Joiner.on("', '").join(distinctMobiles)), new Object[0]);
                for (Map<String, Object> map : maps) {
                        String attachment = String.valueOf(map.get("attachment"));
                        if(StringUtils.isNoneBlank(attachment) && !attachment.toLowerCase().startsWith("http")) {
                                attachment = p2pStaticDomain + "/" + attachment;
                        }
                        imageMap.put(String.valueOf(map.get("mobile")), attachment);
                }
                distinctMobiles.removeAll(imageMap.keySet());
                maps = DBClient.executeQueryMaps(SQL_QUERY_WEIXIN_INFO.replaceAll("\\?", Joiner.on("', '").join(distinctMobiles)), new Object[0]);
                for (Map<String, Object> map : maps) {
                        String userInfoJson = String.valueOf(map.get("user_info"));
                        String img = null;
                        try{
                                JSONObject jsonObject = (JSONObject) JSONObject.parse(userInfoJson);
                                img = jsonObject.getString("headimgurl");
                        }catch (Exception e) {
                                try{
                                        logger.error("解析出错,JSON:" + userInfoJson, e);
                                        JSONObject jsonObject = (JSONObject) JSONObject.parse(userInfoJson.replaceAll("\\\\","\\\\\\\\"));
                                        img = jsonObject.getString("headimgurl");
                                }catch (Exception e1) {
                                        logger.error("解析出错,JSON:" + userInfoJson.replaceAll("\\\\","\\\\\\\\"), e1);
                                }
                        }
                        if(StringUtils.isNoneBlank(img)) {
                                imageMap.put(String.valueOf(map.get("mobile")), img);
                        }
                }

                List<String> result = new ArrayList<>();
                for (String mobile : mobiles) {
                        if(imageMap.containsKey(mobile)) {
                                result.add(imageMap.get(mobile));
                        }else {
                                result.add(lcsDomain +"/static/images/default_avatar@2x.png");
                        }
                }

                return result;
        };

        public static void main(String[] args) {
                UserAvatarHandler handler = new UserAvatarHandler();
                handler.getAvatar(new String[]{"of+UNVieoIETu7t4jFTTUg=="});
        }

}
