/*
package si.better.ddp.batch.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import si.better.ddp.batch.processor.DoctorDto2DoctorModelProcessor;
import si.better.ddp.batch.writer.ModelWriter;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.validator.InputFileValidator;

*/
/**
 * @author ahmetspahics
 *//*

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfigurator
{
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ModelWriter writer;

    @Autowired
    private DoctorDto2DoctorModelProcessor processor;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private InputFileValidator inputFileValidator;

    */
/**
     * path - Doctor XML file location
     *//*

    @Value("#{jobParameters['PATH']}")
    private String pathIn;

    @Bean
    public Job job()
    {
        return jobBuilderFactory.get("DOCTOR-JOB")
            .incrementer(new RunIdIncrementer())
            .start(step())
            .build();
    }

    @Bean
    public Step step()
    {
        return stepBuilderFactory
            .get("DOCTOR-STEP")
            .listener(promotionListener())
            .listener(inputFileValidator)
            .<DoctorDto, Doctor>chunk(1)
            .reader(xmlFileItemReader())
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener()
    {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
        listener.setKeys(new String[]{"doctor-reader", "doctor-processor", "doctor-writer"});
        listener.setStrict(true);
        return listener;
    }

    @Bean
    ItemReader<DoctorDto> xmlFileItemReader()
    {
        StaxEventItemReader<DoctorDto> xmlFileReader = new StaxEventItemReader<>();
        xmlFileReader.setResource(new FileSystemResource(pathIn));
        xmlFileReader.setFragmentRootElementName("doctor");

        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[]{DoctorDto.class});

        xmlFileReader.setUnmarshaller(marshaller);

        return xmlFileReader;
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry)
    {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }
}
*/
