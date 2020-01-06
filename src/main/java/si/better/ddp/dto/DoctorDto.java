package si.better.ddp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author ahmetspahics
 */
@XmlRootElement(name = "doctor")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "id",
    "patients",
    "department"
})
@JsonRootName("doctor")
@Data
@NoArgsConstructor
public class DoctorDto
{
    /**
     * id
     */
    @XmlAttribute(required = true)
    @JsonProperty("_id")
    private String id;

    /**
     * patients - patients collection wraper
     */
    @XmlElement(name = "patients", required = true)
    @JsonProperty("patients")
    private DoctorDto.Patients patients;

    /**
     * department - doctor department
     */
    @XmlAttribute
    @JsonProperty("_department")
    private String department;

    /**
     * Patients nested class
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {"patient"})
    @Data
    @NoArgsConstructor
    public static class Patients
    {
        @XmlElement(name = "patient")
        @JsonProperty("patient_list")
        private List<PatientDto> patient;
    }
}
