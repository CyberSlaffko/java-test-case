package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;
import ru.ncore.docs.docbook.document.ChapterContent;

import static ru.ncore.docs.docbook.document.ChapterContent.Type;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ContentParserFactory {

    public static IContentParser getParserFor(Node contentNode) {
        switch (contentNode.getNodeName()) {
            case "section":
                return new SectionParser().setNode(contentNode);
            case "#text":
                return new PlainTextParser().setNode(contentNode);
            case "xref":
                return new XRefParser().setNode(contentNode);
            case "table":
                return new IContentParser() {
                @Override
                ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
                    ChapterContent para = new ChapterContent();
                    para.setType(Type.TABLE);
                    para.setChapterType(chapterType);
                    para.setLevel(currentLevel);

                    return para;
                }
            }.setNode(contentNode);
            case "programlisting":
                return new IContentParser() {
                    @Override
                    ChapterContent parse(int currentLevel, ChapterContent.ChapterType chapterType) {
                        ChapterContent para = new ChapterContent();
                        para.setType(ChapterContent.Type.PROGRAMLISTING);
                        para.setLevel(currentLevel);
                        para.setChapterType(chapterType);
                        para.setTitle(XMLUtils.getNodeValueNoTrim(xmlDocument, "./text()"));
                        return para;
                    }
                }.setNode(contentNode);
            case "para":
                return new ParaParser().setNode(contentNode);
            case "variablelist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM).setNode(contentNode);
            case "orderedlist":
                return new ItemizedListParser(Type.ORDEREDLIST, Type.ORDEREDLIST_ITEM).setNode(contentNode);
            case "itemizedlist":
                return new ItemizedListParser(Type.ITEMLIST, Type.ITEMLIST_ITEM).setNode(contentNode);
            case "oxy_custom_start":
            case "oxy_custom_end":
                // NOOP
                return null;
            case "link":
            case "phrase":
                return new PhraseParser().setNode(contentNode);
            default: {
                System.out.printf("[W001] Unknown tag: %s\n", contentNode.getNodeName());
                return null;
            }
        }
    }
}
