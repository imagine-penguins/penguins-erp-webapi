package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

import com.knackitsolutions.crm.imaginepenguins.dbservice.listner.InstituteListner;
import com.knackitsolutions.crm.imaginepenguins.dbservice.constant.InstituteType;

import java.util.Objects;
import java.util.Set;

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

	String recognitionNumber;

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

	@OneToMany(mappedBy = "institute")
	private Set<InstituteClass> classes;

	@OneToMany(mappedBy = "institute", fetch = FetchType.LAZY)
	private Set<InstituteDepartment> instituteDepartments;

	public Institute() {
	
	}
	
	public Institute(String recognitionNumber, String name, InstituteType institutionType) {
		this.recognitionNumber = recognitionNumber;
		this.name = name;
		this.instituteType = institutionType;
	}

	public Institute(String name, InstituteType institutionType) {
		this.name = name;
		this.instituteType = institutionType;
	}

	public Institute(String name, InstituteType institutionType, Address address, Contact contact) {
		this.name = name;
		this.instituteType = institutionType;
		this.address = address;
		this.contact = contact;
	}

	public Institute(Integer id, String name, InstituteType institutionType, Address address, Contact contact) {
		this.id = id;
		this.name = name;
		this.instituteType = institutionType;
		this.address = address;
		this.contact = contact;
	}

	public Institute(Integer id, String name, InstituteType instituteType) {
		super();
		this.id = id;
		this.name = name;
		this.instituteType = instituteType;
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

	@Override
	public String toString() {
		return "Institute{" +
				"id=" + id +
				", name='" + name + '\'' +
				", instituteType=" + instituteType +
				", recognitionNumber='" + recognitionNumber + '\'' +
				", address=" + address +
				", contact=" + contact +
				'}';
	}

	public Set<InstituteClass> getClasses() {
		return classes;
	}

	public void setClasses(Set<InstituteClass> classes) {
		this.classes = classes;
	}
}
