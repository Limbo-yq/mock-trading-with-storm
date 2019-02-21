package com.limbo.actual.storm.redis;

import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;

public class RedisStoreBoltBuilder {

    public static RedisStoreBolt build(RedisStoreMapper mapper){
        JedisPoolConfig poolConfig = new JedisPoolConfig.Builder()
                .setHost("172.17.0.4").setPort(6379).build();
        return new RedisStoreBolt(poolConfig,mapper);
    }
}
