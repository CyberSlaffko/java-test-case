package ru.ncore.docs.templates.pmi.renderers.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.SizeUtils;
import ru.ncore.docs.templates.pmi.TableColumnWidthCalculator;
import ru.ncore.docs.templates.pmi.renderers.TemplateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TGroupRenderer extends IContentRenderer {
    static final private Logger logger = LoggerFactory.getLogger(TGroupRenderer.class);
    TableColumnWidthCalculator widthCalculator;

    @Override
    public void render(OutputStream wordDocumentData) {
        ByteArrayOutputStream tableOutputStream = new ByteArrayOutputStream(10240);
        renderTableBody(tableOutputStream);

        TemplateUtils.render("templates/document/table/table.twig", wordDocumentData, new HashMap<String, String>(){{
            put("body", tableOutputStream.toString());
        }});
    }

    private void renderTableBody(OutputStream wordDocumentData) {
        setColumnSizes();

        ByteArrayOutputStream tableOutputStream = new ByteArrayOutputStream(10240);

        try {
            renderTblGrid(tableOutputStream);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        TemplateUtils.render("templates/document/table/grid_wrapper.twig", wordDocumentData, new HashMap<String, String>(){{
            put("body", tableOutputStream.toString());
        }});

        renderTableHead(wordDocumentData, ChapterContent.Type.TABLE_HEAD, "afff4", "/templates/document/table/thead_tr.twig");
        renderTableHead(wordDocumentData, ChapterContent.Type.TABLE_BODY, "afffffffffd", "/templates/document/table/tbody_tr.twig");
    }

    private void renderTableHead(OutputStream tableOutputStream, ChapterContent.Type tablePart, String style, String templatePath) {
        Optional<ChapterContent> tableHead = contentData.getContentList().stream().
                filter(cd -> Objects.equals(cd.getType(), tablePart)).findFirst();
        if (!tableHead.isPresent()) {
            return;
        }

        TableMatrix matrix = TableMatrix.generate(tableHead.get().getContentList().size(),
                (int) getColSpecs().count(),
                getColSpecs().collect(Collectors.toList()));
        for(int i = 0; i < tableHead.get().getContentList().size(); i++) {
            int finalI = i;
            tableHead.get().getContentList().get(i).getContentList().forEach(
                    chapterContent -> matrix.addCellToRow(finalI, chapterContent)
            );
        }

        for(int i = 0; i < matrix.getRows(); i++) {
            ByteArrayOutputStream cells = new ByteArrayOutputStream(1024);
            for (int j = 0; j < matrix.getColumns(); j++) {
                ByteArrayOutputStream cell = new ByteArrayOutputStream(1024);
                switch (matrix.get(i, j).getCellType()) {
                    case MERGE_RESTART: {
                        long width = SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(matrix.get(i, j).getColName()));
                        String instructions = "<w:vMerge w:val=\"restart\" />";
                        renderTc(width, matrix.get(i, j).getContent(), instructions, cell, style);
                        break;
                    }
                    case MERGE: {
                        long width = SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(matrix.get(i, j).getColName()));
                        String instructions = "<w:vMerge />";
                        renderTc(width, matrix.get(i, j).getContent(), instructions, cell, style);
                        break;
                    }
                    case SIMPLE: {
                        long width = SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(matrix.get(i, j).getColName()));
                        renderTc(width, matrix.get(i, j).getContent(), "", cell, style);
                        break;
                    }
                    case GRID_SPAN_SKIP:
                        break;
                    case GRID_SPAN:
                        long width = 0;
                        int colnum = Integer.parseInt(matrix.get(i, j).getColName());
                        int spansize = matrix.get(i, j).getSpanSize();
                        for (int ii = colnum; ii < colnum + spansize; ii++) {
                            width +=SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(String.valueOf(ii)));
                        }
                        String instructions = String.format("<w:gridSpan w:val=\"%d\" />", spansize);
                        renderTc(width, matrix.get(i, j).getContent(), instructions, cell, style);
                        break;
                    default:
                        logger.warn(String.format("Unknown cell style %s", matrix.get(i, j).getCellType()));
                }
                try {
                    cell.writeTo(cells);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            TemplateUtils.render(templatePath,
                    tableOutputStream,
                    new HashMap<String, String>() {{
                        put("body", cells.toString());
                    }});
        }
    }

    private void renderTc(long width, List<ChapterContent> childContent, String instructions, ByteArrayOutputStream tableOutputStream, final String style) {
        // При вводе ввели <entry> </entry>
        if(childContent.size() == 1 && childContent.get(0).getType() == ChapterContent.Type.TEXT && childContent.get(0).getTitle().isEmpty()) {
            logger.info("Rendering table entity without content (case <entry> </entry>)");
            childContent.get(0).setTitle(" ");
        }

        // При вводе ввели <entry />
        if(childContent.size() == 0) {
            logger.info("Rendering table entity without content (case <entry />)");
            ChapterContent chapterContent = new ChapterContent();
            chapterContent.setType(ChapterContent.Type.TEXT);
            chapterContent.setTitle(" ");
            childContent.add(chapterContent);
        }

        ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
        for (ChapterContent chapterContent : childContent) {
            ChapterContent renderMe = chapterContent;
            switch (chapterContent.getType()) {
                case XREF:
                case PHRASE:
                case TEXT:
                    ChapterContent paraWrapper = new ChapterContent();
                    paraWrapper.setType(ChapterContent.Type.PARA);
                    paraWrapper.getContentList().add(chapterContent);
                    renderMe = paraWrapper;
                    break;
                default:
                    logger.warn(String.format("Unknown tag in table cell: %s", chapterContent.getType()));
            }

            ITableContentRenderer renderer = TableCellRendererFactory.getRenderer(renderMe, document, relationManager);
            if (renderer != null) {
                renderer.render(contentBuffer, width, style);
            }
        }

        TemplateUtils.render("templates/document/table/tc.twig", tableOutputStream, new HashMap<String, String>(){{
            put("width", String.valueOf(width));
            put("instructions", instructions);
            put("style", style);
            put("body", contentBuffer.toString());
        }});
    }


    private void setColumnSizes() {
        widthCalculator = new TableColumnWidthCalculator();
        getColSpecs().
                forEach(chapterContent -> widthCalculator.addWidth(chapterContent.getAdditionalAttributes().get("colnum"), chapterContent.getAdditionalAttributes().getOrDefault("width", "*")));
    }

    private Stream<ChapterContent> getColSpecs() {
        return contentData.getContentList().stream().
                filter(cd -> Objects.equals(cd.getType(), ChapterContent.Type.TABLE_COLSPEC));
    }

    /**
     * Рендерит элемент tblGrid со всем размерами колонок
     * @param tableOutputStream
     */
    private void renderTblGrid(ByteArrayOutputStream tableOutputStream) {
        for(String column: widthCalculator.getValues()) {
            logger.debug(String.format("Column %s has width %f", column, widthCalculator.getWidthFor(column)));
            TemplateUtils.render("templates/document/table/grid_item.twig", tableOutputStream, new HashMap<String, String>(){{
                put("width", String.valueOf(SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(column))));
            }});
        }
    }
}
