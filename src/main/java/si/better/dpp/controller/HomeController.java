package si.better.dpp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.better.dpp.model.Doctor;
import si.better.dpp.service.ParticipantService;

/**
 * @author ahmetspahics
 */
@RestController
public class HomeController
{
    @Autowired
    private ParticipantService<Doctor> participantService;

    @RequestMapping("add/doctor")
    public ResponseEntity<?> saveParticipant()
    {
        return null;
    }
}
