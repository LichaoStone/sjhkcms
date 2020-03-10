package com.br.ott.common.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


@SuppressWarnings("static-access")
public class JDBCManager implements ApplicationContextAware{
	
	static private ReadProperties jdbcPro = new ReadProperties("jdbc.properties");

	static final private String DRIVERNAME = jdbcPro.getPara("driverClassName");
	static final private String URL = jdbcPro.getPara("url");
	private static final String USERNAME = jdbcPro.getPara("username");
	private static final String PASSWORD = jdbcPro.getPara("password");
	
	private static ApplicationContext applicationContext;
	
	private static Logger logger = Logger.getLogger(JDBCManager.class);
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public static Connection getConnectionForSource(){
		Connection connection = null;
		try {
			connection = getDataSource().getConnection();
		} catch (SQLException e) {
			logger.info(JDBCManager.class.getName() + "JDBCManager获取DataSource失败");
			e.printStackTrace();
		}
		return connection;
	}
	
	public static Connection getConnection(){
		Connection connection = null;
		try {
			Class.forName(DRIVERNAME);
			connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} catch (ClassNotFoundException e) {
			logger.info(JDBCManager.class.getName() + "JDBCManager读取jdbc.properties文件失败");
			e.printStackTrace();
		} catch (SQLException e) {
			logger.info(JDBCManager.class.getName() + "JDBCManager读取jdbc.properties文件失败");
			e.printStackTrace();
		}
		return connection;
	} 
	
	public static DataSource getDataSource(){
		DataSource ds = (DataSource)applicationContext.getBean("dataSource");
		return ds;
	}
	
	public static void close(ResultSet rs, PreparedStatement stmt, Statement tent, Connection conn) throws SQLException{
		if(rs != null)
			rs.close();
		if(stmt != null)
			stmt.close();
		if(tent != null)
			tent.close();
		if(conn != null)
			conn.close();
	}	
}