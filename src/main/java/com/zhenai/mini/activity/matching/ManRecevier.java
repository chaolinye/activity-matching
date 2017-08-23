package com.zhenai.mini.activity.matching;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author chaolinye
 * @since 2017/8/14
 */
@Component
@RabbitListener(queues = "goodnight_man")
public class ManRecevier {
    @RabbitHandler
    public void process(String msg) {
        System.out.println("add man  : " + msg);
        try {
            Matcher.manQueue.put(msg);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
