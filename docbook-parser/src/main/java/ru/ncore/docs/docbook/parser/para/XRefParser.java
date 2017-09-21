package ru.ncore.docs.docbook.parser.para;

//import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.MD5Utils;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class XRefParser extends LeafContentParserAlgorithm {
    Logger logger = LoggerFactory.getLogger(XRefParser.class);
    @Override
    protected void parseAttributes(ChapterContent content) {
        Attr attr = ((/*DeferredElementNSImpl*/Element) xmlDocument).getAttributeNode("linkend");
        if (null != attr) {
            String xrefLink = attr.getValue();
            content.setTitle(xrefLink);
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
