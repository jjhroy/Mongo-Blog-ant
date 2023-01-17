import {http} from "@/request/request";

import {commentListParams,commentParams,replayListParams,commentList } from "@/types/api/comment"


//新增评论或回复
export const addComment = (params: commentParams) =>
  http.post("/api/comments", params);

//评论列表
export const commentsList = (params: commentListParams) =>
  http.get<commentList>("/api/comments", { params });

//回复列表
export const repliesList = (params: replayListParams, type: number) =>
  http.get(`/api/comments/${type}/replies`, { params });

//评论点赞
export const likeComment = (params: number) =>
  http.post(`/api/comments/${params}/like`);
