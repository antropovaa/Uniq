import org.junit.*;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class UniqTest {
    private String getContent(String args[]) throws FileNotFoundException {
        UniqParser parser = new UniqParser(args);
        return Uniq.inputAndProcess(parser).toString();
    }

    private static void assertFiles(String expectedFileName, String actualFileName) throws IOException {
        File expectedFile = new File(expectedFileName);
        File actualFile = new File(actualFileName);
        Scanner expected = new Scanner(expectedFile);
        Scanner actual = new Scanner(actualFile);

        Assert.assertEquals(expectedFile.length(), actualFile.length());
        while (expected.hasNext())
            assertEquals(expected.next(), actual.next());

    }

    @Test
    public void fromFileToFile() throws IOException {
        String[] args = {"-i", "-c", "-s", "3", "-o", "test/examples/result1.txt", "test/examples/test1.txt"};
        Uniq.main(args);
        assertFiles("test/examples/expected1.txt", "test/examples/result1.txt");
    }

    @Test
    public void fromCmdToFile() throws IOException {
        String[] args = {"-i", "-u", "-s", "3", "-o", "test/examples/result2.txt", "Мама мыла раму", "МАША МЫЛА РАМУ",
                "папа мыл раму", "Света мыла раму"};
        Scanner input = new Scanner("Мама мыла раму\nМАША МЫЛА РАМУ\nпапа мыл раму\nСвета мыла раму");
        UniqParser parser = new UniqParser(args);
        Uniq.output(parser, Uniq.makeFinalList(parser, Uniq.comparison(input, parser)));
        assertFiles("test/examples/expected2.txt", "test/examples/result2.txt");
    }

    @Test
    public void fromFileToCmd() throws FileNotFoundException {
        String[] args = {"-c", "test/examples/test3.txt"};
        String actual = getContent(args);
        String expected = "[2 Мама мыла раму, 2 Света мыла раму]";
        assertEquals(expected, actual);
    }

    @Test
    public void fromCmdToCmd() throws IOException {
        String[] args = {};
        Scanner input = new Scanner("Мама мыла раму\nМама мыла раму");
        UniqParser parser = new UniqParser(args);
        String actual = Uniq.makeFinalList(parser, Uniq.comparison(input, parser)).toString();
        String expected = "[Мама мыла раму]";
        assertEquals(expected, actual);
    }

    @Test
    public void wrongFileFormat() {
        String[] args = {"-i", "-c", "test1.doc"};
        assertThrows(FileNotFoundException.class, () -> Uniq.main(args));
    }

    @Test
    public void fileNotFound() {
        String[] args = {"-i", "-c", "nonexistentFile.txt"};
        assertThrows(FileNotFoundException.class, () -> Uniq.main(args));
    }
}