package com.diplomathesis.mMusic_Backend.model;

import java.util.List;

public class FullUser extends User{
    private String userDocumentId;

    /**
     * Constructors
     */
    public FullUser() {

    }

    public FullUser(String userDocumentId, Boolean artist, String firstname, String lastname, String phone, String birthday, List<String> playlist, String stageName, List<String> uploads) {
        super(artist, firstname, lastname, phone, birthday, playlist, stageName, uploads);
        this.userDocumentId = userDocumentId;
    }

    /**
     * @return String return the userDocumentId
     */
    public String getUserDocumentId() {
        return userDocumentId;
    }

    /**
     * @param userDocumentId the userDocumentId to set
     */
    public void setUserDocumentId(String userDocumentId) {
        this.userDocumentId = userDocumentId;
    }
    @Override
    public String toString() {
        return "FullUser{" +
                "artist='" + super.getArtist() + '\'' +
                ", firstName='" + super.getFirstname() + '\'' +
                ", lastName='" + super.getLastname() + '\'' +
                ", phone='" + super.getPhone() + '\'' +
                ", birthday='" + super.getBirthday() + '\'' +
                ", playlist='" + super.getPlaylist() + '\'' +
                ", stageName='" + super.getStageName() + '\'' +
                ", uploads='" + super.getUploads() + '\'' +
                ", docId='" + userDocumentId + '\'' +
                '}';
    }

    // get only the user details, without its id
    public User getUser(){
        return new User(super.getArtist(), super.getFirstname(), super.getLastname(), super.getPhone(), super.getBirthday(), super.getPlaylist(), super.getStageName(), super.getUploads());
    }

}