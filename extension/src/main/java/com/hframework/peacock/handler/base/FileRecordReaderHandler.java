package com.hframework.peacock.handler.base;

import com.google.common.collect.Lists;
import com.hframework.common.util.message.JsonUtils;
import com.hframework.smartweb.APIErrorType;
import com.hframework.smartweb.APIResultVO;
import com.hframework.smartweb.annotation.Handler;
import com.hframework.smartweb.annotation.SmartParameter;
import com.hframework.smartweb.bean.SmartHandler;
import com.hframework.smartweb.bean.handler.AbstractSmartHandler;
import com.hframework.smartweb.exception.SmartHandlerException;
import org.h2.store.fs.FileUtils;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangquanhong on 2017/2/21.
 */
@Controller
@Handler(path = "/base/file_record_reader", owners = {"2"})
public class FileRecordReaderHandler extends AbstractSmartHandler implements SmartHandler {

    @Handler(version ="1.0.0", description = "读取CSV文件内容", owners = "")
    public void csvRecordRead(
            @SmartParameter(required = true) String filePath,
            HttpServletResponse response) {
        if(!FileUtils.exists(filePath)) {
            throw new SmartHandlerException(APIErrorType.RECODE_IS_NOT_EXISTS);
        }
        try {
            response.setHeader("Content-type", "charset=UTF-8");
            copyToResponse(filePath, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    private static void copyToResponse(String source, ServletOutputStream dest)
            throws IOException {
        InputStream input = null;
        APIResultVO apiResultVO = APIResultVO.success("JSON_BODY");

        String string = JsonUtils.writeValueAsString(apiResultVO);
        dest.write(string.substring(0, string.indexOf("\"JSON_BODY\"")).getBytes());
        dest.write("<![CDATA[".getBytes());
        try {
            input = new FileInputStream(source);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                dest.write(buf, 0, bytesRead);
            }
            dest.write("]]>".getBytes());
            dest.write(string.substring(string.indexOf("\"JSON_BODY\"") + "\"JSON_BODY\"".length()).getBytes());
        } finally {
            input.close();
            dest.close();
        }
    }
}
