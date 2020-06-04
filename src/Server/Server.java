

package Server;

import XML.XML;
import XML.XMLMessage;
import XML.XMLResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTextArea;

public class Server {
    private ServerSocket serverSocket;
    private List<String> allUsers;
    private Map<String, List<String>> convertations;
    private static JTextArea events;
    private static JTextArea queryes;

    public void connections() {
        try {
            while(true) {
                try {
                    Socket sockets = this.serverSocket.accept();
                    InputStream inputStream = sockets.getInputStream();
                    OutputStream outputStream = sockets.getOutputStream();
                    byte[] storage = new byte[65536];
                    String ourMessage = new String(storage, 0, inputStream.read(storage));
                    System.out.println(ourMessage);
                    this.process(ourMessage, outputStream);
                    sockets.close();
                } catch (Exception var6) {
                }
            }
        } catch (Exception var7) {
        }
    }

    public Server(JTextArea el, JTextArea ql) throws Exception {
        events = el;
        queryes = ql;
        new Server();
    }

    public void process(String message, OutputStream outputStream) throws IOException {
        XMLResponse xml = XML.parse(message);
        if (!xml.type.equals("getUsers") && !xml.type.equals("getMessages")) {
            JTextArea var10000 = events;
            String var10001 = (new SimpleDateFormat("[d.MM.Y HH:mm:ss ] ")).format(Calendar.getInstance().getTime());
            var10000.append("<" + var10001 + "> " + message + "\n");
        } else {
            queryes.append(message + "\n");
        }

        Iterator var4;
        String whom;
        Entry entry;
        if (xml.type.equals("login")) {
            var4 = this.allUsers.iterator();

            while(var4.hasNext()) {
                whom = (String)var4.next();
                if (xml.name.equals(whom)) {
                    outputStream.write("ERROR".getBytes());
                    return;
                }
            }

            var4 = this.convertations.entrySet().iterator();

            while(var4.hasNext()) {
                entry = (Entry)var4.next();
                if (((String)entry.getKey()).contains(xml.name)) {
                    ((List)entry.getValue()).add(xml.name + " подключился.");
                }
            }

            this.allUsers.add(xml.name);
            outputStream.write("OK".getBytes());
        }

        if (xml.type.equals("logout")) {
            for(int i = 0; i < this.allUsers.size(); ++i) {
                if (xml.name.equals(this.allUsers.get(i))) {
                    this.allUsers.remove(i);
                    break;
                }
            }

            var4 = this.convertations.entrySet().iterator();

            while(var4.hasNext()) {
                entry = (Entry)var4.next();
                if (((String)entry.getKey()).contains(xml.name)) {
                    ((List)entry.getValue()).add(xml.name + " отключился.");
                }
            }

            outputStream.write("OK".getBytes());
        }

        if (xml.type.equals("getUsers")) {
            outputStream.write(XML.users(this.allUsers).getBytes());
        }

        String s;
        String who;
        if (xml.type.equals("send")) {
            if (!xml.ourMessage.isEmpty()) {
                who = ((XMLMessage)xml.ourMessage.get(0)).from;
                whom = ((XMLMessage)xml.ourMessage.get(0)).to;
                if (who.compareTo(whom) > 0) {
                    s = who + "&" + whom;
                } else {
                    s = whom + "&" + who;
                }

                if (!this.convertations.containsKey(s)) {
                    this.convertations.put(s, new ArrayList());
                }

                Iterator var7 = xml.ourMessage.iterator();

                while(var7.hasNext()) {
                    XMLMessage m = (XMLMessage)var7.next();
                    ((List)this.convertations.get(s)).add(who + ":  " + m.message);
                }
            }

            outputStream.write("OK".getBytes());
        }

        if (xml.type.equals("getMessages")) {
            who = ((XMLMessage)xml.ourMessage.get(0)).from;
            whom = ((XMLMessage)xml.ourMessage.get(0)).to;
            if (who.compareTo(whom) > 0) {
                s = who + "&" + whom;
            } else {
                s = whom + "&" + who;
            }

            outputStream.write(XML.messages((List)this.convertations.get(s)).getBytes());
        }

    }

    public Server() throws Exception {
        this.serverSocket = new ServerSocket(9876, 0, InetAddress.getByName("localhost"));
        this.allUsers = new ArrayList();
        this.convertations = new HashMap();
        this.connections();
    }
}
