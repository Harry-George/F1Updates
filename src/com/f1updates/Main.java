package com.f1updates;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello World");
        // write your code here
        Optional<PacketHeader> header = PacketHeader.ParsePacketHeader(ByteBuffer.allocate(20));
        if (! header.isPresent()) {
            return;
        }
        Object weatherForecast = WeatherForecast.ParseWeather(ByteBuffer.allocate(20));
        System.out.println(weatherForecast);
    }
}
