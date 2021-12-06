package com.tubai.study_html.service;


import com.tubai.study_html.config.jwt.JwtToken;
import com.tubai.study_html.pojo.LoginObject;

public interface CommonService {
    /**
     * 登录 并返回jwtToken
     * @param loginObject
     * @param role
     * @return
     */
    JwtToken login(LoginObject loginObject, String role);


    /**
     * 根据登录来获取永久的Token 私下测试使用
     * @param loginObject
     * @param role
     * @return 返回null说明登录失败
     */
    JwtToken createPermanentToken(LoginObject loginObject,String role);
}
