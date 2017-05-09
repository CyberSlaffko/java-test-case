package ru.ncore.docs.templates.pmi.renderers.table.para;

import org.apache.commons.lang3.StringEscapeUtils;
import ru.ncore.docs.templates.pmi.renderers.TemplateUtils;
import ru.ncore.docs.templates.pmi.renderers.table.ITableContentRenderer;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by Вячеслав Молоков on 09.05.2017.
 */
public class TableTextRenderer extends ITableContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData, long width, String style) {
        if (null == contentData.getTitle() || contentData.getTitle().isEmpty()) {
            return;
        }

        TemplateUtils.render("/templates/document/para_r.twig",
                wordDocumentData,
                new HashMap<String, String>(){{
                    put("para", StringEscapeUtils.escapeXml(contentData.getTitle()));
                }});
    }
}
