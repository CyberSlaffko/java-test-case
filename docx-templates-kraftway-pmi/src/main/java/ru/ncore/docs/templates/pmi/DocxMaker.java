package ru.ncore.docs.templates.pmi;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.rel.Media;
import ru.ncore.docs.templates.pmi.rel.RelationManager;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

public class DocxMaker {
    static final private Logger logger = LoggerFactory.getLogger(DocxMaker.class);
    private static final String WORD_RELS_DOCUMENT_XML_RELS = "/word/_rels/document.xml.rels";

    public void makeDocument(Document document, Path resultPath) throws URISyntaxException, IOException, InvalidFormatException {
        //java.net.URL url = this.getClass().getResource("/template.docx");
        //Path resPath = Paths.get(url.toURI());
        //Files.copy(Files.newInputStream(resPath), resultPath, StandardCopyOption.REPLACE_EXISTING);
        InputStream tplStream = getClass().getResourceAsStream("/template.docx");
        Files.copy(tplStream, resultPath, StandardCopyOption.REPLACE_EXISTING);

        try (FileSystem zipfs = FileSystems.newFileSystem(resultPath, null)) {
            replaceRootFile(zipfs);
            RelationManager relationManager = readRelations(zipfs);
            renderAndWriteRelations(document, relationManager, zipfs);
            renderAndWriteHeader2(document, zipfs);
            renderAndWriteHader4(document, zipfs);
            renderAndWriteDocument(document, relationManager, zipfs);
        }

//        InputStream fs = new FileInputStream(resultPath.toString());
//        XWPFDocument xwpfDocument = new XWPFDocument(OPCPackage.open(fs));
//        xwpfDocument.enforceUpdateFields();
//        xwpfDocument.write(new FileOutputStream(new File(resultPath.toString())));
//        fs.close();
    }

    private void replaceRootFile(FileSystem zipfs) {
        try {
            //java.net.URL url = this.getClass().getResource("/templates/[Content_Types].xml");
            //Path resPath = Paths.get(url.toURI());
            //Files.copy(Files.newInputStream(resPath), zipfs.getPath("/[Content_Types].xml"), StandardCopyOption.REPLACE_EXISTING);
            InputStream resStream = getClass().getResourceAsStream("/templates/[Content_Types].xml");
            Files.copy(resStream, zipfs.getPath("/[Content_Types].xml"), StandardCopyOption.REPLACE_EXISTING);
        } catch (/*URISyntaxException |*/ IOException e) {
            logger.error("Something went wrong", e);
        }
    }

    private void renderAndWriteRelations(Document document, RelationManager relationManager, FileSystem zipfs) throws IOException {
        Files.createDirectory(zipfs.getPath("/word/media/"));
        for(String imgPath : document.getImages()) {
            Media media = relationManager.addRelation(imgPath);
            Files.copy(new ByteArrayInputStream(media.toPNG().toByteArray()), zipfs.getPath("/word/", media.getPath()), StandardCopyOption.REPLACE_EXISTING);
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
        renderAnnotation(document, relationManager, wordDocumentData);
        renderToc(wordDocumentData);

        for (ChapterContent chapter : document.getChaptersList()) {
            renderChapter(document, relationManager, wordDocumentData, chapter, "templates/document/chapter_title.twig");
        }

        for (ChapterContent chapter : document.getAppendicesList()) {
            renderChapter(document, relationManager, wordDocumentData, chapter, "templates/document/appendix_title.twig");
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

    private void renderAnnotation(Document document, RelationManager relationManager, OutputStream wordDocumentData) {
        ChapterContent annotation = document.getAnnotation();
        if (null == annotation) {
            return;
        }

        renderChapter(document, relationManager, wordDocumentData, annotation, "templates/document/annotation_title.twig");
    }

    private void renderChapter(Document document, RelationManager relationManager, OutputStream wordDocumentData, ChapterContent chapter, String templatePath) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        String style = "15";
        if (Objects.equals(chapter.getAdditionalAttributes().getOrDefault("role", ""), "Unnumbered")) {
            style = "aff6";
        }

        model.with("title", chapter.getTitle());
        model.with("uuid", chapter.getBookmarkId());
        model.with("style", style);

        template.render(model, wordDocumentData);

        renderChapterContent(document, relationManager, chapter.getContentList(), wordDocumentData);
    }

    private void renderChapterContent(Document document, RelationManager relationManager, List<ChapterContent> contentList, OutputStream wordDocumentData) {
        for(ChapterContent contentData : contentList) {
            IContentRenderer renderer = ContentRendererFactory.getRenderer(contentData, document, relationManager);
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
