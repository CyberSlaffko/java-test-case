package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocxMaker {
    public void makeDocument(Document document, Path resultPath) throws URISyntaxException, IOException {
        ByteArrayOutputStream header2Data = new ByteArrayOutputStream(10240);
        renderInfo(document, header2Data,"templates/header2.twig");
        ByteArrayOutputStream header4Data = new ByteArrayOutputStream(10240);
        renderInfo(document, header4Data,"templates/header4.twig");

        OutputStream wordDocumentData = new ByteArrayOutputStream(10240);
        renderInfo(document, wordDocumentData,"templates/document/title_page.twig");
        renderAnnotation(document, wordDocumentData);
        renderToc(wordDocumentData);

        for (ChapterContent chapter : document.getChaptersList()) {
            renderChapter(document, wordDocumentData, chapter, "templates/document/chapter_title.twig");
        }

        for (ChapterContent chapter : document.getAppendicesList()) {
            renderChapter(document, wordDocumentData, chapter, "templates/document/appendix_title.twig");
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/document.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("body", wordDocumentData);

        ByteArrayOutputStream xmlDocument = new ByteArrayOutputStream(102400);
        template.render(model, xmlDocument);

        writeDocx(resultPath, xmlDocument, header2Data, header4Data);
    }

    private void renderToc(OutputStream wordDocumentData) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/document/toc.twig");
        JtwigModel model = JtwigModel.newModel();

        template.render(model, wordDocumentData);
    }

    private void renderAnnotation(Document document, OutputStream wordDocumentData) {
        ChapterContent annotation = document.getAnnotation();
        if (null == annotation) {
            return;
        }

        renderChapter(document, wordDocumentData, annotation, "templates/document/annotation_title.twig");
    }

    private void renderChapter(Document document, OutputStream wordDocumentData, ChapterContent chapter, String templatePath) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        model.with("title", chapter.getTitle());
        model.with("uuid", chapter.getUuid());

        template.render(model, wordDocumentData);

        renderChapterContent(document, chapter.getContentList(), wordDocumentData);
    }

    private void renderChapterContent(Document document, List<ChapterContent> contentList, OutputStream wordDocumentData) {
        for(ChapterContent contentData : contentList) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(contentData, document);
            if (null != renderer) {
                renderer.render(wordDocumentData);
            }
        }
    }

    private void writeDocx(Path resultPath, ByteArrayOutputStream xmlDocument, ByteArrayOutputStream header2Data, ByteArrayOutputStream header4Data) throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/template.docx");
        Path resPath = Paths.get(url.toURI());
        Files.copy(resPath.toAbsolutePath(), resultPath, StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem zipfs = FileSystems.newFileSystem(resultPath, null)) {
            Files.copy(new ByteArrayInputStream(xmlDocument.toByteArray()), zipfs.getPath("/word/document.xml"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(header2Data.toByteArray()), zipfs.getPath("/word/header2.xml"), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new ByteArrayInputStream(header4Data.toByteArray()), zipfs.getPath("/word/header4.xml"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void renderInfo(Document document, OutputStream wordDocumentData, String template) {
        JtwigTemplate titlePageTemplate = JtwigTemplate.classpathTemplate(template);
        JtwigModel titlePageModel = JtwigModel.newModel();
        titlePageModel.with("contractnum", document.getInfo().getContractNum());
        titlePageModel.with("title", document.getInfo().getTitle());
        titlePageModel.with("subtitle", document.getInfo().getSubTitle());
        titlePageModel.with("titleabbrev", document.getInfo().getTitleAbbrev());
        titlePageModel.with("productname", document.getInfo().getProductName());
        titlePageModel.with("issuenum", document.getInfo().getIssueNum());

        titlePageTemplate.render(titlePageModel, wordDocumentData);
    }
}
