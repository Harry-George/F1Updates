package com.f1updates;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;


interface IReader {
    ByteBuffer Get() throws Exception;
}

class FileReader implements IReader {
    public FileReader(boolean wait) throws Exception {
        this.inputStream_ = new FileInputStream("C:\\Users\\Hobby\\IdeaProjects\\F1Updates\\data\\f1.output");
        this.wait_ = wait;
    }

    public ByteBuffer Get() throws Exception {
        byte[] length_buffer = new byte[4];
        if (0 == inputStream_.read(length_buffer, 0, 4)) {
            // todo - this better
            throw new Exception("Finished");
        }
        int len = ByteBuffer.wrap(length_buffer).getInt();
        if (len == 0) {
            throw new Exception("Finished 2");
        }

        byte[] timestamp_buffer = new byte[8];
        if (0 == inputStream_.read(timestamp_buffer, 0, 8)) {
            // todo - this better
            throw new Exception("Error getting time");
        }
        long timestamp = ByteBuffer.wrap(timestamp_buffer).getLong();
        if (timestamp == 0) {
            throw new Exception("Error getting time 2");
        }

        if (0 == timestampDelta_) {
            timestampDelta_ = System.nanoTime() - timestamp;
        }

        if (this.wait_) {
            while (timestampDelta_ + timestamp > System.nanoTime()) {
                TimeUnit.MILLISECONDS.sleep(1);
            }
        }

        receiveData_ = new byte[len];
        if (len != inputStream_.read(receiveData_, 0, len)) {
            throw new Exception("Unable to parse file");
        }
        return ByteBuffer.wrap(receiveData_, 0, len);
    }

    long timestampDelta_ = 0;
    byte[] receiveData_;
    InputStream inputStream_;
    boolean wait_;
}

class PacketReader implements IReader {
    public PacketReader(int port) throws Exception {
        System.out.printf("Listening on udp:%s:%d%n",
                InetAddress.getLocalHost().getHostAddress(), port);
        this.serverSocket_ = new DatagramSocket(port);
        this.receivePacket_ = new DatagramPacket(buffer_,
                buffer_.length);
    }

    public ByteBuffer Get() throws Exception {

        serverSocket_.receive(receivePacket_);
        return ByteBuffer.wrap(receivePacket_.getData(), 0, receivePacket_.getLength());
    }

    DatagramSocket serverSocket_;
    DatagramPacket receivePacket_;
    byte[] buffer_ = new byte[2000];
}

class Data {
    SessionData sessionData = null;
    CarsLapData carsLapData = null;
    Participants participants = null;
    CarStatuses carStatuses = null;
    Map<Integer, Integer> previousPositions = null;
}

class DriverInfo {
    public DriverInfo(Integer driverCarNumber, Integer driverGameNumber, Integer driverIndex) {
        this.driverCarNumber = driverCarNumber;
        this.driverGameNumber = driverGameNumber;
        this.driverIndex = driverIndex;
    }

    Integer driverCarNumber;
    Integer driverGameNumber;
    Integer driverIndex;
}

