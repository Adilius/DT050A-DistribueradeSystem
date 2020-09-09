import java.awt.EventQueue;

import javax.swing.JFrame;

import se.miun.distsys.GroupCommuncation;
import se.miun.distsys.listeners.ChatMessageListener;
import se.miun.distsys.messages.ChatMessage;

import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JScrollPane;

//Skeleton code for Distributed systems 9hp, DT050A

public class WindowProgram implements ChatMessageListener, ActionListener {

	JFrame frame;
	JTextPane txtpnChat = new JTextPane();
	JTextPane txtpnMessage = new JTextPane();
	JTextPane txtpnClients = new JTextPane();
	
	GroupCommuncation gc = null;	

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
		initializeFrame();

		gc = new GroupCommuncation();		
		gc.setChatMessageListener(this);
		System.out.println("Group Communcation Started");
	}

	private void initializeFrame() {

		//Set base frame
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 600);
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
		txtpnClients.setEditable(false);

		//Window close
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
	        public void windowClosing(WindowEvent winEvt) {
				gc.sendChatMessage(" leaved the group.", true);
	            gc.shutdown();
	        }
	    });

		//Window open
		frame.addWindowListener(new java.awt.event.WindowAdapter(){
			public void windowOpened(WindowEvent winEvt){
				gc.sendChatMessage(" joined the group.", true);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equalsIgnoreCase("send")) {
			gc.sendChatMessage(txtpnMessage.getText() , false);
		}		
	}
	
	@Override
	public void onIncomingChatMessage(ChatMessage chatMessage) {	
		txtpnChat.setText(chatMessage.chat + "\n" + txtpnChat.getText());				
	}
}
