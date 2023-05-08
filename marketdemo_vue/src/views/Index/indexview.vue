<template>
  <el-container>
    <el-aside width="200px">
      <!-- Aside -->
      <Menu></Menu>
    </el-aside>
    <el-container>
      <el-header>
        <strong>后台管理系统</strong>
        <div class="header-avatar">
          <el-avatar :src="userInfo.avatar"></el-avatar>
          <el-dropdown>
            <span class="el-dropdown-link">
              {{ userInfo.username }}
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <router-link :to="{ name: 'usercenter' }">
                  usermanage
                </router-link>
              </el-dropdown-item>
              <el-dropdown-item @click.native="logout">logout</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-link href="https://www.bilibili.com/" target="_black">
            web1
          </el-link>
          <el-link href="https://element.eleme.cn/" target="_black">
            web2
          </el-link>
        </div>
      </el-header>
      <el-main style="padding: 0;">
        <Tabs></Tabs>
        <div class="main_div01">
          <router-view></router-view>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Menu from "./menu.vue";
import Tabs from "./Tabs.vue";
export default {
  name: "index.vue",
  components: {
    Menu,
    Tabs,
  },
  data() {
    return {
      userInfo: {
        id: "",
        username: "",
        avatar: "",
      },
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      this.$axios.get("/sys/userInfo").then((res) => {
        console.log(res);
        this.userInfo = res.data.data;
      });
    },
    logout() {
      this.$axios.post("/logout").then((resp) => {
        localStorage.clear();
        sessionStorage.clear();
        this.$store.commit("resetState");
        this.$router.push("/login");
      });
    },
  },
};
</script>

<style>
.main_div01{
  padding: 0 15px;
}

.el-dropdown-link {
  cursor: pointer;
}

.header-avatar {
  float: right;
  width: 210px;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  /* text-align: center; */
  padding: 0px;
}

body > .el-container {
  height: 100vh;
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>