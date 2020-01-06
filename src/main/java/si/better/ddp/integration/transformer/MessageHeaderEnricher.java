package si.better.ddp.integration.transformer;

import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.model.report.Source;
import si.better.ddp.util.MessageUtil;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author ahmetspahics
 */
@Component
public class MessageHeaderEnricher
{
    @Transformer(inputChannel = "directoryFileInputChannel", outputChannel = "headerBasedRouterChannel")
    public Message<?> enrich(Message<File> fileMessage)
    {
        return MessageUtil.enrichMessageHeader(fileMessage, Source.DIRECTORY);
    }

}
