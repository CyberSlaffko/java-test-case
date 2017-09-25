package ru.ncore.docs.templates.pmi;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.renderable.RenderResult;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Вячеслав Молоков on 06.05.2017.
 * Modified by CyberSlaffko on 24.09.2017.
 */
public abstract class TemplateUtils {

    protected static EnvironmentConfiguration cfg = EnvironmentConfigurationBuilder.configuration()
            .render().withOutputCharset(StandardCharsets.UTF_8)
            .and().resources().withDefaultInputCharset(StandardCharsets.UTF_8).and().build();

    protected static ConcurrentMap<String, JtwigTemplate> templateCache = new ConcurrentHashMap<>(10);

    public static EnvironmentConfiguration getCfg() {
        return cfg;
    }

    public static void setCfg(EnvironmentConfiguration cfg) {
        TemplateUtils.cfg = cfg;
    }
    
    protected static JtwigTemplate getTemplate(String templatePath) {
        return templateCache.computeIfAbsent(templatePath, tp -> JtwigTemplate.classpathTemplate(tp, cfg));
    }

    public static void render(String templatePath, OutputStream stream, Map<String, Object> data) {
        JtwigModel model = JtwigModel.newModel(data);
        render(templatePath, stream, model);
    }

    public static void render(String templatePath, OutputStream stream, JtwigModel model) {
        getTemplate(templatePath).render(model, stream);
    }

    public static String render(String templatePath, JtwigModel model) {
        return getTemplate(templatePath).render(model);
    }

}
