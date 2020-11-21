package com.f1updates;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;
import java.util.Vector;

class CarStatus {

    int m_tractionControl;          // uint8 0 (off) - 2 (high)
    int m_antiLockBrakes;           // uint8 0 (off) - 1 (on)
    int m_fuelMix;                  // uint8 Fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
    int m_frontBrakeBias;           // uint8 Front brake bias (percentage)
    int m_pitLimiterStatus;         // uint8 Pit limiter status - 0 = off, 1 = on
    float m_fuelInTank;               // float Current fuel mass
    float m_fuelCapacity;             // float Fuel capacity
    float m_fuelRemainingLaps;        // float Fuel remaining in terms of laps (value on MFD)
    int m_maxRPM;                   // uint16 Cars max RPM, point of rev limiter
    int m_idleRPM;                  // uint16 Cars idle RPM
    int m_maxGears;                 // uint8 Maximum number of gears
    int m_drsAllowed;               // uint8 0 = not allowed, 1 = allowed, -1 = unknown

    int m_drsActivationDistance;    // uint16 0 = DRS not available, non-zero - DRS will be available in [X] metres

    int[] m_tyresWear;             // uint8[4] Tyre wear percentage
    int m_actualTyreCompound;        // uint8 F1 Modern - 16 = C5, 17 = C4, 18 = C3, 19 = C2, 20 = C1, 7 = inter, 8 = wet
    // F1 Classic - 9 = dry, 10 = wet
    // F2 – 11 = super soft, 12 = soft, 13 = medium, 14 = hard
    // 15 = wet
    int m_visualTyreCompound;        // uint8 F1 visual (can be different from actual compound)
    // 16 = soft, 17 = medium, 18 = hard, 7 = inter, 8 = wet
    // F1 Classic – same as above
    // F2 – same as above
    int m_tyresAgeLaps;             // uint8 Age in laps of the current set of tyres
    int[] m_tyresDamage;     // uint8[4] Tyre damage (percentage)
    int m_frontLeftWingDamage;      // uint8 Front left wing damage (percentage)
    int m_frontRightWingDamage;     // uint8 Front right wing damage (percentage)
    int m_rearWingDamage;           // uint8 Rear wing damage (percentage)

    int m_drsFault;                 // uint8 Indicator for DRS fault, 0 = OK, 1 = fault

    int m_engineDamage;             // uint8 Engine damage (percentage)
    int m_gearBoxDamage;            // uint8 Gear box damage (percentage)
    int m_vehicleFiaFlags;          // int8 -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
    float m_ersStoreEnergy;           // float ERS energy store in Joules
    int m_ersDeployMode;            // uint8 ERS deployment mode, 0 = none, 1 = medium, 2 = overtake, 3 = hotlap
    float m_ersHarvestedThisLapMGUK;  // float ERS energy harvested this lap by MGU-K
    float m_ersHarvestedThisLapMGUH;  // float ERS energy harvested this lap by MGU-H
    float m_ersDeployedThisLap;       // float ERS energy deployed this lap

    public CarStatus(int m_tractionControl,
                     int m_antiLockBrakes,
                     int m_fuelMix,
                     int m_frontBrakeBias,
                     int m_pitLimiterStatus,
                     float m_fuelInTank,
                     float m_fuelCapacity,
                     float m_fuelRemainingLaps,
                     int m_maxRPM,
                     int m_idleRPM,
                     int m_maxGears,
                     int m_drsAllowed,
                     int m_drsActivationDistance,
                     int[] m_tyresWear,
                     int m_actualTyreCompound,
                     int m_visualTyreCompound,
                     int m_tyresAgeLaps,
                     int[] m_tyresDamage,
                     int m_frontLeftWingDamage,
                     int m_frontRightWingDamage,
                     int m_rearWingDamage,
                     int m_drsFault,
                     int m_engineDamage,
                     int m_gearBoxDamage,
                     int m_vehicleFiaFlags,
                     float m_ersStoreEnergy,
                     int m_ersDeployMode,
                     float m_ersHarvestedThisLapMGUK,
                     float m_ersHarvestedThisLapMGUH,
                     float m_ersDeployedThisLap) {
        this.m_tractionControl = m_tractionControl;
        this.m_antiLockBrakes = m_antiLockBrakes;
        this.m_fuelMix = m_fuelMix;
        this.m_frontBrakeBias = m_frontBrakeBias;
        this.m_pitLimiterStatus = m_pitLimiterStatus;
        this.m_fuelInTank = m_fuelInTank;
        this.m_fuelCapacity = m_fuelCapacity;
        this.m_fuelRemainingLaps = m_fuelRemainingLaps;
        this.m_maxRPM = m_maxRPM;
        this.m_idleRPM = m_idleRPM;
        this.m_maxGears = m_maxGears;
        this.m_drsAllowed = m_drsAllowed;
        this.m_drsActivationDistance = m_drsActivationDistance;
        this.m_tyresWear = m_tyresWear;
        this.m_actualTyreCompound = m_actualTyreCompound;
        this.m_visualTyreCompound = m_visualTyreCompound;
        this.m_tyresAgeLaps = m_tyresAgeLaps;
        this.m_tyresDamage = m_tyresDamage;
        this.m_frontLeftWingDamage = m_frontLeftWingDamage;
        this.m_frontRightWingDamage = m_frontRightWingDamage;
        this.m_rearWingDamage = m_rearWingDamage;
        this.m_drsFault = m_drsFault;
        this.m_engineDamage = m_engineDamage;
        this.m_gearBoxDamage = m_gearBoxDamage;
        this.m_vehicleFiaFlags = m_vehicleFiaFlags;
        this.m_ersStoreEnergy = m_ersStoreEnergy;
        this.m_ersDeployMode = m_ersDeployMode;
        this.m_ersHarvestedThisLapMGUK = m_ersHarvestedThisLapMGUK;
        this.m_ersHarvestedThisLapMGUH = m_ersHarvestedThisLapMGUH;
        this.m_ersDeployedThisLap = m_ersDeployedThisLap;

    }

