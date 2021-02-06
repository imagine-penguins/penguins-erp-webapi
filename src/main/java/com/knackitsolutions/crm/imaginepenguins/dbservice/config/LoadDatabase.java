package com.knackitsolutions.crm.imaginepenguins.dbservice.config;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.LeaveRequest;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
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
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Profile("test")
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private static List<Institute> institutes = new ArrayList<>();

    private static List<InstituteClass> instituteClasss = new ArrayList<>();

    private static List<InstituteClassSection> instituteClassSections = new ArrayList<>();

    private static List<InstituteDepartment> departments = new ArrayList<>();

    private static List<Employee> employees = new ArrayList<>(10);

    private static List<Teacher> teachers = new ArrayList<>(10);

    private static List<Student> students = new ArrayList<>(10);

    private static List<UserDepartment> userDepartments = new ArrayList<>(10);

    private static List<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = new ArrayList<>(10);

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
                , Boolean.TRUE, false
                , new UserProfile("Gautam", "Kumar"
                , new Address("D74", "Gali16", "Delhi", "India", "110053")
                , new Contact("1010", "gautam@gmail.com")), EmployeeType.NON_TEACHER
                , "None", institute1, null, new HashSet<>());
        employee1.setVerified(true);
        employee1.setActive(true);
        employee1.getUserProfile().setUser(employee1);
        institute1.setEmployee(employee1);

        Employee employee2 = new Employee(1l, "gaurav", "gaurav003", UserType.EMPLOYEE
                , false, false
                , new UserProfile("Gaurav", "Kumar"
                , new Address("D14", "Gali16", "Delhi", "India", "110053")
                , new Contact("10111", "gaurav@gmail.com")), EmployeeType.NON_TEACHER
                , "None", institute1, null, new HashSet<>());
        institute1.setEmployee(employee2);

        employee2.setVerified(true);
        employee2.setActive(true);
        employee2.getUserProfile().setUser(employee2);

        employee1 = employeeService.newEmployee(employee1);
        employee2.setManager(employee1);
        employee1.setSubordinates(employee2);

        employee2 = employeeService.newEmployee(employee2);

        employees.addAll(Arrays.asList(employee1, employee2));
    }

    public static void setupTeachers(TeacherServiceImpl teacherServiceImpl) {
        Institute institute1 = institutes.get(0);
        Institute institute2 = institutes.get(1);
        Teacher teacher1 = new Teacher(1l, "manisha", "manisha003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                new UserProfile("Manisha", "Sharma",
                        new Address("d", "74", "Delhi", "India", "110043"),
                        new Contact("9000", "mj01@gmail.com")), EmployeeType.TEACHER, "HOD");
        teacher1.setVerified(true);
        teacher1.setActive(true);
        Teacher teacher2 = new Teacher(1l, "nishi", "nishi003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                new UserProfile("t2", "t2",
                        new Address("d", "22", "Delhi", "India", "110043"),
                        new Contact("888", "nisha1@gmail.com")), EmployeeType.TEACHER, "CI");


        teacher2.setVerified(true);
        teacher2.setActive(true);

        institute1.setEmployee(teacher1);
        institute1.setEmployee(teacher2);

        teacher1.getUserProfile().setUser(teacher1);
        teacher2.getUserProfile().setUser(teacher2);

        teacher1.setManager(employees.get(0));
        employees.get(0).setSubordinates(teacher1);
        teacher1 = teacherServiceImpl.newTeacher(teacher1);

        teacher2.setManager(teacher1);
        teacher1.setSubordinates(teacher2);

        teacher2 = teacherServiceImpl.newTeacher(teacher2);
        teachers.addAll(Arrays.asList(teacher1, teacher2));

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
        student1.setVerified(true);
        student1.setActive(true);
        instituteClassSection1.setStudent(student1);

        Student student2 = new Student(1l, "manan", "manan003", UserType.STUDENT, false, false,
                new UserProfile("Manan", "Kumar"
                        , new Address("D34", "Gali-26", "Telangana", "India", "140088")
                        , new Contact("8003312", "manan@gmail.com")), instituteClassSection1, null);
        student2.setRollNumber("2");
        student2.setVerified(true);
        student2.setActive(false);
        instituteClassSection1.setStudent(student2);

        Student student3 = new Student(1l, "kishor", "kishor003", UserType.STUDENT, false, false,
                new UserProfile("Kishor", "Kumar"
                        , new Address("D75", "Gali-16", "Telangana", "India", "140088")
                        , new Contact("9000011", "kishor@gmail.com")), instituteClassSection1, null);
        student3.setRollNumber("3");
        instituteClassSection1.setStudent(student3);

        Student student4 = new Student(1l, "hiteshi", "hiteshi003", UserType.STUDENT, false, false,
                new UserProfile("Hiteshi", "Kumar"
                        , new Address("D54", "Gali-46", "Telangana", "India", "140088")
                        , new Contact("90000118", "hiteshi@gmail.com")), instituteClassSection2, null);
        student4.setRollNumber("4");
        instituteClassSection2.setStudent(student4);

        Student student5 = new Student(1l, "pooja", "pooja003", UserType.STUDENT, false, false,
                new UserProfile("Pooja", "Kumar"
                        , new Address("D79", "Gali-86", "Telangana", "India", "140088")
                        , new Contact("9009119", "pooja@gmail.com")), instituteClassSection2, null);
        student5.setRollNumber("5");
        instituteClassSection2.setStudent(student5);

        Student student6 = new Student(1l, "neetu", "neetu003", UserType.STUDENT, false, false,
                new UserProfile("Neetu", "Kumar"
                        , new Address("D740", "Gali-916", "Telangana", "India", "140088")
                        , new Contact("881018", "neetu@gmail.com")), instituteClassSection2, null);
        student6.setRollNumber("6");
        instituteClassSection2.setStudent(student6);

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
        Privilege privilege1 = new Privilege(PrivilegeCode.ATTENDANCE
                , "Attendance", "Attendance Module");
        Privilege privilege2 = new Privilege(PrivilegeCode.CALENDAR
                , "Calendar", "Calendar Module");

        privilege1 = privilegeRepository.save(privilege1);
        privilege2 = privilegeRepository.save(privilege2);

        Privilege privilege3 = new Privilege(PrivilegeCode.VIEW_SELF_ATTENDANCE_HISTORY
                , "View Self Attendance", "View Self Attendance");
        Privilege privilege4 = new Privilege(PrivilegeCode.MARK_EMPLOYEE_ATTENDANCE
                , "Mark Employee Attendance", "Mark Employee Attendance");
        Privilege privilege5 = new Privilege(PrivilegeCode.MARK_STUDENT_ATTENDANCE
                , "Mark Student Attendance", "Mark Student Attendance");
