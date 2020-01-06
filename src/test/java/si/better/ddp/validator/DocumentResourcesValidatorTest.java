package si.better.ddp.validator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author ahmetspahics
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Slf4j
public class DocumentResourcesValidatorTest
{
    private MessageChannel errorChannel = Mockito.mock(MessageChannel.class);
    private DocumentResourcesValidator documentValidator = new DocumentResourcesValidator(errorChannel);

    private String fetchResource(String path) throws FileNotFoundException
    {
        String content = null;

        File file = ResourceUtils.getFile(path);

        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
            content = contentBuilder.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return content;
    }

    @Test
    public void validateXmlContent_When_ContentIsOk() throws IOException, SAXException
    {
        String content = fetchResource("src/test/resources/doctor-example.xml");

        boolean isValid = documentValidator.validateXmlDocument(content, "http://www.w3.org/2001/XMLSchema", "src/main/resources/schema/doctor.xsd", null);
        //then
        Assert.assertTrue(isValid);
    }

    @Test(expected = Exception.class)
    public void validateXmlContent_When_ContentIsNotCorrect_Then_ValidationException() throws FileNotFoundException
    {
        String content = fetchResource("src/test/resources/missing-attributes-doctor-example.xml");

        Map<String, Object> map = new HashMap<>();
        map.put("TEST-KEY", "TEST-VALUE");
        MessageHeaders headers = new MessageHeaders(map);

        Message message = MessageBuilder.createMessage("some text", headers);
        documentValidator.validateXmlDocument(content, "http://www.w3.org/2001/XMLSchema", "src/main/resources/schema/doctor.xsd", message);
    }

    @Test
    public void validateJsonContent_When_ContentIsOk() throws IOException
    {
        String content = fetchResource("src/test/resources/doctor-example.json");

        Map<String, Object> map = new HashMap<>();
        map.put("TEST-KEY", "TEST-VALUE");
        MessageHeaders headers = new MessageHeaders(map);

        Message message = MessageBuilder.createMessage("some text", headers);
        boolean isValid = documentValidator.validateJsonDocument(content,"classpath:schema/json-schema.json",message);
        Assert.assertTrue(isValid);
    }

    @Test(expected = Exception.class)
    public void validateJsonContent_When_ContentIsNotCorrect_Then_Exception() throws IOException
    {
        String content = fetchResource("src/test/resources/without-root-element-doctor-example.json");

        Map<String, Object> map = new HashMap<>();
        map.put("TEST-KEY", "TEST-VALUE");
        MessageHeaders headers = new MessageHeaders(map);

        Message message = MessageBuilder.createMessage("some text", headers);
        documentValidator.validateJsonDocument(content,"classpath:schema/json-schema.json",message);
    }

}
