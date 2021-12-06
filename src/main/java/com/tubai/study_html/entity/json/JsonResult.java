package com.tubai.study_html.entity.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@ApiModel(value = "Json格式响应模型",
        description = "200表示正常,201表示操作失败,202表示用户名或密码错误,203表示用户名或密码为空,204表示用户名不存在,205表示参数错误," +
                "400表示凭证过期,401是没有权限,403是被禁止,404是资源不存在")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
public class JsonResult<T> {

    @ApiModelProperty(value = "返回数据,类型是泛型")
    private T data;
    @ApiModelProperty(value = "响应码", dataType = "Integer")
    private Integer code;
    @ApiModelProperty(value = "返回信息", dataType = "String")
    private String description;
    @ApiModelProperty(value = "额外信息", dataType = "Object")
    private Object extra;

    private JsonResult() {

    }

    /**
     * 若没有数据返回,人为指定状态码和提示信息
     * @param code
     * @param description
     */
    private JsonResult(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 若有数据返回,人为指定状态码和提示信息
     * @param code
     * @param description
     * @param data
     */
    private JsonResult(Integer code, String description, T data) {
        this.code = code;
        this.description = description;
        this.data = data;
    }
    private JsonResult(Integer code, String description, T data, Object extra) {
        this.code = code;
        this.description = description;
        this.data = data;
        this.extra = extra;
    }

    public static JsonResult create(Integer code, String message) {
        return new JsonResult(code, message);
    }

    public static <T1> JsonResult create(Integer code, String message,T1 data) {
        return new JsonResult(code, message, data);
    }

    public static <T1> JsonResult create(Integer code, String message,T1 data,Object extra) {
        return new JsonResult(code, message, data,extra);
    }

}
