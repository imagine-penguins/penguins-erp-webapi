package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "holidays")
public class Holiday {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "holiday_list", nullable = false)
    private Date holidayDate;

    @Column(name = "occasion",  nullable = false)
    private String occasion;

    @Column(name = "desc")
    private String desc;

    @ManyToOne(optional = false)
    @JoinColumn(name = "institute_id", nullable = false, updatable = false)
    private Institute institute;

    public Holiday(Long id, Date holidayDate, String occasion, String desc) {
        this.id = id;
        this.holidayDate = holidayDate;
        this.occasion = occasion;
        this.desc = desc;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getHolidayDate() {
        return holidayDate;
    }

    public void setHolidayDate(Date holidayDate) {
        this.holidayDate = holidayDate;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}
