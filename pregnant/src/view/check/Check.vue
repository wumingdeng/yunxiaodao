<template>
  <f7-page>
    <f7-navbar sliding style='border-radius:1px;height:60px;background-color:#1f2d3d;color:#ffffff'>
        <f7-nav-left v-if="!isNecessary">
            <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
        </f7-nav-left>
        <f7-nav-center sliding title="体重管理"></f7-nav-center>
        <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <f7-page style='margin-top:60px'>
    <f7-card>
      <f7-card-content style='text-align: center;'>
        <li style='font-size:37px;margin:0px 0px -52px 65px;color:#fa7699;font-weight:bold' class='ion-load-c'></li>
        <h3 style="text-align:center;color:#fe4365">您已经怀孕　{{weightInfo.currentWeek}}　周</h3>
        <p style="text-align:center;font-size:16px">建议体重范围：{{weightInfo.currentStandard}}</p>
        <p>
          <f7-grid>
            <f7-col><f7-button class='cusBtn' big fill  @click="$router.push('/userInfo')"><li style='font-size:25px;float:left' class='icon ion-home'><div style='font-size:17px;margin-left:30px;float:right'>个人资料</div></li></f7-button></f7-col>
            <f7-col><f7-button class='cusBtn' big fill  @click="$router.push('/record')"><li style='font-size:25px;float:left' class='ion-clock'><div style='font-size:17px;margin-left:30px;float:right'>历史记录</div></li></f7-button></f7-col>
          </f7-grid>
        </p>
      </f7-card-content>
    </f7-card>
    <f7-card>
      <f7-card-content>
        <f7-list form>
          <f7-list-item>
            <f7-label style='width:22%'>当前体重:</f7-label>
            <div style='width:20%'>
              <input id="inputWeight" style='text-align:center;background-color:#f0f0f0;height:30px;border-radius:1px' type="number" placeholder="">
            </div>
            <span style="width:10%">kg</span>
            <f7-button style='width:40%' class='cusBtn' fill  @click='onFillWeight'>评估</f7-button>
          </f7-list-item>
        </f7-list>
      </f7-card-content>
    </f7-card>

    <f7-card v-if='haveData'>
      <f7-card-content clase='p-title'>
        <p><span style='font-weight:bold'>最新称重结果</span> : {{weightInfo.weight}}kg ( {{weightInfo.hospital}} )</p>
        <p><span style='font-weight:bold'>体重情况目前</span> : {{weightInfo.result}}</p>
        <p><span style='font-weight:bold'>建议体重范围</span> : {{adviseWeight}}</p>
        <f7-grid no-gutter>
          <f7-col width="26"><span style='font-weight:bold'>孕妇注意事项</span> :</f7-col>
          <f7-col width="70">{{weightInfo.tip}}</f7-col>
        </f7-grid>
      </f7-card-content>
    </f7-card>

    <f7-card v-else>
      <f7-card-content>
        <p>没数据</p>
      </f7-card-content>
    </f7-card>
    
    <f7-card v-if='haveData'>
      <f7-card-header><div style='margin-left:110px'><li class='ion-ios-medkit' style='color:#fe4365;float:left;font-size:30px;margin-top:-13px'/><span style='margin-left:10px;color:#fa7190'>饮食健康小贴士</span></div></f7-card-header>
      <f7-card-content>{{weightInfo.diet}}</f7-card-content>
    </f7-card>
      </f7-page>
  </f7-page>
</template>

<script>
  export default {
    name: 'check',
    data () {
      return {
        columnStyle: 'border: 1px solid #e5e5e5; padding:5px; text-align: center',
        msg: 'Welcome to Check Page',
        haveData: true,
        weightInfo:{tip:'阿克苏多方了解啊路上看到解放了空间啊死了快点放假苦辣就是独立空间发生地方'},
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
        var isSend = false
        if (weight != '') {
          this.$f7.confirm('','您输入的体重为:' + weight + 'kg',()=>{
            if (isSend) {
              document.getElementById("inputWeight").value = ''
              return
            }
            isSend = true;
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
                  document.getElementById("inputWeight").value = ''
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
          self.weightInfo = res.body.ok
          if (!res.body.ok.id) {
            self.haveData = false;
            console.log('没有体重信息')
          } else {
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
  .custom {
    width: 30%;
  }
  .custom input {
    text-align: right;
  }
  .page div{
    border-radius:10px;
  }
 
  .cusBtn{
    border-radius: 10px;
    background-color:#fa7699;
    height: 35px;
    line-height: 32px;
  }
  .cusareatext textarea{
    font-size:17px;
  }
</style>
