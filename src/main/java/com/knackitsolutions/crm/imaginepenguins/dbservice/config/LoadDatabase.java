package com.knackitsolutions.crm.imaginepenguins.dbservice.config;

import com.google.common.collect.ImmutableList;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.PrivilegeCode;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.EmployeeService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentServiceImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private static List<Institute> institutes = new ArrayList<>();

    private static List<InstituteClass> instituteClasss = new ArrayList<>();

    private static List<InstituteClassSection> instituteClassSections = new ArrayList<>();

    private static List<InstituteDepartment> departments = new ArrayList<>();

    private static List<Privilege> privileges = new ArrayList<>();

    private static List<Employee> employees = new ArrayList<>(10);

    private static List<Teacher> teachers = new ArrayList<>(10);

    private static List<Student> students = new ArrayList<>();

    private static List<UserDepartment> userDepartments = new ArrayList<>();

    private static List<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = new ArrayList<>();

    private static List<UserPrivilege> userPrivileges = new ArrayList<>();

    private static List<Subject> subjects = new ArrayList<>();

    private static List<InstituteClassSectionSubject> instituteClassSectionSubjects = new ArrayList<>();

    public static void setupInstitutes(InstituteRepository instituteRepository) {
        Institute institute1 = new Institute(4, "XYC", InstituteType.SCHOOL
                , new Address("D45", "Raod-3", "Delhi", "India", "110034")
                , new Contact("982", "xyz772@g.com"));
        Institute institute2 = new Institute(5, "ABC", InstituteType.SCHOOL
                , new Address("D45", "Raod-4", "Delhi", "India", "110034")
                , new Contact("98999", "ABC@g.com"));

        institute1 = instituteRepository.save(institute1);
        institute2 = instituteRepository.save(institute2);
        institutes.add(institute1);
        institutes.add(institute2);
    }

    public static void setupEmployees(EmployeeService employeeService) {
        Institute institute1 = institutes.get(0);
        Institute institute2 = institutes.get(1);
        Employee employee1 = new Employee(1l, "gautam", "gautam003", UserType.EMPLOYEE
                , false, false
                , new UserProfile("Gautam", "Kumar"
                , new Address("D74", "Gali16", "Delhi", "India", "110053")
                , new Contact("1010", "gautam@gmail.com")), EmployeeType.NON_TEACHER
                , "None", institute1, null, null);

        institute1.addEmployee(employee1);

        Employee employee2 = new Employee(1l, "gaurav", "gaurav003", UserType.EMPLOYEE
                , false, false
                , new UserProfile("Gaurav", "Kumar"
                , new Address("D14", "Gali16", "Delhi", "India", "110053")
                , new Contact("10111", "gaurav@gmail.com")), EmployeeType.NON_TEACHER
                , "None", institute1, null, null);
        institute1.addEmployee(employee2);

        employees.addAll(Stream.of(employee1, employee2)
                .map(employee -> {
                    employee.getUserProfile().setUser(employee);
                    return employeeService.newEmployee(employee);
                }).collect(Collectors.toList()));
    }

    public static void setupTeachers(TeacherServiceImpl teacherServiceImpl) {
        Institute institute1 = institutes.get(0);
        Institute institute2 = institutes.get(1);
        Teacher teacher1 = new Teacher(1l, "manish", "manish003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                new UserProfile("asdf", "dgsh",
                        new Address("d", "74", "Delhi", "India", "110043"),
                        new Contact("9000", "mj01@gmail.com")), EmployeeType.TEACHER, "HOD");
        Teacher teacher2 = new Teacher(1l, "nisha", "nisha003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                new UserProfile("t2", "t2",
                        new Address("d", "22", "Delhi", "India", "110043"),
                        new Contact("888", "nisha1@gmail.com")), EmployeeType.TEACHER, "CI");
        institute1.addEmployee(teacher1);
        institute1.addEmployee(teacher2);

        teacher1.getUserProfile().setUser(teacher1);
        teacher2.getUserProfile().setUser(teacher2);

        teacher1 = teacherServiceImpl.newTeacher(teacher1);
        teacher2 = teacherServiceImpl.newTeacher(teacher2);

        teachers.addAll(ImmutableList.of(teacher1, teacher2));
