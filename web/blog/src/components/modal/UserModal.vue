<template>
  <a-modal v-model:visible="pinia.showLoginModal" :footer="null" width="600px" centered :closable="false"
    :bodyStyle="{ 'padding': '0', 'border-radius': '16px' }">
    <div :class="'owl' + state.owlStyle" id="owl">
      <div class="hand"></div>
      <div class="hand hand-r"></div>
      <div class="arms">
        <div class="arm"></div>
        <div class="arm arm-r"></div>
      </div>
    </div>
    <div class="form-container">
      <div class="pic-left">
      </div>
      <div class="form-right">
        <div class="form-header">
          <h3>{{ state.currentTabIndex == 1 ? '🥤登录🍔' : state.currentTabIndex == 2 ? '🍺注册🦞' : '☕重置密码🍩' }}</h3>
        </div>
        <div class="form-body">
          <a-form name="custom-validation" :model="state.formState" :rules="rules" @finish="getResponse"
            autocomplete="off" ref="formRef">
            <a-form-item name="username" :colon="false">
              <a-auto-complete v-model:value="state.formState.username" :options="state.options" placeholder="请输入邮箱"
                @search="onSearch" :bordered="false" allowClear />
            </a-form-item>
            <a-form-item name="validcode" :colon="false" v-if="state.currentTabIndex != 1">
              <div class="flex-sb">
                <a-input :bordered="false" v-model:value="state.formState.validcode" placeholder="请输入验证码" />
                <a-button type="primary" @click="sendValidCode(state.formState.username)" :disabled="state.sendFlag">
                  {{ state.sendBtnText }}</a-button>
              </div>
            </a-form-item>
            <a-form-item :bordered="false" name="password" :colon="false">
              <a-input-password v-model:value="state.formState.password" autocomplete="off" placeholder="请输入密码"
                :bordered="false" @focus="showOwl" @blur="hideOwl" />
            </a-form-item>
            <a-form-item style="text-align: center">
              <a-button type="primary" html-type="submit" style="width: 100%">{{ state.currentTabIndex == 1 ? '登陆' :
                  '确认'
              }}
              </a-button>
            </a-form-item>
          </a-form>
        </div>
        <div class="form-footer">
          <div v-if="state.currentTabIndex == 1">
            <div class="forget-register">
              <a @click="switchTab(3)">忘记密码?</a>
              <a @click="switchTab(2)">立即注册</a>
            </div>
            <a-divider orientation="center">其它方式登录</a-divider>
            <div class="social-login">
              <icon-font type="icon-QQ" style="font-size: 32px" @click="qqLogin" />
              <icon-font type="icon-weixin" style="font-size: 32px" />
            </div>
          </div>
          <div style="text-align: center" v-else>
            <a @click="switchTab(1)">已有账号?点击登录</a>
          </div>
        </div>
      </div>
    </div>
  </a-modal>
</template>
<script setup lang="ts">
import { reactive, ref } from "vue";
import config from "@/config/config";
import { message } from "ant-design-vue";
import { createFromIconfontCN } from "@ant-design/icons-vue";
import {
  userLogin,
  userRegister,
  sendCode,
  changePassword,
} from "@/api/auth";
import { useStore } from "@/store/index";
import { useRoute } from "vue-router";
import { loginParams } from "@/types/api/auth"
import { any } from "vue-types";
const route = useRoute();
const pinia = useStore();
const IconFont = createFromIconfontCN({
  scriptUrl: config.ICON_FONT_URL,
});
const formRef = ref();
const state = reactive({
  owlStyle: "",
  options: [] as any,
  formState: {
    username: "",
    password: "",
    validcode: "",
  },
  currentTabIndex: 1,
  sendFlag: false,
  timeCount: 60,
  sendBtnText: "发送"
})
const showOwl = () => {
  state.owlStyle = ' password'
}
const hideOwl = () => {
  state.owlStyle = ''
}
const mockVal = (str: string, suffix: string) => {
  return {
    value: str + suffix
  };
};

const onSearch = (searchText: any) => {
  state.options = !searchText ? [] :
    [mockVal(searchText, "@qq.com"), mockVal(searchText, "@163.com"), mockVal(searchText, "@sina.com"),
    mockVal(searchText, "@foxmail.com"), mockVal(searchText, "@gmail.com"), mockVal(searchText, "@126.com")];
};

const resetForm = () => {
  formRef.value.resetFields();
  //state.currentTabIndex = 1
};
const switchTab = (key: number) => {
  state.currentTabIndex = key
  //清空表单
  resetForm();
};

