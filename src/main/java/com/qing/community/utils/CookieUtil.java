package com.qing.community.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.net.http.HttpRequest;

public class CookieUtil {
    public static String getCookieValue(HttpServletRequest request, String name){
        //判空抛异常
        if (request == null || name == null){
            throw new IllegalArgumentException("参数为空");
        }

        //不为空，取得cookie数组，根据name选取目标
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie:cookies){
                if (cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }


        return null;
    }
}
