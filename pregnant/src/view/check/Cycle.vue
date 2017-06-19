<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
      <f7-nav-left>
          <f7-link icon="icon-back color-black color-black" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="产检日历"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>
    <f7-table v-if="cycleData.length > 0"
      card
      title="体检周期"
      :items="cycleData"
      :headings="['开始周', '结束周', '体检内容', '注意', '检查日期']"
      :columns="['minWeek', 'maxWeek', 'content', 'attention', 'checkDate']"
    />
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

</style>