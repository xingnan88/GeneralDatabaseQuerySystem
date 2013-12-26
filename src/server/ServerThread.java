package server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
/**
 * 
 * @author 唐东洋、冯秀清、陈刚
 *服务器端的服务层
 */
public class ServerThread extends Thread {
	InetAddress yourAddress;
	Socket socket = null;
	DataOutputStream out = null;
	DataInputStream in = null;
	Connection conn = null;
	Statement stmt = null;
	ResultSet rs;
	int number;
/**
 * 
 * @param t Socket对像
 */
	ServerThread(Socket t) {
		socket = t;
		conn = DB.getConn();
		stmt = DB.createStmt(conn);
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
 * (non-Javadoc)
 * @see java.lang.Thread#run()
 */
	public void run() {
		String s = null;
		int N = 0;
		while (true) {
//			读取判断操作类型
			try {
				s = in.readUTF();
			} catch (IOException e) {
				try {
					socket.close();
					DB.close(conn);
				} catch (Exception e1) {
				}
				System.out.println("客户离开了");
				break;
			}
//			登陆
			if(s.equals("login")) {
				try {
					out.writeBoolean(Manage.check(in.readUTF(), in.readUTF()));
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
//				查询
			} else if(s.equals("query")) {
				try {
					String str = in.readUTF();
					if(str!=null) {
						out.writeUTF(Manage.query(str));
					}
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
//				修改
			} else if(s.equals("mod")) {
				try {
					out.writeBoolean(Manage.modifyBook(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readDouble()));
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
//				删除
			}else if(s.equals("del")) {
				try {
					String str = in.readUTF();
					if(str!=null) {
						out.writeBoolean(Manage.delBook(str));
					}
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
//				增添
			} else if(s.equals("add")) {
				try {
					out.writeBoolean(Manage.addBook(in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readUTF(), in.readDouble()));
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
			} else{
//				通用查询
				try {
					if (s.startsWith("字段个数:")) {
						String number = s.substring(s.lastIndexOf(":") + 1);
						N = Integer.parseInt(number);//得到字段数
					} else {
						String sqlCondition = null;
//						解析读取的字符串
						String 表名 = "", 查询内容 = "", 字段 = "", 查询方式 = "";
						StringTokenizer fenxi = new StringTokenizer(s, ":");
						if (fenxi.hasMoreTokens())
							表名 = fenxi.nextToken();
						if (fenxi.hasMoreTokens())
							查询内容 = fenxi.nextToken();
						if (fenxi.hasMoreTokens())
							字段 = fenxi.nextToken();
						if (fenxi.hasMoreTokens())
							查询方式 = fenxi.nextToken();
						if (查询方式.equals("完全一致")) {
							sqlCondition = "SELECT * FROM " + 表名 + " WHERE " + 字段
									+ " LIKE " + "'" + 查询内容 + "' ";
						} else if (查询方式.equals("前方一致")) {
							sqlCondition = "SELECT * FROM " + 表名 + " WHERE " + 字段
									+ " LIKE " + "'" + 查询内容 + "%' ";
						} else if (查询方式.equals("后方一致")) {
							sqlCondition = "SELECT * FROM " + 表名 + " WHERE " + 字段
									+ " LIKE " + "'%" + 查询内容 + "' ";
						} else if (查询方式.equals("中间包含")) {
							sqlCondition = "SELECT * FROM " + 表名 + " WHERE " + 字段
									+ " LIKE " + "'%" + 查询内容 + "%' ";
						}
						try {
//							得到结果集
							rs = stmt.executeQuery(sqlCondition);
							number = 0;//作为是否有查询结果的标志，也可以直接用rs是否为空来判断
							while (rs.next()) {
								number++;
								StringBuffer buff = new StringBuffer();
								for (int k = 1; k <= N; k++) {
									buff.append(rs.getString(k) + ",");
								}
								out.writeUTF("\n" + new String(buff));
							}
							if (number == 0)//或者用rs==null来判断
								out.writeUTF("没有查询到任何记录");
						} catch (SQLException ee) {
							out.writeUTF("" + ee);
						}
					}
				} catch (IOException e) {
					try {
						socket.close();
						DB.close(conn);
					} catch (Exception e1) {
					}
					System.out.println("客户离开了");
					break;
				}
			}
		}
	}
}
