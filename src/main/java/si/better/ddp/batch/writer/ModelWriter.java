package si.better.ddp.batch.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.service.DocumentService;
import si.better.ddp.service.ParticipantService;

import java.util.List;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class ModelWriter implements ItemWriter<Doctor>
{
    /**
     * participantService
     */
    private ParticipantService<Doctor> participantService;

    private DocumentService documentService;

    private StepExecution stepExecution;

    private JobExecution jobExecution;

    private DocumentReport report;

    @Autowired
    public ModelWriter(final ParticipantService<Doctor> participantService, final DocumentService documentService)
    {
        this.participantService = participantService;
        this.documentService =  documentService;
/*        jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        report = (DocumentReport) jobContext.get("DOCUMENT-REPORT");*/
    }

    @Override
    public void write(List<? extends Doctor> doctors) throws Exception
    {
        try {
            log.debug("ModelWriter start");
            doctors.forEach(doctor -> participantService.add(doctor));
            log.debug("ModelWriter end");
        } catch (Exception e) {
/*            log.error(e.getMessage());
            report.setStepComponent(StepComponent.PROCESSOR);
            report.setError(e.getMessage());
            documentService.save(report);
            jobExecution.stop();*/
        }
    }

    @BeforeStep
    public void saveStepExecution(final StepExecution stepExecution)
    {
        this.stepExecution = stepExecution;
    }
}
