package com.movieBooking.backend.model;

import com.movieBooking.backend.types.SeatStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("Screening")
public class Screening {

    @Id
    private String id;

    private String movieId;
    private Date startTime;
    private Date endTime;

    private String hallId;
    private SeatStatus[] seatStatus;

    public Screening(String movieId, Date startTime, Date endTime, String hallId, SeatStatus[] seatStatus) {
        this.movieId = movieId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hallId = hallId;
        this.seatStatus = seatStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHallId() {
        return hallId;
    }

    public void setHallId(String hallId) {
        this.hallId = hallId;
    }

    public SeatStatus[] getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(SeatStatus[] seatStatus) {
        this.seatStatus = seatStatus;
    }

}
