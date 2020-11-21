package com.f1updates;

import java.io.*;
import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;


interface IReader {
    public ByteBuffer Get() throws Exception;
}

class FileReader implements IReader {
    public FileReader() throws Exception {
        this.inputStream_ = new FileInputStream("C:\\Users\\Hobby\\IdeaProjects\\F1Updates\\data\\f1.output");
    }

    public ByteBuffer Get() throws Exception {
        byte[] badger = new byte[4];
        if (0 == inputStream_.read(badger, 0, 4)) {
            // todo - this better
            throw new Exception("Finished");
        }
        int len = ByteBuffer.wrap(badger).getInt();
        if (len == 0) {
            throw new Exception("Finished 2");
        }

        receiveData_ = new byte[len];
        if (len != inputStream_.read(receiveData_, 0, len)) {
            throw new Exception("Unable to parse file");
        }
        return ByteBuffer.wrap(receiveData_, 0, len);
    }

    byte[] receiveData_;
    InputStream inputStream_;
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

public class Main {

    public static void main(String[] args) {
        int port = args.length == 0 ? 20777 : Integer.parseInt(args[0]);
        // TODO - arg parsing
        boolean readFromFile = true;

        IReader reader;

        int rowsRead = 0;
        try {
            if (readFromFile) {
                reader = new FileReader();
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
                System.out.println(header.get().packetId.toString());
                switch (header.get().packetId) {

                    case Motion:
                        new Motion().GetSize();
                        break;
                    case Session:
                        break;
                    case LapData:
                        break;
                    case Event:
                        break;
                    case Participants:
                        Optional<Participants> participants = Participants.Parse(buffer);
                        System.out.println(participants);

                        break;
                    case CarSetups:
                        break;
                    case CarTelemetry:
                        break;
                    case CarStatus:
                        break;
                    case FinalClassifciation:
                        break;
                    case LobbyInfo:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Read " + rowsRead + " rows.");
    }

    public void run(int port) {

        // should close serverSocket in finally block
    }
}
