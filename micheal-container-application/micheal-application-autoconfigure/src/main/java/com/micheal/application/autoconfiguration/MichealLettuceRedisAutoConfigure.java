package com.micheal.application.autoconfiguration;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.micheal.application.redis.MichealRedisProperties;
import com.micheal.common.redis.RedisService;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import javax.annotation.PostConstruct;

/**
 * @author <a href="mailto:wangmk13@163.com">micheal.wang</a>
 * @date 2021/7/15 19:29
 * @Description
 */
@ComponentScan(basePackages = {"com.micheal.application.aspect"})
@EnableConfigurationProperties(MichealRedisProperties.class)
@ConditionalOnProperty(prefix = MichealRedisProperties.REDIS_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class MichealLettuceRedisAutoConfigure {

    private static final Logger logger = LoggerFactory.getLogger(MichealLettuceRedisAutoConfigure.class);

    @Autowired
    private MichealRedisProperties michealRedisProperties;

    @PostConstruct
    public void load() {
        logger.info("分布式事务锁初始化中........................");
    }

    @Bean
    @ConditionalOnMissingBean(RedisService.class)
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisService(redisTemplate);
    }

    @Bean
    @Primary
    public LettuceConnectionFactory getConnectionFactory() {
        RedisStandaloneConfiguration redisConfigutation = buildRedisConfiguration(michealRedisProperties);
        LettucePoolingClientConfiguration lettuceClientConfiguration = buildLettuceClientConfiguration(michealRedisProperties);
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfigutation, lettuceClientConfiguration);
        lettuceConnectionFactory.afterPropertiesSet();
        logger.info("分布式事务锁初始化完成:{}........................", michealRedisProperties);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private LettucePoolingClientConfiguration buildLettuceClientConfiguration(MichealRedisProperties michealRedisProperties) {
        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder lettuceClientConfigurationBuilder;
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(michealRedisProperties.getMaxActive());
        poolConfig.setMaxIdle(michealRedisProperties.getMaxIdle());
        poolConfig.setMinIdle(michealRedisProperties.getMinIdle());
        if (michealRedisProperties.getMaxWait() != null) {
            poolConfig.setMaxWaitMillis(michealRedisProperties.getMaxWait().toMillis());
        }
        if (michealRedisProperties.getTimeBetweenEvictionRuns() != null) {
            poolConfig.setTimeBetweenEvictionRunsMillis(michealRedisProperties.getTimeBetweenEvictionRuns().toMillis());
        }
        lettuceClientConfigurationBuilder = LettucePoolingClientConfiguration.builder().poolConfig(poolConfig);

        if (michealRedisProperties.ssl()) {
            lettuceClientConfigurationBuilder.useSsl();
        }
        if (michealRedisProperties.getTimeOut() != null) {
            lettuceClientConfigurationBuilder.commandTimeout(michealRedisProperties.getTimeOut());
        }
        DefaultClientResources clientResources = DefaultClientResources.create();
        lettuceClientConfigurationBuilder.clientResources(clientResources);
        return lettuceClientConfigurationBuilder.build();
    }

    private RedisStandaloneConfiguration buildRedisConfiguration(MichealRedisProperties michealRedisProperties) {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();
        redisConfiguration.setHostName(michealRedisProperties.getHost());
        redisConfiguration.setPort(michealRedisProperties.getPort());
        redisConfiguration.setDatabase(michealRedisProperties.getDataBase());
        if (StringUtils.isNoneBlank(michealRedisProperties.getPassword())) {
            redisConfiguration.setPassword(RedisPassword.of(michealRedisProperties.getPassword()));
        }
        return redisConfiguration;
    }
}
