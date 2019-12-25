package si.better.dpp.batch.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import si.better.dpp.model.Doctor;
import si.better.dpp.service.ParticipantService;

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

    @Autowired
    public ModelWriter(final ParticipantService<Doctor> participantService)
    {
        this.participantService = participantService;
    }

    @Override
    public void write(List<? extends Doctor> doctors) throws Exception
    {
        log.debug("ModelWriter start");
        doctors.forEach(doctor -> participantService.add(doctor));
        log.debug("ModelWriter end");
    }
}