//        Privilege privilege6 = new Privilege(null, PrivilegeCode.VIEW_STUDENTS_ATTENDANCE_HISTORY
//                , "View Students Attendance History", "View Students Attendance History");

        Privilege privilege7 = new Privilege(PrivilegeCode.EDIT_STUDENTS_ATTENDANCE_HISTORY
                , "Edit Students Attendance History", "Edit Students Attendance History");

        Privilege privilege8 = new Privilege(PrivilegeCode.EDIT_EMPLOYEE_ATTENDANCE_HISTORY
                , "Edit Employee Attendance History", "Edit Employee Attendance History");

        Privilege privilege9 = new Privilege(PrivilegeCode.APPLY_LEAVE_REQUEST
                , "Apply for leave request", "Apply for leave request.");

        Privilege privilege10 = new Privilege(PrivilegeCode.UPDATE_LEAVE_REQUEST
                , "Approve/Reject UserDTO Leave Request", "Approve/Reject UserDTO Leave Request");

        Privilege privilege11 = new Privilege(PrivilegeCode.MANAGE_USERS
                , "Manage Users", "Manage Users");

        Privilege privilege12 = new Privilege(PrivilegeCode.VIEW_LEAVE_REQUEST
                , PrivilegeCode.VIEW_LEAVE_REQUEST.getPrivilegeCode()
                , PrivilegeCode.VIEW_LEAVE_REQUEST.getPrivilegeCode());

        Privilege privilege13 = new Privilege(PrivilegeCode.VIEW_APPLIED_LEAVE_REQUEST
                , PrivilegeCode.VIEW_APPLIED_LEAVE_REQUEST.getPrivilegeCode()
                , PrivilegeCode.VIEW_APPLIED_LEAVE_REQUEST.getPrivilegeCode());

        Privilege privilege14 = new Privilege(PrivilegeCode.VIEW_RECEIVED_LEAVE_REQUEST
                , PrivilegeCode.VIEW_RECEIVED_LEAVE_REQUEST.getPrivilegeCode()
                , PrivilegeCode.VIEW_RECEIVED_LEAVE_REQUEST.getPrivilegeCode());

        Privilege privilege15 = new Privilege(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS
                , PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode()
                , PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS.getPrivilegeCode());

        Privilege privilege16 = new Privilege(PrivilegeCode.EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY
                , "Edit Class Students Attendance History", "Edit Class Students Attendance History");

        Privilege privilege17 = new Privilege(PrivilegeCode.MARK_SUBORDINATES_EMPLOYEE_ATTENDANCE
                , PrivilegeCode.MARK_SUBORDINATES_EMPLOYEE_ATTENDANCE.getPrivilegeCode()
                , PrivilegeCode.MARK_SUBORDINATES_EMPLOYEE_ATTENDANCE.getPrivilegeCode());

        Stream.of(privilege3, privilege4, privilege5, privilege7
                , privilege8, privilege9, privilege10)
                .forEach(privilege1::setPrivileges);

        Stream.of(
                privilege3, privilege4, privilege5, privilege7, privilege8, privilege9, privilege10, privilege11
                , privilege12, privilege13, privilege14, privilege15, privilege16, privilege17
        ).map(privilegeRepository::save).collect(Collectors.toList());

    }

    private static void setupInstituteDepartmentPrivileges(InstituteDepartmentPrivilegeRepository repository
            , PrivilegeRepository privilegeRepository) {
        InstituteDepartment adminDepartment = departments.get(0);
        InstituteDepartment teacherDepartment = departments.get(2);
        InstituteDepartment studentDepartment = departments.get(1);
        //admin department privilege
        instituteDepartmentPrivileges.addAll(
                privilegeRepository.findAll()
                        .stream()
                        .map(privilege -> new InstituteDepartmentPrivilege(adminDepartment, privilege))
                        .map(repository::save)
                        .collect(Collectors.toList())
        );

        //student department privilege
        InstituteDepartmentPrivilege instituteDepartmentPrivilege1 =
                new InstituteDepartmentPrivilege();
        studentDepartment.setPrivileges(instituteDepartmentPrivilege1);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.VIEW_SELF_ATTENDANCE_HISTORY)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege1);

