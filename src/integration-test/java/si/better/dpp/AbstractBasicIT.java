package si.better.dpp;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import si.better.dpp.config.TestAppConfiguration;
import si.better.dpp.model.Doctor;
import si.better.dpp.model.Participant;
import si.better.dpp.model.Patient;
import si.better.dpp.service.ParticipantService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(TestAppConfiguration.class)
public abstract class AbstractBasicIT
{
    @Autowired
    protected ParticipantService<Participant> participantService;

    protected Collection<Patient> preparePatientCollection()
    {
        Patient p1 = new Patient();
        p1.setName("PATIENT-1");
        p1.setTimeCreated(LocalDateTime.now());

        Patient p2 = new Patient();
        p1.setName("PATIENT-2");
        p1.setTimeCreated(LocalDateTime.now());

        Patient p3 = new Patient();
        p1.setName("PATIENT-3");
        p1.setTimeCreated(LocalDateTime.now());

        List<Patient> participantList = new ArrayList<>();
        participantList.add(p1);
        participantList.add(p2);
        participantList.add(p3);

        return participantList;
    }

    protected Collection<Doctor> prepareDoctorCollection()
    {
        Doctor d1 = new Doctor();
        d1.setName("DOCTOR-1");
        d1.setTimeCreated(LocalDateTime.now());

        Doctor d2 = new Doctor();
        d2.setName("DOCTOR-2");
        d2.setTimeCreated(LocalDateTime.now());

        Doctor d3 = new Doctor();
        d3.setName("DOCTOR-3");
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

        return participantService.addAll(participants);
    }

    protected Patient createSinglePatient()
    {
        Patient patient = new Patient();
        patient.setName("SINGLE-PATIENT");
        patient.setTimeCreated(LocalDateTime.now());

        return patient;
    }

    protected Doctor createSingleDoctor()
    {
        Doctor doctor = new Doctor();
        doctor.setName("SINGLE-DOCTOR");
        doctor.setTimeCreated(LocalDateTime.now());

        return doctor;
    }

    protected Optional<Participant> addSingleParticipant(Participant participant)
    {
        return participantService.add(participant);
    }


}
