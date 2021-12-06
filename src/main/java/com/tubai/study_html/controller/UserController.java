package com.tubai.study_html.controller;

import com.tubai.study_html.config.jwt.JwtToken;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.pojo.LoginObject;
import com.tubai.study_html.service.CommonService;
import com.tubai.study_html.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/user")
@Api(tags = "用户相关接口,返回Json数据")
public class UserController {
    @Autowired
    CommonService commonService;

    @ApiOperation(value = "个人登录")
    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody LoginObject loginObject){
        JwtToken jwtToken = commonService.login(loginObject, "admin");
        return JsonResult.create(StatusCode.OK,"登录成功",jwtToken.getJwtToken());
    }

    @ApiOperation(value = "个人获取数据")
    @RequiresRoles("admin")
    @GetMapping("/get_data")
    public JsonResult<String> getData(){
        return JsonResult.create(StatusCode.PARAM_ERROR,"获取成功","数据包");
    }
}
