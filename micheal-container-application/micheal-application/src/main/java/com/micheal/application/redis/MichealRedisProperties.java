package com.micheal.application.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/15 16:01
 * @Description Redis相关属性配置
 */
@SuppressWarnings("ALL")
@ConfigurationProperties(prefix = MichealRedisProperties.REDIS_PREFIX)
public class MichealRedisProperties {

    public static final String REDIS_PREFIX = "micheal.redis.lock.server";

    private String host;

    private int port;

    private String password;

    /**
     * Maximum number of "idle" connections in the pool. Use a negative value to
     * indicate an unlimited number of idle connections.
     */
    private int maxIdle = 8;

    /**
     * Target for the minimum number of idle connections to maintain in the pool. This
     * setting only has an effect if both it and time between eviction runs are
     * positive.
     */
    private int minIdle = 0;

    /**
     * Maximum number of connections that can be allocated by the pool at a given
     * time. Use a negative value for no limit.
     */
    private int maxActive = 8;

    /**
     * Maximum amount of time a connection allocation should block before throwing an
     * exception when the pool is exhausted. Use a negative value to block
     * indefinitely.
     */
    private Duration maxWait = Duration.ofMillis(-1);

    /**
     * Time between runs of the idle object evictor thread. When positive, the idle
     * object evictor thread starts, otherwise no idle object eviction is performed.
     */
    private Duration timeBetweenEvictionRuns;

    /**
     * Whether to enable SSL support.
     */
    private boolean ssl;

    private int dataBase;

    private Duration timeOut;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public Duration getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Duration maxWait) {
        this.maxWait = maxWait;
    }

    public Duration getTimeBetweenEvictionRuns() {
        return timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
        this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }

    public boolean ssl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public int getDataBase() {
        return dataBase;
    }

    public void setDataBase(int dataBase) {
        this.dataBase = dataBase;
    }

    public Duration getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Duration timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "MichealRedisProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", password='" + password + '\'' +
                ", maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                ", maxActive=" + maxActive +
                ", maxWait=" + maxWait +
                ", timeBetweenEvictionRuns=" + timeBetweenEvictionRuns +
                ", dataBase=" + dataBase +
                ", timeOut=" + timeOut +
                '}';
    }
}
