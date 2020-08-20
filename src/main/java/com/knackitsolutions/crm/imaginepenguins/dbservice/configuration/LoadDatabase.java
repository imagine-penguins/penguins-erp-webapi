package com.knackitsolutions.crm.imaginepenguins.dbservice.configuration;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.InstituteRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.PrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserPrivilegeRepository;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private List<Institute> institutes = Stream.of(
            new Institute(1,"MBD Schood", InstituteType.SCHOOL,
                new Address("D-74","Delhi", "India", "110088"),
                new Contact("970198", "mj22@gmail.com")),
            new Institute(2, "Saraswati", InstituteType.SCHOOL,
                    new Address("D-74","Delhi", "India", "110088"),
                    new Contact("970193", "mj23@gmail.com")),
            new Institute(3,"IITM College", InstituteType.COLLEGE,
                    new Address("D-74","Delhi", "India", "110088"),
                    new Contact("970194", "mj24@gmail.com")))
            .collect(Collectors.toList());

    private List<User> users = Stream.of(
            new User(1l, "ankit", "ankit003",UserType.EMPLOYEE, true, true, null),
            new User(2l, "amit", "amit003", UserType.EMPLOYEE, true, false, null),
            new User(3l, "raj", "raj003", UserType.EMPLOYEE, false, false, null),
            new User(4l, "gautam", "gautam003", UserType.EMPLOYEE, false, true, null),
            new User(6l, "mayank", "kill", UserType.STUDENT, true, true, null)
    ).collect(Collectors.toList());

    private List<Privilege> privileges = Stream.of(
            new Privilege("Attendence", "View/Edit Attendence"),
            new Privilege("Calendar", "View/Create events Calendar"),
            new Privilege("ReportCard", "View/Edit ReportCards"),
            new Privilege("AddUser","Add new user"),
            new Privilege("CreateAnnouncement","CreateAnnouncement")
    ).collect(Collectors.toList());

    @Bean
    CommandLineRunner initDatabase(UserPrivilegeRepository userPrivilegeRepository, InstituteRepository instituteRepository, UserRepository userRepository, PrivilegeRepository privilegeRepository){
        log.info("Initializing Database:");
        return args -> {
            log.info("Database Initialization Started");
            log.info("Institutes: {}", institutes);
            institutes.forEach(institute -> instituteRepository.save(institute));
            instituteRepository.findAll().forEach(institute -> log.info("Preloaded: {}",institute));

            List<Privilege> privileges = privilegeRepository.saveAll(this.privileges);

            log.info("Users: {}", users);
            List<User> users = userRepository.saveAll(this.users);
            List<UserPrivilege> userPrivileges = privileges.stream()
                    .map(privilege -> new UserPrivilege(users.get(0), privilege))
                    .collect(Collectors.toList());
            userPrivileges.add(new UserPrivilege(users.get(1), privileges.get(3)));
            userPrivileges.add(new UserPrivilege(users.get(1), privileges.get(2)));
            userPrivileges.add(new UserPrivilege(users.get(2), privileges.get(1)));
            userPrivileges.add(new UserPrivilege(users.get(2), privileges.get(0)));
            userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(4)));
            userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(1)));
            userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(2)));
            userPrivilegeRepository.saveAll(userPrivileges);
            userRepository.findAll().forEach(user -> log.info("Preloaded: {}", user));
            log.info("Database Initialization Finished");
        };
    }
}
