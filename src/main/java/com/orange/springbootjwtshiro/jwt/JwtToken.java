package com.orange.springbootjwtshiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 实现AuthenticationToken 使Realm的doGetAuthenticationInfo能够获取到token进行验证
 *
 * @author:Li ZhiCheng
 * @create:2022-08-2022/8/10 14:44
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token=token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
