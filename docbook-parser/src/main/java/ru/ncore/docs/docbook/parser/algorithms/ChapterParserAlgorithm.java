package ru.ncore.docs.docbook.parser.algorithms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.ContentParserFactory;
import ru.ncore.docs.docbook.parser.IContentParser;
import ru.ncore.docs.docbook.utils.MD5Utils;
import ru.ncore.docs.docbook.utils.XMLUtils;

import java.util.List;

/**
 * Алгоритм разбора глав, аннотации, приложений
 *
 * Предназначен для унификации разбора узлов документа, у которых есть вложенный тег title
 */
public abstract class ChapterParserAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(ChapterParserAlgorithm.class);
    private org.w3c.dom.Document xmlDocument;
    private Document document;

    public ChapterParserAlgorithm(org.w3c.dom.Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public void parse(Document document) {
        this.document = document;
        NodeList nodes = XMLUtils.getNodes(xmlDocument, xpath());

        if(nodes == null) {
            return;
        }

        List<ChapterContent> chaptersList = getDataList(document);

        for (int i = 0; i < nodes.getLength(); i++) {
            chaptersList.add(parseChapter(nodes.item(i)));
        }
    }

    protected abstract List<ChapterContent> getDataList(Document document);

    protected abstract String xpath();

    private ChapterContent parseChapter(Node chapterNode) {
        ChapterContent chapter = new ChapterContent();
        chapter.setLevel(1);

        List<ChapterContent> contentList = chapter.getContentList();
        int nextLevel = chapter.getLevel() + 1;

        NamedNodeMap attributes = chapterNode.getAttributes();
        for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
            Node item = attributes.item(attrIndex);
            switch(item.getNodeName()) {
                case "xml:id":
                    String xrefLink = MD5Utils.HexMD5ForString(item.getNodeValue());
                    chapter.setBookmarkId(xrefLink);
                    document.addLink(xrefLink, getType());
                    break;
                default:
                    logger.info(String.format("Tag %s - Unknown attribute %s with value %s", xmlDocument.getLocalName(), item.getNodeName(), item.getNodeValue()));
            }
        }

        NodeList nodes = XMLUtils.getNodes(chapterNode, "./*");
        if (nodes == null) {
            return chapter;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                chapter.setTitle(XMLUtils.getNodeValue(contentNode, "./text()"));
            } else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode, document);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel, getChapterType()));
                }
            }
        }
        return chapter;
    }

    protected abstract ChapterContent.Type getType();

    protected abstract ChapterContent.ChapterType getChapterType();
}
