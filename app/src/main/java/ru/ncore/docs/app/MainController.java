package ru.ncore.docs.app;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.Parser;
import ru.ncore.docs.pdf.converter.PdfMaker;
import ru.ncore.docs.templates.pmi.DocxMaker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CyberSlaffko on 21.09.2017.
 */
@RestController
public class MainController {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    static final Map<String,Object> SAMPLE_PARAMETERS = new HashMap<String,Object>(){{
        put("data","Hello");
        put("data_ru","Привет");
    }};

    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_MSWORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final String EMPTY_XML="<?xml version=\"1.0\"?>\n<empty/>\n";

    //@Autowired
    //PdfGenerator pdfGenerator;

    @Autowired
    private Parser parser;

    @Autowired
    private DocxMaker docxMaker;

    @Autowired
    private PdfMaker pdfMaker;

    @RequestMapping(value={"/converter/{out}"},method = RequestMethod.POST)
    public @ResponseBody void convert(
            @PathVariable("out") String out,
            @RequestParam(value="src",defaultValue=MainController.EMPTY_XML) String srcURL,
            @RequestParam(value="tpl",defaultValue=MainController.EMPTY_XML) String tplURL,
            @RequestParam(value="params",defaultValue="") String parameters,
            HttpServletRequest request,HttpServletResponse response) throws Exception{

        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String,Object> params = (Map<String,Object>)yaml.load(parameters);

        switch (out){
            case "pdf":generateOut(params, srcURL, tplURL, MIME_PDF,"generated.pdf", request, response);
                break;
            case "png":generateOut(params, srcURL, tplURL, MIME_PNG,"generated.png", request, response);
                break;
            case "docx":generateOut(params, srcURL, tplURL, MIME_MSWORD,"generated.docx", request, response);
                break;
        }
    }

/*
    @RequestMapping(value={"/generate{out}"},method = RequestMethod.POST)
    public @ResponseBody void pdf(
            @PathVariable("out") String out,
            @RequestParam(value="xml",defaultValue=PdfGenerator.EMPTY_XML) String xml,
            @RequestParam(value="xsl",defaultValue=PdfGenerator.EMPTY_XML) String xsl,
            @RequestParam(value="par",defaultValue="") String yml,
            @RequestParam(value="enc",defaultValue="") String enc,
            HttpServletRequest request,HttpServletResponse response) throws Exception{
        if(xml.length() > MAX_LEN || xsl.length() > MAX_LEN || yml.length() > MAX_LEN){
            logger.error("Request data too long, skip");
            response.sendError(response.SC_BAD_REQUEST);
            return;
        }
        Yaml yaml = new Yaml();
        Map<String,Object> par =(Map<String, Object>)yaml.load(yml);
        switch (out){
            case "pdf":generateOut(par,xml,xsl, MimeConstants.MIME_PDF,"generated.pdf",request,response,enc);
                break;
            case "png":generateOut(par,xml,xsl, MimeConstants.MIME_PNG,"generated.png",request,response,enc);
                break;
        }
    }

    @RequestMapping(value={"/samplepdf"},method = RequestMethod.GET)
    public @ResponseBody void samplePdf(
            HttpServletRequest request,HttpServletResponse response) throws Exception{
        String xml = PdfGenerator.EMPTY_XML;
        String xsl = IOUtils.toString(getClass().getResourceAsStream("/xml/sample.xml"));
        generateOut(SAMPLE_PARAMETERS,xml,xsl, MimeConstants.MIME_PDF,"sample.pdf",request,response,null);
    }

    @RequestMapping(value={"/samplepng"},method = RequestMethod.GET)
    public @ResponseBody void samplePng(
            HttpServletRequest request,HttpServletResponse response) throws Exception{
        String xml = PdfGenerator.EMPTY_XML;
        String xsl = IOUtils.toString(getClass().getResourceAsStream("/xml/sample.xml"));
        generateOut(SAMPLE_PARAMETERS,xml,xsl, MimeConstants.MIME_PNG,"sample.png",request,response,null);
    }
*/

    public void generateOut(Map<String,Object> parameters, String srcURL, String tplURL, String mime, String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            logger.debug("Generating output as: " + mime);
            logger.trace("srcURL: " + srcURL);
            logger.trace("tplURL: " + tplURL);
            response.setContentType(mime);
            response.addHeader("Content-disposition", "inline; filename=\"" + URLEncoder.encode(fileName, "UTF8") +"\"");
            switch (mime) {
                case MIME_PDF:
                case MIME_PNG:
                    pdfMaker.generateOut(parameters, srcURL, tplURL, mime, response.getOutputStream());
                    break;
                case MIME_MSWORD:
                    generateDocx(srcURL, tplURL, response.getOutputStream());
                    break;
            }
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("Can't out as "+mime+": "+e, e);
        }
    }

    public void generateDocx(String srcURL, String tplURL, OutputStream outputStream) throws IOException {
        final Document parseResult = parser.parse(srcURL);
        Path targetPath = null;
        try {
            targetPath = Files.createTempFile("dbconv", ".docx");

            if (tplURL == null)
                docxMaker.makeDocument(parseResult, targetPath);
            else
                docxMaker.makeDocument(parseResult, targetPath, tplURL);

            Files.copy(targetPath, outputStream);
        } finally {
            if (targetPath != null) Files.deleteIfExists(targetPath);
        }
    }

}
