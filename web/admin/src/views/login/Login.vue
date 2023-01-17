<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-banner">
        <!-- <img src="https://upload.linkstarted.top/cloudfile/anya.jpg" /> -->
      </div>
      <!-- 登录表单 -->
      <div class="login-form">
        <div class="login-title">
          <h3>欢迎来到博客管理端！👻</h3>
        </div>
        <el-form status-icon :model="loginForm" :rules="rules" ref="ruleForm">
          <el-form-item prop="username">
            <el-input v-model="loginForm.username" prefix-icon="el-icon-user-solid" placeholder="用户名"
              @keyup.enter.native="login" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" prefix-icon="iconfont el-icon-mymima" show-password placeholder="密码"
              @keyup.enter.native="login" />
          </el-form-item>
        </el-form>
        <!-- 登录按钮 -->
        <el-button type="primary" @click="login">登录</el-button>
        <div class="test-login">
          <el-divider>测试账号一键登录</el-divider>
          <div style="text-align: center;">
            <el-button type="primary" round size="small" style="width: 100px;" plain @click="login(2)">测试用户</el-button>
          </div>
        </div>
      </div>
    </div>
    <div class="footer">
      Copyright © 2020-2022 MongoBlog-Admin by Roy
    </div>
  </div>
</template>

<script>
import { generaMenu } from "../../assets/js/menu";
export default {
  data: function () {
    return {
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        password: [{ required: true, message: "密码不能为空", trigger: "blur" }]
      }
    };
  },
  methods: {
    login(type) {
      const submit = () => {
        const that = this;
        // eslint-disable-next-line no-undef
        var captcha = new TencentCaptcha(
          this.config.TENCENT_CAPTCHA,
          function (res) {
            if (res.ret === 0) {
              //发送登录请求
              let param = new URLSearchParams();
              param.append("username", type == 2 ? 'test@qq.com' : that.loginForm.username);
              param.append("password", type == 2 ? '123456789' : that.loginForm.password);
              that.axios.post("/api/login", param).then(({ data }) => {
                if (data.flag) {
                  // 登录后保存用户信息
                  that.$store.commit("login", data.data);
                  // 加载用户菜单
                  generaMenu();
                  that.$message.success("登录成功");
                  that.$router.push({ path: "/" });
                } else {
                  that.$message.error(data.message);
                }
              });
            }
          }
        );
        // 显示验证码
        captcha.show();
      }
      if (type == 2) {
        submit();
      } else {
        this.$refs.ruleForm.validate(valid => {
          if (valid) {
            submit();
          } else {
            return false;
          }
        });
      }
    },
  }
};
</script>

<style scoped>
.footer {
  width: 100%;
  position: fixed;
  bottom: 20px;
  text-align: center;
  color: #909399;
}

.test-login {
  margin-top: 48px;
}

.login-banner {
  position: relative;
  width: 450px;
  overflow: hidden;
  min-height: 500px;
  max-height: 550px;
  background: url("https://upload.linkstarted.top/cloudfile/anya.jpg") center no-repeat;
  background-size: cover;

}

.login-container {
  position: fixed;
  z-index: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at center, #fafafa, #f2f3f5);
}

.login-card {
  /* width: 800px;
  height: 800px; */
  display: flex;
  justify-content: space-between;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%) translateY(-50%);
  background-color: #ffffff;
  border-radius: 10px;
  overflow: hidden;
  box-shadow: 0px 12px 32px 4px rgba(0, 0, 0, .04), 0px 8px 20px rgba(0, 0, 0, .08);
}

.login-title {
  color: #303133;
  font-weight: bold;
  font-size: 1.2rem;
  margin-bottom: 24px;
  text-align: center;
}

.login-form {
  /* margin-top: 1.2rem; */
  width: 450px;
  padding: 50px;
}

.login-card button {
  margin-top: 1rem;
  width: 100%;
}
</style>
