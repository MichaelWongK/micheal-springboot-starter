package com.micheal.application.example.service.impl;

import com.micheal.application.annotation.RedisLock;
import com.micheal.application.example.service.ExampleService;
import com.micheal.common.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/20 14:06
 * @Description
 */
@Service
public class ExampleServiceImpl implements ExampleService {

    private static final Logger logger = LoggerFactory.getLogger(ExampleServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @RedisLock(key = "lockTest")
    public String order() {
        logger.info("lockTest service.......");
        redisService.set("test11", "test112",Duration.ofMillis(60000));
        return "orderTest Ok";
    }
}
