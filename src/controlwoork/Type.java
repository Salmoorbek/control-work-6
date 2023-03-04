package controlwoork;

public enum Type {
    COMMON("обычная задача","black"),
    URGENT("срочное дел","red"),
    JOB("работа","orange"),
    SHOPPING("покупки","pink"),
    ETC("прочее","light-green");
    private String name;
    private String color;

    Type(String name,String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
