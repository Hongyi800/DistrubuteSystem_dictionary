import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ClientDashboard {

	private JFrame frame;
	private JTextField txtWord;
	
	private Socket socket = null;
	private DataOutputStream output = null;
	private JTextField txtSearch;
	private String strDictionary = "";


	
	public void run(Socket socket) {
		try {
			
			ClientDashboard window = new ClientDashboard(socket);
			window.frame.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public ClientDashboard(Socket socket) {
		initialize(socket);
		this.socket = socket;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Socket socket) {		
		frame = new JFrame();
		frame.setBounds(100, 100, 858, 608);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblHongyisDictionary = new JLabel("Hongyi's Dictionary");
		lblHongyisDictionary.setBounds(25, 19, 364, 31);
		lblHongyisDictionary.setFont(new Font("Tahoma", Font.BOLD, 25));
		frame.getContentPane().add(lblHongyisDictionary);
		
		JTextArea textAreaMeaning = new JTextArea();
		textAreaMeaning.setEditable(false);
		textAreaMeaning.setBounds(688, 112, 135, 429);
		frame.getContentPane().add(textAreaMeaning);
		
		JTextArea textAreaWord = new JTextArea();
		textAreaWord.setBounds(530, 112, 135, 429);
		textAreaWord.setEditable(false);
		frame.getContentPane().add(textAreaWord);
		
		
		
		JLabel lblNewLabel_1_2 = new JLabel("Word list");
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewLabel_1_2.setBounds(559, 62, 83, 35);
		frame.getContentPane().add(lblNewLabel_1_2);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Meaning list");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewLabel_1_1_1.setBounds(712, 64, 111, 32);
		frame.getContentPane().add(lblNewLabel_1_1_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Add new word", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(25, 60, 480, 278);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Word:");
		lblNewLabel_1.setBounds(25, 39, 55, 25);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JLabel lblNewLabel_1_1 = new JLabel("Meaning:");
		lblNewLabel_1_1.setBounds(25, 74, 111, 32);
		panel.add(lblNewLabel_1_1);
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JTextArea txtMeaning = new JTextArea();
		txtMeaning.setBounds(119, 82, 316, 107);
		panel.add(txtMeaning);
		
		txtWord = new JTextField();
		txtWord.setBounds(119, 40, 316, 31);
		panel.add(txtWord);
		txtWord.setColumns(10);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(119, 211, 87, 38);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String curWord, curMeaning, stringSend;
				curWord = txtWord.getText();
				curMeaning = txtMeaning.getText();
				
				textAreaWord.append(curWord+"\n");
				textAreaMeaning.append(curMeaning+"\n");
				
				stringSend = "ADD-"+ curWord + ":"+curMeaning;
				send(stringSend);
	            System.out.print(stringSend);
	            try {
					DataInputStream input = new DataInputStream(socket.getInputStream());
					
					curMeaning = input.readUTF();
					System.out.println("Server says: "+curMeaning);
					JOptionPane.showMessageDialog(null, curMeaning);
					

					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnReset = new JButton("Reset");
		btnReset.setBounds(348, 211, 87, 38);
		panel.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtWord.setText("");
				txtMeaning.setText("");
			}
		});
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String curWord, curMeaning, stringSend;
				curWord = txtWord.getText();
				curMeaning = txtMeaning.getText();
				
				textAreaWord.append(curWord+"\n");
				textAreaMeaning.append(curMeaning+"\n");
				
				stringSend = "UPDATE-"+ curWord + ":"+curMeaning;
				send(stringSend);
	            System.out.print(stringSend);
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnUpdate.setBounds(237, 211, 87, 38);
		panel.add(btnUpdate);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search a word", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(25, 366, 483, 175);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblNewLabel_1_2_1 = new JLabel("The word you want to search:");
		lblNewLabel_1_2_1.setBounds(27, 26, 202, 19);
		lblNewLabel_1_2_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		panel_1.add(lblNewLabel_1_2_1);
		
		txtSearch = new JTextField();
		txtSearch.setColumns(10);
		txtSearch.setBounds(27, 55, 320, 41);
		panel_1.add(txtSearch);
		
		JButton btnNewButton_1_1_1 = new JButton("Search");
		btnNewButton_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String curWord, stringSend,curMeaning;
				curWord = txtSearch.getText();
				stringSend = "SEARCH-"+ curWord;
				send(stringSend);
	            System.out.print(stringSend);
	            
	            try {
					DataInputStream input = new DataInputStream(socket.getInputStream());

					curMeaning = input.readUTF();
					System.out.println("Server says: "+curMeaning);
					JOptionPane.showMessageDialog(null, curMeaning);


				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_1_1_1.setBounds(357, 55, 101, 43);
		panel_1.add(btnNewButton_1_1_1);
		
		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String curWord, stringSend,curMeaning;
				curWord = txtSearch.getText();
				stringSend = "SEARCH-"+ curWord;
				send(stringSend);
	            System.out.print(stringSend);
	            
	            try {
					DataInputStream input = new DataInputStream(socket.getInputStream());
					curMeaning = input.readUTF();
					txtWord.setText(curWord);
					txtMeaning.setText(curMeaning);

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(52, 117, 101, 33);
		panel_1.add(btnNewButton_1);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnNewButton_1_2 = new JButton("Delete");
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String curWord, stringSend,curMeaning;
				curWord = txtSearch.getText();
				stringSend = "DELETE-"+ curWord;
				send(stringSend);
	            System.out.print(stringSend);
	            
	            try {
					DataInputStream input = new DataInputStream(socket.getInputStream());
					
					curMeaning = input.readUTF();
					System.out.println("Server says: "+curMeaning);
					JOptionPane.showMessageDialog(null, curMeaning);
					

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton_1_2.setBounds(228, 117, 101, 33);
		panel_1.add(btnNewButton_1_2);
		

		
	}
	
	public void send(String sendStr) {
		try {
			output = new DataOutputStream(socket.getOutputStream());
			output.writeUTF(sendStr);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateWordList(JTextArea textAreaWord, JTextArea textAreaMeaning, Socket socket) {
		try {
			DataInputStream input = new DataInputStream(socket.getInputStream());
			strDictionary = input.readUTF();
			String word,meaning = "";
			String[] wordAndMeanings = strDictionary.split("\n");
			for( int i = 0; i <= wordAndMeanings.length - 1; i++) {
				String[] instance = wordAndMeanings[i].split(":");
				word = instance[0];
				meaning = instance[1];
				
				textAreaWord.append(word+"\n");
				textAreaMeaning.append(meaning+"\n");
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
