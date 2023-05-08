import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';


import request from '@/axios/axios.js'
Vue.prototype.$axios=request

// import axios from '@/axios/axios.js';
// Vue.prototype.$axios=axios
Vue.config.productionTip = false
Vue.use(ElementUI)



// require('@/mock/mock.js')

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
