package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public interface IContentRenderer {
    void render(OutputStream wordDocumentData);
    IContentRenderer setContent(ChapterContent contentData);
}
