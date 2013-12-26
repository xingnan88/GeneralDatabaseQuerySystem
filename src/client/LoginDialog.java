package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
/**
 * 登陆界面的具体实现
 */
class LoginDialog extends JFrame implements ActionListener {
	JTextField userName;
	JTextField password;
	JLabel jl1;
	JLabel jl2;
	JButton button_ok;
	JButton button_cancel;

	LoginDialog(String title){
		super(title);
		this.setLayout(new FlowLayout());
		
		userName = new JTextField(15);
		password = new JPasswordField(15);
		jl1 = new JLabel("用户名:",4);
		jl2 = new JLabel("密    码:",4);
		button_ok = new JButton("登录");
		button_cancel = new JButton("取消");
		
		this.add(jl1);
		this.add(userName);
		this.add(jl2);
		this.add(password);
		this.add(button_ok);
		this.add(button_cancel);
		
		button_ok.addActionListener(this);
		button_cancel.addActionListener(this);
		this.setResizable(false);
		this.setBounds(300, 300, 250,130);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == button_ok){
//			检测输入内容
			if(userName.getText().equals("")){
				JOptionPane.showMessageDialog(null, "请输入用户名!");
			}else if(password.getText().equals("")){
				JOptionPane.showMessageDialog(null, "请输入密码!");
			}
			boolean flag = false;
			String sign = "login";//设置登陆标志
			Socket s = null;
			DataOutputStream dos = null;
			DataInputStream dis = null;
			try {
				s = new Socket("localhost",6666);
				dos = new DataOutputStream(s.getOutputStream());
				dis = new DataInputStream(s.getInputStream());
				dos.writeUTF(sign);
				dos.writeUTF(userName.getText().trim());//write user name
				dos.writeUTF(password.getText().trim());//write password
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
			if(flag){
				this.setVisible(false);
				new Administrator();
			} else {
				JOptionPane.showMessageDialog(null,"登录失败!\n请检查你的用户名或密码是否正确 !");
			}
		} else if(e.getSource() == button_cancel){
			this.setVisible(false);
		}
	}
}
