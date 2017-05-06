package ru.ncore.docs.templates.pmi;

import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.renderers.*;


/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ContentRendererFactory {
    public static IContentRenderer getRenderer(ChapterContent contentData, Document document) {
        switch (contentData.getType()) {
            case PARA:
                return (new ParaRenderer()).setContent(contentData).setDocument(document);
            case ORDEREDLIST:
            case ITEMLIST:
                return (new ItemizedListRenderer()).setDocument(document).setContent(contentData);
            case SECTION:
                return (new SectionRenderer()).setContent(contentData).setDocument(document);
            case TABLE:
                return (new TableRenderer()).setContent(contentData).setDocument(document);
            case FIGURE:
                return (new FigureRenderer()).setContent(contentData).setDocument(document);
            case PROGRAMLISTING:
                return (new ListingRenderer()).setContent(contentData).setDocument(document);
            default:
                System.out.printf("[W002] unknown renderer %s\n", contentData.getType().toString());
                System.out.printf("[D003] %s\n", contentData.getTitle());
                return null;
        }
    }
}