//        Stream.of(teacher1, teacher2).map(teacherServiceImpl::newTeacher).collect(Collectors.toCollection(() -> teachers));

    }

    public static void setupClasses(ClassRepository classRepository
            , InstituteClassRepository instituteClassRepository) {
        Institute institute1 = institutes.get(0);
        Institute institute2 = institutes.get(1);

        Class class1 = classRepository.save(new Class(1l, "8", InstituteType.SCHOOL));
        Class class2 = classRepository.save(new Class(1l, "9", InstituteType.SCHOOL));

        InstituteClass instituteClass1 = new InstituteClass(1l, institute1, class1);
        InstituteClass instituteClass2 = new InstituteClass(1l, institute1, class2);

        institute1.addClasses(instituteClass1);
        institute1.addClasses(instituteClass2);

        class1.addInstituteClass(instituteClass1);
        class2.addInstituteClass(instituteClass2);

        instituteClass1 = instituteClassRepository.save(instituteClass1);
        instituteClass2 = instituteClassRepository.save(instituteClass2);

        instituteClasss.add(instituteClass1);
        instituteClasss.add(instituteClass2);
    }

    public static void setupSections(SectionRepository sectionRepository
            , InstituteClassSectionRepository instituteClassSectionRepository) {
        InstituteClass instituteClass1 = instituteClasss.get(0);
        InstituteClass instituteClass2 = instituteClasss.get(1);

        Section section1 = new Section(1l, "A", InstituteType.SCHOOL);
        Section section2 = new Section(1l, "B", InstituteType.SCHOOL);
        Section section3 = new Section(1l, "C", InstituteType.SCHOOL);

        section1 = sectionRepository.save(section1);
        section2 = sectionRepository.save(section2);
        section3 = sectionRepository.save(section3);

        InstituteClassSection instituteClassSection1 = new InstituteClassSection(1l, instituteClass1, section1);
        InstituteClassSection instituteClassSection2 = new InstituteClassSection(1l, instituteClass1, section2);
        InstituteClassSection instituteClassSection3 = new InstituteClassSection(1l, instituteClass1, section3);
        InstituteClassSection instituteClassSection4 = new InstituteClassSection(1l, instituteClass2, section1);
        InstituteClassSection instituteClassSection5 = new InstituteClassSection(1l, instituteClass2, section2);

        section1.addInstituteClassSection(instituteClassSection1);
        section1.addInstituteClassSection(instituteClassSection4);
        section2.addInstituteClassSection(instituteClassSection2);
        section2.addInstituteClassSection(instituteClassSection5);
        section3.addInstituteClassSection(instituteClassSection3);

        instituteClassSections.addAll(Stream.of(instituteClassSection1, instituteClassSection2
                , instituteClassSection3, instituteClassSection4, instituteClassSection5)
                .map(instituteClassSectionRepository::save)
                .collect(Collectors.toList()));

    }

    public static void setupStudents(StudentServiceImpl studentServiceImpl) {
        InstituteClassSection instituteClassSection1 = instituteClassSections.get(0);
        InstituteClassSection instituteClassSection2 = instituteClassSections.get(1);

        Student student1 = new Student(1l, "naman", "naman003", UserType.STUDENT, false, false,
                new UserProfile("Naman", "Kumar"
                        , new Address("D74", "Gali-16", "Telangana", "India", "140088")
                        , new Contact("9003321", "naman@gmail.com")), instituteClassSection1, null);
        student1.setRollNumber("1");
        instituteClassSection1.addStudent(student1);

        Student student2 = new Student(1l, "manan", "manan003", UserType.STUDENT, false, false,
                new UserProfile("Manan", "Kumar"
                        , new Address("D34", "Gali-26", "Telangana", "India", "140088")
                        , new Contact("8003312", "manan@gmail.com")), instituteClassSection1, null);
        student2.setRollNumber("2");
        instituteClassSection1.addStudent(student2);

        Student student3 = new Student(1l, "kishor", "kishor003", UserType.STUDENT, false, false,
                new UserProfile("Kishor", "Kumar"
                        , new Address("D75", "Gali-16", "Telangana", "India", "140088")
                        , new Contact("9000011", "kishor@gmail.com")), instituteClassSection1, null);
        student3.setRollNumber("3");
        instituteClassSection1.addStudent(student3);

        Student student4 = new Student(1l, "hiteshi", "hiteshi003", UserType.STUDENT, false, false,
                new UserProfile("Hiteshi", "Kumar"
                        , new Address("D54", "Gali-46", "Telangana", "India", "140088")
                        , new Contact("90000118", "hiteshi@gmail.com")), instituteClassSection2, null);
        student4.setRollNumber("4");
        instituteClassSection2.addStudent(student4);

        Student student5 = new Student(1l, "pooja", "pooja003", UserType.STUDENT, false, false,
                new UserProfile("Pooja", "Kumar"
                        , new Address("D79", "Gali-86", "Telangana", "India", "140088")
                        , new Contact("9009119", "pooja@gmail.com")), instituteClassSection2, null);
        student5.setRollNumber("5");
        instituteClassSection2.addStudent(student5);

        Student student6 = new Student(1l, "neetu", "neetu003", UserType.STUDENT, false, false,
                new UserProfile("Neetu", "Kumar"
                        , new Address("D740", "Gali-916", "Telangana", "India", "140088")
                        , new Contact("881018", "neetu@gmail.com")), instituteClassSection2, null);
        student6.setRollNumber("6");
        instituteClassSection2.addStudent(student6);

        students.addAll(Stream.of(student1, student2, student3, student4, student5, student6)
                .map(student -> {
                    student.getUserProfile().setUser(student);
                    return studentServiceImpl.newStudent(student);
                }).collect(Collectors.toList()));

    }

    public static void setupSubjects(SubjectRepository subjectRepository) {
        Subject subject1 = new Subject(1l, "Maths");
        Subject subject2 = new Subject(1l, "English");
        Subject subject3 = new Subject(1l, "Physics");
        Subject subject4 = new Subject(1l, "SocialScience");
        Stream.of(subject1, subject2, subject3, subject4)
                .map(subject -> subjectRepository.save(subject))
                .collect(Collectors.toCollection(() -> subjects));
        subjects.forEach(subject -> log.debug("Subject : {}", subject));
    }

    public static void setupClassTeacher(TeacherRepository teacherRepository
            , InstituteClassSectionRepository classSectionRepository){
        List<Teacher> teacher = teacherRepository.findAll();
        List<InstituteClassSection> instituteClassSections = classSectionRepository.findAll();

        teacher.get(0).setInstituteClassSections(instituteClassSections.get(0));
        teacher.get(1).setInstituteClassSections(instituteClassSections.get(1));
        classSectionRepository.save(instituteClassSections.get(0));
        classSectionRepository.save(instituteClassSections.get(1));
    }

    public static void setupTeacherClassesAndSubjects(InstituteClassSectionSubjectRepository classSectionSubjectRepository
            , SubjectRepository subjectRepository, TeacherRepository teacherRepository) {

        List<Teacher> teachers = teacherRepository.findAll();

        InstituteClassSectionSubject isubject1 = new InstituteClassSectionSubject();
        subjectRepository.findAll().forEach(subject -> log.info("Subject: {}", subject));

        setAttributesToClassSectionSubject(isubject1, teachers.get(0)
                , instituteClassSections.get(0), 1l, subjectRepository.findByName("Maths"));

        InstituteClassSectionSubject isubject2 = new InstituteClassSectionSubject();
        setAttributesToClassSectionSubject(isubject2, teachers.get(0)
                , instituteClassSections.get(0), 1l, subjectRepository.findByName("Physics"));

        InstituteClassSectionSubject isubject3 = new InstituteClassSectionSubject();
        setAttributesToClassSectionSubject(isubject3, teachers.get(1)
                , instituteClassSections.get(0), 1l, subjectRepository.findByName("English"));

        InstituteClassSectionSubject isubject4 = new InstituteClassSectionSubject();
        setAttributesToClassSectionSubject(isubject4, teachers.get(1)
                , instituteClassSections.get(1), 1l, subjectRepository.findByName("English"));

        InstituteClassSectionSubject isubject5 = new InstituteClassSectionSubject();
        setAttributesToClassSectionSubject(isubject5, teachers.get(0)
                , instituteClassSections.get(1), 1l, subjectRepository.findByName("Maths"));

        InstituteClassSectionSubject isubject6 = new InstituteClassSectionSubject();
        setAttributesToClassSectionSubject(isubject6, teachers.get(1)
                , instituteClassSections.get(1), 1l, subjectRepository.findByName("SocialScience"));

        Stream.of(isubject1, isubject2, isubject3, isubject4, isubject5, isubject6)
                .map(isubject -> classSectionSubjectRepository.save(isubject))
                .collect(Collectors.toCollection(() -> instituteClassSectionSubjects));

        instituteClassSectionSubjects.forEach(classSubject -> log.info("InstituteClassSectionSubjects: {}", classSubject));

        teacherRepository.findAll().forEach(teacher -> log.info("Teacher : {}", teacher));

    }

    private static void setAttributesToClassSectionSubject(InstituteClassSectionSubject classSectionSubject, Teacher teacher
            , InstituteClassSection classSection, Long id, Optional<Subject> subject) {
        classSectionSubject.setId(id);
        classSectionSubject.setTeacher(teacher);
        teacher.setInstituteClassSectionSubjects(classSectionSubject);
        classSectionSubject.setInstituteClassSection(classSection);
        classSection.setInstituteClassSectionSubjects(classSectionSubject);
        subject
                .orElseThrow(() -> new Subject.SubjectNotFoundException("Subject Not Found"))
                .setClassSubjects(classSectionSubject);

    }

    private static void setupDepartment(DepartmentRepository departmentRepository) {
        InstituteDepartment department1 = new InstituteDepartment(1l, "ADMIN");
        InstituteDepartment department2 = new InstituteDepartment(1l, "STUDENT");
        InstituteDepartment department3 = new InstituteDepartment(1l, "TEACHER");
        InstituteDepartment department4 = new InstituteDepartment(1l, "PARENT");
        departments.addAll(Stream.of(department1, department2
                , department3, department4)
                .map(department -> {
                    institutes.get(0).addInstituteDepartments(department);
                    return departmentRepository.save(department);
                }).collect(Collectors.toList()));
    }

    private static void setupPrivileges(PrivilegeRepository privilegeRepository) {
        Privilege privilege1 = new Privilege(1, PrivilegeCode.ATTENDANCE
                , "Attendance", "Attendance Module");
        Privilege privilege2 = new Privilege(1, PrivilegeCode.CALENDAR
                , "Calendar", "Calendar Module");
        Privilege privilege3 = new Privilege(1, PrivilegeCode.VIEW_SELF_ATTENDANCE
                , "View Self Attendance", "View Self Attendance");
        Privilege privilege4 = new Privilege(1, PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE
                , "Mark Employee Attendance", "Mark Employee Attendance");
        Privilege privilege5 = new Privilege(1, PrivilegeCode.MARK_STUDENT_ATTENDANCE
                , "Mark Student Attendance", "Mark Student Attendance");

        privileges.addAll(Stream.of(
                privilege1, privilege2, privilege3, privilege4, privilege5
        ).map(privilegeRepository::save).collect(Collectors.toList()));

    }

    private static void setupInstituteDepartmentPrivileges(InstituteDepartmentPrivilegeRepository repository) {
        //admin department privilege
        instituteDepartmentPrivileges.addAll(privileges.stream().map(privilege -> {
            InstituteDepartmentPrivilege departmentPrivilege = new InstituteDepartmentPrivilege(departments.get(0), privilege);
            return repository.save(departmentPrivilege);
        }).collect(Collectors.toList()));

        //student department privilege
        InstituteDepartmentPrivilege instituteDepartmentPrivilege1 =
                new InstituteDepartmentPrivilege(departments.get(1), privileges.get(2));
        departments.get(1).setPrivileges(instituteDepartmentPrivilege1);
        privileges.get(2).addInstituteDepartmentPrivilege(instituteDepartmentPrivilege1);

        //teacher department privilege
        InstituteDepartmentPrivilege instituteDepartmentPrivilege2 =
                new InstituteDepartmentPrivilege(departments.get(2), privileges.get(4));

        departments.get(2).setPrivileges(instituteDepartmentPrivilege2);
        privileges.get(4).addInstituteDepartmentPrivilege(instituteDepartmentPrivilege2);

        //parent department privilege
        InstituteDepartmentPrivilege instituteDepartmentPrivilege3 =
                new InstituteDepartmentPrivilege(departments.get(3), privileges.get(2));

        departments.get(3).setPrivileges(instituteDepartmentPrivilege3);
        privileges.get(2).addInstituteDepartmentPrivilege(instituteDepartmentPrivilege3);

        instituteDepartmentPrivileges.addAll(departments.stream().map(department -> {
            InstituteDepartmentPrivilege privilege = new InstituteDepartmentPrivilege(department, privileges.get(1));
            department.setPrivileges(privilege);
            privileges.get(1).addInstituteDepartmentPrivilege(privilege);
            repository.save(privilege);
            return privilege;
        }).collect(Collectors.toList()));

        instituteDepartmentPrivileges.addAll(Stream.of(instituteDepartmentPrivilege1, instituteDepartmentPrivilege2
                , instituteDepartmentPrivilege3).map(repository::save).collect(Collectors.toList()));
    }

    private static void setupUserDepartment(UserDepartmentRepository userDepartmentRepository) {
        UserDepartment userDepartment1 = new UserDepartment(employees.get(0), departments.get(0));
        employees.get(0).setUserDepartment(userDepartment1);
        departments.get(0).addUserDepartment(userDepartment1);
        UserDepartment userDepartment2 = new UserDepartment(employees.get(1), departments.get(0));
        employees.get(1).setUserDepartment(userDepartment2);
        departments.get(0).addUserDepartment(userDepartment2);

        //Teacher teacher department
        UserDepartment userDepartment3 = new UserDepartment(teachers.get(0), departments.get(2));
        teachers.get(0).setUserDepartment(userDepartment3);
        departments.get(2).addUserDepartment(userDepartment3);

        //Teacher admin department
        UserDepartment userDepartment4 = new UserDepartment(teachers.get(0), departments.get(0));
        teachers.get(0).setUserDepartment(userDepartment4);
        departments.get(0).addUserDepartment(userDepartment4);

        //Teacher teacher department
        UserDepartment userDepartment5 = new UserDepartment(teachers.get(1), departments.get(2));
        teachers.get(1).setUserDepartment(userDepartment5);
        departments.get(2).addUserDepartment(userDepartment5);

        userDepartments.addAll(Stream.of(userDepartment1, userDepartment2, userDepartment3, userDepartment4, userDepartment5)
                .map(userDepartmentRepository::save).collect(Collectors.toList()));

        //students
        userDepartments.addAll(students.stream().map(student -> {
            UserDepartment department = new UserDepartment(student, departments.get(1));
            student.setUserDepartment(department);
            departments.get(1).addUserDepartment(department);
            return userDepartmentRepository.save(department);
        }).collect(Collectors.toList()));

    }

    private static void setupUserPrivileges(UserPrivilegeRepository repository, UserRepository userRepository
            , InstituteDepartmentPrivilegeRepository instituteDepartmentPrivilegeRepository) {
        List<User> users = userRepository.findAll();
//		users.forEach(user -> log.info("user: {}", user));
        log.info("Fill User privileges for employees");
        users.stream().filter(user -> user.getUserType() == UserType.EMPLOYEE).forEach(user -> {
            log.info("User: {}", user);
            user.getUserDepartments()
                    .forEach(userDepartment -> {
                        userPrivileges.addAll(
                                instituteDepartmentPrivilegeRepository
                                        .findByInstituteDepartmentId(userDepartment.getInstituteDepartment().getId())
                                        .stream()
                                        .map(instituteDepartmentPrivilege -> {
                                            UserPrivilege userPrivilege = new UserPrivilege();
                                            instituteDepartmentPrivilege.setPrivileges(userPrivilege);
                                            user.setUserPrivileges(userPrivilege);
                                            return repository.save(userPrivilege);
                                        }).collect(Collectors.toList()));
                    });
        });
        log.info("Fill User privileges for Students");
        users.stream().filter(user -> user.getUserType() == UserType.STUDENT).forEach(user -> {
            log.info("User: {}", user);
            user.getUserDepartments()
                    .forEach(userDepartment -> {
                        userPrivileges.addAll(
                                instituteDepartmentPrivilegeRepository
                                        .findByInstituteDepartmentId(userDepartment.getInstituteDepartment().getId())
                                        .stream()
                                        .map(instituteDepartmentPrivilege -> {
                                            UserPrivilege userPrivilege = new UserPrivilege();
                                            instituteDepartmentPrivilege.setPrivileges(userPrivilege);
                                            user.setUserPrivileges(userPrivilege);
                                            return repository.save(userPrivilege);
                                        }).collect(Collectors.toList()));
                    });
        });

        repository.findAll().forEach(userPrivilege -> log.info("User Privileges: {}", userPrivilege));

    }

