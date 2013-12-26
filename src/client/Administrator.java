package client;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class Administrator extends JFrame implements ActionListener,WindowListener {
	JButton buttonAdd;
	JButton buttonDel;
	JButton buttonMod;
	JPanel panelCenter;
	
	Socket s = null;
	DataOutputStream dos = null;
	DataInputStream dis = null;
    /*
     * 管理界面初始化
     */
	public Administrator() {
		super("图书管理");
		this.setLayout(new BorderLayout());
		
		buttonAdd = new JButton(new ImageIcon("res/add.png"));
		buttonDel = new JButton(new ImageIcon("res/del.png"));
		buttonMod = new JButton(new ImageIcon("res/mod.png"));
		panelCenter = new JPanel();
		
		panelCenter.setLayout(new BorderLayout());
		JPanel panelWest = new JPanel();
		panelWest.setLayout(new GridLayout(3, 1, 0, 40));

//		设置JButton的外观
		buttonAdd.setMargin(new Insets(0, 0, 0, 0));
		buttonAdd.setHideActionText(true);
		buttonAdd.setFocusPainted(false);
		buttonAdd.setBorderPainted(false);
		buttonAdd.setContentAreaFilled(false);
		
		buttonDel.setMargin(new Insets(0, 0, 0, 0));
		buttonDel.setHideActionText(true);
		buttonDel.setFocusPainted(false);
		buttonDel.setBorderPainted(false);
		buttonDel.setContentAreaFilled(false);
		
		buttonMod.setMargin(new Insets(0, 0, 0, 0));
		buttonMod.setHideActionText(true);
		buttonMod.setFocusPainted(false);
		buttonMod.setBorderPainted(false);
		buttonMod.setContentAreaFilled(false);
		
		panelWest.add(buttonAdd);
		panelWest.add(buttonDel);
		panelWest.add(buttonMod);
		buttonAdd.addActionListener(this);
		buttonDel.addActionListener(this);
		buttonMod.addActionListener(this);
		getContentPane().add("West", panelWest);
		getContentPane().add("Center", panelCenter);
		this.setBounds(250, 300, 410, 300);
		this.addWindowListener(this);
		this.setResizable(false);
		this.setVisible(true);
	}
/*
 * (non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
	public void actionPerformed(ActionEvent e) {
//		移除Panel中的所有组件
		panelCenter.removeAll();

		if (e.getSource() == buttonAdd) {
			panelCenter.add(new Add());
			this.add("Center", panelCenter);
			this.setVisible(true);
		} else if (e.getSource() == buttonDel) {
			panelCenter.add(new Del());
			this.add("Center", panelCenter);
			this.setVisible(true);
		} else if (e.getSource() == buttonMod) {
			panelCenter.add(new Mod());
			this.add("Center", panelCenter);
			this.setVisible(true);
		}
	}

	/**
	 * 
	 * @author 唐东洋，冯秀清，陈刚
	 *修改图书模块
	 */
	@SuppressWarnings("serial")
	class Mod extends JPanel implements ActionListener {
		JLabel alabel;
		JTextField afield;
		JButton buttonMod;
		JButton buttonOk,buttonCancel;
		String[] str = { "编    号", "书   名", "作    者", "出版社", "日	    期", "价    格" };
		JLabel label[] = new JLabel[6];
		JTextField field[] = new JTextField[20];
/**
 * 初始化界面
 */
		public Mod() {
			alabel = new JLabel("请在下面第一个输入框中输入要修改书的编号:");
			afield = new JTextField(18);
			buttonMod = new JButton("查询");
			buttonOk = new JButton("修改");
			buttonCancel = new JButton("取消");
			this.add(alabel);
			this.add(buttonMod);
			this.add(afield);
			
			buttonMod.addActionListener(this);
			for (int i = 0; i < str.length; i++) {
				label[i] = new JLabel(str[i], JLabel.RIGHT);
				field[i] = new JTextField(20);
				this.add(label[i]);
				this.add(field[i]);
			}
			field[0].setEditable(false);//让编号不可编辑

			this.add(buttonOk);
			this.add(buttonCancel);
			buttonOk.addActionListener(this);
			buttonCancel.addActionListener(this);

		}
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonMod) {
				if (afield.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入要修改书的编号!");
				}
				/*
				 * 修改数据之前的查询操作
				 */
				String sign = "query";
				String str = null;
				try {
					s = new Socket("cng911.vicp.net", 6666);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
					dos.writeUTF(sign);// 写入操作标志
					dos.writeUTF(afield.getText().trim());// 向服务器端写入书本编号
					str = dis.readUTF();// 读取查询结果
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						if (dos != null) {
							dos.close();
							dos = null;
						}
						if (dis != null) {
							dis.close();
							dis = null;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				String ss[] = new String[7];//存储解析后的字符串
				StringTokenizer fenxi = new StringTokenizer(str, ",");// 解析字符串
				if (fenxi.hasMoreTokens())
					ss[0] = fenxi.nextToken();//查询标志，看是否有这条记录
				if (fenxi.hasMoreTokens())
					ss[1] = fenxi.nextToken();// 书编号
				if (fenxi.hasMoreTokens())
					ss[2] = fenxi.nextToken();// 书名
				if (fenxi.hasMoreTokens())
					ss[3] = fenxi.nextToken();// 作者
				if (fenxi.hasMoreTokens())
					ss[4] = fenxi.nextToken();// 出版社
				if (fenxi.hasMoreTokens())
					ss[5] = fenxi.nextToken();// 出版日期
				if (fenxi.hasMoreTokens())
					ss[6] = fenxi.nextToken();// 价格
				/*
				 * 若标志位ss[0]为false则提示没有查询到相关信息
				 * 并把之前的查询结果清空
				 */
				if (ss[0].equals("false")) {
					for (int i = 0; i < 6; i++) {
						field[i].setText("");
					}
					JOptionPane.showMessageDialog(null, "没有查到相关记录！");
					return;
				}
				/*
				 * 若存在查询结果则直接显示到各个文本框中
				 */
				for (int i = 0; i < 6; i++) {
					field[i].setText(ss[i+1]);
				}
			} else if (e.getSource() == buttonCancel) {
				this.setVisible(false);
//				下面是对输入内容的检测
			} else if (e.getSource() == buttonOk) {
				if (field[1].getText().trim().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入书名!");
				} else if (field[2].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入作者!");
				} else if (field[3].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入出版社!");
				} else if (field[4].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入日期!");
				} else if (field[5].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入价格!");
				}
//				利用正则表达式检测日期格式和价格
				if(!field[4].getText().trim().matches("\\d*+[-]\\d*+[-]\\d*")) {
					JOptionPane.showMessageDialog(null, "请按正确格式输入日期，例如：2009-11-23");
					return;
				}
				if(!field[5].getText().trim().matches("\\d++[.]?\\d*")) {
					JOptionPane.showMessageDialog(null, "请输入合理正确的价格，例如：23、85.8等等");
					return;
				}

				// 讲数据插入到数据库中，不允许修改ISBN
				/*
				 * 下面进行修改数据操作
				 */
				String sign = "mod";
				boolean flag = false;
				try {
					s = new Socket("cng911.vicp.net", 6666);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
					dos.writeUTF(sign);
					dos.writeUTF(field[0].getText().trim());//ISBN
					dos.writeUTF(field[1].getText().trim());//name
					dos.writeUTF(field[2].getText().trim());//author
					dos.writeUTF(field[3].getText().trim());//publisher
					dos.writeUTF(field[4].getText().trim());//date
					dos.writeDouble(Double.parseDouble(field[5].getText().trim()));//price
					flag = dis.readBoolean();
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						if (dos != null) {
							dos.close();
							dos = null;
						}
						if (dis != null) {
							dis.close();
							dis = null;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (!flag) {
					JOptionPane.showMessageDialog(null, "修改数据失败!请检查...");
				} else {
					for (int i = 0; i < 6; i++) {
						field[i].setText("");
					}
					JOptionPane.showMessageDialog(null, "修改数据成功!");
				}
			}
		}
	}
/**
 * 
 * @author 唐东洋，冯秀清，陈刚
 *删除图书
 */
	@SuppressWarnings("serial")
	class Del extends JPanel implements ActionListener {
		JLabel label;
		JTextField field;
		JButton buttonDel;
/*
 * 初始化删除界面
 */
		public Del() {
			label = new JLabel("请在下面文本框中输入要删除书的编号:");
			field = new JTextField(13);
			buttonDel = new JButton("删除");
			this.add(label);
			this.add(field);
			this.add(buttonDel);
			buttonDel.addActionListener(this);
		}
/*
 * (non-Javadoc)
 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
 */
		public void actionPerformed(ActionEvent e) {
//			判断输入是否为空
			if (field.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "请输入要删除书的编号!");
				return;
			}

			String sign = "del";//设置删除操作标志
			boolean flag = false;
			try {
				s = new Socket("cng911.vicp.net", 6666);
				dos = new DataOutputStream(s.getOutputStream());
				dis = new DataInputStream(s.getInputStream());
				dos.writeUTF(sign);//发送删除标志
				dos.writeUTF(field.getText().trim());//发送要删除的书的编号
				flag = dis.readBoolean();//读取操作是否成功的标志
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				try {
					if (dos != null) {
						dos.close();
						dos = null;
					}
					if (dis != null) {
						dis.close();
						dis = null;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (!flag) {
				JOptionPane.showMessageDialog(null, "没有找到你要删除的图书!");
			} else {
				field.setText("");
				JOptionPane.showMessageDialog(null, "删除数据成功!");
			}
		}
	}

	/**
	 * 
	 * @author 唐东洋，冯秀清，陈刚
	 *添加图书部分
	 */
	@SuppressWarnings("serial")
	class Add extends JPanel implements ActionListener {

		JButton buttonOK;
		JButton buttonCancel;
		String[] str = { "编   号", "书   名", "作    者", "出版社", "日    期", "价    格" };
		JLabel label[] = new JLabel[6];
		JTextField field[] = new JTextField[20];

		/*
		 * 初始化添加界面
		 */
		public Add() {

			buttonOK = new JButton("确定");
			buttonCancel = new JButton("取消");
			for (int i = 0; i < str.length; i++) {
				label[i] = new JLabel(str[i], JLabel.RIGHT);
				field[i] = new JTextField(20);
				this.add(label[i]);
				this.add(field[i]);
			}

			this.add(buttonOK);
			this.add(buttonCancel);
			buttonOK.addActionListener(this);
			buttonCancel.addActionListener(this);
		}

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonOK) {
//				对输入内容进行检测
				if (field[0].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入编号!");
				} else if (field[1].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入书名!");
				} else if (field[2].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入作者!");
				} else if (field[3].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入出版社!");
				} else if (field[4].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入日期!");
				} else if (field[5].getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入价格!");
				}
				
				if(!field[4].getText().trim().matches("\\d*+[-]\\d*+[-]\\d*")) {
					JOptionPane.showMessageDialog(null, "请按正确格式输入日期，例如：2009-11-23");
					return;
				}
				if(!field[5].getText().trim().matches("\\d++[.]?\\d*")) {
					JOptionPane.showMessageDialog(null, "请输入合理正确的价格，例如：23、85.8等等");
					return;
				}
				String sign = "add";
				boolean flag = false;
				try {
					s = new Socket("cng911.vicp.net",6666);
					dos = new DataOutputStream(s.getOutputStream());
					dis = new DataInputStream(s.getInputStream());
					dos.writeUTF(sign);
					dos.writeUTF(field[0].getText().trim());//ISBN
					dos.writeUTF(field[1].getText().trim());//name
					dos.writeUTF(field[2].getText().trim());//author
					dos.writeUTF(field[3].getText().trim());//publisher
					dos.writeUTF(field[4].getText().trim());//date
					dos.writeDouble(Double.parseDouble(field[5].getText().trim()));//price
					flag = dis.readBoolean();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {
					try {
						if(dos!=null) {
							dos.close();
							dos = null;
						}
						if(dis!=null) {
							dis.close();
							dis = null;
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				if (!flag) {
					JOptionPane.showMessageDialog(null, "增加数据失败!\n请检查...");
				} else {
					for (int i = 0; i < str.length; i++) {
						field[i].setText("");
					}
					JOptionPane.showMessageDialog(null, "增加数据成功!");
				}
			} else if (e.getSource() == buttonCancel) {
				this.setVisible(false);
			}
		}
	}
	
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
