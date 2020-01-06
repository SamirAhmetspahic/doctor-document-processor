package si.better.ddp.batch.processor;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Patient;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.service.DocumentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ahmetspahics
 */
@Component
@Slf4j
public class DoctorDto2DoctorModelProcessor implements ItemProcessor<DoctorDto, Doctor>
{
    private StepExecution stepExecution;

    private ModelMapper modelMapper;

    private DocumentService documentService;

    @Autowired
    public DoctorDto2DoctorModelProcessor(final ModelMapper modelMapper, final DocumentService documentService)
    {
        this.modelMapper = modelMapper;
    }

    /**
     * @param doctorDTO - DTO obtained from file reader
     *                  return Doctor model
     */
    @Override
    public Doctor process(DoctorDto doctorDTO) throws Exception
    {
        JobExecution jobExecution = stepExecution.getJobExecution();
        ExecutionContext jobContext = jobExecution.getExecutionContext();
        DocumentReport report = (DocumentReport) jobContext.get("DOCUMENT-REPORT");

        Doctor doctor = modelMapper.map(doctorDTO, Doctor.class);

        try {
            List<Patient> patients = doctorDTO.getPatients().getPatient()
                .stream()
                .map(patientDto ->
                {
                    Patient patient = modelMapper.map(patientDto, Patient.class);
                    patient.setDiseasesCollection(patientDto.getDiseases().getDisease());
                    patient.setDoctor(doctor);
                    return patient;
                })
                .collect(Collectors.toList());

            doctor.setPatients(patients);
        } catch (Exception e) {
            log.error(e.getMessage());
            report.setError(e.getMessage());
            jobContext.put("DOCUMENT-REPORT", report);
            documentService.save(report);
            jobExecution.stop();
        }

        return doctor;
    }

    @BeforeStep
    public void saveStepExecution(final StepExecution stepExecution)
    {
        this.stepExecution = stepExecution;
    }

}