//        InstituteDepartmentPrivilege instituteDepartmentPrivilege11 =
//                new InstituteDepartmentPrivilege(departments.get(1), privileges.get(6));
//        departments.get(1).setPrivileges(instituteDepartmentPrivilege11);
//        privileges.get(6).addInstituteDepartmentPrivilege(instituteDepartmentPrivilege11);

        //teacher department privilege
        InstituteDepartmentPrivilege instituteDepartmentPrivilege2 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege2);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.EDIT_CLASS_STUDENTS_ATTENDANCE_HISTORY)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege2);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege3 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege3);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.VIEW_SELF_ATTENDANCE_HISTORY)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege3);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege10 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege10);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.MARK_STUDENT_ATTENDANCE)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege10);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege4 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege4);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.UPDATE_LEAVE_REQUEST_STATUS)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege4);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege5 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege5);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.VIEW_APPLIED_LEAVE_REQUEST)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege5);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege6 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege6);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.UPDATE_LEAVE_REQUEST)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege6);

        InstituteDepartmentPrivilege instituteDepartmentPrivilege7 =
                new InstituteDepartmentPrivilege();
        teacherDepartment.setPrivileges(instituteDepartmentPrivilege7);
        privilegeRepository.findByPrivilegeCode(PrivilegeCode.VIEW_RECEIVED_LEAVE_REQUEST)
                .addInstituteDepartmentPrivilege(instituteDepartmentPrivilege7);

