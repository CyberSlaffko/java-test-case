package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.SECTION;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class SectionParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./*");

        ChapterContent section = new ChapterContent();
        section.setType(SECTION);
        section.setLevel(currentLevel);
        section.setChapterType(chapterType);
        List<ChapterContent> contentList = section.getContentList();
        int nextLevel = currentLevel + 1;

        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("xml:id");
        if (null != attr) {
            String xrefLink = MD5Utils.HexMD5ForString(attr.getValue());
            section.setBookmarkId(xrefLink);
            document.addLink(xrefLink, SECTION);
        }

        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                section.setTitle(XMLUtils.getNodeValue(contentNode, "./text()"));
            } else {
                IContentParser parser = ContentParserFactory.getParserFor(contentNode, document);
                if (parser != null) {
                    contentList.add(parser.parse(nextLevel, chapterType));
                }
            }
        }
        return section;
    }
}

