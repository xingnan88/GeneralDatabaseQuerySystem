package client;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class DatabaseClient extends JFrame implements Runnable, ActionListener {
	String formName; // 数据库中的表名
	DefaultTableModel dtm;
	Label label1,label2;
	TextField 输入查询内容;
	Choice choice;
	Checkbox 完全一致, 前方一致, 后方一致, 中间包含;
	CheckboxGroup group = null;
	Button 查询;
	JTable table;
	JScrollPane jsp;
	Label 提示条;
	Socket socket = null;
	DataInputStream in = null;
	DataOutputStream out = null;
	static Thread thread;
	boolean ok = false;
	int N = 0; // 字段个数
	String[] ziduanName = { "ISBN", "name", "author", "publisher", "date", "price" }; // 存放字段名字的数组
	MenuBar mb;
	MenuItem mi;
	Object[][] obj = {{" ", " ", " ", " ", " ", " "}};//用于初始化JTable行值
	
	public DatabaseClient() {

		super("查阅");
		setBounds(200, 100, 500, 600);
		setResizable(false);
		setBackground(Color.white);
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		第一个Panel中的组件
		label1 = new Label("输入查询内容:", Label.CENTER);
		输入查询内容 = new TextField(19);
		choice = new Choice();
		for (int j = 0; j < ziduanName.length; j++) {
			choice.add(ziduanName[j]);
		}
		N = choice.getItemCount();
		查询 = new Button("查询");
		
//		第二个Panel中的组件
		label2 = new Label("选择查询条件:", Label.CENTER);
		group = new CheckboxGroup();
		完全一致 = new Checkbox("完全一致", true, group);
		前方一致 = new Checkbox("前方一致", false, group);
		后方一致 = new Checkbox("后方一致", false, group);
		中间包含 = new Checkbox("中间包含", false, group);
		
//		第三个Panel中的组件
		dtm = new DefaultTableModel(obj,ziduanName);  
		table = new JTable(dtm);
		jsp = new JScrollPane(table);
		
//		添加菜单栏
		mb = new MenuBar();
		setMenuBar(mb);
		Menu menu = new Menu("管理员登陆");
		mb.add(menu);
		mi = new MenuItem("登陆");
		menu.add(mi);
		mi.addActionListener(this);
//		数据库中表名
		formName = "bookform";

		提示条 = new Label("正在连接到服务器,请稍等...", Label.CENTER);
		提示条.setForeground(Color.red);
		提示条.setFont(new Font("TimesRoman", Font.BOLD, 24));
//		第一个Panel
		Panel box1 = new Panel();
		box1.add(label1);
		box1.add(输入查询内容);
		box1.add(choice);
		box1.add(查询);
		查询.addActionListener(this);
		add(box1);
//		第二个Panel
		Panel box2 = new Panel();
		box2.add(label2);
		box2.add(完全一致);
		box2.add(前方一致);
		box2.add(后方一致);
		box2.add(中间包含);
		add(box2);
		
//		第三个Panel
		Panel box3 = new Panel();
		box3.add(jsp);
		add(box3);
		
		add(提示条);//提示连接数据库是否成功

		setVisible(true);
		conn();
	}
/*
 * 连接服务器
 */
	public void conn() {
		ok = true;
		try {
			socket = new Socket("localhost", 6666);
			in = new DataInputStream(socket.getInputStream());//得到输入流
			out = new DataOutputStream(socket.getOutputStream());//得到输出流
		} catch (IOException ee) {
			提示条.setText("连接失败");
			this.close();
		}
		if (socket != null) {
			提示条.setText("连接成功");
		}
	}
/*
 * 关闭与服务器的连接
 */
	public void close() {
		ok = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
 * (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
	public void run() {
		dtm.removeRow(0);
		String s = null;
		while (true) {
			try {
				s = in.readUTF();
				if(s.equals("没有查询到任何记录")) {
					try {
						Thread.sleep(1000);//为了不让查询语音和这个语音重叠
						Applet.newAudioClip(this.getClass().getResource("没有查询到任何记录.wav")).play();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					String ss[] = new String[6];
					StringTokenizer fenxi = new StringTokenizer(s, ",");// 解析字符串
					if (fenxi.hasMoreTokens())
						ss[0] = fenxi.nextToken();// 书编号
					if (fenxi.hasMoreTokens())
						ss[1] = fenxi.nextToken();// 书名
					if (fenxi.hasMoreTokens())
						ss[2] = fenxi.nextToken();// 作者
					if (fenxi.hasMoreTokens())
						ss[3] = fenxi.nextToken();// 出版社
					if (fenxi.hasMoreTokens())
						ss[4] = fenxi.nextToken();// 出版日期
					if (fenxi.hasMoreTokens())
						ss[5] = fenxi.nextToken();// 价格
					Object[] rowData = {ss[0], ss[1], ss[2], ss[3], ss[4], ss[5] };
					dtm.addRow(rowData);//增加一行
					if (ok == false)
						break;
				}
			} catch (IOException e) {
				提示条.setText("与服务器已断开");
				this.close();
				break;
			}
		}
	}
/*
 * (non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == 查询) {
			Applet.newAudioClip(this.getClass().getResource("查询.wav")).play();
//			删除之前的查询结果，准备显示新的查询结果
			for(int i=dtm.getRowCount()-1; i>-1; i--) {
				dtm.removeRow(i);
			}
			String 查询内容 = "";
			查询内容 = 输入查询内容.getText();
			String 字段 = (String) choice.getSelectedItem();
			String 查询方式 = group.getSelectedCheckbox().getLabel();
//			往服务器端写入查询个要素
			if (查询内容.length() > 0) {
				try {
					out.writeUTF("字段个数:" + N);
					out.writeUTF(formName + ":" + 查询内容 + ":" + 字段 + ":" + 查询方式);
				} catch (IOException e1) {
					提示条.setText("与服务器已断开");
				}
			} else
				输入查询内容.setText("请输入内容");
		} else if ((MenuItem) e.getSource() == mi) {
//			登陆事件
			Applet.newAudioClip(this.getClass().getResource("登陆.wav")).play();
//			初始化登陆框
			new LoginDialog("登陆");
		}
	}
/*
 * 程序运行入口
 */
	public static void main(String[] args) {
		thread = new Thread(new DatabaseClient());
		thread.start();
	}
}
