package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ParaParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel) {
        ChapterContent para = new ChapterContent();
        para.setType(ChapterContent.Type.PARA);
        para.setLevel(currentLevel);
//        para.setTitle(XMLUtils.getNodeValue(xmlDocument, "./text()"));

//        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./*");
        List<ChapterContent> contentList = para.getContentList();
        int nextLevel = para.getLevel() + 1;
        NodeList nodes = xmlDocument.getChildNodes();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            IContentParser parser = ContentParserFactory.getParserFor(contentNode);
            if (parser != null) {
                contentList.add(parser.parse(nextLevel));
            }
        }

        return para;
    }
}
