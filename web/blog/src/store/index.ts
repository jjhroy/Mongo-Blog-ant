// @ts-nocheck
import { Themes } from "md-editor-v3";
import { defineStore } from "pinia";
import { newCommnet } from "../types/store/strore"
export const useStore = defineStore("pinia", {
  state: () => {
    return {
      blogInfo: {
        viewsCount: "",
        articleCount: 0,
        categoryCount: 0,
        tagCount: 0,
        websiteConfig: {
          articleCover: "",
          websiteNotice: "",
          touristAvatar: "",
          userAvatar: "",
          websiteAuthor: "",
          websiteAvatar: "",
          websiteCover: "",
          websiteCreateTime: "",
          websiteIntro: "",
          websiteName: "",
          websiteRecordNo: "",
        }
      },
      loginUserInfo: {
        articleLikeSet: [],
        avatar: "",
        commentLikeSet: [],
        email: "",
        id: 1,
        intro: "",
        ipAddress: "",
        ipSource: "",
        lastLoginTime: 0,
        loginType: 1,
        newCommentsList: [] as newCommnet[],
        nickname: "",
        talkLikeSet: [],
        userInfoId: 1,
        username: "",
        webSite: "",
      },
      isLogin: false,
      showLoginModal: false,
      showSearchModal: false,
      showMessageModal: false,
      loginUrl: "",
      themeName: 'light' as Themes
    };
  },
  actions: {
    switchTheme(theme: boolean) {
      if (theme) {
        this.themeName = 'light'
      } else {
        this.themeName = 'dark'
      }
    },
    saveBlogInfo(blogInfo: any) {
      this.blogInfo = blogInfo;
    },
    saveLoginUserInfo(loginUserInfo: any) {
      this.isLogin = true;
      this.loginUserInfo = loginUserInfo;
    },
    logout() {
      this.loginUserInfo = {};
      this.isLogin = false;
    },
    commentLike(commentId: number) {
      //var commentLikeSet = this.loginUserInfo.commentLikeSet;
      if (this.loginUserInfo.commentLikeSet.indexOf(commentId) != -1) {
        this.loginUserInfo.commentLikeSet.splice(
          this.loginUserInfo.commentLikeSet.indexOf(commentId),
          1
        );
      } else {
        this.loginUserInfo.commentLikeSet.push(commentId);
      }
    },
    articleLike(articleId: number) {
      //var commentLikeSet = this.loginUserInfo.commentLikeSet;
      if (this.loginUserInfo.articleLikeSet.indexOf(articleId) != -1) {
        this.loginUserInfo.articleLikeSet.splice(
          this.loginUserInfo.articleLikeSet.indexOf(articleId),
          1
        );
      } else {
        this.loginUserInfo.articleLikeSet.push(articleId);
      }
    },
    clearMessage(id: number) {
      this.loginUserInfo.newCommentsList.map(
        (item: { id: number; newCommentsCount: number }) => {
          if (item.id == id) {
            item.newCommentsCount = 0;
          }
        }
      );
    },
    saveLoginUrl(url: string) {
      this.loginUrl = url;
    },
    openLoginModal() {
      this.showLoginModal = true;
    }
  },
  persist: {
    enabled: true, // 开启数据缓存
    strategies: [
      {
        storage: sessionStorage,
        paths: ["blogInfo", "loginUserInfo", "isLogin", "themeClass"], //只对指定的state进行缓存
      },
    ],
  },
});
