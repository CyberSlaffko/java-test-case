package ru.ncore.docs.templates.pmi.renderers.table;

import org.jtwig.JtwigModel;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

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

        
        String buf = new String(contentBuffer.toByteArray(), StandardCharsets.UTF_8);
        if (buf.isEmpty()) {
            return;
        }

        TemplateUtils.render("templates/document/table/item_list.twig", wordDocumentData, JtwigModel.newModel()
            .with("body", buf)
        );
    }
}
