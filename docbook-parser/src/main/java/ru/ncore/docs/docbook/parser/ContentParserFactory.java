package ru.ncore.docs.docbook.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.parser.algorithms.LeafContentParserAlgorithm;
import ru.ncore.docs.docbook.utils.XMLUtils;

import static ru.ncore.docs.docbook.document.ChapterContent.Type;
import static ru.ncore.docs.docbook.document.ChapterContent.Type.*;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ContentParserFactory {
    final static Logger logger = LoggerFactory.getLogger(ContentParserFactory.class);

    public static IContentParser getParserFor(Node contentNode, Document document) {
        IContentParser iContentParser = __getParserFor(contentNode, document);

        if (iContentParser == null) {
            return null;
        }

        return iContentParser.setNode(contentNode).setDocument(document);
    }

    public static IContentParser __getParserFor(Node contentNode, Document document) {
        switch (contentNode.getNodeName()) {
            case "section":
                return new SectionParser();
            case "#text":
                return new PlainTextParser();
            case "xref":
                return new XRefParser();
            case "table":
                return new TableParser();
            case "figure":
                return new FigureParser();
            case "programlisting":
                return new ProgramListingParser();
            case "para":
                return new ObjectParser(PARA);
            case "mediaobject":
                return new ObjectParser(MEDIAOBJECT);
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
