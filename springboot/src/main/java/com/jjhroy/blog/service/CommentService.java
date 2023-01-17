package com.jjhroy.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jjhroy.blog.dto.CommentBackDTO;
import com.jjhroy.blog.dto.CommentDTO;
import com.jjhroy.blog.dto.ReplyDTO;
import com.jjhroy.blog.entity.Comment;
import com.jjhroy.blog.vo.CommentVO;
import com.jjhroy.blog.vo.ConditionVO;
import com.jjhroy.blog.vo.PageResult;
import com.jjhroy.blog.vo.ReviewVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 评论服务
 *
 * @author yezhiqiu
 * @date 2021/07/29
 */
public interface CommentService extends IService<Comment> {

    /**
     * 查看评论
     *
     * @param commentVO 评论信息
     * @return 评论列表
     */
    PageResult<CommentDTO> listComments(CommentVO commentVO);

    /**
     * 查看评论下的回复
     *
     * @param commentId 评论id
     * @return 回复列表
     */
    List<ReplyDTO> listRepliesByCommentId(Integer commentId);

    /**
     * 添加评论
     *
     * @param commentVO 评论对象
     */
    void saveComment(CommentVO commentVO, HttpServletRequest request);

    /**
     * 点赞评论
     *
     * @param commentId 评论id
     */
    void saveCommentLike(Integer commentId);

    /**
     * 审核评论
     *
     * @param reviewVO 审核信息
     */
    void updateCommentsReview(ReviewVO reviewVO);

    /**
     * 查询后台评论
     *
     * @param condition 条件
     * @return 评论列表
     */
    PageResult<CommentBackDTO> listCommentBackDTO(ConditionVO condition);

}
