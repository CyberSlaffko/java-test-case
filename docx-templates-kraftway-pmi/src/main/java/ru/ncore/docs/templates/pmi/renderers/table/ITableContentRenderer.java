package ru.ncore.docs.templates.pmi.renderers.table;

import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public abstract class ITableContentRenderer {
    protected ChapterContent contentData;
    protected Document document;
    protected RelationManager relationManager;

    public abstract void render(OutputStream wordDocumentData, long width, String style);

    public ITableContentRenderer setContent(ChapterContent contentData) {
        this.contentData = contentData;
        return this;
    }

    public ITableContentRenderer setDocument(Document document, RelationManager relationManager) {
        this.document = document;
        this.relationManager = relationManager;
        return this;
    }
}
