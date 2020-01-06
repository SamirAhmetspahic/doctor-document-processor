package si.better.ddp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author ahmetspahics
 */
@XmlRootElement(name = "patient")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "name",
    "lastName",
    "diseases"
})
@JsonRootName(value = "patient")
@Data
@NoArgsConstructor
public class PatientDto
{
    /**
     * id - Patient id (external id)
     */
    @XmlElement(name = "id")
    @JsonProperty("id")
    private String id;

    /**
     * name - patient name
     */
    @XmlElement(name = "first_name")
    @JsonProperty("first_name")
    private String name;

    /**
     * name - patient last name
     */
    @XmlElement(name = "last_name")
    @JsonProperty("last_name")
    private String lastName;

    /**
     * diseases - collection wraper
     */
    @XmlElement(name = "diseases")
    @JsonProperty("diseases")
    private Diseases diseases;

    /**
     * Diseases - nested class as disease string collection wraper.
     */
    @XmlAccessorType(value = XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"disease"})
    @Data
    @NoArgsConstructor
    @JsonRootName(value = "diseases")
    public static class Diseases
    {
        @XmlElement(name = "disease")
        @JsonProperty("disease_list")
        private List<String> disease;
    }
}

