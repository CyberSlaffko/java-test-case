package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.utils.XMLUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ItemizedListParser extends IContentParser {
    private ChapterContent.Type listType;
    private ChapterContent.Type itemType;
    private ChapterContent.ChapterType chapterType;

    public ItemizedListParser(ChapterContent.Type listType, ChapterContent.Type itemType) {
        this.listType = listType;
        this.itemType = itemType;
    }

    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        this.chapterType = chapterType;
        ChapterContent itemizedList = new ChapterContent();
        itemizedList.setType(listType);
        itemizedList.setChapterType(chapterType);
        itemizedList.setLevel(1);
        NodeList nodes = XMLUtils.getNodes(xmlDocument, "./d:listitem");
        List<ChapterContent> listItems = itemizedList.getContentList();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node contentNode = nodes.item(i);

            listItems.add(parseItem(contentNode));
        }

        return itemizedList;
    }

    private ChapterContent parseItem(Node contentNode) {
        ChapterContent listItem = new ChapterContent();
        listItem.setType(itemType);
        listItem.setChapterType(chapterType);
        listItem.setLevel(1);

        NodeList nodes = XMLUtils.getNodes(contentNode, "./*");
        List<ChapterContent> listItems = listItem.getContentList();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);

            IContentParser parser = ContentParserFactory.getParserFor(node, document);
            if (parser != null) {
                listItems.add(parser.parse(1, chapterType));
            }
        }

        return listItem;
    }
}
