import { article, tag } from './blog.d';
export interface condition {
    categoryId?: any
    tagId?: any
    current: number
}

export interface articles {
    id: number
    articleCover: string
    articleTitle: string
    articleContent: string
    createTime: Date
    isTop: number
    type: number
    categoryId: number
    categoryName: string
    tagDTOList: tag[]
}

export interface articleList {
    name: string
    articlePreviewDTOList: articlePreviewList[]
}

export interface articlePreviewList {
    id: number
    articleCover: string
    articleTitle: string
    createTime: Date
    categoryId: number
    categoryName: string
    tagDTOList: tag[]
}

export interface articleDetails {
    id: number
    articleTitle: string
    articleCover: string
    articleContent: string
    createTime: Date
    updateTime: Date
    tagDTOList: tag[]
    categoryId: number
    categoryName: string
    originalUrl: string
    likeCount: number
    viewsCount: number
    type: number
    recommendArticleList: recommendArticle[]
    nextArticle: {
        id: number
        articleCover: string
        articleTitle: string
    },
    lastArticle: {
        id: number
        articleCover: string
        articleTitle: string
    },
}

export interface recommendArticle {
    id: number
    articleTitle: string
    articleCover: string
    createTime: Date
}

export interface Theme{
    themeName:string
}