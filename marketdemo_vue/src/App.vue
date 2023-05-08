<template>
  <router-view></router-view>
</template>

<script>
export default{
  name:"App",
  watch:{
    $route(to,from){
      if(to.path!='/login'){
        let obj={
          name:to.name,
          title:to.meta.title,
        }
        this.$store.commit("addTab",obj);
      }
    }
  }
}

</script>


<style>

/* 全局样式定义 */
*{
  padding: 0;
  margin: 0;
}
body{
  height: 100vh;
  font-size: 15px;
}

</style>
