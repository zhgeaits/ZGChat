package org.zhangge.newchat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zhangge.newchat.common.CommonUtil;

/**
 * @author zhangge
 * 登陆策略：帐号信息存放在内存里面，用户第一次登陆后注册，以后登陆需要记住密码
 */
public class ServerChat {
	
	private boolean started=false;//判断服务器启动状态
	private ServerSocket ss=null;//服务器的套接字
	private DataInputStream inFromClient = null; //从上面的TCP接口打开一个输入流
	private DataOutputStream outToClient = null;//从TCP接口打开一个输出流
	private Map<String, String> users = new HashMap<String, String>();//用于存放用户的帐号密码
	private List<Vclient> clients = new ArrayList<Vclient>();//用于存放用户的链表
	
	public static void main(String[] args){
		if (args.length != 0) {
			CommonUtil.PORT = Integer.valueOf(args[0]);
		}
		new ServerChat().start();
	}
	
	/**
	 * @Title:start
	 * @Description:TODO 启动服务端
	 * @param 
	 * @return void
	 * @throws
	 */
	public void start(){
		try{
			ss=new ServerSocket(CommonUtil.PORT);//打开套接字
			started =true;//设置启动标志
			while(started){
				 Socket s = ss.accept();//等待一个客户上线，并且读取登陆信息
				 inFromClient = new DataInputStream(s.getInputStream());
				 outToClient = new DataOutputStream(s.getOutputStream());
				 String message = inFromClient.readUTF();
				 String[] userpassword = message.split(CommonUtil.USERPASSWD_SPLIT);//分离帐号和密码信息
					
				 String username = userpassword[0].substring(CommonUtil.USERNAME_MARK.length());
				 String password = userpassword[1].substring(CommonUtil.PASSWORD_MARK.length());
				 
				 String passwd = users.get(username);
				 if (passwd != null && !password.equals(passwd)) {
					 outToClient.writeUTF(CommonUtil.COMFIRM_FAIL_MARK);
					 s.close();
				 } else {
					 outToClient.writeUTF(CommonUtil.COMFIRM_SUCCESS);
					 users.put(username, password);
					 Vclient c=new Vclient(s, inFromClient, outToClient, clients);
					 c.setName(username);
					 new Thread(c).start();
					 clients.add(c);
				 }
			}
		} catch(BindException e){
			System.out.println("端口使用中.....");
			System.out.println("请关掉相关程序并重新运行服务器！");
			started = false;
			System.exit(0);
		} catch (EOFException e){
			System.out.println("Client has closed!");
		} catch(IOException e){
			System.out.println("打开socket时发生 I/O 错误.....");
			System.out.println("请关掉相关程序并重新运行服务器！");
			started = false;
			System.exit(0);
		}  finally{
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
