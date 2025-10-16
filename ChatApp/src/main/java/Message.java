import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	private String senderName;
	private String recipientName;
	private String msgText;
	private String timestamp;

	public Message(String senderName, String recipientName, String msgText) {
		this.senderName = senderName;
		this.recipientName = recipientName;
		this.msgText = msgText;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		this.timestamp = LocalDateTime.now().format(formatter);
	}

	public Message(String sender, String recipientName, String msgText, String timestamp) {
		this.senderName = sender;
		this.recipientName = recipientName;
		this.msgText = msgText;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		this.timestamp = LocalDateTime.now().format(formatter);
	}

	public String getSenderName() {
		return senderName;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public String getMsgText() {
		return msgText;
	}

	public String getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {

		return  timestamp +" " + senderName + " to " + recipientName + ": " + msgText;
	}
}
