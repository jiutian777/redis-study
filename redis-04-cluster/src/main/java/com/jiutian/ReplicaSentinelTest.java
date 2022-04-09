package com.jiutian;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @Date: 2022/4/9 13:54
 * @Author: jiutian
 * @Description:    主从复制---哨兵
 */
public class ReplicaSentinelTest {
    private static JedisSentinelPool jedisSentinelPool = null;

    public static void main(String[] args) {
        Jedis jedisFromSentinel = getJedisFromSentinel();
    }

    public static Jedis getJedisFromSentinel() {
        if (jedisSentinelPool == null) {
            Set<String> sentinelSet = new HashSet<>();
            sentinelSet.add("192.168.146.124:26379");
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10); //最大可用连接数
            jedisPoolConfig.setMaxIdle(5); //最大闲置连接数
            jedisPoolConfig.setMinIdle(5); //最小闲置连接数
            jedisPoolConfig.setBlockWhenExhausted(true); //连接耗尽是否等待
            jedisPoolConfig.setMaxWaitMillis(2000); //等待时间
            jedisPoolConfig.setTestOnBorrow(true); //取连接的时候进行一下测试 ping pong
            jedisSentinelPool = new JedisSentinelPool("myMaster", sentinelSet, jedisPoolConfig);
        }
        return jedisSentinelPool.getResource();
    }
}
