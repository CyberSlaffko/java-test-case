package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class SectionParser implements IContentParser {
    private Node xmlDocument;

    public SectionParser() {
    }

    @Override
    public IContentParser setNode(Node xmlDocument) {
        this.xmlDocument = xmlDocument;
        return this;
    }

    @Override
    public ChapterContent parse(int currentLevel) {
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./*");

        ChapterContent section = new ChapterContent();
        section.setLevel(currentLevel);
        List<ChapterContent> contentList = section.getContentList();
        int nextLevel = currentLevel + 1;

        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                section.setTitle(XMLUtils.getNodeValue(contentNode, "./text()"));
            } else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel));
                }
            }
        }
        return section;
    }
}

