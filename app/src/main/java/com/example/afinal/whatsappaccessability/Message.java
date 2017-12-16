package com.example.afinal.whatsappaccessability;

import android.graphics.Rect;

/**
 * Created by Dor on 9/28/2017.
 */

public class Message {

    private MessageType type;
    private int timePri;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    private long timestamp;
    private String content;
    private String name;
    private String time;

    public String getSampleTime() {
        return sampleTime;
    }

    public void setSampleTime(String sampleTime) {
        this.sampleTime = sampleTime;
    }

    private String sampleTime;
    private Rect rectTime;
    private boolean isOutGoing;
    private boolean isGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFlush() {
        return isFlush;
    }

    public void setFlush(boolean flush) {
        isFlush = flush;
    }

    public int getTimePri() {
        return timePri;
    }

    public void setTimePri(int timePri) {
        this.timePri = timePri;
    }
    public void addPri(int timePri) {
        this.timePri |= timePri;
    }
    private boolean isFlush;



    public boolean isInMessage() {
        return isOutGoing;
    }

    public void setInMessage(boolean inMessage) {
        this.isOutGoing = inMessage;
    }

    public enum MessageType {
        MESSAGE, VOICE, IMAGE, FILE, CONTACT, LOCATION
    }

    public enum PriorityTime {
        NONE(1), KEY_LOGGER(9), MAIN_PAGE(19), CHAT_PAGE(27);

        private int priority;
        PriorityTime(int pr){this.priority = pr;}

        public int getValue(){return priority;}

    }


    public Message(MessageType type, PriorityTime priorityTime,String content, String time, Rect rectTime)
    {
        this.timePri = priorityTime.ordinal();
        this.isFlush = false;
        this.type = type;
        this.content = content;
        this.time = time;
        this.rectTime = rectTime;
        isOutGoing = true;
    }
    public Rect getRectTime() {
        return rectTime;
    }

    public void setRectTime(Rect rectTime) {
        this.rectTime = rectTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
}
