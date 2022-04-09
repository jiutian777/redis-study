package com.jiutian.controller;

import com.jiutian.handler.ResultBody;
import com.jiutian.lua.SecKillRedisByScript;
import com.jiutian.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Date: 2022/3/30 18:54
 * @Author: jiutian
 * @Description:
 */
@RestController
public class SeckillRedisController {

    private final RedisUtil redisUtil;

    @Autowired
    public SeckillRedisController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    // 秒杀过程
    @PostMapping("/doSeckill")
    public ResultBody<String> doSeckill(@RequestParam String prodId) {
        String userId = new Random().nextInt(50000) + "";
        // return doSecKill(userId, prodId);
        return doSecKillFun(userId, prodId);
    }

    // 秒杀过程（乐观锁--解决超卖问题）
    public ResultBody<String> doSecKill(String userId, String prodId) {
        if (userId == null || prodId == null) {
            return ResultBody.error("");
        }

        // 库存key
        String kcKey = prodId + ":stock";
        // 秒杀成功用户key
        String userKey = prodId + ":userId";

       Object results = redisUtil.exec(new SessionCallback<Object>() {
            @Override
            public  Object execute(RedisOperations operations) throws DataAccessException {
                // 监视
                operations.watch(kcKey);
                // 获取库存，如果库存null，秒杀还没有开始
                Integer stock = (Integer) operations.opsForValue().get(kcKey);
                if (stock == null) {
                    System.out.println("别着急，秒杀还没开始呢！！");
                    return ResultBody.error("别着急，秒杀还没开始呢！！");
                }

                // 判断用户是否重复秒杀操作
                if (operations.opsForSet().isMember(userKey, userId)) {
                    System.out.println("你已经秒杀成功了，不能重复秒杀");
                    return ResultBody.error("你已经秒杀成功了，不能重复秒杀");
                }

                // 判断如果商品数量，库存数量小于1，秒杀结束
                if (stock <= 0) {
                    System.out.println("秒杀已经结束了");
                    return ResultBody.error("秒杀已经结束了");
                }

                // 秒杀过程
                // 开启事务
                operations.multi();
                // 库存 -1
                operations.opsForValue().decrement(kcKey);
                // 把秒杀成功用户添加清单里面
                operations.opsForSet().add(userKey, userId);
                // 执行事务
                List<Object> result = operations.exec();
                // 判断事务提交是否失败
                if (result.size() == 0) {
                    System.out.println("秒杀失败");
                    return ResultBody.error("秒杀失败");
                }
                System.out.println("恭喜你！秒杀成功了");
                return ResultBody.success("恭喜你！秒杀成功了");
            }
        });
        redisUtil.close();
        return (ResultBody<String>) results;
    }

    // 秒杀过程（LUA脚本解决库存剩余问题）
    public ResultBody<String> doSecKillFun(String userId, String prodId) {

        List<String> keys = new ArrayList<>();
        keys.add(userId);
        keys.add(prodId + ":stock");
        keys.add(prodId + ":userId");

        Long result = (Long) redisUtil.exec(SecKillRedisByScript.secKillScript, keys);
        String s;
        if (0 == result) {
            System.out.println("秒杀结束了。。。");
            s = "秒杀结束了。。。";
        } else if (1 == result) {
            System.out.println("恭喜你！秒杀成功了！");
            redisUtil.close();
            return ResultBody.success("恭喜你！秒杀成功了！");
        } else if (2 == result) {
            System.out.println("你已经秒杀成功了，不能重复秒杀");
            s = "你已经秒杀成功了，不能重复秒杀";
        } else {
            System.out.println("秒杀异常啦~");
            s = "秒杀异常啦~";
        }
        redisUtil.close();
        return ResultBody.error(s);
    }
}
