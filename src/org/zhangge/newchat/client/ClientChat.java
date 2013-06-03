package org.zhangge.newchat.client;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientChat {

	public static void main(String[] args) throws UnknownHostException, IOException {
		new LoginChat().loadFrame();
	}
}
