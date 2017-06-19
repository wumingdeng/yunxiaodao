<template>
  <f7-page>
    <f7-card>
      <f7-card-content style='text-align: center;'>
        <img src="static/assets/checkDate.png" 
          style="width:18%;position:absolute;top:-1px;right:10px;"
          @click.prevent.stop="$router.push('/cycle')">
        <div style='font-family:hcpfont;color:#fa7699;font-size:35px;margin:0px 0px -59px 42px'>!</div>
        <h3 style="text-align:center;color:#fe4365">您已经怀孕　{{weightInfo.currentWeek}}　周</h3>
        <p style="text-align:center;font-size:16px">建议体重范围：{{weightInfo.currentStandard}}</p>
        <p>
          <f7-grid>
            <f7-col>
              <f7-button class='cusBtn' big fill @click="$router.push('/userInfo')">
                <div style='font-family:hcpfont;font-size:20px;float:left;margin:5px 5px'>%</div>
                <span style='margin-right:10px'>个人资料</span>
              </f7-button>
            </f7-col>
            <f7-col>
              <f7-button class='cusBtn' big fill @click="$router.push('/record')">
                <div style='font-family:hcpfont;font-size:20px;float:left;margin:5px 5px'>@</div>
                <span style='margin-right:5px'>历史记录</span>
              </f7-button>
            </f7-col>
          </f7-grid>
        </p>
      </f7-card-content>
    </f7-card>
    <f7-card>
      <f7-card-content>
        <f7-list form>
          <f7-list-item>
            <f7-label style='width:27%'>当前体重:</f7-label>
            <div style='width:25%'>
              <input id="inputWeight" style='text-align:center;background-color:#f0f0f0;height:30px;border-radius:1px' type="number" placeholder="">
            </div>
            <span style="width:10%">kg</span>
            <f7-button style='width:30%;font-size:17px' class='cusBtn' fill @click='onFillWeight'>评估</f7-button>
          </f7-list-item>
        </f7-list>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-content clase='p-title'>
        <p>
          <span style='font-weight:bold'>最新体重结果</span> : {{weightInfo.weight}}kg {{recordDate}}</p>
        <p>
          <span style='font-weight:bold'>目前体重情况</span> : {{weightInfo.result}}</p>
        <p>
          <span style='font-weight:bold'>建议体重范围</span> : {{weightInfo.standard}}</p>
        <p>
          <div v-if='haveData' id='w_sug'></div>
          <div v-if='haveData' id='w_diet'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-else>
      <f7-card-content>
        <p>暂无数据</p>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-header>
        <p style='color:#fa7190;width:100%;line-height:30px;text-align:center;'>
          <span style='font-family:hcpfont;'>(</span>
          &nbsp;&nbsp;产检贴士</p>
      </f7-card-header>
      <f7-card-content></f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-content>
        <div id='g_sign'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-content>
        <div id='g_diet'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-content>
        <div id='g_sport'></div>
      </f7-card-content>
    </f7-card>
  </f7-page>
</template>
<script>
export default {
  name: 'check',
  data() {
    return {
      columnStyle: 'border: 1px solid #e5e5e5; padding:5px; text-align: center',
      msg: 'Welcome to Check Page',
      haveData: true,
      weightInfo: {},
      testTip: ''
    }
  },

  computed: {

    adviseWeight() {
      var weight = this.weightInfo.standard
      if (weight) {
        var arr = weight.split(',');
        return '( ' + arr[0] + 'kg - ' + arr[1] + 'kg )'
      } else {
        return ''
      }
    },
    recordDate() {
      var date = this.weightInfo.recordDate
      if (date) {
        return date.substring(0, 10)
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
        this.$f7.confirm('', '您输入的体重为:' + weight + 'kg', () => {
          if (isSend) {
            document.getElementById("inputWeight").value = ''
            return
          }
          isSend = true;
          this.$store.dispatch('fillWeight', {
            self: this,
            info: {
              wxid: this.$store.state.wxid,
              weight: weight
            },
            callback: function (self, res) {
              if (res.body.ok) {
                self.weightInfo = { ...self.weightInfo, ...res.body.ok } //覆盖原数据
                self.haveData = true;
                document.getElementById("inputWeight").value = ''
              } else {

              }
            }
          })
        }, () => {
          document.getElementById("inputWeight").value = ''
        })
      } else {
        this.$f7.alert('', '请输入体重 单位(kg)')
      }
    }
  },
  beforeCreate() {
    document.title = '体重管理'
  },
  mounted() {
    //取体重数据

    console.log(this.$store.state.wxid)
    this.$store.dispatch('getWeightInfo', {
      self: this,
      info: {
        wxid: this.$store.state.wxid
      },
      callback: function (self, res) {
        self.weightInfo = res.body.ok
        if (!res.body.ok.id) {
          self.haveData = false;
          console.log('没有体重信息')
        } else {
            document.getElementById("w_sug").innerHTML = self.weightInfo.tip.con_sug
            document.getElementById("w_diet").innerHTML = self.weightInfo.tip.con_diet
            
            document.getElementById("g_sign").innerHTML = self.weightInfo.diet.key
            document.getElementById("g_diet").innerHTML = self.weightInfo.diet.eat
            document.getElementById("g_sport").innerHTML = self.weightInfo.diet.sport
        }

      }
    })

  },
  beforeRouteEnter(to, from, next) {
    // console.log('before...')
    // console.log(window.Global.s.state)
    // var userInfo = window.Global.s.state.userinfo
    // if (!userInfo.height || !userInfo.weight || !userInfo.lastPeriod) {
    //   next({
    //     path:'/userInfo',
    //     query:{
    //       isNecessary:true
    //     }
    //   })
    // }
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

.page div {
  border-radius: 10px;
}

.cusBtn {
  border-radius: 10px;
  background-color: #fa7699;
  height: 35px;
  line-height: 32px;
}

.cusareatext textarea {
  font-size: 17px;
}
</style>
