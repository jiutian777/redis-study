package com.jiutian.lua;

/**
 * @Date: 2022/4/9 17:44
 * @Author: jiutian
 * @Description:
 */
public class LockLua {
    public static final String DEL_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
}
