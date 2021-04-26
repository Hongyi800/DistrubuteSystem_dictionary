import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DictionaryClient {

	private JFrame frame;
	private JTextField txtIP;
	private JTextField txtPort;
	
	// IP and port
//	private static String ip = "localhost";
//	private static int port = 3002;
	
	private DataOutputStream doc = null;

	
	private Socket socket = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryClient window = new DictionaryClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public DictionaryClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 488, 343);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHongyisDictionary = new JLabel("Hongyi's Dictionary");
		lblHongyisDictionary.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblHongyisDictionary.setBounds(101, 22, 364, 31);
		frame.getContentPane().add(lblHongyisDictionary);
		
		JPanel panel = new JPanel();
		panel.setBounds(22, 72, 426, 224);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Inter Client Infomation:");
		lblNewLabel.setBounds(10, 10, 150, 17);
		panel.add(lblNewLabel);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblNewLabel_1 = new JLabel("IP:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(28, 49, 62, 35);
		panel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Port:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1_1.setBounds(28, 106, 62, 35);
		panel.add(lblNewLabel_1_1);
		
		txtIP = new JTextField();
		txtIP.setColumns(10);
		txtIP.setBounds(98, 55, 277, 31);
		panel.add(txtIP);
		
		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setBounds(98, 112, 277, 31);
		panel.add(txtPort);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String sendStrIP = txtIP.getText();
				String sendStrPort = txtPort.getText();
				int curPort=Integer.parseInt(sendStrPort);  
				
				if(sendStrIP.trim().length()== 0 || sendStrPort.trim().length()== 0) {
					JOptionPane.showMessageDialog(null, "Please enter your port and ip!");
				}else {
					try{
						socket = new Socket(sendStrIP, curPort);
//						socket = new Socket(ip, port);
//						String sendStr = sendStrIP+sendStrPort;
					    
						ClientDashboard dc = new ClientDashboard(socket);
						dc.run(socket);
						frame.dispose();
						
					}catch (UnknownHostException e)
					{
						JOptionPane.showMessageDialog(null, "The input ip is unknow !");
						e.printStackTrace();
					}
					catch (IOException e) 
					{
						JOptionPane.showMessageDialog(null, "Server is closed on input port !");
						e.printStackTrace();
					}

					
				}
				
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(158, 168, 111, 35);
		panel.add(btnNewButton);
	}
	
	public void send(String sendStr) {
		try {
			doc = new DataOutputStream(socket.getOutputStream());
			doc.writeUTF(sendStr);
			doc.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
