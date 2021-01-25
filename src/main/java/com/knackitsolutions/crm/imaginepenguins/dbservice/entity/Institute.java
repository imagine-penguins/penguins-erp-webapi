package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.entity.document.InstituteDocumentStore;
import com.knackitsolutions.crm.imaginepenguins.dbservice.listner.InstituteListner;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

import java.util.*;

@EntityListeners(InstituteListner.class)
@Entity(name = "institute")
@Table(name = "institutes")
public class Institute {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "institute_id")
	Integer id;
	
	String name;
	
	@Column(name = "institution_type", nullable = false)
	InstituteType instituteType;

	@Column(name = "recognition_number")
	String recognitionNumber;

	@Column(name = "open_time")
	@Temporal(TemporalType.TIME)
	Date openTime;

	@Column(name = "close_time")
	@Temporal(TemporalType.TIME)
	Date closeTime;

	@OneToMany(mappedBy = "institute")
	List<InstituteDocumentStore> instituteDocumentStores;

	@Embedded
	@AttributeOverrides(value = {@AttributeOverride(name = "addressLine1", column = @Column(name = "house_number")),
			@AttributeOverride(name = "addressLine2", column = @Column(name = "street"))
	})
	private Address address;

	@Embedded
	@AttributeOverrides(value = {
			@AttributeOverride(name = "phone", column = @Column(name = "phone_number", nullable = false, unique = true)),
			@AttributeOverride(name = "email", column = @Column(name = "email_address", nullable = false, unique = true)),
			@AttributeOverride(name = "alternatePhone", column = @Column(name = "alternate_phone_number", unique = true)),
			@AttributeOverride(name = "alternateEmail", column = @Column(name = "alternate_email_address", unique = true))
	})
	private Contact contact;

	@OneToMany(mappedBy = "institute"
			, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
	private Set<Employee> employees = new HashSet<>();

	@OneToMany(mappedBy = "institute", fetch = FetchType.LAZY
			, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
	private Set<InstituteClass> classes = new HashSet<>();

	@OneToMany(mappedBy = "institute", fetch = FetchType.LAZY
			, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
	private Set<InstituteDepartment> instituteDepartments = new HashSet<>();

	@OneToMany(mappedBy = "institute", orphanRemoval = true)
	private Set<Holiday> holidays = new HashSet<>();

	@OneToMany(mappedBy = "institute", orphanRemoval = true)
	private Set<Event> events = new HashSet<>();

	public Institute() {
	
	}

	public Institute(Integer id, String name, InstituteType institutionType, Address address, Contact contact) {
		this(id, name, institutionType);
		this.address = address;
		this.contact = contact;
	}

	public Institute(Integer id, String name, InstituteType instituteType) {
		super();
		this.id = id;
		this.name = name;
		this.instituteType = instituteType;
	}

	public Institute(Integer id, String name, InstituteType instituteType, String recognitionNumber, Date openTiming, Date closeTiming, Address address, Contact contact) {
		this(id, name, instituteType, address, contact);
		this.recognitionNumber = recognitionNumber;
		this.openTime = openTiming;
		this.closeTime = closeTiming;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InstituteType getInstituteType() {
		return instituteType;
	}

	public void setInstituteType(InstituteType instituteType) {
		this.instituteType = instituteType;
	}

	public String getRecognitionNumber() {
		return recognitionNumber;
	}

	public void setRecognitionNumber(String recognitionNumber) {
		this.recognitionNumber = recognitionNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Institute institute = (Institute) o;
		return Objects.equals(id, institute.id) &&
				Objects.equals(name, institute.name) &&
				instituteType == institute.instituteType &&
				Objects.equals(recognitionNumber, institute.recognitionNumber) &&
				Objects.equals(address, institute.address) &&
				Objects.equals(contact, institute.contact);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, instituteType, recognitionNumber, address, contact);
	}

	public Set<InstituteClass> getClasses() {
		return classes;
	}

	public void setClasses(Set<InstituteClass> classes) {
		classes.forEach(this::addClasses);
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Set<InstituteDepartment> getInstituteDepartments() {
		return instituteDepartments;
	}

	public void setInstituteDepartments(Set<InstituteDepartment> instituteDepartments) {
		instituteDepartments.forEach(this::addInstituteDepartments);
	}

	public void addInstituteDepartments(InstituteDepartment instituteDepartments) {
		this.instituteDepartments.add(instituteDepartments);
		instituteDepartments.setInstitute(this);

	}

	public List<InstituteDocumentStore> getInstituteDocumentStores() {
		return instituteDocumentStores;
	}

	public void setInstituteDocumentStores(InstituteDocumentStore instituteDocumentStore) {
		this.instituteDocumentStores.add(instituteDocumentStore);
		instituteDocumentStore.setInstitute(this);
	}

	public void setInstituteDocumentStores(List<InstituteDocumentStore> instituteDocumentStores) {
		instituteDocumentStores.forEach(this::setInstituteDocumentStores);
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		employees.forEach(this::setEmployee);
	}

	public void setEmployee(Employee employee) {
		employees.add(employee);
		employee.setInstitute(this);
	}

	@Override
	public String toString() {
		return "Institute{" +
				"id=" + id +
				", name='" + name + '\'' +
				", instituteType=" + instituteType +
				", recognitionNumber='" + recognitionNumber + '\'' +
				", openTime=" + openTime +
				", closeTime=" + closeTime +
				", address=" + address +
				", contact=" + contact +
				'}';
	}

	public void addClasses(InstituteClass instituteClass) {
		this.classes.add(instituteClass);
		instituteClass.setInstitute(this);
	}

	public Set<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(Set<Holiday> holidays) {
		holidays.forEach(this::setHolidays);
	}

	public void setHolidays(Holiday holiday) {
		this.holidays.add(holiday);
		holiday.setInstitute(this);
	}

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		events.forEach(this::setEvents);
	}

	public void setEvents(Event event) {
		this.events.add(event);
		event.setInstitute(this);
	}
}
