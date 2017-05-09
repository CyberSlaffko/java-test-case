package ru.ncore.docs.templates.pmi.renderers.table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ncore.docs.docbook.document.ChapterContent;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.SizeUtils;
import ru.ncore.docs.templates.pmi.TableColumnWidthCalculator;
import ru.ncore.docs.templates.pmi.renderers.TemplateUtils;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TGroupRenderer extends IContentRenderer {
    static final private Logger logger = LoggerFactory.getLogger(TGroupRenderer.class);

    @Override
    public void render(OutputStream wordDocumentData) {
        ByteArrayOutputStream tableOutputStream = new ByteArrayOutputStream(10240);
        renderTableHead(tableOutputStream);
        renderTableBody(tableOutputStream);

        TemplateUtils.render("templates/document/table/table.twig", wordDocumentData, new HashMap<String, String>(){{
            put("body", tableOutputStream.toString());
        }});
    }

    private void renderTableBody(OutputStream wordDocumentData) {
        TableColumnWidthCalculator widthCalculator = new TableColumnWidthCalculator();
        contentData.getContentList().stream().
                filter(cd -> Objects.equals(cd.getType(), ChapterContent.Type.TABLE_COLSPEC)).
                forEach(chapterContent -> widthCalculator.addWidth(chapterContent.getAdditionalAttributes().get("colnum"), chapterContent.getAdditionalAttributes().getOrDefault("width", "*")));

        ByteArrayOutputStream tableOutputStream = new ByteArrayOutputStream(10240);

        for(String column: widthCalculator.getValues()) {
            logger.debug(String.format("Column %s has width %f", column, widthCalculator.getWidthFor(column)));
            TemplateUtils.render("templates/document/table/grid_item.twig", tableOutputStream, new HashMap<String, String>(){{
                put("width", String.valueOf(SizeUtils.cmToWordPoints(widthCalculator.getWidthFor(column))));
            }});
        }

        TemplateUtils.render("templates/document/table/grid_wrapper.twig", wordDocumentData, new HashMap<String, String>(){{
            put("body", tableOutputStream.toString());
        }});
    }

    private void renderTableHead(OutputStream wordDocumentData) {

    }
}
