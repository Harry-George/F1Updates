package com.f1updates;
import java.io.IOException;
import java.net.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
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

            while(true)
            {
                serverSocket.receive(receivePacket);

                Optional<PacketHeader> header = PacketHeader.ParsePacketHeader(ByteBuffer.wrap(receivePacket.getData(), 0, receivePacket.getLength()));
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
