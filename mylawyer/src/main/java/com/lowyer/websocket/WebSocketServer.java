package com.lowyer.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@ServerEndpoint(value=("/webSocketServer/{username}"))
@Component
public class WebSocketServer {
	
	//private static final Set<WebSocketServer> connections = new CopyOnWriteArraySet<>();
	private static final Map<String, WebSocketServer> connections = new HashMap<>();
	
	private String nickname;
	private Session session;
	
	private static String getDatetime(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}
	
	@OnOpen
	public void start(@PathParam("username") String username, Session session) {
		this.nickname = username;
		this.session = session;
		connections.put(username, this);
		String message = String.format("* %s %s", nickname,"加入聊天");
		broadcast(message);
	}
	
	@OnClose
	public void end() {
		connections.remove(this.nickname);
		String message = String.format("* %s %s", nickname,"退出群聊");
		broadcast(message);
	}
	
	@OnMessage
	public void pushMsg(String message) {
		broadcast("【" + this.nickname + "】" + getDatetime(new Date()) + " : " + message);
	}
	
	@OnError
	public void onError(Throwable t) throws Throwable{
		
	}
	
	private static void broadcast(String msg) {
		for(String key : connections.keySet()) {
			WebSocketServer client  = null;
			client = (WebSocketServer) connections.get(key);
			try {
				if(client != null) {
					synchronized (client) {
						client.session.getBasicRemote().sendText(msg);
					}
				}else {
					System.out.println(key+"未建立连接");
				}
			}catch (IOException e) {
				connections.remove(client.nickname);
				try {
					client.session.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				String message = String.format("* %s %s", client.nickname,"断开连接");
				broadcast(message);
			}
		}
	}
	
	public void broadToOne(String msg,String user) {
		
		WebSocketServer client = (WebSocketServer) connections.get(user);
		try {
			if(client != null) {
				synchronized (client) {
					client.session.getBasicRemote().sendText(msg);
				}
			}else {
				System.out.println(user+"未建立连接");
			}
		}catch (IOException e) {
			connections.remove(client.nickname);
			try {
				client.session.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String message = String.format("* %s %s", client.nickname,"断开连接");
			broadcast(message);
		}
	}

}
