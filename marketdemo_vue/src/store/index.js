import Vue from 'vue'
import Vuex from 'vuex'
import menus from './modules/menus'
import Tabs from './modules/Tabs'
Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: ''
  },
  getters: {
  },
  mutations: {
    SET_TOKEN: (state, token) => {

      // test
      console.log("store set_token func")
      console.log("token")
      console.log(token)

      state.token = token
      localStorage.setItem("token", token)

      // test
      console.log("localstorage")
      console.log(localStorage)
    },
    resetState: (state) => {
      state.menuList = [],
        state.PermList = [],
        state.hasRoute = false,
        state.editableTabsValue = "Index",
        state.editableTabs = [
          {
            title: "首页",
            name: "Index",
          }
        ]
    }
  },
  actions: {
  },
  modules: {
    menus,
    Tabs,
  }
})
