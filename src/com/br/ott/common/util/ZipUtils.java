/*
 * 文 件 名:  ZipUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  wKF45592
 * 修改时间:  2011-10-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.br.ott.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * <zip打包、解包工具类>
 * 
 * @author wKF45592
 * @version [版本号, 2011-10-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class ZipUtils {

	/** 缓冲区4KB */
	private static final int BUFFER_SIZE = 4096;

	private ZipUtils() {
	}

	/**
	 * <解压zip文件>
	 * 
	 * @param zipFile
	 *            zip文件绝对路径
	 * @param targetDir
	 *            目标目录绝对路径
	 * @return boolean 解压成功与否
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean unzip(String zipFile, String targetDir) {

		// 检查和创建目标文件夹
		if (!checkDir(targetDir)) {
			return false;
		}

		ZipEntry entry = null; // 每个zip条目的实例

		ZipInputStream zis = null;
		BufferedOutputStream dest = null; // 缓冲输出流
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(zipFile);
			zis = new ZipInputStream(new BufferedInputStream(fis));

			int count;
			byte data[] = new byte[BUFFER_SIZE];

			// 循环处理ZipEntry
			while ((entry = zis.getNextEntry()) != null) {
				try {
					File entryFile = new File(targetDir + File.separator
							+ entry.getName());
					// 检查和创建父目录
					if (!checkDir(entryFile.getParent())) {
						return false;
					}

					if (!entry.isDirectory()) {
						fos = new FileOutputStream(entryFile);
						dest = new BufferedOutputStream(fos, BUFFER_SIZE);
						while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
							dest.write(data, 0, count);
						}
						dest.flush();

					} else {
						checkDir(entryFile.getAbsolutePath());
					}

				} catch (Exception ex) {
					return false;
				}
			}
		} catch (IOException ex) {
			return false;
		} finally {
			try {
				if (dest != null)
					dest.close();
				if (fos != null)
					fos.close();
				if (zis != null)
					zis.close();
				if (fis != null)
					fis.close();
			} catch (IOException e) {
			}
		}
		return true;
	}

	/**
	 * <解压zip中的单个文件>
	 * 
	 * @param zipFile
	 * @param desFile
	 * @param targetFile
	 * @return boolean
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean unzipSingleFile(String zipFile, String fileName,
			String targetFile) {

		File tFile = new File(targetFile);

		// 检查和创建目标文件夹
		String parentDir = tFile.getParentFile().getAbsolutePath();
		if (!checkDir(parentDir)) {
			return false;
		}

		try {
			ZipEntry entry = null; // 每个zip条目的实例
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));

			// 循环处理ZipEntry
			while ((entry = zis.getNextEntry()) != null) {
				try {
					if (!fileName.equals(entry.getName())) {
						continue;
					}

					int count;
					byte data[] = new byte[BUFFER_SIZE];
					FileOutputStream fos = new FileOutputStream(tFile);
					BufferedOutputStream dest = new BufferedOutputStream(fos,
							BUFFER_SIZE);
					while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					break;
				} catch (Exception ex) {
					return false;
				}
			}

			zis.close();
		} catch (IOException ex) {
			return false;
		}
		return true;
	}

	/**
	 * <检查并创建目录>
	 * 
	 * @param targetDir
	 * @return boolean
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private static boolean checkDir(String targetDir) {
		File dir = new File(targetDir);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				return false;
			}
		} else if (!dir.isDirectory()) {
			return false;
		}
		return true;
	}

	/**
	 * <打包文件夹下的所有文件>
	 * 
	 * @param srcFolder
	 *            源文件夹
	 * @param zipFile
	 *            目标文件
	 * @return boolean
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean zipFolder(String srcFolder, String zipFile) {

		// 打开要输出的文件
		File file = new File(srcFolder);
		if (!file.exists() || !file.isDirectory()) {
			return false;
		}

		// 获取文件夹下的子文件
		File fileList[] = file.listFiles();
		if (fileList.length == 0) {
			return false;
		}

		LinkedList<String> list = new LinkedList<String>();
		for (File f : fileList) {
			list.add(f.getAbsolutePath());
		}

		return zipFiles(list, zipFile);
	}

	public static boolean zipFile(String srcFolder, String zipFile) {
		LinkedList<String> list = new LinkedList<String>();
		File f = new File(srcFolder);
		list.add(f.getAbsolutePath());
		return zipFiles(list, zipFile);
	}

	/**
	 * <指定文件和文件夹列表压缩为一个文件 >
	 * 
	 * @param fileList
	 *            指定的文件和文件夹列表
	 * @param desFile
	 *            目标zip文件的绝对路径
	 * @return boolean 成功与否
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean zipFiles(Queue<String> fileList, String desFile) {

		if (fileList == null || fileList.size() == 0) {
			return false;
		}

		// 找出父文件夹的路径
		File tempFile = new File(fileList.peek());
		if (!tempFile.exists()) {
			return false;
		}

		// 检查目标目录是否存在
		File desDir = new File(desFile).getParentFile();
		if (!desDir.exists() || !desDir.isDirectory()) {
			if (!desDir.mkdirs()) {
				return false;
			}
		}

		String parentFolderPath = tempFile.getParent() + File.separator;

		// 新的队列用来容纳文件树
		Queue<String> allFiles = new LinkedList<String>(fileList);
		ZipOutputStream outZip = null;

		try {
			// 创建Zip包
			outZip = new ZipOutputStream(new FileOutputStream(desFile));
			// 只归档不压缩, 会导致 CRC mismatch 错误
			// outZip.setMethod(ZipOutputStream.STORED);

			while (!allFiles.isEmpty()) {
				File file = new File(allFiles.poll());
				// 判断是不是文件
				if (file.isFile()) {
					writeFileToZip(parentFolderPath, file, outZip);
				} else if (file.isDirectory()) {
					handleDir(parentFolderPath, file, allFiles, outZip);
				}
			}

			outZip.finish();
			outZip.close();
			return true;
		} catch (IOException e) {
		} catch (Exception e) {
		}

		// 失败之后删除已经创建的文件
		if (!new File(desFile).delete()) {
		}
		return false;
	}

	private static void handleDir(String parentFolderPath, File file,
			Queue<String> allFiles, ZipOutputStream outZip) {
		File[] list = file.listFiles();
		// 如果没有子文件, 则添加进去即可
		if (list.length <= 0) {
			try {
				String path = getRelativePath(parentFolderPath,
						file.getAbsolutePath())
						+ File.separator;
				ZipEntry zipEntry = new ZipEntry(path);
				outZip.putNextEntry(zipEntry);
				outZip.closeEntry();
			} catch (IOException e) {
			}
		}

		// 添加到待处理列表
		for (File f : list) {
			allFiles.add(f.getAbsolutePath());
		}
	}

	private static void writeFileToZip(String parentFolderPath, File file,
			ZipOutputStream outZip) {
		ZipEntry zipEntry = new ZipEntry(getRelativePath(parentFolderPath,
				file.getAbsolutePath()));
		FileInputStream fis = null;
		try {
			outZip.putNextEntry(zipEntry);
			int len;
			byte[] buffer = new byte[BUFFER_SIZE];
			fis = new FileInputStream(file);
			while ((len = fis.read(buffer)) != -1) {
				outZip.write(buffer, 0, len);
			}
			outZip.closeEntry();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <获取相对路径>
	 * 
	 * @param parentFolderPath
	 * @param desFile
	 * @return String
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	private static String getRelativePath(String parentFolderPath,
			String desFile) {
		return desFile.replace(parentFolderPath, "");
	}

	public static void main(String[] args) {
		String srcFolder = "D:/source/2016-03-10/behavior_2020.txt";
		String zipFile = "D:/source/2016-03-10/behavior_2020.zip";
		LinkedList<String> list = new LinkedList<String>();
		File f = new File(srcFolder);
		list.add(f.getAbsolutePath());
		zipFiles(list, zipFile);
	}

}
