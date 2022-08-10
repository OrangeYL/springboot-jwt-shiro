# springboot-jwt-shiro

  流程：  
  1.登录成功后，创建Token,后面的接口调用都要传递Token。  
  2.接口首先通过shiroFilter过滤，确定是否要进入jwtFilter进行处理。  
  3.jwtFilter 首先执行isAccessAllowed方法，如果时OPTIONS请求直接放行，否则进行Shiro的executeLogin验证。  
  4.executeLogin执行的时候会到jwtRealm中调用doGetAuthenticationInfo方法，根据Token获取当前登录用户的信息。   
  5.executeLogin成功后，会看当前访问的接口，有无权限注解@RequiresPermissions。  
  6.如果有注解的话，说需要进行权限验证，Shiro会通过 jwtRealm的doGetAuthorizationInfo方法，获取当前用户的权限进行验证。  
