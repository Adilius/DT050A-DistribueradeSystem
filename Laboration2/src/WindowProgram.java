import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import se.miun.distsys.GroupCommunication;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.ClientListMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.ClientListMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

//Skeleton code for Distributed systems 9hp, DT050A
//Adil Aboulkacim (adab1600)

public class WindowProgram implements ChatMessageListener, JoinMessageListener, LeaveMessageListener, ClientListMessageListener, ActionListener {

    JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnClientList = new JTextPane();
	JTextPane txtpnCurrentClient = new JTextPane();
	public static Integer selectedClient = 0;

    GroupCommunication gc;

    public static String username = "Unknown";
    public static LinkedHashMap<String, Integer> clientList = new LinkedHashMap<String, Integer>();

	public static void main(String[] args) {
	    username = getUsername();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowProgram window = new WindowProgram();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public WindowProgram() {
		initializeFrame();

		gc = new GroupCommunication();
		gc.setChatMessageListener(this);
		gc.setJoinMessageListener(this);
		gc.setLeaveMessageListener(this);
		gc.setClientListMessageListener(this);
		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {

		//Set base frame
		frame = new JFrame();
		frame.setTitle("LAN SPEAK: " + username);
		try {
			frame.setIconImage(ImageIO.read(new File("images/icon.png")));
		} catch(IOException e) {
			e.printStackTrace();
		}
		frame.setBounds(100, 100, 700, 900);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		//Set incoming text pane
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(txtpnChat);
		txtpnChat.setEditable(false);

		//Set outgoing text pane
		txtpnMessage.setText("");
		frame.getContentPane().add(txtpnMessage);

		//Set send button
		JButton btnSendChatMessage = new JButton("Send Chat Message");
		btnSendChatMessage.addActionListener(this);
		btnSendChatMessage.setActionCommand("send");
		frame.getContentPane().add(btnSendChatMessage);

		//Set clients text pane
		JScrollPane scrollPaneClients = new JScrollPane();
		frame.getContentPane().add(scrollPaneClients);
		scrollPaneClients.setViewportView(txtpnClientList);
		txtpnClientList.setEditable(false);

		//Set text and buttons for vector clock manipulation
		frame.getContentPane().add(txtpnCurrentClient);
		txtpnCurrentClient.setText("0");
		txtpnCurrentClient.setEditable(false);

		JButton selectUp = new JButton("↑");
		selectUp.addActionListener(this);
		selectUp.setActionCommand("selectUp");
		frame.getContentPane().add(selectUp);

		JButton selectDown = new JButton("↓");
		selectDown.addActionListener(this);
		selectDown.setActionCommand("selectDown");
		frame.getContentPane().add(selectDown);

		JButton clockIncrement = new JButton("+");
		clockIncrement.addActionListener(this);
		clockIncrement.setActionCommand("clockIncrement");
		frame.getContentPane().add(clockIncrement);

		JButton clockDecrement = new JButton("-");
		clockDecrement.addActionListener(this);
		clockDecrement.setActionCommand("clockDecrement");
		frame.getContentPane().add(clockDecrement);


		//Program shutdown
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	        	gc.sendChatMessage(username, " has left.", clientList);
	            gc.shutdown(username);
	        }
	    });

