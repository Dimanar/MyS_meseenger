
package Client;

import XML.XML;
import java.net.Socket;

public class Client extends Thread {
    private String name;
    public String users = "NULL";
    public String allMessage = "NULL";
    public String conversation;

    public boolean setOnline(String name) {
        String whoMessage = this.sendMessage(XML.login(name));
        return whoMessage.equals("OK");
    }

    public void setOffline(String name) {
        this.sendMessage(XML.logout(name));
    }

    public boolean getUsers() {
        String user = this.sendMessage(XML.getUsers());
        if (!user.equals(this.users)) {
            this.users = user;
            return true;
        } else {
            return false;
        }
    }

    public boolean getMessages() {
        String ourMessage = this.sendMessage(XML.getMessages(this.name, this.conversation));
        if (!ourMessage.equals(this.allMessage)) {
            this.allMessage = ourMessage;
            return true;
        } else {
            return false;
        }
    }

    public Client(String name) {
        this.name = name;
        this.conversation = name;
    }

    public String sendMessage(String message) {
        byte[] storage = new byte[65536];

        try {
            Socket newSoket = new Socket("localhost", 9876);
            newSoket.getOutputStream().write(message.getBytes());
            int l = newSoket.getInputStream().read(storage);
            String result = new String(storage, 0, l);
            newSoket.close();
            return result;
        } catch (Exception var6) {
            return "Error";
        }
    }

    public void sendTo(String whom) {
        this.sendMessage(XML.send(this.name, this.conversation, whom));
    }
}
