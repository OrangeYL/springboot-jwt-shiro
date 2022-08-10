package com.orange.springbootjwtshiro.shiro;

import com.orange.springbootjwtshiro.jwt.JwtToken;
import com.orange.springbootjwtshiro.jwt.JwtUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * @author:Li ZhiCheng
 * @create:2022-08-2022/8/10 15:38
 * @description: shiro自定义认证逻辑
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${custom.jwt.expire_time}")
    private long expireTime;

    /**
     * 设置对应的token类型
     * 必须重写此方法，不然Shiro会报错
     * @param token
     * @return
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //权限认证
        System.out.println("开始权限认证");
        //获取用户名
        String token = (String)SecurityUtils.getSubject().getPrincipal();
        String username = JwtUtil.getUsername(token);
        //模拟数据库校验,写死用户名，其他用户无法登陆成功
        if(!"orange".equals(username)){
            return null;
        }
        //创建授权信息
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        /**
         * 这里实际上要去查询数据库中用户角色和权限列表, 放入SimpleAuthorizationInfo中
         */
        //创建set集合，存储权限
        HashSet<String> rootSet = new HashSet<>();
        //添加权限
        rootSet.add("user:show");
        rootSet.add("user:admin");
        //设置权限
        info.setStringPermissions(rootSet);
        //返回权限实例
        return info;
    }



    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("开始身份认证.....................");
        //获取token
        String token = (String) authenticationToken.getCredentials();
        //创建字符串，存储用户信息
        String username = null;
        try {
            //获取用户名
            username = JwtUtil.getUsername(token);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("header的token拼写错误或者值为空");
        }
        if (username == null) {
            throw new AuthenticationException("token无效");
        }
        // 校验token是否超时失效 & 或者账号密码是否错误
        if (!jwtTokenRefresh(token, username, "123")) {
            throw new AuthenticationException("Token失效，请重新登录!");
        }
        //返回身份认证信息
        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
    /**
     * jwt刷新令牌
     *
     * @param token    令牌
     * @param userName 用户名
     * @param password 通过单词
     * @return boolean
     */
    public boolean jwtTokenRefresh(String token, String userName, String password) {
        String redisToken = (String) redisTemplate.opsForValue().get(token);
        if (redisToken != null) {
            if (!JwtUtil.verify(redisToken, userName, password)) {
                String newToken = JwtUtil.sign(userName, password);
                //设置redis缓存
                redisTemplate.opsForValue().set(token, newToken, expireTime * 2 / 1000, TimeUnit.SECONDS);
            }
            return true;
        }
        return false;
    }
}
