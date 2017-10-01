package ru.ncore.docs.docbook.parser.image;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.XMLUtils;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class ImageDataParser extends LeafContentParserAlgorithm {
    private static final Logger logger = LoggerFactory.getLogger(ImageDataParser.class);
    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.IMAGEDATA;
    }

    @Override
    protected void parseAttributes(ChapterContent content) {
        NamedNodeMap attributes = xmlDocument.getAttributes();
        for (int attrIndex = 0; attrIndex < attributes.getLength(); attrIndex++) {
            Node item = attributes.item(attrIndex);
            switch(item.getNodeName()) {
                case "fileref": {
                    String baseURI = xmlDocument.getBaseURI();
                    Path rootPath = Paths.get(URI.create(baseURI)).getParent();
                    String xrefLink = Paths.get(rootPath.toString(), item.getNodeValue()).toAbsolutePath().toString();
                    logger.debug(String.format("Image with path: %s", xrefLink));
                    content.setTitle(xrefLink);
                    document.addImage(xrefLink);
                    break;
                }
                case "width": {
                    String width = item.getNodeValue();
                    logger.debug(String.format("Image with width: %s", width));
                    content.addAdditionalAttribute("width", width);
                    break;
                }
                default:
                    logger.info(String.format("Tag %s - Unknown attribute %s with value %s", xmlDocument.getLocalName(), item.getNodeName(), item.getNodeValue()));
            }
        }

        NodeList ancestorNodes = XMLUtils.getNodes(xmlDocument, "./ancestor::mediaobject");
        if (ancestorNodes != null && ancestorNodes.getLength() > 0)
            content.addAdditionalAttribute("isInMediaobject", "1");
    }
}
