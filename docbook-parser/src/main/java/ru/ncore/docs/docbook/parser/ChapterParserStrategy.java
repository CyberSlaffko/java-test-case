package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.SECTION;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public abstract class ChapterParserStrategy {
    private org.w3c.dom.Document xmlDocument;
    private ChapterContent.ChapterType chapterType;
    private Document document;

    public ChapterParserStrategy(org.w3c.dom.Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public void parse(Document document) {
        this.document = document;
        NodeList nodes = XMLUtils.getNodes(xmlDocument, xpath());

        List<Chapter> chaptersList = getDataList(document);

        for(int i = 0; i < nodes.getLength(); i++) {
            chaptersList.add(parseChapter(nodes.item(i)));
        }
    }

    protected abstract List<Chapter> getDataList(Document document);

    protected abstract String xpath();

    private Chapter parseChapter(Node chapterNode) {
        Chapter chapter = new Chapter();

        List<ChapterContent> contentList = chapter.getContentList();
        int nextLevel = chapter.getLevel() + 1;

        Attr attr = ((DeferredElementNSImpl) chapterNode).getAttributeNode("xml:id");
        if (null != attr) {
            String xrefLink = MD5Utils.HexMD5ForString(attr.getValue());
            chapter.setBookmarkId(xrefLink);
            document.addLink(xrefLink, SECTION);
        }

        NodeList nodes = XMLUtils.getNodes(chapterNode, "./*");
        for(int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                chapter.setTitle( XMLUtils.getNodeValue(contentNode, "./text()"));
            }
            else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode, document);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel, getChapterType()));
                }
            }
        }
        return chapter;
    }

    protected abstract ChapterContent.ChapterType getChapterType();
}
