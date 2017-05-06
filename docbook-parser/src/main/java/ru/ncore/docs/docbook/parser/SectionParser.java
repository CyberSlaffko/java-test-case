package ru.ncore.docs.docbook.parser;

import com.sun.org.apache.xerces.internal.dom.DeferredElementNSImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.util.List;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.SECTION;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class SectionParser extends ObjectWithTitleParser {
    protected ChapterContent.Type getChapterContentType() {
        return SECTION;
    }
}

