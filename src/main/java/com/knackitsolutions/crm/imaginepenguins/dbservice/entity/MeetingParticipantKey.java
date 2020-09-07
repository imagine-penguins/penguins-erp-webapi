package com.knackitsolutions.crm.imaginepenguins.dbservice.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MeetingParticipantKey implements Serializable {

    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "organizer_id")
    private Long organizerId;

    @Column(name = "participant_id")
    private Long participantId;

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeetingParticipantKey)) return false;
        MeetingParticipantKey that = (MeetingParticipantKey) o;
        return getMeetingId().equals(that.getMeetingId()) &&
                getOrganizerId().equals(that.getOrganizerId()) &&
                getParticipantId().equals(that.getParticipantId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMeetingId(), getOrganizerId(), getParticipantId());
    }
}
