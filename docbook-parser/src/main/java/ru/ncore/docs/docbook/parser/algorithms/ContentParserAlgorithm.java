package ru.ncore.docs.docbook.parser.algorithms;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.ContentParserFactory;
import ru.ncore.docs.docbook.parser.IContentParser;
import ru.ncore.docs.docbook.utils.MD5Utils;

import java.util.List;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.TABLE;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public abstract class ContentParserAlgorithm extends IContentParser {
    protected int currentLevel;
    protected ChapterContent.ChapterType chapterType;

    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        this.currentLevel = currentLevel;
        this.chapterType = chapterType;
        ChapterContent para = new ChapterContent();
        para.setLevel(currentLevel);
        para.setChapterType(chapterType);

        para.setType(getType());
        para.setTitle(getTitle());

        parseChilds(para);
        parseAttributes(para);

        return para;
    }

    protected void parseChilds(ChapterContent content) {
        List<ChapterContent> contentList = content.getContentList();
        int nextLevel = currentLevel + 1;
        NodeList nodes = getChildNodes();
        parseChildsInnerLoop(contentList, nextLevel, nodes);
    }

    protected void parseChildsInnerLoop(List<ChapterContent> contentList, int nextLevel, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            parseSingleNode(contentList, nextLevel, contentNode);
        }
    }

    protected void parseSingleNode(List<ChapterContent> contentList, int nextLevel, Node contentNode) {
        IContentParser parser = ContentParserFactory.getParserFor(contentNode, document);
        if (parser != null) {
            contentList.add(parser.parse(nextLevel, chapterType));
        }
    }

    protected NodeList getChildNodes() {
        return xmlDocument.getChildNodes();
    }

    protected void parseAttributes(ChapterContent content) {
        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("xml:id");
        if (null != attr) {
            String xrefLink = MD5Utils.HexMD5ForString(attr.getValue());
            content.setBookmarkId(xrefLink);
            document.addLink(xrefLink, getType());
        }
    }

    protected abstract String getTitle();

    protected abstract ChapterContent.Type getType();
}
