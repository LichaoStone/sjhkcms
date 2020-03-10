package com.br.ott.common.util.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 
 * @author 陈登民
 * 
 */
public class AES2 {
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";// "算法/模式/补码方式"
	private static final String CHARSET = "utf-8";
	private static final String CIPHER = "AES";

	// 加密
	public static String encrypt(String sSrc, String sKey) throws Exception {
		if (sKey == null) {
			throw new Exception("Key为空null");
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			throw new Exception("Key长度不是16位");
		}
		byte[] raw = sKey.getBytes(CHARSET);
		SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER);
		Cipher cipher = Cipher.getInstance(TRANSFORMATION);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

		return parseByte2HexStr(encrypted);
	}

	// 解密
	public static String decrypt(String con, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				return null;
			}
			byte[] raw = sKey.getBytes(CHARSET);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, CIPHER);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = parseHexStr2Byte(con);
			byte[] original = cipher.doFinal(encrypted1);
			return new String(original, CHARSET);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * 此处使用AES-128-ECB加密模式，key需要为16位。
		 */
		String cKey = "OTT1234567890OTT";
		// 需要加密的字串
		String cSrc = "1688000120130608105705001A95D18853";
		System.out.println("加密前：" + cSrc);
		// 加密
		String enString = encrypt(cSrc, cKey);
		System.out.println("加密后的字串是：" + enString);

		// 解密
		String DeString = decrypt("7A29E275063653891AE3D1A0B061B97AA4474FADFE35A4DEECF0AC9E65EB775C66A1C493613B1807499E8F9165698E86189A6F82EEBA99994CADA1A2EAE94BD01CAF1222AB9CFD1CF4EF0C36580306F67CFCA7D52443BE82AB8FC57CB16B23EE", "D2A5D809BE66FE98");
		System.out
				.println("解密后的字串是："
						+ DeString
						+ "sssssss==>"
						+ "7A29E275063653891AE3D1A0B061B97AA4474FADFE35A4DEECF0AC9E65EB775C66A1C493613B1807499E8F9165698E86189A6F82EEBA99994CADA1A2EAE94BD01CAF1222AB9CFD1CF4EF0C36580306F67CFCA7D52443BE82AB8FC57CB16B23EE"
								.length());
	}
}