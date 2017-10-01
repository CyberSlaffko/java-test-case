package ru.ncore.docs.pdf.converter;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.*;
import org.apache.xmlgraphics.util.MimeConstants;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
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

    Configuration fopConfig;

    DocumentBuilder documentBuilder;

    TransformerFactory transformerFactory;

    /**
     * Default constructor trying to initialize FopFactory with baseURI of current directory
     * and default config file from classpath. In this case fonts also are searching
     * in classpath and default system locations.
     */
    public PdfMaker() {
        initFopFactoryConfiguration();
        initDocumentBuilder();
        initTransformerFactory();
    }

    /**
     * This constructor allows to externalize FopFactory configuration.
     * @param fopBaseURI baseURI of FopFactory instance
     * @param fopConfigStream stream of FOP-configuration file
     */
    public PdfMaker(URI fopBaseURI, InputStream fopConfigStream) {
        this.fopBaseURI = fopBaseURI;
        this.fopConfigStream = fopConfigStream;
        initFopFactoryConfiguration();
        initDocumentBuilder();
        initTransformerFactory();
    }

    private void initFopFactoryConfiguration() {
        try {
            logger.info("Initializing Fop configuration instance");
            if (fopBaseURI == null) {
                fopBaseURI = Paths.get("").toUri();
            }
            if (fopConfigStream == null) {
                URL confURL = getClass().getResource(DEFAULT_FOP_CONFIG);
                if (confURL == null) throw new FileNotFoundException("Cannot find default FOP configuration file \"" + DEFAULT_FOP_CONFIG + "\"");
                fopConfigStream = confURL.openStream();
            }

            DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
            fopConfig = cfgBuilder.build(fopConfigStream);

        } catch (Exception e) {
            logger.error("Cannot initialize fopConfig", e);
            fopConfig = null;
        }
    }

    private void initDocumentBuilder() {
        try {
            logger.info("Initializing DocumentBuilder instance");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringElementContentWhitespace(true);
            dbf.setXIncludeAware(true);
            dbf.setNamespaceAware(true);
            documentBuilder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            logger.error("Cannot initialize documentBuilder", e);
            documentBuilder = null;
        }
    }

    private void initTransformerFactory() {
        this.transformerFactory = TransformerFactory.newInstance();
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

    private Source getXmlSource(InputSource xmlSource) throws IOException, SAXException {
        if (documentBuilder == null)
            throw new IllegalStateException("documentBuilder not initialized");

        Document xmlDocument = documentBuilder.parse(xmlSource);
        xmlDocument.getDocumentElement().normalize();
        return new DOMSource(xmlDocument, xmlSource.getSystemId());
    }

    private Source getXmlSource(String xmlURL) throws IOException, SAXException {
        return getXmlSource(new InputSource(xmlURL));
    }

    private Source getXmlSource(InputStream xmlStream, String systemId) throws IOException, SAXException {
        InputSource xmlSource = new InputSource(xmlStream);
        if (systemId != null) xmlSource.setSystemId(systemId);
        return getXmlSource(xmlSource);
    }

    public void generateOut(Map<String, Object> parameters, String xmlURL, String xslURL, String mime, OutputStream out) throws Exception {
        Source xmlSource = getXmlSource(xmlURL);
        Source xslSource = new StreamSource(xslURL);
        generateOut(parameters, xmlSource, xslSource, mime, out);
    }

    public void generateOut(Map<String, Object> parameters, InputStream xml, InputStream xsl, String mime, OutputStream out) throws Exception {
        Source xmlSource = getXmlSource(xml, null);
        Source xslSource = new StreamSource(xsl);
        generateOut(parameters, xmlSource, xslSource, mime, out);
    }

    public void generateOut(Map<String, Object> parameters, Source xmlSource, Source xslSource, String mime, OutputStream out) throws Exception {
        try {
            if (fopConfig == null)
                throw new IllegalStateException("Fop configuration not initialized");

            if (transformerFactory == null)
                throw new IllegalStateException("TransformerFactory not initialized");

            if(parameters == null)parameters = new HashMap<>();

            if(mime == null)mime = DEFAULT_MIME;

            URI xmlSourceURI = xmlSource.getSystemId() != null ? new URI(xmlSource.getSystemId()) : Paths.get("").toUri();
            URI fopBaseURI = xmlSourceURI.resolve(".");

            logger.debug("Fop baseURI " + fopBaseURI);

            FopFactory fopFactory = new FopFactoryBuilder(fopBaseURI).setConfiguration(fopConfig).build();

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

            if (parameters.containsKey("title"))
                foUserAgent.setTitle((String) parameters.get("title"));

            if (parameters.containsKey("author"))
                foUserAgent.setTitle((String) parameters.get("author"));

            Fop fop = foUserAgent.newFop(mime, out);

            Transformer transformer = transformerFactory.newTransformer(xslSource);

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

    public void xsltProcess(Source xmlSource,Source xslSource,Map<String,Object> parameters,Result outTarget) throws Exception{
        if (transformerFactory == null)
            throw new IllegalStateException("TransformerFactory not initialized");

        Transformer transformer = transformerFactory.newTransformer(xslSource);
        for(String parameterName: parameters.keySet()){
            transformer.setParameter(parameterName,parameters.get(parameterName));
        }
        transformer.transform(xmlSource,outTarget);
    }
}
