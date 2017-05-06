package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;

import static ru.ncore.docs.docbook.document.ChapterContent.Type;
import static ru.ncore.docs.docbook.document.ChapterContent.Type.SECTION;
import static ru.ncore.docs.docbook.document.ChapterContent.Type.TABLE;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ContentParserFactory {

    public static IContentParser getParserFor(Node contentNode, Document document) {
        IContentParser iContentParser = __getParserFor(contentNode, document);

        if (iContentParser == null) {
            return null;
        }

        return iContentParser.setNode(contentNode).setDocument(document);
    }

    public static IContentParser __getParserFor(Node contentNode, Document document) {
        switch (contentNode.getNodeName()) {
            case "section":
                return new SectionParser();
            case "#text":
                return new PlainTextParser();
            case "xref":
                return new XRefParser();
            case "table":
                return new TableParser();
            case "figure":
                return new FigureParser();
            case "programlisting":
                return new ProgramListingParser();
            case "para":
                return new ParaParser();
            case "variablelist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM);
            case "orderedlist":
                return new ItemizedListParser(Type.ORDEREDLIST, Type.ORDEREDLIST_ITEM);
            case "itemizedlist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM);
            case "oxy_custom_start":
            case "oxy_custom_end":
                // NOOP
                return null;
            case "link":
            case "phrase":
                return new PhraseParser();
            default: {
                System.out.printf("[W001] Unknown tag: %s\n", contentNode.getNodeName());
                return null;
            }
        }
    }

    private static class ProgramListingParser extends IContentParser {
        @Override
        ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
            ChapterContent para = new ChapterContent();
            para.setType(ChapterContent.Type.PROGRAMLISTING);
            para.setLevel(currentLevel);
            para.setChapterType(chapterType);
            para.setTitle(XMLUtils.getNodeValueNoTrim(xmlDocument, "./text()"));
            return para;
        }
    }

    private static class TableParser extends IContentParser {
            @Override
            ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
                ChapterContent para = new ChapterContent();
                para.setType(Type.TABLE);
                para.setChapterType(chapterType);
                para.setLevel(currentLevel);
                para.setTitle( XMLUtils.getNodeValue(xmlDocument, "./d:title/text()"));

                Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("xml:id");
                if (null != attr) {
                    String xrefLink = MD5Utils.HexMD5ForString(attr.getValue());
                    para.setBookmarkId(xrefLink);
                    document.addLink(xrefLink, TABLE);
                }

                return para;
            }
        }
}
