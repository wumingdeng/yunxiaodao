package org.liuhe.database;

public class FootStudyInfo {
	
	private String user_id;
	private int period;
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public int getPeriod() {
		return period;
	}
	public void setPeriod(int period) {
		this.period = period;
	}

	private String open_id;						//关注者的Open_id
	private String card_id;						//身份证Card_id
	private String subscribe_time_str;			//关注时间YYYY-MM-DD HH:MM:SS
	private int subscribe_status;				//关注状态：1表示已关注；0表示未关注或取消关注
	private int file_amount;					//该微信号关联的脚型档案总数
	private boolean isSingle ;
	
	private String user_account;				//app用户名
	private String user_pwd;					//app密码
	private String user_phone;					//用户电话
	private String user_email;					//用户邮箱
	
	private String address;
	
	//医院挂号相关的信息
	private String hospital_no;
	private String hospital_name;
	private String clinic_dept;
	private String doctor_name;
	// modify by kael
	private int   current_doctor_id;
	// modify by kael over
	private String clinic_type;
	
	private String clinic_status;
	private String queue_num;
	private String wait_num;
	
	public boolean isSingle() {
		return this.isSingle;
	}
	public void setIsSingle(boolean is) {
		this.isSingle=is;
	}
	
	public String getHospital_no() {
		return hospital_no;
	}
	public void setHospital_no(String hospitalNo) {
		hospital_no = hospitalNo;
	}
	public String getHospital_name() {
		return hospital_name;
	}
	public void setHospital_name(String hospitalName) {
		hospital_name = hospitalName;
	}
	public String getClinic_dept() {
		return clinic_dept;
	}
	public void setClinic_dept(String clinicDept) {
		clinic_dept = clinicDept;
	}
	public void setCurrentDoctor_id(int id) {
		current_doctor_id = id;
	}
	public int getCurrentDoctor_id() {
		return current_doctor_id;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctorName) {
		doctor_name = doctorName;
	}
	public String getClinic_type() {
		return clinic_type;
	}
	public void setClinic_type(String clinicType) {
		clinic_type = clinicType;
	}
	public String getClinic_status() {
		return clinic_status;
	}
	public void setClinic_status(String clinicStatus) {
		clinic_status = clinicStatus;
	}
	public String getQueue_num() {
		return queue_num;
	}
	public void setQueue_num(String queueNum) {
		queue_num = queueNum;
	}
	public String getWait_num() {
		return wait_num;
	}
	public void setWait_num(String waitNum) {
		wait_num = waitNum;
	}

