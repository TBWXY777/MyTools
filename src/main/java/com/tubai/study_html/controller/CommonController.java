package com.tubai.study_html.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import com.tubai.study_html.exception.ServiceException;
import com.tubai.study_html.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = "一些通用的接口,返回Json数据")
public class CommonController {

    @ApiOperation(value = "前往没有权限页面的接口",
            notes = "如果没有权限,则会自动调用这个接口")
    @GetMapping("/unauthorized")
    public JsonResult<String> unauthorized(){
        return JsonResult.create(StatusCode.UNAUTHORIZED,"您没有权限访问!可能是未携带token或者token错误");
    }



    @ApiOperation(value = "前往没有签证过期页面的接口",
            notes = "如果签证到期了,则会自动调用这个接口")
    @GetMapping("/tokenExpired")
    public JsonResult<String> tokenExpired(){
        throw new ServiceException(JsonResult.create(StatusCode.TOKEN_EXPIRED,"签证过期了!"));
    }

    @ApiOperation(value = "验证签证是否过期的接口",notes = "参数要求是json格式的")
    @ApiImplicitParam(name = "token",value = "登录成功后返回的token")
    @PostMapping("/tokenisExpired")
    public JsonResult tokenisExpired(@RequestBody String token){
        boolean result = true;
        try{
            result = JwtUtils.isExpireAt(token);
        }catch (JWTDecodeException e){
            throw new ServiceException(JsonResult.create(StatusCode.ERROR,"非法签证!"));
        }
        if(result){
            return JsonResult.create(StatusCode.TOKEN_EXPIRED,"该签证已经过期了!");
        }
        return JsonResult.create(StatusCode.OK,"该签证还能继续使用!");
    }
}
