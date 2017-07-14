<template>
  <f7-page v-show="showPage">

        <span style="width:18%;position:absolute;top:7px;right:20px;z-index:100" @click="$router.push('/cycle')">
          <img style="width:100%" src="static/assets/checkDate.png">
        </span>
    <f7-card>
      <f7-card-content style='text-align: center;'>
        <div>
          <h3 style="text-align:center;color:#fe4365;display:inline">恭喜您已怀孕&nbsp;&nbsp;&nbsp;</h3>
          <div style='display:inline;position:relative;'>
            <div :style='weightInfo.currentWeek>9?ydSty:ydSty_1'>!</div>
            <h3 style="text-align:center; color:#fe4365;display:inline">{{weightInfo.currentWeek}}</h3>
          </div>
          <h3 style="text-align:center;color:#fe4365;display:inline">&nbsp;&nbsp;&nbsp;周</h3>
        </div>
  
        <p style="text-align:center;font-size:16px">本孕周建议体重：{{weightInfo.currentStandard}}</p>
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
            <f7-label style='width:7em'>输入当前体重:</f7-label>
            <div style='width:25%'>
              <input id="inputWeight" style='text-align:center;background-color:#f0f0f0;height:30px;border-radius:1px' type="number" placeholder="">
            </div>
            <span style="width:10%">&nbsp;kg</span>
            <f7-button style='width:30%;font-size:17px' class='cusBtn' fill @click='onFillWeight'>评估</f7-button>
          </f7-list-item>
        </f7-list>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData'>
      <f7-card-content clase='p-title'>
        <div style='position:relative;top:-1em'>
          <p style="text-align: center;position:absolute;left:0;top:-0.65em;right:0;font-size:18px;">{{storeTitle}}</p>
          <p style="text-align: center">
            <img src="static/assets/weight_title.jpg" style='width:100%' />
          </p>
        </div>
        <custitle :name='"体重数据"'></custitle>
        <p style='font-size:16px'> &nbsp; &nbsp; &nbsp;{{weightInfo.weight}}kg 于 {{recordDate}}</p>
        <custitle :name='"评估结果"'></custitle>
        <div style='font-size:16px;margin-left: 23px;' id='w_sug'></div>
        <custitle :name='"饮食贴士"'></custitle>
        <div  id='w_diet'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-else>
      <f7-card-content>
        <p>暂无数据</p>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData&&overStandard'>
      <f7-card-content>
        <div id='g_sign'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData&&havaDiet&&overStandard'>
      <f7-card-content>
        <div id='g_diet'></div>
      </f7-card-content>
    </f7-card>
    <f7-card v-if='haveData&&overStandard'>
      <f7-card-content>
        <div id='g_sport'></div>
      </f7-card-content>
    </f7-card>
  </f7-page>
</template>
<script>
  import custitle from '../../components/title'
  import moment from 'moment'
