<template>
  <f7-views style='z-index:1000;' id="app">
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
        }

    }
</script>

<style>

</style>
