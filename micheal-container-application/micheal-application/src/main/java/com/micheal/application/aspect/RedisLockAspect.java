package com.micheal.application.aspect;

import com.micheal.application.annotation.RedisLock;
import com.micheal.common.redis.RedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/19 17:05
 * @Description 动态拦截分布式锁
 */
//@DependsOn({"redisService"})
@Aspect
@Component
public class RedisLockAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockAspect.class);

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(com.micheal.application.annotation.RedisLock)")
    public void annotationPointCut() {
        logger.info("11111111111");
    }

    @Around("annotationPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RedisLock annotation = signature.getMethod().getAnnotation(RedisLock.class);
        String key = annotation.key();
        if (key == null || key.length() < 0) {
            key = joinPoint.getTarget().getClass().getName() + signature.getName();
        }
        long startTime = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        Integer spin = annotation.spin();

        while (!lock(key, uuid, annotation)) {
            if (spin > 0) {
                if (logger.isDebugEnabled()) {
                    logger.debug("########## 尝试自旋获取锁:{},剩余次数:{} ##########", key, spin);
                }
                if (lock(key, uuid, annotation)) {
                    break;
                }
                spin--;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("########## 尝试获取锁:{} ##########", key);
                }
                Thread.sleep(annotation.retry());
            }

            if (System.currentTimeMillis() - startTime > annotation.timeoutMills()) {
                throw new RuntimeException("尝试获得分布式锁超时.......");
            }
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("########## {}得到锁:{} ##########",
                        spin < annotation.spin() ? spin > 0 ? "自旋获取" : "重量级获取" : "", key);
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            if (logger.isErrorEnabled()) {
                logger.error("出现异常:{}", e.getMessage());
            }
            throw e;
        } finally {
            String owner = String.valueOf(redisService.get(key));
            if (uuid.equals(owner)) {
                redisService.delete(key);
                if (logger.isDebugEnabled()) {
                    logger.debug("########## 释放锁:{},总耗时:{}ms,{} ##########", key,
                            (System.currentTimeMillis() - startTime));
                }
            }
        }
    }

    private boolean lock(String key, String uuid, RedisLock redisLock) {
        logger.info(">>>>>>>>redisService.setIfAbsent time +1<<<<<<<<<<<<<<");
        return redisService.setIfAbsent(key, uuid, Duration.ofMillis(redisLock.lockExpireTime()));
    }
}
