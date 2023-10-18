package com.qiuyu.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class RequestContext {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes ra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return ra != null ? ra.getRequest() : null;
    }

    public static String getHeaderOrDefault(String headerName, String defaultHeader) {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String header = request.getHeader(headerName);
            return StringUtils.isNotEmpty(header) ? header : defaultHeader;
        }
        return defaultHeader;
    }

    public static String getUsername(){
        return getHeaderOrDefault("X-username","null");
    }
}
