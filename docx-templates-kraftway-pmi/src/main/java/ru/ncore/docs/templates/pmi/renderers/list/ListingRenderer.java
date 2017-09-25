package ru.ncore.docs.templates.pmi.renderers.list;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import ru.ncore.docs.templates.pmi.IContentRenderer;
import ru.ncore.docs.templates.pmi.TemplateUtils;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class ListingRenderer extends IContentRenderer {
    @Override
    public void render(OutputStream wordDocumentData) {
        //JtwigTemplate innerTemplate = JtwigTemplate.classpathTemplate("/templates/document/listing.twig");
        JtwigModel innerModel = JtwigModel.newModel();
        innerModel.with("uuid", contentData.getUuid());

        String templatePath = "/templates/document/listing.twig";

        for (String text: contentData.getTitle().split("\n"))
        {
            innerModel.with("para", StringEscapeUtils.escapeXml(text));
            TemplateUtils.render(templatePath, wordDocumentData, innerModel);
        }
    }
}
