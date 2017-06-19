<template>
	<f7-page navbar-through class="cyclePage">
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="产检日历"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
		
		<div v-for="(item,index) in cycleData">
			<section style="border: none; margin: 2em auto 1em auto; text-align: left;">
				<span class="color" style="border-radius: 50%; overflow: hidden; vertical-align: middle; background-color: rgb(251, 113, 146); display: inline-block;">
					<section class="color" style="width: 30px; height: 30px; line-height: 30px; overflow: hidden; background-color: rgb(251, 113, 146); margin: 8px; border-radius: 50%; color: rgb(255, 255, 255); font-size: 22px; text-align: center; border: 2px solid rgb(255, 255, 255) !important;">
						<p class="wihudong" style="margin: 0px; color: rgb(255, 255, 255);">{{index + 1}}</p>
					</section>
				</span>
				<section class="color" style="font-size: 18px; display: inline-block; color: rgb(255, 255, 255); vertical-align: middle; min-width: 110px; height: 32px; text-align: center; padding-left: 0.5em; padding-right: 0.5em; line-height: 32px; margin-left: -0.5em; background-color: rgb(251, 113, 146); border-top-right-radius: 0.8em; border-bottom-right-radius: 0.8em;">
					<p class="wihudong" style="margin: 0px; color: rgb(255, 255, 255);">{{item.checkDate}}</p>
				</section>
			</section>
			<section id="shifu_c_009" donone="shifuMouseDownCard(&#39;shifu_c_009&#39;)" style="margin: 2em 0em; padding: 0.5em 1em; white-space: normal; border: 1px solid rgb(251, 113, 146); display: block; font-size: 1em; font-family: inherit; font-style: normal; font-weight: inherit; text-decoration: inherit; color: rgb(166, 166, 166); background-color: rgb(255, 255, 255);">
				<section style="margin-top: -1.4em;text-align: center;text-align: center; padding: 0; border: none; line-height: 1.4;">
					<section style="padding: 0 24px;color: rgb(70, 70, 70);font-size: 20px; white-space: normal; text-align: center; font-family:inherit;font-style: normal; font-weight: inherit; text-decoration: inherit;background-color: #feffff;border-color: #ffffff;display: inline-block;">
						<section>
							<span style="font-size: 18px;"><strong>产检项目</strong></span>
						</section>
	        </section>
	      </section><section style="padding: 0px 16px; color: rgb(32, 32, 32); font-size: 1em;
	        line-height: 1.4; font-family: inherit; font-style: normal;"><p>{{item.content}}</p></section>
	    </section>
	    <p class="shifubrush"><span style="color: rgb(192, 0, 0);">注意事项：</span>{{item.attention}}</p>
	    <section style="background-color: none;border:none;border-style: none;margin: 5px auto 0;background: none;">
	    	<section class="color" style="height: 15px; border-bottom: 1px solid rgb(204, 204, 204);">
	    	<br/>
	    	</section>
	    </section>
		</div>
    
<!--     <f7-table v-if="cycleData.length > 0"
      card
      title="体检周期"
      :items="cycleData"
      :headings="['开始周', '结束周', '体检内容', '注意', '检查日期']"
      :columns="['minWeek', 'maxWeek', 'content', 'attention', 'checkDate']"
    /> -->
	</f7-page>
</template>

<script type="text/javascript">
	export default {
		data() {
			return {
				cycleData:[]
			}
		},
		mounted() {
			this.$f7.resize()
			//取体检日期数据
			this.$store.dispatch('getCheckCycle',{
				self:this,
				info:{

				},
				callback(self,res) {
					if (res.body.data) {
						var lastPeriod = self.$store.state.userinfo.lastPeriod
						for (var i in res.body.data) {
							var value = res.body.data[i]
							//计算周数
							if (lastPeriod) {
								lastPeriod = new Date(lastPeriod)
								var checkDate = new Date((lastPeriod/1000 + 86400 * (value.minWeek - 1) * 7)*1000)
								value.checkDate = checkDate.toLocaleDateString();
							}
							self.cycleData.push(value)
						}
					}
				}
			})
		}
	}
</script>

<style>
	.cyclePage {
		background-color: #fff;
    padding: 0 10px;
    box-sizing: border-box;
	}
</style>