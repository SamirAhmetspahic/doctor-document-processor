package si.better.ddp.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ahmetspahics
 */
@Converter
@Slf4j
public class String2MapConverter implements AttributeConverter<Map<Long, String>, String>
{
    @Override
    public String convertToDatabaseColumn(Map<Long, String> longStringMap)
    {
        if(longStringMap != null) {
            return JSONValue.toJSONString(longStringMap);
        } else {
            return null;
        }
    }

    @Override
    public Map<Long, String> convertToEntityAttribute(String s)
    {
        HashMap<Long, String> map = null;
        ObjectReader reader = new ObjectMapper().readerFor(Map.class);
        try {
            map = reader.readValue(s);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return map;
    }
}