		//Program start
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(WindowEvent winEvt){
				//Send initial messages to register on network and get clients
				gc.start(username);

				//Wait for client list to catch up
				while(clientList.size()<1){
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("hello");
				gc.sendChatMessage(username, " has joined.", clientList);
			}
		});
	}

	//Get unique username for this client
	public static String getUsername() {
		//Create random ID for client
		Random rand = new Random();
		int randID = rand.nextInt(10000);

		//Get computer name if possible
		Map<String, String> env = System.getenv();
		String computerName;
		if (env.containsKey("COMPUTERNAME")){
			computerName = env.get("COMPUTERNAME");
		}
		else computerName = env.getOrDefault("HOSTNAME", "Unknown");

		//Return username
		return (computerName + "_" + randID);
	}

	//Updates from client list array
	public void updateClientList(){
		txtpnClientList.setText("");

		Integer counter = 0;
		for(Map.Entry<String, Integer> entry : clientList.entrySet()){
			txtpnClientList.setText(counter + ". " + entry.getKey() + ": " + entry.getValue() + "\n" + txtpnClientList.getText());
			counter++;
		}
		txtpnClientList.setText("---- Client list ----" + "\n" + txtpnClientList.getText());
	}

	//Reset selected client
	public void resetSelectedClient(){
		selectedClient = 0;
		updateSelectedClient();
	}

	//Updates selected client
	public void updateSelectedClient(){
		txtpnCurrentClient.setText(selectedClient.toString());
	}



	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
		    clientList.put(username, clientList.get(username) +1);
			gc.sendChatMessage(username ,  txtpnMessage.getText(), clientList);
			updateClientList();
			txtpnMessage.setText("");
		}

		if (event.getActionCommand().equalsIgnoreCase("selectUp")){
			if(selectedClient<clientList.size()-1){
				selectedClient++;
			}
			updateSelectedClient();
		}

		if (event.getActionCommand().equalsIgnoreCase("selectDown")){
			if(selectedClient != 0) {
				selectedClient--;
			}
			updateSelectedClient();
		}

		if(event.getActionCommand().equalsIgnoreCase("clockIncrement")){
			Integer counter = 0;
			for(Map.Entry<String, Integer> entry : clientList.entrySet()){
				if(counter == selectedClient){
					String selectedKey = entry.getKey();
					Integer selectedValue = entry.getValue();
					selectedValue++;
					clientList.put(selectedKey, selectedValue);
					updateClientList();
					break;
				}else{
					counter++;
				}
			}
		}

		if(event.getActionCommand().equalsIgnoreCase("clockDecrement")){
			Integer counter = 0;
			for(Map.Entry<String, Integer> entry : clientList.entrySet()){
				if(counter == selectedClient){
					String selectedKey = entry.getKey();
					Integer selectedValue = entry.getValue();
					selectedValue--;
					clientList.put(selectedKey, selectedValue);
					updateClientList();
					break;
				}else{
					counter++;
				}
			}
		}
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {
		//Now we check that the client list is correct size
		if(clientList.size() == chatMessage.clientList.size()){

			//Check if our client list is only 1 behind the received client list vector
			Integer localSum = 0;
			for(Integer value : clientList.values()){ localSum += value;}
			Integer receivedSum = 0;
			for(Integer value : chatMessage.clientList.values()){ receivedSum += value; }
			System.out.println("Local sum: " + localSum);
			System.out.println("Received sum: " + receivedSum);

			//If we are only behind one message or the same, then we good
			if(localSum == receivedSum || localSum + 1 == receivedSum){
				txtpnChat.setText(
						chatMessage.username + ":" + chatMessage.chat + " "
								//+ chatMessage.clientList
								+ "\n" + txtpnChat.getText());
				clientList = chatMessage.clientList;	//Adopt new client list vector clock
				updateClientList();
			}else{
				System.out.println("Client list vector clock sum error");
			}



		}else{
			System.out.println("Error: Client list mismatch. Received: " + chatMessage.clientList.size() + " Local: " + clientList.size());
			txtpnChat.setText("Error: Client list mismatch" + "\n" + txtpnChat.getText());
		}
	}

	@Override
	public void onIncomingJoinMessage(JoinMessage joinMessage) {
		//If we already have this user
		if(clientList.containsKey(joinMessage.username)){
			System.out.println("Duplicate join message.");
		//Else add it, update list, send back client list
		}else{
			clientList.put(joinMessage.username, 0);
			updateClientList();
			gc.sendClientListMessage(clientList);
		}
	}

	@Override
	public void onIncomingLeaveMessage(LeaveMessage leaveMessage) {
		//If we have this user on our list
		if(clientList.containsKey(leaveMessage.username)){
			clientList.remove(leaveMessage.username);
			updateClientList();
			resetSelectedClient();
		}else{
			System.out.println("Unknown leave message. ");
		}
	}

	@Override
	public void onIncomingClientListMessage(ClientListMessage clientListMessage) {
		//If new incoming list has more users than ours
		if(clientList.size() < clientListMessage.clientList.size()){
			clientList = clientListMessage.clientList;
			updateClientList();
		}
	}
}
