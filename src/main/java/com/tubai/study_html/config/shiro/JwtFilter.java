package com.tubai.study_html.config.shiro;

import com.tubai.study_html.config.jwt.JwtToken;
import com.tubai.study_html.entity.json.StatusCode;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtFilter extends BasicHttpAuthenticationFilter {

    /**
     * 如果带有 token，则对 token 进行检查，否则直接返回true
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws UnauthorizedException {
        //判断请求的请求头是否带上 "Token"
        if (isLoginAttempt(request, response)) {
            //如果存在，则进入 executeLogin 方法执行登入，检查 token 是否正确
            try {
                executeLogin(request, response);
                return true;
            }catch (AuthenticationException e){
                //捕获到认证异常
                if(e.getMessage().equals(String.valueOf(StatusCode.TOKEN_EXPIRED))) responseException(response,"/tokenExpired");
                else responseException(response, "/unauthorized");
            }
            catch (Exception e) {
                //token 错误
                responseException(response, "/unauthorized");
            }
        }
        //如果请求头不存在 Token，则可能是执行登陆操作或者是游客状态访问，无需检查 token，直接返回 true
        return true;
    }

    /**
     * 检测 header 里面是否包含 Token 字段
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("authenticatedToken");
        return token != null;
    }

    /**
     * 执行登陆操作
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("authenticatedToken");
        JwtToken jwtToken = new JwtToken(token);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 将非法请求跳转到 /unauthorized
     */
    private void responseException(ServletResponse response, String toUrl) {
        try {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            //前往我们自定义的页面
            httpServletResponse.sendRedirect(toUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
