package com.jiutian.controller;

import com.jiutian.lua.LockLua;
import com.jiutian.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Date: 2022/4/9 16:39
 * @Author: jiutian
 * @Description:
 */
@RestController
@RequestMapping("/lock")
public class DistributeLockController {
    private final RedisUtil redisUtil;

    @Autowired
    public DistributeLockController(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 解决问题：A判断 uuid 相同后手动释放锁之前过期时间到锁自动释放，
     * B拿到锁开始执行，这时A再执行手动释放的是B的锁
     * 解决方法：使用 lua 脚本
     */
    @GetMapping("testLockLua")
    public void testLockLua() {
        // 声明一个uuid ,将做为一个value 放入我们的 key所对应的值中
        String uuid = UUID.randomUUID().toString();
        // 定义一个锁：lua 脚本可以使用同一把锁，来实现删除！
        String skuId = "25"; // 访问 skuId为 25号的商品
        String locKey = "lock:" + skuId; // 锁住的是每个商品的数据

        // 获取锁
        boolean lock = redisUtil.setNxAndEx(locKey, uuid, 3, TimeUnit.SECONDS);

        // 如果true
        if (lock) {
            // 执行的业务逻辑开始
            // 获取缓存中的num 数据
            Object value = redisUtil.get("num");
            // 如果是空直接返回
            if (ObjectUtils.isEmpty(value)) {
                return;
            }
            int num = Integer.parseInt(value + "");
            // 使 num 每次 +1 放入缓存
            redisUtil.set("num", ++num);
            // 使用 lua脚本来释放锁
            String script = LockLua.DEL_SCRIPT;
            // 使用 redis执行 lua
            redisUtil.exec(script, Collections.singletonList(locKey), uuid);
        } else {
            // 其他线程等待
            try {
                // 睡眠
                Thread.sleep(1000);
                // 睡醒了之后，调用方法。
                testLockLua();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 解决问题：A执行业务逻辑完成之前到了过期时间锁自动释放，B拿到锁开始执行，
     * 这时A执行结束，再手动释放，释放的是B的锁
     * 解决方法：使用 uuid 的唯一值
     */
    @GetMapping("testLock")
    public void testLock() {
        String uuid = UUID.randomUUID().toString();
        // 获取锁 set nx ex
        boolean lock = redisUtil.setNxAndEx("lock", uuid, 3, TimeUnit.SECONDS);
        // 获取锁成功、查询 num 的值
        if (lock) {
            Object value = redisUtil.get("num");
            // 判断 num为空 return
            if (ObjectUtils.isEmpty(value)) {
                return;
            }
            // 有值就转成 int
            int num = Integer.parseInt(value + "");
            // 把 redis 的 num加 1
            redisUtil.set("num", ++num);
            // 释放锁 del
            // 判断比较uuid值是否一样
            String lockUuid = (String) redisUtil.get("lock");
            if (Objects.equals(lockUuid, uuid)) {
                redisUtil.del("lock");
            }
        } else {
            // 获取锁失败、每隔 0.1秒再获取
            try {
                Thread.sleep(100);
                testLock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
