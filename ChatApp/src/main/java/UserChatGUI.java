import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

public class UserChatGUI extends JFrame {

    private JPanel contentPane;
    private JButton btnRefresh;
    private JTextField tfMsg;
    private JTextField tfPort;
    private JTextField tfSearch;
    private JLabel lblUsers;
    private JList<String> listUsers;
    private JLabel lblChat;
    private JList<String> listChat;
    private JButton btnGetMessages;
    private JButton btnSendMsg;
    private JButton btnSearch;
    private JButton btnConnect;
    private JLabel lblSearch;
    private static final Logger logger = LogManager.getLogger(UserChatGUI.class);
    private ChatClient chatClient;
    private DefaultListModel<String> modelChats;
    private DefaultListModel<String> modelUsers;


    public static void main(String[] args) {

                try {
                    UserChatGUI frame = new UserChatGUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }


    public UserChatGUI() {
        modelChats = new DefaultListModel<>();
        modelUsers = new DefaultListModel<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 778, 614);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);
        String lblHeading = String.format("%-20s%-20s","Users","Msgs");
        lblUsers = new JLabel(lblHeading);
        lblUsers.setBorder(new LineBorder(Color.BLACK));
        lblUsers.setForeground(Color.BLUE);
        lblUsers.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblUsers.setBounds(64, 57, 241, 29);
        contentPane.add(lblUsers);

        listUsers = new JList<>();
        listUsers.setModel(modelUsers);
        listUsers.setBorder(new LineBorder(new Color(0, 0, 0)));
        listUsers.setBounds(64, 86, 241, 261);
        contentPane.add(listUsers);

        lblChat = new JLabel("User Chat Panel");
        lblChat.setHorizontalAlignment(SwingConstants.CENTER);
        lblChat.setForeground(Color.BLUE);
        lblChat.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblChat.setBorder(new LineBorder(Color.BLACK));
        lblChat.setBounds(376, 57, 356, 29);
        contentPane.add(lblChat);

        listChat = new JList<>();
        listChat.setModel(modelChats);
        listChat.setBorder(new LineBorder(new Color(0, 0, 0)));
        listChat.setBounds(376, 86, 356, 261);
        contentPane.add(listChat);

