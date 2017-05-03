package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ItemizedListParser implements IContentParser {
    private Node xmlDocument;

    @Override
    public IContentParser setNode(Node xmlDocument) {
        this.xmlDocument = xmlDocument;
        return this;
    }

    @Override
    public ChapterContent parse(int currentLevel) {
        ChapterContent itemizedList = new ChapterContent();
        itemizedList.setType(ChapterContent.Type.ITEMLIST);
        itemizedList.setLevel(1);

        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./d:listitem");
        List<ChapterContent> listItems = itemizedList.getContentList();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            listItems.add(parseItem(contentNode));
        }

        return itemizedList;
    }

    private ChapterContent parseItem(Node contentNode) {
        ChapterContent listItem = new ChapterContent();
        listItem.setType(ChapterContent.Type.ITEMLIST_ITEM);
        listItem.setLevel(1);

        NodeList nodes = XMLUtils.getNodes(contentNode, "./*");
        List<ChapterContent> listItems = listItem.getContentList();
        for(int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            IContentParser parser = ContentParserFactory.getParserFor(node);
            if (parser != null) {
                listItems.add(parser.parse(1));
            }
        }

        return listItem;
    }
}
