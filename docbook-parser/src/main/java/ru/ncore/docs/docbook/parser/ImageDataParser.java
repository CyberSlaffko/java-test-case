package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.MD5Utils;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class ImageDataParser extends LeafContentParserAlgorithm {
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
            String xrefLink = attr.getValue();
            content.setTitle(xrefLink);
            document.addImage(xrefLink);
        }
    }
}
