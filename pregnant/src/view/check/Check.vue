<template>
  <f7-page>
    <!-- <f7-navbar back-link="返回" title="产检记录" sliding></f7-navbar> -->
    <f7-card>
      <f7-card-content>
        <h3 style="text-align:center;">您已经怀孕 {{weightInfo.week}} 周了</h3>
        <p style="text-align:center;">建议体重范围：{{weightInfo.currentStandard}}</p>
        <p>
          <f7-grid>
            <f7-col><f7-button fill color="green" @click="$router.push('/userInfo')">个人资料</f7-button></f7-col>
            <f7-col><f7-button fill color="orange" @click="$router.push('/record')">历史记录</f7-button></f7-col>
          </f7-grid>
        </p>
      </f7-card-content>
    </f7-card>

    <f7-card>
      <f7-card-content>
        <f7-list style="margin-bottom:10px;" form>
          <f7-list-item>
            <f7-label>手工输入:</f7-label>
            <f7-input id="inputWeight" name="name" type="number" placeholder="kg"></f7-input>
          </f7-list-item>
        </f7-list>
        <f7-button fill color="green" @click='onFillWeight'>评估</f7-button>
      </f7-card-content>
    </f7-card>

    <f7-card v-if='haveData'>
      <f7-card-content>
        <p>最新称重结果: {{weightInfo.weight}}kg {{weightInfo.recordDate.substring(0,10)}} 于 {{weightInfo.hospital}}</p>
        <p>体重: {{weightInfo.result}}</p>
        <p>建议标准体重范围: {{adviseWeight}}</p>
        <p>注意点: {{weightInfo.tip}}</p>
      </f7-card-content>
    </f7-card>

    <f7-card v-else>
      <f7-card-content>
        <p>没数据</p>
      </f7-card-content>
    </f7-card>
    
    <f7-card v-if='haveData'>
      <f7-card-header>饮食健康小贴士</f7-card-header>
      <f7-card-content>{{weightInfo.diet}}</f7-card-content>
    </f7-card>
  </f7-page>
</template>

<script>
  export default {
    name: 'check',
    data () {
      return {
        msg: 'Welcome to Check Page',
        haveData: true,
        weightInfo:{
          week:2,
          currentStandard:'50,60',
          standard:'50,60',
          weight:55,
          hospital:'妇幼医院',
          recordDate:'1999-12-12',
          result:'正常',
          tip:'注意保暖',
          diet:'少吃饭'
        }
      }
    },

    computed:{
      adviseWeight() {
        var weight = this.weightInfo.standard
        if (weight) {
          var arr = weight.split(',');
          return '( ' + arr[0] + 'kg - ' + arr[1] + 'kg )'
        } else {
          return ''
        }
      }
    },

    methods: {
      onFillWeight() {
        var weight = document.getElementById("inputWeight").value;
        if (weight != '') {
          this.$f7.confirm('','您输入的体重为:' + weight + 'kg',()=>{
            this.$store.dispatch('fillWeight',{
              self:this,
              info:{
                wxid: this.$store.state.wxid,
                weight:weight
              },
              callback:function(self,res) {
                if (res.body.ok) {
                  self.weightInfo = {...self.weightInfo,...res.body.ok} //覆盖原数据
                  self.haveData = true;
                } else {

                }
              }
            })
          }, ()=>{
            document.getElementById("inputWeight").value = ''
          })
        } else {
          this.$f7.alert('','请输入体重 单位(kg)')
        }
      }
    },
    mounted () {
      //取体重数据
      console.log(this.$store.state.wxid)
      this.$store.dispatch('getWeightInfo',{
        self:this,
        info:{
          wxid: this.$store.state.wxid
        },
        callback:function(self,res) {
          if (res.body.ok == 0) {
            self.haveData = false;
            console.log('没有体重信息')
          } else {
            self.weightInfo = res.body.ok
          }
        }
      })
    },
    beforeRouteEnter(to,from,next) {
      console.log('before...')
      console.log(window.Global.s.state)
      var userInfo = window.Global.s.state.userinfo
      if (!userInfo.height || !userInfo.weight || !userInfo.lastPeriod) {
        next({
          path:'/userInfo',
          query:{
            isNecessary:true
          }
        })
      }
      next();
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
