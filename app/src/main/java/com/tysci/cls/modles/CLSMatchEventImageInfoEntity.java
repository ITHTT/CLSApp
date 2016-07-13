package com.tysci.cls.modles;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CLSMatchEventImageInfoEntity {

    /**
     * eventImage : http://192.168.10.21:8080/images/event/1.png
     * eventType : 1
     */

    private String eventImage;
    private int eventType;
    private String eventName;

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getEventImage() {
        return eventImage;
    }

    public int getEventType() {
        return eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
