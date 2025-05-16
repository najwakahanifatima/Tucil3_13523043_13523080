import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SaveLoad{
    public String Load(){
        // input file
        Scanner scanner = new Scanner(System.in);
        System.out.println("Masukkan path folder: ");
        String filePath = scanner.nextLine();

        // convert ke string
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Kesalahan saat membaca file: " + e.getMessage());
        }
        return content.toString();
    }
}