package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.HashMap;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public abstract class XMLUtils {
    static public String getNodeValue(Document xmlDocument, String xpath) {
        XPath xPath = getxPath();
        try {
            Node node = ((Node) xPath.evaluate(xpath, xmlDocument.getDocumentElement(), XPathConstants.NODE));
            if (null == node) {
                return null;
            }

            return node.getNodeValue().trim().replaceAll("\\s+", " ");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    static public String getNodeValueNoTrim(Node xmlDocument, String xpath) {
        XPath xPath = getxPath();
        try {
            Node node = ((Node) xPath.evaluate(xpath, xmlDocument, XPathConstants.NODE));
            if (null == node) {
                return "";
            }

            return node.getNodeValue();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return "";
        }
    }

    static public String getNodeValue(Node xmlDocument, String xpath) {
        return getNodeValueNoTrim(xmlDocument, xpath).trim().replaceAll("\\s+", " ");
    }

    static public NodeList getNodes(Document xmlDocument, String xpath) {
        XPath xPath = getxPath();
        try {
            NodeList nodeList = ((NodeList) xPath.evaluate(xpath, xmlDocument.getDocumentElement(), XPathConstants.NODESET));

            return nodeList;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static XPath getxPath() {
        XPath xPath = XPathFactory.newInstance().newXPath();

        HashMap<String, String> prefMap = new HashMap<String, String>() {{
            put("d", "http://docbook.org/ns/docbook");
            put("xi", "http://www.w3.org/2001/XInclude");
            put("xlink", "http://www.w3.org/1999/xlink");
        }};
        SimpleNamespaceContext namespaces = new SimpleNamespaceContext(prefMap);
        xPath.setNamespaceContext(namespaces);

        return xPath;
    }

    static public NodeList getNodes(Node xmlDocument, String xpath) {
        XPath xPath = getxPath();
        try {
            NodeList nodeList = ((NodeList) xPath.evaluate(xpath, xmlDocument, XPathConstants.NODESET));

            return nodeList;
        } catch (XPathExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
