package ru.ncore.docs.templates.pmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.renderers.*;


/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ContentRendererFactory {
    final static Logger logger = LoggerFactory.getLogger(ContentRendererFactory.class);
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
            case MEDIAOBJECT:
                return (new NoopRenderer()).setContent(contentData).setDocument(document);
            case IMAGEOBJECT:
                return (new NoopRenderer()).setContent(contentData).setDocument(document);
            case IMAGEDATA:
                return (new ImageRenderer()).setContent(contentData).setDocument(document);
            case PROGRAMLISTING:
                return (new ListingRenderer()).setContent(contentData).setDocument(document);
            default:
                logger.warn(String.format("Cannot find renderer for %s", contentData.getType().toString()));
                return null;
        }
    }
}
