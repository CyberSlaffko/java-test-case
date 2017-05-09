package ru.ncore.docs.docbook.parser.para;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.XMLUtils;

import static ru.ncore.docs.docbook.document.ChapterContent.Type.PHRASE;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class PhraseParser extends LeafContentParserAlgorithm {
    @Override
    protected String getTitle() {
        return XMLUtils.getNodeValue(xmlDocument, "./text()");
    }

    @Override
    protected ChapterContent.Type getType() {
        return PHRASE;
    }
}