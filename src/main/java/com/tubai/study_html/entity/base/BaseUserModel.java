package com.tubai.study_html.entity.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "基础模型")
public class BaseUserModel implements Serializable {
    private static final long serialVersionUID = 2505668255986953101L;
    @ApiModelProperty(value = "用户的编号,是数据库主键,自增")
    protected Integer id;
    @ApiModelProperty(value = "用户的角色，一个用户对应一个角色")
    protected String role;
    @ApiModelProperty(value = "登录的账号")
    protected String account;
    @ApiModelProperty(value = "登录的密码")
    protected String password;
}
