package com.tubai.study_html.exception;

import com.tubai.study_html.entity.json.JsonResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *服务级异常
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceException extends RuntimeException implements Serializable {
    private  String message;
    private  Integer code;
    public ServiceException(JsonResult jsonResult){
        this.message = jsonResult.getDescription();
        this.code = jsonResult.getCode();
    }
}
