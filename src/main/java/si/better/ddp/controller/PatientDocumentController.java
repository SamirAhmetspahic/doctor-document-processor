package si.better.ddp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.better.ddp.constant.ApplicationConstants;

/**
 * @author ahmetspahics
 */
@RestController
@RequestMapping(value = ApplicationConstants.Controller.ROOT_REQUEST_MAPPING
    + ApplicationConstants.Controller.API_VERSION
    + ApplicationConstants.Controller.PATIENT_REQUEST_BASE)
public class PatientDocumentController
{
}
