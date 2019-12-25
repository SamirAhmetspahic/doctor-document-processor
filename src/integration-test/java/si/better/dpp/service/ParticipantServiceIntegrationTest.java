package si.better.dpp.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.Assert;
import si.better.dpp.AbstractBasicIT;
import si.better.dpp.constant.TestApplicationConstants;
import si.better.dpp.model.Doctor;
import si.better.dpp.model.Participant;
import si.better.dpp.model.Patient;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @author ahmetspahics
 */
public class ParticipantServiceIntegrationTest extends AbstractBasicIT
{
    @Test
    @Transactional
    public void givenParticipantCollection_whenSave_returnOk()
    {
        List<Optional<Participant>> participants = addAllParticipants();
        assertEquals(TestApplicationConstants.NUMBER_OF_RECORDS, participants.size());
    }

    @Test
    @Transactional
    public void givenParticipanstsPair_afterSave_returnCorectSubtypes()
    {
        if (!(addSingleParticipant(createSinglePatient()).get() instanceof Patient)
            && !(addSingleParticipant(createSingleDoctor()).get() instanceof Doctor)) {
            throw new IllegalArgumentException();
        }

    }
}
