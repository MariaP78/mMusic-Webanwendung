package com.diplomathesis.mMusic_Backend.model;

public class ApiResponseSong {
    private int code;
    private String message;

    public ApiResponseSong(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * @return int return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return String return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}