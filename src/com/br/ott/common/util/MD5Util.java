package com.br.ott.common.util;

import java.io.FileInputStream;
import java.io.IOException;

import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.log4j.Logger;

/**
 * MD5加密工具类
 * 
 * @author pKF46373
 * @version [版本号, Apr 20, 2011]
 * @see
 * @since 1.0
 */
public final class MD5Util {
	/**
	 * 实例化一个记录日志
	 */
	private static Logger logger = Logger.getLogger(MD5Util.class);

	private MD5Util() {

	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	/**
	 * 对输入的字符串进行加密处理
	 * 
	 * @param input 要加密的字符串
	 * @return [参数说明] 加密后的字符串
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(input.getBytes()));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	/**
	 * 对输入的字符串按编码要求进行加密处理
	 * 
	 * @param input 要加密的字符串
	 * @param code 加密编码（utf-8,gbk...）
	 * @return [参数说明] 加密后的字符串
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String getMD5(String input, String code) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(input.getBytes(code)));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	public static String getMD5File(String filename) {
		InputStream fis = null;
		byte[] buffer = new byte[Constants.DEFAULT_BYTE];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}

			return toHexString(md5.digest());
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & Constants.OX) >>> Constants.NUM4]);
			sb.append(HEX_DIGITS[b[i] & Constants.OXOF]);
		}
		return sb.toString();
	}

	protected static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	protected static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = Constants.NUM256 + n;
		int d1 = n / Constants.NUM16;
		int d2 = n % Constants.NUM16;
		return Constants.HEXDIGITS[d1] + Constants.HEXDIGITS[d2];
	}

	/**
	 * 对字符串进行签名的操作
	 * 用MD5对字符串进行签名
	 * @param s
	 * @return s
	 * @return String 返回字符串类型
	 * @exception throws 抛出Exception异常
	 * @see MD5,getMD5(String s)
	 */
	public static String getMD51(String s) {

		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);

			byte[] md = mdTemp.digest();
			int j = md.length;

			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = Constants.HEXDIG[byte0 >>> Constants.NUM4 & Constants.OX];
				str[k++] = Constants.HEXDIG[byte0 & Constants.OX];
			}
			return new String(str);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return s;
		}
	}

	public static void main(String[] args) {
		System.out.println("admin");
		System.out.println(getMD5("admin"));
	}

}
