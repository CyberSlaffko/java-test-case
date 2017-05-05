package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class SectionParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./*");

        ChapterContent section = new ChapterContent();
        section.setType(ChapterContent.Type.SECTION);
        section.setLevel(currentLevel);
        section.setChapterType(chapterType);
        List<ChapterContent> contentList = section.getContentList();
        int nextLevel = currentLevel + 1;

        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("xml:id");
        if (null != attr) {
            String xrefLink = attr.getValue();
            section.setBookmarkId(MD5Utils.HexMD5ForString(xrefLink));
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                section.setTitle(XMLUtils.getNodeValue(contentNode, "./text()"));
            } else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel, chapterType));
                }
            }
        }
        return section;
    }
}

