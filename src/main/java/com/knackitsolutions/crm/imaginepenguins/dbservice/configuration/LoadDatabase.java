package com.knackitsolutions.crm.imaginepenguins.dbservice.configuration;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.TeacherResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private List<Institute> newInstitutes(){
        //Open and close timings
        return createSchools();
    }

    private List<Institute> createSchools(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String openTime = "08:00";
        String closeTime = "17:00";
        List<Institute> institutes = null;
        try {
            institutes = Stream.of(
                    new Institute(1, "MBD School", InstituteType.SCHOOL, "9999", dateFormat.parse(openTime),
                            dateFormat.parse(closeTime),
                            new Address("D-74", "Delhi", "India", "110088"),
                            new Contact("970198", "mj22@gmail.com")),
                    new Institute(2, "Saraswati", InstituteType.SCHOOL, "9998", dateFormat.parse(openTime),
                            dateFormat.parse(closeTime),
                            new Address("D-74", "Delhi", "India", "110088"),
                            new Contact("970193", "mj23@gmail.com")),
                    new Institute(3, "IITM College", InstituteType.COLLEGE, "9997", dateFormat.parse(openTime),
                            dateFormat.parse(closeTime),
                            new Address("D-74", "Delhi", "India", "110088"),
                            new Contact("970194", "mj24@gmail.com")))
                    .collect(Collectors.toList());
        }
        catch (ParseException parseException){log.error("Exception: {}", parseException.getMessage());}
        return institutes;
    }

    private List<Class> createNewClasses(){
        return Stream.of(
                new Class(1l, "1", InstituteType.SCHOOL),
                new Class(2l, "2", InstituteType.SCHOOL),
                new Class(3l, "3", InstituteType.SCHOOL),
                new Class(4l, "4", InstituteType.SCHOOL),
                new Class(5l, "5", InstituteType.SCHOOL),
                new Class(6l, "6", InstituteType.SCHOOL),
                new Class(7l, "7", InstituteType.SCHOOL),
                new Class(8l, "8", InstituteType.SCHOOL),
                new Class(9l, "9", InstituteType.SCHOOL),
                new Class(10l, "10", InstituteType.SCHOOL),
                new Class(11l, "Nursery", InstituteType.SCHOOL),
                new Class(12l, "KG", InstituteType.SCHOOL),
                new Class(13l, "LKG", InstituteType.SCHOOL),
                new Class(14l, "11", InstituteType.SCHOOL),
                new Class(15l, "12", InstituteType.SCHOOL)
        ).collect(Collectors.toList());
    }

    private List<Section> createNewSections(){
        return Stream.of(
                new Section(1l, "A", InstituteType.SCHOOL),
                new Section(2l, "B", InstituteType.SCHOOL),
                new Section(1l, "C", InstituteType.SCHOOL),
                new Section(1l, "D", InstituteType.SCHOOL),
                new Section(1l, "E", InstituteType.SCHOOL),
                new Section(1l, "F", InstituteType.SCHOOL),
                new Section(1l, "G", InstituteType.SCHOOL),
                new Section(1l, "H", InstituteType.SCHOOL),
                new Section(1l, "I", InstituteType.SCHOOL),
                new Section(1l, "J", InstituteType.SCHOOL),
                new Section(1l, "K", InstituteType.SCHOOL),
                new Section(1l, "L", InstituteType.SCHOOL),
                new Section(1l, "M", InstituteType.SCHOOL),
                new Section(1l, "N", InstituteType.SCHOOL)
        ).collect(Collectors.toList());
    }

    private List<Subject> createNewSubjects(){
        return Stream.of(
                new Subject(1l, "English"),
                new Subject(1l, "Hindi"),
                new Subject(1l, "Mathematics"),
                new Subject(1l, "Science"),
                new Subject(1l, "Environment Studies"),
                new Subject(1l, "Social Studies"),
                new Subject(1l, "History"),
                new Subject(1l, "Economics"),
                new Subject(1l, "Geography"),
                new Subject(1l, "Sanskrit"),
                new Subject(1l, "Physics"),
                new Subject(1l, "Chemistry")
        ).collect(Collectors.toList());
    }

    private List<InstituteClass> assignClassesToInstitute(List<Institute> institutes, List<Class> classes){
        List<InstituteClass> instituteClasses = new ArrayList<>();

        institutes.get(0).setClasses(classes.stream()
                .limit(5)
                .map(classs-> {
                    return new InstituteClass(1l, institutes.get(0), classs);
                })
                .collect(Collectors.toSet()));
        instituteClasses.addAll(institutes.get(0).getClasses());

        institutes.get(1).setClasses(classes.stream()
                .limit(10)
                .map(classs-> {
                    return new InstituteClass(2l, institutes.get(1), classs);
                })
                .collect(Collectors.toSet()));
        instituteClasses.addAll(institutes.get(1).getClasses());

        institutes.get(2).setClasses(classes.stream()
                .limit(12)
                .map(classs-> {
                    return new InstituteClass(3l, institutes.get(2), classs);
                })
                .collect(Collectors.toSet()));
        instituteClasses.addAll(institutes.get(2).getClasses());

        return instituteClasses;
    }

    private List<InstituteClassSection> assignInstituteClassSection(List<InstituteClass> instituteClasses
            , List<Section> sections){
        List<InstituteClassSection> instituteClassSections = new ArrayList<>();
        instituteClasses.get(0).setClassSections(sections.stream()
                .limit(3)
                .map(section -> new InstituteClassSection(1l, instituteClasses.get(0),
                        section))
                .collect(Collectors.toSet()));
        instituteClassSections.addAll(instituteClasses.get(0).getClassSections());

        instituteClasses.get(1).setClassSections(sections.stream()
                .limit(7)
                .map(section -> new InstituteClassSection(2l, instituteClasses.get(1),
                        section))
                .collect(Collectors.toSet()));
        instituteClassSections.addAll(instituteClasses.get(1).getClassSections());

        instituteClasses.get(2).setClassSections(sections.stream()
                .limit(14)
                .map(section -> new InstituteClassSection(3l, instituteClasses.get(2),
                        section))
                .collect(Collectors.toSet()));
        instituteClassSections.addAll(instituteClasses.get(2).getClassSections());

        return instituteClassSections;
    }

    private List<InstituteClassSection> assignTeachersToSection(List<InstituteClassSection> instituteClassSections
            , List<Teacher> teachers){
        if (instituteClassSections.size() != teachers.size())
            return null;

        AtomicInteger integer = new AtomicInteger();
        instituteClassSections.forEach(instituteClassSection -> {
            teachers.get(integer.get()).setInstituteClassSections(instituteClassSection);
            instituteClassSection.setTeacher(teachers.get(integer.getAndIncrement()));
        });

        return instituteClassSections;
    }

    private List<InstituteClassSectionSubject> assignSubjectsToClassesPlusTeachers(List<InstituteClassSection> classes
            , List<Subject> subjects, List<Teacher> teachers){
        List<InstituteClassSectionSubject> sendList = new ArrayList<>();

        List<InstituteClassSectionSubject> instituteClassSectionSubjects = new ArrayList<>();

        instituteClassSectionSubjects.addAll(assignSubjects(subjects, 5, classes.get(0)));
        //assign teachers
        instituteClassSectionSubjects.get(0).setTeacher(teachers.get(0));
        teachers.get(0).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(0));
        instituteClassSectionSubjects.get(1).setTeacher(teachers.get(1));
        teachers.get(1).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(1));
        instituteClassSectionSubjects.get(2).setTeacher(teachers.get(2));
        teachers.get(2).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(2));
        instituteClassSectionSubjects.get(3).setTeacher(teachers.get(1));
        teachers.get(1).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(3));
        instituteClassSectionSubjects.get(4).setTeacher(teachers.get(3));
        teachers.get(3).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(4));

        sendList.addAll(instituteClassSectionSubjects);
        instituteClassSectionSubjects.clear();

        instituteClassSectionSubjects.addAll(assignSubjects(subjects, 8, classes.get(1)));
        //assign teachers
        instituteClassSectionSubjects.get(0).setTeacher(teachers.get(1));
        teachers.get(1).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(0));
        instituteClassSectionSubjects.get(1).setTeacher(teachers.get(2));
        teachers.get(2).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(1));
        instituteClassSectionSubjects.get(2).setTeacher(teachers.get(0));
        teachers.get(0).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(2));
        instituteClassSectionSubjects.get(3).setTeacher(teachers.get(1));
        teachers.get(1).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(3));
        instituteClassSectionSubjects.get(4).setTeacher(teachers.get(2));
        teachers.get(2).setInstituteClassSectionSubjects(instituteClassSectionSubjects.get(4));

        sendList.addAll(instituteClassSectionSubjects);
        instituteClassSectionSubjects.clear();

        return sendList;
    }

    private List<InstituteClassSectionSubject> assignSubjects(List<Subject> subjects, Integer limit
            , InstituteClassSection classs){
        return subjects.stream().limit(limit).map(subject -> {
            InstituteClassSectionSubject t = new InstituteClassSectionSubject(1l, classs, subject);
            subject.setClassSubjects(t);
            return t;
        }).collect(Collectors.toList());

    }

    private List<Employee> createNewEmployees(){
        return null;
    }

    private List<Teacher> newTeachers(){
        return Stream.of(
                new Teacher(1l, "amit", "amit003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                        new UserProfile("t", "t",
                        new Address("d", "74", "Delhi", "India", "110043"),
                        new Contact("9717", "mj112295@gmail.com")), EmployeeType.TEACHER, "HOD"),
                new Teacher(1l, "ankit", "ankit003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                        new UserProfile("t2", "t2",
                        new Address("d", "22", "Delhi", "India", "110043"),
                        new Contact("97171", "mj11@gmail.com")), EmployeeType.TEACHER, "CI"),
                new Teacher(1l, "mayank", "mayank003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                        new UserProfile("t3", "t3",
                        new Address("d", "90", "Delhi", "India", "110043"),
                        new Contact("97173", "mj9599@gmail.com")), EmployeeType.TEACHER, ""),
                new Teacher(1l, "chayanika", "chayanika003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                        new UserProfile("t4", "t4",
                        new Address("d2", "99", "Delhi", "India", "110043"),
                        new Contact("9717322", "mj@gmail.com")), EmployeeType.TEACHER, ""),
                new Teacher(1l, "raj", "raj003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
                        new UserProfile("t5", "t5",
                        new Address("d2", "22", "UP", "India", "110043"),
                        new Contact("9717321", "mj0@gmail.com")), EmployeeType.TEACHER,
                        "Math Professor")
