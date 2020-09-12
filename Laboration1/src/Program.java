import java.io.BufferedReader;
import java.io.InputStreamReader;

import se.miun.distsys.GroupCommunication;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.messages.ChatMessage;

//Skeleton code for Distributed systems 9hp, DT050A

public class Program implements ChatMessageListener{

	boolean runProgram = true;
	
	GroupCommunication gc = null;
	
	public static void main(String[] args) {
		Program program = new Program();
	}
		
	public Program() {
		gc = new GroupCommunication();
		gc.setChatMessageListener(this);
		System.out.println("Group Communcation Started");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		
		while(runProgram) {			
			try {
				
				System.out.println("Write message to send: ");	
				String chat = br.readLine();			
				gc.sendChatMessage(chat);
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		gc.shutdown();
	}

	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {		
		System.out.println("Incoming chat message: " + chatMessage.chat);	
	}
}