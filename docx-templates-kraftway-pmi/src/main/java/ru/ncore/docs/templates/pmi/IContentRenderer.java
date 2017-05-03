package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public abstract class IContentRenderer {
    protected ChapterContent contentData;

    abstract void render(OutputStream wordDocumentData);

    IContentRenderer setContent(ChapterContent contentData) {
        this.contentData = contentData;
        return this;
    }
}
