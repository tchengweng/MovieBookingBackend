package com.movieBooking.backend;

import com.movieBooking.backend.misc.DateSGFormat;
import com.movieBooking.backend.model.Hall;
import com.movieBooking.backend.model.Movie;
import com.movieBooking.backend.model.Reservation;
import com.movieBooking.backend.model.Screening;
import com.movieBooking.backend.payload.ReturnPayload;
import com.movieBooking.backend.repository.HallRepository;
import com.movieBooking.backend.repository.MovieRepository;
import com.movieBooking.backend.repository.ReservationRepository;
import com.movieBooking.backend.repository.ScreeningRepository;
import com.movieBooking.backend.types.ReservationStatus;
import com.movieBooking.backend.types.SeatStatus;
import com.movieBooking.backend.types.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@SpringBootApplication
@EnableMongoRepositories
public class BackendApplication {

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	MovieRepository movieRepo;

	@Autowired
	HallRepository hallRepo;

	@Autowired
	ReservationRepository reservationRepo;

	@Autowired
	ScreeningRepository screeningRepo;

	private final DateSGFormat dateSgFormat = new DateSGFormat();

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	//Check and reserve seats
	private Tuple2<ReservationStatus,Optional<Reservation>>
	reserveSeats(String name, String email, int[] seats, String screeningId)
	{
		Optional<Screening> toResScreening = screeningRepo.findById(screeningId);

		System.out.println("Handling reservation, name: " + name +
				" , email: " + email +
				" , Seats: " + Arrays.toString(seats) +
				" , Screening Id: " + screeningId);

		if(toResScreening.isPresent())
		{
			System.out.println("Screening Id available");

			boolean seatsAvailable = true;
			Screening screening = toResScreening.get();
			SeatStatus[] seatStatus = screening.getSeatStatus();

			//Check if seats are available and reserve
			for(int i=0;i<seats.length;++i)
			{
				if(seatStatus[seats[i]] != SeatStatus.EMPTY)
				{
					seatsAvailable=false;
					break;
				}
				else
				{
					seatStatus[seats[i]] = SeatStatus.RESERVED;
				}
			}

			if(!seatsAvailable)
			{
				System.out.println("Seats TAKEN!");
				return new Tuple2(ReservationStatus.SEATS_TAKEN, Optional.empty());
			}

			screening = screeningRepo.save(screening);
			Reservation reservation = reservationRepo.save(new Reservation(screening.getId(),name,email,seats));

			System.out.println("Seats SUCCESSFULLY_RESERVED!");
			return new Tuple2(ReservationStatus.SEATS_SUCCESSFULLY_RESERVED, Optional.of(reservation));
		}

		System.out.println("Screening Id not found!");
		return new Tuple2(ReservationStatus.SCREENING_ID_NOT_FOUND, Optional.empty());
	}

	public void sendEmail(Reservation reservation, Screening screening, Hall hall, Movie movie) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(reservation.getEmail());

		int[] seats = reservation.getSeats();
		for(int i=0;i<seats.length;++i)
		{
			seats[i]++;
		}

		msg.setSubject("Movie Reservation Details");
		String body = "Movie: " + movie.getName() +
				"\nHall: " + hall.getName() +
				"\nDate and time: " +  dateSgFormat.get().format(screening.getStartTime()) +
				"\nSeats: " + Arrays.toString(reservation.getSeats());

		msg.setText(body);

