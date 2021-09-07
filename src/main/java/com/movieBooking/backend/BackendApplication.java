package com.movieBooking.backend;

import com.movieBooking.backend.model.Movie;
import com.movieBooking.backend.payload.ReturnPayload;
import com.movieBooking.backend.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@SpringBootApplication
@EnableMongoRepositories
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Autowired
	MovieRepository movieRepo;

	@RequestMapping(
			value = "/test",
			method = RequestMethod.POST)
	public ReturnPayload<List<Movie>> processPost(@RequestBody Map<String, Object> payload)
			throws Exception {

		System.out.println(payload);

		movieRepo.save(new Movie("Shang Chi",
				"Martial-arts master Shang-Chi confronts the past he thought he left behind when he's drawn into the web of the mysterious Ten Rings organization.",
				"https://m.media-amazon.com/images/M/MV5BNTliYjlkNDQtMjFlNS00NjgzLWFmMWEtYmM2Mzc2Zjg3ZjEyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg", 120));

		List<Movie> movies = movieRepo.findAll();

		ReturnPayload<List<Movie>> returnPayload = new ReturnPayload<>("/test post",movies);

		return returnPayload;
	}

	@RequestMapping(
			value = "/test",
			method = RequestMethod.GET)
	public ReturnPayload<List<Movie>> processGet()
			throws Exception {

		List<Movie> movies = movieRepo.findAll();

		ReturnPayload<List<Movie>> returnPayload = new ReturnPayload<>("/test get",movies);

		return returnPayload;
	}
}
