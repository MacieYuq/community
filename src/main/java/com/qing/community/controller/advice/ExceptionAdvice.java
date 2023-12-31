package com.qing.community.controller.advice;

import com.qing.community.utils.CommunityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器异常" + e.getMessage());
        //从异常数组中获取所有异常并记录到日志中
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        //从request获取是否是异步请求
        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)) {//是异步请求
            response.setContentType("application/plain;charset=utf-8");//返回json字符串
            //输出到页面
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器发生异常"));
        }else {//否则重定向至错误页面
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
