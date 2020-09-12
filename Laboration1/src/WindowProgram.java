import java.awt.EventQueue;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import se.miun.distsys.GroupCommunication;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.listeners.ClientListMessageListener;
import se.miun.distsys.listeners.JoinMessageListener;
import se.miun.distsys.listeners.LeaveMessageListener;
import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.ClientListMessage;
import se.miun.distsys.messages.JoinMessage;
import se.miun.distsys.messages.LeaveMessage;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

import javax.swing.JScrollPane;

//Skeleton code for Distributed systems 9hp, DT050A

public class WindowProgram implements ChatMessageListener, JoinMessageListener, LeaveMessageListener, ClientListMessageListener, ActionListener {

    JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnClients = new JTextPane();
	
	GroupCommunication gc;

    public static String username = "Unknown";

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
		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {

		//Set base frame
		frame = new JFrame();
		frame.setTitle("LAN SP34K: " + username);
		try {
			frame.setIconImage(ImageIO.read(new File("images/icon.png")));
		} catch(IOException e) {
			e.printStackTrace();
		}
		frame.setBounds(100, 100, 700, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		//Set incoming text pane
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(txtpnChat);
		txtpnChat.setEditable(false);	
		txtpnChat.setText("---- Group Chat ----");

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
		scrollPaneClients.setViewportView(txtpnClients);
		txtpnClients.setEditable(false);

		//Window listeners
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
	            gc.shutdown();
	        }
	    });
	}

	public static String getUsername() {
		//Create random ID for client
		Random rand = new Random();
		int randID = rand.nextInt(1000);

		//Get computer name if possible
		Map<String, String> env = System.getenv();
		String computerName;
		if (env.containsKey("COMPUTERNAME")){
			computerName = env.get("COMPUTERNAME");
		}
		else if (env.containsKey("HOSTNAME")){
			computerName = env.get("HOSTNAME");
		}
		else {
			computerName = "Unknown";
		}

		//Return username
		return (computerName + "_" + randID);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.sendChatMessage(txtpnMessage.getText());
		}		
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {	
		txtpnChat.setText(chatMessage.chat + "\n" + txtpnChat.getText());				
	}

	@Override
	public void onIncomingJoinMessage(JoinMessage joinMessage) {

	}

	@Override
	public void onIncomingLeaveMessage(LeaveMessage leaveMessage) {

	}

	@Override
	public void onIncomingLeaveMessage(ClientListMessage clientListMessage) {

	}
}
