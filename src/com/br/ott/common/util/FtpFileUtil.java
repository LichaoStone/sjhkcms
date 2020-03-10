package com.br.ott.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import com.br.ott.Global;
import com.br.ott.common.exception.OTTException;

/* 
 *  FTP文件操作
 *  文件名：FtpFileUtil.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方网络科技有限公司
 *  创建人：陈登民
 *  创建时间：2013-2-20 - 上午11:40:10
 */
public class FtpFileUtil {
	private static final Logger LOGGER = Logger.getLogger(FtpFileUtil.class);

	private static final int BUFFER_SIZE = 1024000;
	private static final String CONTROL_ENCODING = "UTF-8";

	private FTPClient ftpClient;
	private String hostname;
	private String username;
	private String password;
	private int port;

	/**
	 * 
	 * 构造方法
	 * 
	 * @param hostname
	 *            FTP主机
	 * @param username
	 *            FTP账号
	 * @param password
	 *            FTP密码
	 */
	public FtpFileUtil(String hostname, String username, String password) {
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		ftpClient = new FTPClient();
	}

	public FtpFileUtil(String hostname, int port, String username,
			String password) {
		this(hostname, username, password);
		this.port = port;
	}

	private void login() throws OTTException {
		try {
			if (port == 0) {
				ftpClient.connect(hostname);
			} else {
				ftpClient.connect(hostname, port);
			}
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				LOGGER.info("FTP服务器拒绝连接");
				throw new OTTException("500", "FTP服务器拒绝连接");
			}

			ftpClient.login(username, password);
		} catch (Exception e) {
			LOGGER.error("连接FTP服务器失败", e);
			throw new OTTException("500", "连接FTP服务器失败");
		}
	}

	private void logout() throws OTTException {
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
			}

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("关闭FTP连接发生异常！", e);
			throw new OTTException("500", "关闭FTP连接发生异常");
		}
	}

	/**
	 * 上传文件 创建人：陈登民 创建时间：2013-2-21 - 上午10:03:02
	 * 
	 * @param srcFile
	 *            要上传文件的完整路径 （/data/tmp/a.txt）
	 * @param targetFileName
	 *            上传到服务器上的文件名称 (test.txt)
	 * @param path
	 *            当前用户所在目录下的子目录 (如：用户所在目录为/data/, 现将文件上传到/data/test下，则该处应填 test)
	 * @return
	 * @throws OTTException
	 *             返回类型：boolean
	 * @exception throws
	 */
	public boolean upload(String srcFile, String targetFileName, String path)
			throws OTTException {
		File file = new File(srcFile);
		return upload(file, targetFileName, path);
	}

	public boolean upload(File file, String targetFileName, String path)
			throws OTTException {
		if (null == file) {
			LOGGER.info("no find file");
			return false;
		}
		// 登录FTP
		login();
		boolean success = false;
		String fileName = StringUtil.isEmpty(targetFileName) ? file.getName()
				: targetFileName;
		FileInputStream fis = null;
		try {
			// 创建子目录,并切换到子目录
			createDir(path);
			fis = new FileInputStream(file);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setBufferSize(BUFFER_SIZE);
			ftpClient.setControlEncoding(CONTROL_ENCODING);

			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// 上传文件
			success = ftpClient.storeFile(fileName, fis);
		} catch (IOException e) {
			LOGGER.error("上传到FTP服务器失败", e);
			throw new OTTException("500", "上传到FTP服务器失败" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				// 退出FTP
				logout();
			} catch (OTTException e) {
				success = false;
				throw e;
			}
		}
		return success;
	}

	public List<File> downloadFiles(String targetBaseTmpPath, boolean delete)
			throws OTTException {
		// 登录FTP
		login();

		if (!FileUtil.isDirExist(targetBaseTmpPath)) {
			FileUtil.createDir(targetBaseTmpPath);
		}

		List<File> ftpFiles = null;
		try {
			File localFile = null;
			OutputStream is = null;
			ftpFiles = new ArrayList<File>();

			ftpClient.enterLocalPassiveMode();
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile ftpFile : files) {
				if (ftpFile.isFile()) {
					localFile = new File(targetBaseTmpPath + "/"
							+ ftpFile.getName());
					is = new FileOutputStream(localFile);
					boolean flag = ftpClient
							.retrieveFile(ftpFile.getName(), is);
					is.close();
					if (flag) {
						ftpFiles.add(localFile);
					}
					if (delete) {
						// 移动到备份文件夹下
						createDir("backup", false);// 创建备份文件夹
						ftpClient.rename(ftpFile.getName(),
								"backup/" + ftpFile.getName());
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("下载文件出错", e);
			throw new OTTException("500", "下载文件出错" + e.getMessage());
		} finally {
			logout();
		}
		return ftpFiles;
	}

	public File downloadSingleFile(String targetPath, String remotePath,
			String fileName) throws OTTException {
		// 登录FTP
		login();

		if (!FileUtil.isDirExist(targetPath)) {
			FileUtil.createDir(targetPath);
		}
		OutputStream is = null;
		try {

			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(remotePath);
			FTPFile[] files = ftpClient.listFiles();
			for (FTPFile ftpFile : files) {
				LOGGER.debug("fileName===" + ftpFile.getName());
				if (ftpFile.isFile() && ftpFile.getName().equals(fileName)) {
					File localFile = new File(targetPath + "/"
							+ ftpFile.getName());
					is = new FileOutputStream(localFile);
					boolean flag = ftpClient
							.retrieveFile(ftpFile.getName(), is);
					if (flag) {
						return localFile;
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("下载文件出错", e);
			throw new OTTException("500", "下载文件出错" + e.getMessage());
		} finally {
			logout();
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private boolean createDir(String path) throws IOException {
		return createDir(path, true);
	}

	@SuppressWarnings("unused")
	private boolean createDir(String path, boolean changeWorkingDir)
			throws IOException {
		if (path == null || path == "" || ftpClient == null) {
			LOGGER.info("no child dir create");
			return false;
		}
		String[] paths = path.split("/");
		for (int i = 0; i < paths.length; i++) {
			FTPFile[] ffs = ftpClient.listDirectories(paths[i]);
			boolean is = false;
			boolean f = true;
			if (f) {
				for (FTPFile ftpFile : ffs) {
					if (changeWorkingDir) {
						ftpClient.changeWorkingDirectory(paths[i]);
					}
					is = true;
					break;
				}
			}
			if (!is) {
				f = false;
				boolean a, x = false;
				a = ftpClient.makeDirectory(new String(paths[i]
						.getBytes(CONTROL_ENCODING), "iso-8859-1"));
				if (changeWorkingDir) {
					x = ftpClient.changeWorkingDirectory(paths[i]);
				}
				if (!a || !x)
					return false;
			}
		}
		return true;
	}

	/**
	 * 执行FTP命令 创建人：陈登民 创建时间：2013-2-22 - 上午11:33:06
	 * 
	 * @param command
	 * @throws OTTException
	 *             返回类型：void
	 * @exception throws
	 */
	public void exeCommand(String command) throws OTTException {
		try {
			login();
			int res = ftpClient.sendCommand(command);
			logout();
			if (res == 500) {
				throw new OTTException("500", "命令无效");
			} else if (res == 550) {
				throw new OTTException("500", "无该命令操作权限");
			}
		} catch (IOException e) {
			LOGGER.error("执行FTP命令出错", e);
			throw new OTTException("500", "命令执行出错" + e.getMessage());
		}
	}

	public boolean rename(String fromName, String toName, String toDir)
			throws OTTException {
		boolean flag = false;
		try {
			login();

			createDir(toDir, false);

			flag = ftpClient.rename(fromName, toDir + "/" + toName);
			logout();
		} catch (IOException e) {
			LOGGER.error("执行FTP重命名出错", e);
			throw new OTTException("500", "重命名出错" + e.getMessage());
		}
		return flag;
	}

	public static void main(String[] args) {
		FtpFileUtil ffu = new FtpFileUtil(Global.FTP_HOSTNAME, Global.FTP_PORT,
				Global.FTP_ACCOUNT, Global.FTP_PASSWD);
		try {
			// boolean falg = ffu.upload("d:\\aadf.zip", "", "");
			// System.out.println("flag:" + falg);
			File file = ffu.downloadSingleFile("d:/", "", "710.zip");
			System.out.println("file:" + file.getName());
		} catch (OTTException e) {
			e.printStackTrace();
		}
	}
}