		javaMailSender.send(msg);
	}

	@RequestMapping(
			value = "/ReserveSeats",
			method = RequestMethod.POST)
	public ReturnPayload<Object> reserveSeatsPost(@RequestBody Map<String, Object> payload) {

		System.out.println(payload);
		System.out.println(payload.containsKey("name"));
		System.out.println(payload.containsKey("email"));
		System.out.println(payload.containsKey("seats"));
		System.out.println(payload.containsKey("screeningId"));

		if(!payload.containsKey("name") ||
				!payload.containsKey("email") ||
				!payload.containsKey("seats") ||
				!payload.containsKey("screeningId"))
		{
			return new ReturnPayload<>("query success", "Invalid Json Object");
		}

		//Convert arrayList to int[]
		ArrayList<Integer> seats = (ArrayList<Integer>)payload.get("seats");
		final int[] seatPrimitive  = new int[seats.size()];
		Arrays.setAll(seatPrimitive, seats::get);

		Tuple2<ReservationStatus,Optional<Reservation>> result = reserveSeats((String)payload.get("name"),
				(String)payload.get("email"),
				seatPrimitive,
				(String)payload.get("screeningId"));

		if(result.getFirst() == ReservationStatus.SEATS_SUCCESSFULLY_RESERVED)
		{
			Reservation res = result.getSecond().get();
			Screening scr = screeningRepo.findById(res.getScreeningId()).get();
			Movie mov = movieRepo.findById(scr.getMovieId()).get();
			Hall hall = hallRepo.findById(scr.getHallId()).get();

			sendEmail(result.getSecond().get(), scr,hall,mov);

			System.out.println("After Send Email");
			return new ReturnPayload<>("query success", "Seats Reserved");
		}
		else if(result.getFirst()==ReservationStatus.SEATS_TAKEN)
		{
			return new ReturnPayload<>("query success", "Seats Taken");
		}

		return new ReturnPayload<>("query success", "Screening not found");
	}

	@RequestMapping(
			value = "/AllScreenings",
			method = RequestMethod.GET)
	public ReturnPayload<List<Screening>> allScreeningsGet() {
		List<Screening> screenings = screeningRepo.findAll();

		return new ReturnPayload<>("query success",screenings);
	}

	@RequestMapping(
			value = "/AllScreeningByMovie",
			method = RequestMethod.POST)
	public ReturnPayload<List<Screening>> allScreeningsByMoviePost(@RequestBody Map<String, Object> payload) {

		if(!payload.containsKey("movieId"))
		{
			return new ReturnPayload<>("Invalid Json format",null);
		}

		List<Screening> listOfScreening = screeningRepo.findAllItemByMovieId((String)payload.get("movieId"));

		return new ReturnPayload<>("query success",listOfScreening);
	}

	@RequestMapping(
			value = "/ScreeningById",
			method = RequestMethod.POST)
	public ReturnPayload<Screening> screeningsByIdPost(@RequestBody Map<String, Object> payload) {

		if(!payload.containsKey("screeningId"))
		{
			return new ReturnPayload<>("Invalid Json format",null);
		}

		Optional<Screening> screening = screeningRepo.findById((String)payload.get("screeningId"));

		if(screening.isPresent())
		{
			return new ReturnPayload<>("query success",screening.get());
		}

		return new ReturnPayload<>("No screening found",null);
	}

	@RequestMapping(
			value = "/HallByScreeningId",
			method = RequestMethod.POST)
	public ReturnPayload<Hall> hallByScreeningIdPost(@RequestBody Map<String, Object> payload) {

		if(!payload.containsKey("screeningId"))
		{
			return new ReturnPayload<>("Invalid Json format",null);
		}

		Optional<Screening> screening = screeningRepo.findById((String)payload.get("screeningId"));
		if(screening.isPresent())
		{
			Optional<Hall> hall = hallRepo.findById(screening.get().getHallId());

			if(hall.isPresent())
			{
				return new ReturnPayload<>("query success",hall.get());
			}
		}

		return new ReturnPayload<>("No hall found",null);
	}

	@RequestMapping(
			value = "/MovieByScreeningId",
			method = RequestMethod.POST)
	public ReturnPayload<Movie> movieByScreeningIdPost(@RequestBody Map<String, Object> payload) {

		if(!payload.containsKey("screeningId"))
		{
			return new ReturnPayload<>("Invalid Json format",null);
		}

		Optional<Screening> screening = screeningRepo.findById((String)payload.get("screeningId"));
		if(screening.isPresent())
		{
			Optional<Movie> movie = movieRepo.findById(screening.get().getMovieId());

			if(movie.isPresent())
			{
				return new ReturnPayload<>("query success",movie.get());
			}
		}

		return new ReturnPayload<>("No hall found",null);
	}

	@RequestMapping(
			value = "/AllMovies",
			method = RequestMethod.GET)
	public ReturnPayload<List<Movie>> allMoviesGet() {

		List<Movie> movies = movieRepo.findAll();

		return new ReturnPayload<>("query success",movies);
	}

	@RequestMapping(
			value = "/AllHalls",
			method = RequestMethod.GET)
	public ReturnPayload<List<Hall>> allHallsGet() {

		List<Hall> halls = hallRepo.findAll();

		return new ReturnPayload<>("query success",halls);
	}

	@RequestMapping(
			value = "/MovieById",
			method = RequestMethod.POST)
	public ReturnPayload<Movie> movieByIdPost(@RequestBody Map<String, Object> payload) {

		if(!payload.containsKey("movieId"))
		{
			return new ReturnPayload<>("Invalid Json format",null);
		}

		Optional<Movie> movie = movieRepo.findById((String)payload.get("movieId"));

		if(movie.isPresent())
		{
			return new ReturnPayload<>("query success",movie.get());
		}

		return new ReturnPayload<>("No movie found",null);
	}
}
