
package Client;

import XML.XML;
import XML.XMLMessage;
import XML.XMLResponse;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientInterface extends JFrame {
    private JPanel mainPage;
    private JTextField ourMessage;
    private JTextField username;
    private Client ourClient;
    private boolean processing;
    private JLabel userStatus;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ClientInterface userInterface = new ClientInterface();
                    userInterface.setVisible(true);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    public ClientInterface() {
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                if (ClientInterface.this.userStatus.getText() == "Status: online") {
                    ClientInterface.this.ourClient.setOffline(ClientInterface.this.username.getText());
                }

            }
        });
        this.setTitle("Messenger");
        this.setDefaultCloseOperation(3);
        this.setBounds(150, 150, 750, 500);
        this.mainPage = new JPanel();
        this.mainPage.setBorder(new EmptyBorder(1, 1, 1, 1));
        this.setContentPane(this.mainPage);
        this.mainPage.setLayout((LayoutManager)null);
        this.username = new JTextField();
        this.username.setBounds(620, 80, 100, 25);
        this.mainPage.add(this.username);
        this.username.setColumns(10);
        JLabel label = new JLabel("Your login:");
        label.setBounds(620, 50, 100, 16);
        this.mainPage.add(label);
        final JButton send = new JButton("Send");
        send.setEnabled(false);
        send.setBounds(500, 400, 100, 25);
        this.mainPage.add(send);
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!ClientInterface.this.ourMessage.getText().isEmpty()) {
                    ClientInterface.this.ourClient.sendTo(ClientInterface.this.ourMessage.getText());
                    ClientInterface.this.ourMessage.setText("");
                }

            }
        });
        this.userStatus = new JLabel("Status: offline");
        this.userStatus.setBounds(620, 150, 100, 25);
        this.mainPage.add(this.userStatus);
        this.ourMessage = new JTextField();
        this.ourMessage.setEnabled(false);
        this.ourMessage.setBounds(10, 400, 300, 25);
        this.mainPage.add(this.ourMessage);
        this.ourMessage.setColumns(15);
        final JComboBox<Object> allUser = new JComboBox();
        allUser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                if (!ClientInterface.this.processing) {
                    if (allUser.getSelectedIndex() != -1) {
                        ClientInterface.this.ourClient.conversation = allUser.getSelectedItem().toString();
                    } else {
                        ClientInterface.this.ourClient.conversation = ClientInterface.this.username.getText();
                    }
                }

            }
        });
        allUser.setEnabled(false);
        allUser.setBounds(320, 400, 160, 25);
        this.mainPage.add(allUser);
        final JTextArea chat = new JTextArea();
        chat.setEditable(false);
        chat.setBounds(10, 10, 600, 370);
        this.mainPage.add(chat);
        final JButton apply = new JButton("Login");
        apply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (apply.getText().equals("Login")) {
                    ClientInterface.this.ourClient = new Client(ClientInterface.this.username.getText());
                    if (!ClientInterface.this.ourClient.setOnline(ClientInterface.this.username.getText())) {
                        JOptionPane.showMessageDialog(ClientInterface.this.mainPage, "User already registered.", "Error", 0);
                        return;
                    }

                    (new Thread(new Runnable() {
                        public void run() {
                            while(true) {
                                if (apply.getText().equals("Log out")) {
                                    XMLResponse xml;
                                    if (ClientInterface.this.ourClient.getUsers()) {
                                        ClientInterface.this.processing = true;
                                        xml = XML.parse(ClientInterface.this.ourClient.users);
                                        allUser.removeAllItems();
                                        int chatUser = 0;

                                        for(int i = 0; i < xml.ourMessage.size(); ++i) {
                                            Object userPage = ((XMLMessage)xml.ourMessage.get(i)).from;
                                            allUser.addItem(userPage);
                                            if (((XMLMessage)xml.ourMessage.get(i)).from.equals(ClientInterface.this.ourClient.conversation)) {
                                                chatUser = i;
                                            }
                                        }

                                        ClientInterface.this.processing = false;
                                        allUser.setSelectedIndex(chatUser);
                                    }

                                    if (ClientInterface.this.ourClient.getMessages()) {
                                        xml = XML.parse(ClientInterface.this.ourClient.allMessage);
                                        chat.setText("");
                                        if (xml != null && xml.ourMessage != null && !xml.ourMessage.isEmpty()) {
                                            Iterator var6 = xml.ourMessage.iterator();

                                            while(var6.hasNext()) {
                                                XMLMessage m = (XMLMessage)var6.next();
                                                chat.append(m.message + "\n");
                                            }
                                        }
                                    }
                                }

                                try {
                                    Thread.sleep(250L);
                                } catch (Exception var5) {
                                }
                            }
                        }
                    })).start();
                    ClientInterface.this.userStatus.setText("Status: online");
                    send.setEnabled(true);
                    ClientInterface.this.ourMessage.setEnabled(true);
                    ClientInterface.this.username.setEnabled(false);
                    allUser.setEnabled(true);
                    apply.setText("Log out");
                } else {
                    ClientInterface.this.ourClient.setOffline(ClientInterface.this.username.getText());
                    ClientInterface.this.userStatus.setText("Status: offline");
                    ClientInterface.this.username.setEnabled(true);
                    send.setEnabled(false);
                    ClientInterface.this.ourMessage.setEnabled(false);
                    allUser.setEnabled(false);
                    apply.setText("Login");
                }

            }
        });
        apply.setBounds(620, 120, 100, 25);
        this.mainPage.add(apply);
    }

    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent eventm) {
                if (eventm.isPopupTrigger()) {
                    this.showMenu(eventm);
                }

            }

            public void mouseReleased(MouseEvent eventm) {
                if (eventm.isPopupTrigger()) {
                    this.showMenu(eventm);
                }

            }

            private void showMenu(MouseEvent eventm) {
                popup.show(eventm.getComponent(), eventm.getX(), eventm.getY());
            }
        });
    }
}
