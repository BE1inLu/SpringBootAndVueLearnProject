import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default ({
  state: {

    // 菜单列表
    menuList: [],
    // 权限列表
    PermList: [],
    // route获取判定
    hasRoute: false,
  },
  getters: {

  },
  mutations: {
    SetMenuList(state, menus) {
      state.menuList = menus;
    },
    SetPermList(state, perm) {
      state.PermList = perm;
    },
    changeRouteStatus(state, hasRoute) {
      state.hasRoute = hasRoute;
      sessionStorage.setItem('hasRoute', hasRoute);
    },
  },
  actions: {

  }
})