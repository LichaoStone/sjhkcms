package com.br.ott.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.br.ott.Global;

/* 
 *  
 *  文件名：RunShellUtile.java
 *  版权：BoRuiCubeNet. Copyright 2012-2012,All rights reserved
 *  公司名称：青岛博瑞立方科技有限公司
 *  创建人：pananz
 *  创建时间：2014-3-27 - 上午9:58:58
 */
public class LinuxCmdUtils {
	private static final Logger logger = Logger.getLogger(LinuxCmdUtils.class);

	// String[] commands = new String[] { "find", ".", "-name", "*mysql*",
	// "-print" };
	public static String runCmd(String[] cmds) {
		try {
			Process process = Runtime.getRuntime().exec(cmds);
			InputStreamReader isr = new InputStreamReader(
					process.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
			isr.close();
			logger.info("执行命令反馈提示：" + sb.toString());
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("执行命令出错................");
		}
		return null;
	}

	/**
	 * SCP传送文件到其他服务器 先建中间目录，再传文件 ssh $USER_NAME@$1 mkdir -p sourcePath scp
	 * filePath root@IP:/savePath 创建人：pananz 创建时间：2014-3-27 - 上午10:28:22
	 * 
	 * @param filePath
	 * @return 返回类型：String
	 * @exception throws
	 */
	public static boolean scpFile(String filePath,String finalPath, String dir) {
		try {
			String sourcePath = Global.SOURCE_PATH + dir;
			
			String[] urls = "aa,bb,cc".split(",");
			for (String url : urls) {
				String mkCmd = "ssh " + url + " mkdir -p " + sourcePath;
				String scpCmd = "scp " + filePath + " " + url + ":"
						+ sourcePath;
				String mvcmd = "ssh " + url + " mv " + filePath +" "+finalPath;
				
				String[] mkdirs = { "/bin/sh", "-c", mkCmd };
				String[] cmds = { "/bin/sh", "-c", scpCmd };
				String[] mvcmds = { "/bin/sh", "-c", mvcmd };
				logger.info(mkCmd + "==" + scpCmd + "\n");
				
				runCmd(mkdirs);
				
				runCmd(cmds);
				
				Thread.sleep(60*1000);
				runCmd(mvcmds);
			}
			return true;
		} catch (Exception e) {
			logger.error("传送文件出错..................");
		}
		return false;
	}

}
