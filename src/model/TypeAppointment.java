package model;

public class TypeAppointment {

    private String appointmentType;
    private Integer number;

    public TypeAppointment(String appointmentType, Integer number) {
        this.appointmentType = appointmentType;
        this.number = number;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
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
