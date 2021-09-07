package com.movieBooking.backend.payload;

public class ReturnPayload<T> {
    private String status;
    private T payload;

    public ReturnPayload(String status, T payload) {
        this.status = status;
        this.payload = payload;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
