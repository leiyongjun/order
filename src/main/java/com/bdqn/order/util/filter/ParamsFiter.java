package com.bdqn.order.util.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "paramsFilter",urlPatterns = "/*")
public class ParamsFiter implements Filter {
    Logger logger = LoggerFactory.getLogger(ParamsFiter.class.getName());

    public void init(FilterConfig fitlerConfg){
        logger.info("过滤器初始化");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 过滤前做的事情
        String url = ((HttpServletRequest)servletRequest).getRequestURI();
        logger.info("开始过滤了: " + url);
        // 对传入参数做校验
        Map<String, String[]> map = servletRequest.getParameterMap();
        for ( String key : map.keySet() ) {
            if("or".equals(map.get(key)[0])){
                logger.error("请求" + url + "中的非法参数: " + map.get(key));
                throw new IOException("非法参数");
                //return;
            }
        }
        // 放行, 调用具体的方法
        filterChain.doFilter(servletRequest, servletResponse);
        // 过滤后做的事情
        logger.info("完成过滤了: " + url);
    }

    public void destroy() {
        System.out.println("我是过滤器的被销毁时调用的方法！，活不下去了................" );
    }
}
