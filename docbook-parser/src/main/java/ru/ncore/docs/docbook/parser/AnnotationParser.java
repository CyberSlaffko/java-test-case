package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

import static ru.ncore.docs.docbook.document.ChapterContent.ChapterType.ANNOTATION;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class AnnotationParser {
    private static final String ANNOTATION_TITLE = "Аннотация";
    private static final int ANNOTATION_INDEX = 0;
    private Document xmlDocument;

    public AnnotationParser(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    public void parse(ru.ncore.docs.docbook.Document document) {
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "/d:book/d:preface");

        if (0 == nodes.getLength()) {
            return;
        }

        Chapter annotation = parseChapter(nodes.item(ANNOTATION_INDEX));
        if (null == annotation.getTitle() || annotation.getTitle().isEmpty()) {
            annotation.setTitle(ANNOTATION_TITLE);
        }

        document.setAnnotaion(annotation);
    }

    private Chapter parseChapter(Node chapterNode) {
        NodeList nodes = XMLUtils.getNodes(chapterNode, "./*");
        Chapter chapter = new Chapter();

        List<ChapterContent> contentList = chapter.getContentList();
        int nextLevel = chapter.getLevel() + 1;

        for(int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("info")) {
                chapter.setTitle( XMLUtils.getNodeValue(contentNode, "./d:title/text()"));
            }
            else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel, ANNOTATION));
                }
            }
        }
        return chapter;
    }
}
