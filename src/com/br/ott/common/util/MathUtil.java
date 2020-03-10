package com.br.ott.common.util;

import java.math.BigDecimal;

/**
 * 数字处理类 文件名：MathUtil.java 版权：BoRuiCubeNet. Copyright 2012-2012,All rights
 * reserved 公司名称：青岛博瑞立方科技有限公司 创建人：陈登民 创建时间：2016年3月11日 - 下午2:36:51
 */
public final class MathUtil {

	/**
	 * 保留两位小数
	 */
	public static double getDecimal2(double number) {
		BigDecimal b = new BigDecimal(number);
		return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
