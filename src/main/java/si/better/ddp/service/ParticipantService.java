package si.better.ddp.service;

import si.better.ddp.model.participant.Participant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
public interface ParticipantService<T extends Participant>
{
    T findByInternalId(long id);

    T findByExternalId(Map<Long, String> mapId);

    List<T> findAll();

    Optional<T> add(T participant);

    List<Optional<T>> addAll(List<T> participantCollection);
}
