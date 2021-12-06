package com.tubai.study_html.exception;


import com.tubai.study_html.entity.json.JsonResult;
import com.tubai.study_html.entity.json.StatusCode;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 接口增强器
 * 只能拦截接口抛出的异常
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    /**
     * 拦截捕捉自定义异常 ServiceException.class
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public JsonResult serviceExceptionHandler(ServiceException e) {
        return JsonResult.create(e.getCode(),e.getMessage());
    }

    /**
     * 拦截捕捉权限异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = UnauthorizedException.class)
    public JsonResult unauthorizedExceptionHandler(UnauthorizedException e) {
        return JsonResult.create(StatusCode.UNAUTHORIZED,"该账号没有权限，或者还未登录！");
    }

    /**
     * 拦截认证异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = UnauthenticatedException.class)
    public JsonResult unauthenticatedExceptionHandler(UnauthenticatedException e) {
        return JsonResult.create(StatusCode.UNAUTHORIZED,"没有权限！未携带token或者token错误！");
    }

}
