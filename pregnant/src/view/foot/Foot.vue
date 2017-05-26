<template>
	<f7-page name="footPage">
		<f7-card>
			<f7-card-header><div style='margin-left:10px;font-size:17px'><li class='ion-pricetag' style='color:#fa7190;float:left'/><span style='margin-left:10px;color:#000000;font-weight:bold'>足部健康评测报告</span></div></f7-card-header>
<!-- 			<f7-card-header>
				<img class="iconTitle" src='static/assets/icon/icon_title.png'></img>
				<span>足部健康评测报告</span>
			</f7-card-header> -->
			<f7-card-content>
				<f7-grid>
					<f7-col>
						<div class="border">
							<div class="bTitle">
	    						<img src='static/assets/icon/icon_foot.png'></img>
								  足型扫描
							</div>
						</div>
					</f7-col>
					<f7-col>
						<div class="border">
							<div class="bTitle">
	    						<img src='static/assets/icon/icon_result.png'></img>
								  测量结果
							</div>
							<div>
								<table class="footTable" border="0">
									<tr>
										<td>左脚</td>
										<td></td>
										<td>右脚</td>
									</tr>
									<tr v-for="(item,index) in dataName">
										<td>{{footData['l' + valueName[index]]}}</td>
										<td>{{item}}</td>
										<td>{{footData['r' + valueName[index]]}}</td>
									</tr>
								</table>
							</div>
						</div>
					</f7-col>
				</f7-grid>
			</f7-card-content>
		</f7-card>

		<f7-card>			
			<f7-card-header><div style='margin-left:10px;font-size:17px'><li class='ion-pricetag' style='color:#fa7190;float:left'/><span style='margin-left:10px;color:#000000;font-weight:bold'>足型判断</span></div></f7-card-header>
<!-- 			<f7-card-header>
				<img class="iconTitle" src='static/assets/icon/icon_title.png'></img>
				<span>足型判断</span>
			</f7-card-header> -->
			<f7-card-content style="height:90px;">
				{{footData.footJudgment}}
			</f7-card-content>
		</f7-card>

		<f7-card>
			<f7-card-header><div style='margin-left:10px;font-size:17px'><li class='ion-pricetag' style='color:#fa7190;float:left'/><span style='margin-left:10px;color:#000000;font-weight:bold'>建议鞋型</span></div></f7-card-header>
<!-- 			<f7-card-header>
				<img class="iconTitle" src='static/assets/icon/icon_title.png'></img>
				<span>建议鞋型</span>
			</f7-card-header> -->
			<f7-card-content>
				<f7-list form style="height:120px;">
					<li style="width: 33%;float:left;" v-for="(item,n) in shoeType" :key="n">
						<label class="item-content label-checkbox" style="padding-right:0px;">
							<input :value="n" :checked="footData.suggestShoe == n + 1" name="my-radio" disabled type="checkbox">
							<div class="item-media">
								<i class="icon icon-form-checkbox"></i>
							</div>
							<span style="width:90%;margin-left:5px;">
								<div class="item-title" style="font-size:14px;">{{item}}</div>
							</span>
						</label>
					</li>
				</f7-list>
<!-- 		    <f7-list form style="margin-bottom:10px;">
	      	<f7-list-item style="width:50%" disabled v-for="(item,n) in shoeType" :key="n" checkbox name="my-radio" :checked="footData.suggestShoe == n + 1" :value="n" :title="item"></f7-list-item>
		    </f7-list> -->
				<p style="margin:5px auto;width:40%;"><f7-button fill style="background-color:#fa7699;height:35px;line-height:35px;" @click="$router.push('/shoeDetail')">去看看</f7-button></p>
			</f7-card-content>
		</f7-card>

		<f7-card>
			<f7-card-header><div style='margin-left:10px;font-size:17px'><li class='ion-pricetag' style='color:#fa7190;float:left'/><span style='margin-left:10px;color:#000000;font-weight:bold'>足部健康知识</span></div></f7-card-header>
<!-- 			<f7-card-header>
				<img class="iconTitle" src='static/assets/icon/icon_title.png'></img>
				<span>足部健康知识</span>
			</f7-card-header> -->
			<f7-card-content style="height:90px;">
				{{footData.footKnowledge}}
			</f7-card-content>
		</f7-card>
	</f7-page>
</template>


<script>
	export default {
		data() {
			return {
				footData:{
					suggestShoe:5
				},
				dataName:['足长','足宽','鞋码','型宽','足型'],
				valueName:['footLength','footWidth','size','typeWidth','footType'],
				shoeType:['基础功能型','加强缓震型', '控制型', '超级稳定型', '保胎孕妇鞋']
			}
		},
    beforeCreate() {
      document.title = '足部报告'
    },
		mounted() {
			//取数据
			var wxid = this.$route.query.wxid;
			this.$store.dispatch('getFootRecord',{
				self:this,
				info:{
					wxid:wxid
				},
				callback(self,res) {
					if (res.body.ok == 0) {
						self.$f7.alert('您还没有足部健康记录')
					} else {
						self.footData = res.body.ok
					}
				}
			})
		}
	}
</script>


<style scoped>
	#footPage > .card-header {
		float:left;
	}
	.border {
		height:180px;
		/*box-shadow: 0 1px 2px rgba(0,0,0,.3);*/
		background-color: #f6f6f6;
	}
	.iconTitle {
		height:18px;
		margin:0px 5px 0 5px;
		float:left;
	}
	.iconTitle + span {
		position: absolute;
		left: 50px;
	}
	.bTitle {
		height:30px;
		background-color: #ffa1b3;
		line-height: 30px;
		color:#ffffff;
	}
	.bTitle img {
		height:70%;
		margin:3px 5px 0 5px;
		float:left;
		vertical-align:middle;
	}
	.footTable {
		width:100%;
		text-align: center;
	}
</style>