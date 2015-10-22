package com.spielpark.steve.bernieapp.wrappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Steve on 7/9/2015.
 */
public class Event<T> implements Comparable<Event<T>> {

    public boolean isrss = false;
    private String name;
    private String url;
    private String date;
    private String description;
    private String timezone;
    private String eventType;
    private String venue;
    private String venue_addr;
    private String venue_city;
    private String state;
    private String time;
    private int zip;
    private int capacity;
    private int attendee_count;
    private double latitude;
    private double longitude;
    private boolean isOfficial;

    public Event() {

    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String s) {
        this.time = s;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVenue_addr() {
        return venue_addr;
    }

    public void setVenue_addr(String venue_addr) {
        this.venue_addr = venue_addr;
    }

    public String getVenue_city() {
        return venue_city;
    }

    public void setVenue_city(String venue_city) {
        this.venue_city = venue_city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendee_count() {
        return attendee_count;
    }

    public void setAttendee_count(int attendee_count) {
        this.attendee_count = attendee_count;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return this.getName() + " : " + this.getDate();
    }

    @Override
    public int compareTo(Event<T> another) {
        int ret;
        SimpleDateFormat ft = new SimpleDateFormat("MMMM d, yyyy");
        Date l;
        Date r;
        try {
            l = ft.parse(this.getDate());
            r = ft.parse(another.getDate());
            ret = r.compareTo(l);
        } catch (ParseException e) {
            e.printStackTrace();
            ret = -1;
        }
        return ret;
    }
}
