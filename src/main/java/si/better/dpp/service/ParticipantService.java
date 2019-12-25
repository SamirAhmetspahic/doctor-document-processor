package si.better.dpp.service;

import si.better.dpp.model.Doctor;
import si.better.dpp.model.Participant;
import si.better.dpp.model.Patient;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
public interface ParticipantService<T extends Participant>
{
    T findById(long id);

    List<T> findAll();

    Optional<T> add(T participant);

    List<Optional<T>> addAll(List<T> participantCollection);
}
