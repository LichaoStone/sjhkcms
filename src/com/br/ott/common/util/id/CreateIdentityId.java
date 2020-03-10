package com.br.ott.common.util.id;

import java.text.DecimalFormat;

import com.br.ott.common.util.StringUtil;

/**
 * 假设在1毫秒内的并发数不超过10000次
 * 产生一个String型的多并发的唯一流水号
 * 
 * @author chenzongqi
 *
 */
public class CreateIdentityId {
	private static CreateIdentityId creator = new CreateIdentityId();

	private DecimalFormat df = new DecimalFormat("0000");

	private int num = 0;

	/**
	 * 获取单例的引用
	 * 
	 * @return CreateIdentityId
	 */
	public static CreateIdentityId getInstance() {
		return creator;
	}

	/**
	 * 产生一个14位的唯一流水号
	 * 2位系统编码，8位的毫秒数字，4位的同步递增的序列数
	 * 
	 * @return String型的流水号
	 */
	public String createId() {
		String id = getNum();
		String ret = SystemParam.getCurrentSysId()
				+ StringUtil.substring(String.valueOf(System.currentTimeMillis()), 5, 13) + id;
		return ret;
	}

	/**
	 * 产生一个22位的唯一流水号
	 * 前面3位操作的编码，第5、6位为系统编码，13位的毫秒数字，5位的同步递增的序列数
	 * 
	 * @param opCode
	 * @return
	 */
	public String createId(String opCode) {
		return org.apache.commons.lang.StringUtils.trim(opCode) + createId();
	}

	/**
	 * 产生一个11位的唯一流水号
	 * 2位系统编码，5位的毫秒数字，4位的同步递增的序列数
	 * 
	 * @return String型的流水号
	 */
	public String createId2() {
		String id = getNum();
		String ret = StringUtil.substring(String.valueOf(System.currentTimeMillis()), 8, 13) + id;
		return ret;
	}
	public String createId2(String opCode) {
		return org.apache.commons.lang.StringUtils.trim(opCode) + createId();
	}
	
	private synchronized String getNum() {
		String numStr = "";
		if (num == 10000) {
			num = 0;
		}
		numStr = df.format(num++);

		return numStr;
	}
}
