package org.liuhe.database;

public class ClinicInfo_item {
	private String clinic_dept;
	private String doctor_name;
	private String clinic_type;
	private String wait;
	private String count_num;
	// modify by kael
	private int id;
	public int getid() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}
	// modify by kael over
	public String getClinic_dept() {
		return clinic_dept;
	}
	public void setClinic_dept(String clinicDept) {
		clinic_dept = clinicDept;
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
	public String getWait() {
		return wait;
	}
	public void setWait(String wait) {
		this.wait = wait;
	}
	public String getCount_num() {
		return count_num;
	}
	public void setCount_num(String countNum) {
		count_num = countNum;
	}
	
}
