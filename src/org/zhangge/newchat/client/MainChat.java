package org.zhangge.newchat.client;

import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.zhangge.newchat.common.CommonUtil;

public class MainChat extends JFrame implements Runnable {

	private static final long serialVersionUID = 1720361181828968073L;
	private TextArea tfTxt =new TextArea();
	private JLabel content = new JLabel(CommonUtil.TALKING_HISTORY);
	private TextArea taContent=new TextArea();
	private JLabel list = new JLabel(CommonUtil.ONLINE_USER);
	private TextArea talkList = new TextArea();
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean beConnected;
	
	public MainChat(DataInputStream dis, DataOutputStream dos) {
		this.dis = dis;
		this.dos = dos;
		this.beConnected = true;
	}
	/**
	 * @Title:launchFrame
	 * @Description:TODO 启动聊天主窗口
	 * @param 
	 * @return void
	 * @throws
	 */
	public void launchFrame() {
		this.setTitle(CommonUtil.MAIN_TITLE);
		setBounds(100,100,650,600);
		this.setLayout(null);
		list.setBounds(50, 30, 200, 20);
		this.add(list);
		talkList.setBounds(10, 70, 170, 400);
		this.add(talkList);
		content.setBounds(250, 30, 200, 20);
		this.add(content);
		taContent.setBounds(220, 70, 400, 400);
		this.add(taContent);
		tfTxt.setBounds(10, 480, 600, 100);
		this.add(tfTxt);
		setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					beConnected = false;
					dis.close();
					dos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		tfTxt.addKeyListener(new MyListener(tfTxt, dos));
	}

	public void run() {
		try{
			while (beConnected){
				String str=dis.readUTF();//读取服务器的信息
				if (str.length() >= CommonUtil.USER_LIST.length() && str.substring(0, CommonUtil.USER_LIST.length()).equals(CommonUtil.USER_LIST)) {
					String userList = str.substring(CommonUtil.USER_LIST.length());//更新用户列表
					talkList.setText(userList);
				} else {
					taContent.setText(taContent.getText()+str+'\n');//更新聊天内容
				}
			}
		} catch (SocketException e) {
			this.showReturnMessage(CommonUtil.SOCKET_ERROR);
		} catch (EOFException e) {
			this.showReturnMessage(CommonUtil.SOCKET_ERROR);
		} catch(IOException e){
			this.showReturnMessage(CommonUtil.IOEXCEPTION_MESSAGE);
		} 
	}
	
	/**
	 * @param message
	 *  显示反馈信息
	 */
	public void showReturnMessage(String message) {
		JOptionPane.showMessageDialog(null, message, CommonUtil.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
	}
}
