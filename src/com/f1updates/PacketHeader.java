package com.f1updates;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;

public class PacketHeader {
    int packetFormat;             // 2020
    int gameMajorVersion;         // Game major version - "X.00"
    int gameMinorVersion;         // Game minor version - "1.XX"
    int packetVersion;            // Version of this packet type, all start from 1

    enum PacketID {
        Motion,
        Session,
        LapData,
        Event,
        Participants,
        CarSetups,
        CarTelemetry,
        CarStatus,
        FinalClassifciation,
        LobbyInfo
    }

    PacketID packetId;                 // Identifier for the packet type, see below
    long sessionUID;               // Unique identifier for the session
    float sessionTime;              // Session timestamp - 4 bytes?
    int frameIdentifier;          // Identifier for the frame the data was retrieved on
    int playerCarIndex;           // Index of player's car in the array

    // ADDED IN BETA 2:
    int secondaryPlayerCarIndex;  // Index of secondary player's car in the array (splitscreen)
    // 255 if no second player

    public PacketHeader(int packetFormat,
                        int gameMajorVersion,
                        int gameMinorVersion,
                        int packetVersion,
                        PacketID packetId,
                        long sessionUID,
                        float sessionTime,
                        int frameIdentifier,
                        int playerCarIndex,
                        int secondaryPlayerCarIndex) {
        this.packetFormat = packetFormat;
        this.gameMajorVersion = gameMajorVersion;
        this.gameMinorVersion = gameMinorVersion;
        this.packetVersion = packetVersion;
        this.packetId = packetId;
        this.sessionUID = sessionUID;
        this.sessionTime = sessionTime;
        this.frameIdentifier = frameIdentifier;
        this.playerCarIndex = playerCarIndex;
        this.secondaryPlayerCarIndex = secondaryPlayerCarIndex;
    }

    public static int Size() {
        return
                2 + //uint16    m_packetFormat;             // 2020
                        1 + //uint8     m_gameMajorVersion;         // Game major version - "X.00"
                        1 + //uint8     m_gameMinorVersion;         // Game minor version - "1.XX"
                        1 + //uint8     m_packetVersion;            // Version of this packet type, all start from 1
                        1 + //uint8     m_packetId;                 // Identifier for the packet type, see below
                        8 + //uint64    m_sessionUID;               // Unique identifier for the session
                        4 + //float     m_sessionTime;              // Session timestamp
                        4 + //uint32    m_frameIdentifier;          // Identifier for the frame the data was retrieved on
                        1 + //uint8     m_playerCarIndex;           // Index of player's car in the array
//
                        //// ADDED IN BETA 2:
                        1; //uint8     m_secondaryPlayerCarIndex;  // Index of secondary player's car in the array (splitscreen)
        //// 255 if no second player
    }

    public static Optional<PacketHeader> ParsePacketHeader(ByteBuffer buf) {
        buf = buf.order(ByteOrder.LITTLE_ENDIAN);
        try {
            int packetFormat = buf.getShort() & 0xFFFF;
            int gameMajorVersion = buf.get() & 0xFF;
            int gameMinorVersion = buf.get() & 0xFF;
            int packetVersion = buf.get() & 0xFF;
            int packetIdInt = buf.get() & 0xFF;
            PacketID packetId = PacketID.values()[packetIdInt];
            long sessionUID = buf.getLong();
            float sessionTime = buf.getFloat();
            int frameIdentifier = buf.getInt();
            int playerCarIndex = buf.get() & 0xFF;
            int secondaryPlayerCarIndex = buf.get() & 0xFF;


            return Optional.of(new PacketHeader(packetFormat,
                    gameMajorVersion,
                    gameMinorVersion,
                    packetVersion,
                    packetId,
                    sessionUID,
                    sessionTime,
                    frameIdentifier,
                    playerCarIndex,
                    secondaryPlayerCarIndex));
        } catch (Exception e) {
            System.out.println("Got exception" + e.getMessage());
            return null;
        }
    }

    public String toString() {
        return "PacketHeader - " +
                " packetFormat:" + packetFormat +
                " gameMajorVersion:" + gameMajorVersion +
                " gameMinorVersion:" + gameMinorVersion +
                " packetVersion:" + packetVersion +
                " packetId:" + packetId +
                " sessionUID:" + sessionUID +
                " sessionTime:" + sessionTime +
                " frameIdentifier:" + frameIdentifier +
                " playerCarIndex:" + playerCarIndex +
                " secondaryPlayerCarIndex:" + secondaryPlayerCarIndex;
    }

}
