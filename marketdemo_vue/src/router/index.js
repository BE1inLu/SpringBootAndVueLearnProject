import Vue from 'vue'
import VueRouter from 'vue-router'
import axios from 'axios'

import HomeView from '@/views/Home/homeview.vue'
import IndexView from '@/views/Index/indexview.vue'
import DictView from '@/views/DictManage/dict.vue'
import MenuView from '@/views/MenuManage/menu.vue'
import RoleView from '@/views/RoleManage/role.vue'
import UserView from '@/views/UserManage/user.vue'
import LoginView from '@/views/Login/loginview.vue'
import UserCenterView from '@/views/UserCenter/usercenter.vue'

import store from '@/store'
import menus from '@/store/modules/menus'


Vue.use(VueRouter)



const routes = [
  {
    path: '/index',
    name: 'index',
    component: IndexView,
    children: [
      {
        path: '/view/home',
        name: 'Index',
        meta: {
          title: '首页',
        },
        component: HomeView,
      },
      {
        // 用户个人信息修改
        path: '/usercenter',
        name: 'usercenter',
        component: UserCenterView,
      },
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  { path: '/', redirect: '/login' },
];






const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});

// 路由守卫
router.beforeEach((to, from, next) => {

  // test
  console.log("路由守卫进入");
  console.log("获取localStorage.token");
  // localStorage.clear();
  // sessionStorage.clear();
  console.log(localStorage.getItem("token"));

  // 获取store各类参数
  let hasRoute = store.state.menus.hasRoute;
  let menus = store.state.menus.menuList;
  let token = localStorage.getItem("token");

  // test
  console.log("hasRoute");
  console.log(hasRoute);
  console.log("menuList");
  console.log(menus);
  console.log("token");
  console.log(token);

  // 页面判断
  if (to.path == '/login') {
    console.log("login page");
    next();
  } else if (!token) {
    console.log("token not get it");
    next({ path: '/login' });
  } else if (to.path == '/' || to.path == '') {
    next({ path: "/index" });
  } else if (!hasRoute) {

    console.log("路由绑定");

    // 动态绑定路由
    let newRoutes = router.options.routes;

    console.log("newRoutes");
    console.log(newRoutes);

    // axios请求
    console.log("axios请求后端nav");

    axios.get("/sys/menu/nav", {
      headers: {
        Authorization: localStorage.getItem("token"),
      }
    }).then((res) => {

      // test
      console.log("后端回传的导航栏navjson：res.data.obj.nav");
      console.log(res.data.obj.nav);
      console.log("后端回传的user权限json：res.data.obj.authoritys");
      console.log(res.data.obj.authoritys);

      // 赋值store.menulist
      // console.log("赋值查看");

      // console.log(store.state.menus.menuList);
      // 拿用户权限
      console.log("后端回传的navjson设置在stroe：menuList");
      store.commit('SetMenuList', res.data.obj.nav);
      console.log("后端回传的user权限json设置在stroe：Permlist");
      store.commit('SetPermList', res.data.obj.authoritys);

      // 对menu的子路由进行路由映射
      // 遍历循环obj.nav
      res.data.obj.nav.forEach(menu => {

        // // menu?
        console.log("res.data.obj.nav->menu");
        console.log(menu);

        if (menu.children) {
          menu.children.forEach(e => {
            // 路由转换
            let route = menuToRoute(e);
            // 路由管理add
            if (route) {

              // // test
              // console.log("route");
              // console.log(route);

              // console.log("newRoutes");
              // console.log(newRoutes);

              console.log("newRoutes[0].children");
              console.log(newRoutes[0].children);

              newRoutes[0].children.push(route);
            }
          })
        }
      });

      // test
      console.log("newroutes");
      console.log(newRoutes);

      // 旧版本废弃方法 
      // router.addRoutes(newRoutes);

      // 新版本方法
      for(let i=0;i<newRoutes.length;i++){
        router.addRoute(newRoutes[i])
      }

      store.commit('changeRouteStatus', true);
      next();
    });
  };
  console.log("路由守卫退出");
  next();
});

// json转路由对象
const menuToRoute = (menu) => {
  if (!menu.component) {
    return null;
  }
  let route = {
    path: menu.path,
    name: menu.name,
    meta: {
      icon: menu.icon,
      title: menu.title,
    }
  }
  route.component = () => import("@/views/"+ menu.component +".vue");
  return route;
}
export default router;
