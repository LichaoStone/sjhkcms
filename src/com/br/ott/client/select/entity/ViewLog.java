package com.br.ott.client.select.entity;

import com.br.ott.common.util.Pagination;

public class ViewLog extends Pagination {
	private Integer id;//id
	private Integer dramaId;//资产id
	private String phone;//用户电话号码（用户唯一标识）
	private String dramaChannel;//保存资产频道（0收藏1最近收看）
	private String dramaType;//保存资产类型（0点播1直播）
	private String savaTime;//开始时间(用字符串处理比较简单)
	private String sxTime;//失效时间（默认开始时间7天后观看记录失效）(用字符串处理比较简单)
	private String status;//1:有效，0：失效
	
	/**********用于页面查询****************/
	private String orderColumn;
	private String minDate;
	private String maxDate;
	
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getMinDate() {
		return minDate;
	}
	public void setMinDate(String minDate) {
		this.minDate = minDate;
	}
	public String getMaxDate() {
		return maxDate;
	}
	public void setMaxDate(String maxDate) {
		this.maxDate = maxDate;
	}
	
	public Integer getid(){
		return this.id;
	}
	public void setid(Integer mid){
		this.id = mid;
	}
	public Integer getdramaId(){
		return this.dramaId;
	}
	public void setdramaId(Integer mdramaId){
		this.dramaId = mdramaId;
	}
	public String getphone(){
		return this.phone;
	}
	public void setphone(String mphone){
		this.phone = mphone;
	}
	public String getdramaChannel(){
		return this.dramaChannel;
	}
	public void setdramaChannel(String mdramaChannel){
		this.dramaChannel = mdramaChannel;
	}
	public String getdramaType(){
		return this.dramaType;
	}
	public void setdramaType(String mdramaType){
		this.dramaType = mdramaType;
	}
	public String getsavaTime(){
		return this.savaTime;
	}
	public void setsavaTime(String msavaTime){
		this.savaTime = msavaTime;
	}
	public String getsxTime(){
		return this.sxTime;
	}
	public void setsxTime(String msxTime){
		this.sxTime = msxTime;
	}
	public String getstatus(){
		return this.status;
	}
	public void setstatus(String mstatus){
		this.status = mstatus;
	}
	
}