package com.zhenai.mini.activity.matching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.*;

/**
 * @author chaolinye
 * @since 2017/8/10
 */
@Component
public class Matcher extends Thread{
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Resource(name="redisTemplate")
    private  SetOperations<String,String> setOperations;
    @Resource(name="redisTemplate")
    private HashOperations<String,String,String> hashOperations;

    private final String MATCH_STRING="mini:activity:goodnight:match";

    private final String PAIR_STRING="mini:activity:goodnight:pair";

    private final String MAN_QUEUE="goodnight_man";
    private final String WOMAN_QUEUE="goodnight_woman";

    private Logger logger= LoggerFactory.getLogger(Matcher.class);

    // the queue of man
    public static LinkedBlockingQueue<String> manQueue=new LinkedBlockingQueue<>();
    // the queue of woman
    public static LinkedBlockingQueue<String> womanQueue=new LinkedBlockingQueue<>();


    @Override
    public void run() {
        for(;;) {
            String manId=null;
            try{
                manId=manQueue.take();
            }catch (InterruptedException e) {
                logger.error("manQueue take interrupt exception={}", e);
            }
            if(manId==null) continue;
            if(!setOperations.isMember(MATCH_STRING,manId)){
                continue;
            }
            String womanId=null;
            try{
                womanId=womanQueue.poll(1,TimeUnit.SECONDS);
            }catch (InterruptedException e){
                logger.error("womanQueue poll interrupt exception={}",e);
            }
            if(womanId==null||!setOperations.isMember(MATCH_STRING,womanId)){
                rabbitTemplate.convertAndSend(MAN_QUEUE, manId);
                logger.info("send man id={}",manId);
            }else{
                if(!setOperations.isMember(MATCH_STRING,manId)){
                    rabbitTemplate.convertAndSend(WOMAN_QUEUE, womanId);
                }else{
                    hashOperations.put(PAIR_STRING,manId,womanId);
                    hashOperations.put(PAIR_STRING,womanId,manId);
                    setOperations.remove(MATCH_STRING,manId);
                    setOperations.remove(MATCH_STRING,womanId);
                    logger.info("matching success man_id={} woman_id={}",manId,womanId);
                }
            }
        }
    }
}
