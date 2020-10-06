package se.miun.distsys.messages;

import java.util.HashMap;
import java.util.Map;

public class ChatMessage extends Message {

	public String username = "";
	public String chat = "";
	public Map<String, Integer> clientList = new HashMap<String, Integer>();
	
	public ChatMessage(String username, String chat, Map<String, Integer> clientList) {
		this.username = username;
		this.chat = chat;
		this.clientList = clientList;
	}
}
