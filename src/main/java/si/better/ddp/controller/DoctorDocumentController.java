package si.better.ddp.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.better.ddp.constant.ApplicationConstants;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Participant;
import si.better.ddp.model.participant.Patient;
import si.better.ddp.service.ParticipantService;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author ahmetspahics
 */
@RestController
@RequestMapping(value = ApplicationConstants.Controller.ROOT_REQUEST_MAPPING
    + ApplicationConstants.Controller.API_VERSION
    + ApplicationConstants.Controller.DOCTOR_REQUEST_BASE)
public class DoctorDocumentController
{
    private ModelMapper modelMapper;

    private ParticipantService<Doctor> participantService;

    @Autowired
    public DoctorDocumentController(ParticipantService<Doctor> participantService, ModelMapper modelMapper)
    {
        this.modelMapper = modelMapper;
        this.participantService = participantService;
    }

    /**
     * Retrieve doctor by composed, external {mapId}
     *
     * @param department - doctor {department} obtained from uploaded file
     * @param externalId - doctor {id} obtained from uploaded file
     * @return {Doctor}
     */
    @RequestMapping(ApplicationConstants.Controller.DOCTOR)
    public ResponseEntity<Doctor> getDoctor(@PathParam("department") String department,
                                            @PathParam("externalId") long externalId)
    {
        Map<Long, String> singletonMap = Collections.singletonMap(externalId, department);
        Doctor doctor = participantService.findByExternalId(singletonMap);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
    }

    /**
     * @return {Doctor collection}
     */
    @RequestMapping(ApplicationConstants.Controller.DOCTORS)
    public ResponseEntity<List<Doctor>> getDoctors()
    {
        List<Doctor> doctors = participantService.findAll();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }


}
