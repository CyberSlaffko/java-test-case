package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.MD5Utils;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class XRefParser extends LeafContentParserAlgorithm {
    @Override
    protected void parseAttributes(ChapterContent content) {
        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("linkend");
        if (null != attr) {
            String xrefLink = attr.getValue();
            content.setBookmarkId(MD5Utils.HexMD5ForString(xrefLink));
        }
    }

    @Override
    protected String getTitle() {
        return null;
    }

    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.XREF;
    }
}
