package com.hframework.peacock.controller;

import com.hframework.beans.controller.ResultData;
import com.hframework.common.util.collect.CollectionUtils;
import com.hframework.common.util.collect.bean.Grouper;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.exception.SmartHandlerException;
import com.hframework.peacock.config.domain.model.CfgFeature;
import com.hframework.peacock.config.domain.model.CfgFeature_Example;
import com.hframework.peacock.config.domain.model.CfgIndex;
import com.hframework.peacock.config.service.interfaces.ICfgFeatureSV;
import com.hframework.peacock.config.service.interfaces.ICfgIndexSV;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangquanhong on 2017/12/25.
 */
@Controller
@RequestMapping("/extend")
public class PeacockRuleExpressEditController {

    @Resource
    private ICfgIndexSV cfgIndexSV;
    @Resource
    private ICfgFeatureSV cfgFeatureSV;


    @RequestMapping("/cfg_runtime_rule_expressedit.json")
    @ResponseBody
    public ResultData getIndexs(ModelAndView mav){
        try {

            List<CfgIndex> cfgIndexAll = cfgIndexSV.getCfgIndexAll();
            Map<String, List<CfgIndex>> group = CollectionUtils.group(cfgIndexAll, new Grouper<String, CfgIndex>() {
                @Override
                public <K> K groupKey(CfgIndex cfgIndex) {
                    String editFeatures = cfgIndex.getEditFeatures();
                    String varName = "$" + cfgIndex.getCode();
                    String varTitle = cfgIndex.getName();
                    if("6".equals(editFeatures)) {
                        cfgIndex.setRemark("{\"var\":[\"" + varTitle + "\",\"" + varName + "\"],\"symbol\":\"BOOLEAN\",\"value\":[\"NUMBER\", \"\"]}");
                    }else if("7".equals(editFeatures)) {
                        cfgIndex.setRemark("{\"var\":[\"" + varTitle + "\",\"" + varName + "\"],\"symbol\":\"BOOLEAN\",\"value\":[\"NUMBER\", \"MONEY_UNIT\"]}");
                    }else if("8".equals(editFeatures)) {
                        cfgIndex.setRemark("{\"var\":[\"" + varTitle + "\",\"" + varName + "\"],\"symbol\":\"FUTURE-DATE\",\"value\":[\"NUMBER\", \"DATE_UNIT\"]}");
                    }else if("9".equals(editFeatures)) {
                        cfgIndex.setRemark("{\"var\":[\"" + varTitle + "\",\"" + varName + "\"],\"symbol\":\"PASS-DATE\",\"value\":[\"NUMBER\", \"DATE_UNIT\"]}");
                    }else if("10".equals(editFeatures)) {
                        cfgIndex.setRemark("{\"var\":[\"" + varTitle + "\",\"" + varName + "\"],\"symbol\":\"BOOLEAN\",\"value\":[\"NUMBER\", \"\"]}");
                    }

                    return (K) String.valueOf(cfgIndex.getFeatures());
                }
            });

            CfgFeature_Example example = new CfgFeature_Example();
            example.createCriteria();
            // .andTypeEqualTo((byte) 1);
            List<CfgFeature> cfgFeatureListByExample = cfgFeatureSV.getCfgFeatureListByExample(example);
            for (CfgFeature cfgFeature : cfgFeatureListByExample) {
                if(group.containsKey(String.valueOf(cfgFeature.getId()))) {
                    List<CfgIndex> cfgIndexes = group.get(String.valueOf(cfgFeature.getId()));
                    group.put(cfgFeature.getName(), cfgIndexes);
                    group.remove(String.valueOf(cfgFeature.getId()));
                }else {
//                    group.put(cfgFeature.getName(),null);
                }
            }
            mav.addObject("indexs", group);
        }catch (Exception e) {
            e.printStackTrace();
            throw new SmartHandlerException(APIErrorType.SERVER_ERROR);
        }
        return ResultData.success();
    }

}
