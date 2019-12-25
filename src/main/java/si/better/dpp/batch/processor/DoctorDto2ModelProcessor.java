package si.better.dpp.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import si.better.dpp.dto.DoctorDTO;
import si.better.dpp.model.Doctor;

/**
 * @author ahmetspahics
 */
public class DoctorDto2ModelProcessor implements ItemProcessor<DoctorDTO, Doctor>
{
    @Override
    public Doctor process(DoctorDTO doctorDTO) throws Exception
    {
        return null;
    }
}
