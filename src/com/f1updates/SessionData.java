package com.f1updates;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Vector;

enum Weather {
    clear,
    light_cloud,
    overcast,
    light_rain,
    heavy_rain,
    storm
}

class WeatherForecast {
    enum SessionType {
        unknown,
        P1,
        P2,
        P3,
        Short_P,
        Q1,
        Q2,
        Q3,
        Short_Q,
        OSQ,
        R,
        R2,
        Time_Trial
    }

    SessionType sessionType;
    int minutesAhead;
    Weather weather;
    int trackTemp;
    int airTemp;

    public WeatherForecast(SessionType sessionType, int minutesAhead, Weather weather, int trackTemp, int airTemp) {
        this.sessionType = sessionType;
        this.minutesAhead = minutesAhead;
        this.weather = weather;
        this.trackTemp = trackTemp;
        this.airTemp = airTemp;
    }

    public static int Size() {
        return
                1 +  //        uint8 m_sessionType;
                        1 +  //        uint8 m_timeOffset;
                        1 +  //        uint8 m_weather;
                        1 +  //        int8 m_trackTemperature;
                        1;  //        int8 m_airTemperature;

    }

    public static Optional<WeatherForecast> Parse(ByteBuffer buf) {
        try {
            SessionType sessionType = SessionType.values()[buf.get() & 0xFF];
            int minutesAhead = buf.get() & 0xFF;
            Weather weather = Weather.values()[buf.get() & 0xFF];
            int trackTemp = buf.get();
            int airTemp = buf.get();
            return Optional.of(new WeatherForecast(sessionType,
                    minutesAhead,
                    weather,
                    trackTemp,
                    airTemp));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        return "Weather - sessionType:" + sessionType.toString() +
                " minutesAhead:" + minutesAhead +
                " weather:" + weather.toString() +
                " trackTemp:" + trackTemp +
                " airTemp:" + airTemp;
    }

}

class MarshalZone {
    float m_zoneStart;   // float Fraction (0..1) of way through the lap the marshal zone starts
    int m_zoneFlag;    // int8 -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red

    public MarshalZone(float m_zoneStart,
                       int m_zoneFlag) {
        this.m_zoneStart = m_zoneStart;
        this.m_zoneFlag = m_zoneFlag;
    }

    public static int Size() {
        return
                4 +  // float Fraction (0..1) of way through the lap the marshal zone starts
                        1;  // int8 -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
    }

    public static Optional<MarshalZone> Parse(ByteBuffer buf) {
        try {
            float m_zoneStart = buf.getFloat();
            int m_zoneFlag = buf.get() & 0xFF;
            return Optional.of(new MarshalZone(m_zoneStart, m_zoneFlag));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        return "TODO";
    }

}


public class SessionData {

    Weather m_weather;                   // uint8 Weather - 0 = clear, 1 = light cloud, 2 = overcast
    // 3 = light rain, 4 = heavy rain, 5 = storm
    int m_trackTemperature;          // int8 Track temp. in degrees celsius
    int m_airTemperature;            // int8 Air temp. in degrees celsius
    int m_totalLaps;                 // uint8 Total number of laps in this race
    int m_trackLength;               // uint16 Track length in metres
    int m_sessionType;               // uint8 0 = unknown, 1 = P1, 2 = P2, 3 = P3, 4 = Short P
    // 5 = Q1, 6 = Q2, 7 = Q3, 8 = Short Q, 9 = OSQ
    // 10 = R, 11 = R2, 12 = Time Trial
    int m_trackId;                   // int8 -1 for unknown, 0-21 for tracks, see appendix
    int m_formula;                   // uint8 Formula, 0 = F1 Modern, 1 = F1 Classic, 2 = F2,
    // 3 = F1 Generic
    int m_sessionTimeLeft;           // uint16 Time left in session in seconds
    int m_sessionDuration;           // uint16 Session duration in seconds
    int m_pitSpeedLimit;             // uint8 Pit speed limit in kilometres per hour
    int m_gamePaused;                // uint8 Whether the game is paused
    int m_isSpectating;              // uint8 Whether the player is spectating
    int m_spectatorCarIndex;         // uint8 Index of the car being spectated
    int m_sliProNativeSupport;     // uint8 SLI Pro support, 0 = inactive, 1 = active
    int m_numMarshalZones;           // uint8 Number of marshal zones to follow
    MarshalZone[] m_marshalZones;          // MarshalZone[21] List of marshal zones – max 21
    int m_safetyCarStatus;           // uint8 0 = no safety car, 1 = full safety car
    // 2 = virtual safety car
    int m_networkGame;               // uint8 0 = offline, 1 = online
    int m_numWeatherForecastSamples; // uint8 Number of weather samples to follow
    Vector<WeatherForecast> m_weatherForecastSamples;   // m_weatherForecastSamples[20] Array of weather forecast samples

