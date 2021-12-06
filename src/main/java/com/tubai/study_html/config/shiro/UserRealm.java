package com.tubai.study_html.config.shiro;



import com.tubai.study_html.config.jwt.JwtToken;
import com.tubai.study_html.entity.base.BaseUserModel;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.exception.ServiceException;
import com.tubai.study_html.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;

//自定义Realm 只需要继承这个类AuthorizingRealm
public class UserRealm extends AuthorizingRealm {

    /**
     * 限定这个 Realm 只处理 JwtToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Object principal = principalCollection.getPrimaryPrincipal();
        BaseUserModel baseUserModel = (BaseUserModel)principal;
        String role = baseUserModel.getRole();
        //不授权
        if(ObjectUtils.isEmpty(role)){
            return null;
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //执行授权
        info.addRole(role);
        return info;
    }

    /**
     * 认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken)token;
        if(JwtUtils.isExpireAt(jwtToken.getJwtToken())){
            throw new AuthenticationException(String.valueOf(StatusCode.TOKEN_EXPIRED));
        }
        //拿出未验证用户封装在payload的account
        String account = JwtUtils.get(jwtToken.getJwtToken(),"account");
        String role = JwtUtils.get(jwtToken.getJwtToken(), "role");
        String methodName = new StringBuffer(role).append("Login").toString();

        try {
            Object invoke = UserRealm.class
                    .getDeclaredMethod(methodName, String.class, JwtToken.class, String.class)
                    .invoke(this, account, jwtToken, role);
            return (AuthenticationInfo)invoke;
        }catch (ServiceException e){
            throw new ServiceException(e.getMessage(),StatusCode.SERVICE_ERROR);
        }
        catch (IllegalAccessException e) {
            throw new ServiceException(e.getMessage(),StatusCode.SERVICE_ERROR);
        }//这个异常是方法里面的异常
        catch (InvocationTargetException e) {
            //获取自定义异常需要这样写 不能直接getMessage
            String message = e.getCause().getMessage();
            throw new ServiceException(message,StatusCode.ERROR);
        } catch (NoSuchMethodException e) {
            throw new ServiceException(e.getMessage(),StatusCode.SERVICE_ERROR);
        }
    }

    //后续添加新角色 只需要在这里添加处理逻辑即可 跟下面一样即可
    private AuthenticationInfo commonLogin(BaseUserModel obj, JwtToken jwtToken, String role, String account){
        if(ObjectUtils.isEmpty(obj)){
            throw new ServiceException("用户名不存在",StatusCode.ACCOUNT_NOT_FONUD);
        }
        String password = obj.getPassword();
        boolean loginState = JwtUtils.verifyToken(jwtToken.getJwtToken(), account, password
                ,role);
        if(!loginState){
            throw new ServiceException("用户名或密码错误!",StatusCode.ERROR);
        }else{
            //登录成功的话 直接将token封装给AuthenticationInfo的对象
            AuthenticationInfo info = new SimpleAuthenticationInfo(
                    //实体(用于辨认用户身份),用户密码,以及Realm的名字
                    obj,jwtToken.getCredentials(),getName()
            );
            return info;
        }
    }
    // TODO 写XXLogin
    // 比如private AuthenticationInfo adminLogin(String account, JwtToken jwtToken, String role)

    private AuthenticationInfo adminLogin(String account, JwtToken jwtToken, String role){
        BaseUserModel userModel = new BaseUserModel(null, "admin", "admin", "tubai");
        return commonLogin(userModel,jwtToken,role,account);
    }
    /**
     * 自定义方法：清除所有 授权缓存
     */
    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    /**
     * 自定义方法：清除所有 认证缓存
     */
    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    /**
     * 自定义方法：清除所有的  认证缓存  和 授权缓存
     */
    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }
}
