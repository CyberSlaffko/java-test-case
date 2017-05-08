package ru.ncore.docs.docbook;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.document.DocumentInfo;
import ru.ncore.docs.docbook.utils.MD5Utils;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testable
class ParserTest {
    @Test
    void testShouldHandleIncorrectFileNames() {
        Parser parser = new Parser();
        final Document parseResult = parser.parse("some_shit_file_name.ohh");

        assertAll(
                () -> assertNotEquals(null, parseResult),
                () -> assertEquals(Document.Type.ERROR, parseResult.getType()),
                () -> assertEquals(false, parseResult.isParsedSuccessfully())
        );
    }

    @Test
    void testShouldParseSimpleFile() throws URISyntaxException {
        java.net.URL url = this.getClass().getResource("/simple/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());

        checkParserResultSuccess(parseResult);

        DocumentInfo info = parseResult.getInfo();
        checkDocumentInfo(info);

        ChapterContent annotationChapter = parseResult.getAnnotation();
        assertNotNull(annotationChapter);

        assertAll(
                () -> assertEquals("Аннотация1", annotationChapter.getTitle()),
                () -> assertEquals(1, annotationChapter.getContentList().size())
        );

        assertAll(
                () -> assertEquals(4, annotationChapter.getContentList().get(0).getContentList().size()),
                () -> assertEquals("http://bd-test.it.mvd.ru/", annotationChapter.getContentList().get(0).getContentList().get(0).getTitle())
        );

        List<ChapterContent> chaptersList = parseResult.getChaptersList();
        assertAll(
                () -> assertEquals(1, chaptersList.size())
        );

        ChapterContent chapter = chaptersList.get(0);
        assertAll(
                () -> assertEquals("Название главы 1", chapter.getTitle()),
                () -> assertEquals(MD5Utils.HexMD5ForString("test2"), chapter.getBookmarkId())
        );

        List<ChapterContent> contentList = chapter.getContentList();
        assertEquals(1, contentList.size());

        ChapterContent firstPara = contentList.get(0);
        assertAll(
                () -> assertEquals(2, firstPara.getLevel()),
                () -> assertEquals("Состав дистрибутива", firstPara.getTitle()),
                () -> assertEquals(MD5Utils.HexMD5ForString("test1"), firstPara.getBookmarkId())
        );
    }

    private void checkParserResultSuccess(Document parseResult) {
        assertNotEquals(null, parseResult);
        assertAll("should be a book parsed successfully",
                () -> assertEquals(Document.Type.BOOK, parseResult.getType()),
                () -> assertEquals(true, parseResult.isParsedSuccessfully())
        );
    }

    @Test
    void testShouldParseFileWithXInclude() throws URISyntaxException {
        java.net.URL url = this.getClass().getResource("/with_xinclude/index.xml");
        java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
        Parser parser = new Parser();
        final Document parseResult = parser.parse(resPath.toString());

        checkParserResultSuccess(parseResult);

        DocumentInfo info = parseResult.getInfo();
        checkDocumentInfo(info);

        List<ChapterContent> chaptersList = parseResult.getChaptersList();
        assertAll(
                () -> assertEquals(1, chaptersList.size())
        );

        ChapterContent chapter = chaptersList.get(0);
        assertAll(
                () -> assertEquals("Название главы 1", chapter.getTitle())
        );

        List<ChapterContent> contentList = chapter.getContentList();
        assertEquals(2, contentList.size());

        ChapterContent firstPara = contentList.get(0);
        assertAll(
                () -> assertEquals(2, firstPara.getLevel()),
                () -> assertEquals("Общие требования", firstPara.getTitle())
        );
    }

    private void checkDocumentInfo(DocumentInfo info) {
        assertAll(
                () -> assertEquals("Название ОКР", info.getTitle()),
                () -> assertEquals("шифр окр", info.getTitleAbbrev()),
                () -> assertEquals("Название СЧ ОКР", info.getProductName()),
                () -> assertEquals("РУКОВОДСТВО ПО СБОРКЕ", info.getSubTitle()),
                () -> assertEquals("RU.35897050.00004-01", info.getContractNum()),
                () -> assertEquals("RU.35897050.00004-01 98 01", info.getIssueNum()),
                () -> assertEquals("2016", info.getPubDate())
        );
    }
}