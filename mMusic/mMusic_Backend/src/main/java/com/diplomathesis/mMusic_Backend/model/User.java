package com.diplomathesis.mMusic_Backend.model;

import java.util.List;

/**
     * User POJO Class
     */
public class User {

    private Boolean artist;
    private String firstname;
    private String lastname;
    private String phone;
    private String birthday;
    private List<String> playlist;
    private String stageName;
    private List<String> uploads;

    /**
     * Constructors
     */
    public User() {

    }  

    public User(Boolean artist, String firstname, String lastname, String phone, String birthday, List<String> playlist,  String stageName, List<String> uploads) {
        this.artist = artist;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.birthday = birthday;
        this.playlist = playlist;
        this.stageName = stageName;
        this.uploads = uploads;
    }

    /**
     * @return Boolean return the artist
     */
    public Boolean getArtist() {
        return artist;
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(Boolean artist) {
        this.artist = artist;
    }
    /**
     * @return String return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return String return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return String return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Timestamp return the birthday
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * @param birthday the birthday to set
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * @return List<String> return the playlist
     */
    public List<String> getPlaylist() {
        return playlist;
    }

    /**
     * @param playlist the playlist to set
     */
    public void setPlaylist(List<String> playlist) {
        this.playlist = playlist;
    }

    /**
     * @return String return the stageName
     */
    public String getStageName() {
        return stageName;
    }

    /**
     * @param stageName the stageName to set
     */
    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    /**
     * @return List<String> return the uploads
     */
    public List<String> getUploads() {
        return uploads;
    }

    /**
     * @param uploads the uploads to set
     */
    public void setUploads(List<String> uploads) {
        this.uploads = uploads;
    }

    @Override
    public String toString() {
        return "User{" +
                "artist='" + artist + '\'' +
                ", firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", birthday='" + birthday + '\'' +
                ", playlist='" + playlist + '\'' +
                ", stageName='" + stageName + '\'' +
                ", uploads='" + uploads + '\'' +
                '}';
    }
}