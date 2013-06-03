package org.zhangge.newchat.client;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.zhangge.newchat.common.CommonUtil;

public class LoginChat extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	
	//登陆窗口的组建
	private JLabel UserName = new JLabel(CommonUtil.USERNAME);
	private JTextField  Name = new JTextField();
	private JLabel UserPaw = new JLabel (CommonUtil.PASSWORD);
	private JPasswordField Paw = new JPasswordField();
	private JLabel ServerHost = new JLabel(CommonUtil.HOSTSERVER);
	private JTextField Host = new JTextField();
	private JLabel ServerPort = new JLabel(CommonUtil.SERVERPORT);
	private JTextField Port = new JTextField();
	private JButton Load = new JButton(CommonUtil.LOAD);
	private JButton Quit = new JButton(CommonUtil.QUIT);
	
	//连接参数
	private Socket s=null;
	private DataOutputStream dos=null;
	private DataInputStream dis=null;
	private boolean bconnected=false;
	
	private String message;
	private String host;
	private int port;

	/**
	 * @Title:LoadFrame
	 * @Description:TODO 登陆窗口，启动的入口
	 * @param 
	 * @return void
	 * @throws
	 */
	public void loadFrame() {
		this.setTitle(CommonUtil.LOGIN_TITLE);
		Container c = this.getContentPane();
		this.setLayout(null);
		this.setBounds(400, 300, 350, 350);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		UserName.setBounds(50, 40, 100, 20);
		c.add(UserName);
		Name.setBounds(150, 40, 120, 20);
		c.add(Name);
		
		UserPaw.setBounds(50, 90, 100, 20);
		c.add(UserPaw);
		Paw.setBounds(150, 90, 120, 20);
		c.add(Paw);
		
		ServerHost.setBounds(50, 140, 100, 20);
		c.add(ServerHost);
		Host.setBounds(150, 140, 120, 20);
		c.add(Host);
		
		ServerPort.setBounds(50, 190, 100, 20);
		c.add(ServerPort);
		Port.setBounds(150, 190, 120, 20);
		c.add(Port);
		
		Load.setBounds(50,250,80,40);
		c.add(Load);
		Quit.setBounds(190,250,80,40);
		c.add(Quit);
		Load.addActionListener(this);
		Quit.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == Load) {
			String name = Name.getText();
			name.trim();
			String passwd = new String(Paw.getPassword());
			passwd.trim();
			if (name.length() == 0 || passwd.length() == 0) {
				this.showReturnMessage(CommonUtil.NAMEPASSWORD_NULL);
			} else {
				message = CommonUtil.USERNAME_MARK + name + CommonUtil.USERPASSWD_SPLIT + CommonUtil.PASSWORD_MARK + passwd;
				try {
					host = Host.getText();
					port = Integer.valueOf(Port.getText());
					boolean connect = this.connect(host, port);//与服务器进行连接
					if (connect) {
						this.setVisible(false);//隐藏登陆窗口
						MainChat mc = new MainChat(dis, dos);
						mc.launchFrame();//打开聊天主窗口
						new Thread(mc).start();//启动线程，获取聊天内容和在线用户列表
					}
				} catch (NumberFormatException e1) {
					this.showReturnMessage(CommonUtil.PORT_ILLEGAL);
				}
			}
 		} else if (e.getSource() == Quit) {
			System.exit(0);
		}
	}
	
	/**
	 * @Title:connect
	 * @Description:TODO 与服务器进行连接
	 * @param 
	 * @return void
	 * @throws
	 */
	public boolean connect(String host, int port){
		try {
			if (host == null || host.equals("")) {
				host = CommonUtil.DEFAULT_HOST;
			}
			s=new Socket(host, port);
			dos=new DataOutputStream(s.getOutputStream());
			dis=new DataInputStream(s.getInputStream());
System.out.println(CommonUtil.SUCCEED_CONNECTE);
			dos.writeUTF(message);//发送帐号密码信息
			String conmsg = dis.readUTF();
			if (conmsg.equals(CommonUtil.COMFIRM_FAIL_MARK)) {
				this.showReturnMessage(CommonUtil.COMFIRM_FAIL);
				bconnected = false;
			} else {
System.out.println(CommonUtil.COMFIRM_SUCCESS);
				bconnected = true;
			}
		} catch(IllegalArgumentException e) {
			this.showReturnMessage(CommonUtil.PORT_ILLEGAL);
		} catch (UnknownHostException e) {
			this.showReturnMessage(CommonUtil.HOST_ERROR);
		} catch(SocketException e) {
			this.showReturnMessage(CommonUtil.PORT_ERROR);
		} catch (IOException e) {
			this.showReturnMessage(CommonUtil.IOEXCEPTION_MESSAGE);
			System.exit(0);
		}
		return bconnected;
	}
	
	/**
	 * @param message
	 *  显示反馈信息
	 */
	public void showReturnMessage(String message) {
		JOptionPane.showMessageDialog(null, message, CommonUtil.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}
}
