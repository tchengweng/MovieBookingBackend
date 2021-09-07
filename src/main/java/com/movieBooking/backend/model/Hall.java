package com.movieBooking.backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Hall")
public class Hall {

    @Id
    private String id;

    private String name;
    private int seatCapacity;
    private int row;
    private int col;

    public Hall(String name, int seatCapacity, int row, int col) {
        this.name = name;
        this.seatCapacity = seatCapacity;
        this.row = row;
        this.col = col;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
