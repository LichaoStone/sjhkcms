package com.br.ott.client.common.entity;

import com.br.ott.common.util.StringUtil;


public class OperaLog {
	private String id;
	/**
	 * 1新增、２修改、３删除、4批量导入
	 */
	private String operType;

	/**
	 * 操作详情
	 */
	private String content;
	/**
	 * 耗时
	 */
	private int cost;
	/**
	 * 所有ＩＰ
	 */
	private String createIp;
	/**
	 * 操作者
	 */
	private String createUser;
	/**
	 * 操作时间(默认系统当前时间)
	 */
	private String createDate;
	/**
	 * 业务名称
	 */
	private String businame;
	
	// 临时变量(结束时间)
	private String endTime;
	// 临时变量
	private String tmp;
	
	private String orderColumn;
	
	
	

	public String getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getTmp() {
		return tmp;
	}

	public void setTmp(String tmp) {
		this.tmp = tmp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getBusiname() {
		return businame;
	}

	public void setBusiname(String businame) {
		this.businame = businame;
	}
	
	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOperaTypeStr(){
		if(!StringUtil.isEmpty(operType)){
			if("1".equals(operType)){
				return "新增";
			} else if("2".equals(operType)){
				return "修改";
			} else if("3".equals(operType)){
				return "删除";
			} else if("4".equals(operType)){
				return "批量导入";
			}
		}
		return null;
	}
	
	public String getSecondStr(){
		if(cost>0){
			String tmp = String.valueOf(cost);
			if(tmp.length() > 3){
				String hou3 = tmp.substring(tmp.length()-3,tmp.length());
				tmp = tmp.substring(0, tmp.length()-3) + "."+ hou3;
			} else if(tmp.length()==3){
				tmp = "0."+cost;
			} else if(tmp.length()==2){
				tmp = "0.0"+cost;
			} else if(tmp.length()==1){
				tmp = "0.00"+cost;
			}
			return tmp;
		}
		return null;
	}
	
	public static void main(String[] args) {
		OperaLog log = new OperaLog();
		log.setCost(12);
		String str = log.getSecondStr();
		System.out.println(str);
	}

}
