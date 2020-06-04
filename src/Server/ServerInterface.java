
package Server;

import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class ServerInterface extends JFrame {
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ServerInterface frame = new ServerInterface();
                    frame.setVisible(true);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        });
    }

    public ServerInterface() {
        this.setTitle("Server");
        this.setDefaultCloseOperation(3);
        this.setBounds(100, 100, 750, 450);
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
        this.setContentPane(this.contentPane);
        this.contentPane.setLayout((LayoutManager)null);
        final JTextArea logEvent = new JTextArea();
        JScrollPane el = new JScrollPane(logEvent);
        logEvent.setEditable(false);
        el.setBounds(10, 40, 400, 300);
        this.contentPane.add(el);
        final JTextArea queryLog = new JTextArea();
        JScrollPane ql = new JScrollPane(queryLog);
        queryLog.setEditable(false);
        ql.setBounds(420, 40, 300, 300);
        this.contentPane.add(ql);
        JButton clearEventLog = new JButton("Clear Events Log");
        clearEventLog.setBounds(10, 350, 200, 25);
        this.contentPane.add(clearEventLog);
        JButton clearQueryLog = new JButton("Clear Queries Log");
        clearQueryLog.setBounds(420, 350, 200, 25);
        this.contentPane.add(clearQueryLog);
        JLabel label = new JLabel("Events log");
        label.setBounds(15, 10, 70, 20);
        this.contentPane.add(label);
        JLabel label_1 = new JLabel("Queries log");
        label_1.setBounds(420, 10, 70, 20);
        this.contentPane.add(label_1);
        clearEventLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logEvent.setText("");
            }
        });
        clearQueryLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                queryLog.setText("");
            }
        });
        (new Thread(new Runnable() {
            public void run() {
                try {
                    new Server(logEvent, queryLog);
                } catch (Exception var2) {
                    var2.printStackTrace();
                }

            }
        })).start();
    }
}
