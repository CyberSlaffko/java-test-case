package ru.ncore.docs.docbook.parser.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ContentParserAlgorithm;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TGroupParser extends ContentParserAlgorithm {
    static final private Logger logger = LoggerFactory.getLogger(TGroupParser.class);

    @Override
    protected void parseAttributes(ChapterContent content) {
        NamedNodeMap attributes = xmlDocument.getAttributes();
        for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
            Node item = attributes.item(attrIndex);
            switch(item.getNodeName()) {
                case "cols":
                    content.addAdditionalAttribute("columns", item.getNodeValue());
                    break;
                default:
                    logger.info(String.format("Tag %s - Unknown attribute %s with value %s", xmlDocument.getLocalName(), item.getNodeName(), item.getNodeValue()));
            }
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.TGROUP;
    }
}
