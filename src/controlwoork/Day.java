package controlwoork;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

public class Day {
    private ArrayList<Task> tasks;
    private LocalDate date;
    private String dateName;

    public Day(LocalDate date) {
        tasks = new ArrayList<>();
        this.date = date;
        dateName = getDateNameWithFormat();
    }
    public Day(){
        this(LocalDate.now());
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }
    public String getDateNameWithFormat(){
        return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("ru")));
    }
}
