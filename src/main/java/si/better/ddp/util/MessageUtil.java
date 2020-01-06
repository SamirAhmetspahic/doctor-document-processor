package si.better.ddp.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.model.report.Source;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ahmetspahics
 */
public class MessageUtil
{
    public static void sendErrorMessage(Exception e, Message<?> message, MessageChannel errorChannel)
    {
        DocumentReport report = (DocumentReport) message.getHeaders()
            .get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY);
        report.setError(e.getMessage());

        Message<?> enrichedDocumentReportHeaderMessage = MessageBuilder.fromMessage(message)
            .setHeader(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY, report)
            .setHeader(ApplicationConstants.Integration.MESSAGE_ERROR_FLAG, 1)
            .build();

        errorChannel.send(new ErrorMessage(e, enrichedDocumentReportHeaderMessage));
    }

    /**
     * Generate DocumentReport with required attributes
     *
     * @return {@link DocumentReport}
     */
    private static DocumentReport getDocumentReport(Source source)
    {
        return new DocumentReport(LocalDateTime.now(), source);
    }

    public static Message<File> enrichMessageHeader(Message<File> fileMessage, Source source)
    {
        String stringPath = fileMessage.getPayload().getPath();
        String fileType = stringPath.endsWith(".json") ? "JSON" : "XML";

        Message<File> message = MessageBuilder
            .fromMessage(fileMessage)
            .setHeader("FILE-TYPE", fileType)
            .setHeader(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY, getDocumentReport(source))
            .setHeader(ApplicationConstants.Integration.FILE_PATH, Paths.get(stringPath))
            .build();
        return message;
    }

    /**
     * Generate file name unique preffix
     *
     * @return {@link String}
     */
    public static String generateDateTimePreffix()
    {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

        return now.format(formatter);
    }
}
