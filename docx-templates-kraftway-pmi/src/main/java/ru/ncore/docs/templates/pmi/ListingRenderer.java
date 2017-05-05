package ru.ncore.docs.templates.pmi;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.OutputStream;

/**
 * Created by Вячеслав Молоков on 05.05.2017.
 */
public class ListingRenderer extends IContentRenderer {
    @Override
    void render(OutputStream wordDocumentData) {
        JtwigTemplate innerTemplate = JtwigTemplate.classpathTemplate("/templates/document/listing.twig");
        JtwigModel innerModel = JtwigModel.newModel();
        innerModel.with("uuid", contentData.getUuid());

        for (String text: contentData.getTitle().split("\n"))
        {
            innerModel.with("para", StringEscapeUtils.escapeXml(text));
            innerTemplate.render(innerModel, wordDocumentData);
        }
    }
}
