package com.qiuyu.demo.utils.redis;

import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
* @Description: redis工具类
* @Author: qy
**/
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String prefix = null;

    private ValueOperations<String, String> valueOperations;

    private HashOperations<String, String, String> hashOperations;

    private ListOperations<String, String> listOperations;

    private SetOperations<String, String> setOperations;

    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        listOperations = redisTemplate.opsForList();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }

    public RedisUtil setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    //--------------------------------------------global--------------------------------------------
    private String buildKey(String key) {
        return Objects.isNull(prefix) ? key : prefix + key;
    }

    public void expire(String key, long expire, TimeUnit unit) {
        redisTemplate.expire(buildKey(key), expire, unit);
    }

    public void expire(String key, long expire) {
        expire(key, expire, TimeUnit.SECONDS);
    }

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void remove(String key) {
        redisTemplate.delete(key);
    }
    //--------------------------------------------global--------------------------------------------


    //--------------------------------------------valueOperations--------------------------------------------
    public <T> void set(String key, T t, long expire, TimeUnit unit) {
        valueOperations.set(key, JSONUtil.toJsonStr(t), expire, unit);
    }

    public <T> void set(String key, T t, long expire) {
        set(buildKey(key), t, expire, TimeUnit.SECONDS);
    }

    public <T> void set(String key, T t) {
        valueOperations.set(buildKey(key), JSONUtil.toJsonStr(t));
    }
    //--------------------------------------------valueOperations--------------------------------------------


    //--------------------------------------------setOperations--------------------------------------------
    public <T> void addSet(String key, T t) {
        setOperations.add(buildKey(key), JSONUtil.toJsonStr(t));
    }

    public <T> void removeSet(String key, T t) {
        setOperations.remove(buildKey(key), JSONUtil.toJsonStr(t));
    }

    public <T> List<T> members(String key, Class<T> clazz) {
        Set<String> members = setOperations.members(buildKey(key));
        return JSONUtil.toList(JSONUtil.parseArray(members), clazz);
    }
    //--------------------------------------------setOperations--------------------------------------------

    //--------------------------------------------hashOperations--------------------------------------------
    public <T> void put(String key, String hashKey, T t) {
        hashOperations.put(buildKey(key), hashKey, JSONUtil.toJsonStr(t));
    }

    public <T> T getHash(String key, String hashKey, Class<T> clazz) {
        String s = hashOperations.get(key, hashKey);
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return JSONUtil.toBean(s, clazz);
    }

    public <T> List<T> getHashAll(String key, Class<T> tClass) {
        List<String> values = hashOperations.values(key);
        List<T> list = new ArrayList<>(values.size());
        for (String value : values) {
            T t = JSONUtil.toBean(value, tClass);
            list.add(t);
        }
        return list;
    }
    //--------------------------------------------hashOperations--------------------------------------------


}
