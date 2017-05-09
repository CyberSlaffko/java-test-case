package ru.ncore.docs.templates.pmi.renderers.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;
import ru.ncore.docs.templates.pmi.renderers.table.para.TableParaRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.para.TableTextRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.para.TableXRefRenderer;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public abstract class TableCellRendererFactory {
    private final static Logger logger = LoggerFactory.getLogger(TableCellRendererFactory.class);
    public static ITableContentRenderer getRenderer(ChapterContent contentData, Document document, RelationManager relationManager) {
        ITableContentRenderer renderer = null;

        switch (contentData.getType()) {
            case PHRASE:
            case TEXT:
                renderer = new TableTextRenderer();
                break;
            case PARA:
                renderer = new TableParaRenderer();
                break;
            case XREF:
                renderer = new TableXRefRenderer();
                break;
            case ITEMLIST_ITEM:
                renderer = new TableItemListItemRenderer();
                break;
            case ITEMLIST:
                renderer = new TableItemListRenderer();
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
