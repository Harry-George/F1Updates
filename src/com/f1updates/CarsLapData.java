package com.f1updates;

import javafx.util.Pair;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Vector;

import static java.lang.Integer.max;

class CarLapData {

    float m_lastLapTime;               // Last lap time in seconds
    float m_currentLapTime;            // Current time around the lap in seconds

    //UPDATED in Beta 3:
    int m_sector1TimeInMS;           // uint16 Sector 1 time in milliseconds
    int m_sector2TimeInMS;           // uint16 Sector 2 time in milliseconds
    float m_bestLapTime;               // Best lap time of the session in seconds
    int m_bestLapNum;                // uint8 Lap number best time achieved on
    int m_bestLapSector1TimeInMS;    // uint16 Sector 1 time of best lap in the session in milliseconds
    int m_bestLapSector2TimeInMS;    // uint16 Sector 2 time of best lap in the session in milliseconds
    int m_bestLapSector3TimeInMS;    // uint16 Sector 3 time of best lap in the session in milliseconds
    int m_bestOverallSector1TimeInMS;// uint16 Best overall sector 1 time of the session in milliseconds
    int m_bestOverallSector1LapNum;  // uint8 Lap number best overall sector 1 time achieved on
    int m_bestOverallSector2TimeInMS;// uint16 Best overall sector 2 time of the session in milliseconds
    int m_bestOverallSector2LapNum;  // uint8 Lap number best overall sector 2 time achieved on
    int m_bestOverallSector3TimeInMS;// uint16 Best overall sector 3 time of the session in milliseconds
    int m_bestOverallSector3LapNum;  // uint8 Lap number best overall sector 3 time achieved on


    float m_lapDistance;               // Distance vehicle is around current lap in metres – could
    // be negative if line hasn’t been crossed yet
    float m_totalDistance;             // Total distance travelled in session in metres – could
    // be negative if line hasn’t been crossed yet
    float m_safetyCarDelta;            // Delta in seconds for safety car
    int m_carPosition;               // uint8 Car race position
    int m_currentLapNum;             // uint8 Current lap number
    int m_pitStatus;                 // uint8 0 = none, 1 = pitting, 2 = in pit area
    int m_sector;                    // uint8 0 = sector1, 1 = sector2, 2 = sector3
    int m_currentLapInvalid;         // uint8 Current lap invalid - 0 = valid, 1 = invalid
    int m_penalties;                 // uint8 Accumulated time penalties in seconds to be added
    int m_gridPosition;              // uint8 Grid position the vehicle started the race in
    int m_driverStatus;              // uint8 Status of driver - 0 = in garage, 1 = flying lap
    // 2 = in lap, 3 = out lap, 4 = on track
    int m_resultStatus;              // uint8 Result status - 0 = invalid, 1 = inactive, 2 = active

    // 3 = finished, 4 = disqualified, 5 = not classified
    // 6 = retired
    public CarLapData(float m_lastLapTime,
                      float m_currentLapTime,
                      int m_sector1TimeInMS,
                      int m_sector2TimeInMS,
                      float m_bestLapTime,
                      int m_bestLapNum,
                      int m_bestLapSector1TimeInMS,
                      int m_bestLapSector2TimeInMS,
                      int m_bestLapSector3TimeInMS,
                      int m_bestOverallSector1TimeInMS,
                      int m_bestOverallSector1LapNum,
                      int m_bestOverallSector2TimeInMS,
                      int m_bestOverallSector2LapNum,
                      int m_bestOverallSector3TimeInMS,
                      int m_bestOverallSector3LapNum,
                      float m_lapDistance,
                      float m_totalDistance,
                      float m_safetyCarDelta,
                      int m_carPosition,
                      int m_currentLapNum,
                      int m_pitStatus,
                      int m_sector,
                      int m_currentLapInvalid,
                      int m_penalties,
                      int m_gridPosition,
                      int m_driverStatus,
                      int m_resultStatus) {
        this.m_lastLapTime = m_lastLapTime;
        this.m_currentLapTime = m_currentLapTime;
        this.m_sector1TimeInMS = m_sector1TimeInMS;
        this.m_sector2TimeInMS = m_sector2TimeInMS;
        this.m_bestLapTime = m_bestLapTime;
        this.m_bestLapNum = m_bestLapNum;
        this.m_bestLapSector1TimeInMS = m_bestLapSector1TimeInMS;
        this.m_bestLapSector2TimeInMS = m_bestLapSector2TimeInMS;
        this.m_bestLapSector3TimeInMS = m_bestLapSector3TimeInMS;
        this.m_bestOverallSector1TimeInMS = m_bestOverallSector1TimeInMS;
        this.m_bestOverallSector1LapNum = m_bestOverallSector1LapNum;
        this.m_bestOverallSector2TimeInMS = m_bestOverallSector2TimeInMS;
        this.m_bestOverallSector2LapNum = m_bestOverallSector2LapNum;
        this.m_bestOverallSector3TimeInMS = m_bestOverallSector3TimeInMS;
        this.m_bestOverallSector3LapNum = m_bestOverallSector3LapNum;
        this.m_lapDistance = m_lapDistance;
        this.m_totalDistance = m_totalDistance;
        this.m_safetyCarDelta = m_safetyCarDelta;
        this.m_carPosition = m_carPosition;
        this.m_currentLapNum = m_currentLapNum;
        this.m_pitStatus = m_pitStatus;
        this.m_sector = m_sector;
        this.m_currentLapInvalid = m_currentLapInvalid;
        this.m_penalties = m_penalties;
        this.m_gridPosition = m_gridPosition;
        this.m_driverStatus = m_driverStatus;
        this.m_resultStatus = m_resultStatus;
    }

//    public static int Size() {
//        return
//                1 + // m_aiControlled uint8 Whether the vehicle is AI (1) or Human (0) controlled
//                        1 + // m_driverId uint8 Driver id - see appendix
//                        1 + // m_teamId uint8 Team id - see appendix
//                        1 + // m_raceNumber uint8 Race number of the car
//                        1 + // m_nationality uint8 Nationality of the driver
//                        48 + // m_name char[48] Name of lapData in UTF-8 format – null terminated
//                        1; // m_yourTelemetry uint8 The player's UDP setting, 0 = restricted, 1 = public
//
//    }

