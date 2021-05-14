package com.qiuyu.demo.utils.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {

    private static final Logger log = LoggerFactory.getLogger(RedisUtils.class);

    private static RedisUtils redisUtils;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostConstruct
    public void init() {
        redisUtils = this;
        RedisSerializer stringSerializer = new StringRedisSerializer();
        this.redisTemplate.setKeySerializer(stringSerializer);
        this.redisTemplate.setValueSerializer(stringSerializer);
        this.redisTemplate.setHashKeySerializer(stringSerializer);
        this.redisTemplate.setHashValueSerializer(stringSerializer);
        redisUtils.redisTemplate = this.redisTemplate;
    }


    /**
     * 写入缓存
     * @param key 键值
     * @param value 该键值对应的值
     * @return 是否写入成功
     */
    public static boolean set(final String key, Object value) {
        boolean result = false;
        try {
            if(redisUtils.redisTemplate != null ){
                ValueOperations<Serializable, Object> operations = redisUtils.redisTemplate.opsForValue();
                operations.set(key, value );
                result = true;
            }

        } catch (Exception e) {
            log.info("将数据写入缓存出现异常");
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     * @param key 键值
     * @param value 该键值对应的值
     * @return 是否写入成功
     */
    public static boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisUtils.redisTemplate.opsForValue();
            operations.set(key, value);
            redisUtils.redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断缓存中是否有对应的value
     */
    public static boolean exists(final String key) {
        boolean flag = false;
        try {
            flag = redisUtils.redisTemplate == null ? false : redisUtils.redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.info("从redis中判断缓存中是否有对应的key出现异常");
        }
        return flag;
    }

    /**
     * 删除对应的value
     */
    public static void remove(final String key) {
        if (exists(key)) {
            redisUtils.redisTemplate.delete(key);
        }
    }

    /**
     * 读取缓存
     */
    public static Object get(final String key) {
        Object result ;
        try {
            ValueOperations<Serializable, Object> operations = redisUtils.redisTemplate.opsForValue();
            result = operations.get(key);
        } catch (Exception e) {
            log.info("从redis读取缓存时出现异常");
            return null;
        }
        return result;
    }



}
