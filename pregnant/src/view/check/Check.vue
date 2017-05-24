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
      <f7-card-content>
        <h3 style="text-align:center;color:#fe4365">您已经怀孕 {{weightInfo.currentWeek}} 周</h3>
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
        <f7-list style="margin-bottom:10px;" form>
          <f7-list-item>
            <f7-label>手工输入:</f7-label>
            <div class="item-input custom">
              <input id="inputWeight" type="number" placeholder="">
            </div>
            <!-- <f7-input class="custom" id="weightInput" type="number" placeholder="" v-model="userWeight"></f7-input> -->
            <span style="width:100%;margin-left:10px;">kg</span>
          </f7-list-item>
   <!--        <f7-list-item>
            <f7-label>手工输入:</f7-label>
            <f7-input id="inputWeight" name="name" type="number" placeholder="kg"></f7-input>
          </f7-list-item> -->
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
      <f7-card-header><div><li class='ion-ios-medkit' style='color:#fe4365;float:left'/><span style='margin-left:10px;color:#fa7190'>饮食健康小贴士</span></div></f7-card-header>
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
        msg: 'Welcome to Check Page',
        haveData: true,
        weightInfo:{}
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
</style>
