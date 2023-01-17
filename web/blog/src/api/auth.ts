import { http } from "@/request/request";
import { loginParams, authLoginParams, emailParams } from "@/types/api/auth"
//设置为form-dara的数据请求格式
const config = {
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  transformRequest: [function (data: any) {
    let ret = '';
    let num = 1;
    for (let it in data) {
      if (Object.keys(data).length === num) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]);
      } else {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      num++;
    }
    return ret;
  }]
}

//用户登录
export const userLogin = (params: loginParams) =>
  http.post("/api/login", params, config);

//qq验证登录
export const qqAuthLogin = (params: authLoginParams) =>
  http.post("/api/users/oauth/qq", params);

//用户注册
export const userRegister = (params: loginParams) =>
  http.post("/api/register", params);

//验证码发送
export const sendCode = (params: string) =>
  http.get(`/api/users/code?username=${params}`);

//密码修改
export const changePassword = (params: loginParams) =>
  http.put("/api/users/password", params);

//邮箱修改
export const changeEmail = (params: emailParams) =>
  http.post("/api/users/email", params);