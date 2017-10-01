package ru.ncore.docs.templates.pmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;
import ru.ncore.docs.templates.pmi.renderers.*;
import ru.ncore.docs.templates.pmi.renderers.image.FigureRenderer;
import ru.ncore.docs.templates.pmi.renderers.image.ImageRenderer;
import ru.ncore.docs.templates.pmi.renderers.list.ItemizedListRenderer;
import ru.ncore.docs.templates.pmi.renderers.list.ListingRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.TGroupRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.TableRenderer;


/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class ContentRendererFactory {
    private final static Logger logger = LoggerFactory.getLogger(ContentRendererFactory.class);
    public static IContentRenderer getRenderer(ChapterContent contentData, Document document, RelationManager relationManager) {
//        logger.debug(String.format("Getting renderer for %s", contentData.getType().toString()));
        IContentRenderer renderer = null;
        switch (contentData.getType()) {
            case PARA:
                renderer = new ParaRenderer();
                break;
            case ORDEREDLIST:
            case ITEMLIST:
                renderer = new ItemizedListRenderer();
                break;
            case SECTION:
                renderer = new SectionRenderer();
                break;
            case TGROUP:
                renderer = new TGroupRenderer();
                break;
            case TABLE:
                renderer = new TableRenderer();
                break;
            case FIGURE:
                renderer = new FigureRenderer();
                break;
            case INLINEEQUATION:
                renderer = new NoopRenderer();
                break;
            case MEDIAOBJECT:
                renderer = new MediaObjectRenderer();
                break;
            case INLINEMEDIAOBJECT:
                renderer = new InlineObjectRenderer();
                break;
            case IMAGEOBJECT:
                renderer = new NoopRenderer();
                break;
            case IMAGEDATA:
                renderer = new ImageRenderer();
                break;
            case PROGRAMLISTING:
                renderer = new ListingRenderer();
                break;
            default:
                logger.warn(String.format("Cannot find renderer for %s", contentData.getType().toString()));
        }

        if (renderer == null) {
            return null;
        }

        renderer.setContent(contentData).setDocument(document, relationManager);
        return renderer;
    }
}
