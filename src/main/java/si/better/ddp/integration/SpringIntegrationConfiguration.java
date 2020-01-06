package si.better.ddp.integration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator;
import com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidatorFilter;
import com.sun.org.apache.xerces.internal.impl.xs.opti.DefaultXMLDocumentHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.json.JsonToObjectTransformer;
import org.springframework.integration.router.HeaderValueRouter;
import org.springframework.integration.support.AbstractIntegrationMessageBuilder;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.support.MessageBuilderFactory;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.integration.xml.transformer.UnmarshallingTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.model.report.Source;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author ahmetspahics
 */
@Configuration
@Slf4j
public class SpringIntegrationConfiguration
{
    @Value("${file.dir.input}")
    private String inputDirectory;

    @Bean
    public MessageChannel restFileInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel directoryFileInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel headerBasedRouterChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel json2PojoTransformerChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel xml2PojoTransformerChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel doctorDto2DoctorTransformChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel writeModelChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel outChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel persistDataChannel()
    {
        return new DirectChannel();
    }

    @Bean
    @InboundChannelAdapter(value = "directoryFileInputChannel", poller = @Poller(fixedDelay = "9000"))
    public MessageSource<File> fileReadingMessageSource()
    {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File(inputDirectory));
        source.setScanEachPoll(true);
        source.setUseWatchService(true);
        return source;
    }


    @ServiceActivator(inputChannel = "headerBasedRouterChannel")
    @Bean
    public HeaderValueRouter router()
    {
        HeaderValueRouter router = new HeaderValueRouter("FILE-TYPE");
        router.setChannelMapping("JSON", "json2PojoTransformerChannel");
        router.setChannelMapping("XML", "xml2PojoTransformerChannel");
        return router;
    }

}

