<template>
  <el-menu
    :default-active="this.$store.state.Tabs.editableTabsValue"
    class="el-menu-vertical-demo"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
  >
    <router-link to="/view/home">
      <el-menu-item index="Index" @click="selectMenu({name:'Index',title:'首页'})">
        <template slot="title">
          <i class="el-icon-s-home"></i>   
          <span slot="title">首页</span>
        </template>
      </el-menu-item>
    </router-link>
    <el-submenu :index="menu.name" :key="index" v-for="(menu,index) in menuList">
      <template slot="title">
        <i :class="menu.icon"></i>
        <span>{{menu.title}}</span>
      </template>
      <router-link :to="item.path" :key="to" v-for="(item,to) in menu.children">
        <el-menu-item :index="item.name" @click="selectMenu(item)">
          <template slot="title">
            <!-- vue3采用组件方式引入 -->
            <i :class="item.icon"></i>
            <span slot="title">{{item.title}}</span>
          </template>
        </el-menu-item>
      </router-link>
    </el-submenu>
  </el-menu>
</template>

<script>
export default {
  name: "SideMenu.vue",
  data() {
    console.log("this.$store.state.menus.menuList");
    console.log(this.$store.state.menus.menuList);
    return {
    };
  },
  computed:{
    menuList:{
      get(){
        return this.$store.state.menus.menuList;
      }
    },
  },
  methods:{
    selectMenu(item){
      this.$store.commit("addTab",item);
    }
  }
};
</script>

<style>
.el-menu-vertical-demo {
  height: 100vh;
}

a {
  text-decoration: none;
}
</style>