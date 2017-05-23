<template>
    <f7-page name="recordPage" navbar-through id="withdrawPage" infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
    <f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="历史记录"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    
    <div v-if="isNoData"
      style="text-align:center;height:100%;" 
    >
      <span style="height:100%;vertical-align:middle;display:inline-block;"></span>
      <img style="vertical-align:middle;" src="static/assets/no_records.png">
    </div>

    <f7-card v-if="!isNoData">
      <f7-card-content>
        <div id="charts">
            <div id="myChart"  :style="{width:'390px',height:'400px'}"></div>
        </div>
      </f7-card-content>
    </f7-card>
    <f7-table v-if="!isNoData" card>
      <f7-table-row heading>
        <f7-table-cell label>日期</f7-table-cell>
        <f7-table-cell label>孕周</f7-table-cell>
        <f7-table-cell numeric>体重 (kg)</f7-table-cell>
        <f7-table-cell label>状态</f7-table-cell>
      </f7-table-row>
      <f7-table-row v-for="(item,index) in weightInfo" :key="index">
        <f7-table-cell label>{{getRecordDate(item.recordDate)}}</f7-table-cell>
        <f7-table-cell label>{{item.week}}</f7-table-cell>
        <f7-table-cell numeric>{{item.weight}}</f7-table-cell>
        <f7-table-cell label>{{item.result}}</f7-table-cell>
      </f7-table-row>
    </f7-table>
  </f7-page>
</template>

<script>
  // 引入基本模板
  let echarts = require('echarts/lib/echarts')
  // 引入饼图组件
  require('echarts/lib/chart/line')
  require('echarts/lib/component/dataZoom');
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/markLine');
  export default {
    data () {
      return {
        isNoData:true,
        page:1,
        pageCount:10,
        isPreloader:true,
        weightInfo:[
          // {
          //   recordDate:'2017-5-1',
          //   week:'0',
          //   weight:30,
          //   result:'正常'
          // },
          // {
          //   recordDate:'2017-5-1',
          //   week:'1',
          //   weight:41,
          //   result:'正常'
          // },
          // {
          //   recordDate:'2017-5-1',
          //   week:'2',
          //   weight:45,
          //   result:'正常'
          // },
          // {
          //   recordDate:'2017-5-1',
          //   week:'3',
          //   weight:50,
          //   result:'正常'
          // },
          // {
          //   recordDate:'2017-5-1',
          //   week:'4',
          //   weight:100,
          //   result:'正常'
          // }
        ]
      }
    },
    methods:{
      //显示图表
      showCharts() {
        var self = this;
        var options = {
          title: {
              text: '体重'
          },
          tooltip: {
              trigger: 'axis',
              formatter: function (params) {
                  return '孕期:第' + params[0].data[0] + '周' + '<br>体重: ' + params[0].data[1] + 'kg';
              }
          },
          xAxis: {
              name: '孕期 (周)',
              nameLocation: 'middle',
              nameGap: 20,
              // interval:1,
              boundaryGap: false,
              type: 'value',
              min: 0,
              max: 40
              // interval:1,
              // data: self.chartData.map(function (item) {
              //     return item.week;
              // })
          },
          yAxis: {
              splitLine: {
                  show: true
              },
              name:'体重 (kg)',
              min: 30
          },
          // toolbox: {
          //     left: 'center',
          //     feature: {
          //         dataZoom: {
          //             yAxisIndex: 'none'
          //         },
          //         restore: {},
          //         saveAsImage: {}
          //     }
          // },
          // dataZoom: [{
          //     startValue: 0
          // }, {
          //     type: 'inside'
          // }],
          series: {
              name: '体重',
              type: 'line',
              data: self.chartData.map(function (item) {
                  return [item.week, item.weight];
              }),
              smooth:true,
              areaStyle: {normal: {
                color:'#dd5555'
              }},
              // markLine: {
              //     silent: true,
              //     data: [{
              //         yAxis: 30
              //     }, {
              //         yAxis: 40
              //     }, {
              //         yAxis: 50
              //     }, {
              //         yAxis: 60
              //     }, {
              //         yAxis: 70
              //     }]
              // }
          }
        }

        let chartBox = document.getElementById('charts')
        let myChart = document.getElementById('myChart')

        // 用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
        function resizeCharts () {
          console.log("en?")
          console.log(chartBox.offsetWidth)
          myChart.style.width = chartBox.offsetWidth + 'px'
          myChart.style.height = chartBox.style.height + 'px'
        }
        // 设置容器高宽
        resizeCharts()
              
        let mainChart = echarts.init(myChart)
        mainChart.setOption(options)
      },
      onInfinite(event, done){
        //获取数据
        console.log('pulldown...')
        this.getWeightData(done);
      },

      getRecordDate(date){
        console.log(date)
        return date.substring(0,10)
      },
      getChartData(data) {
        //取折线图数据
        this.$store.dispatch('getWeightChart',{
          self:this,
          info:{
            wxid:this.$store.state.wxid
          },
          callback(self,res) {
            if (res.body.ok != 0) {
              self.chartData = res.body.ok
              self.showCharts();
            }
          }
        })
      },
      getWeightData(callback) {
        this.$store.dispatch('getWeightData',{
          self:this,
          info:{
              wxid:this.$store.state.wxid,
              offset:(this.page - 1) * this.pageCount,
              limit:this.pageCount
          },
          callback:(self,res)=>{
              self.weightInfo = self.weightInfo.concat(res.body.ok)
              if (res.body.ok.length > 0) {
                  self.isNoData = false;
                  this.getChartData();
              }
              if (res.body.ok.length < self.pageCount) {
                  //停止加载
                  self.$f7.detachInfiniteScroll('.infinite-scroll')
                  self.isPreloader = false;
                  console.log('stoppredown...')
              }
              if (callback) {
                  callback();
              }
          }
      })
      this.page++;
      }
    },

    mounted () {
      // this.$f7.resize()
      this.$f7.initPageInfiniteScroll('#app');

      //取每条记录
      this.getWeightData();
    }
  }
</script>
