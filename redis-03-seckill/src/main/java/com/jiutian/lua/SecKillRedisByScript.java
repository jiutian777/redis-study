package com.jiutian.lua;

public class SecKillRedisByScript {

    public static String secKillScript = "local userId=KEYS[1];\r\n" +
            "local stockKey=KEYS[2];\r\n" +
            "local userIdKey=KEYS[3];\r\n" +
            "local userExists=redis.call(\"sismember\",userIdKey,userId); \r\n" +
            "if tonumber(userExists)==1 \r\n" +
            "then \r\n" +
            "  return 2;\r\n" +
            "end \r\n" +
            "local num= redis.call(\"get\" ,stockKey);\r\n" +
            "if tonumber(num)<=0 then   return 0;\r\n" +
            "else \r\n " +
            " redis.call(\"decr\",stockKey);\r\n" +
            "redis.call(\"sadd\",userIdKey,userId);\r\n" +
            "end \r\n" +
            "return 1;";

    static String secKillScript2 =
            "local userExists=redis.call(\"sismember\",\"{sk}:0101:usr\",userId);\r\n" +
                    " return 1";
}