export default {
  name: 'check',
  data() {
    return {
      columnStyle: 'border: 1px solid #e5e5e5; padding:5px; text-align: center',
      msg: 'Welcome to Check Page',
      haveData: true,
      showPage:false,
      havaDiet: true,
      weightInfo: {},
      testTip: '',
      ydSty:'font-family:hcpfont;color:#fa7699;font-size:35px;display:inline;position:absolute;top:-11px;left:-8px',
      ydSty_1:'font-family:hcpfont;color:#fa7699;font-size:35px;display:inline;position:absolute;top:-11px;left:-13px',
    }
  },
  components: {
    'custitle': custitle
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
      if (this.weightInfo.recordDate) {
        var date = moment(this.weightInfo.recordDate).format("YYYY-MM-DD")
        return date
      } else {
        return ''
      }
    },
    overStandard(){
      return this.weightInfo.currentWeek<=40
    },
    storeTitle(){
      var userInfo = window.Global.s.state.userinfo
      var difdata = new Date(this.weightInfo.recordDate).getTime() - new Date(userInfo.lastPeriod).getTime()
     
      var week = Math.floor(difdata/(7 * 24 * 3600 * 1000))+1

      console.log(week)

      if(week == this.weightInfo.currentWeek){
        return "体重评估报告"
      }else{
        return "历史体重评估报告"
      }
    }
  },

  methods: {
    onFillWeight() {
      var weight = document.getElementById("inputWeight").value;
      var isSend = false
      if (weight != '') {
        if (weight == 0) {
          this.$f7.alert("","请输入大于0的数字哦!")
          document.getElementById("inputWeight").value = ''
          return
        }
        this.$f7.confirm('', '您输入的体重为:' + weight + 'kg', () => {
          if (isSend) {
            document.getElementById("inputWeight").value = ''
            return
          }
          isSend = true;
          this.$f7.showPreloader()
          this.$store.dispatch('fillWeight', {
            self: this,
            info: {
              // wxid: this.$store.state.wxid,
              weight: weight
            },
            callback: function (self, res) {
              if (res.body.ok) {
                self.weightInfo = { ...self.weightInfo, ...res.body.ok } //覆盖原数据
                self.haveData = true;
                self.$nextTick(function () {
                  
                  document.getElementById("inputWeight").value = ''
                  
                  var str = self.weightInfo.tip.con_sug.replace(/{{weightInfo.currentStandard}}/g, self.weightInfo.currentStandard)  
                  document.getElementById("w_sug").innerHTML = str || ''
                  //document.getElementById("w_sug").innerHTML = self.weightInfo.tip.con_sug || ''
                  document.getElementById("w_diet").innerHTML = self.weightInfo.tip.con_diet || ''

                  if(self.weightInfo.currentWeek>40) return 
                  document.getElementById("g_sign").innerHTML = self.weightInfo.diet.key || ''
                 
                  document.getElementById("g_diet").innerHTML = self.weightInfo.diet.eat || ''
                  if (!self.weightInfo.diet.eat || self.weightInfo.diet.eat == '') {
                    self.havaDiet = false
                  }
                  document.getElementById("g_sport").innerHTML = self.weightInfo.diet.sport || ''
                  
                })
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
    document.title = '孕期体重管理'
    this.$store.commit("LOADING",true)
  },
  mounted() {
    //取体重数据
    this.$store.dispatch('getWeightInfo', {
      self: this,
      info: {
        // wxid: this.$store.state.wxid
      },
      callback: function (self, res) {
        self.showPage = true;
        self.$store.state.isloading = false;
        self.weightInfo = res.body.ok
        if (!res.body.ok.id) {
          self.haveData = false;
          console.log('没有体重信息')
        } else {
          self.$nextTick(function () {
            var str = self.weightInfo.currentStandard.replace(/-/,'到')
            str = self.weightInfo.tip.con_sug.replace(/{{weightInfo.currentStandard}}/g, str)  
            document.getElementById("w_sug").innerHTML = str || ''
            document.getElementById("w_diet").innerHTML = self.weightInfo.tip.con_diet || ''

            if(self.weightInfo.currentWeek>40) return 
            document.getElementById("g_sign").innerHTML = self.weightInfo.diet.key || ''
           
            document.getElementById("g_diet").innerHTML = self.weightInfo.diet.eat || ''
            if (!self.weightInfo.diet.eat || self.weightInfo.diet.eat == '') {
              self.havaDiet = false
            }
            document.getElementById("g_sport").innerHTML = self.weightInfo.diet.sport || ''
          })
        }
      }
    })

  },
  beforeRouteEnter(to, from, next) {
    // console.log('before...')
    // console.log(window.Global.s.state)
    var userInfo = window.Global.s.state.userinfo
    if (!userInfo.height || !userInfo.weight || !userInfo.lastPeriod) {
      next({
        path: '/userInfo',
        query: {
          isNecessary: true
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

.page div {
  border-radius: 10px;
}

.cusBtn {
  border-radius: 5px;
  background-color: #fa7699;
  height: 35px;
  line-height: 32px;
}

.cusareatext textarea {
  font-size: 17px;
}
</style>
