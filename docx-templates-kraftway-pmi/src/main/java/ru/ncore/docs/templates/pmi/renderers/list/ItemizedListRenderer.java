package ru.ncore.docs.templates.pmi.renderers.list;

import com.google.common.collect.ImmutableMap;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.renderers.ParaRenderer;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ItemizedListRenderer extends IContentRenderer {
    final private static String itemizedTemplatePath = "templates/document/itemizedlist_1.twig";
    final private static String orderedTemplatePath = "templates/document/orderedlist_1.twig";

    @Override
    public void render(OutputStream wordDocumentData) {

        int numb = 0;
        String s = this.contentData.getAdditionalAttributes().get("num");
        String lvl = Integer.toString(this.contentData.getLevel()-1);

        for (ChapterContent itemList : contentData.getContentList()) {
            if (0 <= itemList.getContentList().size()) {
                ChapterContent content = itemList.getContentList().get(0);

                ParaRenderer paraRenderer = new ParaRenderer();
                if (ChapterContent.Type.ITEMLIST_ITEM == itemList.getType()) {
                    paraRenderer.setTemplatePath(itemizedTemplatePath);
                } else if (ChapterContent.Type.ORDEREDLIST_ITEM == itemList.getType()) {
                    paraRenderer.setTemplatePath(orderedTemplatePath);
                }
                paraRenderer.setContent(content);
                paraRenderer.setDocument(document, relationManager);
                //paraRenderer.render(wordDocumentData, ImmutableMap.of("numb", Integer.toString(++numb)));
                if (s != null) {
                    paraRenderer.render(wordDocumentData, ImmutableMap.of("numb", s, "lvl", lvl));
                } else {
                    paraRenderer.render(wordDocumentData);
                }
            }
        }
    }
}
