package model;

/**
 * Class used to create the report for appointments in a month. It includes a counter for each retrieved from the
 * database. */
public class MonthAppointment {
    private String month;
    private Integer number;
    private String type;

    public MonthAppointment(String month, String type, Integer number) {
        this.month = month;
        this.number = number;
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addOne(){
        number = number + 1;
    }
}
