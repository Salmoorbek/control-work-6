package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controlwoork.Day;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileService {
    public static void writeFile(ArrayList<Day> days){
        Path path = Paths.get("./data/data.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Day[] result = days.stream().toArray(Day[]::new);
        String json = gson.toJson(result);
        try{
            byte[] arr = json.getBytes();
            Files.write(path, arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Day[] readDays(){
        Path path = Paths.get("./data/data.json");
        String json = "";
        try{
            json = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Day[].class);
    }
}
