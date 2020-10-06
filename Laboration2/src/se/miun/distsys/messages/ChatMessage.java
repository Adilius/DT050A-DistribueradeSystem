package se.miun.distsys.messages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChatMessage extends Message {

	public String username = "";
	public String chat = "";
	public LinkedHashMap<String, Integer> clientList = new LinkedHashMap<String, Integer>();
	
	public ChatMessage(String username, String chat, LinkedHashMap<String, Integer> clientList) {
		this.username = username;
		this.chat = chat;
		this.clientList = clientList;
	}
}
