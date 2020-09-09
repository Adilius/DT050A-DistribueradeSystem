import java.awt.EventQueue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.messages.ChatMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.regex.Pattern;

//Skeleton code for Distributed systems 9hp, DT050A

public class WindowProgram implements ChatMessageListener, ActionListener {

	JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnClients = new JTextPane();
	
	GroupCommuncation gc = null;

	ArrayList<String> ClientList = new ArrayList<String>();

	String JoinMessage = new String(" |joined the group|");
	String LeftMessage = new String(" |left the group|");
	String ClientsMessage = new String(" |Clients|");
	String ClientsDelimiter = new String("&/?"); //Divides the clients

	public static void main(String[] args) {
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
		gc = new GroupCommuncation();
		gc.setChatMessageListener(this);
		initializeFrame();

		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {

		//Set base frame
		frame = new JFrame();
		frame.setTitle("LAN SP3AK - " + gc.getComputerName());
		//Set icon
		try {
			frame.setIconImage(ImageIO.read(new File("Images/icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setBounds(100, 100, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));


		//Set incoming text pane
		JScrollPane scrollPaneText = new JScrollPane();
		frame.getContentPane().add(scrollPaneText);
		scrollPaneText.setViewportView(txtpnChat);
		txtpnChat.setEditable(false);	
		txtpnChat.setText("--== Group Chat ==--");

		//Set outgoing text pane
		frame.getContentPane().add(txtpnMessage);

		//Set send button
		JButton btnSendChatMessage = new JButton("Send Chat Message");
		btnSendChatMessage.addActionListener(this);
		btnSendChatMessage.setActionCommand("send");
		frame.getContentPane().add(btnSendChatMessage);

		//Set clients text pane
		JScrollPane scrollPaneClients = new JScrollPane();
		frame.getContentPane().add(scrollPaneClients);
		scrollPaneClients.setViewportView(txtpnClients);
		txtpnClients.setEditable(false);

		//Window close
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
				gc.sendChatMessage(LeftMessage, true);
	            gc.shutdown();
	        }
	    });

		//Window open
		frame.addWindowListener(new java.awt.event.WindowAdapter(){
			public void windowOpened(WindowEvent winEvt){
				gc.sendChatMessage(JoinMessage, true);
			}
		});
	}

	//Send message action
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.sendChatMessage(txtpnMessage.getText() , false);
		}		
	}

	//Get message action
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {
		//If join message detected and is valid
		if(chatMessage.chat.contains(JoinMessage) && !chatMessage.chat.contains(":")){

			//Add to own list
			String client = new String(chatMessage.chat.split(" ")[0]);
			System.out.println("Client joined: " + client);
			ClientList.add(client);
			updateClientList();

			//Message to all clients about client list
			String ClientListString = String.join(ClientsDelimiter, ClientList);
			gc.sendChatMessage(ClientsMessage + " " + ClientListString, true);
		}

		//If leave message detected and is valid
		else if(chatMessage.chat.contains(LeftMessage) && !chatMessage.chat.contains(":")){
			//Remove client from our list
			String client = new String(chatMessage.chat.split(" ")[0]);
			System.out.println("Client left: " + client);
			ClientList.remove(client);
			updateClientList();

		//If clients message is detected and is valid
		}else if(chatMessage.chat.contains(ClientsMessage) && !chatMessage.chat.contains(":")){
			System.out.println("Join message recieved!");

			//Scrape for clients into ArrayList
			String RecievedClientsString[] = Pattern.compile(ClientsMessage, Pattern.LITERAL).split(chatMessage.chat);
			RecievedClientsString = Arrays.copyOfRange(RecievedClientsString, 1, RecievedClientsString.length); //Remove overhead
			RecievedClientsString = RecievedClientsString[0].split(Pattern.quote(ClientsDelimiter)); //Split clients into each
			ArrayList<String> RecievedClients = new ArrayList<String>(Arrays.asList(RecievedClientsString));

			//Compare if new ArrayList of clients is bigger
			if(RecievedClients.size() > ClientList.size()){
				ClientList = RecievedClients;
				updateClientList();
			}

		//Else normal message
		}else{
			txtpnChat.setText(chatMessage.chat + "\n" + txtpnChat.getText());
		}
	}

	//Updates from client list array
	public void updateClientList(){
		txtpnClients.setText("");
		for(String c : ClientList){
			c.trim();	//Trim whitespaces
			txtpnClients.setText(c + "\n" + txtpnClients.getText());
		}
	}
}
