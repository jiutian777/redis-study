package com.jiutian.test;

import redis.clients.jedis.Jedis;

/**
 * ClassName:TestPing
 * Package:com.jiutian
 * Description:
 *
 * @Date:2021/11/24 16:23
 * @Author:jiutian
 */
public class TestPing {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        // 验证密码，如果没有设置密码这段代码省略
        jedis.auth("123456");
        System.out.println(jedis.isConnected()); //false
        System.out.println(jedis.ping());
        System.out.println(jedis.isConnected()); //true
        jedis.connect(); //连接
        System.out.println(jedis.isConnected()); //true
        jedis.disconnect(); //断开连接
        System.out.println(jedis.flushAll());//清空所有的key
        jedis.close();  //关闭连接
    }
}
