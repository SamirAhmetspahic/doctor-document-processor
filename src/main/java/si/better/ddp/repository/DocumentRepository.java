package si.better.ddp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.better.ddp.model.report.DocumentReport;

/**
 * @author ahmetspahics
 */
@Repository
public interface DocumentRepository extends JpaRepository<DocumentReport, Long>
{
}
