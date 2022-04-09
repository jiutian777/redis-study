package com.jiutian.test;

import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * ClassName:testTX
 * Package:com.jiutian
 * Description:
 *
 * @Date:2021/11/24 16:40
 * @Author:jiutian
 */
public class testTX {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("123456");
        jedis.flushDB();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hello", "world");
        jsonObject.put("name", "jiutian");

        // 开启事务
        Transaction multi = jedis.multi();
        String result = jsonObject.toString();

        try {
            multi.set("user1", result);
            multi.set("user2", result);
            // int i = 1/0;
            multi.exec();   //执行事务
        } catch (Exception e) {
            multi.discard();   //放弃事务
            e.printStackTrace();
        } finally {
            System.out.println(jedis.get("user1"));
            System.out.println(jedis.get("user2"));
            jedis.close();  //关闭连接
        }
    }
}
