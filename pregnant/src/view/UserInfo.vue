<template>
	<f7-page navbar-through>
    <f7-navbar sliding>
      <f7-nav-left v-if="!isNecessary">
          <f7-link icon="icon-back" @click="$router.go(-1)"></f7-link>
      </f7-nav-left>
      <f7-nav-center sliding title="个人资料"></f7-nav-center>
      <f7-nav-right></f7-nav-right>
    </f7-navbar>

    <f7-list id="userinfoForm" form>
      <f7-list-item>
        <f7-label>身高</f7-label>
        <f7-input id="heightInput" type="number" placeholder="cm" v-model="userHeight"></f7-input>
      </f7-list-item>
      <f7-list-item>
        <f7-label>孕前体重</f7-label>
        <f7-input id="weightInput" type="number" placeholder="kg" v-model="userWeight"></f7-input>
      </f7-list-item>
      <f7-list-item>
        <f7-label>末次月经时间</f7-label>
        <f7-input id="lastPeriodInput" type="date" placeholder="" value="2016-04-30" v-model="userLastPeroid"></f7-input>
      </f7-list-item>
      <f7-list-item>
        <f7-label>是否单胎妊娠</f7-label>
        <f7-input id="isSingleInput" type="select" v-model="userIsSingle">
          <option value="1">是</option>
          <option value="0">否</option>
        </f7-input>
      </f7-list-item>
      <p><f7-button round fill color="green" @click="onUpdateInfo">更新</f7-button></p>
    </f7-list>
	</f7-page>
</template>

<script>
	export default {
		data() {
			return {
				isNecessary:false
			}
		},
		computed:{
			userWeight() {
				return this.$store.state.userinfo.weight;
			},
			userHeight() {
				return this.$store.state.userinfo.height;
			},
			userLastPeroid() {
        var last = this.$store.state.userinfo.lastPeriod;
        if (last) {
          last = last.substring(0,10)
          // last = last.replace(/\-/g,'/')
          return last;
        }
				return '';
			},
			userIsSingle() {
				return this.$store.state.userinfo.isSingle;
			}
		},

		methods:{
			checkInput() {
  			var height = document.getElementById("heightInput").value
    		if (!height) {
    			this.$f7.alert('','请输入身高 单位:cm')
    			return false
    		}
    		if (height < 100 || height > 300) {
    			this.$f7.alert('','请输入身高 单位:cm')
    			return false
    		}
  			var weight = document.getElementById("weightInput").value
    		if (!weight) {
    			this.$f7.alert('','请输入体重 单位:kg')
    			return false
    		}
  			var isSingle = document.getElementById("isSingleInput").value
  			var	lastPeriod = document.getElementById("lastPeriodInput").value
    		if (!lastPeriod) {
    			this.$f7.alert('','请选择末次月经时间')
    			return false
    		}
    		var lastTime = new Date(lastPeriod).getTime();
    		var now = new Date().getTime();
    		if (lastTime + 24*60*60*1000 > now) {
    			this.$f7.alert('','请选择当日之前的时间')
    			return false
    		}

    		return true;
			},
			onUpdateInfo() {
    		if (!this.checkInput()) return;
    		this.$f7.showPreloader('保存中...')

    		//结果保存到服务端
  			var info = {
  				wxid:this.$store.state.wxid,
  				height:document.getElementById("heightInput").value,
  				weight:document.getElementById("weightInput").value,
  				isSingle:document.getElementById("isSingleInput").value,
  				lastPeriod:document.getElementById("lastPeriodInput").value
  			}
				this.$store.dispatch('updateInfo',{
					self:this,
					info:info,
  				callback:function(self,res) {
	          self.$store.commit('USERINFO',info);
	          //返回
	          self.$f7.alert('','保存成功',()=>{
	          	self.$router.go(-1);
	          })
  				}
				})
			}
		},

		mounted() {
      this.$f7.resize()
      this.isNecessary = this.$route.query.isNecessary || false;
      console.log(this.userLastPeroid)
      document.getElementById('lastPeriodInput').value = this.userLastPeroid
		}
	}
</script>