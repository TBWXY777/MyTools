package com.tubai.study_html.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ApiModel(value = "用户登录模型")
public class LoginObject {
    @ApiModelProperty(value = "登录的账号")
    private String account;
    @ApiModelProperty(value = "登录的密码")
    private String password;
}
