package si.better.ddp.service;

import org.springframework.stereotype.Service;
import si.better.ddp.model.report.DocumentReport;

import java.util.List;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
@Service
public interface DocumentService
{
    Optional<DocumentReport> findById(long id);

    List<DocumentReport> findAll();

    void save(DocumentReport documentReport);
}
