package si.better.ddp.service;

import org.junit.Test;
import si.better.ddp.AbstractBasicIT;
import si.better.ddp.constant.TestApplicationConstants;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Participant;
import si.better.ddp.model.participant.Patient;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * @author ahmetspahics
 */
@Transactional
public class ParticipantServiceIntegrationTest extends AbstractBasicIT
{
    @Test
    public void givenParticipantCollection_whenSave_returnOk()
    {
        List<Optional<Participant>> participants = addAllParticipants();
        assertEquals(TestApplicationConstants.NUMBER_OF_PARTICIPANT_RECORDS, participants.size());
    }

    @Test
    public void givenParticipanstsPair_afterSave_returnCorectSubtypes()
    {
        if (!(addSingleParticipant(createSinglePatient()).get() instanceof Patient)
            && !(addSingleParticipant(createSingleDoctor()).get() instanceof Doctor)) {
            throw new IllegalArgumentException();
        }

    }

    @Test(expected = ValidationException.class)
    public void givenParticipantWithoutRequiredAttribute_whenSave_throwValidationException()
    {
        createAndSaveParticipantWithoutRequiredAttribute();
    }
}
