package ru.ncore.docs.templates.pmi.renderers.table.para;

import org.jtwig.JtwigModel;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.TemplateUtils;
import ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer;
import ru.ncore.docs.templates.pmi.renderers.table.TableCellRendererFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableParaRenderer extends ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        ByteArrayOutputStream innerData = new ByteArrayOutputStream(1024);
        boolean rendered = innerParaRender(innerData, contentData, width, style);

        for(ChapterContent innerContent : contentData.getContentList()) {
            if (innerContent.isList() || innerContent.isTable() || innerContent.isImage()) {
                closePara(wordDocumentData, new String(innerData.toByteArray(), TemplateUtils.getCfg().getResourceConfiguration().getDefaultCharset()), rendered, width, style);
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

        closePara(wordDocumentData, new String(innerData.toByteArray(), TemplateUtils.getCfg().getResourceConfiguration().getDefaultCharset()), rendered, width, style);
    }

    private void closePara(OutputStream wordDocumentData, String innerData, boolean rendered, long width, String style) {
        if (!rendered) {
            return;
        }

        TemplateUtils.render("templates/document/table/tc_p.twig", wordDocumentData, JtwigModel.newModel()
            .with("width", String.valueOf(width))
            .with("style", style)
            .with("body", innerData)
        );
    }

    private boolean innerParaRender(OutputStream innerData, ChapterContent innerContent, long width, String style) {
        String text = innerContent.getTitle();
        if (null == text || text.isEmpty()) {
            return false;
        }

//        boolean rendered = false;
//        for (ChapterContent chapterContent : innerContent.getContentList()) {
//            ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(chapterContent, document, relationManager);
//            if (null != renderer) {
//                renderer.render(innerData, width, style);
//                rendered = true;
//            }
//        }
        ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(innerContent, document, relationManager);
        if (null != renderer) {
            renderer.render(innerData, width, style);
            return true;
        }

        return true;
    }
}
