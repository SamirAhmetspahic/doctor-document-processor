package si.better.ddp.integration.transformer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.transformer.MessageTransformationException;
import org.springframework.integration.xml.transformer.UnmarshallingTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
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
public class Xml2PojoTransformer
{
    /**
     * schemaLang
     */
    @Value("${file.path.schema.schema-lang}")
    private String schemaLang;

    /**
     * path - XSD location
     */
    @Value("${file.path.schema.xsd}")
    private String XSDSchema;

    private MessageChannel errorChannel;

    @Autowired
    public Xml2PojoTransformer(MessageChannel errorChannel)
    {
        this.errorChannel = errorChannel;
    }

    @Transformer(inputChannel = "xml2PojoTransformerChannel", outputChannel = "doctorDto2DoctorTransformChannel")
    public Message<DoctorDto> unmarshallingTransformer(Message<File> fileMessage)
    {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[]{DoctorDto.class});
        UnmarshallingTransformer transformer = new UnmarshallingTransformer(marshaller);

        Message<DoctorDto> doctorDtoMessage = null;

        DocumentReport report = (DocumentReport) fileMessage.getHeaders().get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY);
        try {
            DoctorDto doctorDto = (DoctorDto) transformer.transformPayload(fileMessage.getPayload());
            report.setDoctorId(doctorDto.getId());

            doctorDtoMessage = MessageBuilder.withPayload(doctorDto).copyHeaders(fileMessage.getHeaders())
                .setHeader(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY, report).build();
        } catch (MessageTransformationException mtex) {
            MessageUtil.sendErrorMessage(mtex, fileMessage, errorChannel);
            log.error("Exception while transforme xml message>: " + mtex.getMessage());
        } catch (Exception e) {
            MessageUtil.sendErrorMessage(e, fileMessage, errorChannel);
            log.error(e.getMessage());
        }
        return doctorDtoMessage;
    }
}
