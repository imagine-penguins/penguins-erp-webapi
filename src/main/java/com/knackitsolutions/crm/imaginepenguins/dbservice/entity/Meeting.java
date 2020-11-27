package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "meetings")
@NoArgsConstructor
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "agenda")
    private String agenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizer")
    private User createdBy;

    @OneToMany(mappedBy = "meeting")
    private List<MeetingParticipant> participants;

    @ManyToOne(optional = false)
    @JoinColumn(name = "institute_id", nullable = false, updatable = false)
    private Institute institute;

    public Meeting(Long id, Date from, Date to, String agenda, User createdBy, List<MeetingParticipant> participants) {
        this.id = id;
        this.startTime = from;
        this.endTime = to;
        this.agenda = agenda;
        this.createdBy = createdBy;
        this.participants = participants;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public List<MeetingParticipant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MeetingParticipant> participants) {
        this.participants = participants;
    }

    public Institute getInstitute() {
        return institute;
    }

    public void setInstitute(Institute institute) {
        this.institute = institute;
    }
}