//    public void assignInstituteTeacher(Teacher teacher, Institute institute){
//        teacher.setInstitute(institute);
//        institute.setEmployee(teacher);
//    }

    @Bean
    CommandLineRunner initDatabase(@Autowired InstituteRepository instituteRepository
            , @Autowired EmployeeService employeeService, @Autowired TeacherServiceImpl teacherServiceImpl
            , @Autowired ClassRepository classRepository, @Autowired SectionRepository sectionRepository
            , @Autowired InstituteClassRepository instituteClassRepository
            , @Autowired InstituteClassSectionRepository instituteClassSectionRepository
            , @Autowired StudentServiceImpl studentServiceImpl
            , @Autowired DepartmentRepository departmentRepository
            , @Autowired PrivilegeRepository privilegeRepository
            , @Autowired InstituteDepartmentPrivilegeRepository instituteDepartmentPrivilegeRepository
            , @Autowired UserDepartmentRepository userDepartmentRepository
            , @Autowired UserPrivilegeRepository userPrivilegeRepository
            , @Autowired UserRepository userRepository
            , @Autowired InstituteClassSectionSubjectRepository classSectionSubjectRepository
            , @Autowired SubjectRepository subjectRepository
            , @Autowired TeacherRepository teacherRepository) {

        return args -> {
            log.info("Loading Database");
            setupInstitutes(instituteRepository);
            setupClasses(classRepository, instituteClassRepository);
            setupSections(sectionRepository, instituteClassSectionRepository);
            setupEmployees(employeeService);
            setupTeachers(teacherServiceImpl);
            setupStudents(studentServiceImpl);
            setupClassTeacher(teacherRepository, instituteClassSectionRepository);
            setupSubjects(subjectRepository);
            setupTeacherClassesAndSubjects(classSectionSubjectRepository, subjectRepository, teacherRepository);
            setupDepartment(departmentRepository);
            setupPrivileges(privilegeRepository);
            setupInstituteDepartmentPrivileges(instituteDepartmentPrivilegeRepository);
            setupUserDepartment(userDepartmentRepository);
            setupUserPrivileges(userPrivilegeRepository, userRepository, instituteDepartmentPrivilegeRepository);
            log.info("Finished Loading Database");
        };

    }
}