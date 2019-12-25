package si.better.dpp.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import si.better.dpp.repository.ParticipantRepository;
import si.better.dpp.service.ParticipantService;
import si.better.dpp.service.impl.DefaultParticipantService;

/**
 * @author ahmetspahics
 */
@TestConfiguration
public class TestAppConfiguration
{
    @Bean
    public ParticipantService participantService(ParticipantRepository repository)
    {
        return new DefaultParticipantService(repository);
    }
}
