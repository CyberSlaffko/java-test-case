package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocxMaker {
    static final private Logger logger = LoggerFactory.getLogger(DocxMaker.class);
    public static final String WORD_RELS_DOCUMENT_XML_RELS = "/word/_rels/document.xml.rels";

    public void makeDocument(Document document, Path resultPath) throws URISyntaxException, IOException {
        java.net.URL url = this.getClass().getResource("/template.docx");
        Path resPath = Paths.get(url.toURI());
        Files.copy(resPath.toAbsolutePath(), resultPath, StandardCopyOption.REPLACE_EXISTING);

        Map<String, String> env = new HashMap<>();
        env.put("create", "true");
        try (FileSystem zipfs = FileSystems.newFileSystem(resultPath, null)) {
            RelationManager relationManager = readRelations(zipfs);
            renderAndWriteRelations(document, relationManager, zipfs);
            renderAndWriteHeader2(document, zipfs);
            renderAndWriteHader4(document, zipfs);
            renderAndWriteDocument(document, relationManager, zipfs);
        }
    }

    private void renderAndWriteRelations(Document document, RelationManager relationManager, FileSystem zipfs) throws IOException {
        for(String imgPath : document.getImages()) {
            RelationManager.Media media = relationManager.addRelation(imgPath);
            Files.copy(new ByteArrayInputStream(media.toPNG().toByteArray()), zipfs.getPath(media.getPath()), StandardCopyOption.REPLACE_EXISTING);
        }
        ByteArrayOutputStream xmlDocument = relationManager.generateRelFile();
        Files.copy(new ByteArrayInputStream(xmlDocument.toByteArray()), zipfs.getPath(WORD_RELS_DOCUMENT_XML_RELS), StandardCopyOption.REPLACE_EXISTING);
    }

    private RelationManager readRelations(FileSystem zipfs) {
        logger.debug("Reading document relations");
        RelationManager relationManager = new RelationManager();
        try {
            InputStream inputStream = Files.newInputStream(zipfs.getPath(WORD_RELS_DOCUMENT_XML_RELS), StandardOpenOption.READ);
            relationManager.parseRelFile(inputStream);
        } catch (IOException e) {
            logger.info("Cannot read '/word/_rels/document.xml.rels'. Empty rel will be used");
        }
        return relationManager;
    }

    private void renderAndWriteDocument(Document document, RelationManager relationManager, FileSystem zipfs) throws IOException {
        OutputStream wordDocumentData = new ByteArrayOutputStream(10240);
        renderInfo(document, wordDocumentData, "templates/document/title_page.twig");
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
        Files.copy(new ByteArrayInputStream(xmlDocument.toByteArray()), zipfs.getPath("/word/document.xml"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void renderAndWriteHader4(Document document, FileSystem zipfs) throws IOException {
        ByteArrayOutputStream header4Data = new ByteArrayOutputStream(10240);
        renderInfo(document, header4Data, "templates/header4.twig");
        Files.copy(new ByteArrayInputStream(header4Data.toByteArray()), zipfs.getPath("/word/header4.xml"), StandardCopyOption.REPLACE_EXISTING);
    }

    private void renderAndWriteHeader2(Document document, FileSystem zipfs) throws IOException {
        ByteArrayOutputStream header2Data = new ByteArrayOutputStream(10240);
        renderInfo(document, header2Data, "templates/header2.twig");

        Files.copy(new ByteArrayInputStream(header2Data.toByteArray()), zipfs.getPath("/word/header2.xml"), StandardCopyOption.REPLACE_EXISTING);
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
