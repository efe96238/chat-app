import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Server {

	private Map<String, Socket> connectedClients;
	private List<Message> messages;
	private static final Logger logger = LogManager.getLogger(Server.class);


	public Server() {
		connectedClients = new HashMap<>();
		messages = new ArrayList<>();
	}

	public void startServer() {
		try {
			InetAddress host = InetAddress.getByName("localhost"); // Change to the desired host
			ServerSocket serverSocket = new ServerSocket(1111, 0, host);  //change the port
			System.out.println("Server running on host: " + host.getHostAddress());
			System.out.println("Server started. Listening on port " + serverSocket.getLocalPort());
			logger.info("Server started on port " + serverSocket.getLocalPort());
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket);
				Thread clientThread = new Thread(new ClientConnection(clientSocket));
				clientThread.start();
			}
		} catch (IOException e) {
			logger.error("An error occurred", e);
			e.printStackTrace();
		}
	}


	private class ClientConnection implements Runnable {
		private Socket clientSocket;
		private BufferedReader reader;
		private PrintWriter writer;
		private String clientId;

		public ClientConnection(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public void run() {
			try {
				reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				writer = new PrintWriter(clientSocket.getOutputStream(), true);

				clientId = reader.readLine();
				connectedClients.put(clientId, clientSocket);

				logger.info("New client connected: " + clientSocket);

				System.out.println("Connected Clients: " + connectedClients.keySet().toString());

				String clientCommand;
				while ((clientCommand = reader.readLine()) != null) {
					processClientCommand(clientCommand);
				}

				reader.close();
				writer.close();
				clientSocket.close();

				connectedClients.remove(clientId);
			} catch (IOException e) {
				//e.printStackTrace();
				connectedClients.remove(clientId);
				logger.info("Client Disconnected", clientId);
			}
			
		}

		private void processClientCommand(String command) {
			System.out.println("Command: " + command);
			logger.debug("Received command: " + command);
			String[] tokens = command.split(";");
			String userAction = tokens[0];

			switch (userAction) {
			case "send":
				String recipient = tokens[1];
				String message = tokens[2];
				sendMessage(clientId, recipient, message);
				break;
			case "get_users_list":
				sendUserList(clientId);
				break;
			case "find_message":
				String searchText = tokens[1];
				searchMessage(clientId, searchText);
				break;
			case "get_message_history":
				String targetUser = tokens[1];
				sendMessageHistory(clientId, targetUser);
				break;
			default:
				System.out.println("Unknown command: " + command);
				break;
			}
		}
	}

	private void sendMessage(String sender, String recipient, String messageText) {
		Message message = new Message(sender, recipient, messageText);
		messages.add(message);
	}

	private void sendUserList(String clientId) {
		List<String> userList = new ArrayList<>(connectedClients.keySet());
		userList.remove(clientId);

		if (userList.isEmpty()) {
			sendMessageToClient(clientId, null);
		} else {
			System.out.println("Sending list: " + userList);
			for (String user : userList) {
				int receivedCount = receivedMessageCount(clientId, user);
				String msg = String.format("%-30s%-20d",user+":",receivedCount);
				sendMessageToClient(clientId, msg);
			}
			sendMessageToClient(clientId, null);
		}

	}

	private int receivedMessageCount(String sender, String recipient) {
		int count = 0;
		for (Message message : messages) {
			if ((message.getSenderName().equals(sender) && message.getRecipientName().equals(recipient))
					|| (message.getRecipientName().equals(sender) && message.getSenderName().equals(recipient))) {
				count++;
			}
		}
		return count;
	}

	private void searchMessage(String clientId, String searchText) {

		for (Message message : messages) {
			if (message.getMsgText().contains(searchText)
					&& (message.getSenderName().equals(clientId) || message.getRecipientName().equals(clientId))) {
				sendMessageToClient(clientId, message.toString());
			}
		}
		sendMessageToClient(clientId, null);
	}

	private void sendMessageHistory(String sender, String recipient) {

		for (Message message : messages) {
			if ((message.getSenderName().equals(sender) && message.getRecipientName().equals(recipient))
					|| (message.getRecipientName().equals(sender) && message.getSenderName().equals(recipient))) {
				sendMessageToClient(sender, message.toString());
			}
		}
		sendMessageToClient(sender, null);

	}

	private void sendMessageToClient(String clientId, String message) {
		Socket clientSocket = connectedClients.get(clientId);
		try {
			PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
			System.out.println("Sending: " + message);
			writer.println(message);
		} catch (IOException e) {
			logger.error("An error occurred", e);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.setProperty("log4j.configurationFile", "log4j2.xml");
		Server server = new Server();
		server.startServer();
	}
}
