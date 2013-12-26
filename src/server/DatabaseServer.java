package server;

import java.net.*;
import java.io.*;
/**
 * 
 * @author 冯秀清、唐东洋、陈刚
 *服务器端
 */
public class DatabaseServer {
	static ServerSocket server;
	static Socket serverThread;
	static InetAddress yourAddress;

	public DatabaseServer() {
		System.out.println("我是服务器端程序,负责处理用户的连接请求");
	}
	
	public static void main(String args[]) {
		while (true) {
			try {
				server = new ServerSocket(6666);//监听端口
			} catch (IOException e1) {
				System.out.println("正在监听:");
			}
			try {
				System.out.println("等待用户呼叫.");
				serverThread = server.accept();//接受客户端
				yourAddress = serverThread.getInetAddress();//得到客户端IP
				System.out.println("客户的IP:" + yourAddress);
			} catch (IOException e) {
			}
			if (serverThread != null) {
				new ServerThread(serverThread).start();//启动服务器端线程
			} else
				continue;
		}
	}
}
