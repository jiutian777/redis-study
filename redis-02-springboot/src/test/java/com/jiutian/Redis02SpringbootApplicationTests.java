package com.jiutian;

import com.jiutian.pojo.User;
import com.jiutian.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;


@SpringBootTest
class Redis02SpringbootApplicationTests {

    // @Autowired
    // @Qualifier("customerRedisTemplate")
    // private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void test1() {
        redisUtil.set("name", "jiutian");
        System.out.println(redisUtil.get("name"));
    }

    @Test
    void test2() {
        long set1 = redisUtil.sAdd("set1", 666);
        System.out.println(set1);
        long set2 = redisUtil.sAdd("set1", "555");
        System.out.println(set2);

        Set<Object> set11 = redisUtil.sMembers("set1");
        System.out.println(set11);
    }

    @Test
    void contextLoads() {
        // opsForValue  String
        // opsForList    List
        // opsForSet     Set
        // opsForHash    Hash
        // opsForZSet    ZSet
        // opsForGeo    geo
        // opsForHyperLogLog  HyperLogLog

        // RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        // connection.flushDb();
        // redisTemplate.opsForValue().set("mykey", "九天");
        // System.out.println(redisTemplate.opsForValue().get("mykey"));
    }

    @Test
    void test() {
        User user = new User("九天", 3);
        // String jsonUser = new ObjectMapper().writeValueAsString(user);
        // redisTemplate.opsForValue().set("user", user);
        // System.out.println(redisTemplate.opsForValue().get("user"));
    }
}
