package server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 
 * @author 冯秀清、唐东洋、陈刚
 *管理业务逻辑处理层
 */
public class Manage {
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	private static String str = null;
	/**
	 * 增加记录
	 * @param ISBN 编号
	 * @param name 书名
	 * @param author 作者
	 * @param publisher 出版社
	 * @param date 出版日期
	 * @param price 价格
	 * @return Boolean值
	 */
	public static boolean addBook(String ISBN, String name, String author,
			String publisher, String date, Double price) {
		boolean flag = false;
		try {
			conn = DB.getConn();
			stmt = DB.createStmt(conn);
			String sql;
			sql = "insert into bookform values(\'" + ISBN + "\',\'" + name
					+ "\',\'" + author + "\',\'" + publisher + "\',\'" + date
					+ "\'," + price + ")";
			if (stmt.executeUpdate(sql) != 0) {
				flag = true;
			}
			DB.close(conn);
			DB.close(stmt);
			DB.close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 删除记录
	 * @param ISBN 编号
	 * @return Boolean变量
	 */
	public static boolean delBook(String ISBN) {
		boolean flag = false;
		try {
			conn = DB.getConn();
			stmt = DB.createStmt(conn);
			String sql;
			sql = "delete from bookform where ISBN=\'" + ISBN + "\'";
			if (stmt.executeUpdate(sql) == 1) {
				flag = true;
			}
			DB.close(conn);
			DB.close(stmt);
			DB.close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 修改图书模块
	 * @param ISBN 编号
	 * @param name 书名
	 * @param author 作者
	 * @param publisher 出版社
	 * @param date 出版日期
	 * @param price 价格
	 * @return Boolean变量
	 */
	public static boolean modifyBook(String ISBN, String name, String author,
			String publisher, String date, Double price) {
		boolean flag = false;
		try {
			conn = DB.getConn();
			stmt = DB.createStmt(conn);
			String sql;
			sql = "update bookform set name=\'" + name + "\',author=\'"
					+ author + "\',publisher=\'" + publisher + "\',date=\'"
					+ date + "\',price=" + price + " where ISBN=\'" + ISBN
					+ "\'";
			if (stmt.executeUpdate(sql) != 0) {
				flag = true;
			}
			DB.close(conn);
			DB.close(stmt);
			DB.close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
	/**
	 * 通过编号查询记录
	 * @param ISBN 查询编号
	 * @return String对象
	 */
	public static String query(String ISBN) {
		/*
		 * 在重新查询的时候必须把str置为空值，不然在服务器运行期间第一次的查询会一直保存在str中，
		 * 这样会致使标志位始终为true。
		 */
		str = null;
		boolean flag = false;
		try {
			conn = DB.getConn();
			stmt = DB.createStmt(conn);
			String sql;
			sql = "select ISBN,name,author,publisher,date,price from bookform where ISBN=\'"
					+ ISBN + "\'";
			rs = DB.getRs(stmt, sql);
			while (rs.next()) {
				str = rs.getString("ISBN") +","+ rs.getString("name") +","+ rs.getString("author") 
				+","+ rs.getString("publisher") +","+ rs.getString("date") +","+ rs.getString("price");
			}
			/*
			 * 如果str为空则表示没有查询到任何的结果
			 */
			if (str != null) {
				flag = true;
			}
			DB.close(conn);
			DB.close(stmt);
			DB.close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag + "," + str;//把标志和查询结果一起发到客户端，然后客户端对其进行解析
	}
	/**
	 * 登陆检测
	 * @param userName 用户名
	 * @param password 密码
	 * @return Boolean值，用于判断是否登陆.true-登陆成功；false-登陆失败
	 */
	public static boolean check(String userName, String password) {
		boolean flag = false;
		try {
			conn = DB.getConn();
			stmt = DB.createStmt(conn);
			String sql;
			sql = "select userName,password from login ";
			rs = DB.getRs(stmt, sql);
			while (rs.next()) {
				if (rs.getString("userName").trim().equals(userName)
						&& rs.getString("password").trim().equals(password)) {
					flag = true;
					break;
				} else {
					flag = false;
				}
			}
			DB.close(conn);
			DB.close(stmt);
			DB.close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
