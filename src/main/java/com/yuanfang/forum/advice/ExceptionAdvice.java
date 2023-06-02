package com.yuanfang.forum.advice;

import com.yuanfang.forum.utils.ForumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)   //表示加了Controller注解的类才会被扫描
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})   //参数表示处理哪些异常类
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.error("服务器发生异常: " + e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            logger.error(element.toString());
        }

        //判断请求的方式是否是异步请求和普通的请求
        String xRequestedWith = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(xRequestedWith)){ //请求的方式是否为异步请求
            response.setContentType("application/plain;charset=utf-8");  //设置响应的数据类型
            PrintWriter writer = response.getWriter();
            writer.write(ForumUtil.getJSONString(1,"服务器异常！"));
        }else{
            response.sendRedirect(request.getContextPath() + "/error");
        }


    }

}
