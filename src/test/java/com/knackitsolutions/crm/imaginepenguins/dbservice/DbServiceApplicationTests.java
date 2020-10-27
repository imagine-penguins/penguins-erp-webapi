package com.knackitsolutions.crm.imaginepenguins.dbservice;

import static org.assertj.core.api.Assertions.*;

import com.google.common.collect.ImmutableList;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceRequestMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.attendance.AttendanceResponseMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceHistoryDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.AttendanceRequestDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.attendance.StudentAttendanceResponseDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.Attendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendance;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.attendance.StudentAttendanceKey;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.AppDashboardFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.StudentFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.TeacherFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.service.StudentService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.TeacherService;
import com.knackitsolutions.crm.imaginepenguins.dbservice.service.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest
class DbServiceApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DbServiceApplicationTests.class);

	@Test
	void contextLoads() {
	}

	@Autowired
	UserProfileMapper userProfileMapper;

	@Autowired
	AddressMapperImpl addressMapperImpl;

	@Autowired
	ContactMapperImpl contactMapperImpl;

	@Test
	void mapAddressEntityToDTOTest() {
		Address address = new Address("74", "najafgarh", "Delhi", "India", "110045");
		AddressDTO addressDTO = addressMapperImpl.addressToAddressDTO(address);
		assertThat(address.getAddressLine1().equals(addressDTO.getLine1()));
		assertThat(address.getAddressLine2().equals(addressDTO.getLine1()));
		assertThat(address.getCountry().equals(addressDTO.getCountry()));

	}

	@Test
	void mapContactEntityToDTOTest() {
		Contact contact = new Contact("982", "mj@g.co");
		ContactDTO dto = contactMapperImpl.contactToContactDTO(contact);
		assertThat(contact.getPhone().equals(dto.getPhone()));
		assertThat(contact.getEmail().equals(dto.getEmail()));

	}

	@Test
	void mapUserProfileToDTOTest() {
		Address address = new Address("74", "najafgarh", "Delhi", "India", "110045");
		Contact contact = new Contact("982", "mj@g.co");
		UserProfile userProfile = new UserProfile("mayank", "kumar", address, contact);
		UserProfileDTO userProfileDTO = userProfileMapper.userProfileToDTO(userProfile);
		assertThat(userProfile.getAddress().getAddressLine1().equals(userProfileDTO.getAddress().getLine1()));
		assertThat(userProfile.getAddress().getAddressLine2().equals(userProfileDTO.getAddress().getLine2()));
		assertThat(address.getCountry().equals(userProfileDTO.getAddress().getCountry()));
		assertThat(userProfile.getFirstName().equals(userProfileDTO.getFirstName()));
		assertThat(userProfile.getLastName().equals(userProfileDTO.getLastName()));
	}

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

	public static void setupInstitutes(InstituteRepository instituteRepository) {
		Institute institute1 = new Institute(11, "XYC", InstituteType.SCHOOL
				, new Address("D45", "Raod-3", "Delhi", "India", "110034")
				, new Contact("9821", "xyz7721@g.com"));
		Institute institute2 = new Institute(12, "ABC", InstituteType.SCHOOL
				, new Address("D45", "Raod-4", "Delhi", "India", "110034")
				, new Contact("989991", "ABC1@g.com"));

		institute1 = instituteRepository.save(institute1);
		institute2 = instituteRepository.save(institute2);
		institutes.add(institute1);
		institutes.add(institute2);
	}

	public static void setupEmployees(EmployeeRepository employeeRepository) {
		Institute institute1 = institutes.get(0);
		Institute institute2 = institutes.get(1);
		Employee employee1 = new Employee(1l, "gautam1", "gautam003", UserType.EMPLOYEE
				, false, false
				, new UserProfile("Gautam", "Kumar"
				, new Address("D74", "Gali16", "Delhi", "India", "110053")
				, new Contact("10101", "gautam1@gmail.com")), EmployeeType.NON_TEACHER
				, "None", institute1, null, null);

		institute1.addEmployee(employee1);

		Employee employee2 = new Employee(1l, "gaurav1", "gaurav003", UserType.EMPLOYEE
				, false, false
				, new UserProfile("Gaurav", "Kumar"
				, new Address("D14", "Gali16", "Delhi", "India", "110053")
				, new Contact("101111", "gaurav1@gmail.com")), EmployeeType.NON_TEACHER
				, "None", institute1, null, null);
		institute1.addEmployee(employee2);

		employees.addAll(Stream.of(employee1, employee2)
				.map(employee -> {
					employee.getUserProfile().setUser(employee);
					return employeeRepository.save(employee);
				}).collect(Collectors.toList()));
	}

	public static void setupTeachers(TeacherRepository teacherRepository) {
		Institute institute1 = institutes.get(0);
		Institute institute2 = institutes.get(1);
		Teacher teacher1 = new Teacher(1l, "manish1", "manish003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
				new UserProfile("asdf", "dgsh",
						new Address("d", "74", "Delhi", "India", "110043"),
						new Contact("90001", "manish1@gmail.com")), EmployeeType.TEACHER, "HOD");
		Teacher teacher2 = new Teacher(1l, "nisha1", "nisha003", UserType.EMPLOYEE, Boolean.FALSE, Boolean.FALSE,
				new UserProfile("t2", "t2",
						new Address("d", "22", "Delhi", "India", "110043"),
						new Contact("8881", "nisha11@gmail.com")), EmployeeType.TEACHER, "CI");
		institute1.addEmployee(teacher1);
		institute1.addEmployee(teacher2);

		teacher1.getUserProfile().setUser(teacher1);
		teacher2.getUserProfile().setUser(teacher2);

		teacher1 = teacherRepository.save(teacher1);
		teacher2 = teacherRepository.save(teacher2);

		teachers.addAll(Stream.of(teacher1, teacher2).collect(Collectors.toList()));
	}

	public static void setupClasses(ClassRepository classRepository
			, InstituteClassRepository instituteClassRepository) {
		Institute institute1 = institutes.get(0);
		Institute institute2 = institutes.get(1);

		Class class1 = classRepository.save(new Class(1l, "Nursery", InstituteType.SCHOOL));
		Class class2 = classRepository.save(new Class(1l, "K.G.", InstituteType.SCHOOL));

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

	public static void setupStudents(StudentRepository studentRepository) {
		InstituteClassSection instituteClassSection1 = instituteClassSections.get(0);
		InstituteClassSection instituteClassSection2 = instituteClassSections.get(1);
		InstituteClassSection instituteClassSection3 = instituteClassSections.get(2);
		InstituteClassSection instituteClassSection4 = instituteClassSections.get(3);
		InstituteClassSection instituteClassSection5 = instituteClassSections.get(4);


		Student student1 = new Student(1l, "naman1", "naman003", UserType.STUDENT, false, false,
				new UserProfile("Naman", "Kumar"
						, new Address("D74", "Gali-16", "Telangana", "India", "140088")
						, new Contact("90033211", "naman1@gmail.com")), instituteClassSection1, null);
		instituteClassSection1.addStudent(student1);

		Student student2 = new Student(1l, "manan1", "manan003", UserType.STUDENT, false, false,
				new UserProfile("Manan", "Kumar"
						, new Address("D34", "Gali-26", "Telangana", "India", "140088")
						, new Contact("80033121", "manan1@gmail.com")), instituteClassSection1, null);
		instituteClassSection1.addStudent(student2);

		Student student3 = new Student(1l, "kishor1", "kishor003", UserType.STUDENT, false, false,
				new UserProfile("Kishor", "Kumar"
						, new Address("D75", "Gali-16", "Telangana", "India", "140088")
						, new Contact("90000111", "kishor1@gmail.com")), instituteClassSection2, null);
		instituteClassSection2.addStudent(student3);

		Student student4 = new Student(1l, "hiteshi1", "hiteshi003", UserType.STUDENT, false, false,
				new UserProfile("Hiteshi", "Kumar"
						, new Address("D54", "Gali-46", "Telangana", "India", "140088")
						, new Contact("900001181", "hiteshi1@gmail.com")), instituteClassSection2, null);
		instituteClassSection2.addStudent(student4);

		Student student5 = new Student(1l, "pooja1", "pooja003", UserType.STUDENT, false, false,
				new UserProfile("Pooja", "Kumar"
						, new Address("D79", "Gali-86", "Telangana", "India", "140088")
						, new Contact("90091191", "pooja1@gmail.com")), instituteClassSection3, null);
		instituteClassSection3.addStudent(student5);

		Student student6 = new Student(1l, "neetu1", "neetu003", UserType.STUDENT, false, false,
				new UserProfile("Neetu", "Kumar"
						, new Address("D740", "Gali-916", "Telangana", "India", "140088")
						, new Contact("8810181", "neetu1@gmail.com")), instituteClassSection3, null);
		instituteClassSection3.addStudent(student6);

		students.addAll(Stream.of(student1, student2, student3, student4, student5, student6)
				.map(student -> {
					student.getUserProfile().setUser(student);
					return studentRepository.save(student);
				}).collect(Collectors.toList()));

	}

	public static void setupDepartment(DepartmentRepository departmentRepository) {
		InstituteDepartment department1 = new InstituteDepartment(1l,"ADMIN");
		InstituteDepartment department2 = new InstituteDepartment(1l,"STUDENT");
		InstituteDepartment department3 = new InstituteDepartment(1l,"TEACHER");
		InstituteDepartment department4 = new InstituteDepartment(1l,"PARENT");
		departments.addAll(Stream.of(department1, department2
				, department3, department4)
				.map(department -> {
					institutes.get(0).addInstituteDepartments(department);
					return departmentRepository.save(department);
				}).collect(Collectors.toList()));
	}

	public static void setupPrivileges(PrivilegeRepository privilegeRepository) {
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

	public static void setupInstituteDepartmentPrivileges(InstituteDepartmentPrivilegeRepository repository) {
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

	@Test
	void does_departmentsHaveNecessaryPrivileges_test(@Autowired DepartmentRepository repository){
		List<InstituteDepartment> studentDepartment = repository.findAll().stream()
				.filter(department -> department.getDepartmentName() == "STUDENT")
				.collect(Collectors.toList());

		assertThat(studentDepartment).isNotNull();
		assertThat(studentDepartment).isNotEmpty();

		assertThat(studentDepartment.size()).isGreaterThan(0);


		Set<InstituteDepartmentPrivilege> instituteDepartmentPrivileges = studentDepartment.get(0).getPrivileges();
		assertThat(instituteDepartmentPrivileges).isNotNull();
		assertThat(instituteDepartmentPrivileges).isNotEmpty();

		assertThat(instituteDepartmentPrivileges.size()).isEqualTo(2);


	}

	public static void setupUserDepartment(UserDepartmentRepository userDepartmentRepository) {
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

	public static void setupUserPrivileges(UserPrivilegeRepository repository, UserRepository userRepository
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

	@BeforeAll
	public static void setup(@Autowired InstituteRepository instituteRepository
			, @Autowired EmployeeRepository employeeRepository, @Autowired TeacherRepository teacherRepository
			, @Autowired ClassRepository classRepository, @Autowired SectionRepository sectionRepository
			, @Autowired InstituteClassRepository instituteClassRepository
			, @Autowired InstituteClassSectionRepository instituteClassSectionRepository
			, @Autowired StudentRepository studentRepository
			, @Autowired DepartmentRepository departmentRepository
			, @Autowired PrivilegeRepository privilegeRepository
			, @Autowired InstituteDepartmentPrivilegeRepository instituteDepartmentPrivilegeRepository
			, @Autowired UserDepartmentRepository userDepartmentRepository
			, @Autowired UserPrivilegeRepository userPrivilegeRepository
			, @Autowired UserRepository userRepository) {

//		setupInstitutes(instituteRepository);
//		setupClasses(classRepository, instituteClassRepository);
//		setupSections(sectionRepository, instituteClassSectionRepository);
//		setupEmployees(employeeRepository);
//		setupTeachers(teacherRepository);
//		setupStudents(studentRepository);
//
//		setupDepartment(departmentRepository);
//		setupPrivileges(privilegeRepository);
//		setupInstituteDepartmentPrivileges(instituteDepartmentPrivilegeRepository);
//		setupUserDepartment(userDepartmentRepository);
//		setupUserPrivileges(userPrivilegeRepository, userRepository, instituteDepartmentPrivilegeRepository);

	}

	@Test
	void foundAllNonTeacherEmployee_fromEmployeeTable_test(@Autowired EmployeeRepository employeeRepository) {
		List<Employee> employees = employeeRepository.findByEmployeeType(EmployeeType.NON_TEACHER);
		assertThat(employees.get(0).getEmployeeType()).isEqualTo(EmployeeType.NON_TEACHER);
		assertThat(employees.get(0).getEmployeeType()).isNotEqualTo(EmployeeType.TEACHER);
	}

	@Test
	void foundAllNonTeacherEmployee_fromUserTable_test(@Autowired UserRepository userRepository) {
		List<User> users = userRepository.findByUserType(UserType.EMPLOYEE);
		List<Employee> employees = users.stream()
				.map(user -> ((Employee)user))
				.collect(Collectors.toList());
		Long count = employees.stream()
				.filter(user -> ((Employee)user).getEmployeeType() == EmployeeType.NON_TEACHER)
				.count();
		assertThat(count).isPositive();
		assertThat(count).isEqualTo(2);

	}

	@Test
	void countEmployeeByInstituteId_test(@Autowired EmployeeRepository employeeRepository
			, @Autowired InstituteRepository instituteRepository) {
		List<Institute> institutes = instituteRepository.findAll();
		assertThat(employeeRepository.countByInstituteId(institutes.get(0).getId())).isGreaterThan(0);
		assertThat(employeeRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(4);
	}

	@Test
	void countTeachersByInstituteId_test(@Autowired TeacherRepository teacherRepository
			, @Autowired InstituteRepository instituteRepository) {
		List<Institute> institutes = instituteRepository.findAll();
		assertThat(teacherRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(2);
		assertThat(teacherRepository.countByInstituteId(institutes.get(1).getId())).isEqualTo(0);
	}

	@Test
	void countStudentByInstituteId_test(@Autowired StudentRepository studentRepository
			, @Autowired InstituteRepository instituteRepository) {
		List<Institute> institutes = instituteRepository.findAll();
		assertThat(studentRepository.countByInstituteId(institutes.get(0).getId())).isGreaterThan(0);
		assertThat(studentRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(6);
	}

	@Test
	void countEmployeesByInstituteIdAndEmployeeType_test(@Autowired EmployeeRepository employeeRepository
			, @Autowired InstituteRepository instituteRepository) {
		List<Institute> institutes = instituteRepository.findAll();
		assertThat(employeeRepository.countEmployeesByInstituteId(institutes.get(0).getId()
				, EmployeeType.NON_TEACHER)).isEqualTo(2);

		assertThat(employeeRepository.countEmployeesByInstituteId(institutes.get(0).getId()
				, EmployeeType.TEACHER)).isEqualTo(2);

	}

	@Test
	void does_usersHavePrivileges_test(@Autowired UserPrivilegeRepository repository
			, @Autowired StudentRepository studentRepository) {
		List<Student> students = studentRepository.findAll();

		assertThat(students).isNotNull();
		assertThat(students).isNotEmpty();

		List<UserPrivilege> userPrivileges1 = repository.findByUserId(students.get(0).getId());

		assertThat(userPrivileges1).isNotNull();
		assertThat(userPrivileges1).isNotEmpty();

		List<InstituteDepartmentPrivilege> departmentPrivileges = userPrivileges1.stream()
				.map(userPrivilege -> userPrivilege.getDepartmentPrivilege())
				.collect(Collectors.toList());

		assertThat(departmentPrivileges).isNotNull();
		assertThat(departmentPrivileges).isNotEmpty();

		List<InstituteDepartment> instituteDepartments = departmentPrivileges.stream()
				.map(departmentPrivilege -> departmentPrivilege.getInstituteDepartment())
				.collect(Collectors.toList());

		assertThat(instituteDepartments).isNotNull();
		assertThat(instituteDepartments).isNotEmpty();

		instituteDepartments.forEach(instituteDepartment -> log.info("Department ID: {}", instituteDepartment.getId()));

		List<UserPrivilege> userPrivileges2 = repository.findByUserIdAndDepartmentId(students.get(0).getId(),
				students.get(0).getUserDepartments().stream().findFirst().get().getInstituteDepartment().getId());

		assertThat(userPrivileges2).isNotNull();
		assertThat(userPrivileges2).isNotEmpty();

		List<Privilege> privileges = userPrivileges2.stream()
				.map(userPrivilege -> userPrivilege.getDepartmentPrivilege().getPrivilege())
				.collect(Collectors.toList());

		assertThat(privileges).isNotNull();
		assertThat(privileges).isNotEmpty();

		PrivilegeMapper mapper = new PrivilegeMapper();

		List<PrivilegeDTO> dtos = privileges.stream().map(mapper::entityToDTO).collect(Collectors.toList());

		dtos.stream().forEach(this::privilegeDTOAttributesAreNotNull_test);

	}

	@Test
	void appDashBoardFacade_getPrivilegeDTOsByUserIdAndDepartmentId_test(@Autowired StudentRepository repository
			,@Autowired AppDashboardFacade facade){

		List<Student> students = repository.findAll();
		List<PrivilegeDTO> privilegeDTOS = facade.getPrivileges(students.get(0).getId(), students.get(0)
				.getUserDepartments()
				.stream()
				.findFirst()
				.get()
				.getInstituteDepartment()
				.getId());

		assertThat(students).isNotNull();
		assertThat(students).isNotEmpty();
		privilegeDTOS.forEach(this::privilegeDTOAttributesAreNotNull_test);
	}

	private void privilegeDTOAttributesAreNotNull_test(PrivilegeDTO privilegeDTO){
		assertThat(privilegeDTO).isNotNull();
		assertThat(privilegeDTO.getBgImg()).isNull();
		assertThat(privilegeDTO.getId()).isNotNull();
		assertThat(privilegeDTO.getId()).isNotZero();
		assertThat(privilegeDTO.getName()).isNotNull();
	}

	@Test
	void removeFromStartAndTrim_test() {
		String value = "Bearer b856850e-1ad4-456d-b5ca-1c2bfc355e5e";
		String BEARER = "Bearer";
		String result = removeFromStartAndTrim(value, BEARER);
		assertThat(result).isNotNull();
		assertThat(result).isNotEmpty();
		assertThat(result).isEqualTo("b856850e-1ad4-456d-b5ca-1c2bfc355e5e");
	}

//	@Test
	void privilegeNotEmpty_whenPrivilegePresent_test(@Autowired UserService userService) {
		String username = "naman";
		String endPoint = "/users";
		Optional<Privilege> privilege = userService.getPrivilege(username, endPoint);
		assertThat(privilege).isNotEmpty();
		
	}

//	@Test
	void privilegeIsEmpty_whenPrivilegeNotPresent_Test(@Autowired UserService userService) {

	}

	@Test
	void teachersClassSectionSubject_list(@Autowired TeacherRepository teacherRepository
			, @Autowired TeacherFacade teacherFacade, @Autowired ClassSectionSubjectMapper mapper) {

		List<Teacher> teachers = teacherRepository.findAll();

		ClassSectionSubjectDTO dto = mapper
				.entityToDTO(teacherRepository
								.getClassSectionSubjects(teachers.get(0).getId())
						, teacherRepository.getClassSections(teachers.get(0).getId()));

		assertThat(dto).isNotNull();
		assertThat(dto.getSubjectClasses()).isNotNull();
		assertThat(dto.getSubjectClasses()).isNotEmpty();
		assertThat(dto.getSubjectClasses().get(0)).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getClassName()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getSectionName()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getSubjectName()).isNotNull();
	}

	@Test
	void teacherClassSectionlist_notEmpty_whenMappedToDTO(@Autowired TeacherRepository teacherRepository
			, @Autowired TeacherFacade teacherFacade, @Autowired ClassSectionSubjectMapper mapper) {
		List<Teacher> teachers = teacherRepository.findAll();

		ClassSectionSubjectDTO dto = mapper
				.entityToDTO(teacherRepository
						.getClassSectionSubjects(teachers.get(0).getId())
						, teacherRepository.getClassSections(teachers.get(0).getId()));

		assertThat(dto).isNotNull();
		assertThat(dto.getClassDTOs()).isNotNull();
		assertThat(dto.getClassDTOs()).isNotEmpty();
		assertThat(dto.getClassDTOs().get(0)).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getClassName()).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getSectionName()).isNotNull();
	}

	@Test
	void studentInfo_isNotEmpty(@Autowired StudentAttendanceRepository repository
		, @Autowired InstituteClassSectionRepository instituteClassSectionRepository
			, @Autowired AttendanceResponseMapper mapper) {
		List<InstituteClassSection> instituteClassSections = instituteClassSectionRepository.findAll();
		log.debug("class id: {}", instituteClassSections.get(0).getId());
		List<StudentAttendanceResponseDTO> studentsInfo = repository
				.findByClassSectionId(instituteClassSections.get(0).getId())
				.stream()
				.map(mapper::mapStudentAttendanceToStudent)
				.collect(Collectors.toList());

		assertThat(studentsInfo).isNotNull();
		assertThat(studentsInfo).isNotEmpty();
		assertThat(studentsInfo.get(0).getUserId()).isNotZero();
		assertThat(studentsInfo.get(0).getFirstName()).isNotNull();
		assertThat(studentsInfo.get(0).getRollNumber()).isNotNull();
	}

	@Test
	void studentAttendanceEntityNotNull_whenStudentAttendanceMapper_mappedDTOToEntity_test(
			@Autowired AttendanceRequestMapper mapper
			, @Autowired StudentRepository studentRepository
			, @Autowired StudentService studentService
			, @Autowired TeacherRepository teacherRepository){
		Student student = studentRepository.findAll().get(0);
		Teacher teacher = teacherRepository.findAll().get(0);

		AttendanceRequestDTO.UserAttendanceRequestDTO dto = new AttendanceRequestDTO.UserAttendanceRequestDTO(student.getId()
				, AttendanceStatus.PRESENT);

		AttendanceRequestDTO attendanceRequestDTO =
				new AttendanceRequestDTO();
		Long subjectId = teacher
				.getInstituteClassSectionSubjects()
				.stream()
				.findFirst()
				.get()
				.getId();

		Long teacherId = teacher.getId();
		Date date = new Date(System.currentTimeMillis());
		attendanceRequestDTO.setAttendanceData(ImmutableList.of(dto));
		attendanceRequestDTO.setAttendanceDate(date);
		attendanceRequestDTO.setSupervisorId(teacherId);
		List<StudentAttendance> entities =
				mapper.dtoToEntity(attendanceRequestDTO, Optional.empty(), Optional.ofNullable(subjectId));

		assertThat(entities).isNotNull();
		assertThat(entities).isNotEmpty();
		assertThat(entities.get(0)).isNotNull();
		assertThat(entities.get(0).getAttendance()).isNotNull();
		assertThat(entities.get(0).getAttendance().getSupervisor()).isNotNull();
		assertThat(entities.get(0).getAttendance().getAttendanceDate()).isNotNull();
		assertThat(entities.get(0).getAttendance().getAttendanceStatus()).isNotNull();
		assertThat(entities.get(0).getAttendance().getId()).isNotNull();

		assertThat(entities.get(0).getStudent()).isNotNull();

		if (entities.get(0).getClassSection() == null)
			assertThat(entities.get(0).getInstituteClassSectionSubject()).isNotNull();
		if (entities.get(0).getInstituteClassSectionSubject() == null)
			assertThat(entities.get(0).getClassSection()).isNotNull();

		entities = studentService.saveAttendance(entities);

		assertThat(entities).isNotNull();
		assertThat(entities).isNotEmpty();
		assertThat(entities.get(0)).isNotNull();
		assertThat(entities.get(0).getAttendance()).isNotNull();
		assertThat(entities.get(0).getAttendance().getSupervisor()).isNotNull();
		assertThat(entities.get(0).getAttendance().getAttendanceDate()).isNotNull();
		assertThat(entities.get(0).getAttendance().getAttendanceStatus()).isNotNull();
		assertThat(entities.get(0).getAttendance().getId()).isNotNull();

		assertThat(entities.get(0).getStudent()).isNotNull();

		if (entities.get(0).getClassSection() == null)
			assertThat(entities.get(0).getInstituteClassSectionSubject()).isNotNull();
		if (entities.get(0).getInstituteClassSectionSubject() == null)
			assertThat(entities.get(0).getClassSection()).isNotNull();

	}

	@Test
	void test_teacherClasses_notNull_whenIdis17(@Autowired TeacherService teacherService) {
		Long teacherId = 17l;
		List<InstituteClassSection> instituteClassSections = teacherService.loadClassSections(17l);
		assertThat(instituteClassSections).isNotNull();
		assertThat(instituteClassSections).isNotEmpty();
		instituteClassSections
				.forEach(classs -> {
					assertThat(classs).isNotNull();
					assertThat(classs.getInstituteClass()).isNotNull();
					assertThat(classs.getInstituteClass().getId()).isNotNull();
					log.debug("id: {}", classs.getInstituteClass().getId());
				});
	}

	@Test
	void test_teacherFacadeClassesList_isNotEmptyAndNonNull(@Autowired TeacherFacade teacherFacade
			, @Autowired TeacherRepository teacherRepository) {
		List<Teacher> teachers = teacherRepository.findAll();
		ClassSectionSubjectDTO dto = teacherFacade.loadClasses(teachers.get(0).getId());
		assertThat(dto).isNotNull();

		assertThat(dto.getClassDTOs()).isNotNull();
		assertThat(dto.getClassDTOs()).isNotEmpty();
		assertThat(dto.getClassDTOs().get(0)).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getClassName()).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getSectionName()).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getLinks()).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getLinks()).isNotEmpty();
		assertThat(dto.getClassDTOs().get(0).getLink("class-students")).isNotNull();
		assertThat(dto.getClassDTOs().get(0).getInstituteClassSectionId()).isNotZero();

		assertThat(dto.getSubjectClasses()).isNotNull();
		assertThat(dto.getSubjectClasses()).isNotEmpty();
		assertThat(dto.getSubjectClasses().get(0)).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getSubjectName()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getClassName()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getSectionName()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getLinks()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getLinks()).isNotEmpty();
		assertThat(dto.getSubjectClasses().get(0).getLink("class-students").get()).isNotNull();
		assertThat(dto.getSubjectClasses().get(0).getInstituteClassSectionSubjectId()).isNotZero();
	}

	@Test
	void throwsException_whenPrivilegeNotPresent_Test(@Autowired UserService userService) {

	}

	@Test
	void attendancehistoryStudentList_notEmpty() {
		AttendanceHistoryDTO historyDTO = new AttendanceHistoryDTO();
		StudentAttendanceResponseDTO student = new StudentAttendanceResponseDTO();
		List<StudentAttendanceResponseDTO> students = new ArrayList<>();
		student.setFirstName("Mayank");
		student.setRollNumber("3");
		student.setStatus(Optional.of(AttendanceStatus.PRESENT));
		StudentAttendanceKey key = new StudentAttendanceKey();
		key.setStudentId(1l);
		key.setAttendanceId(100l);
		student.setAttendanceId(Optional.ofNullable(key.getAttendanceId()));
		student.setUserId(key.getStudentId());

		students.add(student);

		historyDTO.setStudents(students);

		assertThat(historyDTO).isNotNull();
		assertThat(historyDTO.getStudents()).isNotNull();
		assertThat(historyDTO.getStudents()).isNotEmpty();
		assertThat(historyDTO.getStudents().get(0)).isNotNull();
		assertThat(historyDTO.getStudents().get(0).getAttendanceId()).isNotNull();

	}

	@Test
	void attendanceHistory_GraphData_isNotNull() {
		AttendanceHistoryDTO dto = new AttendanceHistoryDTO();
		AttendanceHistoryDTO.GraphData graphData = new AttendanceHistoryDTO.GraphData();
		graphData.setAbsentPercent(10);
		graphData.setPresentPercent(80);
		graphData.setLeavePercent(20);
		dto.setGraphData(graphData);

		assertThat(dto).isNotNull();
		assertThat(dto.getGraphData()).isNotNull();
		assertThat(dto.getGraphData().getAbsentPercent()).isNotNull();

	}

	@Test
	void studentAttendanceList_notEmpty_when_recordsArePresentInDB(@Autowired StudentService studentService
			, @Autowired AttendanceRepository attendanceRepository, @Autowired TeacherRepository teacherRepository) {
		Student student = studentService.all().get(0);
		Attendance attendance = new Attendance(1l, new Date(System.currentTimeMillis()), AttendanceStatus.PRESENT);
		attendance.setSupervisor(
				teacherRepository
						.findByInstituteClassSectionsId(student.getInstituteClassSection().getId())
						.get(0)
		);
		attendance = attendanceRepository.save(attendance);

		StudentAttendanceKey studentAttendanceKey = new StudentAttendanceKey();
		studentAttendanceKey.setStudentId(student.getId());
		studentAttendanceKey.setAttendanceId(attendance.getId());

		StudentAttendance studentAttendance = new StudentAttendance();
		studentAttendance.setStudentAttendanceKey(studentAttendanceKey);
		student.getInstituteClassSection().setStudentAttendances(studentAttendance);
		student.setStudentAttendances(studentAttendance);
		attendance.setStudentAttendance(studentAttendance);

		studentService.saveAttendance(studentAttendance);

		List<StudentAttendance> studentAttendances
				= studentService.getStudentAttendancesByClassId(student.getInstituteClassSection().getId());

		assertThat(studentAttendances).isNotNull();
		assertThat(studentAttendances).isNotEmpty();
		assertThat(studentAttendances.get(0)).isNotNull();
		assertThat(studentAttendances.get(0).getStudent()).isNotNull();
		assertThat(studentAttendances.get(0).getAttendance()).isNotNull();
		assertThat(studentAttendances.get(0).getStudentAttendanceKey()).isNotNull();
		assertThat(studentAttendances.get(0).getInstituteClassSectionSubject()).isNull();
		assertThat(studentAttendances.get(0).getClassSection()).isNotNull();

	}

	@Test
	void studentAttendance_notNull_when_recordsPresent(@Autowired StudentService studentService
			, @Autowired AttendanceRepository attendanceRepository, @Autowired TeacherRepository teacherRepository) {
		Student student = studentService.all().get(0);
		Attendance attendance = new Attendance(1l, new Date(System.currentTimeMillis()), AttendanceStatus.PRESENT);
		attendance.setSupervisor(
				teacherRepository
						.findByInstituteClassSectionsId(student.getInstituteClassSection().getId())
						.get(0)
		);
		attendance = attendanceRepository.save(attendance);

		StudentAttendanceKey studentAttendanceKey = new StudentAttendanceKey();
		studentAttendanceKey.setStudentId(student.getId());
		studentAttendanceKey.setAttendanceId(attendance.getId());

		StudentAttendance studentAttendance = new StudentAttendance();
		studentAttendance.setStudentAttendanceKey(studentAttendanceKey);
		student.getInstituteClassSection().setStudentAttendances(studentAttendance);
		student.setStudentAttendances(studentAttendance);
		attendance.setStudentAttendance(studentAttendance);

		studentService.saveAttendance(studentAttendance);

		List<StudentAttendance> studentAttendances = studentService.getStudentAttendancesByStudentId(student.getId());
		assertThat(studentAttendances).isNotNull();
		assertThat(studentAttendances).isNotEmpty();
		assertThat(studentAttendances.get(0)).isNotNull();

	}

	private Attendance createAndSaveAttendance(Date date, AttendanceStatus status, User supervisor
			, AttendanceRepository attendanceRepository) {
		Attendance attendance1 = new Attendance(1l, date, status);
		attendance1.setSupervisor(supervisor);
		attendance1.setUpdateTime(date);
		return attendanceRepository.save(attendance1);
	}

	private StudentAttendanceKey getStudentAttendanceKey(Long studentId, Long attendanceId) {
		StudentAttendanceKey studentAttendanceKey = new StudentAttendanceKey();
		studentAttendanceKey.setStudentId(studentId);
		studentAttendanceKey.setAttendanceId(attendanceId);
		return studentAttendanceKey;
	}

	private StudentAttendance createAndSaveStudentAttendance(StudentAttendanceKey key, Student student
			, Attendance attendance, StudentService studentService) {
		StudentAttendance studentAttendance = new StudentAttendance();
		studentAttendance.setStudentAttendanceKey(key);
		student.getInstituteClassSection().setStudentAttendances(studentAttendance);
		student.setStudentAttendances(studentAttendance);
		attendance.setStudentAttendance(studentAttendance);
		studentAttendance = studentService.saveAttendance(studentAttendance).get();
		return studentAttendance;
	}

	private void createStudentAttendances(StudentService studentService, AttendanceRepository attendanceRepository
			,Student student, Teacher teacher) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Attendance attendance1 = createAndSaveAttendance(format.parse("26-09-2020")
				, AttendanceStatus.PRESENT, teacher, attendanceRepository);
		Attendance attendance2 = createAndSaveAttendance(format.parse("20-09-2020")
				, AttendanceStatus.PRESENT, teacher, attendanceRepository);
		Attendance attendance3 = createAndSaveAttendance(format.parse("21-09-2020")
				, AttendanceStatus.PRESENT, teacher, attendanceRepository);
		Attendance attendance4 = createAndSaveAttendance(format.parse("23-09-2020")
				, AttendanceStatus.PRESENT, teacher, attendanceRepository);

		StudentAttendanceKey studentAttendanceKey1 = getStudentAttendanceKey(student.getId(), attendance1.getId());
		StudentAttendanceKey studentAttendanceKey2 = getStudentAttendanceKey(student.getId(), attendance2.getId());
		StudentAttendanceKey studentAttendanceKey3 = getStudentAttendanceKey(student.getId(), attendance3.getId());
		StudentAttendanceKey studentAttendanceKey4 = getStudentAttendanceKey(student.getId(), attendance4.getId());

		createAndSaveStudentAttendance(studentAttendanceKey1, student, attendance1, studentService);
		createAndSaveStudentAttendance(studentAttendanceKey2, student, attendance2, studentService);
		createAndSaveStudentAttendance(studentAttendanceKey3, student, attendance3, studentService);
		createAndSaveStudentAttendance(studentAttendanceKey4, student, attendance4, studentService);
	}

	@Test
	void studentAttendanceBetweenDates_notNull_when_recordsPresent(@Autowired StudentService studentService
			, @Autowired AttendanceRepository attendanceRepository
			, @Autowired TeacherRepository teacherRepository) throws ParseException{
		Student student = studentService.all().get(0);
		Teacher teacher = teacherRepository
				.findByInstituteClassSectionsId(student.getInstituteClassSection().getId())
				.get(0);
		createStudentAttendances(studentService, attendanceRepository, student, teacher);
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		List<StudentAttendance> studentAttendances = studentService.getStudentAttendancesByStudentId(
				student.getId()
				, Optional.of(format.parse("21-09-2020"))
				, Optional.of(format.parse("21-09-2020"))
		);
		studentAttendances
				.stream()
				.forEach(studentAttendance -> log.info("StudentAttendance: {}", studentAttendance.toString()));


		assertThat(studentAttendances).isNotNull();
		assertThat(studentAttendances).isNotEmpty();

		assertThat(studentAttendances.size()).isEqualTo(1);

		assertThat(studentAttendances.get(0)).isNotNull();
		assertThat(studentAttendances.get(0).getStudentAttendanceKey()).isNotNull();
		assertThat(studentAttendances.get(0).getStudentAttendanceKey().getStudentId()).isNotNull();
		assertThat(studentAttendances.get(0).getAttendance()).isNotNull();
		assertThat(studentAttendances.get(0).getAttendance().getAttendanceDate()).isNotNull();
		assertThat(format.format(studentAttendances.get(0).getAttendance().getAttendanceDate())).isEqualTo("21-09-2020");

	}

	@Test
	void studentAttendance_isNotNull_when_studentKeyIsPresent(@Autowired StudentService studentService
			, @Autowired AttendanceRepository attendanceRepository
			, @Autowired TeacherRepository teacherRepository) throws ParseException{
		Student student = studentService.all().get(0);
		Teacher teacher = teacherRepository
				.findByInstituteClassSectionsId(student.getInstituteClassSection().getId())
				.get(0);

		createStudentAttendances(studentService, attendanceRepository, student, teacher);

		StudentAttendance studentAttendance = studentService
				.getStudentAttendanceById(
						student.getStudentAttendances().get(0).getStudentAttendanceKey());

		assertThat(studentAttendance).isNotNull();


	}

	private final String removeFromStartAndTrim(String value, String remove) {
		return value.replaceFirst(remove, "").trim();
	}

	@AfterAll
	public static void tearDown() {
		institutes.clear();
		assertThat(institutes.size()).isEqualTo(0);
		institutes = null;
		assertThat(institutes).isNull();
	}

}