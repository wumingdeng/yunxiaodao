<template>
	<f7-page navbar-through>
		<f7-navbar sliding>
			<f7-nav-left>
				<f7-link icon="icon-back color-black" @click="$router.go(-1)"></f7-link>
			</f7-nav-left>
			<f7-nav-center sliding style="left:-22px;" title=""></f7-nav-center>
			<f7-nav-right></f7-nav-right>
		</f7-navbar>
		<f7-card style='font-size:18px;border-radius:7px'>
			<f7-card-content>
				<spane>推荐人</spane>
				<spane>&nbsp;&nbsp;&nbsp;{{$store.state.currentRequest.upName}}</spane>
				<f7-button fill style="float:right; width:30%;background:#68cfc1;font-size:18px" @click="onCheck">详情</f7-button>
			</f7-card-content>
		</f7-card>
		<f7-card inner style='border-radius:7px;margin: 10px;padding:20px 0px 5px 0px;'>
			<f7-list form style='margin-top:0px'>
				<f7-list-item :title="'姓名'" class="cus_item">
					<f7-label class='cus_label' style='width:40%'>{{$store.state.currentRequest.realName}}</f7-label>
				</f7-list-item>
				<f7-list-item :title="'手机号码'" class="cus_item">
					<f7-label class='cus_label' style='width:40%'>{{$store.state.currentRequest.phone}}</f7-label>
				</f7-list-item>
				<f7-list-item :title="'职业'" class="cus_item">
					<f7-label class='cus_label' style='width:40%'>{{job}}</f7-label>
				</f7-list-item>
				<f7-list-item :title="'执业医院'" class="cus_item">
					<f7-label class='cus_label' style='width:40%'>{{$store.state.currentRequest.hospital}}</f7-label>
				</f7-list-item>
				<f7-list-item :title="'科室'" class="cus_item">
					<f7-label class='cus_label' style='width:40%'>{{$store.state.currentRequest.department}}</f7-label>
				</f7-list-item>
				<f7-list-item class="cus_item">
					<f7-label style='width:30%;margin-bottom:100px'>执业证书</f7-label>
					<v-touch
            tag='img' 
            style="width:30%; display:block;" 
            v-lazy="cert"
            v-bind:enabled="{ swipe: false}"
            @tap="openPhotoBrowser(1)"
            @swipeup="onTouchStart"
            >
          </v-touch>
          <f7-photo-browser
            ref="pb"
            :photos="photos"
            backLinkText=""
            ofText="/"
            :loop="true"
            :exposition="false"
            :expositionHideCaptions="false"
            :lazyLoading="true"
            :lazyLoadingInPrevNext="true"
            @click="onClickPB"
          ></f7-photo-browser>
				</f7-list-item>
				<f7-list-item>
					<f7-label style='width:30%;margin-bottom:60px'>审核建议</f7-label>
					<f7-input id="adviceInput" class='cus_textarea' style='height:5em;background:rgb(230,230,230);border-radius:10px;margin:0px;padding:0px 10px' type="textarea"></f7-input>
				</f7-list-item>
			</f7-list>
			<f7-grid style='margin:10px'>
				<f7-col width=50>
					<f7-button fill big style='background:#68cfc1;border-radius:10px' @click="onAccept">审核通过</f7-button>
				</f7-col>
				<f7-col width=50>
					<f7-button fill big style='background:rgba(0,0,0,0);color:#68cfc1;border:2px solid #68cfc1;border-radius:10px' @click="onRefuse">审核不通过</f7-button>
				</f7-col>
			</f7-grid>
		</f7-card>
	</f7-page>
</template>

<script>
import g from '../../globals/global'
export default {
	data() {
		return {
			upid: null,	//上线
			isTouch: false, //连点控制
			isRequest: false 	//是否为申请推广人
		}
	},
	computed: {
		photos() {
				var cert = this.$store.state.currentRequest.certificate;
				return [g.serverAddress + '/'+cert]
				// return [g.serverAddress + '/certificate/cert-oni2YwgQMsC6sEFHTde3xA5aH0_A.jpeg']
      },
		job() {
			return this.$store.state.currentRequest.job == 1 ? '医生' : '护士';
		},
		cert() {
			var cert = this.$store.state.currentRequest.certificate;
			console.log(cert)
			if (cert) {
				// return g.serverAddress + '/certificate/cert-oni2YwgQMsC6sEFHTde3xA5aH0_A.jpeg';
				return g.serverAddress + '/'+cert;
			} else {
				return ''
			}
		}
	},
	methods: {
		onTouchStart() {
        console.log('touchstart')
      },
		openPhotoBrowser(index) {
        console.log(index)
        this.$refs.pb.open(index)
        this.$refs.pb.enableExposition()
      },
      onClickPB(swiper,event) {
        this.$nextTick(this.$refs.pb.close)
      },
		onCheck() {
			this.$router.push({
				path: '/checkSaleman',
				query: {
					userid: this.$store.state.currentRequest.upid
				}
			})
		},
		onAccept() {
			var self = this
			this.$f7.confirm('您将通过申请', function () {
				self.$store.dispatch('acceptRequest', {
					self: self,
					info: {
						id: self.$store.state.currentRequest.id
					},
					callback(self, res) {
						if (res.body.ok == 0) {
							self.$f7.alert('', '已经通过推广申请', function () {
								self.$router.go(-1);
							})
						}
					}
				})
			})
		},
		onRefuse() {
			var adviceInput = document.getElementById('adviceInput').value
			if (!adviceInput) {
				this.$f7.alert('', '请填写审核建议');
				return false
			}
			this.$store.dispatch('refuseRequest', {
				self: this,
				info: {
					id: this.$store.state.currentRequest.id,
					advice: adviceInput
				},
				callback(self, res) {
					if (res.body.ok == 0) {
						self.$f7.alert('', '已经拒绝此推广申请', function () {
							self.$router.go(-1);
						})
					}
				}
			})
		}
	},
	mounted() {
		this.isRequest = this.$route.query.request;
		this.upid = this.$route.query.upid;
	},
	beforeDestroy() {
      this.$refs.pb.close();
      this.$f7.closeModal()
    }
}
</script>

<style>
.content-block-inner {
	border-radius: 10px;
}
.cus_item {
	border-bottom:1px solid rgb(196,195,200,0.7)
}
.cus_item .item-inner:after {
		background-color: rgba(0,0,0,0);
}

.cus_label {
	text-align: right;
	color:rgb(150,150,150)
}

.cus_textarea textarea {
	height:80px;
}
</style>