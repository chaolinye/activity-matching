package com.zhenai.mini.activity.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chaolinye
 * @since 2017/8/14
 */
@Configuration
public class RabbitConfig {
    @Bean
    public Queue manQueue(){
        return new Queue("goodnight_man",false,false,false,null);
    }
    @Bean
    public Queue womanQueue(){
        return new Queue("goodnight_woman",false,false,false,null);
    }
}
