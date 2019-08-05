package com.bluesnap.androidapi.models;

import android.support.annotation.Nullable;

import org.json.JSONObject;

import static com.bluesnap.androidapi.utils.JsonParser.getOptionalString;

/**
 * A representation of API auth response, matches Cardinal V1
 */
public class BS3DSAuthResponse extends  BSModel{

    public static final String AUTHENTICATION_UNAVAILABLE = "AUTHENTICATION_UNAVAILABLE";

    private String enrollmentStatus;
    private String acsUrl;
    private String payload;
    private String transactionId;

    @Nullable
    public static BS3DSAuthResponse fromJson(@Nullable JSONObject jsonObject) {
        if (jsonObject == null)  return null;
        BS3DSAuthResponse response = new BS3DSAuthResponse();
        response.setEnrollmentStatus(getOptionalString(jsonObject, "enrollmentStatus"));
        response.setAcsUrl(getOptionalString(jsonObject, "acsUrl"));
        response.setPayload(getOptionalString(jsonObject, "payload"));
        response.setTransactionId(getOptionalString(jsonObject, "transactionId"));
        return response;
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setAcsUrl(String acsUrl) {
        this.acsUrl = acsUrl;
    }

    public String getAcsUrl() {
        return acsUrl;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "BS3DSAuthResponse{" +
                "enrollmentStatus='" + enrollmentStatus + '\'' +
                ", acsUrl='" + acsUrl + '\'' +
                ", payload='" + payload + '\'' +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
