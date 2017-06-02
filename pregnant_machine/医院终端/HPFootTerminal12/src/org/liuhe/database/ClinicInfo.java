package org.liuhe.database;

import java.util.List;

public class ClinicInfo {
	private List<ClinicInfo_item> clinic;
	private int page_num = 1;
	private int page_size = 16;
	private int page_total;
	public List<ClinicInfo_item> getClinic() {
		return clinic;
	}
	public void setClinic(List<ClinicInfo_item> clinic) {
		this.clinic = clinic;
		page_total = clinic.size()/page_size+(clinic.size()%page_size>0?1:0);
	}
	public int getPage_num() {
		return page_num;
	}
	public void setPage_num(int pageNum) {
		page_num = pageNum;
	}
	public int getPage_size() {
		return page_size;
	}
	public void setPage_size(int pageSize) {
		page_size = pageSize;
	}
	public int getPage_total() {
		return page_total;
	}
	
	// �Ƿ�Ϊ��ҳ&&��ҳ
	public boolean isFirst(){
		if(page_num == 1){
			return true;
		}else{
			return false;
		}
	}
	public void first(){
		page_num = 1;
	}
	// �Ƿ�����һҳ&&��һҳ
	public boolean havePrev(){
		if(page_num > 1){
			return true;
		}else{
			return false;
		}
	}
	public void prev(){
		page_num --;
	}
	// �Ƿ�����һҳ&&��һҳ
	public boolean haveNext(){
		if(page_num < page_total){
			return true;
		}else{
			return false;
		}
	}
	public void next(){
		page_num ++;
	}
	// �Ƿ�Ϊĩҳ&&ĩҳ
	public boolean isLast(){
		if(page_num == page_total){
			return true;
		}else{
			return false;
		}
	}
	public void last(){
		page_num = page_total;
	}
	// ��ʼ��ţ������ñ�ţ�
	public int startIndex(){
		return page_size*(page_num-1);
	}
	// ������ţ��������ñ�ţ�
	public int endIndex(){
		int endIndex = page_size*page_num;
		if(endIndex > clinic.size()){
			endIndex = clinic.size();
		}
		return endIndex;
	}
}