package com.n9.dto;

public class StatusDTO {

    public StatusDTO() {

    }

    public StatusDTO(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    private String statusMsg;

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
