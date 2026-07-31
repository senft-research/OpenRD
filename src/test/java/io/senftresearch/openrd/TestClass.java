package io.senftresearch.openrd;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RuidaTest {

    private OpenRD rd;

    @BeforeEach
    void setUp() {
        rd = OpenRD.getInstance();
    }

    @Test
    void testRuidaLayerSetup() throws IOException, InterruptedException {
        List<RDPoint> markPath1 = List.of(new RDPoint(12, 10), new RDPoint(38, 25), new RDPoint(12, 40), new RDPoint(12, 10));
        List<RDPoint> markPath2 = List.of(new RDPoint(16, 6), new RDPoint(10, 6), new RDPoint(13, 3), new RDPoint(16, 6));
        List<RDPoint> markPath3 = List.of(new RDPoint(60, 6), new RDPoint(54, 6), new RDPoint(57, 3), new RDPoint(60, 6));

        List<List<RDPoint>> pathsListMark = new ArrayList<>();
        pathsListMark.add(markPath1);
        pathsListMark.add(markPath2);
        pathsListMark.add(markPath3);

        List<List<RDPoint>> pathsListCut = List.of(List.of(
                new RDPoint(0, 0),
                new RDPoint(50, 0),
                new RDPoint(50, 50),
                new RDPoint(0, 50),
                new RDPoint(0, 0)
        ));

        RDLayer layer0 = new RDLayer(pathsListMark, 100, new RDPoint(10, 18), new RDColour(0, 255, 0), 20.0f, null);
        RDLayer layer1 = new RDLayer(pathsListCut, 30, new RDPoint(40, 70), new RDColour(255, 0, 0), 20.0f, null);

        rd.set(layer0, 0);
        rd.set(layer1, 0);

        String filePath = "test.rd";
        rd.write(true, filePath);

        byte[] rdFileBytes = Files.readAllBytes(Paths.get(filePath));
        assertNotNull(rdFileBytes, "Generated payload should not be null");
        assertTrue(rdFileBytes.length > 0, "Generated payload should contain data");

        String ruidaControllerIp = "10.0.0.100";

        RDUdpHandler udpClient = new RDUdpHandler(ruidaControllerIp);
        try {
            System.out.println("Sending " + rdFileBytes.length + " bytes to Ruida Controller...");

            byte[] response = udpClient.write(rdFileBytes);

            if (response == null) {
                System.out.println("Transmission successful! All packets acknowledged.");
            } else {
                System.out.printf("Transmission cut short. Non-ACK response received: 0x%02X%n", response[0]);
            }

        } catch (IOException e) {
            System.err.println("Network failure or timeout during transmission: " + e.getMessage());
            throw e;
        } finally {
            udpClient.close();
        }
    }
}
