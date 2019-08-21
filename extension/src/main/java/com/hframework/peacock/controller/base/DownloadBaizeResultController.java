package com.hframework.peacock.controller.base;

import com.hframework.common.frame.ServiceFactory;
import com.hframework.peacock.handler.base.FileRecordReaderHandler;
import com.hframework.smartweb.annotation.SmartApi;
import com.hframework.smartweb.annotation.SmartHolder;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.checker.GenericAuthChecker;
import com.hframework.peacock.handler.base.FileRecordReaderHandler;
import com.hframework.peacock.parser.ConfigurationParser;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * Created by zhangquanhong on 2017/6/12.
 */
@Controller
@SmartApi("/base")
public class DownloadBaizeResultController {
    private static final String FILE_ROOT_PATH = "D:\\data";

    /**
     * http://localhost:8080/base/baize/download?taskCode=usercategroy&taskCycle=2017012343&clientId=50003&sign=6797D9365A6304ECFE71D0DA82449CEC
     * @param taskCode
     * @param taskCycle
     * @param response
     */
    @SmartApi(path = "/baize/download", version = "1.0.0", name = "白泽离线任务数据下载", description = "这是第一个接口",checker = GenericAuthChecker.class, owners = "2")
    public void download(
            @SmartParameter(required = true, description = "任务编码") String taskCode,
            @SmartParameter(required = true, description = "任务周期") String taskCycle,
            @SmartHolder(name="extension", defaultValue = "csv", description = "文件后缀", options = {"csv","txt"}, parser = ConfigurationParser.class) String extension,
            HttpServletResponse response){
        ServiceFactory.getService(FileRecordReaderHandler.class).
                csvRecordRead(FILE_ROOT_PATH + File.separatorChar + taskCode + File.separatorChar + taskCycle + "." + extension, response);
    }
}
