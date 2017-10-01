package ru.ncore.docs.docbook.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.parser.image.FigureParser;
import ru.ncore.docs.docbook.parser.image.ImageDataParser;
import ru.ncore.docs.docbook.parser.para.PhraseParser;
import ru.ncore.docs.docbook.parser.para.PlainTextParser;
import ru.ncore.docs.docbook.parser.para.XRefParser;
import ru.ncore.docs.docbook.parser.table.*;
import ru.ncore.docs.docbook.utils.XMLUtils;

import static ru.ncore.docs.docbook.document.ChapterContent.Type;
import static ru.ncore.docs.docbook.document.ChapterContent.Type.*;

/**
 * Фабрика для определения конкретного парсера для указанного узла XML-документа
 */
public class ContentParserFactory {
    final static Logger logger = LoggerFactory.getLogger(ContentParserFactory.class);

    /**
     * Определяет парсер для указанного элемента contentNode
     * @param contentNode Узел XML-документа для которого нужно получить парсер
     * @param document Документ, который обрабатывается
     * @return null если парсер не найден, иначе возвращается подходящий парсер
     */
    public static IContentParser getParserFor(Node contentNode, Document document) {
        IContentParser iContentParser = __getParserFor(contentNode, document);

        if (iContentParser == null) {
            return null;
        }

        return iContentParser.setNode(contentNode).setDocument(document);
    }

    private static IContentParser __getParserFor(Node contentNode, Document document) {
        switch (contentNode.getNodeName()) {
            case "section":
                return new SectionParser();
            case "#text":
                return new PlainTextParser();
            case "emphasis":
            case "para":
                return new ObjectParser(PARA);
            case "xref":
                return new XRefParser();
            case "entry":
                return new TableEntryParser();
            case "table":
                return new TableParser();
            case "tgroup":
                return new TGroupParser();
            case "thead":
                return new THeadParser();
            case "tbody":
                return new TBodyParser();
            case "row":
                return new ObjectParser(TABLE_ROW);
            case "colspec":
                return new TableColspecParser();
            case "figure":
                return new FigureParser();
            case "programlisting":
                return new ProgramListingParser();
            case "mediaobject":
                return new ObjectParser(MEDIAOBJECT);
            case "inlineequation":
                return new ObjectParser(INLINEEQUATION);
            case "inlinemediaobject":
                return new ObjectParser(INLINEMEDIAOBJECT);
            case "imageobject":
                return new ObjectParser(IMAGEOBJECT);
            case "imagedata":
                return new ImageDataParser();
            case "variablelist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM);
            case "orderedlist":
                return new ItemizedListParser(Type.ORDEREDLIST, Type.ORDEREDLIST_ITEM);
            case "itemizedlist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM);
            case "oxy_custom_start":
            case "oxy_custom_end":
            case "#comment":
            case "lb":
            case "textobject":
                // NOOP
                return null;
            case "link":
            case "phrase":
                return new PhraseParser();
            default: {
                logger.warn(String.format("Unknown tag: %s", contentNode.getNodeName()));
                return null;
            }
        }
    }

    private static class ProgramListingParser extends LeafContentParserAlgorithm {
        @Override
        protected String getTitle() {
            return XMLUtils.getNodeValueNoTrim(xmlDocument, "./text()");
        }

        @Override
        protected Type getType() {
            return PROGRAMLISTING;
        }
    }
}
