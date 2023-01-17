import {http} from "@/request/request";

import {uesrInfo} from "@/types/api/user"

// 上传访客信息
export const uploadUserInfo = () => http.post("/api/report");

//修改个人修改
export const updateUserInfo = (params: uesrInfo) => http.put("/api/users/info", params)

//修改头像
export const updateAvatar = () => http.post("/users/avatar")

//清除未读消息提示
export const deleteNewComments = (params: number) =>
  http.post("/api/deleteNewComment", { articleId: params });

