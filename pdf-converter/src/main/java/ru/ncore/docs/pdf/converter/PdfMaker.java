package ru.ncore.docs.pdf.converter;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.*;
import org.apache.xmlgraphics.util.MimeConstants;
import org.slf4j.Logger;

import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CyberSlaffko on 21.09.2017.
 */
public class PdfMaker {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    public static final String DEFAULT_MIME = MimeConstants.MIME_PDF;
    public static final String EMPTY_XML="<?xml version=\"1.0\"?>\n<empty/>\n";

    public static final String DEFAULT_FOP_CONFIG="/fop/fop.xml";

    private URI fopBaseURI = null;
    private InputStream fopConfigStream = null;

    FopFactory fopFactory;

    /**
     * Default constructor trying to initialize FopFactory with baseURI of current directory
     * and default config file from classpath. In this case fonts also are searching
     * in classpath and default system locations.
     */
    public PdfMaker() {
        initFopFactory();
    }

    /**
     * This constructor allows to externalize FopFactory configuration.
     * @param fopBaseURI baseURI of FopFactory instance
     * @param fopConfigStream stream of FOP-configuration file
     */
    public PdfMaker(URI fopBaseURI, InputStream fopConfigStream) {
        this.fopBaseURI = fopBaseURI;
        this.fopConfigStream = fopConfigStream;
        initFopFactory();
    }

    private void initFopFactory() {
        try {
            logger.info("Initializing FopFactory instance");
            if (fopBaseURI == null) {
                fopBaseURI = Paths.get("").toUri();
            }
            if (fopConfigStream == null) {
                URL confURL = getClass().getResource(DEFAULT_FOP_CONFIG);
                if (confURL == null) throw new FileNotFoundException("Cannot find default FOP configuration file \"" + DEFAULT_FOP_CONFIG + "\"");
                fopConfigStream = confURL.openStream();
            }
            fopFactory = FopFactory.newInstance(fopBaseURI, fopConfigStream);
        } catch (Exception e) {
            logger.error("Cannot initialize fopFactory", e);
            fopFactory = null;
        }
    }

    public void cleanUp() throws Exception {
        logger.info("Cleaning PdfMaker");
        IOUtils.closeQuietly(fopConfigStream);
    }

    public void generatePdf(Map<String, Object> parameters, InputStream xml, InputStream xsl, OutputStream out) throws Exception {
        generateOut(parameters, xml, xsl, MimeConstants.MIME_PDF, out);
    }

    public void generatePng(Map<String, Object> parameters, InputStream xml, InputStream xsl, OutputStream out) throws Exception {
        generateOut(parameters, xml, xsl, MimeConstants.MIME_PNG, out);
    }

    public void generateOut(Map<String, Object> parameters, String xmlURL, String xslURL, String mime, OutputStream out) throws Exception {
        Source xmlSource = new StreamSource(xmlURL);
        Source xslSource = new StreamSource(xslURL);
        generateOut(parameters, xmlSource, xslSource, mime, out);
    }

    public void generateOut(Map<String, Object> parameters, InputStream xml, InputStream xsl, String mime, OutputStream out) throws Exception {
        Source xmlSource = new StreamSource(xml);
        Source xslSource = new StreamSource(xsl);
        generateOut(parameters, xmlSource, xslSource, mime, out);
    }

    public void generateOut(Map<String, Object> parameters, Source xmlSource, Source xslSource, String mime, OutputStream out) throws Exception {
        try {
            if (fopFactory == null)
                throw new IllegalStateException("FopFactory not initialized");

            if(parameters == null)parameters = new HashMap<>();

            if(mime == null)mime = DEFAULT_MIME;

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            if (parameters.containsKey("title"))
                foUserAgent.setTitle((String) parameters.get("title"));

            if (parameters.containsKey("author"))
                foUserAgent.setTitle((String) parameters.get("author"));

            Fop fop = fopFactory.newFop(mime, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xslSource);

            for(String parameterName: parameters.keySet()){
                transformer.setParameter(parameterName,parameters.get(parameterName));
            }

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);

        }catch(Exception e){
            logger.error("Error while processing pdf",e);
            throw e;
        }
    }

    public static void xsltProcess(Source xmlSource,Source xslSource,Map<String,Object> parameters,Result outTarget) throws Exception{
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer(xslSource);
        for(String parameterName: parameters.keySet()){
            transformer.setParameter(parameterName,parameters.get(parameterName));
        }
        transformer.transform(xmlSource,outTarget);
    }
}
