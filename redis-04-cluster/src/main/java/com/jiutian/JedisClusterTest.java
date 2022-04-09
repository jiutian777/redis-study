package com.jiutian;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @Date: 2022/4/9 13:56
 * @Author: jiutian
 * @Description:    集群
 */
public class JedisClusterTest {
    public static void main(String[] args) {
        // Set<HostAndPort> set = new HashSet<>();
        // set.add(new HostAndPort("192.168.146.124", 6379));
        // JedisCluster jedisCluster = new JedisCluster(set);

        HostAndPort hostAndPort = new HostAndPort("192.168.146.124", 6379);
        JedisCluster jedisCluster = new JedisCluster(hostAndPort);
        jedisCluster.set("myKey","666");
        System.out.println(jedisCluster.get("myKey"));
    }
}
