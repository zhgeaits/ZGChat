package org.zhangge.newchat.common;

public class CommonUtil {

	public static int PORT = 52027;//设置服务器端口号
	public static String USERPASSWD_SPLIT = "%";//帐号和密码信息的分隔符
	public static String USERNAME_MARK = "username:";//登陆信息的用户名标志
	public static String PASSWORD_MARK = "password:";//登陆信息的密码标志
	public static String USER_LIST = "u%l:";//用户列表信息标志
	public static String WELCOME_MESSAGE = "********大家一起来欢迎新登陆用户^_^: ";//成功登陆的欢迎信息
	public static String DEFAULT_HOST = "127.0.0.1";//默认服务器地址
	public static String LOGIN_TITLE = "张戈即时聊天室登陆窗口";//登陆窗口标题
	public static String USERNAME = "登陆帐号：";//登陆帐号
	public static String PASSWORD = "登陆密码：";//登陆密码
	public static String HOSTSERVER = "服务器地址：";//服务器地址
	public static String SERVERPORT = "服务器端口：";//服务器端口
	public static String LOAD = "登陆";//登陆按钮
	public static String QUIT = "退出";//退出登陆按钮
	public static String MAIN_TITLE = "张戈即时聊天室聊天窗口";//聊天主窗口标题
	public static String ONLINE_USER = "当前在线用户列表....";//用户列表显示标题
	public static String TALKING_HISTORY = "聊天记录....";//聊天记录显示标题
	
	public static String SUCCEED_CONNECTE = "连接成功!\n现在开始验证帐号和密码!";//成功连接
	public static String COMFIRM_FAIL = "登陆失败，你的密码错误，请重新登陆.....";//验证失败信息
	public static String COMFIRM_FAIL_MARK = "passwordfail";//验证失败的返回标志
	public static String COMFIRM_SUCCESS = "成功登陆.....";//验证成功信息
	
	public static String ERROR_TITLE = "错误提示";//错误信息窗口标题
	public static String PORT_ILLEGAL = "输入非法端口，请重新输入1024-65535之间的任意一个端口....";//端口非法错误提示
	public static String IOEXCEPTION_MESSAGE = "未知IO错误，请重新再试试，不行马上与管理员联系！";//IOException错误提示
	public static String HOST_ERROR = "未知服务器地址，请重新填写后再次登陆.....";//服务器地址错误提示信息
	public static String PORT_ERROR = "端口输入错误，请重新输入....";//端口错误提示
	public static String NAMEPASSWORD_NULL = "帐号密码不能为空，请重新输入....";//登陆帐号密码非法
	public static String SOCKET_ERROR = "连接断开，请检查你的网络情况.....";//socketexception错误提示
}
