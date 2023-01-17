package com.jjhroy.blog.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.jjhroy.blog.constant.HexoConst;
import com.jjhroy.blog.enums.ArticleStatusEnum;
import com.jjhroy.blog.enums.ArticleTypeEnum;
import com.jjhroy.blog.exception.BizException;
import com.jjhroy.blog.service.ArticleService;
import com.jjhroy.blog.strategy.ArticleImportStrategy;
import com.jjhroy.blog.vo.HexoArticleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hexo文章导入策略
 *
 * @author linweiyuan
 * @date 2022/07/28
 */
@Slf4j
@Service("hexoArticleImportStrategyImpl")
public class HexoArticleImportStrategyImpl implements ArticleImportStrategy {
    @Autowired
    private ArticleService articleService;

    /**
     * hexo最大分隔符数
     */
    private final int HEXO_MAX_DELIMITER_COUNT = 2;

    /**
     * hexo最小分隔符数
     */
    private final int HEXO_MIN_DELIMITER_COUNT = 1;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void importArticles(MultipartFile file) {
        try {
            HexoArticleVO hexoArticleVO = new HexoArticleVO();
            // 原创
            hexoArticleVO.setType(ArticleTypeEnum.ORIGINAL.getType());
            // 公开 （DRAFT不保存分类）
            hexoArticleVO.setStatus(ArticleStatusEnum.PUBLIC.getStatus());

            AtomicInteger hexoDelimiterCount = new AtomicInteger();
            StringBuilder articleContent = new StringBuilder();

            // 分类或标签标记
            AtomicInteger flag = new AtomicInteger(HexoConst.NORMAL_FLAG);

            List<String> tagList = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            reader.lines().forEach(line -> {
                if (hexoDelimiterCount.get() == HEXO_MAX_DELIMITER_COUNT) {
                    // 分隔符结束就是正文
                    articleContent.append(line).append(HexoConst.NEW_LINE);
                } else {
                    if (line.equals(HexoConst.DELIMITER)) {
                        hexoDelimiterCount.getAndIncrement();
                    }
                    if (hexoDelimiterCount.get() == HEXO_MIN_DELIMITER_COUNT) {
                        if (line.startsWith(HexoConst.TITLE_PREFIX)) {
                            hexoArticleVO.setArticleTitle(line.replace(HexoConst.TITLE_PREFIX, "").trim());
                        } else if (line.startsWith(HexoConst.DATE_PREFIX)) {
                            hexoArticleVO.setCreateTime(LocalDateTime.parse(line.replace(HexoConst.DATE_PREFIX, "").trim(), formatter));
                        } else if (line.startsWith(HexoConst.CATEGORIES_PREFIX)) {
                            flag.set(HexoConst.CATEGORY_FLAG);
                        } else if (line.startsWith(HexoConst.TAGS_PREFIX)) {
                            flag.set(HexoConst.TAG_FLAG);
                        } else if (line.startsWith(HexoConst.PREFIX) && flag.intValue() == HexoConst.CATEGORY_FLAG) {
                            hexoArticleVO.setCategoryName(line.replace(HexoConst.PREFIX, "").trim());
                        } else if (line.startsWith(HexoConst.PREFIX) && flag.intValue() == HexoConst.TAG_FLAG) {
                            tagList.add(line.replace(HexoConst.PREFIX, "").trim());
                        }
                    }
                }
            });

            hexoArticleVO.setTagNameList(tagList);
            hexoArticleVO.setArticleContent(articleContent.toString());

            // 如果分类或标签为空则设为草稿
            if (CollectionUtils.isEmpty(hexoArticleVO.getTagNameList()) || StrUtil.isBlank(hexoArticleVO.getCategoryName())) {
                hexoArticleVO.setStatus(ArticleStatusEnum.DRAFT.getStatus());
            }

            articleService.saveOrUpdateArticle(hexoArticleVO);
        } catch (IOException e) {
            log.error(StrUtil.format("导入Hexo文章失败, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("导入Hexo文章失败");
        }
    }

}
