package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ItemizedListRenderer extends IContentRenderer {
    @Override
    void render(OutputStream wordDocumentData) {
        String itemizedTemplatePath = "templates/document/itemizedlist_1.twig";
        String orderedTemplatePath = "templates/document/orderedlist_1.twig";

        for(ChapterContent itemList: contentData.getContentList()) {
            if (0 <= itemList.getContentList().size()) {
                ChapterContent content = itemList.getContentList().get(0);

                ParaRenderer paraRenderer = new ParaRenderer();
                if (ChapterContent.Type.ITEMLIST_ITEM == itemList.getType()) {
                    paraRenderer.setTemplatePath(itemizedTemplatePath);
                } else if (ChapterContent.Type.ORDEREDLIST_ITEM == itemList.getType()) {
                    paraRenderer.setTemplatePath(orderedTemplatePath);
                }
                paraRenderer.setContent(content);
                paraRenderer.setDocument(document);
                paraRenderer.render(wordDocumentData);
            }
        }
    }
}
