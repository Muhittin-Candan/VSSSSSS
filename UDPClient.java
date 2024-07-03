import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// Aufgabe E
public class UDPClient {

    // Port des Zeitservers
    private static final int PORT = 37;
    // Zeitdifferenz in Sekunden zwischen den Jahren 1900 und 1970
    private static final long DIFF_1900_TO_1970 = 2208988800L;

    public static void main(String[] args) {
        // Überprüfen, ob der Servername als Argument übergeben wurde
        if (args.length < 1) {
            System.err.println("Usage: java UDPTimeClient <server>");
            return;
        }

        // Den Servernamen aus den Argumenten übernehmen
        String serverName = args[0];
        args[0]="time.nist.gov";

        try {
            // Erstellen und Verbinden des DatagramChannel mit dem angegebenen Server und Port
            DatagramChannel channel = DatagramChannel.open();
            channel.connect(new InetSocketAddress(serverName, PORT));

            // Erstellen und Senden eines leeren Pakets zur Initialisierung der Anfrage
            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put((byte) 0);
            buffer.flip();
            channel.write(buffer);

            // Empfang des Antwortpakets
            buffer = ByteBuffer.allocate(4);
            channel.read(buffer);
            buffer.flip();

            // Konvertieren der Antwort in Zeit
            long secondsSince1900 = buffer.getInt() & 0xffffffffL;
            long secondsSince1970 = secondsSince1900 - DIFF_1900_TO_1970;
            Instant instant = Instant.ofEpochSecond(secondsSince1970);
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Europe/Berlin"));
            
            // Formatieren und Ausgeben des Ergebnisses
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd\nHH:mm:ss");
            System.out.println(formatter.format(dateTime));

            // Schließen des Kanals
            channel.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
