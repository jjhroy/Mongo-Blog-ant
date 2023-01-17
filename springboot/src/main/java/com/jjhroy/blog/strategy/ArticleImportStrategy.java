package com.jjhroy.blog.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文章导入策略
 *
 * @author linweiyuan
 * @date 2022/07/28
 */
public interface ArticleImportStrategy {

    /**
     * 导入文章
     *
     * @param file 文件
     */
    void importArticles(MultipartFile file);
}
