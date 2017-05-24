<template>
  <f7-views style='z-index:1000;' id="app">
    <div v-if="showLoading" class="loading" 
    >
      <span style="height:100%;vertical-align:middle;display:inline-block;"></span>
      <img style="vertical-align:middle;" src="static/assets/loading.gif">
    </div>
    <router-view></router-view>
  </f7-views>
</template>

<script>
    // import navFooter from './views/navFooter'
    export default {
        data () {
          return {

          }
        },
        mounted () {
            console.log('enter app')
        },
        created () {
          console.log(this)
        },
        computed:{
          showLoading() {
            return this.$store.state.isloading
          }
        }

    }
</script>

<style>
  .loading {
    text-align:center;
    width:100%;
    height:100%;
    z-index:999;
    position:absolute;
    top:0;
    left:0
  }
</style>
