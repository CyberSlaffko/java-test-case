package ru.ncore.docs.docbook.parser.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ContentParserAlgorithm;

/**
 * Парсер для тега colspec в таблицах
 */
public class TableColspecParser extends ContentParserAlgorithm {
    static final private Logger logger = LoggerFactory.getLogger(TableColspecParser.class);

    @Override
    protected void parseAttributes(ChapterContent content) {
        NamedNodeMap attributes = xmlDocument.getAttributes();
        for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
            Node item = attributes.item(attrIndex);
            switch(item.getNodeName()) {
                case "colnum":
                    content.addAdditionalAttribute("colnum", item.getNodeValue());
                    break;
                case "colname":
                    content.setTitle(item.getNodeValue());
                    break;
                case "colwidth":
                    content.addAdditionalAttribute("width", item.getNodeValue());
                    break;
                default:
                    logger.info(String.format("Tag %s - Unknown attribute %s with value %s", xmlDocument.getLocalName(), item.getNodeName(), item.getNodeValue()));
            }
        }
        if(null == content.getTitle()) {
            content.setTitle(String.format("autonamedcol%s", content.getUuid()));
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TABLE_COLSPEC;
    }
}
