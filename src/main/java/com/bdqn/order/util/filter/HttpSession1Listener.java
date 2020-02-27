package com.bdqn.order.util.filter;

import com.bdqn.order.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
@Component
public class HttpSession1Listener implements HttpSessionListener {
    Logger logger = LoggerFactory.getLogger(HttpSession1Listener.class);

    public void sessionCreated(HttpSessionEvent se) {
        logger.info("session增加");
        SysUtil.ONLINE_COUNT += 1;
    }

    public void sessionDestroyed(HttpSessionEvent se) {
        logger.info("session销毁");
        SysUtil.ONLINE_COUNT -= 1;
    }
}
