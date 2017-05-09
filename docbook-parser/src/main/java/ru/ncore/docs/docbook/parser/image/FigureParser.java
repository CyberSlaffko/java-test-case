package ru.ncore.docs.docbook.parser.image;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.parser.algorithms.ObjectWithTitleParserAlgorithm;
import ru.ncore.docs.docbook.utils.XMLUtils;

import java.util.List;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public class FigureParser extends ObjectWithTitleParserAlgorithm {
    @Override
    protected ChapterContent.Type getType() {
        return ChapterContent.Type.FIGURE;
    }
}
