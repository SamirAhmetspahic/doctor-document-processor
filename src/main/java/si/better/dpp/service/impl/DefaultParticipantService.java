package si.better.dpp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.better.dpp.model.Participant;
import si.better.dpp.repository.ParticipantRepository;
import si.better.dpp.service.ParticipantService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ahmetspahics
 */
@Service
public class DefaultParticipantService<T extends Participant> implements ParticipantService<T>
{
    private ParticipantRepository<T> participantRepository;

    /**
     * @param participantRepository - Constructor injection
     */
    @Autowired
    public DefaultParticipantService(ParticipantRepository<T> participantRepository)
    {
        this.participantRepository = participantRepository;
    }

    /**
     * @param id - Unique identifier of Participant
     *           return Participant
     */
    @Override
    public T findById(long id)
    {
        return null;
    }

    /**
     * return Participant collection
     */
    @Override
    public List<T> findAll()
    {
        return participantRepository.findAll();
    }

    @Override
    @Transactional
    public Optional<T> add(T participant)
    {
        participantRepository.save(participant);
        return participantRepository.findById(participant.getId());
    }

    @Override
    @Transactional
    public List<Optional<T>> addAll(List<T> participantCollection)
    {
        participantRepository.saveAll(participantCollection);
        return participantCollection.stream()
            .map(participant -> participantRepository.findById(participant.getId()))
            .collect(Collectors.toList());
    }
}