    public static Optional<CarLapData> Parse(ByteBuffer buf) {
        try {
            float m_lastLapTime = buf.getFloat();               // Last lap time in seconds
            float m_currentLapTime = buf.getFloat();            // Current time around the lap in seconds

            //UPDATED in Beta 3:
            int m_sector1TimeInMS = buf.getShort() & 0xFFFF;           // uint16 Sector 1 time in milliseconds
            int m_sector2TimeInMS = buf.getShort() & 0xFFFF;           // uint16 Sector 2 time in milliseconds
            float m_bestLapTime = buf.getFloat();               // Best lap time of the session in seconds
            int m_bestLapNum = buf.get() & 0xFF;                // uint8 Lap number best time achieved on
            int m_bestLapSector1TimeInMS = buf.getShort() & 0xFFFF;    // uint16 Sector 1 time of best lap in the session in milliseconds
            int m_bestLapSector2TimeInMS = buf.getShort() & 0xFFFF;    // uint16 Sector 2 time of best lap in the session in milliseconds
            int m_bestLapSector3TimeInMS = buf.getShort() & 0xFFFF;    // uint16 Sector 3 time of best lap in the session in milliseconds
            int m_bestOverallSector1TimeInMS = buf.getShort() & 0xFFFF;// uint16 Best overall sector 1 time of the session in milliseconds
            int m_bestOverallSector1LapNum = buf.get() & 0xFF;  // uint8 Lap number best overall sector 1 time achieved on
            int m_bestOverallSector2TimeInMS = buf.getShort() & 0xFFFF;// uint16 Best overall sector 2 time of the session in milliseconds
            int m_bestOverallSector2LapNum = buf.get() & 0xFF;  // uint8 Lap number best overall sector 2 time achieved on
            int m_bestOverallSector3TimeInMS = buf.getShort() & 0xFFFF;// uint16 Best overall sector 3 time of the session in milliseconds
            int m_bestOverallSector3LapNum = buf.get() & 0xFF;  // uint8 Lap number best overall sector 3 time achieved on


            float m_lapDistance = buf.getFloat();               // Distance vehicle is around current lap in metres – could
            // be negative if line hasn’t been crossed yet
            float m_totalDistance = buf.getFloat();             // Total distance travelled in session in metres – could
            // be negative if line hasn’t been crossed yet
            float m_safetyCarDelta = buf.getFloat();            // Delta in seconds for safety car
            int m_carPosition = buf.get() & 0xFF;               // uint8 Car race position
            int m_currentLapNum = buf.get() & 0xFF;             // uint8 Current lap number
            int m_pitStatus = buf.get() & 0xFF;                 // uint8 0 = none, 1 = pitting, 2 = in pit area
            int m_sector = buf.get() & 0xFF;                    // uint8 0 = sector1, 1 = sector2, 2 = sector3
            int m_currentLapInvalid = buf.get() & 0xFF;         // uint8 Current lap invalid - 0 = valid, 1 = invalid
            int m_penalties = buf.get() & 0xFF;                 // uint8 Accumulated time penalties in seconds to be added
            int m_gridPosition = buf.get() & 0xFF;              // uint8 Grid position the vehicle started the race in
            int m_driverStatus = buf.get() & 0xFF;              // uint8 Status of driver - 0 = in garage, 1 = flying lap
            // 2 = in lap, 3 = out lap, 4 = on track
            int m_resultStatus = buf.get() & 0xFF;              // uint8 Result status - 0 = invalid, 1 = inactive, 2 = active
            return Optional.of(new CarLapData(m_lastLapTime,
                    m_currentLapTime,
                    m_sector1TimeInMS,
                    m_sector2TimeInMS,
                    m_bestLapTime,
                    m_bestLapNum,
                    m_bestLapSector1TimeInMS,
                    m_bestLapSector2TimeInMS,
                    m_bestLapSector3TimeInMS,
                    m_bestOverallSector1TimeInMS,
                    m_bestOverallSector1LapNum,
                    m_bestOverallSector2TimeInMS,
                    m_bestOverallSector2LapNum,
                    m_bestOverallSector3TimeInMS,
                    m_bestOverallSector3LapNum,
                    m_lapDistance,
                    m_totalDistance,
                    m_safetyCarDelta,
                    m_carPosition,
                    m_currentLapNum,
                    m_pitStatus,
                    m_sector,
                    m_currentLapInvalid,
                    m_penalties,
                    m_gridPosition,
                    m_driverStatus,
                    m_resultStatus));
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return "CarLapData { " +
                "TODO" +
                "}";
    }

}

public class CarsLapData {

