package com.knackitsolutions.crm.imaginepenguins.dbservice.security.model;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class UserContext {
    private Long userId;
    private UserType userType;
    private String username;
    private List<GrantedAuthority> authorities;
    private Integer instituteId = 0;
    private Long instituteClassSectionId = 0l;

    private UserContext(Long userId, UserType userType, String username
            , List<GrantedAuthority> authorities, Integer instituteId, Long instituteClassSectionId) {
        this.userId = userId;
        this.userType = userType;
        this.username = username;
        this.authorities = new ArrayList<>(authorities);
        this.instituteId = instituteId;
        this.instituteClassSectionId = instituteClassSectionId;
    }

    public static UserContext create(Long userId, String username, UserType userType
            , Integer instituteId, Long instituteClassSectionId, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        if (userId == null || userId == 0) {
            throw new IllegalArgumentException("User Id cannot be null or Zero: " + userId);
        }
        if (userType.equals(UserType.EMPLOYEE) && instituteId == 0) {
            throw new IllegalArgumentException("There is no institute id for this employee. Please provide an employee id.");
        }
        if (userType.equals(UserType.STUDENT) && instituteClassSectionId == 0) {
            log.error("There is no institute class section id on this student please provide institute class section id.");
        }
        return new UserContext(userId, userType, username, authorities, instituteId, instituteClassSectionId);
    }

}
