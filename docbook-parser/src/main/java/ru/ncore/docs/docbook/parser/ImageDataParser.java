package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.MD5Utils;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class ImageDataParser extends LeafContentParserAlgorithm {
    static final Logger logger = LoggerFactory.getLogger(ImageDataParser.class);
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
        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("fileref");
        if (null != attr) {
            String baseURI = xmlDocument.getBaseURI();
            Path rootPath = Paths.get(URI.create(baseURI)).getParent();
            String xrefLink = Paths.get(rootPath.toString(), attr.getValue()).toAbsolutePath().toString();
            logger.debug(String.format("Image with path: %s", xrefLink));
            content.setTitle(xrefLink);
            document.addImage(xrefLink);
        }

        Attr widthAttr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("width");
        if (null != widthAttr) {
            String xrefLink = widthAttr.getValue();
            logger.debug(String.format("Image with width: %s", xrefLink));
            List<ChapterContent> contentList = content.getContentList();
            ChapterContent chapterContent = new ChapterContent();
            chapterContent.setType(ChapterContent.Type.WIDTH);
            chapterContent.setTitle(xrefLink);
            contentList.add(chapterContent);
        }
    }
}