    public static int Size() {
        return
                1 + // uint8 0 (off) - 2 (high)
                        1 + // uint8 0 (off) - 1 (on)
                        1 + // uint8 Fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
                        1 + // uint8 Front brake bias (percentage)
                        1 + // uint8 Pit limiter status - 0 = off, 1 = on
                        4 + // float Current fuel mass
                        4 + // float Fuel capacity
                        4 + // float Fuel remaining in terms of laps (value on MFD)
                        2 + // uint16 Cars max RPM, point of rev limiter
                        2 + // uint16 Cars idle RPM
                        1 + // uint8 Maximum number of gears
                        1 + // uint8 0 = not allowed, 1 = allowed, -1 = unknown
                        2 + // uint16 0 = DRS not available, non-zero - DRS will be available in [X] metres
                        1 + // uint8[4] Tyre wear percentage
                        1 + // uint8 F1 Modern - 16 = C5, 17 = C4, 18 = C3, 19 = C2, 20 = C1, 7 = inter, 8 = wet
                        1 + // uint8 F1 visual (can be different from actual compound)
                        1 + // uint8 Age in laps of the current set of tyres
                        1 + // uint8[4] Tyre damage (percentage)
                        1 + // uint8 Front left wing damage (percentage)
                        1 + // uint8 Front right wing damage (percentage)
                        1 + // uint8 Rear wing damage (percentage)
                        1 + // uint8 Indicator for DRS fault, 0 = OK, 1 = fault
                        1 + // uint8 Engine damage (percentage)
                        1 + // uint8 Gear box damage (percentage)
                        1 + // int8 -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
                        4 + // float ERS energy store in Joules
                        1 + // uint8 ERS deployment mode, 0 = none, 1 = medium, 2 = overtake, 3 = hotlap
                        4 + // float ERS energy harvested this lap by MGU-K
                        4 + // float ERS energy harvested this lap by MGU-H
                        4;// float ERS energy deployed this lap

    }

