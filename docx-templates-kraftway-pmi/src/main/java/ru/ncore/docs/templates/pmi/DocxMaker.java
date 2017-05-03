package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.ChapterContent;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocxMaker {
    public void makeDocument(Document document, Path resultPath) throws URISyntaxException, IOException {
        OutputStream wordDocumentData = new ByteArrayOutputStream(10240);

        renderInfo(document, wordDocumentData);
        renderAnnotation(document, wordDocumentData);

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/document.twig");
        JtwigModel model = JtwigModel.newModel();
        model.with("body", wordDocumentData);

        ByteArrayOutputStream xmlDocument = new ByteArrayOutputStream(10240);
        template.render(model, xmlDocument);

        writeDocx(resultPath, xmlDocument);
    }

    private void renderAnnotation(Document document, OutputStream wordDocumentData) {
        Chapter annotation = document.getAnnotaion();
        if (null == annotation) {
            return;
        }

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/document/annotation_title.twig");
        JtwigModel model = JtwigModel.newModel();

        model.with("title", annotation.getTitle());
        model.with("uuid", annotation.getUuid());

        template.render(model, wordDocumentData);

        renderChapterContent(annotation.getContentList(), wordDocumentData);
    }

    private void renderChapterContent(List<ChapterContent> contentList, OutputStream wordDocumentData) {
        for(ChapterContent contentData : contentList) {
            ContentRendererFactory.getRenderer(contentData).render(wordDocumentData);
        }
    }

    private void writeDocx(Path resultPath, ByteArrayOutputStream xmlDocument) throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/template.docx");
        Path resPath = Paths.get(url.toURI());
        Files.copy(resPath.toAbsolutePath(), resultPath, StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem zipfs = FileSystems.newFileSystem(resultPath, null)) {
            Path pathInZipfile = zipfs.getPath("/word/document.xml");
            Files.copy(new ByteArrayInputStream(xmlDocument.toByteArray()), pathInZipfile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void renderInfo(Document document, OutputStream wordDocumentData) {
        JtwigTemplate titlePageTemplate = JtwigTemplate.classpathTemplate("templates/document/title_page.twig");
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
