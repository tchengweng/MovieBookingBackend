package com.movieBooking.backend.repository;

import com.movieBooking.backend.model.Screening;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ScreeningRepository extends MongoRepository<Screening, String> {

    @Query("{movieId:'?0'}")
    List<Screening> findAllItemByMovieId(String movieId);
}
