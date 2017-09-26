package ru.ncore.docs.app;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.Yaml;
import ru.ncore.docs.docbook.Document;
import ru.ncore.docs.docbook.Parser;
import ru.ncore.docs.pdf.converter.PdfMaker;
import ru.ncore.docs.templates.pmi.DocxMaker;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Created by CyberSlaffko on 21.09.2017.
 */
@RestController
public class MainController {
    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_MSWORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    @Autowired
    private Parser parser;

    @Autowired
    private DocxMaker docxMaker;

    @Autowired
    private PdfMaker pdfMaker;

    @RequestMapping(value={"/converter/{out:.+}"}, method = RequestMethod.GET)
    public ResponseEntity<byte[]> convert(
            @PathVariable("out") String out,
            @RequestParam(value="src") String srcURL,
            @RequestParam(value="tpl", required = false) String tplURL,
            @RequestParam(value="params",defaultValue="") String parameters) throws Exception{

        Yaml yaml = new Yaml();
        @SuppressWarnings("unchecked")
        Map<String,Object> params = (Map<String,Object>)yaml.load(parameters);

        String ext = FilenameUtils.getExtension(out);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        HttpHeaders responseHeaders = new HttpHeaders();

        switch (ext){
            case "pdf":
                pdfMaker.generateOut(params, srcURL, tplURL, MIME_PDF, outputStream);
                responseHeaders.setContentType(MediaType.valueOf(MIME_PDF));
                break;
            case "png":
                pdfMaker.generateOut(params, srcURL, tplURL, MIME_PNG, outputStream);
                responseHeaders.setContentType(MediaType.valueOf(MIME_PNG));
                break;
            case "docx":
                generateDocx(srcURL, tplURL, outputStream);
                responseHeaders.setContentType(MediaType.valueOf(MIME_MSWORD));
                break;
            default:
                throw new RuntimeException("Unsupported format: " + ext);
        }

        byte[] result = outputStream.toByteArray();

        responseHeaders.setContentLength(result.length);
        responseHeaders.setContentDispositionFormData(out, URLEncoder.encode(out, "UTF-8"));

        return new ResponseEntity<> (result,
                responseHeaders,
                HttpStatus.OK);
    }

    private void generateDocx(String srcURL, String tplURL, OutputStream outputStream) throws IOException {
        final Document parseResult = parser.parse(srcURL);
        Path targetPath = null;
        try {
            targetPath = Files.createTempFile("dbconv", ".docx");

            if (tplURL == null || "".equals(tplURL))
                docxMaker.makeDocument(parseResult, targetPath);
            else
                docxMaker.makeDocument(parseResult, targetPath, tplURL);

            Files.copy(targetPath, outputStream);
        } finally {
            if (targetPath != null) Files.deleteIfExists(targetPath);
        }
    }

}