//        parent department privilege
//        InstituteDepartmentPrivilege instituteDepartmentPrivilege5 =
//                new InstituteDepartmentPrivilege(departments.get(3), privileges.get(2));
//
//        departments.get(3).setPrivileges(instituteDepartmentPrivilege3);
//        privileges.get(2).addInstituteDepartmentPrivilege(instituteDepartmentPrivilege3);

        Stream.of(
                instituteDepartmentPrivilege1
                , instituteDepartmentPrivilege2
                , instituteDepartmentPrivilege3
                , instituteDepartmentPrivilege4
                , instituteDepartmentPrivilege5
                , instituteDepartmentPrivilege6
                , instituteDepartmentPrivilege7
                , instituteDepartmentPrivilege10)
                .map(repository::save)
                .collect(Collectors.toList());
    }

    private static void setupUserDepartment(UserDepartmentRepository userDepartmentRepository
            , DepartmentRepository departmentRepository) {
        InstituteDepartment admin = departmentRepository.findByDepartmentName("ADMIN");
        InstituteDepartment teacher = departmentRepository.findByDepartmentName("TEACHER");
        InstituteDepartment student = departmentRepository.findByDepartmentName("STUDENT");

        UserDepartment userDepartment1 =
                new UserDepartment(employees.get(0), admin);
        employees.get(0).setUserDepartment(userDepartment1);
        admin.addUserDepartment(userDepartment1);

        UserDepartment userDepartment2 = new UserDepartment(employees.get(1), admin);
        employees.get(1).setUserDepartment(userDepartment2);
        admin.addUserDepartment(userDepartment2);

        //teacher department
        UserDepartment userDepartment3 = new UserDepartment(teachers.get(0), teacher);
        teachers.get(0).setUserDepartment(userDepartment3);
        teacher.addUserDepartment(userDepartment3);

        //admin department
        UserDepartment userDepartment4 = new UserDepartment(teachers.get(0), admin);
        teachers.get(0).setUserDepartment(userDepartment4);
        admin.addUserDepartment(userDepartment4);

        //teacher department
        UserDepartment userDepartment5 = new UserDepartment(teachers.get(1), teacher);
        teachers.get(1).setUserDepartment(userDepartment5);
        teacher.addUserDepartment(userDepartment5);

        userDepartments.addAll(Stream.of(userDepartment1, userDepartment2, userDepartment3
                , userDepartment4, userDepartment5)
                .map(userDepartmentRepository::save).collect(Collectors.toList()));

        //students
        userDepartments.addAll(students.stream().map(studentT -> {
            UserDepartment department = new UserDepartment(studentT, student);
            studentT.setUserDepartment(department);
            student.addUserDepartment(department);
            return userDepartmentRepository.save(department);
        }).collect(Collectors.toList()));
    }

    private static void setupUserPrivileges(UserPrivilegeRepository repository
            , UserRepository userRepository
            , InstituteDepartmentPrivilegeRepository instituteDepartmentPrivilegeRepository) {
        List<User> users = userRepository.findAll();
//		users.forEach(user -> log.info("user: {}", user));
        log.info("Fill UserDTO privileges for employees");
        users.stream().filter(user -> user.getUserType() == UserType.EMPLOYEE).forEach(user -> {
            log.info("UserDTO: {}", user);
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
        log.info("Fill UserDTO privileges for Students");
        users.stream().filter(user -> user.getUserType() == UserType.STUDENT).forEach(user -> {
            log.info("UserDTO: {}", user);
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

        repository.findAll().forEach(userPrivilege -> log.info("UserDTO Privileges: {}", userPrivilege));

    }

    public void setupAttendanceData(AttendanceRepository attendanceRepository
            , StudentAttendanceRepository studentAttendanceRepository
            , EmployeeAttendanceRepository employeeAttendanceRepository
            , UserRepository userRepository) {
        Attendance attendance = new Attendance(LocalDate.now(), AttendanceStatus.PRESENT);
        userRepository.findByUsername("nishi").get().setAttendances(attendance);

        attendance = attendanceRepository.save(attendance);

        StudentAttendance studentAttendance = new StudentAttendance();
        attendance.setStudentAttendance(studentAttendance);

        Student student = (Student) userRepository.findByUsername("hiteshi").get();
        student.setStudentAttendances(studentAttendance);

        StudentAttendanceKey studentAttendanceKey = new StudentAttendanceKey(student.getId(), attendance.getId());
        studentAttendance.setStudentAttendanceKey(studentAttendanceKey);

        student.getInstituteClassSection().setStudentAttendances(studentAttendance);

        studentAttendanceRepository.save(studentAttendance);
    }

    public void setLeaveRequests(LeaveRequestRepository leaveRequestRepository
            , UserRepository userRepository) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setLeaveReason("Sick with Corona.");
        leaveRequest.setLeaveRequestStatus(LeaveRequestStatus.PENDING);
        leaveRequest.setLeaveType(LeaveType.SICK_LEAVE);
//        leaveRequest.setStartDate(new Date(System.currentTimeMillis() + (1 * 24 * 60 * 60 * 1000)));
        leaveRequest.setStartDate(LocalDateTime.now().plusDays(1));
        leaveRequest.setEndDate(LocalDateTime.now().plusDays(5));

        userRepository.findByUsername("nishi").get().setLeaveRequestsApprover(leaveRequest);

        User user = userRepository.findByUsername("hiteshi").get();
        user.setLeaveRequests(leaveRequest);

        leaveRequestRepository.save(leaveRequest);
    }

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
            , @Autowired TeacherRepository teacherRepository
            , @Autowired AttendanceRepository attendanceRepository
            , @Autowired StudentAttendanceRepository studentAttendanceRepository
            , @Autowired EmployeeAttendanceRepository employeeAttendanceRepository
            , @Autowired LeaveRequestRepository leaveRequestRepository
            , @Autowired StudentRepository studentRepository) {

        return args -> {
            log.info("Loading Database");
//            setupInstitutes(instituteRepository);
//            setupClasses(classRepository, instituteClassRepository);
//            setupSections(sectionRepository, instituteClassSectionRepository);
//            setupEmployees(employeeService);
//            setupTeachers(teacherServiceImpl);
//            setupStudents(studentServiceImpl);
//            setupClassTeacher(teacherRepository, instituteClassSectionRepository);
//            setupSubjects(subjectRepository);
//            setupTeacherClassesAndSubjects(classSectionSubjectRepository, subjectRepository, teacherRepository);
//            setupDepartment(departmentRepository);
//            setupPrivileges(privilegeRepository);
//            setupInstituteDepartmentPrivileges(instituteDepartmentPrivilegeRepository, privilegeRepository);
//            setupUserDepartment(userDepartmentRepository, departmentRepository);
//            setupUserPrivileges(userPrivilegeRepository, userRepository, instituteDepartmentPrivilegeRepository);
//            setupAttendanceData(attendanceRepository, studentAttendanceRepository, employeeAttendanceRepository, userRepository);
//            setLeaveRequests(leaveRequestRepository, userRepository);
            log.info("Finished Loading Database");
        };

    }

}
