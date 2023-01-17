// export interface registerParams {
//   username: string;
//   password: string;
//   code?: number;
// }

export interface loginParams {
  username: string;
  password: string;
  code?: string;
}

export interface authLoginParams {
  openId: string;
  accessToken: string;
}

export interface emailParams {
  email: string;
  code: number;
}