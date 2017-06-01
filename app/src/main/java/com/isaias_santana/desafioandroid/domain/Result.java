package com.isaias_santana.desafioandroid.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Isaías Santana on 31/05/17.
 *         email: isds.santana@gmail.com
 */

public class Result implements Serializable
{
    private String status;
    private String message;
    private String error;

    @SerializedName("error_message")
    private String erroMessage;

    public Result(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErroMessage() {
        return erroMessage;
    }

    public void setErroMessage(String erroMessage) {
        this.erroMessage = erroMessage;
    }
}
