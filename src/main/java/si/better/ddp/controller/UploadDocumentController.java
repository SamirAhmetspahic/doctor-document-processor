package si.better.ddp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.util.MessageUtil;
import si.better.ddp.validator.DocumentResourcesValidator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ahmetspahics
 */
@RestController
@RequestMapping(value = ApplicationConstants.Controller.ROOT_REQUEST_MAPPING
    + ApplicationConstants.Controller.API_VERSION + ApplicationConstants.Controller.DOCTOR_REQUEST_BASE)
public class UploadDocumentController
{
    /**
     * path - Doctor XML file location
     */
    @Value("${file.dir.temp}")
    private String temp;

    /**
     * path - Doctor XML location
     */
    @Value("${file.dir.input}")
    private String pathIn;

    /**
     * path - XSD location
     */
    @Value("${file.path.schema.xsd}")
    private String XSDSchema;

    /**
     * path - Json schema location
     */
    @Value("${file.path.schema.json}")
    private String JSONSchema;

    /**
     * schemaLang
     */
    @Value("${file.path.schema.schema-lang}")
    private String schemaLang;

    /**
     * fileInputChannel - send message into integration flow
     */
    private MessageChannel restFileInputChannel;

    /**
     * errorChannel - when error then send DocumentRaport with error
     */
    private MessageChannel errorChannel;

    private DocumentResourcesValidator documentResourceValidator;

    @Autowired
    public UploadDocumentController(MessageChannel restFileInputChannel, MessageChannel errorChannel, DocumentResourcesValidator documentResourceValidator)
    {
        this.restFileInputChannel = restFileInputChannel;
        this.errorChannel = errorChannel;
        this.documentResourceValidator = documentResourceValidator;
    }

    @RequestMapping(value = ApplicationConstants.Controller.XML,
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_XML_VALUE)
    public String uploadXmlDocument(@RequestBody byte[] requestBody) throws IOException
    {
        //Create document file message with header containing DocumentReport property.
        Message<File> XMLMessage = createFileMessage(requestBody, ApplicationConstants.XML_FILE);

        //Validate XML file formmat
        documentResourceValidator.validateXmlDocument(new String(requestBody), schemaLang, XSDSchema, XMLMessage);

        //If no validation exception...

        //Send xml message into integration flow
        return sendMessage(XMLMessage);
    }

    @RequestMapping(value = ApplicationConstants.Controller.JSON, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String uploadJsonDocument(@RequestBody byte[] requestBody) throws IOException
    {
        //Create document file message with header containing DocumentReport property.
        Message<File> JSONMessage = createFileMessage(requestBody, ApplicationConstants.JSON_FILE);

        //Validate Json file formmat
        documentResourceValidator.validateJsonDocument(new String(requestBody), JSONSchema, JSONMessage);

        //If no validation exception...

        //Send xml message into integration flow
        return sendMessage(JSONMessage);
    }

    /**
     * Create message
     *
     * @param requestBody
     * @param fileName
     * @return {@link Message}
     */
    private Message<File> createFileMessage(byte[] requestBody, String fileName)
    {
        Message<File> message = null;
        try {
            //Temporary file
            Path path = Files.write(new File(temp + fileName).toPath(), requestBody);

            message = MessageBuilder.withPayload(new File(path.toUri()))
                .setHeader(ApplicationConstants.Integration.FILE_PATH, path)
                .setHeader(ApplicationConstants.Integration.MESSAGE_ERROR_FLAG, 0).build();
            return message;
        } catch (IOException e) {
            //Send message into error channel if exception...
            MessageUtil.sendErrorMessage(e, message, errorChannel);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Send message
     *
     * @param message
     * @return {@link String}
     */
    private String sendMessage(Message<File> message)
    {
        try {
            boolean sendOK = restFileInputChannel.send(message);

            if (!sendOK) {
                throw new MessageHandlingException(message);
            }
        } catch (MessageHandlingException e) {
            //Send message into error channel if exception...
            MessageUtil.sendErrorMessage(e, message, errorChannel);
        }
        return ApplicationConstants.Controller.UPLOAD_RESPONSE_OK;
    }

}
