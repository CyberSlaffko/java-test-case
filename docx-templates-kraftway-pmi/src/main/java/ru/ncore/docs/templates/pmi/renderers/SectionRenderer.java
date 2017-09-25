package ru.ncore.docs.templates.pmi.renderers;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.ContentRendererFactory;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class SectionRenderer extends IContentRenderer {
    static final Logger logger = LoggerFactory.getLogger(SectionRenderer.class);
    @Override
    public void render(OutputStream wordDocumentData) {
        String templatePath = String.format("templates/document/sections/section_%d.twig", (contentData.getLevel() > 6 ? 6 : contentData.getLevel()));
        if (contentData.getLevel() > 9) {
            logger.warn(String.format("Too deep section (level %d) -> %s", contentData.getLevel(), contentData.getTitle()));
        }

        //JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", StringEscapeUtils.escapeXml(contentData.getTitle()));
        model.with("styleId", getStyleId(contentData.getLevel(), contentData.getChapterType()));
        model.with("uuid", contentData.getBookmarkId());

        TemplateUtils.render(templatePath, wordDocumentData, model);

        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent, document, relationManager);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }

    private String getStyleId(int level, ChapterContent.ChapterType chapterType) {
        if (chapterType == ChapterContent.ChapterType.CHAPTER) {
            switch (level) {
                case 2: return "24";
                case 3: return "33";
                case 4: return "40";
                case 5: return "50";
                case 6: return "6";
                case 7: return "7";
                case 8: return "8";
                default: return "9";
            }
        } else if (chapterType == ChapterContent.ChapterType.APPENDIX) {
            switch(level) {
                case 2: return "22";
                case 3: return "30";
                case 4: return "4";
                default: return "5";
            }
        }
        return "24";
    }
}