    Vector<CarLapData> carsLapData;
   static Map<Integer, Integer> deltas;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[33m";


    public CarsLapData(Vector<CarLapData> carsLapData) {
        this.carsLapData = carsLapData;
    }

//    public static int Size() {
//        return 1 + // uint8           m_numActiveCars;	// Number of active cars in the data – should match number of
//
//                (22 * CarLapData.Size()); //  CarLapDataData m_carsLapData[22];
//    }

    public static Optional<CarsLapData> Parse(ByteBuffer buf) {
        try {

            Vector<CarLapData> carsLapData = new Vector<>();
            for (int i = 0; i < 22; ++i) {
                Optional<CarLapData> lapData = CarLapData.Parse(buf);
                if (!lapData.isPresent()) {
                    return Optional.empty();
                } else {
                    carsLapData.add(lapData.get());
                }
            }

            return Optional.of(new CarsLapData(carsLapData));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        String toPrint = "CarsLapData - ";

        for (CarLapData lapData : carsLapData) {
            toPrint += lapData.toString();
        }

        return toPrint;
    }

    // Returns map of position -> string representaion of delta
    public Map<Integer, String> CalculateDeltas(Integer lapLength) {

        Map<Integer, CarLapData> internal = new TreeMap<>();

        int maxPosition = 0;
        for (CarLapData lapData : carsLapData) {
            if (lapData.m_carPosition > 0) {
                internal.put(lapData.m_carPosition, lapData);
                maxPosition = max(lapData.m_carPosition, maxPosition);
            }
        }

        Map<Integer, Integer> deltas = new TreeMap<>();
        deltas.put(1, null);
        deltas.put(1, Math.round((internal.get(maxPosition).m_totalDistance + lapLength) - internal.get(1).m_totalDistance));
        for (int i = 2; i <= maxPosition; ++i) {
            CarLapData previous = internal.get(i - 1);
            CarLapData cur = internal.get(i);
            deltas.put(i, Math.round(previous.m_totalDistance - cur.m_totalDistance));
        }


        Map<Integer, String> stringDeltas = new TreeMap<>();

//        stringDeltas.put(1, "--");

        DecimalFormat lapFractionFormat = new DecimalFormat(" 000.00");
        for (int i = 1; i <= maxPosition; ++i) {
            String deltaStr = "";
            if (this.deltas != null) {
                Integer curDelta = deltas.get(i);
                Integer lastDelta = this.deltas.get(i);
                Integer deltaDelta = curDelta - lastDelta;
                deltaStr = lapFractionFormat.format((float)(curDelta * 100) / lapLength) + "%(change:";
                if (deltaDelta > 0) {
                    deltaStr += ANSI_RED;
                } else if (deltaDelta < 0) {
                    deltaStr += ANSI_GREEN;
                } else {
                    deltaStr += ANSI_BLUE;
                }
                deltaStr += deltaDelta + ANSI_RESET + "M)";
            }
            stringDeltas.put(i, deltaStr);
        }

        this.deltas = deltas;

        return stringDeltas;

    }

}
