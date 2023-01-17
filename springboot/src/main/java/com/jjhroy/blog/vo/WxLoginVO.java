package com.jjhroy.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(description = "微信登录信息")
public class WxLoginVO {
    /**
     * code
     */
    @NotBlank(message = "code不能为空")
    @ApiModelProperty(name = "code", value = "code", required = true, dataType = "String")
    private String code;
}
