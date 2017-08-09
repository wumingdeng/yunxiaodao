<template>
	<f7-page navbar-through infinite-scroll :infinite-scroll-preloader="isPreloader" @infinite="onInfinite">
        <f7-navbar sliding>
          <f7-nav-left>
              <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
          </f7-nav-left>
          <f7-nav-center sliding title="收入明细"></f7-nav-center>
          <f7-nav-right></f7-nav-right>
        </f7-navbar>
	  <div class="incomeDetailTop">
      <span>
          <p>总收入</p>
            <p>{{0}}元</p>
      </span>  
        <div class="topShuxian"></div>
        <span>
          <p>已提现</p>
        <p>{{0}}元</p>
      </span>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
    </div>
	 <f7-card class='cus_card' v-for="(item,index) in incomeDetails" :key='index'>
        <f7-card-content>
            <f7-grid>
                <f7-col width=20>
                    <img :src="'static/assets/userCenter/income_'+item.type+'.jpg'">
                </f7-col>
                <f7-col width=50>
                    <p style='color:rgb(100,100,100)'>{{incomeDate(item.createtime)}}</p>
                    <span>{{incomeType(item.type)}}</span>
                </f7-col>
                <f7-col width=30>
                    <p style='color:rgb(244,84,44);text-align:right'>{{incomeTxt(item.type,item.money)}}</p>
                    <span style='float:right'>. . .</span>
                </f7-col>
            </f7-grid>
        </f7-card-content>
     </f7-card>

    <div style="position:absolute;top:14em;text-align:center;width:100%;">
        暂无收入明细
    </div>
	</f7-page>
</template>

<script>
	import g from '../../globals/global'
	export default {
		data() {
			return {
                page: 1,
                pageCount: 10,
                isPreloader: true,
                isNoData: true,
                income:10,
                cash:10,
				incomeDetails:[],
			}
		},
		computed:{
			
            
		},
		methods:{
            incomeTxt(type,income){
                if(type == 2){
                    return '-'+income+'元'
                }else{
                    return '+'+income+'元'
                }
            },
            incomeType(type){
                switch(type){
                    case 1:
                        return '一级推广';
                    case 2:
                        return '提现';
                    case 3:
                        return '代言人推广奖励';
                    case 4:
                        return '二级推广';
                    default:
                        return '';
                }
            },
            incomeDate(nS){
                // return new Date('Y-m-d',parseInt(date) * 1000)
                var date = new Date(parseInt(nS) * 1000)
                return date.getFullYear()+'年'+date.getMonth()+1+'月'+date.getDate()+'日'
            },
			onCheck() {

			},
			getData(done){
				this.$store.dispatch('getIncomeDetails', {
                    self: this,
                    info: {
                        wxid: this.$store.state.wxid,
                        offset: (this.page - 1) * this.pageCount,
                        limit: this.pageCount
                    },
                    callback: (self, res) => {
                        if (res.body.ok) {
                            self.incomeDetails = self.incomeDetails.concat(res.body.ok);
                            if (res.body.ok.length > 0) {
                                self.isNoData = false;
                            }
                            if (res.body.ok.length < self.pageCount) {
                                //停止加载
                                self.$f7.detachInfiniteScroll('.infinite-scroll')
                                self.isPreloader = false;
                                console.log('stoppredown...')
                            }
                            if (done) {
                                done();
                            }
                        }
                    }
                })
                this.page++;
			},
            onInfinite(event, done) {
                //获取数据
                this.getData(done);
            },
		},
		mounted() {
            this.$nextTick(this.$f7.resize)
			this.getData();
		},
        beforeDestroy() {
            console.log('destory...')
        }
	}
</script>

<style>
	.incomeDetailTop {
        background: url('/yzg/static/assets/userCenter/user_bg.jpg') no-repeat rgb(111,200,200);
        background-size:cover;
        width:100%;
        height:auto;
        /*border: 1px solid #d4d4d4;*/
        min-width: 288px;
	}
	.incomeDetailTop .topShuxian {
        width: 1px;
        height: 1.6em;
        /*background-color: rgba(0,115,154,1);*/
        border-right: 1px solid #28d7ff;
        display: inline-block;
        padding: 10px 0;
        position: absolute;
        margin: 2em 0em 0 0.6em;
    }
	
	.incomeDetailTop span {
        line-height: 25px;
        height: 80px;
        font-size: 16px;
        width:44%;
        color: #fff;
        display: inline-block;
        margin: 15px 0 0 10px;
        position: relative;
        top:-16px;
        left:10px;
        text-align:center;
	}
	.incomeDetailTop span p {
		margin: 10px 0px;
		white-space: nowrap;
		font-size:18px;
		line-height:24px;
		color:rgba(255,255,255,0.8);
        position: relative;
        top:12px;
	}
    .cus_card{
        border-radius:10px;
        color: #515151;
    }
    .cus_card img{
        width:100%;
    }
    .cus_card p{
        line-height:0px;
        font-size:18px;
        margin-top:13px;
        margin-bottom:15px;
    }
    .cus_card span{
        font-size:18px;
        color: #515151;
    }
    .cus_card .card-content-inner{
        padding:10px;
    }
</style>


