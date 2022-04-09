package com.jiutian;

import com.jiutian.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
class Redis03SeckillApplicationTests {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void contextLoads() {

    }

}
