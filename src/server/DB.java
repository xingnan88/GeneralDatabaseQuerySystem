package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author 唐东洋、冯秀清、陈刚
 *连接数据库的包装类
 */
public class DB {
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static String str = "sun.jdbc.odbc.JdbcOdbcDriver";
	private static String url = "jdbc:odbc:book";
	private static String user = "sa";
	private static String pwd = "123456";
	/**
	 * 得到Connection连接
	 * @return Connection对象
	 */
	public static Connection getConn() {
		try {
			Class.forName(str);
			conn = DriverManager.getConnection(url,user,pwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	/**
	 * 创建一个Statement对象
	 * @param conn Connection变量
	 * @return Statement对象
	 */
	public static Statement createStmt(Connection conn) {
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stmt;
	}
	/**
	 * 得到一个结果集
	 * @param stmt Statement对象
	 * @param sql 一条SQL语句
	 * @return ResultSet结果集
	 */
	public static ResultSet getRs(Statement stmt, String sql) {
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * 关闭Connection连接
	 * @param conn Connection对象
	 */
	public static void close(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}
	/**
	 * 关闭Statement
	 * @param stmt Statement对象
	 */
	public static void close(Statement stmt) {
		if(stmt!=null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			stmt = null;
		}
	}
	/**
	 * 关闭ResultSet结果集
	 * @param rs ResultSet结果集对象
	 */
	public static void close(ResultSet rs) {
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			rs = null;
		}
	}
}
