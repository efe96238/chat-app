import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class ChatClient {

	private String clientId;
	private String serverHost;
	private int serverPort;
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;

	private static final Logger logger = LogManager.getLogger(ChatClient.class);


	public ChatClient(String clientName) {
		this.clientId = clientName;

	}

	public void sendMessage(String recipient, String text) {
		writer.println("send;" + recipient + ";" + text);
		logger.debug("Sending message to " + recipient + ": " + text);
	}

	public List<String> getConnectedUsers() {
		logger.debug("Requesting connected users list");
		List<String> userList = new ArrayList<>();
		try {

			writer.println("get_users_list");

			System.out.println("reading users");
			String userName;
			while ((userName = reader.readLine()) != null) {
				System.out.println("Found: " + userName);
				if (userName == null || userName.equals("null")) {
					logger.info("Connected users list received");
					return userList;
				}
				userList.add(userName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userList;
	}

	public List<String> searchMessages(String searchText) {
		logger.debug("Searching messages with text: " + searchText);
		List<String> searchedMessages = new ArrayList<>();
		try {

			writer.println("find_message;"+searchText);

			String msg;
			while ((msg = reader.readLine()) != null) {
				if (msg == null || msg.equals("null")) {
					logger.info("Message search completed");
					return searchedMessages;
				}
				searchedMessages.add(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return searchedMessages;
	}

	public List<String> getAllMessages(String sender) {
		logger.debug("Requesting message history for sender: " + sender);
		List<String> listMessages = new ArrayList<>();
		try {

			writer.println("get_message_history;" + sender);

			System.out.println("reading messages");
			String msg;
			while ((msg = reader.readLine()) != null) {
				if (msg == null || msg.equals("null")) {
					logger.info("Message history received");
					return listMessages;
				}
				listMessages.add(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listMessages;
	}

	public String getAddress(){
		InetSocketAddress clientSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		return (clientSocketAddress.getAddress().getHostAddress()+ ": " + socket.getLocalPort());
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public boolean connectToServer(){
		try {
			socket = new Socket(serverHost, serverPort);
			writer = new PrintWriter(socket.getOutputStream(), true);
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer.println(clientId);
			logger.info("Client started: " + clientId);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Invalid Server Address");
			return false;
		}
	}

	public String getClientId() {
		return clientId;
	}
}
