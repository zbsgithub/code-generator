package com.davidstudio.gbp.tool.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import oracle.jdbc.pool.OracleConnectionCacheImpl;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 说明：oracle数据库访问的工具类。
 * 
 * @author 郑王力 2005-3-13
 */
public class DBTool {

	private static DataSource dataSource = null; // datasource对象

	// 私有的构造方法，用于初始化数据库连接池
	private DBTool() {

	}

	/**
	 * 初始化datasource对象，使用前必须首先调用该方法设置连接参数
	 * 
	 * @throws Exception
	 */
	public static void initDataSource(String url, String username,
			String password) throws SQLException {
		OracleConnectionCacheImpl con;

		try {
			con = new OracleConnectionCacheImpl();
			con.setURL(url);
			con.setUser(username);
			con.setPassword(password);

			con.getConnection();

			dataSource = con;

		} catch (SQLException e) {
			System.out.println("无法建立数据库连接:" + e.toString());
			throw e;
		}
	}

	public static void initDataSource(String driver, String url,
			String username, String password) {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(username);
		driverManagerDataSource.setPassword(password);
		dataSource = driverManagerDataSource;
	}

	/**
	 * 获得数据库连接。 <br>
	 * <br>
	 */
	public static Connection getConnection() throws SQLException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			System.out.println("无法得到数据库连接:" + e.toString());
			throw e;
		}
	}

	/**
	 * 执行查询操作，返回查询结果，并组成List对象
	 */
	public static List<Map<Object, Object>> executeQuery(String sql)
			throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);
		ResultSet rs = st.executeQuery();

		List<Map<Object, Object>> result = convertToList(rs);
		close(conn, st, rs);

		return result;
	}

	/**
	 * 执行更新或删除操作，不返回结果
	 */
	public static void executeUpdate(String sql) throws SQLException {
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement(sql);

		st.executeUpdate();

		close(conn, st, null);

	}

	/**
	 * 将查询结果封装成List。 <br>
	 * List中元素类型为封装一行数据的Map，Map key为字段名，value为相应字段值
	 * 注意：为了统一处理不同数据库的大小写，字段名全部转换为小写
	 */
	public static List<Map<Object, Object>> convertToList(ResultSet rs)
			throws SQLException {
		if (rs == null) {
			return Collections.emptyList();
		}

		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();

		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		Map<Object, Object> rowData;
		while (rs.next()) {
			rowData = new HashMap<Object, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i).toLowerCase(), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 仅关闭connection对象
	 */
	public static void close(Connection conn) {
		close(conn, null, null);
	}

	/**
	 * 关闭ResultSet、Statement和Connection
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (java.sql.SQLException ex) {
				//
			}
		}
		if (stmt != null) {
			try {
				stmt.close();
			} catch (java.sql.SQLException ex) {
				//
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (java.sql.SQLException ex) {
				//
			}
		}
	}

}