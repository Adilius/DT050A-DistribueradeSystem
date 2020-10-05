package se.miun.distsys.messages;

public class ChatMessage extends Message {

	public String chat = "";
	public String username = "";
	
	public ChatMessage(String username, String chat) {
		this.username = username;
		this.chat = chat;
	}
}
