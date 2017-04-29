package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ChapterParser {
    private org.w3c.dom.Document xmlDocument;

    public ChapterParser(org.w3c.dom.Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public void parse(Document document) {
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "/d:book/d:chapter");

        List<Chapter> chaptersList = document.getChaptersList();

        for(int i = 0; i < nodes.getLength(); i++) {
            chaptersList.add(parseChapter(nodes.item(i)));
        }
    }

    private Chapter parseChapter(Node chapterNode) {
//        String nodeValue = XMLUtils.getNodeValue(chapterNode, "./title/text()");

        NodeList nodes = XMLUtils.getNodes(chapterNode, "./*");
        Chapter chapter = new Chapter();

        List<ChapterContent> contentList = chapter.getContentList();
        int nextLevel = chapter.getLevel() + 1;

        for(int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                chapter.setTitle( XMLUtils.getNodeValue(contentNode, "./text()"));
            }
            else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel));
                }
            }
        }
        return chapter;
    }
}
