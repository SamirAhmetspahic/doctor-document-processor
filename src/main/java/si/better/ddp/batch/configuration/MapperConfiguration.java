package si.better.ddp.batch.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import si.better.ddp.dto.DoctorDto;
import si.better.ddp.dto.PatientDto;
import si.better.ddp.model.participant.Doctor;
import si.better.ddp.model.participant.Patient;

import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author ahmetspahics
 */
@Configuration
public class MapperConfiguration
{
    /**
     * Register configured mappings to ModelMapper.
     */
/*    @PostConstruct
    public void registerTypeMaps()
    {
        configurePatientDto2PatientModel();
        configureDoctorDto2DoctorModelMap();
    }*/

    /**
     * modelMapper - singleton skope.
     */
    @Bean
    //@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ModelMapper modelMapper()
    {
        ModelMapper mapper = new ModelMapper();
        return mapper;
    }

    /**
     * DoctorDto to Doctor specific mapping configuration
     */
    public void configureDoctorDto2DoctorModelMap()
    {
        GenericPropertyMap<DoctorDto, Doctor> doctorDto2DoctorModelMap = new GenericPropertyMap<>(typeMap ->
        {
            //external id mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getId(), Doctor::setId));

            // patient collection mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getPatients().getPatient()
                        .stream()
                        .map(patientDto -> modelMapper().map(patientDto, Patient.class))
                        .collect(Collectors.toList()), Doctor::setPatients));

            //department mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getDepartment(), Doctor::setDepartment));

        }, modelMapper(), DoctorDto.class, Doctor.class);
    }

    /**
     * PatientDto to Patient specific mapping configuration
     */
    public void configurePatientDto2PatientModel()
    {
        GenericPropertyMap<PatientDto, Patient> patientDto2PatientModelMap = new GenericPropertyMap<>(typeMap ->
        {
            //external id mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getId(), Patient::setId));

            //name mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getName(), Patient::setName));

            //last name mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getLastName(), Patient::setLastName));

            //diseases collection mapping
            typeMap.addMappings(mapper ->
                mapper.map(src ->
                    src.getDiseases().getDisease(), Patient::setDiseasesCollection));
        }, modelMapper(), PatientDto.class, Patient.class);
    }


    public static class GenericPropertyMap<T, D>
    {
        private final Class<T> source;
        private final Class<D> destination;
        private final Consumer<TypeMap<T, D>> consumer;
        private final ModelMapper modelMapper;

        public GenericPropertyMap(final Consumer<TypeMap<T, D>> consumer, final ModelMapper modelMapper, final Class<T> source, final Class<D> destination)
        {
            this.destination = destination;
            this.source = source;
            this.consumer = consumer;
            this.modelMapper = modelMapper;

            createTypeMap();
        }

        public TypeMap<T, D> createTypeMap()
        {
            //Create @TypeMap using @ModelMapper : singleton
            TypeMap<T, D> typeMap = modelMapper.getTypeMap(source, destination);

            //Check if typeMap exist. If not, create a now one.
            if (typeMap == null) {
                modelMapper.createTypeMap(source, destination);
            }

            return typeMap;
        }

        public void addModelMapperConfiguration(TypeMap<T, D> typeMap)
        {
            consumer.accept(typeMap);
        }
    }
}
