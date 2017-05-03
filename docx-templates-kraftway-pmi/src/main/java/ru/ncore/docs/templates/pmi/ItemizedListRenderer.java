package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ItemizedListRenderer extends IContentRenderer {
    @Override
    void render(OutputStream wordDocumentData) {
        String templatePath = "templates/document/itemizedlist_1.twig";

        for(ChapterContent itemList: contentData.getContentList()) {
            if (0 <= itemList.getContentList().size()) {
                ParaRenderer paraRenderer = new ParaRenderer();
                paraRenderer.setTemplatePath(templatePath);
                paraRenderer.setContent(itemList.getContentList().get(0));
                paraRenderer.render(wordDocumentData);
            }
        }
    }
}
