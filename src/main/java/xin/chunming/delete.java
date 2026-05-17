package xin.chunming;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class delete {
    public static void main(String[] args) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("fails"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(new File(line).delete()+line);
            }
        }
    }
}
