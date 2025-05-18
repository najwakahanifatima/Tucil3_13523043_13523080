package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static void saveMatrixToTxt(char[][] matrix, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    writer.write(String.valueOf(matrix[i][j]));
                    
                }
                writer.newLine(); // Baris baru untuk setiap baris matrix
            }
            System.out.println("Matrix berhasil disimpan ke " + filename);
        } catch (IOException e) {
            System.err.println("Error saat menyimpan matrix: " + e.getMessage());
        }
    }
}