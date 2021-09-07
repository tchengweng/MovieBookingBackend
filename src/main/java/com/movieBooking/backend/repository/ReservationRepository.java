package com.movieBooking.backend.repository;

import com.movieBooking.backend.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    public long count();
}