    public static Optional<CarStatus> Parse(ByteBuffer buf) {
        try {

            int m_tractionControl = buf.get() & 0xFF;          // uint8 0 (off) - 2 (high)
            int m_antiLockBrakes = buf.get() & 0xFF;           // uint8 0 (off) - 1 (on)
            int m_fuelMix = buf.get() & 0xFF;                  // uint8 Fuel mix - 0 = lean, 1 = standard, 2 = rich, 3 = max
            int m_frontBrakeBias = buf.get() & 0xFF;           // uint8 Front brake bias (percentage)
            int m_pitLimiterStatus = buf.get() & 0xFF;         // uint8 Pit limiter status - 0 = off, 1 = on
            float m_fuelInTank = buf.getFloat();               // float Current fuel mass
            float m_fuelCapacity = buf.getFloat();             // float Fuel capacity
            float m_fuelRemainingLaps = buf.getFloat();        // float Fuel remaining in terms of laps (value on MFD)
            int m_maxRPM = buf.getShort() & 0xFFFF;                   // uint16 Cars max RPM, point of rev limiter
            int m_idleRPM = buf.getShort() & 0xFFFF;                  // uint16 Cars idle RPM
            int m_maxGears = buf.get() & 0xFF;                 // uint8 Maximum number of gears
            int m_drsAllowed = buf.get() & 0xFF;               // uint8 0 = not allowed, 1 = allowed, -1 = unknown

            int m_drsActivationDistance = buf.getShort() & 0xFFFF;    // uint16 0 = DRS not available, non-zero - DRS will be available in [X] metres

            int[] m_tyresWear = new int[4]; // uint8[4] Tyre wear percentage
            for (int i = 0; i < 4; ++i) {
                m_tyresWear[i] = buf.get() & 0xFF;
            }
            int m_actualTyreCompound = buf.get() & 0xFF;        // uint8 F1 Modern - 16 = C5, 17 = C4, 18 = C3, 19 = C2, 20 = C1, 7 = inter, 8 = wet
            // F1 Classic - 9 = dry, 10 = wet
            // F2 – 11 = super soft, 12 = soft, 13 = medium, 14 = hard
            // 15 = wet
            int m_visualTyreCompound = buf.get() & 0xFF;        // uint8 F1 visual (can be different from actual compound)
            // 16 = soft, 17 = medium, 18 = hard, 7 = inter, 8 = wet
            // F1 Classic – same as above
            // F2 – same as above
            int m_tyresAgeLaps = buf.get() & 0xFF;             // uint8 Age in laps of the current set of tyres
            int[] m_tyresDamage = new int[4];     // uint8[4] Tyre damage (percentage)
            for (int i = 0; i < 4; ++i) {
                m_tyresDamage[i] = buf.get() & 0xFF;
            }
            int m_frontLeftWingDamage = buf.get() & 0xFF;      // uint8 Front left wing damage (percentage)
            int m_frontRightWingDamage = buf.get() & 0xFF;     // uint8 Front right wing damage (percentage)
            int m_rearWingDamage = buf.get() & 0xFF;           // uint8 Rear wing damage (percentage)

            int m_drsFault = buf.get() & 0xFF;                 // uint8 Indicator for DRS fault, 0 = OK, 1 = fault

            int m_engineDamage = buf.get() & 0xFF;             // uint8 Engine damage (percentage)
            int m_gearBoxDamage = buf.get() & 0xFF;            // uint8 Gear box damage (percentage)
            int m_vehicleFiaFlags = buf.get();          // int8 -1 = invalid/unknown, 0 = none, 1 = green, 2 = blue, 3 = yellow, 4 = red
            float m_ersStoreEnergy = buf.getFloat();           // float ERS energy store in Joules
            int m_ersDeployMode = buf.get() & 0xFF;            // uint8 ERS deployment mode, 0 = none, 1 = medium, 2 = overtake, 3 = hotlap
            float m_ersHarvestedThisLapMGUK = buf.getFloat();  // float ERS energy harvested this lap by MGU-K
            float m_ersHarvestedThisLapMGUH = buf.getFloat();  // float ERS energy harvested this lap by MGU-H
            float m_ersDeployedThisLap = buf.getFloat();       // float ERS energy deployed this lap
            return Optional.of(new CarStatus(m_tractionControl,
                    m_antiLockBrakes,
                    m_fuelMix,
                    m_frontBrakeBias,
                    m_pitLimiterStatus,
                    m_fuelInTank,
                    m_fuelCapacity,
                    m_fuelRemainingLaps,
                    m_maxRPM,
                    m_idleRPM,
                    m_maxGears,
                    m_drsAllowed,
                    m_drsActivationDistance,
                    m_tyresWear,
                    m_actualTyreCompound,
                    m_visualTyreCompound,
                    m_tyresAgeLaps,
                    m_tyresDamage,
                    m_frontLeftWingDamage,
                    m_frontRightWingDamage,
                    m_rearWingDamage,
                    m_drsFault,
                    m_engineDamage,
                    m_gearBoxDamage,
                    m_vehicleFiaFlags,
                    m_ersStoreEnergy,
                    m_ersDeployMode,
                    m_ersHarvestedThisLapMGUK,
                    m_ersHarvestedThisLapMGUH,
                    m_ersDeployedThisLap));
        } catch (Exception e) {
            return null;
        }
    }

    public String toString() {
        return "CarStatus { " +
                " m_fuelInTank:" + this.m_fuelInTank +
                " m_fuelRemainingLaps:" + this.m_fuelRemainingLaps +
                " m_tyresWear:" + Arrays.toString(this.m_tyresWear) +
                " m_tyresDamage:" + Arrays.toString(this.m_tyresDamage) +
                " m_tyresAgeLaps:" + this.m_tyresAgeLaps +
                "}";
    }

}

public class CarStatuses {

    Vector<CarStatus> carStatuses;

    public CarStatuses(Vector<CarStatus> carStatuses) {
        this.carStatuses = carStatuses;
    }

    public static int Size() {
        return 1 + // uint8           m_numActiveCars;	// Number of active cars in the data – should match number of

                (22 * CarStatus.Size()); //  CarStatusData m_carStatuses[22];
    }

    public static Optional<CarStatuses> Parse(ByteBuffer buf) {
        try {
            Vector<CarStatus> carStatuses = new Vector<>();
            for (int i = 0; i < 22; ++i) {
                Optional<CarStatus> participant = CarStatus.Parse(buf);
                if (!participant.isPresent()) {
                    return Optional.empty();
                } else {
                    carStatuses.add(participant.get());
                }
            }

            return Optional.of(new CarStatuses(carStatuses));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String toString() {
        String toPrint = "CarStatuses - ";

        for (CarStatus carStatus : carStatuses) {
            toPrint += carStatuses.toString();
        }

        return toPrint;
    }

    public int[] estimateLapsLeft(int index) {
        CarStatus status = carStatuses.get(index);
        int[] estimations = new int[4];
        for (int i = 0; i < 4; ++i) {
            if (0 == status.m_tyresAgeLaps || 0 == status.m_tyresWear[i]) {
                estimations[i] = 999;
                continue;
            }
            estimations[i] = ((status.m_tyresAgeLaps * 100) / status.m_tyresWear[i]) - status.m_tyresAgeLaps;
        }
        return estimations;
    }

}
