package ru.ncore.docs.docbook.parser.algorithms;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.ContentParserFactory;
import ru.ncore.docs.docbook.parser.IContentParser;
import ru.ncore.docs.docbook.utils.MD5Utils;
import ru.ncore.docs.docbook.utils.XMLUtils;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public abstract class ObjectWithTitleParserAlgorithm extends ContentParserAlgorithm {
    private ChapterContent content;

    @Override
    protected void parseChilds(ChapterContent content) {
        this.content = content;
        super.parseChilds(content);
    }

    @Override
    protected void parseChildsInnerLoop(List<ChapterContent> contentList, int nextLevel, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            if (contentNode.getNodeName().equals("title")) {
                content.setTitle(XMLUtils.getNodeValue(contentNode, "./text()"));
            } else {
                parseSingleNode(contentList, nextLevel, contentNode);
            }
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }
}
