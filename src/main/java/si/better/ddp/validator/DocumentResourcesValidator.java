package si.better.ddp.validator;

import lombok.extern.slf4j.Slf4j;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.xml.transform.StringSource;
import org.xml.sax.SAXException;
import si.better.ddp.util.MessageUtil;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class DocumentResourcesValidator
{
    /**
     * errorChannel - when error then send DocumentRaport with error
     */
    private MessageChannel errorChannel;

    @Autowired
    public DocumentResourcesValidator(MessageChannel errorChannel)
    {
        this.errorChannel = errorChannel;
    }

    /**
     * XML Validator using doctor.xsd
     *
     * @param requestBody
     * @param schemaLang
     * @param pathXSD
     */
    public boolean validateXmlDocument(String requestBody, String schemaLang, String pathXSD, Message<File> message)
    {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(schemaLang);
            javax.xml.validation.Schema schema = factory.newSchema(new StreamSource(pathXSD));
            Validator validator = schema.newValidator();
            validator.validate(new StringSource(requestBody));
            return true;
        } catch (SAXException | IOException e) {
            MessageUtil.sendErrorMessage(e, message, errorChannel);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * JSON Validator using schema.json
     *
     * @param requestBody
     */
    public boolean validateJsonDocument(String requestBody, String JSONSchema, Message<File> message) throws IOException
    {
        File file = ResourceUtils.getFile("classpath:schema/json-schema.json");

        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));

            String sch = contentBuilder.toString();
            //Schema JSONObject
            JSONObject jsonSchema = new JSONObject(
                new JSONTokener(sch));
            //Content JSONObject
            JSONObject jsonSubject = new JSONObject(
                new JSONTokener(requestBody));
            //JSON schema loader
            Schema schema = SchemaLoader.load(jsonSchema);
            schema.validate(jsonSubject);

            return true;
        } catch (ValidationException | IOException e) {
            MessageUtil.sendErrorMessage(e, message, errorChannel);
            throw new RuntimeException(e.getMessage());
        }
    }

}
