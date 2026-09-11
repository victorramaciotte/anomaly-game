package main;

import java.io.*;

public class HighScore {
    private static final String FILE_PATH = "best_time.txt";

    public static double load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            return Double.parseDouble(reader.readLine());
        } catch (IOException | NumberFormatException e) {
            return Double.MAX_VALUE; // sem recorde ainda
        }
    }

    public static void save(double seconds) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println(seconds);
        } catch (IOException e) {
            System.out.println("Falha ao salvar recorde: " + e.getMessage());
        }
    }
}