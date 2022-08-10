package com.orange.springbootjwtshiro.controller;

import com.orange.springbootjwtshiro.common.Result;
import com.orange.springbootjwtshiro.jwt.JwtUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author:Li ZhiCheng
 * @create:2022-08-2022/8/10 14:29
 * 示例控制层
 */

@RestController
@RequestMapping("/shiro")
public class HelloController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${custom.jwt.expire_time}")
    private long expireTime;

    @GetMapping("/getToken")
    public Result getToken(){
        String token = JwtUtil.sign("orange", "123");
        redisTemplate.opsForValue().set(token,token, expireTime*2/100, TimeUnit.SECONDS);
        return Result.success(token);
    }

    @RequiresPermissions("user:admin")
    @RequestMapping("/test")
    public Result test(){
        System.out.println("进入测试，只有带有令牌才可以进入该方法");
        return Result.success(200,"访问接口成功");
    }
}
