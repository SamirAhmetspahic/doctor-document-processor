package si.better.ddp.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import si.better.ddp.AbstractBasicIT;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.constant.TestApplicationConstants;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author ahmetspahics
 */

@Slf4j
public class UploadDocumentControllerTest extends AbstractBasicIT
{
    @LocalServerPort
    private int port;

    @Value("${file.dir.classpath-resources.input.xml}")
    private String xmlPATH;

    @Value("${file.dir.classpath-resources.input.json}")
    private String jsonPATH;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @Transactional
    public void testXmlDocumentUpload() throws FileNotFoundException
    {
        String content = buildDocumentContent(xmlPATH);

        HttpHeaders headers = createHttpHeaders(MediaType.APPLICATION_XML);

        //given
        HttpEntity<String> entity = new HttpEntity<>(content, headers);

        //when
        int statusCode = restTemplate
            .postForEntity("http://localhost:" + port + "/"
                + ApplicationConstants.Controller.ROOT_REQUEST_MAPPING
                + ApplicationConstants.Controller.API_VERSION
                + ApplicationConstants.Controller.DOCTOR_REQUEST_BASE
                + ApplicationConstants.Controller.XML, entity, String.class).getStatusCodeValue();

        //then
        assertEquals(
            TestApplicationConstants.STATUS_CODE_OK, statusCode
        );

    }

    @Test
    @Transactional
    public void testJsonDocumentUpload() throws FileNotFoundException
    {
        String content = buildDocumentContent(jsonPATH);

        HttpHeaders headers = createHttpHeaders(MediaType.APPLICATION_JSON);

        //given
        HttpEntity<String> entity = new HttpEntity<>(content, headers);

        //when
        int statusCode = restTemplate
            .postForEntity("http://localhost:" + port + "/"
                + ApplicationConstants.Controller.ROOT_REQUEST_MAPPING
                + ApplicationConstants.Controller.API_VERSION
                + ApplicationConstants.Controller.DOCTOR_REQUEST_BASE
                + ApplicationConstants.Controller.JSON, entity, String.class).getStatusCodeValue();

        //then
        assertEquals(
            TestApplicationConstants.STATUS_CODE_OK, statusCode);

    }

    public HttpHeaders createHttpHeaders(MediaType mediaType)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }

    public String buildDocumentContent(String path) throws FileNotFoundException
    {
        File file = ResourceUtils.getFile(path);

        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return contentBuilder.toString();
    }
}