//                ,
//                new Teacher(1l, "teacher6", "teacher6", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t6", "t6",
//                        new Address("dq", "91", "Delhi", "India", "110043"),
//                        new Contact("971700", "mj34@gmail.com")), EmployeeType.TEACHER, ""),
//                new Teacher(1l, "teacher7", "teacher7", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t7", "t7",
//                        new Address("d1", "00", "Delhi", "India", "110043"),
//                        new Contact("97173220", "mj999@gmail.com")), EmployeeType.TEACHER, ""),
//                new Teacher(1l, "teacher8", "teacher8", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t8", "t8",
//                        new Address("qq", "990", "Delhi", "India", "110043"),
//                        new Contact("9717322080", "mj009@gmail.com")), EmployeeType.TEACHER, ""),
//                new Teacher(1l, "teacher9", "teacher9", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t9", "t9",
//                        new Address("dp", "990", "Delhi", "India", "110043"),
//                        new Contact("9717322000", "mj00001@gmail.com")), EmployeeType.TEACHER, ""),
//                new Teacher(1l, "teacher10", "teacher10", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t10", "t10",
//                        new Address("r1", "9999", "Delhi", "India", "110043"),
//                        new Contact("917322", "mj9999999@gmail.com")), EmployeeType.TEACHER, ""),
//                new Teacher(1l, "teacher11", "teacher11", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
//                        new UserProfile("t11", "t11",
//                        new Address("d11", "11", "Delhi", "India", "110043"),
//                        new Contact("97173221111", "mj1111@gmail.com")), EmployeeType.TEACHER, "")
        ).collect(Collectors.toList());
    }

    private List<Teacher> assignManagers(List<Teacher> teachers){
        teachers.get(0).setManager(teachers.get(1));
        teachers.get(1).setManager(teachers.get(5));
        teachers.get(2).setManager(teachers.get(1));
        teachers.get(3).setManager(teachers.get(2));
        teachers.get(4).setManager(teachers.get(2));
        teachers.get(5).setManager(teachers.get(11));
        teachers.get(6).setManager(teachers.get(2));
        teachers.get(7).setManager(teachers.get(3));
        teachers.get(8).setManager(teachers.get(3));
        teachers.get(9).setManager(teachers.get(3));
        teachers.get(10).setManager(teachers.get(5));

        return teachers;
    }

    private List<InstituteDepartment> createDepartments(){
        return Stream.of(
                new InstituteDepartment("Administrator"),
                new InstituteDepartment("Library"),
                new InstituteDepartment("Accounts"),
                new InstituteDepartment("Transportation"),
                new InstituteDepartment("Sports")
        ).collect(Collectors.toList());
    }

    private List<InstituteDepartment> assignDepartmentsToInstitute(List<InstituteDepartment> instituteDepartments
            , List<Institute> institutes){
        instituteDepartments.get(0).setInstitute(institutes.get(0));
        instituteDepartments.get(1).setInstitute(institutes.get(0));
        instituteDepartments.get(2).setInstitute(institutes.get(0));
        instituteDepartments.get(3).setInstitute(institutes.get(0));
        instituteDepartments.get(4).setInstitute(institutes.get(0));

        instituteDepartments.get(0).setInstitute(institutes.get(1));
        instituteDepartments.get(1).setInstitute(institutes.get(1));
        instituteDepartments.get(2).setInstitute(institutes.get(1));
        instituteDepartments.get(3).setInstitute(institutes.get(1));
        instituteDepartments.get(4).setInstitute(institutes.get(1));

        instituteDepartments.get(0).setInstitute(institutes.get(2));
        instituteDepartments.get(1).setInstitute(institutes.get(2));
        instituteDepartments.get(2).setInstitute(institutes.get(2));
        instituteDepartments.get(3).setInstitute(institutes.get(2));
        instituteDepartments.get(4).setInstitute(institutes.get(2));

        instituteDepartments.get(0).setInstitute(institutes.get(3));
        instituteDepartments.get(1).setInstitute(institutes.get(3));
        instituteDepartments.get(2).setInstitute(institutes.get(3));
        instituteDepartments.get(3).setInstitute(institutes.get(3));
        instituteDepartments.get(4).setInstitute(institutes.get(3));

        return instituteDepartments;
    }
    private List<Privilege> createNewPrivilege(){
        return Stream.of(
                new Privilege("Attendance", "View/Edit Attendance"),
                new Privilege("View Attendance Self", "View their own attendance history."),
                new Privilege("View Attendance Others", "View others attendance history."),
                new Privilege("Mark Attendance", "Check reserved books."),
                new Privilege("Edit Attendance History", "Check reserved books."),

                new Privilege("Calendar", "View/Create events Calendar"),

                new Privilege("User Management","User management"),
                new Privilege("Add User", "Add new users."),
                new Privilege("Browse User", "Filter all users."),
                new Privilege("Remove User", "Remove/delete user."),
                new Privilege("Deactivate User", "Deactivate User."),

                new Privilege("Accounts","Create new fees structure."),
                new Privilege("Fees Creation","Create new fees structure."),
                new Privilege("Fees Collection","Generate Fee receipts."),
                new Privilege("Fees Notification","Send Fee Notification."),
                new Privilege("Fine Creation","Fines Structure Creation."),
                new Privilege("Fine Collection","Generate Fine receipts."),
                new Privilege("Fine Notification", "Send Fines Notification."),

                new Privilege("Transport", "Transport Module."),
                new Privilege("Create Bus Route", "Create new transport routes."),
                new Privilege("Assign Transport Route", "Assign transport to route."),
                new Privilege("Assign Transport Student", "Assign transport to student."),

                new Privilege("Library", "Library Module."),
                new Privilege("Add Books", "Create New Bar Code for Books and assign to books."),
                new Privilege("Issue Books", "Issue new books to User."),
                new Privilege("Charge Fine", "Charge late submit Fine."),
                new Privilege("Reserved Books", "Check reserved books."),
                new Privilege("Books History", "Previously issued books history books."),
                new Privilege("E-Library", "Online Books for reading."),

                new Privilege("School Leaving", "Application for school leaving."),


                new Privilege("Assignment", "Assignment Module"),
                new Privilege("New Assignment", "Create new assignments teachers."),
                new Privilege("View Assignment", "Checkout assigned assignments."),

                new Privilege("ReportCard", "View/Edit ReportCards"),
                new Privilege("Create Report Card", "Generate Report Card."),
                new Privilege("View Report Card", "View Report Card."),

                new Privilege("Exam", "Exam Result."),
                new Privilege("Upload Exam Result", "Upload exam Results."),
                new Privilege("View Exam Result", "View exam Results."),

                new Privilege("Time Table", "Time Table for student and teachers."),
                new Privilege("View Time Table", "View time tables."),
                new Privilege("Edit Time Table", "edit time table."),

                new Privilege("Leave Request", "Leave request."),
                new Privilege("Create Leave Request", "Leave request."),
                new Privilege("Approve Leave Request", "Leave request."),

                new Privilege("Announcement", "Check reserved books."),
                new Privilege("New Announcement", "Check reserved books."),
                new Privilege("View Announcement", "Browse Announcement."),

                new Privilege("Profile", "Profile Module."),
                new Privilege("View Profile", "View Profile Module."),
                new Privilege("Edit Profile", "Edit Parts of Profile Module."),

                new Privilege("Feeds", "Feeds Module."),
                new Privilege("View Feeds", "View Feeds"),
                new Privilege("Delete Feeds", "Delete Feeds"),
                new Privilege("Edit Feeds", "Edit Feeds"),

                new Privilege("Syllabus", "Syllabus Module."),
                new Privilege("View Syllabus", "View Syllabus Module."),
                new Privilege("Edit Syllabus", "Edit Syllabus Module."),
                new Privilege("Add Syllabus", "Add new Syllabus Module."),

                new Privilege("Events", "Event Module."),
                new Privilege("Create Events", "Create Events within their scope. For example." +
                        " Class Teacher can arrange meeting with their class students parent"),

                new Privilege("Inventory", "Inventory Module."),

                new Privilege("E-Learning", "E-leaning Module."),

                new Privilege("Visitor", "Visitor Module"),
                new Privilege("Visitor Register", "Visitor Register"),
                new Privilege("Validate Visitor", "Validate Visitor using barcode for now.")

        ).collect(Collectors.toList());
    }

    private List<InstituteDepartmentPrivilege> assignPrivilegesToDepartment(List<InstituteDepartment> instituteDepartments
            , List<Privilege> privileges){
        
        List<InstituteDepartmentPrivilege> list = new ArrayList<>();

        privileges.stream()
                .forEach(privilege -> list
                        .add(new InstituteDepartmentPrivilege(instituteDepartments.get(0), privilege)));

        IntStream.range(5, 11).mapToObj(
                    i -> list.add(new InstituteDepartmentPrivilege(
                            instituteDepartments.get(2), privileges.get(i))));
//                ).collect(Collectors.toList());
        IntStream.range(11, 13).mapToObj(
                i -> list.add(new InstituteDepartmentPrivilege(
                        instituteDepartments.get(3), privileges.get(i))));

        IntStream.range(13, privileges.size()).mapToObj(
                i -> list.add(new InstituteDepartmentPrivilege(
                        instituteDepartments.get(1), privileges.get(i))));

        return list;
    }

    private List<UserDepartment> assignDepartmentsToEmployees(List<Employee> employees, List<InstituteDepartment> departments){
        List<UserDepartment> employeeDepartments = new ArrayList<>();
        employeeDepartments.add(new UserDepartment(employees.get(0), departments.get(0)));
        employeeDepartments.add(new UserDepartment(employees.get(0), departments.get(3)));
        employeeDepartments.add(new UserDepartment(employees.get(1), departments.get(2)));
        employeeDepartments.add(new UserDepartment(employees.get(2), departments.get(3)));
        employeeDepartments.add(new UserDepartment(employees.get(3), departments.get(4)));
        employeeDepartments.add(new UserDepartment(employees.get(4), departments.get(0)));

        return employeeDepartments;

    }

    private List<UserPrivilege> assignDepartmentsPrivilegesToUserPrivilege(List<Employee> employees
            , List<InstituteDepartmentPrivilege> departmentPrivileges){


        return null;
    }

    private List<TeacherSubject> assignSubjectsToTeacher(List<Teacher> teachers, List<Subject> subjects){
        return null;
    }

    private void switchEmployeeDepartments(){}

    //Assign class incharge
    private List<InstituteClassSection> assignClassTeachers(List<Teacher> teachers, List<InstituteClassSection> classes){
        return null;
    }

