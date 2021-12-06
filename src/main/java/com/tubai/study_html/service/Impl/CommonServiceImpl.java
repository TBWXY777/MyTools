package com.tubai.study_html.service.Impl;

import com.tubai.study_html.config.jwt.JwtToken;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.exception.ServiceException;
import com.tubai.study_html.pojo.LoginObject;
import com.tubai.study_html.service.CommonService;
import com.tubai.study_html.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
    @Override
    public JwtToken login(LoginObject loginObject, String role){
        String account = loginObject.getAccount();
        String password = loginObject.getPassword();
        if(ObjectUtils.isEmpty(account)|| ObjectUtils.isEmpty(password)){
            throw new ServiceException(JsonResult.create(StatusCode.ACCOUNT_PASSWORD_EMPTY,"用户名或密码不能为空"));
        }
        //将未验证用户的account封装进payload,将用户的密码封装进secret
        String loginToken = JwtUtils.createToken(account,password,role);
        JwtToken jToken = new JwtToken(loginToken);
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(jToken);
            return jToken;
        }catch (UnknownAccountException e){
            throw new ServiceException(JsonResult.create(StatusCode.ACCOUNT_NOT_FONUD,"不存在此用户名"));
        }catch (IncorrectCredentialsException e){
            throw new ServiceException(JsonResult.create(StatusCode.LOGIN_ERROR,"用户名或密码错误"));
        }catch (ServiceException e){
            throw new ServiceException(e.getMessage(),StatusCode.ERROR);
        }
    }

    @Override
    public JwtToken createPermanentToken(LoginObject loginObject, String role) {
        JwtToken token = login(loginObject, role);
        //如果能成功登录 说明没问题
        if(token!=null){
            String permanentToken = JwtUtils.createPermanentToken(loginObject.getAccount(),
                    loginObject.getPassword(),
                    role);
            return new JwtToken(permanentToken);
        }
        return null;
    }
}
