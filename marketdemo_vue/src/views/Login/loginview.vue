
<!-- 登录页面窗口 -->
<template>
  <!-- element布局引用 6：1：6 -->
  <el-row type="flex" class="row-bg" justify="center">
    <!-- 6：左侧标题+图片 -->
    <el-col :span="6">
      <h3>left title</h3>

      <el-image
        class="left-image"
        :src="require('@/assets/logo.png')"
      ></el-image>

      <p>111</p>
      <p>222</p>
    </el-col>
    <!-- 1：中间分隔栏 -->
    <el-col :span="1">
      <el-divider direction="vertical"></el-divider>
    </el-col>
    <!-- 6：右侧登录表单 -->
    <el-col :span="6">
      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginForm"
        label-width="100px"
        class="demo-loginForm"
      >
        <el-form-item class="username" label="用户名" prop="username">
          <el-input v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item class="password" label="密码" prop="password">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <div class="code">
          <el-form-item class="code-input" label="验证码" prop="code">
            <el-input v-model="loginForm.code"></el-input>
          </el-form-item>
          <el-image
            :src="captchaImg"
            class="captchaImg"
            @click="getCaptcha"
          ></el-image>
        </div>
        <el-form-item class="button-01">
          <el-button type="primary" @click="submitForm('loginForm')">
            Log in
          </el-button>
          <el-button @click="resetForm('loginForm')">reset</el-button>
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>

  <!-- </div> -->
</template>

<script>
// import axios from "@/axios/axios.js";
import qs from "qs";

export default {
  name: "login",
  data() {
    return {
      loginForm: {
        username: "",
        password: "",
        code: "",
        token: "",
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 5, max: 5, message: "长度在 5 个字符", trigger: "blur" },
        ],
      },
      captchaImg: null,
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios
            .post(
              "/login?" +
                "username=" +
                this.loginForm.username +
                "&password=" +
                this.loginForm.password +
                "&code=" +
                this.loginForm.code +
                "&token=" +
                this.loginForm.token
            )
            .then((res) => {
              // test
              console.log("this res:");
              console.log(res);
              console.log(
                "res.headers.Authorization: " + res.headers.authorization
              );
              console.log(
                "loginurl: " + "/login?" + qs.stringify(this.loginForm)
              );

              // TODO：注释注意
              // 注：后期需要弄成jwt，这里用mockjs生成的token替代
              // const jwt=res.data.data.token
              // 这里注意对应的是res.config.headers的token
              // if (res.config.headers["Authorization"] == "undefined") {
              //   console.log("进入(res.config.headers['Authorization'] == null")
              //   res.config.headers["Authorization"] = res.headers.authorization;
              // }

              // const jwt = res.config.headers["Authorization"];
              const jwt = res.headers.authorization;

              console.log("jwt/res.config.headers['Authorization']: " + jwt);

              // store 设置回传 token
              this.$store.commit("SET_TOKEN", jwt);

              // 跳转/view/home
              this.$router.push("/view/home");
            });

          // alert("submit!");
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    // 获取验证码图片
    getCaptcha() {
      this.$axios.get("/captcha").then((res) => {
        // test
        console.log("/test");
        console.log(res);
        console.log("res.data.obj.captchaImg: " + res.data.obj.captchaImg);
        console.log("res.data.obj.token: " + res.data.obj.token);

        this.captchaImg = res.data.obj.captchaImg;
        this.loginForm.token = res.data.obj.token;
        this.loginForm.code = "";

        // 日志输出 浏览器信息
        console.log("loginForm.token: " + this.loginForm.token);
        console.log("captchaImg: " + this.captchaImg);
      });
    },
  },
  created() {
    this.getCaptcha();
  },
};
</script>

<style>
.row-bg {
  height: 100vh;
  background-color: #fafafa;
  display: flex;
  align-items: center;
  text-align: center;
}

.left-image {
  width: 150px;
  height: 150px;
}

.el-divider {
  height: 200px;
}

.username,
.password {
  width: 380px;
  padding-top: 5px;
}
.code {
  width: 380px;
  display: flex;
}

.code-input {
  width: 200px;
  padding-top: 5px;
}

.captchaImg {
  width: 120px;
  height: 40px;
  margin-top: 5px;
  border-radius: 5px;
  margin-left: 8px;
}

.button-01 {
  display: inline-block;
}
</style>