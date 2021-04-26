import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.net.ServerSocketFactory;
import javax.swing.JButton;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DictionaryServer {

	private JFrame frame;
	// Declare the port number
	private static int port = 3002;

	private JButton btnStop = new JButton("Stop");
	private JButton btnNewButton = new JButton("Start");
	private JLabel lblNewLabel = new JLabel("Hongyi's Dictionary Server");
	private final JTextArea textArea = new JTextArea();
	
	private HashMap<String, String> dictionary = null;
	private String strDictionary = "";
	protected ServerSocket server = null;
	private Socket clientSocket = null;
	

    
    private ArrayList<ClientConnect> clientList = new ArrayList<ClientConnect>();

    private boolean ifStart = false;
    private static String fileName = "myDictionary.txt";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		if(args.length == 2) {
			try {
				port = Integer.parseInt(args[0]);
				fileName = args[1];
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DictionaryServer window = new DictionaryServer();
//					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public DictionaryServer() {
		initialize();
		dictionary = readDictionary();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		
		frame = new JFrame();
		frame.setBounds(100, 100, 564, 363);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		lblNewLabel.setBounds(100, 32, 390, 31);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 25));
		frame.getContentPane().add(lblNewLabel);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifStart = true;
				startServer();
			}
		});
		
		
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setBounds(100, 257, 105, 37);
		frame.getContentPane().add(btnNewButton);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeServer();
			}
		});
		
		
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnStop.setBounds(329, 257, 105, 37);
		frame.getContentPane().add(btnStop);
		
		textArea.setBounds(100, 73, 334, 174);
		textArea.setEditable(false);
		
		frame.getContentPane().add(textArea);
		
		
	}
	
	/**
	 * start the dictionary server
	 */
	public void startServer() {
		ServerSocketFactory factory = ServerSocketFactory.getDefault();
		try {
			this.server = factory.createServerSocket(port);
			if(ifStart) {
				textArea.append("Server Start.."+"\n");
				System.out.println("Server Start.."+"\n");
			}

			while(ifStart)
			{
				Socket client = this.server.accept();
				ClientConnect curClient = new ClientConnect(client);
				clientList.add(curClient);

			}
			
		} catch (IOException e) {
			System.out.println("The Server is already start!"+"\n");
			JOptionPane.showMessageDialog(null, "The Server is already start!");
			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * close the dictionary server
	 */
	public void closeServer() {
		ifStart = false;
		try{
			
			if(!this.server.isClosed()) {
				this.server.close();
				textArea.append("Server Closed.."+"\n");
			}
			if(!clientSocket.isClosed()) {
				clientSocket.close();
			}
			
			System.exit(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch(NullPointerException nulle) {
			JOptionPane.showMessageDialog(null, "The server is closed already!");
		}
	}
	
	
	class ClientConnect implements Runnable{
		Socket s = null;
		String msg = null;
		
		public ClientConnect(Socket s){
			this.s = s;
			(new Thread(this)).start();
		}
		
		public void run() {
			try {
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				while(ifStart)
			    {
//					send(strDictionary,s);
					
					String clientMsg = dis.readUTF();
					String[] clientMsgArr = clientMsg.split("-");
					String option = clientMsgArr[0];
					String wordAndMeaning = clientMsgArr[1];
					
					if(option.equals("ADD")) {
						msg = addDictionary(dictionary, wordAndMeaning);
						send(msg, s);
					}else if(option.equals("SEARCH")) {
						msg = searchDictionary(dictionary, wordAndMeaning);
						send(msg, s);
					}else if(option.equals("UPDATE")) {
						msg = updateDictionary(dictionary, wordAndMeaning);
						send(msg, s);
					}else if(option.equals("DELETE")) {
						msg = deleteDictionary(dictionary, wordAndMeaning);
						send(msg, s);
					}
					
					if(msg != null) {
						System.out.println(msg);
					}
					
					if(dictionary != null) {
						writeDictionary(dictionary);
					}
					
			    }
				
				
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	public HashMap<String, String> readDictionary(){
		createFile();
		
		String path = System.getProperty("user.dir") + "\\src\\" + fileName;

		File f = new File(path);
		dictionary = new HashMap<String, String>();
		
		try {
            FileInputStream fileInputStream = new FileInputStream(f);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String myDic, myWord, myMeaning = null;
            String[] instance = null;
            
            while ((myDic = bufferedReader.readLine()) != null) {
            	strDictionary = strDictionary+myDic+"\n";
            	instance = myDic.split(":");
            	myWord = instance[0];
            	myMeaning = instance[1];
                dictionary.put(myWord, myMeaning);
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		return dictionary;
	}
	
	
	public void createFile() {
		try {
			FileReader fr = new FileReader(fileName);
			System.out.println("file exist!");
		} catch (FileNotFoundException e) {
			try {
				FileWriter fw = new FileWriter(fileName);
				System.out.println("file create!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void writeDictionary(HashMap<String, String> dictionary) {
		String path = System.getProperty("user.dir") + "\\src\\" + fileName;
		File f = new File(path);
		
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(f);
            Set<String> words = dictionary.keySet();
            for (int i = 0; i < dictionary.size(); i++) {
                Object word = words.toArray()[i];
                String line = word.toString() + ":" + dictionary.get(word);
                fileOutputStream.write(line.getBytes());
                fileOutputStream.write('\n');
            }
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void send(String sendStr, Socket socket) {
		try {
			DataOutputStream doc = new DataOutputStream(socket.getOutputStream());
			doc.writeUTF(sendStr);
			doc.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String addDictionary(HashMap<String, String> dictionary, String str) {
		String myWord, myMeaning, message= null;
		String[] instance = null;
		instance = str.split(":");
    	myWord = instance[0];
    	myMeaning = instance[1];
    	
    	try {
            if (dictionary.containsKey(myWord)) {
                message = "Fail to add, the word " + myWord + " exist!";

            } else {
                dictionary.put(myWord, myMeaning);
                message = "Add " + myWord + " SUCCESS!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return message;
	}
	
	public String searchDictionary(HashMap<String, String> dictionary, String word) {
		String message, curMeaning= null;
		if (dictionary.get(word) != null) {
			curMeaning = dictionary.get(word).toString();
			message = "MEANING:" + curMeaning;
        } else {
        	message = "Can not find the word "  + word + " in current Dictionary!";
        }
		return message;
	}
	
	public String updateDictionary(HashMap<String, String> dictionary, String str) {
		String myWord, myMeaning, message= null;
		String[] instance = null;
		instance = str.split(":");
    	myWord = instance[0];
    	myMeaning = instance[1];
    	
    	try {
            if (dictionary.containsKey(myWord)) {
            	dictionary.remove(myWord, dictionary.get(myWord));
            	dictionary.put(myWord, myMeaning);
                
                message = "Update " + myWord + " SUCCESS!";
            } else {
            	message = "Fail to update, the word not exist in dictionary!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return message;
	}
	
	public String deleteDictionary(HashMap<String, String> dictionary, String word) {
		String message= null;
    	
    	try {
            if (dictionary.containsKey(word)) {
            	dictionary.remove(word, dictionary.get(word));

                message = "Delete " + word + " SUCCESS!";
            } else {
            	message = "Fail to delete, the word not exist in dictionary!";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	return message;
	}
}
