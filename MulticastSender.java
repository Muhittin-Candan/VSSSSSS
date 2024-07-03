import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

// Aufgabe G
public class MulticastSender {
    // Definieren der Multicast-Gruppe und des Ports
    private static final String MULTICAST_GROUP = "225.4.5.6";
    private static final int PORT = 9000;

    public static void main(String[] args) {
        MulticastSocket socket = null;
        try {
            // Erstellen eines DatagramSockets
            socket = new MulticastSocket();

            // Multicast-Adresse auflösen
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);

            // Scanner für die Benutzereingabe
            Scanner scanner = new Scanner(System.in);

            System.out.println("Multicast Sender gestartet. Geben Sie Text ein und drücken Sie Enter, um ihn zu senden.");

            while (true) {
                // Benutzereingabe abfragen
                System.out.print("Eingabe: ");
                String message = scanner.nextLine();

                // Nachricht in Byte-Array umwandeln
                byte[] buffer = message.getBytes(StandardCharsets.UTF_8);

                // DatagramPacket erstellen
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);

                // Paket senden
                socket.send(packet);

                System.out.println("Nachricht gesendet: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Socket schließen, falls er geöffnet wurde
            if (socket != null) {
                socket.close();
            }
        }
    }
}