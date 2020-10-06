package se.miun.distsys;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.List;
import java.util.Map;

import se.miun.distsys.listeners.*;
import se.miun.distsys.messages.*;

public class GroupCommunication {
	
	private int datagramSocketPort = 1337;
	DatagramSocket datagramSocket = null;	
	boolean runGroupCommunication = true;
	MessageSerializer messageSerializer = new MessageSerializer();
	
	//Listeners
	ChatMessageListener chatMessageListener = null;
	JoinMessageListener joinMessageListener = null;
	LeaveMessageListener leaveMessageListener = null;
	ClientListMessageListener clientListMessageListener = null;

	public GroupCommunication() {
		try {
			runGroupCommunication = true;
			datagramSocket = new MulticastSocket(datagramSocketPort);
						
			ReceiveThread rt = new ReceiveThread();
			rt.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void shutdown(String username) {
		sendLeaveMessage(username);
		runGroupCommunication = false;
	}

	public void start(String username) {
		runGroupCommunication = true;
		sendJoinMessage(username);
	}
	

	class ReceiveThread extends Thread{
		
		@Override
		public void run() {
			byte[] buffer = new byte[65536];		
			DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
			
			while(runGroupCommunication) {
				try {
					datagramSocket.receive(datagramPacket);										
					byte[] packetData = datagramPacket.getData();					
					Message receivedMessage = messageSerializer.deserializeMessage(packetData);					
					handleMessage(receivedMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		//Handles incoming message into correct message type
		private void handleMessage (Message message) {

			//Chat message
			if(message instanceof ChatMessage) {				
				ChatMessage chatMessage = (ChatMessage) message;				
				if(chatMessageListener != null){
					chatMessageListener.onIncomingChatMessage(chatMessage);
				}
			//Join message
			}else if(message instanceof JoinMessage) {
				JoinMessage joinMessage = (JoinMessage) message;
				if(joinMessageListener != null){
					joinMessageListener.onIncomingJoinMessage(joinMessage);
				}
			//Leave message
			}else if(message instanceof LeaveMessage) {
				LeaveMessage leaveMessage = (LeaveMessage) message;
				if(leaveMessageListener != null){
					leaveMessageListener.onIncomingLeaveMessage(leaveMessage);
				}
			//Client list message
			}else if(message instanceof ClientListMessage){
				ClientListMessage clientListMessage = (ClientListMessage) message;
				if(clientListMessageListener != null){
					clientListMessageListener.onIncomingClientListMessage(clientListMessage);
				}
			//Unknown message type
			}else {
				System.out.println("Received unknown message type.");
			}			
		}		
	}	
	
	public void sendChatMessage(String username, String chat, Map<String, Integer> clientList) {
		try {
			ChatMessage chatMessage = new ChatMessage(username, chat, clientList);
			byte[] sendData = messageSerializer.serializeMessage(chatMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, 
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	public void sendJoinMessage(String username) {
		try {
			JoinMessage joinMessage = new JoinMessage(username);
			byte[] sendData = messageSerializer.serializeMessage(joinMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendLeaveMessage(String username) {
		try {
			LeaveMessage leaveMessage = new LeaveMessage(username);
			byte[] sendData = messageSerializer.serializeMessage(leaveMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendClientListMessage(Map<String, Integer> clientList) {
		try {
			ClientListMessage clientListMessage = new ClientListMessage(clientList);
			byte[] sendData = messageSerializer.serializeMessage(clientListMessage);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
					InetAddress.getByName("255.255.255.255"), datagramSocketPort);
			datagramSocket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setChatMessageListener(ChatMessageListener listener) {
		this.chatMessageListener = listener;
	}

	public void setJoinMessageListener(JoinMessageListener listener) {
		this.joinMessageListener = listener;
	}

	public void setLeaveMessageListener(LeaveMessageListener listener) {
		this.leaveMessageListener = listener;
	}

	public void setClientListMessageListener(ClientListMessageListener listener) {
		this.clientListMessageListener = listener;
	}
	
}
