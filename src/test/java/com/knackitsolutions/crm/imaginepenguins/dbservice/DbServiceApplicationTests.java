package com.knackitsolutions.crm.imaginepenguins.dbservice;

import static org.assertj.core.api.Assertions.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.EmployeeType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.UserType;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.AddressMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.ContactMapperImpl;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.PrivilegeMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.converter.model.UserProfileMapper;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.AddressDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.ContactDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.PrivilegeDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.dto.UserProfileDTO;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.*;
import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.Class;
import com.knackitsolutions.crm.imaginepenguins.dbservice.facade.AppDashboardFacade;
import com.knackitsolutions.crm.imaginepenguins.dbservice.repository.*;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

	public static void setupEmployees(EmployeeRepository employeeRepository) {
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
					return employeeRepository.save(employee);
				}).collect(Collectors.toList()));
	}

	public static void setupTeachers(TeacherRepository teacherRepository) {
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


		Student student1 = new Student(1l, "naman", "naman003", UserType.STUDENT, false, false,
				new UserProfile("Naman", "Kumar"
						, new Address("D74", "Gali-16", "Telangana", "India", "140088")
						, new Contact("9003321", "naman@gmail.com")), instituteClassSection1, null);
		instituteClassSection1.addStudent(student1);

		Student student2 = new Student(1l, "manan", "manan003", UserType.STUDENT, false, false,
				new UserProfile("Manan", "Kumar"
						, new Address("D34", "Gali-26", "Telangana", "India", "140088")
						, new Contact("8003312", "manan@gmail.com")), instituteClassSection1, null);
		instituteClassSection1.addStudent(student2);

		Student student3 = new Student(1l, "kishor", "kishor003", UserType.STUDENT, false, false,
				new UserProfile("Kishor", "Kumar"
						, new Address("D75", "Gali-16", "Telangana", "India", "140088")
						, new Contact("9000011", "kishor@gmail.com")), instituteClassSection2, null);
		instituteClassSection2.addStudent(student3);

		Student student4 = new Student(1l, "hiteshi", "hiteshi003", UserType.STUDENT, false, false,
				new UserProfile("Hiteshi", "Kumar"
						, new Address("D54", "Gali-46", "Telangana", "India", "140088")
						, new Contact("90000118", "hiteshi@gmail.com")), instituteClassSection2, null);
		instituteClassSection2.addStudent(student4);

		Student student5 = new Student(1l, "pooja", "pooja003", UserType.STUDENT, false, false,
				new UserProfile("Pooja", "Kumar"
						, new Address("D79", "Gali-86", "Telangana", "India", "140088")
						, new Contact("9009119", "pooja@gmail.com")), instituteClassSection3, null);
		instituteClassSection3.addStudent(student5);

		Student student6 = new Student(1l, "neetu", "neetu003", UserType.STUDENT, false, false,
				new UserProfile("Neetu", "Kumar"
						, new Address("D740", "Gali-916", "Telangana", "India", "140088")
						, new Contact("881018", "neetu@gmail.com")), instituteClassSection3, null);
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
		Privilege privilege1 = new Privilege(1, "ATD"
				, "Attendance", "Attendance Module");
		Privilege privilege2 = new Privilege(1, "CLD"
				, "Calendar", "Calendar Module");
		Privilege privilege3 = new Privilege(1, "VSATD"
				, "View Self Attendance", "View Self Attendance");
		Privilege privilege4 = new Privilege(1, "MEATD"
				, "Mark Employee Attendance", "Mark Employee Attendance");
		Privilege privilege5 = new Privilege(1, "MSATD"
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

		setupInstitutes(instituteRepository);
		setupClasses(classRepository, instituteClassRepository);
		setupSections(sectionRepository, instituteClassSectionRepository);
		setupEmployees(employeeRepository);
		setupTeachers(teacherRepository);
		setupStudents(studentRepository);

		setupDepartment(departmentRepository);
		setupPrivileges(privilegeRepository);
		setupInstituteDepartmentPrivileges(instituteDepartmentPrivilegeRepository);
		setupUserDepartment(userDepartmentRepository);
		setupUserPrivileges(userPrivilegeRepository, userRepository, instituteDepartmentPrivilegeRepository);

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
	void countEmployeeByInstituteId_test(@Autowired EmployeeRepository employeeRepository) {
		assertThat(employeeRepository.countByInstituteId(institutes.get(0).getId())).isGreaterThan(0);
		assertThat(employeeRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(4);
	}

	@Test
	void countTeachersByInstituteId_test(@Autowired TeacherRepository teacherRepository) {
		assertThat(teacherRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(2);
		assertThat(teacherRepository.countByInstituteId(institutes.get(1).getId())).isEqualTo(0);
	}

	@Test
	void countStudentByInstituteId_test(@Autowired StudentRepository studentRepository) {
		assertThat(studentRepository.countByInstituteId(institutes.get(0).getId())).isGreaterThan(0);
		assertThat(studentRepository.countByInstituteId(institutes.get(0).getId())).isEqualTo(6);
	}

	@Test
	void countEmployeesByInstituteIdAndEmployeeType_test(@Autowired EmployeeRepository employeeRepository) {
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
		assertThat(privilegeDTO.getLogo()).isNull();
		assertThat(privilegeDTO.getId()).isNotNull();
		assertThat(privilegeDTO.getId()).isNotZero();
		assertThat(privilegeDTO.getName()).isNotNull();
	}

	@AfterAll
	public static void tearDown() {
		institutes.clear();
		assertThat(institutes.size()).isEqualTo(0);
		institutes = null;
		assertThat(institutes).isNull();
	}

}