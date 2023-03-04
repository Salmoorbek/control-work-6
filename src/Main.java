import controlwoork.ControlWork;

import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try {
            new ControlWork("localhost", 6129).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        LocalDate localDate = LocalDate.now();
//        System.out.println(localDate);

    }
}