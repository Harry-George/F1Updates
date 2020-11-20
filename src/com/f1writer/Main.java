package com.f1writer;

import com.f1updates.PacketHeader;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {

        int port = args.length == 0 ? 20777 : Integer.parseInt(args[0]);
        int rowsWritten = 0;

        try {
            AtomicBoolean keepGoing = new AtomicBoolean(true);
            SignalHandler handler = sig -> {
                System.out.println("captured signal " + sig);
                keepGoing.set(false);
            };
            Signal.handle(new Signal("INT"), handler);

            DatagramSocket serverSocket = new DatagramSocket(port);
            // Hopefully this is big enough for any packet
            byte[] receiveData = new byte[10000];

            System.out.printf("Listening on udp:%s:%d%n",
                    InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData,
                    receiveData.length);
            // TODO - make it so we can pass this in
            OutputStream outputStream = new FileOutputStream("C:\\Users\\Hobby\\IdeaProjects\\F1Updates\\data\\f1.output");
            while (keepGoing.get()) {

                rowsWritten++;

                serverSocket.receive(receivePacket);

                outputStream.write(ByteBuffer.allocate(4).putInt(receivePacket.getLength()).array());
                System.out.println("Wrote bytes:" + receivePacket.getLength());
                outputStream.write(receivePacket.getData(), 0, receivePacket.getLength());

            }
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println("Wrote " + rowsWritten + " rows.");
    }

    public void run(int port) {

        // should close serverSocket in finally block
    }
}
