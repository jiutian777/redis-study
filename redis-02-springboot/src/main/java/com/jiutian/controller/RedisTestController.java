package com.jiutian.controller;

import com.jiutian.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date: 2022/3/30 11:45
 * @Author: jiutian
 * @Description:
 */
@RestController
@RequestMapping("/testRedis")
public class RedisTestController {

    private final RedisUtil redisUtil;

    @Autowired
    public RedisTestController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @GetMapping
    public String test(){
        redisUtil.set("name","mike");
        return (String) redisUtil.get("name");
    }
}
