package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static ru.ncore.docs.docbook.document.ChapterContent.Type;
import static ru.ncore.docs.docbook.document.ChapterContent.Type;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ContentParserFactory {

    public static IContentParser getParserFor(Node contentNode) {
        switch (contentNode.getNodeName()) {
            case "section": return new SectionParser().setNode(contentNode);
            case "#text": return new IContentParser() {
                @Override
                public ChapterContent parse(int currentLevel) {
                    ChapterContent para = new ChapterContent();
                    para.setType(ChapterContent.Type.TEXT);
                    para.setLevel(currentLevel);
                    para.setTitle(xmlDocument.getNodeValue().replaceAll("\\s+", " ").replaceAll("\\A\\s+\\z", ""));
                    return para;
                }
            }.setNode(contentNode);
            case "xref": return new IContentParser() {
                @Override
                public ChapterContent parse(int currentLevel) {
                    ChapterContent para = new ChapterContent();
                    para.setType(Type.XREF);
                    para.setLevel(currentLevel);

                    try {
                        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("linkend");
                        if (null != attr) {
                            String xrefLink = attr.getValue();
                            para.setTitle(HexMD5ForString(xrefLink));
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    return para;
                }
            }.setNode(contentNode);
            case "para": return new ParaParser().setNode(contentNode);
            case "variablelist": return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM).setNode(contentNode);
            case "orderedlist": return new ItemizedListParser(Type.ORDEREDLIST, Type.ORDEREDLIST_ITEM).setNode(contentNode);
            case "itemizedlist": return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM).setNode(contentNode);
            case "oxy_custom_start":
            case "oxy_custom_end":
                // NOOP
                return null;
            case "link":
            case "phrase": {
                return new IContentParser() {
                    @Override
                    public ChapterContent parse(int currentLevel) {
                        ChapterContent para = new ChapterContent();
                        para.setType(ChapterContent.Type.PHRASE);
                        para.setLevel(currentLevel);
                        para.setTitle(XMLUtils.getNodeValue(xmlDocument, "./text()"));
                        return para;
                    }
                }.setNode(contentNode);
            }
            default: {
                System.out.printf("[W001] Unknown tag: %s\n", contentNode.getNodeName());
                return null;
            }
        }
    }

    static String HexForByte(byte b) {
        String Hex = Integer.toHexString((int) b & 0xff);
        boolean hasTwoDigits = (2 == Hex.length());
        if(hasTwoDigits) return Hex;
        else return "0" + Hex;
    }

    static String HexForBytes(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(byte b : bytes) sb.append(HexForByte(b));
        return sb.toString();
    }

    static String HexMD5ForString(String text) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(text.getBytes());
        return HexForBytes(digest);
    }
}
