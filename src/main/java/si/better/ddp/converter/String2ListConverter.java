package si.better.ddp.converter;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ahmetspahics
 */
@Converter
@Slf4j
public class String2ListConverter implements AttributeConverter<List<String>, String>
{
    @Override
    public String convertToDatabaseColumn(List<String> list)
    {
        if (list != null) {
            return String.join(",", list);
        }
        else {
            return null;
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String joined)
    {
        return new ArrayList<>(Arrays.asList(joined.split(",")));
    }
}
