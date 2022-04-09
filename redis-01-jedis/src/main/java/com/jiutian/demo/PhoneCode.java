package com.jiutian.demo;


import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Random;

/**
 * @Date: 2022/3/30 10:36
 * @Author: jiutian
 * @Description:
 */
public class PhoneCode {
    public static void main(String[] args) {
        boolean b = saveVerifyCode("13000000000");
        if(b){
            verifyCode("13000000000","031776");
        }
    }

    // 验证
    public static void verifyCode(String phone, String code) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        String codeKey = "verify" + phone + "code";
        String redisCode = jedis.get(codeKey);
        if(Objects.equals(redisCode,code)){
            System.out.println("成功！");
        }else{
            System.out.println("失败");
        }
    }

    // 发送验证码
    public static boolean saveVerifyCode(String phone) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        String countKey = "verify" + phone + "count";
        String codeKey = "verify" + phone + "code";

        String count = jedis.get(countKey);
        if (count == null) {
            // 统计的数量值一天过期
            jedis.setex(countKey, 24 * 60 * 60, "1");
        } else if (Integer.parseInt(count) <= 2) {
            jedis.incr(countKey);
        } else if (Integer.parseInt(count) > 2) {
            System.out.println("今天的发送次数已超过三次，不能再发送");
            jedis.close();
            return false;
        }
        String vcode = getRandomCode();
        // 验证码 2 分钟过期
        jedis.setex(codeKey, 120, vcode);
        jedis.close();
        return true;
    }


    // 获得随机验证码
    public static String getRandomCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int a = random.nextInt(10);
            sb.append(a);
        }
        return sb.toString();
    }
}

