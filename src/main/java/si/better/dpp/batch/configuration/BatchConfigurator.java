package si.better.dpp.batch.configuration;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import si.better.dpp.dto.DoctorDTO;

/**
 * @author ahmetspahics
 */
@Configuration
public class BatchConfigurator
{
    /**
     * path - Doctor XML file location
     */
    @Value("${file.location}")
    private String path;

    @Bean
    ItemReader<DoctorDTO> xmlFileItemReader()
    {
        StaxEventItemReader<DoctorDTO> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setResource(new FileSystemResource(path));
        xmlFileReader.setFragmentRootElementName("doctor");

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[]{DoctorDTO.class});
        xmlFileReader.setUnmarshaller(marshaller);

        return xmlFileReader;
    }
}
