package com.project.transaction_fraud_monitor.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomResponse {
    private String status;
    private String reason;
    private String message;
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
        if (reason != null) {
            this.message = null;
        }
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        if (this.reason == null) {
            this.message = message;
        }
    }

}
