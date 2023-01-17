import { http } from "@/request/request";
import { condition } from "@/types/api/article"

//获取文章列表
export const getArticleList = (page: number, type: number) =>
    http.get("/api/articles", { params: { current: page, type: type } });

//根据分类或标签获取文章列表
export const getArticleListBy = (params: condition) =>
    http.get("/api/articles/condition", { params });

//获取文章详情
export const getArticleDetail = (params: string) => http.get(`/api${params}`);

//点赞文章
export const likeArticle = (params: number) =>
    http.post(`/api/articles/${params}/like`);

//搜索
export const searchArticle = (params: string) =>
    http.get("/api/articles/search", {
        params: { current: 1, keywords: params },
    });