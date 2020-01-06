package si.better.ddp.integration.transformer;

import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.dto.PatientDto;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Patient;
import si.better.ddp.util.MessageUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ahmetspahics
 */
@Component
public class DoctorDto2DoctorTransformer
{
    private ModelMapper modelMapper;

    private MessageChannel errorChannel;

    @Autowired
    public DoctorDto2DoctorTransformer(final ModelMapper modelMapper, MessageChannel errorChannel)
    {
        this.modelMapper = modelMapper;
        this.errorChannel = errorChannel;
    }

    @Transformer(inputChannel = "doctorDto2DoctorTransformChannel", outputChannel = "persistDataChannel")
    public Message<Doctor> transformDoctorDto2DoctorModel(Message<DoctorDto> doctorDtoMessge)
    {
        Message<Doctor> message = null;

        DoctorDto doctorDto = doctorDtoMessge.getPayload();
        Map<String, String> mapId = new HashMap<>();
        mapId.put(doctorDto.getId(), doctorDto.getDepartment());

        List<PatientDto> patiendDtoList = doctorDto.getPatients().getPatient();

        try {
            Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
            doctor.setCreatedBy("INPUT-FOLDER");
            doctor.setTimeCreated(LocalDateTime.now());
            doctor.setMapId(mapId);

            List<Patient> patients = patiendDtoList.stream()
                .map(patientDto ->
                {
                    Map<String, String> patientMapId = new HashMap<>();
                    patientMapId.put(patientDto.getId(), doctorDto.getDepartment());

                    Patient patient = modelMapper.map(patientDto, Patient.class);
                    patient.setDoctor(doctor);
                    patient.setTimeCreated(LocalDateTime.now());
                    patient.setMapId(patientMapId);
                    patient.setDiseasesCollection(patientDto.getDiseases().getDisease());

                    return patient;
                })
                .collect(Collectors.toList());

            doctor.setPatients(patients);

            message = MessageBuilder.withPayload(doctor)
                .copyHeaders(doctorDtoMessge.getHeaders())
                .build();
            if (doctor == null) {
                throw new MappingException(Arrays.asList(new org.modelmapper.spi.ErrorMessage("Mapping exception")));
            }
            if (message == null) {
                throw new MessageHandlingException(doctorDtoMessge);
            }
        } catch (MappingException mex) {
            MessageUtil.sendErrorMessage(mex, message, errorChannel);
        } catch (MessageHandlingException hex) {
            MessageUtil.sendErrorMessage(hex, doctorDtoMessge, errorChannel);
        }

        return message;
    }
}
