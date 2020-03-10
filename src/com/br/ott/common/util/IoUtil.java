package com.br.ott.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import org.apache.log4j.Logger;

/**
 * 
 * Io流操作工具类
 *  
 * @author  pKF46373
 * @version  [版本号, May 17, 2011]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public final class IoUtil {
	/**
	 * 实例化一个记录日志
	 */
	private static Logger logger = Logger.getLogger(IoUtil.class);

	/** 服务端返回的xml主体结束标记  */
	private static final int CONNECTTIMEOUT = 30000;
	/** 服务端返回的xml主体结束标记  */
	private static final int READTIMEOUT = 30000;

	/**
	 * 通过mark方法添加一个标记
	 * 
	 * @param 输入流
	 * @return 输入流
	 * 
	 * @return InputStream [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static InputStream prepareStream(InputStream ins) {
		BufferedInputStream buffered = new BufferedInputStream(ins);
		buffered.mark(Integer.MAX_VALUE);
		return buffered;
	}

	/**
	 * 当完成一次对流的使用之后，通过reset方法就可以把流的读取位置重置到上次标记的位置，即流开始的地方。
	 * 
	 * @param ins [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static void resetStream(InputStream ins) {

		try {
			ins.reset();
			ins.mark(Integer.MAX_VALUE);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	}

	/**
	 * 把一个InputStream保存为字节数组
	 * 
	 * @param input 输入流
	 * @return 字节数组
	 * @return byte[] [返回类型说明]
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static byte[] saveStream(final InputStream input) {
		ByteBuffer buffer = ByteBuffer.allocate(Constants.DEFAULT_BYTE);
		ReadableByteChannel readChannel = Channels.newChannel(input);
		ByteArrayOutputStream output = new ByteArrayOutputStream(Constants.OUT_DEFAULT_BYTE);
		WritableByteChannel writeChannel = Channels.newChannel(output);
		try {
			while ((readChannel.read(buffer)) > 0 || buffer.position() != 0) {
				buffer.flip();
				writeChannel.write(buffer);
				buffer.compact();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return output.toByteArray();
	}

	/** 

	 * 将一个字符串转化为输入流 

	 */

	public static InputStream getStringStream(String sInputString) {

		if (sInputString != null && !sInputString.trim().equals("")) {
			try {
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("utf-8"));
				return tInputStringStream;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	/** 

	 * 将一个输入流转化为字符串 

	 */

	public static String getStreamString(InputStream tInputStream) {
		if (tInputStream != null) {
			try {
				BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
				StringBuffer tStringBuffer = new StringBuffer();
				String sTempOneLine = "";
				while ((sTempOneLine = tBufferedReader.readLine()) != null) {
					tStringBuffer.append(sTempOneLine);
				}
				return tStringBuffer.toString();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return null;

	}

	/**
	 * 发送请求，获取请求返回的输入流 <功能详细描述>
	 * 
	 * @param input 向请求地址发送的请求流
	 * @param cmd 请求命令字
	 * @return [参数说明]
	 * @return InputStream 请求返回一个流
	 * @throws IOException 
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static synchronized InputStream visitInterface(InputStream input, String visitUrl) {
		InputStream in = null;
		URL url;
		try {
			url = new URL(visitUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
			conn.setConnectTimeout(CONNECTTIMEOUT);
			conn.setReadTimeout(READTIMEOUT);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", Constants.CHARSET);
			conn.setRequestProperty("Content-Type", Constants.XML_CONTENTTYPE);
			byte[] b = new byte[Constants.DEFAULT_BYTE];
			int l = 0;
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			while ((l = input.read(b, 0, b.length)) != -1) {
				out.write(b, 0, l);
			}
			out.flush();
			out.close();
			in = conn.getInputStream();
			return in;
		} catch (IOException e) {
			logger.error(e.getMessage());
			return null;
		} finally {
			IoUtil.destroyInputStream(input);
		}

	}

	/**
	 * <默认构造函数>
	 */
	private IoUtil() {

	}

	/**
	 * 销毁输入流对象
	 * 
	 * @param in 输入流
	 * 
	 * @return void 不需要返回
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static void destroyInputStream(InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * 销毁输出流对象
	 * 
	 * @param outs 输出流
	 * 
	 * @return void 不需要返回
	 * @exception throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public static void destroyOutputStream(OutputStream outs) {
		try {
			outs.flush();
			outs.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

}