const sendValidCode = (username: string) => {
  // eslint-disable-next-line no-undef
  //@ts-ignore
  let captcha = new TencentCaptcha(config.TENCENT_CAPTCHA, async () => {
    countDown();
    await sendCode(username)
    message.success("发送成功")
  });

  //显示滑动验证码
  captcha.show();
};
//验证码倒计时
const countDown = () => {
  state.sendFlag = true;
  let timer = setInterval(() => {
    state.timeCount--;
    state.sendBtnText = state.timeCount + "s";
    if (state.timeCount <= 0) {
      clearInterval(timer);
      state.sendBtnText = "发送";
      state.timeCount = 60;
      state.sendFlag = false;
    }
  }, 1000);
};
//qq授权登录
const qqLogin = () => {
  //保留当前路径
  pinia.saveLoginUrl(route.fullPath);
  //发起验证
  if (
    navigator.userAgent.match(
      /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
    )
  ) {
    // eslint-disable-next-line no-undef
    //@ts-ignore
    QC.Login.showPopup({
      appId: config.QQ_APP_ID,
      redirectURI: config.QQ_REDIRECT_URI,
    });
  } else {
    window.open(
      "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=" +
      +config.QQ_APP_ID +
      "&response_type=token&scope=all&redirect_uri=" +
      config.QQ_REDIRECT_URI,
      "_self"
    );
  }
};
const getResponse = async () => {
  //请求滑动验证码
  //@ts-ignore
  var captcha = new TencentCaptcha(config.TENCENT_CAPTCHA, async (res: any) => {
    if (res.ret === 0) {
      //登录
      if (state.currentTabIndex === 1) {
        let params: loginParams = {
          username: state.formState.username,
          password: state.formState.password
        }
        const data = await userLogin(params)
        pinia.showLoginModal = false;
        pinia.isLogin = true;
        pinia.saveLoginUserInfo(data);
        message.success("登录成功");
      }
      //注册
      else if (state.currentTabIndex === 2) {
        let params: loginParams = {
          username: state.formState.username,
          password: state.formState.password,
          code: state.formState.validcode,
        }
        await userRegister(params)
        state.currentTabIndex = 1
        message.success("注册成功");
      }
      //重置密码
      else {
        let params: loginParams = {
          username: state.formState.username,
          password: state.formState.password,
          code: state.formState.validcode
        }
        await changePassword(params)
        state.currentTabIndex = 1
        message.success("修改成功");
      }
    }
  });
  // 显示滑动验证码
  captcha.show();
};
//验证码规则
let validateCode = async (_rule: any, value: any) => {
  if (value === "") {
    return Promise.reject("验证码不能为空");
  }

  if (value.length != 6) {
    return Promise.reject("验证码的长度为6");
  } else {
    return Promise.resolve();
  }
};
//邮箱验证规则
let validateUsername = async (_rule: any, value: any) => {
  if (value === "") {
    return Promise.reject("邮箱不能为空");
  }
  var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
  if (!reg.test(value)) {
    return Promise.reject("请输入正确格式的邮箱");
  } else {
    return Promise.resolve();
  }
};
//密码验证规则
let validatePassword = async (_rule: any, value: any) => {
  if (value === "") {
    return Promise.reject("密码不能为空");
  }
  if (value.length < 6 || value.length > 16) {
    return Promise.reject("密码的长度必须在6-16位之间");
  } else {
    return Promise.resolve();
  }
};

const rules = {
  username: [
    {
      validator: validateUsername,
      //trigger: 'change',
    },
  ],
  password: [
    {
      validator: validatePassword,
      trigger: "change",
    },
  ],
  validcode: [
    {
      validator: validateCode,
      trigger: "change",
    },
  ],
};
</script>
<style lang="less" scoped>
/* 接下来是猫头鹰的样式 */
.owl {
  width: 211px;
  height: 108px;
  /* 背景图片 */
  background: url("https://upload.linkstarted.top/cloud/1c0a045d44d61609399c4b1a51ada0e6.png") no-repeat;
  /* 绝对定位 */
  position: absolute;
  top: -100px;
  /* 水平居中 */
  left: 50%;
  transform: translateX(-50%);
  z-index: 2;
}

.owl .hand {
  width: 34px;
  height: 34px;
  border-radius: 40px;
  background-color: #472d20;
  /* 绝对定位 */
  position: absolute;
  left: 12px;
  bottom: -8px;
  /* 沿Y轴缩放0.6倍（压扁） */
  transform: scaleY(0.6);
  /* 动画过渡 */
  transition: 0.3s ease-out;
}

.owl .hand.hand-r {
  left: 170px;
}

.owl.password .hand {
  transform: translateX(42px) translateY(-15px) scale(0.7);
}

.owl.password .hand.hand-r {
  transform: translateX(-42px) translateY(-15px) scale(0.7);
}

.owl .arms {
  position: absolute;
  top: 58px;
  width: 100%;
  height: 41px;
  overflow: hidden;
}

.owl .arms .arm {
  width: 40px;
  height: 65px;
  position: absolute;
  left: 20px;
  top: 40px;
  background: url("https://upload.linkstarted.top/cloud/52c30e8b6c6180b7ad2317f51356f174.png") no-repeat;
  transform: rotate(-20deg);
  transition: 0.3s ease-out;
}

.owl .arms .arm.arm-r {
  transform: rotate(20deg) scaleX(-1);
  left: 158px;
}

.owl.password .arms .arm {
  transform: translateY(-40px) translateX(40px);
}

.owl.password .arms .arm.arm-r {
  transform: translateY(-40px) translateX(-40px) scaleX(-1);
}

.form-container {
  display: flex;

  .pic-left {
    z-index: 1;
    position: relative;
    width: 250px;
    height: 400px;
    overflow: hidden;
    background: url("https://upload.linkstarted.top/cloudfile/anya.jpg") center no-repeat;
    background-size: cover;
  }

  .form-right {
    width: 350px;
    padding: 16px;

    .form-header {
      color: #303133;
      font-weight: bold;
      font-size: 1.2rem;
      margin-bottom: 24px;
      text-align: center;
    }

    .form-footer {

      display: flex;
      flex-direction: column;

      .forget-register {
        display: flex;
        justify-content: space-between;
      }

      .social-login {
        display: flex;
        justify-content: space-between;
        margin: 0 96px;
      }

      .test-account {
        text-align: center;
        margin-top: 12px;
      }
    }
  }

}
</style>
