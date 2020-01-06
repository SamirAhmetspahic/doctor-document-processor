package si.better.ddp.integration.activator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.service.DocumentService;
import si.better.ddp.util.FileUtil;
import si.better.ddp.util.MessageUtil;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class ErrorServiceActivator
{
    /**
     * errorPATH -
     */
    @Value("${file.dir.error}")
    private String errorPATH;

    private DocumentService documentService;

    @Autowired
    public ErrorServiceActivator(DocumentService documentService)
    {
        this.documentService = documentService;
    }

    @ServiceActivator(inputChannel = "errorChannel")
    public void handleErrorMessage(ErrorMessage errorMessage)
    {
        Message<File> originalMessage;

        if (errorMessage.getOriginalMessage().getPayload() instanceof File) {
            originalMessage = (Message<File>) errorMessage.getOriginalMessage();
            Path sourcePath = (Path) originalMessage.getHeaders().get(ApplicationConstants.Integration.FILE_PATH);
            DocumentReport report = (DocumentReport) originalMessage.getHeaders()
                .get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY);

            Path path = FileUtil.rename(sourcePath,
                Paths.get(errorPATH + MessageUtil.generateDateTimePreffix() + sourcePath.getFileName()));

            if (path == null) {
                log.warn("The file could not be renamed");
            }

            documentService.save(report);
        }
    }
}
