package cjcompany.nutridog;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class TestTester {

    @Test
    public void testOfTests() {

        BufferedReader br = null;
        try {
            FileReader fr = new FileReader("src/main/assets/test.txt");
            br = new BufferedReader(fr);

            System.out.print("I got past initiating the br and fr!");

            String str = br.readLine();
            while (str != null) {
                System.out.print("I'm about to see what the first line is");
                assertTrue(str.equals("TEST"));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Test.txt is not at expected location");
        } catch (IOException ex1) {
            System.err.println("Error while reading test file");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
