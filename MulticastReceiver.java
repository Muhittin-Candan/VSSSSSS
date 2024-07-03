import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.charset.StandardCharsets;

// Aufgabe F
public class MulticastReceiver {
    // Definieren der Multicast-Gruppe und des Ports
    private static final String MULTICAST_GROUP = "225.4.5.6";
    private static final int PORT = 9000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        DatagramChannel channel = null;
        try {
            // Öffnen eines DatagramChannels
            channel = DatagramChannel.open();

            // Binden des Kanals an die Multicast-Adresse und den Port
            InetSocketAddress group = new InetSocketAddress(MULTICAST_GROUP, PORT);

            // Netzwerkschnittstelle auswählen (ändern Sie den Namen entsprechend Ihrer Netzwerkschnittstelle)
            NetworkInterface networkInterface = NetworkInterface.getByName("lo"); // Beispiel: "en0" auf macOS
            
            if (networkInterface == null) {
                System.out.println("Netzwerkschnittstelle nicht gefunden.");
                return;
            }

            // Kanal an den angegebenen Port binden
            channel.bind(new InetSocketAddress(PORT));

            // Der Multicast-Gruppe beitreten
            MembershipKey key = channel.join(group.getAddress(), networkInterface);

            System.out.println("Listening on multicast group " + MULTICAST_GROUP + " at port " + PORT);

            // Puffer zum Empfangen von Nachrichten erstellen
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (true) {
                // Puffer für den nächsten Empfangsvorgang vorbereiten
                buffer.clear();
                // Nachricht empfangen
                channel.receive(buffer);
                // Puffer zum Lesen umschalten
                buffer.flip();

                // Nachricht als UTF-8 dekodieren
                String message = StandardCharsets.UTF_8.decode(buffer).toString();
                // Empfangene Nachricht ausgeben
                System.out.println("Received message: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Kanal schließen, falls er geöffnet wurde
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}