public class Main {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[33m";

    public static String GetPositionColour(Integer position, Integer previousPosition) {
        if (previousPosition > position) {
            return ANSI_RED;
        }
        if (previousPosition < position) {
            return ANSI_GREEN;
        }
        return "";
    }

    public static String PrintIfDamage(String name, int value) {
        String ret = "";
        if (value > 0) {
            ret += ANSI_RED + "'" + name + ":" + value + "' " + ANSI_RESET;
        }
        return ret;
    }

    public static String PrintIfInPits(int value) {
        String ret = "";
        if (value == 1) {
            ret += ANSI_YELLOW + "'PITTING' " + ANSI_RESET;
        } else if (value == 2) {
            ret += ANSI_YELLOW + "'IN PITS' " + ANSI_RESET;
        }
        return ret;
    }

    public static String PrintDriverStatus(int value) {

        switch (value) {
            case 0:
                return "IN GARAGE";
            case 1:
                return "FLYING LAP";
            case 2:
                return "IN LAP";
            case 3:
                return "OUT LAP";
            case 4:
                return "ON TRACK";
        }

        String ret = "";
        if (value == 1) {
            ret += ANSI_YELLOW + "'PITTING' " + ANSI_RESET;
        } else if (value == 2) {
            ret += ANSI_YELLOW + "'IN PITS' " + ANSI_RESET;
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        int port = args.length == 0 ? 20778 : Integer.parseInt(args[0]);
        // TODO - arg parsing
        // If true read from file, false read from packets
        boolean readFromFile = false;
        boolean showDeltas = true;
        // if true (and reading from file) play back packets as if simulating the session, otherwise play as fast as
        // you can
        boolean playFileInRealTime = true;
        boolean showDriverId = false;

        boolean printQualiStuff = true;


        Vector<DriverInfo> driversWeCareAbouts = new Vector<>();
        class Highlight {
            String name;
            String colour;

            public Highlight(String name, String colour) {
                this.name = name;
                this.colour = colour;
            }
        }
        Map<Integer, Highlight> driversToHighlight = new HashMap<Integer, Highlight>();
        // Note: If all drivers are being printed, is the driver index.
        driversToHighlight.put(108, new Highlight("THOMAS", ANSI_BLUE));
        driversToHighlight.put(111, new Highlight("XANDER", "\u001b[38;5;90m"));
        driversToHighlight.put(112, new Highlight("JUSTIN", "\u001b[38;5;120m"));
        driversToHighlight.put(110, new Highlight("BOBBY", "\u001b[38;5;112m"));

//        driversWeCareAbouts.add(new DriverInfo(19, null));
//        driversWeCareAbouts.add(new DriverInfo(71, null));
//        driversWeCareAbouts.add(new DriverInfo(69, null));
//        driversWeCareAbouts.add(new DriverInfo(28, null));

        Data latestData = new Data();
        latestData.previousPositions = new TreeMap<>();


        long lastUpdateTime = Instant.now().getEpochSecond();

        DecimalFormat floatingPointFormat = new DecimalFormat("0.00");
        DecimalFormat twoDigitFormat = new DecimalFormat("00");

        IReader reader;

        int rowsRead = 0;
        try {
            if (readFromFile) {
                reader = new FileReader(playFileInRealTime);
            } else {
                reader = new PacketReader(port);
            }


            while (true) {
                rowsRead += 1;
                ByteBuffer buffer = reader.Get();
                Optional<PacketHeader> header = PacketHeader.ParsePacketHeader(buffer);
                if (!header.isPresent()) {
                    System.out.println("Failed to parse");
                    continue;
                }
//                System.out.println(header.get().packetId.toString());
                switch (header.get().packetId) {

                    case Motion:
                        new Motion().GetSize();
                        break;
                    case Session:
                        Optional<SessionData> sessionData = SessionData.Parse(buffer);
                        if (sessionData.isPresent()) {
                            latestData.sessionData = sessionData.get();
                        }
                        break;
                    case LapData:
                        Optional<CarsLapData> carsLapData = CarsLapData.Parse(buffer);
                        if (carsLapData.isPresent()) {
                            latestData.carsLapData = carsLapData.get();
                        }
                        break;
                    case Event:
                        break;
                    case Participants:
                        Optional<Participants> participants = Participants.Parse(buffer);
                        if (participants.isPresent()) {
                            boolean printAllDrivers = true;

                            if (printAllDrivers) {
                                latestData.participants = participants.get();
                                driversWeCareAbouts.clear();
                                int len = participants.get().participants.size();
                                for (int i = 0; i < len; ++i) {
                                    if (participants.get().participants.elementAt(i).m_raceNumber != 0) {
                                        driversWeCareAbouts.add(new DriverInfo(participants.get().participants.elementAt(i).m_raceNumber, participants.get().participants.elementAt(i).m_driverId, i));
                                    }
                                }
                            } else {
                                latestData.participants = participants.get();
                                for (DriverInfo driver : driversWeCareAbouts) {
                                    int len = participants.get().participants.size();
                                    for (int i = 0; i < len; ++i) {
                                        if (participants.get().participants.elementAt(i).m_raceNumber == driver.driverCarNumber) {
                                            driver.driverIndex = i;
                                        }
                                    }
                                }
                            }

                        }

                        break;
                    case CarSetups:
                        break;
                    case CarTelemetry:
                        break;
                    case CarStatus:
                        Optional<CarStatuses> carStatuses = CarStatuses.Parse(buffer);
                        if (carStatuses.isPresent()) {
                            latestData.carStatuses = carStatuses.get();

                        }
                        break;
                    case FinalClassifciation:
                        break;
                    case LobbyInfo:
                        break;
                }

                // Print
                if (lastUpdateTime + 2 < Instant.now().getEpochSecond() ||
                        !playFileInRealTime) {
                    lastUpdateTime = Instant.now().getEpochSecond();
                    if (null == latestData.sessionData
                            || null == latestData.carsLapData
                            || null == latestData.carStatuses
                            || null == latestData.participants) {
                        System.out.println("No data");
                        continue;
                    }
                    for (WeatherForecast cast : latestData.sessionData.m_weatherForecastSamples) {
                        System.out.println(cast);
                    }
                    Map<Integer, String> OutMap = new TreeMap<>();

                    Map<Integer, String> deltas = null;
                    if (showDeltas) {
                        deltas = latestData.carsLapData.CalculateDeltas(latestData.sessionData.m_trackLength);
                    }

                    for (DriverInfo index : driversWeCareAbouts) {
                        if (index.driverIndex == null) {
                            System.out.println(latestData.participants.participants);
                            break;
                        }

                        CarLapData curCarLapData = latestData.carsLapData.carsLapData.get(index.driverIndex);
                        CarStatus curCarStatus = latestData.carStatuses.carStatuses.get(index.driverIndex);

                        String string = new String("");

                        if (latestData.previousPositions.get(index.driverIndex) != null) {
                            string += GetPositionColour(latestData.previousPositions.get(index.driverIndex), curCarLapData.m_carPosition);
                        }
                        latestData.previousPositions.put(index.driverIndex, curCarLapData.m_carPosition);

                        string += "Pos:";
                        string += curCarLapData.m_carPosition;
                        string += "\t";
                        if (driversToHighlight.containsKey(index.driverGameNumber)) {
                            string += driversToHighlight.get(index.driverGameNumber).name;
                        } else {
                            string += "Car:" + index.driverCarNumber;
                        }
                        string += ANSI_RESET;

                        if (showDriverId) {
                            string += " DriverID:" + index.driverGameNumber;
                        }

                        if (driversToHighlight.containsKey(index.driverGameNumber)) {
                            string += driversToHighlight.get(index.driverGameNumber).colour;
                        }

                        if (curCarLapData.m_resultStatus != 2) {
                            string += ANSI_RED + " Status:" + curCarLapData.m_resultStatus + ANSI_RESET;
                            OutMap.put(curCarLapData.m_carPosition, string);
                            continue;
                        }

                        string += "\tTyres[";
                        string += "Compound:" + CarStatus.VisualCompoundToString(curCarStatus.m_visualTyreCompound);
                        if (driversToHighlight.containsKey(index.driverGameNumber)) {
                            string += driversToHighlight.get(index.driverGameNumber).colour;
                        }
                        string += " Age:" + curCarStatus.m_tyresAgeLaps;
                        string += " LapsLeft:[";
                        boolean comma = false;
                        for (Integer value : latestData.carStatuses.estimateLapsLeft(index.driverIndex,
                                (int) curCarLapData.m_lapDistance,
                                latestData.sessionData.m_trackLength)) {
                            if (comma) {
                                string += ",";
                            }
                            if (value == 99) {
                                string += "--";
                            } else {
                                string += twoDigitFormat.format(value);
                            }
                            comma = true;
                        }

                        string += "]\tFuel[laps:";
                        string += floatingPointFormat.format(curCarStatus.m_fuelRemainingLaps);
                        string += " mix:";
                        string += CarStatus.FuelMixString(curCarStatus.m_fuelMix);
                        string += "]";

                        if (printQualiStuff) {
                            string += "\t" + PrintDriverStatus(curCarLapData.m_driverStatus);
                            if (curCarLapData.m_currentLapInvalid == 1) {
                                string += ANSI_RED + "\tINVALIDATED" + ANSI_RESET;
                            }
                        } else if (deltas != null) {
                            string += "\tdistance:" + deltas.get(curCarLapData.m_carPosition);
                        }

                        if (curCarLapData.m_penalties > 0) {
                            string += ANSI_RED + "\tPenalties: +" + curCarLapData.m_penalties + ANSI_RESET;
                        }

                        {
                            int tyreIndex = 0;
                            String damageString = "";
                            damageString += PrintIfDamage("drs faults", curCarStatus.m_drsFault);
                            damageString += PrintIfDamage("frontLeftWingDamage", curCarStatus.m_frontLeftWingDamage);
                            damageString += PrintIfDamage("frontRightWingDamage", curCarStatus.m_frontRightWingDamage);
                            damageString += PrintIfDamage("rearWingDamage", curCarStatus.m_rearWingDamage);

                            damageString += PrintIfInPits(curCarLapData.m_pitStatus);

                            if (!damageString.isEmpty()) {
                                string += "\t" + damageString;
                            }
                        }

                        string += ANSI_RESET;

                        if (printQualiStuff) {
                            Integer value = Math.round(curCarLapData.m_lapDistance);
                            if (value < 0) {
                                value += latestData.sessionData.m_trackLength;
                            }

                            string += "\t" + value;
                            OutMap.put(value, string);
                        } else {
                            OutMap.put(curCarLapData.m_carPosition, string);

                        }

                    }
                    for (Map.Entry<Integer, String> pair : OutMap.entrySet()) {
                        System.out.println(pair.getValue());
                    }


                }
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Read " + rowsRead + " rows.");
            throw e;
        }
    }

}
