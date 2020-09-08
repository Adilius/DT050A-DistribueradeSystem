package se.miun.distsys.messages;

public class ChatMessage extends Message {

	public String chat = "";	
	
	public ChatMessage(String chat) {
		this.chat = chat;
	}
}
