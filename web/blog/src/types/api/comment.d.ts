export interface commentParams {
  commentContent: string;
  type: number;
  topicId: any;
}

export interface commentListParams {
  current: number;
  type: number;
  topicId: any;
}

export interface replayListParams {
  current: number;
  size: number;
}

export interface replayStates {
  type: number,
  replyUserId: number,
  parentId: number,
  topicId: any,
  nickname: string,
  index: number,
}

export interface commentList {
  count: number,
  recordList: comment[],
}

export interface replyDTO {
  parentId: number,
  /**
   * 评论id
   */
  id: number

  /**
   * 用户id
   */
  userId: number

  /**
   * 用户昵称
   */
  nickname: string

  /**
   * 用户头像
   */
  avatar: string

  /**
   * 个人网站
   */
  webSite: string

  /**
   * 评论内容
   */
  commentContent: string

  /**
   * 点赞数
   */
  likeCount: number

  /**
   * 评论时间
   */
  createTime: Date

  /**
   * 回复量
   */
  replyCount: number

  replyNickname: string
  replyUserId: number
  replyWebSite: string
}

export interface comment {
  replayCurrent: number,
  showReplayPaging: boolean,
  /**
  * 评论id
  */
  id: number

  /**
   * 用户id
   */
  userId: number

  /**
   * 用户昵称
   */
  nickname: string

  /**
   * 用户头像
   */
  avatar: string

  /**
   * 个人网站
   */
  webSite: string

  /**
   * 评论内容
   */
  commentContent: string

  /**
   * 点赞数
   */
  likeCount: number

  /**
   * 评论时间
   */
  createTime: Date

  /**
   * 回复量
   */
  replyCount: number

  /**
   * 回复列表
   */
  replyDTOList: replyDTO[]

  /**
   * 用户ip
   */
  ipAddress: string

  /**
   * ip来源
   */
  ipSource: string
}