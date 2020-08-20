package com.knackitsolutions.crm.imaginepenguins.dbservice.dto;

import javax.persistence.Id;
import java.util.List;

public class EmployeeUpdateDTO {
    @Id
    Long id;

    List<Integer> departments;


}
