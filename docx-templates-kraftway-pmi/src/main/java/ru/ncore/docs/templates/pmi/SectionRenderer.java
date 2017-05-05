package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 03.05.2017.
 */
public class SectionRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        String templatePath = String.format("templates/document/section_%d.twig", contentData.getLevel());

        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", contentData.getTitle());
        model.with("styleId", getStyleId(contentData.getLevel(), contentData.getChapterType()));
        model.with("uuid", contentData.getBookmarkId());

        template.render(model, wordDocumentData);

        for(ChapterContent subContent : contentData.getContentList()) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(subContent);
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
                default: return "6";
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
