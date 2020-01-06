package si.better.ddp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.better.ddp.model.participant.Participant;

import java.util.Map;

/**
 * @author ahmetspahics
 */
@Repository
public interface ParticipantRepository<T extends Participant> extends JpaRepository<T, Long>
{
    @Query("SELECT p FROM Participant p WHERE p.mapId = ?1")
    T findByExternalId(Map<Long, String> idMap);
}
