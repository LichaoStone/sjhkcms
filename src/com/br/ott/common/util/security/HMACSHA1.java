package com.br.ott.common.util.security;

import com.br.ott.common.util.StringUtil;

/* 
 *  
 *  文件名：HMACSHA1.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2013-12-16 - 下午4:26:39
 */
public class HMACSHA1 {

	/**
	 * 取得数字签名
	 * 
	 * 1.取出签名的前10个字节 2.调用hex2asc接口将10个BYTE转换成可显示的ASCII码，共20个字节 3.签名后面无需分号，直接进行换行
	 * 创建人：pananz 创建时间：2013-12-16 - 下午4:32:49
	 * 
	 * @param data
	 *            要加密的数据
	 * @param key
	 *            密钥
	 * @return
	 * @throws Exception
	 *             返回类型：byte[]
	 * @exception throws
	 */
	public static byte[] getHmacSHA1(String data, String key) throws Exception {
		byte[] ipadArray = new byte[64];
		byte[] opadArray = new byte[64];
		byte[] keyArray = new byte[64];
		int ex = key.length();
		SHA1 sha1 = new SHA1();
		if (key.length() > 64) {
			byte[] temp = sha1.getDigestOfBytes(hexToByteArr(key));
			ex = temp.length;
			for (int i = 0; i < ex; i++) {
				keyArray[i] = temp[i];
			}
		} else {
			byte[] temp = hexToByteArr(key);
			for (int i = 0; i < temp.length; i++) {
				keyArray[i] = temp[i];
			}
		}
		for (int i = ex; i < 64; i++) {
			keyArray[i] = 0;
		}
		for (int j = 0; j < 64; j++) {
			ipadArray[j] = (byte) (keyArray[j] ^ 0x36);
			opadArray[j] = (byte) (keyArray[j] ^ 0x5C);
		}
		byte[] tempResult = sha1.getDigestOfBytes(join(ipadArray,
				data.getBytes()));

		return sha1.getDigestOfBytes(join(opadArray, tempResult));
	}

	private static byte[] join(byte[] b1, byte[] b2) {
		int length = b1.length + b2.length;
		byte[] newer = new byte[length];
		for (int i = 0; i < b1.length; i++) {
			newer[i] = b1[i];
		}
		for (int i = 0; i < b2.length; i++) {
			newer[i + b1.length] = b2[i];
		}
		return newer;
	}

	public static byte[] hexToByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public static String getSignature(String deviceStr, String orderKey)
			throws Exception {
		String snMac = SHA1.byteArrayToHexString(getHmacSHA1(deviceStr,
				orderKey));
		if (StringUtil.isNotEmpty(snMac) && snMac.length() >= 20) {
			snMac = snMac.substring(0, 20);
		}
		return snMac;
	}

	protected static int byteArrayToInt(byte[] bytedata, int i) {
		return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16)
				| ((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff);
	}

	protected static Integer hexToInt(String str, int start, int end) {
		String t = str.substring(start, end);

		char[] ch = t.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < end - start; i += 2) {
			sb.append(Integer.parseInt(
					String.valueOf(ch[i]) + String.valueOf(ch[i + 1]), 16));
		}

		return Integer.parseInt(sb.toString());
	}

	public static void main(String[] args) {
		try {
			String a = HMACSHA1
					.getSignature(
							"全是156023700111312110000000212:23:45:67:23:442013120414295011",
							"3F516EEEFE7CCA4F13D6E6D4134C6771");
			System.out.println(a);
			String b = HMACSHA1.getSignature(
					"15601370011131111999981822013121512005911",
					"D2A5D809BE66FE9840D2E6DBAB87E986");
			System.out.println(b);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
