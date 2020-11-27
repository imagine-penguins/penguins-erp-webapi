package com.knackitsolutions.crm.imaginepenguins.dbservice.common.filter;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataFilter {
    private Set<UserType> userTypes;
    private Set<Long> instituteDepartmentIds;
    private Set<PrivilegeCode> privilegeCodes;
    private Optional<Boolean> active;
}
