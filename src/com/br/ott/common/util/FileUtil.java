/*
 * 文 件 名:  FileUtil.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cKF46827
 * 修改时间:  2011-11-14
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */

package com.br.ott.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.DigestUtils;

/**
 * 文件操作相关
 * 
 * @author cKF46827
 * @version [1.0, 2011-11-14]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class FileUtil {

	/**
	 * <默认构造函数>
	 */
	public FileUtil() {
	}

	/**
	 * 取得文件的大小 <功能详细描述>
	 * 
	 * @param mFile
	 * @return
	 * @throws IOException
	 *             [参数说明]
	 * @return long [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static long getFileSize(File mFile) throws IOException {
		FileInputStream file = null;
		long available = -1;
		try {
			file = new FileInputStream(mFile);
			available = file.available();
			file.close();
		} catch (FileNotFoundException e) {
			
		} finally {
			try {
				if (null != file) {
					file.close();
				}
			} catch (Exception e) {
			}
		}
		return available;
	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 *            [路径]
	 * @return [参数说明]
	 * @return boolean [是否成功？]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean createCatalog(String path) {
		File file = new File(path);
		return file.mkdir();
	}

	/**
	 * <输入文件名读出为字符串>
	 * 
	 * @param fileName
	 * @return String [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static String readFileToString(String fileName) {
		try {
			InputStream is = new FileInputStream(fileName);
			String xml = streamToString(is);
			try {
				is.close();
			} catch (IOException e) {
			}
			return xml;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 流转换成字符串
	 * 
	 * @param input
	 *            [输入流]
	 * @return [参数说明]
	 * @return String [字符串]
	 */
	public static String streamToString(InputStream input) {

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input,
					"UTF-8"));
			String temp = br.readLine();
			while (null != temp && !"".equals(temp)) {
				sb.append(temp);
				temp = br.readLine();
			}
		} catch (IOException e) {
			return null;
		}
		return sb.toString();
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isDirExist(String fileName) {
		if (StringUtil.isEmpty(fileName)) {
			return false;
		}
		File file = new File(fileName);
		return file.exists() && file.isDirectory();
	}

	/**
	 * <判断文件夹是否存在 > <功能详细描述>
	 * 
	 * @param filePath
	 * @return [参数说明]
	 * @return boolean [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isFileExist(String filePath) {
		if (!StringUtil.isEmpty(filePath)) {
			File file = new File(filePath);
			return file.exists();
		}
		return false;
	}

	/**
	 * <写入文件>
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean writeFile(String data, String filePath,
			String fileName) {
		if (createDir(filePath)) {
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(new File(filePath, fileName));
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
				osw.write(data);
				osw.flush();
				osw.close();
				return true;
			} catch (FileNotFoundException e) {
				
			} catch (UnsupportedEncodingException e) {
				
			} catch (IOException e) {
				
			}
			return false;
		}
		return false;
	}

	/**
	 * 创建文件夹
	 */
	public static boolean createDir(String directory) {
		File destDir = new File(directory);
		destDir.mkdirs();
		return true;
	}

	/**
	 * 生成文件 创建人：pananz 创建时间：2012-12-19 - 下午3:08:39
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 *             返回类型：boolean
	 * @exception throws
	 */
	public static boolean createFile(String filePath) throws IOException {
		File file = new File(filePath);
		return file.createNewFile();
	}

	/**
	 * 用于生成文件名中的随机串部分
	 */
	private static final String RANDOM_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STR_LENGTH = 5;

	private static final String FILE_SP = "/";
	private static final int MAX_DIRECTORY = 300;

	/**
	 * 获取file在服务器里的存储路径.
	 * 
	 * @param userId
	 *            用户标识.
	 * @param filename
	 *            上传的文件名(含扩展名)
	 * @return file在服务器里的存储路径.
	 */
	public static String file(Long partnerId, String filename) {
		return getProudctDirBase(partnerId) + genFileName(filename);
	}

	public static String file(Long partnerId) {
		return dir(partnerId);
	}

	public static String dir(Long partnerId) {
		return getPartnerDirBase(partnerId);
	}

	/**
	 * 上传文件时生成文件名<br>
	 * 规则： md5Hex(文件名 + 当前时间毫秒数 + 5位随机串 ) + "." + 扩展名.
	 * 
	 * @param filename
	 *            上传的文件名(含扩展名)
	 * @return
	 */
	public static String genFileName(String filename) {
		String name = FilenameUtils.getBaseName(filename)
				+ System.currentTimeMillis()
				+ RandomStringUtils.random(RANDOM_STR_LENGTH, RANDOM_STR);
		return DigestUtils.md5DigestAsHex(name.getBytes()) + "."
				+ FilenameUtils.getExtension(filename);
	}

	class FileFilter implements FilenameFilter { // 实现FilenameFilter接口
		private String fileExtension;

		public FileFilter(String fileExtension) {
			this.fileExtension = fileExtension;
		}

		public boolean accept(File dir, String name) {
			return name.endsWith("." + fileExtension);
		}
	}

	private static String getProudctDirBase(Long partnerId) {
		if (null == partnerId || partnerId <= 0) {
			return StringUtils.EMPTY;
		}
		StringBuilder dir = new StringBuilder();
		dir.append(FILE_SP);
		dir.append(getFirstDirName(partnerId));
		dir.append(FILE_SP);
		dir.append(getSecondDirName(partnerId));
		dir.append(FILE_SP);
		dir.append(getThirdDirName(partnerId));
		dir.append(FILE_SP);
		dir.append(DateFormatUtils.format(new Date(), "yyyyMM"));
		dir.append(FILE_SP);
		return dir.toString();
	}

	private static String getPartnerDirBase(Long partnerId) {
		if (null == partnerId || partnerId <= 0) {
			return StringUtils.EMPTY;
		}
		StringBuilder dir = new StringBuilder();
		dir.append(FILE_SP);
		dir.append(getFirstDirName(partnerId));
		dir.append(FILE_SP);
		return dir.toString();
	}

	private static String getFirstDirName(Long userId) {
		if (null == userId || userId <= 0) {
			return StringUtils.EMPTY;
		}
		return String.valueOf(userId / (MAX_DIRECTORY * MAX_DIRECTORY));
	}

	private static String getSecondDirName(Long userId) {
		if (null == userId || userId <= 0) {
			return StringUtils.EMPTY;
		}
		return String.valueOf(userId / (MAX_DIRECTORY));
	}

	private static String getThirdDirName(Long userId) {
		if (null == userId || userId <= 0) {
			return StringUtils.EMPTY;
		}
		return String.valueOf(userId);
	}

	public static String createFileTime(String filePath) {
		try {
			Process proc = Runtime.getRuntime().exec(
					"cmd /c dir d:\\13013621.jpg /tc");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			String data = "";
			// it's quite stupid but work
			for (int i = 0; i < 6; i++) {
				data = br.readLine();
			}
			System.out.println("Extracted value : " + data);
			// split by space
			StringTokenizer st = new StringTokenizer(data);
			String date = st.nextToken();// Get date
			String time = st.nextToken();// Get time
			br.close();
			System.out.println("Creation Date  : " + date);
			System.out.println("Creation Time  : " + time);
			return date + " " + time;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

	}

	public static void main(String[] args) {
		System.out.println(createFileTime("ss"));
	}
}
