package ru.ncore.docs.templates.pmi.renderers;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 */
public abstract class TemplateUtils {
    public static void render(String templatePath, OutputStream stream, Map<String, String> data) {
        JtwigTemplate template = JtwigTemplate.classpathTemplate(templatePath);
        JtwigModel model = JtwigModel.newModel();

        for (Map.Entry<String, String> entry : data.entrySet())
        {
            model.with(entry.getKey(), entry.getValue());
        }

        template.render(model, stream);

    }
}
