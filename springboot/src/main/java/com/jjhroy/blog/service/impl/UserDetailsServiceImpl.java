package com.jjhroy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jjhroy.blog.constant.RedisPrefixConst;
import com.jjhroy.blog.dao.ArticleDao;
import com.jjhroy.blog.dao.RoleDao;
import com.jjhroy.blog.dao.UserAuthDao;
import com.jjhroy.blog.dao.UserInfoDao;
import com.jjhroy.blog.dto.ArticleDTO;
import com.jjhroy.blog.dto.UserDetailDTO;
import com.jjhroy.blog.entity.UserAuth;
import com.jjhroy.blog.entity.UserInfo;
import com.jjhroy.blog.enums.ZoneEnum;
import com.jjhroy.blog.exception.BizException;
import com.jjhroy.blog.service.RedisService;
import com.jjhroy.blog.util.IpUtils;
import com.jjhroy.blog.vo.NewCommentsVO;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * 用户详细信息服务
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAuthDao userAuthDao;
    @Autowired
    private UserInfoDao userInfoDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RedisService redisService;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private ArticleDao articleDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new BizException("用户名不能为空！");
        }
        // 查询账号是否存在
        UserAuth userAuth = userAuthDao.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername, UserAuth::getPassword, UserAuth::getLoginType)
                .eq(UserAuth::getUsername, username));
        if (Objects.isNull(userAuth)) {
            throw new BizException("用户名不存在!");
        }
        // 封装登录信息
        return convertUserDetail(userAuth, request);
    }

    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserDetailDTO convertUserDetail(UserAuth user, HttpServletRequest request) {
        // 查询账号信息
        UserInfo userInfo = userInfoDao.selectById(user.getUserInfoId());
        // 查询账号角色
        List<String> roleList = roleDao.listRolesByUserInfoId(userInfo.getId());
        // 查询账号点赞信息
        Set<Object> articleLikeSet = redisService.sMembers(RedisPrefixConst.ARTICLE_USER_LIKE + userInfo.getId());
        Set<Object> commentLikeSet = redisService.sMembers(RedisPrefixConst.COMMENT_USER_LIKE + userInfo.getId());
        Set<Object> talkLikeSet = redisService.sMembers(RedisPrefixConst.TALK_USER_LIKE + userInfo.getId());
        // 获取设备信息
        String ipAddress = IpUtils.getIpAddress(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        UserAgent userAgent = IpUtils.getUserAgent(request);
        //获取新评论合集
        Map<String, Object> map  = redisService.hGetAll(RedisPrefixConst.NEW_ARTICLE_COMMENT);
        List<NewCommentsVO> newCommentsList = new ArrayList<>();
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            Integer articleId = Integer.parseInt(entry.getKey());
            Integer newCommentsCount = (Integer)entry.getValue();
            ArticleDTO article = articleDao.getArticleById(articleId);
            newCommentsList.add(NewCommentsVO.builder()
                    .id(articleId)
                    .articleTitle(article.getArticleTitle())
                    .newCommentsCount(newCommentsCount)
                    .build());
        }
        // 封装权限集合
        return UserDetailDTO.builder()
                .id(user.getId())
                .loginType(user.getLoginType())
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(userInfo.getEmail())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .newCommentsList(newCommentsList)
                .talkLikeSet(talkLikeSet)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .isDisable(userInfo.getIsDisable())
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(LocalDateTime.now(ZoneId.of(ZoneEnum.SHANGHAI.getZone())))
                .build();
    }

}
