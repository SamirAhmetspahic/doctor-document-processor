package si.better.dpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.better.dpp.model.Participant;

/**
 * @author ahmetspahics
 */
@Repository
public interface ParticipantRepository<T extends Participant> extends JpaRepository<T, Long>
{
}