	//身高体重
	private String height;
	private String weight;
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public Float getHeight_float() {
		if(height==null||height.equals("")){
			return null;
		}else{
			return Float.parseFloat(height);
		}
	}
	public void setHeight_float(Float height_float) {
		if(height_float==null){
			height = "";
		}else{
			height = height_float+"";
		}
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Float getWeight_float() {
		if(weight==null||weight.equals("")){
			return null;
		}else{
			return Float.parseFloat(weight);
		}
	}
	public void setWeight_float(Float weight_float) {
		if(weight_float==null){
			weight = "";
		}else{
			weight = weight_float+"";
		}
	}
	//微信用户和账号用户
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String openId) {
		open_id = openId;
	}
	public String getSubscribe_time_str() {
		return subscribe_time_str;
	}
	public void setSubscribe_time_str(String subscribeTimeStr) {
		subscribe_time_str = subscribeTimeStr;
	}
	public int getSubscribe_status() {
		return subscribe_status;
	}
	public void setSubscribe_status(int subscribeStatus) {
		subscribe_status = subscribeStatus;
	}
	public int getFile_amount() {
		return file_amount;
	}
	public void setFile_amount(int fileAmount) {
		file_amount = fileAmount;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String userAccount) {
		user_account = userAccount;
	}
	public String getUser_pwd() {
		return user_pwd;
	}
	public void setUser_pwd(String user_pwd) {
		this.user_pwd = user_pwd;
	}
	public String getUser_phone() {
		return user_phone;
	}
	public void setUser_phone(String userPhone) {
		user_phone = userPhone;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String userEmail) {
		user_email = userEmail;
	}
	public String getCard_id() {
		return card_id;
	}
	public void setCard_id(String cardId) {
		card_id = cardId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	//档案基本信息
	private String mac_id;						//未关联微信Open_id时的表间关联字段
	private String from_id;						//标记该档案是来自哪里：为终端的MAC地址
	private String from_app;					//标记来源的公众号app
	private String ticket;						//场景值
	private String scene;						//场景ID
	private String name;						//该档案的名称
	private String birth_str;					//出生日期
	private int age;							//年龄
	private String sex;							//性别
	private String district;					//七大地区之一
	private String province;					//省份
	private String city;						//城市
	private String county;						//县市区
	private String country;						//国籍
	private String nation;						//民族
	private String date_host_str;				//归档日期（采集时间）
	private String sign;						//标记号
	private String crowd;						//标记人群
	private String remark;						//备注
	private String date_yunfu_str;				//末次月经时间
	//参数分析(左右各21个参数)
	private String left_length;
	private String left_width;
	private String left_width_41full;
	private String left_center_angle;
	private String left_length_90;
	private String left_length_825;
	private String left_length_78;
	private String left_length_725;
	private String left_length_635;
	private String left_length_68;
	private String left_length_41;
	private String left_length_18;
	private String left_width_90;
	private String left_width_78;
	private String left_width_725;
	private String left_width_635;
	private String left_width_41;
	private String left_width_18;
	private String left_width_ratio;
	private String left_width_68;
	private String left_incline_angle;
	private String left_size_01;
	private String left_size_02;
	private String left_size_03;
	private String left_size_04;
	private String left_size_05;
	private String left_size_06;
	private String left_size_07;
	private String left_size_08;
	private String left_size_09;
	private String left_size_10;
	private String left_size_11;
	private String left_size_12;
	private String left_size_13;
	//右脚分析参数
	private String right_length;
	private String right_width;
	private String right_width_41full;
	private String right_center_angle;
	private String right_length_90;
	private String right_length_825;
	private String right_length_78;
	private String right_length_725;
	private String right_length_635;
	private String right_length_68;
	private String right_length_41;
	private String right_length_18;
	private String right_width_90;
	private String right_width_78;
	private String right_width_725;
	private String right_width_635;
	private String right_width_41;
	private String right_width_18;
	private String right_width_ratio;
	private String right_width_68;
	private String right_incline_angle;
	private String right_size_01;
	private String right_size_02;
	private String right_size_03;
	private String right_size_04;
	private String right_size_05;
	private String right_size_06;
	private String right_size_07;
	private String right_size_08;
	private String right_size_09;
	private String right_size_10;
	private String right_size_11;
	private String right_size_12;
	private String right_size_13;
	//专家意见
	private String left_foot_size;
	private String left_foot_width;
	private String left_foot_width2;
	private String left_foot_status;
	private String left_advice;
	private String right_foot_size;
	private String right_foot_width;
	private String right_foot_width2;
	private String right_foot_status;
	private String right_advice;
	private String left_para_01;
	private String left_para_02;
	private String left_para_03;
	private String left_para_04;
	private String left_para_05;
	private String left_para_06;
	private String right_para_01;
	private String right_para_02;
	private String right_para_03;
	private String right_para_04;
	private String right_para_05;
	private String right_para_06;
	private String left_foot_type;
	private String right_foot_type;
	private String foot_leg;
	private int left_int_status;
	private int right_int_status;
	//图片路径
	private String left_url_ab;
	private String right_url_ab;
	private int left_dpi;
	private int right_dpi;
	private String left_url;
	private String right_url;
	private String left_urla;
	private String right_urla;
	//其他坐标辅助参考
	private float lcircle_01_x;
	private float lcircle_01_y;
	private float lcircle_02_x;
	private float lcircle_02_y;
	private float lcircle_03_x;
	private float lcircle_03_y;
	private float lcircle_725_x;
	private float lcircle_725_y;
	private float lcircle_635_x;
	private float lcircle_635_y;
	private float lcircle_4101_x;
	private float lcircle_4101_y;
	private float lcircle_4102_x;
	private float lcircle_4102_y;
	private float lbreak_01_x;
	private float lbreak_01_y;
	private float lbreak_02_x;
	private float lbreak_02_y;
	private int lfoot_top;
	private int lfoot_bottom;
	private int lfoot_right;
	private int lfoot_left;
	private double lscale;
	private float lcircle_18_x;
	private float lcircle_18_y;
	private float lcircle_1801_x;
	private float lcircle_1801_y;
	private float lcircle_1802_x;
	private float lcircle_1802_y;
	private float lcircle_90_x;
	private float lcircle_90_y;
	private float lcircle_825_x;
	private float lcircle_825_y;
	private float lcircle_78_x;
	private float lcircle_78_y;
	private float lcircle_80_x;
	private float lcircle_80_y;
	private float lcircle_65_x;
	private float lcircle_65_y;
	//右脚参考坐标和辅助线
	private float rcircle_01_x;
	private float rcircle_01_y;
	private float rcircle_02_x;
	private float rcircle_02_y;
	private float rcircle_03_x;
	private float rcircle_03_y;
	private float rcircle_725_x;
	private float rcircle_725_y;
	private float rcircle_635_x;
	private float rcircle_635_y;
	private float rcircle_4101_x;
	private float rcircle_4101_y;
	private float rcircle_4102_x;
	private float rcircle_4102_y;
	private float rbreak_01_x;
	private float rbreak_01_y;
	private float rbreak_02_x;
	private float rbreak_02_y;
	private int rfoot_top;
	private int rfoot_bottom;
	private int rfoot_right;
	private int rfoot_left;
	private double rscale;
	private float rcircle_18_x;
	private float rcircle_18_y;
	private float rcircle_1801_x;
	private float rcircle_1801_y;
	private float rcircle_1802_x;
	private float rcircle_1802_y;
	private float rcircle_90_x;
	private float rcircle_90_y;
	private float rcircle_825_x;
	private float rcircle_825_y;
	private float rcircle_78_x;
	private float rcircle_78_y;
	private float rcircle_80_x;
	private float rcircle_80_y;
	private float rcircle_65_x;
	private float rcircle_65_y;
	//左右脚的翻转状态
	private int lh_turn;
	private int lv_turn;
	private int rh_turn;
	private int rv_turn;
	
	//档案基本信息
	public String getMac_id(){
		return mac_id;
	}
	public void setMac_id(String macId){
		mac_id = macId;
	}
	public String getFrom_id(){
		return from_id;
	}
	public void setFrom_id(String fromId){
		from_id = fromId;
	}
	public String getFrom_app(){
		return from_app;
	}
	public void setFrom_app(String from_app){
		this.from_app = from_app;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getScene() {
		return scene;
	}
	public void setScene(String scene) {
		this.scene = scene;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBirth_str() {
		return birth_str;
	}
	public void setBirth_str(String birthStr) {
		birth_str = birthStr;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	/*//@unused
	public void setIndate(Date indate,String format) {
		if(indate != null){
			SimpleDateFormat simpledate = new SimpleDateFormat(format);
			date_host_str = simpledate.format(indate);
		}
	}*/
	public String getDate_host_str() {
		return date_host_str;
	}
	public void setDate_host_str(String date_host_str) {
		this.date_host_str = date_host_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getCrowd() {
		return crowd;
	}
	public void setCrowd(String crowd) {
		this.crowd = crowd;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRemark() {
		return remark;
	}
	public void setDistrict(String district){
		this.district = district;
	}
	public String getDistrict(){
		return district;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public void setDate_yunfu_str(String date_yunfu_str) {
		this.date_yunfu_str = date_yunfu_str;
	}
	public String getDate_yunfu_str() {
		return date_yunfu_str;
	}
	//参数分析(左右各21个参数)
	public String getLeft_length() {
		return left_length;
	}
	public void setLeft_length(String leftLength) {
		left_length = leftLength;
	}
	public Float getLeft_length_float() {
		if(left_length==null||left_length.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length);
		}
	}
	public void setLeft_length_float(Float leftLength) {
		if(leftLength==null){
			left_length = "";
		}else{
			left_length = leftLength+"";
		}
	}//
	public String getLeft_width() {
		return left_width;
	}
	public void setLeft_width(String leftWidth) {
		left_width = leftWidth;
	}
	public Float getLeft_width_float() {
		if(left_width==null||left_width.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width);
		}
	}
	public void setLeft_width_float(Float leftWidth) {
		if(leftWidth==null){
			left_width = "";
		}else{
			left_width = leftWidth+"";
		}
	}//
	public String getLeft_width_41full() {
		return left_width_41full;
	}
	public void setLeft_width_41full(String leftWidth_41full) {
		left_width_41full = leftWidth_41full;
	}
	public Float getLeft_width_41full_float() {
		if(left_width_41full==null||left_width_41full.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_41full);
		}
	}
	public void setLeft_width_41full_float(Float leftWidth_41full) {
		if(leftWidth_41full==null){
			left_width_41full = "";
		}else{
			left_width_41full = leftWidth_41full+"";
		}
	}//
	public String getLeft_center_angle() {
		return left_center_angle;
	}
	public void setLeft_center_angle(String leftCenterAngle) {
		left_center_angle = leftCenterAngle;
	}
	public Float getLeft_center_angle_float() {
		if(left_center_angle.equals("")){
			return null;
		}else{
			try{
				return Float.parseFloat(left_center_angle);
			}catch(Exception e){
				return 0.0f;
			}
		}
	}
	public void setLeft_center_angle_float(Float leftCenterAngle) {
		if(leftCenterAngle==null){
			left_center_angle = "";
		}else{
			left_center_angle = leftCenterAngle+"";
		}
	}//
	public String getLeft_length_90() {
		return left_length_90;
	}
	public void setLeft_length_90(String leftLength_90) {
		left_length_90 = leftLength_90;
	}
	public Float getLeft_length_90_float() {
		if(left_length_90.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_90);
		}
	}
	public void setLeft_length_90_float(Float leftLength_90) {
		if(leftLength_90==null){
			left_length_90 = "";
		}else{
			left_length_90 = leftLength_90+"";
		}
	}//
	public String getLeft_length_825() {
		return left_length_825;
	}
	public void setLeft_length_825(String leftLength_825) {
		left_length_825 = leftLength_825;
	}
	public Float getLeft_length_825_float() {
		if(left_length_825.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_825);
		}
	}
	public void setLeft_length_825_float(Float leftLength_825) {
		if(leftLength_825==null){
			left_length_825 = "";
		}else{
			left_length_825 =leftLength_825+"";
		}
	}//
	public String getLeft_length_78() {
		return left_length_78;
	}
	public void setLeft_length_78(String leftLength_78) {
		left_length_78 = leftLength_78;
	}
	public Float getLeft_length_78_float() {
		if(left_length_78.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_78);
		}
	}
	public void setLeft_length_78_float(Float leftLength_78) {
		if(leftLength_78==null){
			left_length_78 = "";
		}else{
			left_length_78 = leftLength_78+"";
		}
	}//
	public String getLeft_length_725() {
		return left_length_725;
	}
	public void setLeft_length_725(String leftLength_725) {
		left_length_725 = leftLength_725;
	}
	public Float getLeft_length_725_float() {
		if(left_length_725.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_725);
		}
	}
	public void setLeft_length_725_float(Float leftLength_725) {
		if(leftLength_725==null){
			left_length_725 = "";
		}else{
			left_length_725 = leftLength_725+"";
		}
		
	}//
	public String getLeft_length_635() {
		return left_length_635;
	}
	public void setLeft_length_635(String leftLength_635) {
		left_length_635 = leftLength_635;
	}
	public Float getLeft_length_635_float() {
		if(left_length_635.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_635);
		}
	}
	public void setLeft_length_635_float(Float leftLength_635) {
		if(leftLength_635==null){
			left_length_635 = "";
		}else{
			left_length_635 = leftLength_635+"";
		}
	}//
	public String getLeft_length_68() {
		return left_length_68;
	}
	public void setLeft_length_68(String leftLength_68) {
		left_length_68 = leftLength_68;
	}
	public Float getLeft_length_68_float() {
		if(left_length_68.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_68);
		}
	}
	public void setLeft_length_68_float(Float leftLength_68) {
		if(leftLength_68==null){
			left_length_68 = "";
		}else{
			left_length_68 = leftLength_68+"";
		}
	}//
	public String getLeft_length_41() {
		return left_length_41;
	}
	public void setLeft_length_41(String leftLength_41) {
		left_length_41 = leftLength_41;
	}
	public Float getLeft_length_41_float() {
		if(left_length_41.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_41);
		}
	}
	public void setLeft_length_41_float(Float leftLength_41) {
		if(leftLength_41==null){
			left_length_41 = "";
		}else{
			left_length_41 = leftLength_41+"";
		}
	}//
	public String getLeft_length_18() {
		return left_length_18;
	}
	public void setLeft_length_18(String leftLength_18) {
		left_length_18 = leftLength_18;
	}
	public Float getLeft_length_18_float() {
		if(left_length_18.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_length_18);
		}
	}
	public void setLeft_length_18_float(Float leftLength_18) {
		if(leftLength_18==null){
			left_length_18 = "";
		}else{
			left_length_18 = leftLength_18+"";
		}
	}//
	public String getLeft_width_90() {
		return left_width_90;
	}
	public void setLeft_width_90(String leftWidth_90) {
		left_width_90 = leftWidth_90;
	}
	public Float getLeft_width_90_float() {
		if(left_width_90.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_90);
		}
	}
	public void setLeft_width_90_float(Float leftWidth_90) {
		if(leftWidth_90==null){
			left_width_90 = "";
		}else{
			left_width_90 = leftWidth_90+"";
		}
	}//
	public String getLeft_width_78() {
		return left_width_78;
	}
	public void setLeft_width_78(String leftWidth_78) {
		left_width_78 = leftWidth_78;
	}
	public Float getLeft_width_78_float() {
		if(left_width_78.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_78);
		}
	}
	public void setLeft_width_78_float(Float leftWidth_78) {
		if(leftWidth_78==null){
			left_width_78 = "";
		}else{
			left_width_78 = leftWidth_78+"";
		}
	}//
	public String getLeft_width_725() {
		return left_width_725;
	}
	public void setLeft_width_725(String leftWidth_725) {
		left_width_725 = leftWidth_725;
	}
	public Float getLeft_width_725_float() {
		if(left_width_725.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_725);
		}
	}
	public void setLeft_width_725_float(Float leftWidth_725) {
		if(leftWidth_725==null){
			left_width_725 = "";
		}else{
			left_width_725 = leftWidth_725+"";
		}
	}//
	public String getLeft_width_635() {
		return left_width_635;
	}
	public void setLeft_width_635(String leftWidth_635) {
		left_width_635 = leftWidth_635;
	}
	public Float getLeft_width_635_float() {
		if(left_width_635.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_635);
		}
	}
	public void setLeft_width_635_float(Float leftWidth_635) {
		if(leftWidth_635==null){
			left_width_635 = "";
		}else{
			left_width_635 = leftWidth_635+"";
		}
	}//
	public String getLeft_width_41() {
		return left_width_41;
	}
	public void setLeft_width_41(String leftWidth_41) {
		left_width_41 = leftWidth_41;
	}
	public Float getLeft_width_41_float() {
		if(left_width_41.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_41);
		}
	}
	public void setLeft_width_41_float(Float leftWidth_41) {
		if(leftWidth_41==null){
			left_width_41 = "";
		}else{
			left_width_41 = leftWidth_41+"";
		}
	}//
	public String getLeft_width_18() {
		return left_width_18;
	}
	public void setLeft_width_18(String leftWidth_18) {
		left_width_18 = leftWidth_18;
	}
	public Float getLeft_width_18_float() {
		if(left_width_18.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_18);
		}
	}
	public void setLeft_width_18_float(Float leftWidth_18) {
		if(leftWidth_18==null){
			left_width_18 = "";
		}else{
			left_width_18 = leftWidth_18+"";
		}
	}//
	public String getLeft_width_ratio() {
		return left_width_ratio;
	}
	public void setLeft_width_ratio(String leftWidthRatio) {
		left_width_ratio = leftWidthRatio;
	}
	public Float getLeft_width_ratio_float() {
		if(left_width_ratio.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_ratio);
		}
	}
	public void setLeft_width_ratio_float(Float leftWidthRatio) {
		if(leftWidthRatio==null){
			left_width_ratio = "";
		}else{
			left_width_ratio = leftWidthRatio+"";
		}
	}//
	public String getLeft_width_68() {
		return left_width_68;
	}
	public void setLeft_width_68(String leftWidth_68) {
		left_width_68 = leftWidth_68;
	}
	public Float getLeft_width_68_float() {
		if(left_width_68.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_width_68);
		}
	}
	public void setLeft_width_68_float(Float leftWidth_68) {
		if(leftWidth_68==null){
			left_width_68 = "";
		}else{
			left_width_68 = leftWidth_68+"";
		}
	}//
	public String getLeft_incline_angle() {
		return left_incline_angle;
	}
	public void setLeft_incline_angle(String leftInclineAngle) {
		left_incline_angle = leftInclineAngle;
	}
	public Float getLeft_incline_angle_float() {
		if(left_incline_angle.equals("")){
			return null;
		}else{
			try{
				return Float.parseFloat(left_incline_angle);
			}catch(Exception e){
				return 0.0f;
			}
		}
	}
	public void setLeft_incline_angle_float(Float leftInclineAngle) {
		if(leftInclineAngle==null){
			left_incline_angle = "";
		}else{
			left_incline_angle = leftInclineAngle+"";
		}
	}//
	public String getLeft_size_01() {
		return left_size_01;
	}
	public void setLeft_size_01(String leftSize_01) {
		left_size_01 = leftSize_01;
	}
	public Float getLeft_size_01_float() {
		if(left_size_01.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_01);
		}
	}
	public void setLeft_size_01_float(Object leftSize_01) {
		if(leftSize_01==null){
			left_size_01 = "";
		}else{
			left_size_01 = leftSize_01+"";
		}
	}//
	public String getLeft_size_02() {
		return left_size_02;
	}
	public void setLeft_size_02(String leftSize_02) {
		left_size_02 = leftSize_02;
	}
	public Float getLeft_size_02_float() {
		if(left_size_02.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_02);
		}
	}
	public void setLeft_size_02_float(Object leftSize_02) {
		if(leftSize_02==null){
			left_size_02 = "";
		}else{
			left_size_02 = leftSize_02+"";
		}
	}//
	public String getLeft_size_03() {
		return left_size_03;
	}
	public void setLeft_size_03(String leftSize_03) {
		left_size_03 = leftSize_03;
	}
	public Float getLeft_size_03_float() {
		if(left_size_03.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_03);
		}
	}
	public void setLeft_size_03_float(Object leftSize_03) {
		if(leftSize_03==null){
			left_size_03 = "";
		}else{
			left_size_03 = leftSize_03+"";
		}
	}//
	public String getLeft_size_04() {
		return left_size_04;
	}
	public void setLeft_size_04(String leftSize_04) {
		left_size_04 = leftSize_04;
	}
	public Float getLeft_size_04_float() {
		if(left_size_04.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_04);
		}
	}
	public void setLeft_size_04_float(Object leftSize_04) {
		if(leftSize_04==null){
			left_size_04 = "";
		}else{
			left_size_04 = leftSize_04+"";
		}
	}//
	public String getLeft_size_05() {
		return left_size_05;
	}
	public void setLeft_size_05(String leftSize_05) {
		left_size_05 = leftSize_05;
	}
	public Float getLeft_size_05_float() {
		if(left_size_05.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_05);
		}
	}
	public void setLeft_size_05_float(Object leftSize_05) {
		if(leftSize_05==null){
			left_size_05 = "";
		}else{
			left_size_05 = leftSize_05+"";
		}
	}//
	public String getLeft_size_06() {
		return left_size_06;
	}
	public void setLeft_size_06(String leftSize_06) {
		left_size_06 = leftSize_06;
	}
	public Float getLeft_size_06_float() {
		if(left_size_06.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_06);
		}
	}
	public void setLeft_size_06_float(Object leftSize_06) {
		if(leftSize_06==null){
			left_size_06 = "";
		}else{
			left_size_06 = leftSize_06+"";
		}
	}//
	public String getLeft_size_07() {
		return left_size_07;
	}
	public void setLeft_size_07(String leftSize_07) {
		left_size_07 = leftSize_07;
	}
	public Float getLeft_size_07_float() {
		if(left_size_07.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_07);
		}
	}
	public void setLeft_size_07_float(Object leftSize_07) {
		if(leftSize_07==null){
			left_size_07 = "";
		}else{
			left_size_07 = leftSize_07+"";
		}
	}//
	public String getLeft_size_08() {
		return left_size_08;
	}
	public void setLeft_size_08(String leftSize_08) {
		left_size_08 = leftSize_08;
	}
	public Float getLeft_size_08_float() {
		if(left_size_08.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_08);
		}
	}
	public void setLeft_size_08_float(Object leftSize_08) {
		if(leftSize_08==null){
			left_size_08 = "";
		}else{
			left_size_08 = leftSize_08+"";
		}
	}//
	public String getLeft_size_09() {
		return left_size_09;
	}
	public void setLeft_size_09(String leftSize_09) {
		left_size_09 = leftSize_09;
	}
	public Float getLeft_size_09_float() {
		if(left_size_09.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_09);
		}
	}
	public void setLeft_size_09_float(Object leftSize_09) {
		if(leftSize_09==null){
			left_size_09 = "";
		}else{
			left_size_09 = leftSize_09+"";
		}
	}//
	public String getLeft_size_10() {
		return left_size_10;
	}
	public void setLeft_size_10(String leftSize_10) {
		left_size_10 = leftSize_10;
	}
	public Float getLeft_size_10_float() {
		if(left_size_10.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_10);
		}
	}
	public void setLeft_size_10_float(Object leftSize_10) {
		if(leftSize_10==null){
			left_size_10 = "";
		}else{
			left_size_10 = leftSize_10+"";
		}
	}//
	public String getLeft_size_11() {
		return left_size_11;
	}
	public void setLeft_size_11(String leftSize_11) {
		left_size_11 = leftSize_11;
	}
	public Float getLeft_size_11_float() {
		if(left_size_11.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_11);
		}
	}
	public void setLeft_size_11_float(Object leftSize_11) {
		if(leftSize_11==null){
			left_size_11 = "";
		}else{
			left_size_11 = leftSize_11+"";
		}
	}//
	public String getLeft_size_12() {
		return left_size_12;
	}
	public void setLeft_size_12(String leftSize_12) {
		left_size_12 = leftSize_12;
	}
	public Float getLeft_size_12_float() {
		if(left_size_12.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_12);
		}
	}
	public void setLeft_size_12_float(Object leftSize_12) {
		if(leftSize_12==null){
			left_size_12 = "";
		}else{
			left_size_12 = leftSize_12+"";
		}
	}//
	public String getLeft_size_13() {
		return left_size_13;
	}
	public void setLeft_size_13(String leftSize_13) {
		left_size_13 = leftSize_13;
	}
	public Float getLeft_size_13_float() {
		if(left_size_13.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_size_13);
		}
	}
	public void setLeft_size_13_float(Object leftSize_13) {
		if(leftSize_13==null){
			left_size_13 = "";
		}else{
			left_size_13 = leftSize_13+"";
		}
	}//
	//右脚的分析参数的getXXX()和setXXX()方法
	public String getRight_length() {
		return right_length;
	}
	public void setRight_length(String rightLength) {
		right_length = rightLength;
	}
	public Float getRight_length_float() {
		if(right_length==null||right_length.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length);
		}
	}
	public void setRight_length_float(Float rightLength) {
		if(rightLength==null){
			right_length = "";
		}else{
			right_length = rightLength+"";
		}
	}//
	public String getRight_width() {
		return right_width;
	}
	public void setRight_width(String rightWidth) {
		right_width = rightWidth;
	}
	public Float getRight_width_float() {
		if(right_width==null||right_width.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width);
		}
	}
	public void setRight_width_float(Float rightWidth) {
		if(rightWidth==null){
			right_width = ""; 
		}else{
			right_width = rightWidth+"";
		}
	}//
	public String getRight_width_41full() {
		return right_width_41full;
	}
	public void setRight_width_41full(String rightWidth_41full) {
		right_width_41full = rightWidth_41full;
	}
	public Float getRight_width_41full_float() {
		if(right_width_41full==null||right_width_41full.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_41full);
		}
	}
	public void setRight_width_41full_float(Float rightWidth_41full) {
		if(rightWidth_41full==null){
			right_width_41full = "";
		}else{
			right_width_41full = rightWidth_41full+"";
		}
	}//
	public String getRight_center_angle() {
		return right_center_angle;
	}
	public void setRight_center_angle(String rightCenterAngle) {
		right_center_angle = rightCenterAngle;
	}
	public Float getRight_center_angle_float() {
		if(right_center_angle.equals("")){
			return null;
		}else{
			try{
				return Float.parseFloat(right_center_angle);
			}catch(Exception e){
				return 0.0f;
			}
		}
	}
	public void setRight_center_angle_float(Float rightCenterAngle) {
		if(rightCenterAngle==null){
			right_center_angle = "";
		}else{
			right_center_angle = rightCenterAngle+"";
		}
	}//
	public String getRight_length_90() {
		return right_length_90;
	}
	public void setRight_length_90(String rightLength_90) {
		right_length_90 = rightLength_90;
	}
	public Float getRight_length_90_float() {
		if(right_length_90.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_90);
		}
	}
	public void setRight_length_90_float(Float rightLength_90) {
		if(rightLength_90==null){
			right_length_90 = "";
		}else{
			right_length_90 = rightLength_90+"";
		}
	}//
	public String getRight_length_825() {
		return right_length_825;
	}
	public void setRight_length_825(String rightLength_825) {
		right_length_825 = rightLength_825;
	}
	public Float getRight_length_825_float() {
		if(right_length_825.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_825);
		}
	}
	public void setRight_length_825_float(Float rightLength_825) {
		if(rightLength_825==null){
			right_length_825 = "";
		}else{
			right_length_825 = rightLength_825+"";
		}
	}//
	public String getRight_length_78() {
		return right_length_78;
	}
	public void setRight_length_78(String rightLength_78) {
		right_length_78 = rightLength_78;
	}
	public Float getRight_length_78_float() {
		if(right_length_78.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_78);
		}
	}
	public void setRight_length_78_float(Float rightLength_78) {
		if(rightLength_78==null){
			right_length_78 = "";
		}else{
			right_length_78 = rightLength_78+"";
		}
	}//
	public String getRight_length_725() {
		return right_length_725;
	}
	public void setRight_length_725(String rightLength_725) {
		right_length_725 = rightLength_725;
	}
	public Float getRight_length_725_float() {
		if(right_length_725.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_725);
		}
	}
	public void setRight_length_725_float(Float rightLength_725) {
		if(rightLength_725==null){
			right_length_725 = "";
		}else{
			right_length_725 = rightLength_725+"";
		}
	}//
	public String getRight_length_635() {
		return right_length_635;
	}
	public void setRight_length_635(String rightLength_635) {
		right_length_635 = rightLength_635;
	}
	public Float getRight_length_635_float() {
		if(right_length_635.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_635);
		}
	}
	public void setRight_length_635_float(Float rightLength_635) {
		if(rightLength_635==null){
			right_length_635 = "";
		}else{
			right_length_635 = rightLength_635+"";
		}
	}//
	public String getRight_length_68() {
		return right_length_68;
	}
	public void setRight_length_68(String rightLength_68) {
		right_length_68 = rightLength_68;
	}
	public Float getRight_length_68_float() {
		if(right_length_68.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_68);
		}
	}
	public void setRight_length_68_float(Float rightLength_68) {
		if(rightLength_68==null){
			right_length_68 = "";
		}else{
			right_length_68 = rightLength_68+"";
		}
	}//
	public String getRight_length_41() {
		return right_length_41;
	}
	public void setRight_length_41(String rightLength_41) {
		right_length_41 = rightLength_41;
	}
	public Float getRight_length_41_float() {
		if(right_length_41.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_41);
		}
	}
	public void setRight_length_41_float(Float rightLength_41) {
		if(rightLength_41==null){
			right_length_41 = "";
		}else{
			right_length_41 = rightLength_41+"";
		}
	}//
	public String getRight_length_18() {
		return right_length_18;
	}
	public void setRight_length_18(String rightLength_18) {
		right_length_18 = rightLength_18;
	}
	public Float getRight_length_18_float() {
		if(right_length_18.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_length_18);
		}
	}
	public void setRight_length_18_float(Float rightLength_18) {
		if(rightLength_18==null){
			right_length_18 = "";
		}else{
			right_length_18 = rightLength_18+"";
		}
	}//
	public String getRight_width_90() {
		return right_width_90;
	}
	public void setRight_width_90(String rightWidth_90) {
		right_width_90 = rightWidth_90;
	}
	public Float getRight_width_90_float() {
		if(right_width_90.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_90);
		}
	}
	public void setRight_width_90_float(Float rightWidth_90) {
		if(rightWidth_90==null){
			right_width_90 = "";
		}else{
			right_width_90 = rightWidth_90+"";
		}
	}//
	public String getRight_width_78() {
		return right_width_78;
	}
	public void setRight_width_78(String rightWidth_78) {
		right_width_78 = rightWidth_78;
	}
	public Float getRight_width_78_float() {
		if(right_width_78.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_78);
		}
	}
	public void setRight_width_78_float(Float rightWidth_78) {
		if(rightWidth_78==null){
			right_width_78 = "";
		}else{
			right_width_78 = rightWidth_78+"";
		}
	}//
	public String getRight_width_725() {
		return right_width_725;
	}
	public void setRight_width_725(String rightWidth_725) {
		right_width_725 = rightWidth_725;
	}
	public Float getRight_width_725_float() {
		if(right_width_725.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_725);
		}
	}
	public void setRight_width_725_float(Float rightWidth_725) {
		if(rightWidth_725==null){
			right_width_725 = "";
		}else{
			right_width_725 = rightWidth_725+"";
		}
	}//
	public String getRight_width_635() {
		return right_width_635;
	}
	public void setRight_width_635(String rightWidth_635) {
		right_width_635 = rightWidth_635;
	}
	public Float getRight_width_635_float() {
		if(right_width_635.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_635);
		}
	}
	public void setRight_width_635_float(Float rightWidth_635) {
		if(rightWidth_635==null){
			right_width_635 = "";
		}else{
			right_width_635 = rightWidth_635+"";
		}
	}//
	public String getRight_width_41() {
		return right_width_41;
	}
	public void setRight_width_41(String rightWidth_41) {
		right_width_41 = rightWidth_41;
	}
	public Float getRight_width_41_float() {
		if(right_width_41.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_41);
		}
	}
	public void setRight_width_41_float(Float rightWidth_41) {
		if(rightWidth_41==null){
			right_width_41 = ""; 
		}else{
			right_width_41 = rightWidth_41+"";
		}
	}//
	public String getRight_width_18() {
		return right_width_18;
	}
	public void setRight_width_18(String rightWidth_18) {
		right_width_18 = rightWidth_18;
	}
	public Float getRight_width_18_float() {
		if(right_width_18.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_18);
		}
	}
	public void setRight_width_18_float(Float rightWidth_18) {
		if(rightWidth_18==null){
			right_width_18 = "";
		}else{
			right_width_18 = rightWidth_18+"";
		}
	}//
	public String getRight_width_ratio() {
		return right_width_ratio;
	}
	public void setRight_width_ratio(String rightWidthRatio) {
		right_width_ratio = rightWidthRatio;
	}
	public Float getRight_width_ratio_float() {
		if(right_width_ratio.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_ratio);
		}
	}
	public void setRight_width_ratio_float(Float rightWidthRatio) {
		if(rightWidthRatio==null){
			right_width_ratio = "";
		}else{
			right_width_ratio = rightWidthRatio+"";
		}
	}//
	public String getRight_width_68() {
		return right_width_68;
	}
	public void setRight_width_68(String rightWidth_68) {
		right_width_68 = rightWidth_68;
	}
	public Float getRight_width_68_float() {
		if(right_width_68.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_width_68);
		}
	}
	public void setRight_width_68_float(Float rightWidth_68) {
		if(rightWidth_68==null){
			right_width_68 = "";
		}else{
			right_width_68 = rightWidth_68+"";
		}
	}//
	public String getRight_incline_angle() {
		return right_incline_angle;
	}
	public void setRight_incline_angle(String rightInclineAngle) {
		right_incline_angle = rightInclineAngle;
	}
	public Float getRight_incline_angle_float() {
		if(right_incline_angle.equals("")){
			return null;
		}else{
			try{
				return Float.parseFloat(right_incline_angle);
			}catch(Exception e){
				return 0.0f;
			}
		}
	}
	public void setRight_incline_angle_float(Float rightInclineAngle) {
		if(rightInclineAngle==null){
			right_incline_angle = "";
		}else{
			right_incline_angle = rightInclineAngle+"";
		}
	}//
	public String getRight_size_01() {
		return right_size_01;
	}
	public void setRight_size_01(String rightSize_01) {
		right_size_01 = rightSize_01;
	}
	public Float getRight_size_01_float() {
		if(right_size_01.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_01);
		}
	}
	public void setRight_size_01_float(Object rightSize_01) {
		if(rightSize_01==null){
			right_size_01 = "";
		}else{
			right_size_01 = rightSize_01+"";
		}
	}//
	public String getRight_size_02() {
		return right_size_02;
	}
	public void setRight_size_02(String rightSize_02) {
		right_size_02 = rightSize_02;
	}
	public Float getRight_size_02_float() {
		if(right_size_02.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_02);
		}
	}
	public void setRight_size_02_float(Object rightSize_02) {
		if(rightSize_02==null){
			right_size_02 = "";
		}else{
			right_size_02 = rightSize_02+"";
		}
	}//
	public String getRight_size_03() {
		return right_size_03;
	}
	public void setRight_size_03(String rightSize_03) {
		right_size_03 = rightSize_03;
	}
	public Float getRight_size_03_float() {
		if(right_size_03.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_03);
		}
	}
	public void setRight_size_03_float(Object rightSize_03) {
		if(rightSize_03==null){
			right_size_03 = "";
		}else{
			right_size_03 = rightSize_03+"";
		}
	}//
	public String getRight_size_04() {
		return right_size_04;
	}
	public void setRight_size_04(String rightSize_04) {
		right_size_04 = rightSize_04;
	}
	public Float getRight_size_04_float() {
		if(right_size_04.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_04);
		}
	}
	public void setRight_size_04_float(Object rightSize_04) {
		if(rightSize_04==null){
			right_size_04 = "";
		}else{
			right_size_04 = rightSize_04+"";
		}
	}//
	public String getRight_size_05() {
		return right_size_05;
	}
	public void setRight_size_05(String rightSize_05) {
		right_size_05 = rightSize_05;
	}
	public Float getRight_size_05_float() {
		if(right_size_05.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_05);
		}
	}
	public void setRight_size_05_float(Object rightSize_05) {
		if(rightSize_05==null){
			right_size_05 = "";
		}else{
			right_size_05 = rightSize_05+"";
		}
	}//
	public String getRight_size_06() {
		return right_size_06;
	}
	public void setRight_size_06(String rightSize_06) {
		right_size_06 = rightSize_06;
	}
	public Float getRight_size_06_float() {
		if(right_size_06.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_06);
		}
	}
	public void setRight_size_06_float(Object rightSize_06) {
		if(rightSize_06==null){
			right_size_06 = "";
		}else{
			right_size_06 = rightSize_06+"";
		}
	}//
	public String getRight_size_07() {
		return right_size_07;
	}
	public void setRight_size_07(String rightSize_07) {
		right_size_07 = rightSize_07;
	}
	public Float getRight_size_07_float() {
		if(right_size_07.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_07);
		}
	}
	public void setRight_size_07_float(Object rightSize_07) {
		if(rightSize_07==null){
			right_size_07 = "";
		}else{
			right_size_07 = rightSize_07+"";
		}
	}//
	public String getRight_size_08() {
		return right_size_08;
	}
	public void setRight_size_08(String rightSize_08) {
		right_size_08 = rightSize_08;
	}
	public Float getRight_size_08_float() {
		if(right_size_08.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_08);
		}
	}
	public void setRight_size_08_float(Object rightSize_08) {
		if(rightSize_08==null){
			right_size_08 = "";
		}else{
			right_size_08 = rightSize_08+"";
		}
	}//
	public String getRight_size_09() {
		return right_size_09;
	}
	public void setRight_size_09(String rightSize_09) {
		right_size_09 = rightSize_09;
	}
	public Float getRight_size_09_float() {
		if(right_size_09.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_09);
		}
	}
	public void setRight_size_09_float(Object rightSize_09) {
		if(rightSize_09==null){
			right_size_09 = "";
		}else{
			right_size_09 = rightSize_09+"";
		}
	}//
	public String getRight_size_10() {
		return right_size_10;
	}
	public void setRight_size_10(String rightSize_10) {
		right_size_10 = rightSize_10;
	}
	public Float getRight_size_10_float() {
		if(right_size_10.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_10);
		}
	}
	public void setRight_size_10_float(Object rightSize_10) {
		if(rightSize_10==null){
			right_size_10 = "";
		}else{
			right_size_10 = rightSize_10+"";
		}
	}//
	public String getRight_size_11() {
		return right_size_11;
	}
	public void setRight_size_11(String rightSize_11) {
		right_size_11 = rightSize_11;
	}
	public Float getRight_size_11_float() {
		if(right_size_11.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_11);
		}
	}
	public void setRight_size_11_float(Object rightSize_11) {
		if(rightSize_11==null){
			right_size_11 = "";
		}else{
			right_size_11 = rightSize_11+"";
		}
	}//
	public String getRight_size_12() {
		return right_size_12;
	}
	public void setRight_size_12(String rightSize_12) {
		right_size_12 = rightSize_12;
	}
	public Float getRight_size_12_float() {
		if(right_size_12.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_12);
		}
	}
	public void setRight_size_12_float(Object rightSize_12) {
		if(rightSize_12==null){
			right_size_12 = "";
		}else{
			right_size_12 = rightSize_12+"";
		}
	}//
	public String getRight_size_13() {
		return right_size_13;
	}
	public void setRight_size_13(String rightSize_13) {
		right_size_13 = rightSize_13;
	}
	public Float getRight_size_13_float() {
		if(right_size_13.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_size_13);
		}
	}
	public void setRight_size_13_float(Object rightSize_13) {
		if(rightSize_13==null){
			right_size_13 = "";
		}else{
			right_size_13 = rightSize_13+"";
		}
	}
	//专家意见
	public String getLeft_foot_type() {
		return left_foot_type;
	}
	public String getLeft_foot_size() {
		return left_foot_size;
	}
	public void setLeft_foot_size(String leftFootSize) {
		left_foot_size = leftFootSize;
	}
	public String getLeft_foot_width() {
		return left_foot_width;
	}
	public void setLeft_foot_width(String leftFootWidth) {
		left_foot_width = leftFootWidth;
	}
	public String getLeft_foot_width2() {
		return left_foot_width2;
	}
	public void setLeft_foot_width2(String leftFootWidth2) {
		left_foot_width2 = leftFootWidth2;
	}
	public String getLeft_foot_status() {
		return left_foot_status;
	}
	public void setLeft_foot_status(String leftFootStatus) {
		left_foot_status = leftFootStatus;
	}
	public String getLeft_advice() {
		return left_advice;
	}
	public void setLeft_advice(String leftAdvice) {
		left_advice = leftAdvice;
	}
	public String getRight_foot_size() {
		return right_foot_size;
	}
	public void setRight_foot_size(String rightFootSize) {
		right_foot_size = rightFootSize;
	}
	public String getRight_foot_width() {
		return right_foot_width;
	}
	public void setRight_foot_width(String rightFootWidth) {
		right_foot_width = rightFootWidth;
	}
	public String getRight_foot_width2() {
		return right_foot_width2;
	}
	public void setRight_foot_width2(String rightFootWidth2) {
		right_foot_width2 = rightFootWidth2;
	}
	public String getRight_foot_status() {
		return right_foot_status;
	}
	public void setRight_foot_status(String rightFootStatus) {
		right_foot_status = rightFootStatus;
	}
	public String getRight_advice() {
		return right_advice;
	}
	public void setRight_advice(String rightAdvice) {
		right_advice = rightAdvice;
	}
	public int getLeft_int_status() {
		return left_int_status;
	}
	public void setLeft_int_status(int leftIntStatus) {
		left_int_status = leftIntStatus;
	}
	public int getRight_int_status() {
		return right_int_status;
	}
	public void setRight_int_status(int rightIntStatus) {
		right_int_status = rightIntStatus;
	}//
	public String getLeft_para_01() {
		return left_para_01;
	}
	public void setLeft_para_01(String leftPara_01) {
		left_para_01 = leftPara_01;
	}
	public Float getLeft_para_01_float() {
		if(left_para_01.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_01);
		}
	}
	public void setLeft_para_01_float(Object leftPara_01) {
		if(leftPara_01==null){
			left_para_01 = "";
		}else{
			left_para_01 = leftPara_01+"";
		}
	}//
	public String getLeft_para_02() {
		return left_para_02;
	}
	public void setLeft_para_02(String leftPara_02) {
		left_para_02 = leftPara_02;
	}
	public Float getLeft_para_02_float() {
		if(left_para_02.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_02);
		}
	}
	public void setLeft_para_02_float(Object leftPara_02) {
		if(leftPara_02==null){
			left_para_02 = "";
		}else{
			left_para_02 = leftPara_02+"";
		}
	}//
	public String getLeft_para_03() {
		return left_para_03;
	}
	public void setLeft_para_03(String leftPara_03) {
		left_para_03 = leftPara_03;
	}
	public Float getLeft_para_03_float() {
		if(left_para_03.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_03);
		}
	}
	public void setLeft_para_03_float(Object leftPara_03) {
		if(leftPara_03==null){
			left_para_03 = "";
		}else{
			left_para_03 = leftPara_03+"";
		}
	}//
	public String getLeft_para_04() {
		return left_para_04;
	}
	public void setLeft_para_04(String leftPara_04) {
		left_para_04 = leftPara_04;
	}
	public Float getLeft_para_04_float() {
		if(left_para_04.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_04);
		}
	}
	public void setLeft_para_04_float(Object leftPara_04) {
		if(leftPara_04==null){
			left_para_04 = "";
		}else{
			left_para_04 = leftPara_04+"";
		}
	}//
	public String getLeft_para_05() {
		return left_para_05;
	}
	public void setLeft_para_05(String leftPara_05) {
		left_para_05 = leftPara_05;
	}
	public Float getLeft_para_05_float() {
		if(left_para_05.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_05);
		}
	}
	public void setLeft_para_05_float(Object leftPara_05) {
		if(leftPara_05==null){
			left_para_05 = "";
		}else{
			left_para_05 = leftPara_05+"";
		}
	}//
	public String getLeft_para_06() {
		return left_para_06;
	}
	public void setLeft_para_06(String leftPara_06) {
		left_para_06 = leftPara_06;
	}
	public Float getLeft_para_06_float() {
		if(left_para_06.equals("")){
			return null;
		}else{
			return Float.parseFloat(left_para_06);
		}
	}
	public void setLeft_para_06_float(Object leftPara_06) {
		if(leftPara_06==null){
			left_para_06 = "";
		}else{
			left_para_06 = leftPara_06+"";
		}
	}//
	public String getRight_para_01() {
		return right_para_01;
	}
	public void setRight_para_01(String rightPara_01) {
		right_para_01 = rightPara_01;
	}
	public Float getRight_para_01_float() {
		if(right_para_01.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_01);
		}
	}
	public void setRight_para_01_float(Object rightPara_01) {
		if(rightPara_01==null){
			right_para_01 = "";
		}else{
			right_para_01 = rightPara_01+"";
		}
	}//
	public String getRight_para_02() {
		return right_para_02;
	}
	public void setRight_para_02(String rightPara_02) {
		right_para_02 = rightPara_02;
	}
	public Float getRight_para_02_float() {
		if(right_para_02.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_02);
		}
	}
	public void setRight_para_02_float(Object rightPara_02) {
		if(rightPara_02==null){
			right_para_02 = "";
		}else{
			right_para_02 = rightPara_02+"";
		}
	}//
	public String getRight_para_03() {
		return right_para_03;
	}
	public void setRight_para_03(String rightPara_03) {
		right_para_03 = rightPara_03;
	}
	public Float getRight_para_03_float() {
		if(right_para_03.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_03);
		}
	}
	public void setRight_para_03_float(Object rightPara_03) {
		if(rightPara_03==null){
			right_para_03 = "";
		}else{
			right_para_03 = rightPara_03+"";
		}
	}//
	public String getRight_para_04() {
		return right_para_04;
	}
	public void setRight_para_04(String rightPara_04) {
		right_para_04 = rightPara_04;
	}
	public Float getRight_para_04_float() {
		if(right_para_04.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_04);
		}
	}
	public void setRight_para_04_float(Object rightPara_04) {
		if(rightPara_04==null){
			right_para_04 = "";
		}else{
			right_para_04 = rightPara_04+"";
		}
	}//
	public String getRight_para_05() {
		return right_para_05;
	}
	public void setRight_para_05(String rightPara_05) {
		right_para_05 = rightPara_05;
	}
	public Float getRight_para_05_float() {
		if(right_para_05.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_05);
		}
	}
	public void setRight_para_05_float(Object rightPara_05) {
		if(rightPara_05==null){
			right_para_05 = "";
		}else{
			right_para_05 = rightPara_05+"";
		}
	}//
	public String getRight_para_06() {
		return right_para_06;
	}
	public void setRight_para_06(String rightPara_06) {
		right_para_06 = rightPara_06;
	}
	public Float getRight_para_06_float() {
		if(right_para_06.equals("")){
			return null;
		}else{
			return Float.parseFloat(right_para_06);
		}
	}
	public void setRight_para_06_float(Object rightPara_06) {
		if(rightPara_06==null){
			right_para_06 = "";
		}else{
			right_para_06 = rightPara_06+"";
		}
	}//
	public void setLeft_foot_type(String leftFootType) {
		left_foot_type = leftFootType;
	}
	public String getRight_foot_type() {
		return right_foot_type;
	}
	public void setRight_foot_type(String rightFootType) {
		right_foot_type = rightFootType;
	}
	public String getFoot_leg() {
		return foot_leg;
	}
	public void setFoot_leg(String footLeg) {
		foot_leg = footLeg;
	}
	//图片路径
	public String getLeft_url() {
		return left_url;
	}
	public void setLeft_url(String leftUrl) {
		left_url = leftUrl;
	}
	public String getRight_url() {
		return right_url;
	}
	public void setRight_url(String rightUrl) {
		right_url = rightUrl;
	}
	public String getLeft_urla() {
		return left_urla;
	}
	public void setLeft_urla(String leftUrla) {
		left_urla = leftUrla;
	}
	public String getRight_urla() {
		return right_urla;
	}
	public void setRight_urla(String rightUrla) {
		right_urla = rightUrla;
	}
	public int getLeft_dpi() {
		return left_dpi;
	}
	public void setLeft_dpi(int leftDpi) {
		left_dpi = leftDpi;
	}
	public int getRight_dpi() {
		return right_dpi;
	}
	public void setRight_dpi(int rightDpi) {
		right_dpi = rightDpi;
	}
	public String getLeft_url_ab() {
		return left_url_ab;
	}
	public void setLeft_url_ab(String leftUrlAb) {
		left_url_ab = leftUrlAb;
	}
	public String getRight_url_ab() {
		return right_url_ab;
	}
	public void setRight_url_ab(String rightUrlAb) {
		right_url_ab = rightUrlAb;
	}
	//其他坐标辅助参考(左脚)
	public float getLcircle_01_x() {
		return lcircle_01_x;
	}
	public void setLcircle_01_x(float lcircle_01X) {
		lcircle_01_x = lcircle_01X;
	}
	public float getLcircle_01_y() {
		return lcircle_01_y;
	}
	public void setLcircle_01_y(float lcircle_01Y) {
		lcircle_01_y = lcircle_01Y;
	}
	public float getLcircle_02_x() {
		return lcircle_02_x;
	}
	public void setLcircle_02_x(float lcircle_02X) {
		lcircle_02_x = lcircle_02X;
	}
	public float getLcircle_02_y() {
		return lcircle_02_y;
	}
	public void setLcircle_02_y(float lcircle_02Y) {
		lcircle_02_y = lcircle_02Y;
	}
	public float getLcircle_03_x() {
		return lcircle_03_x;
	}
	public void setLcircle_03_x(float lcircle_03X) {
		lcircle_03_x = lcircle_03X;
	}
	public float getLcircle_03_y() {
		return lcircle_03_y;
	}
	public void setLcircle_03_y(float lcircle_03Y) {
		lcircle_03_y = lcircle_03Y;
	}
	public float getLcircle_725_x() {
		return lcircle_725_x;
	}
	public void setLcircle_725_x(float lcircle_725X) {
		lcircle_725_x = lcircle_725X;
	}
	public float getLcircle_725_y() {
		return lcircle_725_y;
	}
	public void setLcircle_725_y(float lcircle_725Y) {
		lcircle_725_y = lcircle_725Y;
	}
	public float getLcircle_635_x() {
		return lcircle_635_x;
	}
	public void setLcircle_635_x(float lcircle_635X) {
		lcircle_635_x = lcircle_635X;
	}
	public float getLcircle_635_y() {
		return lcircle_635_y;
	}
	public void setLcircle_635_y(float lcircle_635Y) {
		lcircle_635_y = lcircle_635Y;
	}
	public float getLcircle_4101_x() {
		return lcircle_4101_x;
	}
	public void setLcircle_4101_x(float lcircle_4101X) {
		lcircle_4101_x = lcircle_4101X;
	}
	public float getLcircle_4101_y() {
		return lcircle_4101_y;
	}
	public void setLcircle_4101_y(float lcircle_4101Y) {
		lcircle_4101_y = lcircle_4101Y;
	}
	public float getLcircle_4102_x() {
		return lcircle_4102_x;
	}
	public void setLcircle_4102_x(float lcircle_4102X) {
		lcircle_4102_x = lcircle_4102X;
	}
	public float getLcircle_4102_y() {
		return lcircle_4102_y;
	}
	public void setLcircle_4102_y(float lcircle_4102Y) {
		lcircle_4102_y = lcircle_4102Y;
	}
	public float getLbreak_01_x() {
		return lbreak_01_x;
	}
	public void setLbreak_01_x(float lbreak_01X) {
		lbreak_01_x = lbreak_01X;
	}
	public float getLbreak_01_y() {
		return lbreak_01_y;
	}
	public void setLbreak_01_y(float lbreak_01Y) {
		lbreak_01_y = lbreak_01Y;
	}
	public float getLbreak_02_x() {
		return lbreak_02_x;
	}
	public void setLbreak_02_x(float lbreak_02X) {
		lbreak_02_x = lbreak_02X;
	}
	public float getLbreak_02_y() {
		return lbreak_02_y;
	}
	public void setLbreak_02_y(float lbreak_02Y) {
		lbreak_02_y = lbreak_02Y;
	}
	public int getLfoot_top() {
		return lfoot_top;
	}
	public void setLfoot_top(int lfootTop) {
		lfoot_top = lfootTop;
	}
	public int getLfoot_bottom() {
		return lfoot_bottom;
	}
	public void setLfoot_bottom(int lfootBottom) {
		lfoot_bottom = lfootBottom;
	}
	public int getLfoot_right() {
		return lfoot_right;
	}
	public void setLfoot_right(int lfootRight) {
		lfoot_right = lfootRight;
	}
	public int getLfoot_left() {
		return lfoot_left;
	}
	public void setLfoot_left(int lfootLeft) {
		lfoot_left = lfootLeft;
	}
	public double getLscale() {
		return lscale;
	}
	public void setLscale(double lscale) {
		this.lscale = lscale;
	}
	public float getLcircle_18_x() {
		return lcircle_18_x;
	}
	public void setLcircle_18_x(float lcircle_18X) {
		lcircle_18_x = lcircle_18X;
	}
	public float getLcircle_18_y() {
		return lcircle_18_y;
	}
	public void setLcircle_18_y(float lcircle_18Y) {
		lcircle_18_y = lcircle_18Y;
	}
	public float getLcircle_1801_x() {
		return lcircle_1801_x;
	}
	public void setLcircle_1801_x(float lcircle_1801X) {
		lcircle_1801_x = lcircle_1801X;
	}
	public float getLcircle_1801_y() {
		return lcircle_1801_y;
	}
	public void setLcircle_1801_y(float lcircle_1801Y) {
		lcircle_1801_y = lcircle_1801Y;
	}
	public float getLcircle_1802_x() {
		return lcircle_1802_x;
	}
	public void setLcircle_1802_x(float lcircle_1802X) {
		lcircle_1802_x = lcircle_1802X;
	}
	public float getLcircle_1802_y() {
		return lcircle_1802_y;
	}
	public void setLcircle_1802_y(float lcircle_1802Y) {
		lcircle_1802_y = lcircle_1802Y;
	}
	public float getLcircle_90_x() {
		return lcircle_90_x;
	}
	public void setLcircle_90_x(float lcircle_90X) {
		lcircle_90_x = lcircle_90X;
	}
	public float getLcircle_90_y() {
		return lcircle_90_y;
	}
	public void setLcircle_90_y(float lcircle_90Y) {
		lcircle_90_y = lcircle_90Y;
	}
	public float getLcircle_825_x() {
		return lcircle_825_x;
	}
	public void setLcircle_825_x(float lcircle_825X) {
		lcircle_825_x = lcircle_825X;
	}
	public float getLcircle_825_y() {
		return lcircle_825_y;
	}
	public void setLcircle_825_y(float lcircle_825Y) {
		lcircle_825_y = lcircle_825Y;
	}
	public float getLcircle_78_x() {
		return lcircle_78_x;
	}
	public void setLcircle_78_x(float lcircle_78X) {
		lcircle_78_x = lcircle_78X;
	}
	public float getLcircle_78_y() {
		return lcircle_78_y;
	}
	public void setLcircle_78_y(float lcircle_78Y) {
		lcircle_78_y = lcircle_78Y;
	}
	public float getLcircle_80_x() {
		return lcircle_80_x;
	}
	public void setLcircle_80_x(float lcircle_80X) {
		lcircle_80_x = lcircle_80X;
	}
	public float getLcircle_80_y() {
		return lcircle_80_y;
	}
	public void setLcircle_80_y(float lcircle_80Y) {
		lcircle_80_y = lcircle_80Y;
	}
	public float getLcircle_65_x() {
		return lcircle_65_x;
	}
	public void setLcircle_65_x(float lcircle_65X) {
		lcircle_65_x = lcircle_65X;
	}
	public float getLcircle_65_y() {
		return lcircle_65_y;
	}
	public void setLcircle_65_y(float lcircle_65Y) {
		lcircle_65_y = lcircle_65Y;
	}
	//其他坐标辅助参考(右脚)
	public float getRcircle_01_x() {
		return rcircle_01_x;
	}
	public void setRcircle_01_x(float rcircle_01X) {
		rcircle_01_x = rcircle_01X;
	}
	public float getRcircle_01_y() {
		return rcircle_01_y;
	}
	public void setRcircle_01_y(float rcircle_01Y) {
		rcircle_01_y = rcircle_01Y;
	}
	public float getRcircle_02_x() {
		return rcircle_02_x;
	}
	public void setRcircle_02_x(float rcircle_02X) {
		rcircle_02_x = rcircle_02X;
	}
	public float getRcircle_02_y() {
		return rcircle_02_y;
	}
	public void setRcircle_02_y(float rcircle_02Y) {
		rcircle_02_y = rcircle_02Y;
	}
	public float getRcircle_03_x() {
		return rcircle_03_x;
	}
	public void setRcircle_03_x(float rcircle_03X) {
		rcircle_03_x = rcircle_03X;
	}
	public float getRcircle_03_y() {
		return rcircle_03_y;
	}
	public void setRcircle_03_y(float rcircle_03Y) {
		rcircle_03_y = rcircle_03Y;
	}
	public float getRcircle_725_x() {
		return rcircle_725_x;
	}
	public void setRcircle_725_x(float rcircle_725X) {
		rcircle_725_x = rcircle_725X;
	}
	public float getRcircle_725_y() {
		return rcircle_725_y;
	}
	public void setRcircle_725_y(float rcircle_725Y) {
		rcircle_725_y = rcircle_725Y;
	}
	public float getRcircle_635_x() {
		return rcircle_635_x;
	}
	public void setRcircle_635_x(float rcircle_635X) {
		rcircle_635_x = rcircle_635X;
	}
	public float getRcircle_635_y() {
		return rcircle_635_y;
	}
	public void setRcircle_635_y(float rcircle_635Y) {
		rcircle_635_y = rcircle_635Y;
	}
	public float getRcircle_4101_x() {
		return rcircle_4101_x;
	}
	public void setRcircle_4101_x(float rcircle_4101X) {
		rcircle_4101_x = rcircle_4101X;
	}
	public float getRcircle_4101_y() {
		return rcircle_4101_y;
	}
	public void setRcircle_4101_y(float rcircle_4101Y) {
		rcircle_4101_y = rcircle_4101Y;
	}
	public float getRcircle_4102_x() {
		return rcircle_4102_x;
	}
	public void setRcircle_4102_x(float rcircle_4102X) {
		rcircle_4102_x = rcircle_4102X;
	}
	public float getRcircle_4102_y() {
		return rcircle_4102_y;
	}
	public void setRcircle_4102_y(float rcircle_4102Y) {
		rcircle_4102_y = rcircle_4102Y;
	}
	public float getRbreak_01_x() {
		return rbreak_01_x;
	}
	public void setRbreak_01_x(float rbreak_01X) {
		rbreak_01_x = rbreak_01X;
	}
	public float getRbreak_01_y() {
		return rbreak_01_y;
	}
	public void setRbreak_01_y(float rbreak_01Y) {
		rbreak_01_y = rbreak_01Y;
	}
	public float getRbreak_02_x() {
		return rbreak_02_x;
	}
	public void setRbreak_02_x(float rbreak_02X) {
		rbreak_02_x = rbreak_02X;
	}
	public float getRbreak_02_y() {
		return rbreak_02_y;
	}
	public void setRbreak_02_y(float rbreak_02Y) {
		rbreak_02_y = rbreak_02Y;
	}
	public int getRfoot_top() {
		return rfoot_top;
	}
	public void setRfoot_top(int rfootTop) {
		rfoot_top = rfootTop;
	}
	public int getRfoot_bottom() {
		return rfoot_bottom;
	}
	public void setRfoot_bottom(int rfootBottom) {
		rfoot_bottom = rfootBottom;
	}
	public int getRfoot_right() {
		return rfoot_right;
	}
	public void setRfoot_right(int rfootRight) {
		rfoot_right = rfootRight;
	}
	public int getRfoot_left() {
		return rfoot_left;
	}
	public void setRfoot_left(int rfootLeft) {
		rfoot_left = rfootLeft;
	}
	public double getRscale() {
		return rscale;
	}
	public void setRscale(double rscale) {
		this.rscale = rscale;
	}
	public float getRcircle_18_x() {
		return rcircle_18_x;
	}
	public void setRcircle_18_x(float rcircle_18X) {
		rcircle_18_x = rcircle_18X;
	}
	public float getRcircle_18_y() {
		return rcircle_18_y;
	}
	public void setRcircle_18_y(float rcircle_18Y) {
		rcircle_18_y = rcircle_18Y;
	}
	public float getRcircle_1801_x() {
		return rcircle_1801_x;
	}
	public void setRcircle_1801_x(float rcircle_1801X) {
		rcircle_1801_x = rcircle_1801X;
	}
	public float getRcircle_1801_y() {
		return rcircle_1801_y;
	}
	public void setRcircle_1801_y(float rcircle_1801Y) {
		rcircle_1801_y = rcircle_1801Y;
	}
	public float getRcircle_1802_x() {
		return rcircle_1802_x;
	}
	public void setRcircle_1802_x(float rcircle_1802X) {
		rcircle_1802_x = rcircle_1802X;
	}
	public float getRcircle_1802_y() {
		return rcircle_1802_y;
	}
	public void setRcircle_1802_y(float rcircle_1802Y) {
		rcircle_1802_y = rcircle_1802Y;
	}
	public float getRcircle_90_x() {
		return rcircle_90_x;
	}
	public void setRcircle_90_x(float rcircle_90X) {
		rcircle_90_x = rcircle_90X;
	}
	public float getRcircle_90_y() {
		return rcircle_90_y;
	}
	public void setRcircle_90_y(float rcircle_90Y) {
		rcircle_90_y = rcircle_90Y;
	}
	public float getRcircle_825_x() {
		return rcircle_825_x;
	}
	public void setRcircle_825_x(float rcircle_825X) {
		rcircle_825_x = rcircle_825X;
	}
	public float getRcircle_825_y() {
		return rcircle_825_y;
	}
	public void setRcircle_825_y(float rcircle_825Y) {
		rcircle_825_y = rcircle_825Y;
	}
	public float getRcircle_78_x() {
		return rcircle_78_x;
	}
	public void setRcircle_78_x(float rcircle_78X) {
		rcircle_78_x = rcircle_78X;
	}
	public float getRcircle_78_y() {
		return rcircle_78_y;
	}
	public void setRcircle_78_y(float rcircle_78Y) {
		rcircle_78_y = rcircle_78Y;
	}
	public float getRcircle_80_x() {
		return rcircle_80_x;
	}
	public void setRcircle_80_x(float rcircle_80X) {
		rcircle_80_x = rcircle_80X;
	}
	public float getRcircle_80_y() {
		return rcircle_80_y;
	}
	public void setRcircle_80_y(float rcircle_80Y) {
		rcircle_80_y = rcircle_80Y;
	}
	public float getRcircle_65_x() {
		return rcircle_65_x;
	}
	public void setRcircle_65_x(float rcircle_65X) {
		rcircle_65_x = rcircle_65X;
	}
	public float getRcircle_65_y() {
		return rcircle_65_y;
	}
	public void setRcircle_65_y(float rcircle_65Y) {
		rcircle_65_y = rcircle_65Y;
	}
	//左右脚的水平和垂直的翻转状态
	public int getLh_turn() {
		return lh_turn;
	}
	public void setLh_turn(int lhTurn) {
		lh_turn = lhTurn;
	}
	public int getLv_turn() {
		return lv_turn;
	}
	public void setLv_turn(int lvTurn) {
		lv_turn = lvTurn;
	}
	public int getRh_turn() {
		return rh_turn;
	}
	public void setRh_turn(int rhTurn) {
		rh_turn = rhTurn;
	}
	public int getRv_turn() {
		return rv_turn;
	}
	public void setRv_turn(int rvTurn) {
		rv_turn = rvTurn;
	}
//end...FootStudyInfo.java
}