package si.better.ddp.transformer;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.integration.transformer.Json2PojoTransformer;
import si.better.ddp.integration.transformer.Xml2PojoTransformer;
import si.better.ddp.model.report.DocumentReport;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author ahmetspahics
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Slf4j
public class Xml2PojoTransformerTest
{
    private MessageChannel errorChannel = Mockito.mock(MessageChannel.class);

    private Message<File> createMessage(String path)
    {
        File file = new File(Paths.get(path).toUri());
        DocumentReport report = new DocumentReport();
        Message<File> message = MessageBuilder.withPayload(file)
            .setHeader(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY, report)
            .setHeader(ApplicationConstants.Integration.FILE_PATH, file.getPath()).build();
        return message;
    }

    @Test
    public void transformXmlMessage()
    {
        Xml2PojoTransformer transformer = new Xml2PojoTransformer(errorChannel);
        Message<DoctorDto> doctorDtoMessage = transformer.unmarshallingTransformer(createMessage("src/test/resources/doctor-example.xml"));
        assertTrue(doctorDtoMessage.getPayload() instanceof DoctorDto);
        assertTrue(doctorDtoMessage.getHeaders()
            .get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY) instanceof DocumentReport);
        assertNotNull(ApplicationConstants.Integration.FILE_PATH);
    }

    @Test
    public void transformJsonMessage()
    {
        Json2PojoTransformer transformer = new Json2PojoTransformer(errorChannel);
        Message<DoctorDto> doctorDtoMessage = transformer.handle(createMessage("src/test/resources/doctor-example.json"));
        assertTrue(doctorDtoMessage.getPayload() instanceof DoctorDto);
        assertTrue(doctorDtoMessage.getHeaders()
            .get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY) instanceof DocumentReport);
        assertNotNull(ApplicationConstants.Integration.FILE_PATH);
    }

}
