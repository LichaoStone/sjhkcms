/*
 * 文 件 名:  UploadFile.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  jKF46825
 * 修改时间:  2011-7-11
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.br.ott.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.br.ott.Global;
import com.br.ott.common.exception.OTTException;
import com.br.ott.common.exception.OTTRuntimeException;
import com.br.ott.common.exception.SystemExceptionCode.UploadCode;

/**
 * <上传文件> <功能详细描述>
 * 
 * @author jKF46825
 * @version [版本号, 2011-7-11]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class UploadFile {
	/**
	 * 文件大小设置
	 */
	private static final int BUFFER_SIZE = 16 * 1024;
	/**
	 * 文件标题
	 */
	private String title;
	/**
	 * 上传文件域对象
	 */
	private File upload;
	/**
	 * 上传文件名
	 */
	private String uploadFileName;
	/**
	 * 上传文件类型
	 */
	private String uploadContentType;

	/**
	 * 实例化一个记录日志对象
	 */
	private static Logger logger = Logger.getLogger(UploadFile.class);

	/**
	 * <上传文件> <功能详细描述>
	 * 
	 * @param src
	 *            :上传文件地址
	 * @param dst
	 *            ：保存文件全路径 [参数说明]
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static void copy(File src, File dst) {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	/**
	 * 上传文件到远程服务器并返回一个地址 创建人：pananz 创建时间：2012-11-15 - 下午5:50:59
	 * 
	 * @param file
	 * @param id
	 * @param request
	 * @param uploadUrl
	 * @return
	 * @throws OTTException
	 *             返回类型：String
	 * @exception throws
	 */
	public static String uploadFileToServer(Object object, String id,
			String appUrl, String oType) throws OTTException {
		if (object == null)
			return "";
		MultipartFile file = null;
		File f = null;
		if(object instanceof CommonsMultipartFile)
			file = (MultipartFile)object;
		else
			f = (File)object;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("fileName", (file != null ? file.getOriginalFilename() : f.getName()));
		params.put("secondsDir", appUrl);
		String url = Global.SOURCE_UPLOAD_SOURCE_URL;
		logger.debug("上传地址: " + url);
		HashMap<String, Object> map = UploadApiUtil.uploadFile(url, (file != null ? file : f),
				params);
		String returnCode = (String) map.get("returnCode");
		if (!"0".equals(returnCode)) {
			throw new OTTException((String) map.get("returnCode"),
					(String) map.get("returnMsg"));
		}
		return (String) map.get("fileUrl");
	}

	/**
	 * 上传产品图片并压缩 创建人：pananz 创建时间：2012-11-27 - 上午10:09:16
	 * 
	 * @param file
	 * @param id
	 * @param appUrl
	 * @return
	 * @throws OTTException
	 *             返回类型：String
	 * @exception throws
	 */
	public static String uploadImageToServerAndPress(MultipartFile file,
			Long id, String appUrl) throws OTTException {
		if (file.isEmpty())
			return null;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", String.valueOf(id));
		params.put("fileName", file.getOriginalFilename());
		params.put("secondsDir", appUrl);
		params.put("isPress", String.valueOf(true));

		HashMap<String, Object> map = UploadApiUtil.uploadFile(
				Global.SOURCE_UPLOAD_SOURCE_URL, file, params);
		String returnMsg = (String) map.get("returnMsg");
		if (!StringUtil.isEmpty(returnMsg)) {
			throw new OTTException((String) map.get("returnCode"), returnMsg);
		}
		return (String) map.get("fileUrl");
	}

	/**
	 * 上传合作伙伴/产品图片
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String uploadFile(MultipartFile file, Long id,
			HttpServletRequest request, String basePath) throws OTTException {
		if (file.isEmpty())
			return null;
		String toFilePath = basePath + FileUtil.file(id);
		String path = request.getSession().getServletContext()
				.getRealPath(toFilePath);
		if (!FileUtil.isDirExist(path)) {
			FileUtil.createDir(path);
		}

		String newFileName = FileUtil.genFileName(file.getOriginalFilename());
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		try {
			File toFile = new File(path + "/" + newFileName);
			// 开始读取文件
			byte[] bytes = new byte[1024 * 10];
			fos = new FileOutputStream(toFile);
			bos = new BufferedOutputStream(fos);
			is = file.getInputStream();
			int len = 0;
			while ((len = is.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			throw new OTTRuntimeException(UploadCode.FILE_NO_FOUND, e);
		} catch (IOException e) {
			throw new OTTRuntimeException(UploadCode.FILE_IO_UPLOAD_ERROR, e);
		} finally {
			try {
				if (null != is) {
					is.close();
					is = null;
				}
				if (null != bos) {
					bos.close();
					bos = null;
				}
				if (null != fos) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				throw new OTTRuntimeException(UploadCode.FILE_IO_CLOSE_ERROR, e);
			}
		}
		return toFilePath + newFileName;
	}

	public static String uploadFile2(MultipartFile file,
			HttpServletRequest request, String basePath) throws OTTException {
		if (file.isEmpty())
			return "";
		if (!FileUtil.isDirExist(basePath)) {
			FileUtil.createDir(basePath);
		}
		String fileName = file.getOriginalFilename();
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		InputStream is = null;
		try {
			File toFile = new File(basePath + "/" + fileName);
			// 开始读取文件
			byte[] bytes = new byte[1024];
			fos = new FileOutputStream(toFile);
			bos = new BufferedOutputStream(fos);
			is = file.getInputStream();
			int len = 0;
			while ((len = is.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			bos.flush();
			return basePath + fileName;
		} catch (FileNotFoundException e) {
			throw new OTTRuntimeException(UploadCode.FILE_NO_FOUND, e);
		} catch (IOException e) {
			throw new OTTRuntimeException(UploadCode.FILE_IO_UPLOAD_ERROR, e);
		} finally {
			try {
				if (null != is) {
					is.close();
					is = null;
				}
				if (null != bos) {
					bos.close();
					bos = null;
				}
				if (null != fos) {
					fos.close();
					fos = null;
				}
			} catch (IOException e) {
				throw new OTTRuntimeException(UploadCode.FILE_IO_CLOSE_ERROR, e);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

}
