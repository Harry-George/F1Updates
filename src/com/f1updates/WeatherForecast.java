package com.f1updates;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class WeatherForecast {
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

    enum Weather {
        clear,
        light_cloud,
        overcast,
        light_rain,
        heavy_rain,
        storm
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

    public static Optional<WeatherForecast> ParseWeather(ByteBuffer buf) {
        try {
            SessionType sessionType = SessionType.values()[buf.getChar()];
            int minutesAhead = buf.getChar() & 0xFF;
            Weather weather = Weather.values()[buf.getChar()];
            int trackTemp = buf.getChar() & 0xFF;
            int airTemp = buf.getChar() & 0xFF;
            return Optional.of(new WeatherForecast(sessionType,
                    minutesAhead,
                    weather,
                    trackTemp,
                    airTemp));
        } catch (Exception e) {
            return null;
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
