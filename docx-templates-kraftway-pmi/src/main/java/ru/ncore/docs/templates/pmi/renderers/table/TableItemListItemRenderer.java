package ru.ncore.docs.templates.pmi.renderers.table;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.renderers.TemplateUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by Вячеслав Молоков on 10.05.2017.
 */
public class TableItemListItemRenderer extends ITableContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();

        for (ChapterContent chapterContent : contentData.getContentList()) {
            if (chapterContent.getType() == ChapterContent.Type.PARA) {
                for (ChapterContent innerContent : chapterContent.getContentList()) {
                    ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(innerContent, document, relationManager);
                    if (renderer != null) {
                        renderer.render(contentBuffer, width, style);
                    }
                }
                break;
            }

            ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(chapterContent, document, relationManager);
            if (renderer != null) {
                renderer.render(contentBuffer, width, style);
            }
        }

        String buf = contentBuffer.toString();
        if (buf.isEmpty()) {
            return;
        }

        TemplateUtils.render("templates/document/table/item_list.twig", wordDocumentData, new HashMap<String, String>(){{
            put("body", buf);
        }});
    }
}
