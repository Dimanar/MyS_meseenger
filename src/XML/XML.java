package XML;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.text.Format;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XML {
    public XML() {
    }

    public static XMLResponse parse(String XML) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        XML.Handler handler = new XML.Handler();

        try {
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(handler);
            xmlReader.parse(new InputSource(new ByteArrayInputStream(XML.getBytes("utf-8"))));
        } catch (Exception var5) {
        }

        return handler.getResponse();
    }

    public static String login(String name) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("login");
            out.writeEndElement();
            out.writeStartElement("login");
            out.writeCharacters(name);
            out.writeEndElement();
            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var3) {
            return "";
        }
    }

    public static String getUsers() {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("getUsers");
            out.writeEndElement();
//            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var2) {
            return "";
        }
    }

    public static String logout(String name) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("logout");
            out.writeEndElement();
            out.writeStartElement("login");
            out.writeCharacters(name);
            out.writeEndElement();
//            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var3) {
            return "";
        }
    }

    public static String send(String from, String to, String message) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("send");
            out.writeEndElement();
            out.writeStartElement("message");
            out.writeStartElement("who");
            out.writeCharacters(from);
            out.writeEndElement();
            out.writeStartElement("whom");
            out.writeCharacters(to);
            out.writeEndElement();
            out.writeStartElement("text");
            out.writeCharacters(message);
            out.writeEndElement();
//            out.writeEndElement();
//            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var5) {
            return "";
        }
    }

    public static String users(List<String> allUsers) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("users");
            out.writeEndElement();
            Iterator var3 = allUsers.iterator();

            while(var3.hasNext()) {
                String user = (String)var3.next();
                out.writeStartElement("message");
                out.writeStartElement("who");
                out.writeCharacters(user);
                out.writeEndElement();
                out.writeEndElement();
            }

            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var5) {
            return "";
        }
    }

    public static String getMessages(String from, String to) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("getMessages");
            out.writeEndElement();
            out.writeStartElement("message");
            out.writeStartElement("who");
            out.writeCharacters(from);
            out.writeEndElement();
            out.writeStartElement("whom");
            out.writeCharacters(to);
            out.writeEndElement();
//            out.writeEndElement();
//            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var4) {
            return "";
        }
    }

    public static String messages(List<String> allMessages) {
        try {
            StringWriter stringOut = new StringWriter();
            XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(stringOut);
            out.writeStartDocument();
            out.writeStartElement("xml");
            out.writeStartElement("type");
            out.writeCharacters("users");
            out.writeEndElement();
            Iterator var3 = allMessages.iterator();

            while(var3.hasNext()) {
                String message = (String)var3.next();
                out.writeStartElement("message");
                out.writeStartElement("text");
                out.writeCharacters(message);
                out.writeEndElement();
                out.writeEndElement();
            }

            out.writeEndElement();
            out.writeEndDocument();
            out.close();
            return stringOut.toString();
        } catch (Exception var5) {
            return "";
        }
    }

    private static class Handler extends DefaultHandler {
        private Format format;
        private XMLResponse xml;
        private XMLMessage message;
        private String currentTag;

        private Handler() {
        }

        public void startElement(String uri, String localName, String tag, Attributes attributes) throws SAXException {
            this.currentTag = tag;
            String var5 = this.currentTag;
            byte var6 = -1;
            switch(var5.hashCode()) {
                case 118807:
                    if (var5.equals("xml")) {
                        var6 = 0;
                    }
                    break;
                case 954925063:
                    if (var5.equals("message")) {
                        var6 = 1;
                    }
            }

            switch(var6) {
                case 0:
                    this.xml = new XMLResponse();
                    this.xml.ourMessage = new ArrayList();
                    break;
                case 1:
                    this.message = new XMLMessage();
            }

        }

        public void characters(char[] characters, int start, int lenght) throws SAXException {
            String text = new String(characters, start, lenght);
            String var5 = this.currentTag;
            byte var6 = -1;
            switch(var5.hashCode()) {
                case -892481550:
                    if (var5.equals("status")) {
                        var6 = 4;
                    }
                    break;
                case 117694:
                    if (var5.equals("who")) {
                        var6 = 1;
                    }
                    break;
                case 3556653:
                    if (var5.equals("text")) {
                        var6 = 3;
                    }
                    break;
                case 3575610:
                    if (var5.equals("type")) {
                        var6 = 0;
                    }
                    break;
                case 3648623:
                    if (var5.equals("whom")) {
                        var6 = 2;
                    }
                    break;
                case 103149417:
                    if (var5.equals("login")) {
                        var6 = 5;
                    }
            }

            switch(var6) {
                case 0:
                    this.xml.type = text;
                    break;
                case 1:
                    this.message.from = text;
                    break;
                case 2:
                    this.message.to = text;
                    break;
                case 3:
                    this.message.message = text;
                    break;
                case 4:
                    this.xml.status = text;
                    break;
                case 5:
                    this.xml.name = text;
            }

        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
            byte var5 = -1;
            switch(qName.hashCode()) {
                case 954925063:
                    if (qName.equals("message")) {
                        var5 = 0;
                    }
                default:
                    switch(var5) {
                        case 0:
                            this.xml.ourMessage.add(this.message);
                        default:
                    }
            }
        }

        XMLResponse getResponse() {
            return this.xml;
        }
    }
}
