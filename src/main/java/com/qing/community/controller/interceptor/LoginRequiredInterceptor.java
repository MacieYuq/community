package com.qing.community.controller.interceptor;

import com.qing.community.annotation.LoginRequired;
import com.qing.community.utils.HostHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断拦截请求的类型
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();//获取方法
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);//获取该方法的loginRequired注解
            if (loginRequired != null && hostHolder.getUser() == null) {//有登录注解，但是没登录
                response.sendRedirect(request.getContextPath() + "/login");//用response重定向到登录界面，controller中的return重定向底层也是用的这个方法
                return false;
            }
        }
        return true;
    }
}
