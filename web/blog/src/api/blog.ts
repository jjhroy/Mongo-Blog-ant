import { http } from "@/request/request";
import { linkParams } from "@/types/api/blog"

//获取博客信息
export const getBlogInfo = () => http.get("/api/");

//获取分类数据
export const getCategoryData = () => http.get("/api/categories");

//获取标签数据
export const getTagsData = () => http.get("/api/tags");

//获取归档数据
export const getArchiveData = (params: number) =>
  http.get(`/api/articles/archives?current=${params}`);

//友链信息
export const listFriendLink = () => http.get("/api/links");

//申请添加友链
export const addFriendLink = (params: linkParams) =>
  http.post("/api/admin/links", params);



