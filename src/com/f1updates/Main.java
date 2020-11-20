package com.f1updates;
import java.io.*;
import java.net.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;


public class Main {

    public static void main(String[] args) {
        int port = args.length == 0 ? 20777 : Integer.parseInt(args[0]);
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[500];

            System.out.printf("Listening on udp:%s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);
            InputStream inputStream = new FileInputStream("C:\\Users\\Hobby\\IdeaProjects\\F1Updates\\data\\f1.output");
            while(true)
            {

//                serverSocket.receive(receivePacket);

                byte[] badger = new byte[4];
                if (inputStream.read(badger,0,4) == 0) {
                    return;
                }
                int len = ByteBuffer.wrap(badger).getInt();
                if (len == 0) {
                    return;
                }
                receiveData = new byte[len];
                System.out.println("Read bytes:" + len);
                inputStream.read(receiveData,0,len);
                 Optional<PacketHeader> header = PacketHeader.ParsePacketHeader(ByteBuffer.wrap(receiveData, 0, len));
                if (header == null) {
                    System.out.println("Failed to parse");
                } else {
                    System.out.println(header.get().packetId.toString());
                }
//                Object weatherForecast = WeatherForecast.ParseWeather(ByteBuffer.allocate(20));
//                System.out.println(weatherForecast);

            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run(int port) {

        // should close serverSocket in finally block
    }
}
