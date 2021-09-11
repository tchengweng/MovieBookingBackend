package com.movieBooking.backend.repository;

import com.movieBooking.backend.model.Hall;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface HallRepository extends MongoRepository<Hall, String> {

    @Query("{name:'?0'}")
    Hall findItemByName(String name);
}