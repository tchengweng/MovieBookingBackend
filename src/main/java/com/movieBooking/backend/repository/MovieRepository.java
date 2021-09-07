package com.movieBooking.backend.repository;

import com.movieBooking.backend.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface MovieRepository extends MongoRepository<Movie, String>{

    @Query("{name:'?0'}")
    Movie findItemByName(String name);

    public long count();
}

