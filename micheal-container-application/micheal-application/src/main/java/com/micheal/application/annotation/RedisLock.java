package com.micheal.application.annotation;

import java.lang.annotation.*;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/15 16:22
 * @Description
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {

    /**
     *  -锁值,默认为类全路径名+方法名
     * @return
     */
    String key() default "";

    /**
     * -单位毫米, 默认60s直接跳出
     * @return
     */
    long timeoutMills() default 60000;

    /**
     * -尝试获取所次数
     * @return
     */
    int retry() default 50;

    /**
     * -锁过期时间
     * @return
     */
    long lockExpireTime() default 60000;

    /**
     * -最大自旋次数
     * @return
     */
    int spin() default 10;
}
