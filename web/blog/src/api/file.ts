import {http} from "@/request/request";

//文件上传
export const uploadFile = (params: any) =>
  http.post("/api/upload/cloud", params);

//上传列表
export const getAllFileName = () => http.post("/api/upload/getName");