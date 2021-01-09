package com.f1updates;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Vector;

class Participant {

    int m_aiControlled;           // uint8 Whether the vehicle is AI (1) or Human (0) controlled
    int m_driverId;               // uint8 Driver id - see appendix
    int m_teamId;                 // uint8 Team id - see appendix
    int m_raceNumber;             // uint8 Race number of the car
    int m_nationality;            // uint8 Nationality of the driver
    String m_name;                // char[48] Name of participant in UTF-8 format – null terminated
    // Will be truncated with … (U+2026) if too long
    int m_yourTelemetry;          // uint8 The player's UDP setting, 0 = restricted, 1 = public

    public Participant(int m_aiControlled,
                       int m_driverId,
                       int m_teamId,
                       int m_raceNumber,
                       int m_nationality,
                       String m_name,
                       int m_yourTelemetry) {
        this.m_aiControlled = m_aiControlled;
        this.m_driverId = m_driverId;
        this.m_teamId = m_teamId;
        this.m_raceNumber = m_raceNumber;
        this.m_nationality = m_nationality;
        this.m_name = m_name;
        this.m_yourTelemetry = m_yourTelemetry;
    }

    public static int Size() {
        return
                1 + // m_aiControlled uint8 Whether the vehicle is AI (1) or Human (0) controlled
                        1 + // m_driverId uint8 Driver id - see appendix
                        1 + // m_teamId uint8 Team id - see appendix
                        1 + // m_raceNumber uint8 Race number of the car
                        1 + // m_nationality uint8 Nationality of the driver
                        48 + // m_name char[48] Name of participant in UTF-8 format – null terminated
                        1; // m_yourTelemetry uint8 The player's UDP setting, 0 = restricted, 1 = public

    }

    public static Optional<Participant> Parse(ByteBuffer buf) {
        try {
            int m_aiControlled = buf.get() & 0xFF;
            int m_driverId = buf.get() & 0xFF;
            int m_teamId = buf.get() & 0xFF;
            int m_raceNumber = buf.get() & 0xFF;
            int m_nationality = buf.get() & 0xFF;
            byte[] m_name_buffer = new byte[48];
            buf.get(m_name_buffer, 0, 48);
            String m_name = new String(m_name_buffer);
            int m_yourTelemetry = buf.get() & 0xFF;
            return Optional.of(new Participant(m_aiControlled,
                    m_driverId,
                    m_teamId,
                    m_raceNumber,
                    m_nationality,
                    m_name,
                    m_yourTelemetry));
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return "Participant { " +
                " m_aiControlled:" + this.m_aiControlled +
                " m_driverId:" + this.m_driverId +
                " m_teamId:" + this.m_teamId +
                " m_raceNumber:" + this.m_raceNumber +
                " m_nationality:" + this.m_nationality +
                " m_name:" + this.m_name +
                " m_yourTelemetry:" + this.m_yourTelemetry +
                "}";
    }

}

public class Participants {

    Vector<Participant> participants;

    public Participants(Vector<Participant> participants) {
        this.participants = participants;
    }

    public static int Size() {
        return 1 + // uint8           m_numActiveCars;	// Number of active cars in the data – should match number of

                (22 * Participant.Size()); //  ParticipantData m_participants[22];
    }

    public static Optional<Participants> Parse(ByteBuffer buf) {
        try {
            int numOfParticipants = buf.get() & 0xFF;

            Vector<Participant> participants = new Vector<>();
            for (int i = 0; i < 22; ++i) {
                Optional<Participant> participant = Participant.Parse(buf);
                if (participant == null) {
                    return null;
                } else {
                    participants.add(participant.get());
                }
            }

            return Optional.of(new Participants(participants));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        String toPrint = "Participants - ";

        for (Participant participant : participants) {
            toPrint += participant.toString();
        }

        return toPrint;
    }

}
