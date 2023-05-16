<template>
  <div class="div_from">
    <h2>你好！{{ userInfo.username }} 同学</h2>
    <el-form
      :status-icon="rules"
      ref="passFrom"
      rules="rules"
      label-width="100px"
      :model="passForm"
    >
      <el-form-item label="旧密码" prop="currentPass">
        <el-input
          type="password"
          auto-complete="off"
          v-model="passForm.currentPass"
        ></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="password">
        <el-input
          type="password"
          auto-complete="off"
          v-model="passForm.password"
        ></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input
          type="password"
          auto-complete="off"
          v-model="passForm.checkPass"
        ></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submitForm('passForm')"
          >submit</el-button
        >
        <el-button @click="resetFrom('passForm')">Reset</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: "usercenter",
  data() {
    var validatePass = (rules, value, callback) => {
      if (value === "") {
        callback(new Error("again push password"));
      } else if (value !== this.passForm.password) {
        callback(new Error("second password error"));
      } else {
        callback();
      }
    };
    return {
      userInfo: {},
      passForm: {
        password: "",
        checkPass: "",
        currentPass: "",
      },
      rules: {
        password: [
          {
            required: true,
            message: "please input new password",
            trigger: "blur",
          },
          { min: 6, max: 12, message: "6-12 str", trigger: "blur" },
        ],
        checkPass: [
          { required: true, validator: validatePass, trigger: "blur" },
        ],
        currentPass: [
          {
            required: true,
            message: "please input history password",
            trigger: "blur",
          },
        ],
      },
    };
  },

  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        this.userInfo = res.data.obj;
      });
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this;
          this.$axios
            .post("/sys/user/updataPass", this.passForm)
            .then((res) => {
              _this.$alert(res.data.msg, "tips", {
                confirmButtonText: "accept",
                callback: (action) => {
                  this.$refs[formName].resetFields();
                },
              });
            });
        } else {
          console.log("error submit");
          return false;
        }
      });
    },
    resetFrom(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style>
.div_from {
  text-align: center;
}

.el-from {
  width: 420px;
  margin: 50px auto;
}
</style>