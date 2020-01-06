package si.better.ddp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import si.better.ddp.model.report.DocumentReport;
import si.better.ddp.repository.DocumentRepository;
import si.better.ddp.service.DocumentService;

import java.util.List;
import java.util.Optional;

/**
 * @author ahmetspahics
 */
@Service
public class DefaultDocumentService implements DocumentService
{
    private DocumentRepository documentRepository;

    /**
     * @param documentRepository - Constructor injection
     */
    @Autowired
    public DefaultDocumentService(final DocumentRepository documentRepository)
    {
        this.documentRepository = documentRepository;
    }

    @Override
    public Optional<DocumentReport> findById(long id)
    {
        return documentRepository.findById(id);
    }

    @Override
    public List<DocumentReport> findAll()
    {
        return documentRepository.findAll();
    }

    @Override
    public void save(DocumentReport documentReport)
    {
        documentRepository.save(documentReport);
    }
}
