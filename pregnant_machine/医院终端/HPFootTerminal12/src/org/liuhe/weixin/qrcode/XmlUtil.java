package org.liuhe.weixin.qrcode;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlUtil {
	
	public static Map<String, String> parseXML(String protocolXML) {
		protocolXML = protocolXML.replaceAll("(\r|\n|\t)", "");
		Map<String, String> map = new HashMap<String, String>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(protocolXML)));
            Element root = doc.getDocumentElement();
            NodeList books = root.getChildNodes();
            if (books != null) {
               for (int i = 0; i < books.getLength(); i++) {
                    Node book = books.item(i);
                    String node_name = book.getNodeName();
                    String node_value = book.getFirstChild().getNodeValue();
                    map.put(node_name,node_value);
                }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }
         return map;
    }
	
}