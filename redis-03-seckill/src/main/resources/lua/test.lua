local userId=KEYS[1];
local stockKey=KEYS[2];
local userIdKey=KEYS[3];
local userExists=redis.call("sismember",userIdKey,userId);
if tonumber(userExists)==1
then
  return 2;
end
local num= redis.call("get" ,stockKey);
if tonumber(num)<=0 then   return 0;
else
 redis.call("decr",stockKey);
redis.call("sadd",userIdKey,userId);
end
return 1;
