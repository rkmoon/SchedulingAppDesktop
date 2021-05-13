package model;

public class MonthAppointment {
    private String month;
    private Integer number;

    public MonthAppointment(String month, Integer number) {
        this.month = month;
        this.number = number;
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

    public void addOne(){
        number = number + 1;
    }
}
