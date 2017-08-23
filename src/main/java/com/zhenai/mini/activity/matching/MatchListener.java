package com.zhenai.mini.activity.matching;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * @author chaolinye
 * @since 2017/8/3
 */
@WebListener
public class MatchListener implements ServletContextListener {
    @Autowired
    private Matcher matcher;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        matcher.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if(matcher!=null&& !matcher.isInterrupted()){
            matcher.interrupt();
        }
    }
}




