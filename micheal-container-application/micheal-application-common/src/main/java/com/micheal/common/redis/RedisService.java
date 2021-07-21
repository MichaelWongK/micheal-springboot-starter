package com.micheal.common.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.time.Duration;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/19 11:53
 * @Description redis 操作service
 */
public class RedisService {

    private RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 普通缓存并设置持续时间
     * @param key       键
     * @param value
     * @param timeout
     * @return
     */
    public boolean set(String key, Object value, Duration timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String ... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean setIfAbsent(String key, Object value, Duration timeout) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
    }
}
