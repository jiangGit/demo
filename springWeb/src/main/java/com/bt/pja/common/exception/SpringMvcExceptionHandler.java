package com.bt.pja.common.exception;


import com.bt.pja.common.result.FbResult;
import com.bt.pja.common.result.ResultCodes;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * 全局异常处理
 */
@Component
public class SpringMvcExceptionHandler implements HandlerExceptionResolver {
    private static final Logger LOG = LoggerFactory.getLogger(SpringMvcExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj, Exception e) {
        List<String> params = new ArrayList<>();
        // 遍历并取出抛出异常方法的参数
        Enumeration<?> attr = request.getParameterNames();
        if (attr != null) {
            while (attr.hasMoreElements()) {
                Object o = attr.nextElement();
                String paramName = (String) o;
                String paramValue = request.getParameter(paramName);
                params.add(String.format("%s:%s", paramName, paramValue));
            }
        }
        // request里取出来的参数顺序是不固定的
        Collections.sort(params);
        if (filterWarn(e)) {
            LOG.warn(String.format("[%s] params(%s)", obj, StringUtils.join(params, ",")), e);
        } else {
            LOG.error(String.format("[%s] params(%s)", obj, StringUtils.join(params, ",")), e);
        }
        response.setStatus(500);// 系统错误,返回500
        return FbResult.ERROR(ResultCodes.EXCEPTION).toView("", 500, response);
    }

    // 有些报错没必要记到error里
    private boolean filterWarn(Exception e) {
        if (e != null) {
            String msg = e.getMessage();
            if (msg != null) {
                if (msg.contains("Session already invalidated")) {
                    return true;
                }
                if (msg.contains("Broken pipe")) {
                    return true;
                }
                if (msg.contains("Error reading PNG image data")) {
                    return true;
                }
                if (msg.contains("Unexpected EOF read on the socket")) {
                    return true;
                }
                if (msg.contains("Stream ended unexpectedly")) {
                    return true;
                }
            }
        }
        return false;
    }
}
