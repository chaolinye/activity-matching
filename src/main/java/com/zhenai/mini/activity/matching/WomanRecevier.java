package com.zhenai.mini.activity.matching;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author chaolinye
 * @since 2017/8/14
 */
@Component
@RabbitListener(queues = "goodnight_woman")
public class WomanRecevier {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("add woman  : " + msg);
        try {
            Matcher.womanQueue.put(msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
