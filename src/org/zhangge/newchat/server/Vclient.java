package org.zhangge.newchat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.zhangge.newchat.common.CommonUtil;

public class Vclient implements Runnable {

	private List<Vclient> clients;
	private Socket s;//连接的套接字
	private DataInputStream dis=null;
	private DataOutputStream dos=null;
	private boolean beConnected=false;//判断连接的标志
	private String name;//用户名
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Vclient(Socket s, DataInputStream br, DataOutputStream ds, List<Vclient> clients){
		this.s = s;
		dis=br;
		dos=ds;
		beConnected=true;
		this.clients = clients;
	}
	
	/**
	 * @param message
	 * 往客户端发送数据
	 */
	public void sendMessage(StringBuilder message){
		try {
			for (int i=0;i<clients.size();i++){
				Vclient c=clients.get(i);
				c.dos.writeUTF(message.toString());
			}
		} catch (NullPointerException e) {
			System.out.println("发送信息失败");
		} catch (IOException e) {
			System.out.println("发送失败！");
		}
	}
	
	/**
	 * 关闭连接
	 */
	public void close() {
		clients.remove(this);
		StringBuilder offlineMsg = new StringBuilder();
		offlineMsg.append(name).append(" has gone!");
		this.sendMessage(offlineMsg);
		this.sendUserList();
		try {
			if (dis != null) dis.close();
			if (dos != null) dos.close();
			if (s != null) s.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * 往每个用户发送在线用户链表
	 */
	public void sendUserList() {
		StringBuilder nameList = new StringBuilder();//用于存放在线用户列表
		nameList.append(CommonUtil.USER_LIST);
		for (int i = 0; i < clients.size(); i++) {
			Vclient c = clients.get(i);
			nameList.append(c.getName()).append("\n");
		}
		sendMessage(nameList);
	}
	
	public void run() {
		try{ 
			String message = null;//客户端发送过来的内容，服务器转发给所有用户
			StringBuilder welMsg = new StringBuilder();
			welMsg.append(CommonUtil.WELCOME_MESSAGE).append(name).append("********");
			sendMessage(welMsg);//第一次登陆，发送欢迎信息
			sendUserList();//发送新的用户列表
			while (beConnected){
				message=dis.readUTF();//读取客户端的信息
				DateFormat d1 = DateFormat.getDateTimeInstance();//发送聊天信息
				StringBuilder chatMsg = new StringBuilder();
				chatMsg.append("----------").append(name).append(" ").append(d1.format(new Date())).append(" : \n ").append(message);
				sendMessage(chatMsg);
			}
		}catch(SocketException e){
			System.out.println("a client quit!");
		}catch(EOFException e){
			System.out.println("Client closed!");
		}catch (IOException e) {
			System.out.println("发生未知IO错误！");
		}finally{//发生任何exception都要响应地对虚拟客户端做出响应
			this.close();
		}
	}
}