        btnRefresh = new JButton("Refresh");
        btnRefresh.setOpaque(true);
        btnRefresh.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        btnRefresh.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnRefresh.setBackground(Color.BLUE);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                refreshAction();
            }
        });
        btnRefresh.setBounds(64, 350, 241, 35);
        contentPane.add(btnRefresh);

        btnGetMessages = new JButton("Get Messages");
        btnGetMessages.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getMessageAction();
            }
        });
        btnGetMessages.setOpaque(true);
        btnGetMessages.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        btnGetMessages.setForeground(Color.WHITE);
        btnGetMessages.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnGetMessages.setBackground(Color.BLUE);
        btnGetMessages.setBounds(376, 350, 356, 35);
        contentPane.add(btnGetMessages);

        tfMsg = new JTextField();
        tfMsg.setBounds(376, 393, 219, 40);
        contentPane.add(tfMsg);
        tfMsg.setColumns(10);

        btnSendMsg = new JButton("Send Msg");
        btnSendMsg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendMessageAction();
            }
        });
        btnSendMsg.setOpaque(true);
        btnSendMsg.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        btnSendMsg.setForeground(Color.WHITE);
        btnSendMsg.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnSendMsg.setBackground(Color.BLUE);
        btnSendMsg.setBounds(607, 397, 125, 29);
        contentPane.add(btnSendMsg);

        btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                searchMessageAction();
            }
        });
        btnSearch.setOpaque(true);
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnSearch.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        btnSearch.setBackground(Color.BLUE);
        btnSearch.setBounds(322, 463, 125, 29);
        contentPane.add(btnSearch);

        tfSearch = new JTextField();
        tfSearch.setColumns(10);
        tfSearch.setBounds(64, 458, 241, 40);
        contentPane.add(tfSearch);

        lblSearch = new JLabel("");
        lblSearch.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        lblSearch.setBounds(64, 514, 458, 29);
        contentPane.add(lblSearch);

        tfPort = new JTextField();
        tfPort.setBounds(64, 6, 241, 39);
        contentPane.add(tfPort);
        tfPort.setColumns(10);

        btnConnect = new JButton("Connect");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                connectAction();
            }
        });
        btnConnect.setOpaque(true);
        btnConnect.setForeground(Color.WHITE);
        btnConnect.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        btnConnect.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
        btnConnect.setBackground(Color.BLUE);
        btnConnect.setBounds(311, 12, 125, 29);
        contentPane.add(btnConnect);

        btnRefresh.setEnabled(false);
        btnSearch.setEnabled(false);
        btnGetMessages.setEnabled(false);
        btnSendMsg.setEnabled(false);
        tfPort.setText("server ip:port");
        tfPort.setForeground(Color.gray);
        tfPort.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(tfPort.getText().equals("server ip:port")){
                    tfPort.setText("");
                    tfPort.setForeground(Color.black);
                }
            }
        });
        tfPort.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if(tfPort.getText().isEmpty()){
                    tfPort.setText("server ip:port");
                    tfPort.setForeground(Color.gray);
                }
            }
        });

        tfMsg.setText("Type msg here...");
        tfMsg.setForeground(Color.gray);
        tfMsg.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if(tfMsg.getText().equals("Type msg here...")){
                    tfMsg.setText("");
                    tfMsg.setForeground(Color.black);
                }
            }
        });
        tfMsg.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if(tfMsg.getText().isEmpty()){
                    tfMsg.setText("Type msg here...");
                    tfMsg.setForeground(Color.gray);
                }
            }
        });

        String name = JOptionPane.showInputDialog("Enter name: ");
        setTitle("ChatApp\t"+name);
        chatClient = new ChatClient(name);
    }

    private void sendMessageAction(){
        if (listUsers.getSelectedValue() != null) {
            String recipient = listUsers.getSelectedValue().split(":")[0];
            String message = tfMsg.getText();
            logger.debug("Sending message to recipient: " + recipient + ", message: " + message);
            chatClient.sendMessage(recipient, message);
            tfMsg.setText("");
        }
    }

    private void refreshAction(){
        logger.debug("Refreshing user list");
        List<String> userList = chatClient.getConnectedUsers();
        updateUserList(userList);
        logger.info("User list refreshed");
    }

    private void getMessageAction(){
        if (listUsers.getSelectedValue() != null) {
            String sender = listUsers.getSelectedValue().split(":")[0];
            logger.debug("Requesting messages for sender: " + sender);
            List<String> userList = chatClient.getAllMessages(sender);
            updateMessagesList(userList);
            logger.info("Message history received for sender: " + sender);
            lblChat.setText(sender+" - chat panel");
        }else{
            modelChats.clear();
        }
    }

    private void searchMessageAction(){
        if(!tfSearch.getText().isEmpty()){
            String searchText = tfSearch.getText();
            logger.debug("Searching messages with text: " + searchText);
            List<String> searchResults = chatClient.searchMessages(searchText);
            if(searchResults.isEmpty()){
                lblSearch.setText("Not Found");
            }else{
                lblSearch.setText(searchResults.get(0));
            }
            logger.info("Message search completed");
        }
    }

    private void connectAction(){
        try{
            String address = tfPort.getText();
            String ip = address.split(":")[0];
            int port = Integer.parseInt(address.split(":")[1]);
            chatClient.setServerHost(ip);
            chatClient.setServerPort(port);
            if(chatClient.connectToServer()){
                JOptionPane.showMessageDialog(null,"Successfully Connected!");
                btnRefresh.setEnabled(true);
                btnSearch.setEnabled(true);
                btnGetMessages.setEnabled(true);
                btnSendMsg.setEnabled(true);
                btnConnect.setEnabled(false);
                tfPort.setEnabled(false);
            }

        }catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Invalid Server Address");
        }
    }

    private void updateUserList(List<String> list) {
        modelUsers.clear();
        for (String user : list) {
            modelUsers.addElement(user);
        }

    }

    private void updateMessagesList(List<String> list) {
        modelChats.clear();
        for (String msg : list) {
            if(msg.contains(chatClient.getClientId()+" to " + listUsers.getSelectedValue().split(":")[0]+":")){
                msg = msg.replace(chatClient.getClientId()+" to " + listUsers.getSelectedValue().split(":")[0]+":", " > ");
            }else{
                msg = msg.replace(listUsers.getSelectedValue().split(":")[0]+" to " + chatClient.getClientId()+":", " < ");
            }
            modelChats.addElement(msg);
        }
    }
}