    public SessionData(Weather m_weather,
                       int m_trackTemperature,
                       int m_airTemperature,
                       int m_totalLaps,
                       int m_trackLength,
                       int m_sessionType,
                       int m_trackId,
                       int m_formula,
                       int m_sessionTimeLeft,
                       int m_sessionDuration,
                       int m_pitSpeedLimit,
                       int m_gamePaused,
                       int m_isSpectating,
                       int m_spectatorCarIndex,
                       int m_sliProNativeSupport,
                       int m_numMarshalZones,
                       MarshalZone[] m_marshalZones,
                       int m_safetyCarStatus,
                       int m_networkGame,
                       int m_numWeatherForecastSamples,
                       Vector<WeatherForecast> m_weatherForecastSamples) {

        this.m_weather = m_weather;
        this.m_trackTemperature = m_trackTemperature;
        this.m_airTemperature = m_airTemperature;
        this.m_totalLaps = m_totalLaps;
        this.m_trackLength = m_trackLength;
        this.m_sessionType = m_sessionType;
        this.m_trackId = m_trackId;
        this.m_formula = m_formula;
        this.m_sessionTimeLeft = m_sessionTimeLeft;
        this.m_sessionDuration = m_sessionDuration;
        this.m_pitSpeedLimit = m_pitSpeedLimit;
        this.m_gamePaused = m_gamePaused;
        this.m_isSpectating = m_isSpectating;
        this.m_spectatorCarIndex = m_spectatorCarIndex;
        this.m_sliProNativeSupport = m_sliProNativeSupport;
        this.m_numMarshalZones = m_numMarshalZones;
        this.m_marshalZones = m_marshalZones;
        this.m_safetyCarStatus = m_safetyCarStatus;
        this.m_networkGame = m_networkGame;
        this.m_numWeatherForecastSamples = m_numWeatherForecastSamples;
        this.m_weatherForecastSamples = m_weatherForecastSamples;

    }

//    public static int Size() {
//        return 1 + // uint8           m_numActiveCars;	// Number of active cars in the data – should match number of
//
//                (22 * Participant.Size()); //  ParticipantData m_participants[22];
//    }

    public static Optional<SessionData> Parse(ByteBuffer buf) {
        try {
            Weather m_weather = Weather.values()[buf.get() & 0xFF];   // uint8 Weather - 0 = clear, 1 = light cloud, 2 = overcast
            // 3 = light rain, 4 = heavy rain, 5 = storm
            int m_trackTemperature = buf.get();          // int8 Track temp. in degrees celsius
            int m_airTemperature = buf.get();            // int8 Air temp. in degrees celsius
            int m_totalLaps = buf.get() & 0xFF;                 // uint8 Total number of laps in this race
            int m_trackLength = buf.getShort();               // uint16 Track length in metres
            int m_sessionType = buf.get() & 0xFF;               // uint8 0 = unknown, 1 = P1, 2 = P2, 3 = P3, 4 = Short P
            // 5 = Q1, 6 = Q2, 7 = Q3, 8 = Short Q, 9 = OSQ
            // 10 = R, 11 = R2, 12 = Time Trial
            int m_trackId = buf.get();                   // int8 -1 for unknown, 0-21 for tracks, see appendix
            int m_formula = buf.get() & 0xFF;                   // uint8 Formula, 0 = F1 Modern, 1 = F1 Classic, 2 = F2,
            // 3 = F1 Generic
            int m_sessionTimeLeft = buf.getShort();           // uint16 Time left in session in seconds
            int m_sessionDuration = buf.getShort();           // uint16 Session duration in seconds
            int m_pitSpeedLimit = buf.get() & 0xFF;             // uint8 Pit speed limit in kilometres per hour
            int m_gamePaused = buf.get() & 0xFF;                // uint8 Whether the game is paused
            int m_isSpectating = buf.get() & 0xFF;              // uint8 Whether the player is spectating
            int m_spectatorCarIndex = buf.get() & 0xFF;         // uint8 Index of the car being spectated
            int m_sliProNativeSupport = buf.get() & 0xFF;     // uint8 SLI Pro support, 0 = inactive, 1 = active
            int m_numMarshalZones = buf.get() & 0xFF;           // uint8 Number of marshal zones to follow
            MarshalZone[] m_marshalZones = new MarshalZone[21];          // MarshalZone[21] List of marshal zones – max 21
            for (int i = 0; i < 21; ++i) {
                // TODO - check parses
                m_marshalZones[i] = MarshalZone.Parse(buf).get();
            }
            int m_safetyCarStatus = buf.get() & 0xFF;           // uint8 0 = no safety car, 1 = full safety car
            // 2 = virtual safety car
            int m_networkGame = buf.get() & 0xFF;               // uint8 0 = offline, 1 = online
            int m_numWeatherForecastSamples = buf.get() & 0xFF; // uint8 Number of weather samples to follow
            Vector<WeatherForecast> m_weatherForecastSamples = new Vector<>();   // m_weatherForecastSamples[20] Array of weather forecast samples
            for (int i = 0; i < 20; ++i) {
                // TODO - check parses
                Optional<WeatherForecast> next = WeatherForecast.Parse(buf);
                if (next.isPresent() && next.get().sessionType != WeatherForecast.SessionType.unknown) {
                    m_weatherForecastSamples.add(next.get());
                }
            }

            return Optional.of(new SessionData(m_weather,
                    m_trackTemperature,
                    m_airTemperature,
                    m_totalLaps,
                    m_trackLength,
                    m_sessionType,
                    m_trackId,
                    m_formula,
                    m_sessionTimeLeft,
                    m_sessionDuration,
                    m_pitSpeedLimit,
                    m_gamePaused,
                    m_isSpectating,
                    m_spectatorCarIndex,
                    m_sliProNativeSupport,
                    m_numMarshalZones,
                    m_marshalZones,
                    m_safetyCarStatus,
                    m_networkGame,
                    m_numWeatherForecastSamples,
                    m_weatherForecastSamples));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        String toPrint = "SessionData - TODO";
//
//        for (Participant participant : participants) {
//            toPrint += participant.toString();
//        }
//
        return toPrint;
    }

}
