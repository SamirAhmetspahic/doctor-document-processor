package si.better.ddp;

import org.hibernate.id.UUIDGenerator;
import org.hibernate.type.UUIDCharType;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import si.better.ddp.config.TestAppConfiguration;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Participant;
import si.better.ddp.model.participant.Patient;
import si.better.ddp.service.ParticipantService;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author ahmetspahics
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TestAppConfiguration.class)
public abstract class AbstractBasicIT
{
    @Autowired
    protected ParticipantService<Participant> participantService;

    protected List<Patient> preparePatientCollection()
    {
        Patient p1 = new Patient();
        p1.setId(UUID.randomUUID().toString().replace("-", ""));
        p1.setName("PATIENT-1");
        p1.setTimeCreated(LocalDateTime.now());

        Patient p2 = new Patient();
        p2.setId(UUID.randomUUID().toString().replace("-", ""));
        p1.setName("PATIENT-2");
        p1.setTimeCreated(LocalDateTime.now());

        Patient p3 = new Patient();
        p3.setId(UUID.randomUUID().toString().replace("-", ""));
        p1.setName("PATIENT-3");
        p1.setTimeCreated(LocalDateTime.now());

        List<Patient> participantList = new ArrayList<>();
        participantList.add(p1);
        participantList.add(p2);
        participantList.add(p3);

        return participantList;
    }

    protected List<Doctor> prepareDoctorCollection()
    {
        Doctor d1 = new Doctor();
        d1.setId(UUID.randomUUID().toString().replace("-", ""));
        d1.setTimeCreated(LocalDateTime.now());

        Doctor d2 = new Doctor();
        d2.setId(UUID.randomUUID().toString().replace("-", ""));
        d2.setTimeCreated(LocalDateTime.now());

        Doctor d3 = new Doctor();
        d3.setId(UUID.randomUUID().toString().replace("-", ""));
        d3.setTimeCreated(LocalDateTime.now());

        List<Doctor> participantList = new ArrayList<>();
        participantList.add(d1);
        participantList.add(d2);
        participantList.add(d3);

        return participantList;
    }

    protected List<Optional<Participant>> addAllParticipants()
    {
        Collection<Patient> patients = preparePatientCollection();
        Collection<Doctor> doctors = prepareDoctorCollection();
        List<Participant> participants = new ArrayList<>();
        participants.addAll(patients);
        participants.addAll(doctors);

        List<Optional<Participant>> optionals = participantService.addAll(participants);
        return optionals;
    }

    protected Patient createSinglePatient()
    {
        Patient patient = new Patient();
        patient.setId(UUID.randomUUID().toString().replace("-", ""));
        patient.setName("SINGLE-PATIENT");
        patient.setTimeCreated(LocalDateTime.now());

        return patient;
    }

    protected Doctor createSingleDoctor()
    {
        Doctor doctor = new Doctor();
        doctor.setId(UUID.randomUUID().toString().replace("-", ""));
        doctor.setTimeCreated(LocalDateTime.now());

        return doctor;
    }

    protected void createAndSaveParticipantWithoutRequiredAttribute()
    {
        Doctor doctor = new Doctor();
        doctor.setTimeCreated(LocalDateTime.now());
        doctor.setDepartment("Department");
        participantService.add(doctor);
    }


    protected Optional<Participant> addSingleParticipant(Participant participant)
    {
        return participantService.add(participant);
    }


}
