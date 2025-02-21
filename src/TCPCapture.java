import jpcap.*;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

import java.io.IOException;
import java.util.Scanner;

public class TCPCapture {
    public static void main(String[] args) {
        try {
            // Get list of available network interfaces
            NetworkInterface[] devices = JpcapCaptor.getDeviceList();
            if (devices.length == 0) {
                System.out.println("No network interfaces found.");
                return;
            }

            // Display available interfaces
            System.out.println("Available Network Interfaces:");
            for (int i = 0; i < devices.length; i++) {
                System.out.println(i + ": " + devices[i].name + " (" + devices[i].description + ")");
            }

            // Let the user select an interface
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the interface number to capture packets: ");
            int interfaceIndex = scanner.nextInt();

            if (interfaceIndex < 0 || interfaceIndex >= devices.length) {
                System.out.println("Invalid selection.");
                return;
            }

            // Open selected network interface for capturing
            JpcapCaptor captor = JpcapCaptor.openDevice(devices[interfaceIndex], 65535, true, 20);

            System.out.println("Capturing TCP packets... Press Ctrl+C to stop.");

            // Capture TCP packets indefinitely
            while (true) {
                Packet packet = captor.getPacket();
                if (packet instanceof TCPPacket) {
                    TCPPacket tcpPacket = (TCPPacket) packet;
                    System.out.println("=== Captured TCP Packet ===");
                    System.out.println("Source IP: " + tcpPacket.src_ip);
                    System.out.println("Destination IP: " + tcpPacket.dst_ip);
                    System.out.println("Source Port: " + tcpPacket.src_port);
                    System.out.println("Destination Port: " + tcpPacket.dst_port);
                    System.out.println("Sequence Number: " + tcpPacket.sequence);
                    System.out.println("ACK Number: " + tcpPacket.ack_num);
                    System.out.println("Flags: FIN=" + tcpPacket.fin + ", SYN=" + tcpPacket.syn + ", ACK=" + tcpPacket.ack);
                    System.out.println("==========================");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}