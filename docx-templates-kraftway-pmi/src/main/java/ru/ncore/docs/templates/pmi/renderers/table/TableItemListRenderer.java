package ru.ncore.docs.templates.pmi.renderers.table;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 10.05.2017.
 */
public class TableItemListRenderer extends ITableContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        for (ChapterContent chapterContent : contentData.getContentList()) {
            ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(chapterContent, document, relationManager);
            if (renderer != null) {
                renderer.render(wordDocumentData, width, style);
            }
        }
    }
}
