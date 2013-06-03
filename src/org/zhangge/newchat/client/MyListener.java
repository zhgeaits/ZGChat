package org.zhangge.newchat.client;

import java.awt.TextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;

public class MyListener extends KeyAdapter {

	private TextArea tfTxt;
	private DataOutputStream dos;
	
	public MyListener(TextArea tfTxt, DataOutputStream dos) {
		this.tfTxt = tfTxt;
		this.dos = dos;
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
			String str = tfTxt.getText().trim();//获取输入框的内容
			tfTxt.setText("");
			try {
				dos.writeUTF(str);//往流里写，发到服务器
				dos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
