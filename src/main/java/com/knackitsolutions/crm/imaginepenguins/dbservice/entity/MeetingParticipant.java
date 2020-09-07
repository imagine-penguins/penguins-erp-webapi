package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "meeting_participants")
public class MeetingParticipant {

    @EmbeddedId
    MeetingParticipantKey meetingParticipantKey;

    @ManyToOne(optional = false)
    @JoinColumn(name = "meeting_id")
    @MapsId("meetingId")
    private Meeting meeting;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id")
    @MapsId("participantId")
    private User participant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "organizer")
    @MapsId("organizerId")
    private User organizer;

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public MeetingParticipantKey getMeetingParticipantKey() {
        return meetingParticipantKey;
    }

    public void setMeetingParticipantKey(MeetingParticipantKey meetingParticipantKey) {
        this.meetingParticipantKey = meetingParticipantKey;
    }

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }
}
