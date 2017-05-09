package ru.ncore.docs.docbook.parser.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.MD5Utils;

/**
 * Парсер ячейки таблицы
 */
public class TableEntryParser extends ContentParserAlgorithm {
    private final static Logger logger = LoggerFactory.getLogger(TableEntryParser.class);
    @Override
    protected void parseAttributes(ChapterContent content) {
        NamedNodeMap attributes = xmlDocument.getAttributes();
        for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
            Node item = attributes.item(attrIndex);
            switch(item.getNodeName()) {
                case "morerows":
                    content.addAdditionalAttribute("morerows", item.getNodeValue());
                    break;
                case "namest":
                    content.addAdditionalAttribute("namest", item.getNodeValue());
                    break;
                case "nameend":
                    content.addAdditionalAttribute("nameend", item.getNodeValue());
                    break;
                default:
                    logger.info(String.format("Unknown attribute %s with value %s", item.getNodeName(), item.getNodeValue()));
            }
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TABLE_CELL;
    }
}
