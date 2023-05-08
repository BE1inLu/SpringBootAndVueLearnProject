<template>
  <div class="login_main">
    <loginviewVue></loginviewVue>
  </div>
</template>

<script>
import loginviewVue from "./loginview.vue";
export default {
  name: "login_imdex_vue",
  components: {
    loginviewVue,
  },
};
</script>

<style>
.login_main{
  background-color: #fff;
}
</style>