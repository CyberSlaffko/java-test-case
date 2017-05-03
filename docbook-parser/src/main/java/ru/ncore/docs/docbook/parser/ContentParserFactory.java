package ru.ncore.docs.docbook.parser;

import org.w3c.dom.Node;

/**
 * Created by Вячеслав Молоков on 29.04.2017.
 */
public class ContentParserFactory {

    public static IContentParser getParserFor(Node contentNode) {
        switch (contentNode.getNodeName()) {
            case "section": return new SectionParser().setNode(contentNode);
            case "para": return new ParaParser().setNode(contentNode);
            case "itemizedlist": return new ItemizedListParser().setNode(contentNode);
            default: {
                System.out.printf("[W001] Unknown tag: %s\n", contentNode.getNodeName());
                return null;
            }
        }
    }
}
