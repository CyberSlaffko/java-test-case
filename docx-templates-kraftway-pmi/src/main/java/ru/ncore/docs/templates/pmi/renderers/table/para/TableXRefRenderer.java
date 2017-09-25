package ru.ncore.docs.templates.pmi.renderers.table.para;

import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.TemplateUtils;
import ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer;

import java.io.OutputStream;

/**
 * Рендерер ссылок в таблице
 */
public class TableXRefRenderer extends ITableContentRenderer {
    private final static Logger logger = LoggerFactory.getLogger(TableXRefRenderer.class);
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        TemplateUtils.render(templateFor(contentData),
                wordDocumentData,
                JtwigModel.newModel()
//                    put("para", StringEscapeUtils.escapeXml(contentData.getTitle()));
                    .with("uuid", contentData.getBookmarkId())
        );
    }

    private String templateFor(ChapterContent chapterContent) {
        ChapterContent.Type linkType = document.getLinkType(chapterContent.getBookmarkId());
        if (linkType == null) {
            logger.warn("Link to nowhere");
            logger.debug(String.format("XRef linkend=%s, hash=%s", chapterContent.getTitle(), chapterContent.getBookmarkId()));
            return "templates/document/ref_obj.twig";
        }
        switch (linkType) {
            case SECTION:
            case CHAPTER:
            case APPENDIX:
                return "templates/document/ref.twig";
            default:
                return "templates/document/ref_obj.twig";
        }
    }
}
