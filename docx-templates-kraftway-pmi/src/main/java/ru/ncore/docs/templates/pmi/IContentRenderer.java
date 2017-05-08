package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public abstract class IContentRenderer {
    protected ChapterContent contentData;
    protected Document document;
    protected RelationManager relationManager;

    public abstract void render(OutputStream wordDocumentData);

    public IContentRenderer setContent(ChapterContent contentData) {
        this.contentData = contentData;
        return this;
    }

    public IContentRenderer setDocument(Document document, RelationManager relationManager) {
        this.document = document;
        this.relationManager = relationManager;
        return this;
    }
}
