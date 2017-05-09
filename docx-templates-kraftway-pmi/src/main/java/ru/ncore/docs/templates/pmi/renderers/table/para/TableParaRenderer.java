package ru.ncore.docs.templates.pmi.renderers.table.para;

import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.renderers.TemplateUtils;
import ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.TableCellRendererFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableParaRenderer extends ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        OutputStream innerData = new ByteArrayOutputStream(1024);
        boolean rendered = innerParaRender(innerData, contentData, width, style);

        for(ChapterContent innerContent : contentData.getContentList()) {
            if (innerContent.isList() || innerContent.isTable() || innerContent.isImage()) {
                closePara(wordDocumentData, innerData, rendered, width, style);
                innerData = new ByteArrayOutputStream(1024);
                rendered = false;

                ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(innerContent, document, relationManager);
                if (null != renderer) {
                    renderer.render(wordDocumentData, width, style);
                }
            }
            else {
                rendered = innerParaRender(innerData, innerContent, width, style) || rendered;
            }
        }

        closePara(wordDocumentData, innerData, rendered, width, style);
    }

    private void closePara(OutputStream wordDocumentData, OutputStream innerData, boolean rendered, long width, String style) {
        if (!rendered) {
            return;
        }

        TemplateUtils.render("templates/document/table/tc_p.twig", wordDocumentData, new HashMap<String, String>(){{
            put("width", String.valueOf(width));
            put("style", style);
            put("body", innerData.toString());
        }});
    }

    private boolean innerParaRender(OutputStream innerData, ChapterContent innerContent, long width, String style) {
        String text = innerContent.getTitle();
        if (null == text || text.isEmpty()) {
            return false;
        }

        ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(innerContent, document, relationManager);
        if (null != renderer) {
            renderer.render(innerData, width, style);
            return true;
        }

        return false;
    }
}