/*    private List<User> users = Stream.of(

            new User(1l, "ankit", "ankit003",UserType.EMPLOYEE, true, true, null),
            new User(2l, "amit", "amit003", UserType.EMPLOYEE, true, false, null),
            new User(3l, "raj", "raj003", UserType.EMPLOYEE, false, false, null),
            new User(4l, "gautam", "gautam003", UserType.EMPLOYEE, false, true, null),
            new User(6l, "mayank", "kill", UserType.STUDENT, true, true, null)
    ).collect(Collectors.toList());*/

/*    private List<InstituteDepartmentPrivilege> departmentPrivileges = Stream.of(
            new InstituteDepartmentPrivilege(departments.get(0), privileges.get(3)),
            new InstituteDepartmentPrivilege(departments.get(0), privileges.get(4)),
            new InstituteDepartmentPrivilege(departments.get(1), privileges.get(13)),
            new InstituteDepartmentPrivilege(departments.get(1), privileges.get(14)),
            new InstituteDepartmentPrivilege(departments.get(1), privileges.get(15)),
            new InstituteDepartmentPrivilege(departments.get(2), privileges.get(9)),
            new InstituteDepartmentPrivilege(departments.get(2), privileges.get(5)),
            new InstituteDepartmentPrivilege(departments.get(2), privileges.get(7)),
            new InstituteDepartmentPrivilege(departments.get(3), privileges.get(10)),
            new InstituteDepartmentPrivilege(departments.get(3), privileges.get(11)),
            new InstituteDepartmentPrivilege(departments.get(3), privileges.get(12))

    ).collect(Collectors.toList());

    private void assignDepartmentPrivileges(){
        this.departments.get(0).setPrivileges(Stream.of(
                departmentPrivileges.get(0),
                departmentPrivileges.get(1))
                .collect(Collectors.toSet()));

        this.departments.get(1).setPrivileges(Stream.of(
                departmentPrivileges.get(2),
                departmentPrivileges.get(3),
                departmentPrivileges.get(4)
                ).collect(Collectors.toSet()));

        this.departments.get(2).setPrivileges(Stream.of(
                departmentPrivileges.get(5),
                departmentPrivileges.get(6),
                departmentPrivileges.get(7)
                ).collect(Collectors.toSet()));

        this.departments.get(3).setPrivileges(Stream.of(
                departmentPrivileges.get(9),
                departmentPrivileges.get(9),
                departmentPrivileges.get(10)
        ).collect(Collectors.toSet()));
    }

    private List<UserPrivilege> assignUserPrivileges(){
        List<UserPrivilege> userPrivileges = new ArrayList<>();
        userPrivileges.add(new UserPrivilege(users.get(0), privileges.get(3)));
        userPrivileges.add(new UserPrivilege(users.get(0), privileges.get(2)));
        userPrivileges.add(new UserPrivilege(users.get(1), privileges.get(3)));
        userPrivileges.add(new UserPrivilege(users.get(1), privileges.get(2)));
        userPrivileges.add(new UserPrivilege(users.get(2), privileges.get(1)));
        userPrivileges.add(new UserPrivilege(users.get(2), privileges.get(0)));
        userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(4)));
        userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(1)));
        userPrivileges.add(new UserPrivilege(users.get(3), privileges.get(2)));
        return userPrivileges;
    }*/

    @Bean
    CommandLineRunner initDatabase(UserPrivilegeRepository userPrivilegeRepository,
                                   InstituteRepository instituteRepository, UserRepository userRepository,
                                   PrivilegeRepository privilegeRepository,
                                   DepartmentRepository departmentRepository,
                                   InstituteDepartmentPrivilegeRepository instituteDepartmentPrivilegeRepository,
                                   TeacherRepository teacherRepository){
        log.info("Initializing Database:");
        return args -> {
            log.info("Database Initialization Started");
            log.info("Institutes: {}", newInstitutes());
            newInstitutes().forEach(institute -> instituteRepository.save(institute));
            instituteRepository.findAll().forEach(institute -> log.info("Preloaded: {}",institute));

//            List<Privilege> privileges = privilegeRepository.saveAll(this.privileges);

//            assignDepartmentPrivileges();

//            List<InstituteDepartment> departments = departmentRepository.saveAll(this.departments);

            log.info("Users: {}", newTeachers());
            newTeachers().stream().map(teacher -> {
                teacher.getUserProfile().setUser(teacher);
                return teacherRepository.save(teacher);
            }).forEach(teacher -> log.info("Teacher: {}", teacher));
//            List<Teacher> users = teacherRepository.saveAll(newTeachers());

//            userPrivilegeRepository.saveAll(assignUserPrivileges());

//            userRepository.findAll().forEach(user -> log.info("Preloaded: {}", user));
            log.info("Database Initialization Finished");
        };
    }

}
