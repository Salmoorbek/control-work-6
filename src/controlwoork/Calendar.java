package controlwoork;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static util.FileService.readDays;

public class Calendar {
    private int day;
    private int today;
    private int weekDay;
    private String month;
    private ArrayList<Day> days = new ArrayList<>();

    public Calendar() {
        LocalDate localDate = LocalDate.now();
        day = localDate.lengthOfMonth();
        today = localDate.getDayOfMonth();
        weekDay = localDate.minusDays(localDate.getDayOfMonth() - 1).getDayOfWeek().getValue();
        month = localDate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru"));
        for(int i = 1; i <= localDate.lengthOfMonth(); i++){
            days.add(new Day(LocalDate.of(localDate.getYear(), localDate.getMonth(), i)));
        }
        days = new ArrayList<>(List.of(readDays()));
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }
}
