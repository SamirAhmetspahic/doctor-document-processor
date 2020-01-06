package si.better.ddp.integration.activator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Participant;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.service.DocumentService;
import si.better.ddp.service.ParticipantService;
import si.better.ddp.util.FileUtil;
import si.better.ddp.util.MessageUtil;

import javax.persistence.PersistenceException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class PersistDataServiceActivator
{
    /**
     * outPATH -
     */
    @Value("${file.dir.out}")
    private String outPATH;

    /**
     * participantService
     */
    private ParticipantService<Participant> participantService;

    /**
     * documentService
     */
    private DocumentService documentService;

    /**
     * errorChannel - send DocumentRaport with error message
     */
    private MessageChannel errorChannel;

    @Autowired
    public PersistDataServiceActivator(ParticipantService<Participant> participantService, DocumentService documentService)
    {
        this.participantService = participantService;
        this.documentService = documentService;
    }

    @ServiceActivator(inputChannel = "persistDataChannel")
    public void write(Message<Doctor> message) throws Exception
    {
        DocumentReport report = (DocumentReport) message.getHeaders().get(ApplicationConstants.Integration.MESSAGE_HEADER_REPORT_KEY);
        Doctor doctor = message.getPayload();

        try {
            Optional<Participant> savedDoctor = participantService.add(doctor);
            if (savedDoctor == null) {
                throw new PersistenceException(String.format("Cann't persist Doctor model with id = %s", doctor.getId()));
            }
            documentService.save(report);
            log.info(String.format("Doctor with external id = %s successfully persisted into database", doctor.getId()));

            //Move source file to target destination
            Path sourcePath = (Path) message.getHeaders().get(ApplicationConstants.Integration.FILE_PATH);
            FileUtil.rename(sourcePath, Paths.get(outPATH + MessageUtil.generateDateTimePreffix() + sourcePath.getFileName()));
        } catch (PersistenceException pex) {
            log.error(pex.getMessage());
            MessageUtil.sendErrorMessage(pex, message, errorChannel);
        }
    }
}
