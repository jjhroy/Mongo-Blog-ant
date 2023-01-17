package com.jjhroy.blog.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 未查看的新回复
 *
 * @author jjhroy
 * @date 2021/08/03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "未读回复")
public class NewCommentsVO {

    /**
     * 文章id
     */
    @ApiModelProperty(name = "id", value = "文章id", dataType = "Integer")
    private Integer id;

    /**
     * 标题
     */
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;


    /**
     * 未读回复数量
     */
    @ApiModelProperty(name = "newCommentsCount", value = "未读回复数量", dataType = "Integer")
    private Integer newCommentsCount;



}
