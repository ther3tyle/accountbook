package io.dsub.datasource;

import io.dsub.model.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class TransactionFileReaderTest {

    private static final Logger logger = Logger.getLogger(TransactionFileReaderTest.class.getName());
    private static TransactionFileReader reader;

    @BeforeAll
    static void prepTest() {
        reader = new TransactionFileReader();
    }

    @Test
    void readAll() {
        try {
            File file = generateRandomData();
            file.deleteOnExit();
            List<Transaction> list = reader.readAll(file.toURI());
            assertEquals(19, list.size());
        } catch(IOException e) {
            logger.severe(e.getMessage());
        }
    }

    @Test
    void read() {
        try {
            File file = generateRandomData();
            file.deleteOnExit();
            Transaction t = reader.read(file, UUID.fromString("80f69a24-44cc-42fb-a6c4-335a8a16bbe7"));
            assertNotNull(t);
            assertThrows(NoSuchElementException.class, () -> {
                reader.read(file.toURI(), UUID.fromString("332-44cc-42fb-a6c4-335a8a16bbe7"));
            });
        } catch(IOException e) {
            logger.severe(e.getMessage());
        }
    }

    private File generateRandomData() throws IOException {
        File file = File.createTempFile("test_", "");

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        String[] dummy = {"554,2,2020-10-14T15:21:19.943171,c7810d0c-f776-44d0-93cd-215437cb197a",
                    "332,4,2020-10-14T15:21:59.009052,80f69a24-44cc-42fb-a6c4-335a8a16bbe7",
                    "12,15,2020-10-14T15:21:59.080363,05466a13-00fc-40ec-b9be-6922ae9fe7e3",
                    "33422,43,2020-10-14T15:21:59.080425,5927545e-1d45-40cc-b898-3366e1b54337",
                    "1238,47,2020-10-14T15:21:59.080459,c1c6b3f3-2b5f-4b30-ab8f-a94f95427566",
                    "842,1,2020-10-14T15:21:59.080585,78a27b77-5fe3-4384-8245-4183318eda9e",
                    "554,2,2020-10-14T15:21:59.080616,28cbec7f-8000-419e-ae4a-d7aab121ba33",
                    "332,4,2020-10-14T15:50:50.234475,63337401-321c-4e55-b596-15269db93b2e",
                    "12,15,2020-10-14T15:50:50.268446,09e1899f-762c-452a-9c95-e608a29f113b",
                    "33422,43,2020-10-14T15:50:50.268495,34f42b46-9c27-4dd3-b940-b8af261ab559",
                    "1238,47,2020-10-14T15:50:50.268522,3d0f5ece-b349-4dd7-a804-417ed2d4881b",
                    "842,1,2020-10-14T15:50:50.268561,85036006-32d7-4d75-a287-2f23963f712f",
                    "554,2,2020-10-14T15:50:50.268584,36240f41-ef1e-4a8a-adf5-fbab59279ed1",
                    "332,4,2020-10-14T15:51:16.215717,d31ac93d-9e91-4a65-afd0-1063793e8e34",
                    "12,15,2020-10-14T15:51:16.279942,5f355d5b-c63c-496e-a195-3b05469d3cec",
                    "33422,43,2020-10-14T15:51:16.279997,53e3877f-f23f-484e-b123-bf034d1ab707",
                    "1238,47,2020-10-14T15:51:16.280042,f8e145cf-d421-47b3-b0bd-72cf7451a916",
                    "842,1,2020-10-14T15:51:16.280083,ab8a752c-ebb0-4952-9b3a-b248c5b6ac4a",
                    "554,2,2020-10-14T15:51:16.280110,65d15c45-004d-4a65-9b97-08c58030ac7b"};

        for (String s : dummy) {
            writer.write(s);
            writer.newLine();
        }

        writer.flush();
        writer.close();

        return file;
    }
}