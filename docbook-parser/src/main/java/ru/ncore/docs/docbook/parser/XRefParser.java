package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import ru.ncore.docs.docbook.document.ChapterContent;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class XRefParser extends IContentParser {
    @Override
    public ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
        ChapterContent para = new ChapterContent();
        para.setType(ChapterContent.Type.XREF);
        para.setLevel(currentLevel);
        para.setChapterType(chapterType);

        Attr attr = ((DeferredElementNSImpl) xmlDocument).getAttributeNode("linkend");
        if (null != attr) {
            String xrefLink = attr.getValue();
            para.setBookmarkId(MD5Utils.HexMD5ForString(xrefLink));
            return para;
        }

        return null;
    }
}
