package si.better.ddp.integration.transformer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.util.MessageUtil;

import java.io.File;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class Json2PojoTransformer
{
    private MessageChannel errorChannel;

    @Autowired
    public Json2PojoTransformer(MessageChannel errorChannel)
    {
        this.errorChannel = errorChannel;
    }

    @Transformer(inputChannel = "json2PojoTransformerChannel", outputChannel = "doctorDto2DoctorTransformChannel")
    public Message<DoctorDto> handle(Message<File> fileMessage)
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        JsonObjectMapper<JsonNode, JsonParser> jm = new Jackson2JsonObjectMapper(mapper);
        JsonToObjectTransformer transformer = new JsonToObjectTransformer(DoctorDto.class, jm);

        DocumentReport report = (DocumentReport) fileMessage.getHeaders().get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY);
        Message<DoctorDto> transformedDoctorDtoMessage = null;
        try {
            Message<DoctorDto> doctorDtoMessage = (Message<DoctorDto>) transformer.transform(fileMessage);
            report.setDoctorId(doctorDtoMessage.getPayload().getId());
            transformedDoctorDtoMessage = MessageBuilder.withPayload(doctorDtoMessage.getPayload()).copyHeaders(fileMessage.getHeaders())
                .setHeader(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY, report).build();
        } catch (MessageTransformationException mtex) {
            MessageUtil.sendErrorMessage(mtex, fileMessage, errorChannel);
        }
        return transformedDoctorDtoMessage;
    }

}
