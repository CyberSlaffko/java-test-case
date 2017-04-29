package ru.ncore.docs.docbook;

import org.junit.jupiter.api.Test;
import ru.ncore.docs.docbook.document.Chapter;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.docbook.document.DocumentInfo;

import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        checkDocuementInfo(info);

        List<Chapter> chaptersList = parseResult.getChaptersList();
        assertAll(
                () -> assertEquals(1, chaptersList.size())
        );

        Chapter chapter = chaptersList.get(0);
        assertAll(
                () -> assertEquals("Название главы 1", chapter.getTitle())
        );

        List<ChapterContent> contentList = chapter.getContentList();
        assertEquals(1, contentList.size());

        ChapterContent firstPara = contentList.get(0);
        assertAll(
                () -> assertEquals(2, firstPara.getLevel()),
                () -> assertEquals("Состав дистрибутива", firstPara.getTitle())
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
        checkDocuementInfo(info);

        List<Chapter> chaptersList = parseResult.getChaptersList();
        assertAll(
                () -> assertEquals(1, chaptersList.size())
        );

        Chapter chapter = chaptersList.get(0);
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

    private void checkDocuementInfo(DocumentInfo info) {
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