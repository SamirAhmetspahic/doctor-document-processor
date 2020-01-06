package si.better.ddp.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import si.better.ddp.repository.ParticipantRepository;
import si.better.ddp.service.ParticipantService;
import si.better.ddp.service.impl.DefaultParticipantService;

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

    @Bean
    public RestTemplate restTemplate()
    {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory()
    {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
            .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
            new HttpComponentsClientHttpRequestFactory(httpClient);
        return clientHttpRequestFactory;
    }
}
