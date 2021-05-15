package model;

import utils.TimeUtilities;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * Class for containing an Appointment Object retrieved from the database
 */
public class Appointment {

    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;
    private int custId;
    private int userId;
    private int contactId;
    private String contactName;
    private LocalDateTime localStartTime;
    private LocalDateTime localEndTime;
    private String formattedLocalStartTime;
    private String formattedLocalEndTime;

    public Appointment(){
    }
    public Appointment(int id, String title, String description, String location, String type, Timestamp start,
                       Timestamp end, Timestamp createDate, String createdBy, Timestamp lastUpdate,
                       String lastUpdateBy, int custId, int userId, int contactId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.custId = custId;
        this.userId = userId;
        this.contactId = contactId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStart() {
        return start;
    }

    /**
     * Sets the start time with a timestamp, and also converts the start time to a local date time and a formatted
     * datetime and sets those variables
     * @param start start time
     */
    public void setStart(Timestamp start) {
        this.start = start;
        this.localStartTime = TimeUtilities.utcToLocal(start.toLocalDateTime());
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        this.formattedLocalStartTime = localStartTime.format(format);
    }

    public Timestamp getEnd() {
        return end;
    }

    /**
     * Sets the end time with a timestamp, and also converts the end time to a local date time and a formatted
     * datetime and sets those variables
     * @param end end time
     */
    public void setEnd(Timestamp end) {
        this.end = end;
        this.localEndTime = TimeUtilities.utcToLocal(end.toLocalDateTime());
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        this.formattedLocalEndTime = localEndTime.format(format);
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDateTime getLocalStartTime() {
        return localStartTime;
    }

    public LocalDateTime getLocalEndTime() {
        return localEndTime;
    }

    public String getFormattedLocalStartTime() {
        return formattedLocalStartTime;
    }

    public String getFormattedLocalEndTime() {
        return formattedLocalEndTime;
    }
}
