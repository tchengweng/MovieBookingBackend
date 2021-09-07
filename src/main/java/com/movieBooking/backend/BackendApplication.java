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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@SpringBootApplication
@EnableMongoRepositories
public class BackendApplication {
//public class BackendApplication implements CommandLineRunner {

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

/*	public void run(String... args) {
		//Fill database
		movieRepo.deleteAll();
		hallRepo.deleteAll();
		screeningRepo.deleteAll();
		reservationRepo.deleteAll();

		movieRepo.save(new Movie("Shang Chi",
				"Martial-arts master Shang-Chi confronts the past he thought " +
						"he left behind when he's drawn into the web of the mysterious Ten Rings organization.",
				"https://m.media-amazon.com/images/M/MV5BNTliYjlkNDQtMjFlNS00NjgzLWFmMWEtYmM2Mzc2Zjg3ZjEyXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_.jpg",
				120));

		movieRepo.save(new Movie("Free Guy",
				"When a bank teller discovers he's actually a background player in an open-world video game, " +
						"he decides to become the hero of his own story -- one that he can rewrite himself. In a world " +
						"where there's no limits, he's determined to save the day his way before it's too late, and " +
						"maybe find a little romance with the coder who conceived him.",
				"https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcQEUMqXik1Ntuc2NTpCgbX2JENwlZD3kwDZa4nDm6TCkXVX9FvU",
				115));

		hallRepo.save(new Hall("Alpha",30,5,6));
		hallRepo.save(new Hall("Beta",40,5,8));

		String movieId = movieRepo.findItemByName("Shang Chi").getId();

		Date startDate = null;
		try {
			startDate = dateSgFormat.get().parse("18-09-2021 18:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Date endDate = null;
		try {
			endDate = dateSgFormat.get().parse("18-09-2021 20:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Hall alphaHall = hallRepo.findItemByName("Alpha");
		String HallAlphaId = alphaHall.getId();
		int seatCapacityAlpha = alphaHall.getSeatCapacity();

		SeatStatus[] seatsAlpha = new SeatStatus[seatCapacityAlpha];

		for(int i=0;i<seatCapacityAlpha;++i)
		{
			seatsAlpha[i] = SeatStatus.EMPTY;
		}

		screeningRepo.save(new Screening(movieId,startDate,endDate,HallAlphaId,seatsAlpha));

		Hall betaHall = hallRepo.findItemByName("Beta");
		String HallBetaId = betaHall.getId();
		int seatCapacityBeta = betaHall.getSeatCapacity();

		SeatStatus[] seatsBeta = new SeatStatus[seatCapacityBeta];

		for(int i=0;i<seatCapacityBeta;++i)
		{
			seatsBeta[i] = SeatStatus.EMPTY;
		}

		screeningRepo.save(new Screening(movieId,startDate,endDate,HallBetaId,seatsBeta));

		try {
			startDate = dateSgFormat.get().parse("18-09-2021 13:30");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try {
			endDate = dateSgFormat.get().parse("18-09-2021 15:25");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Screening sc1 = screeningRepo.save(new Screening(movieRepo.findItemByName("Free Guy").getId(),startDate,endDate,HallAlphaId,seatsAlpha));

		int[] seatsToReserve = new int[3];
		seatsToReserve[0]=1;
		seatsToReserve[1]=2;
		seatsToReserve[2]=3;

		reserveSeats("Cheng Weng","tcwcheng@hotmail.com",seatsToReserve,sc1.getId());
*//*		Tuple2<ReservationStatus,Optional<Reservation>> res2 = reserveSeats("Cheng Weng 2","tcwcheng@hotmail.com",seatsToReserve,sc1.getId());

		System.out.println(res1.getFirst().toString());
		System.out.println(res1.getSecond().get());

		System.out.println(res2.getFirst().toString());
		System.out.println(res2.getSecond().toString());

		if(res1.getFirst() == ReservationStatus.SEATS_SUCCESSFULLY_RESERVED)
		{
			Reservation res = res1.getSecond().get();
			Screening scr = screeningRepo.findById(res.getScreeningId()).get();
			Movie mov = movieRepo.findById(scr.getMovieId()).get();
			Hall hall = hallRepo.findById(scr.getHallId()).get();

			sendEmail(res1.getSecond().get(), scr,hall,mov);
		}*//*

		List<Screening> listOfScreening = screeningRepo.findAllItemByMovieId(movieId);

		for(Screening s: listOfScreening)
		{
			System.out.println(s.getHallId());
			System.out.println(s.getStartTime());
		}
	}*/

	@RequestMapping(
			value = "/ReserveSeats",
			method = RequestMethod.POST)
	public String processPost(@RequestBody Map<String, Object> payload)
			throws Exception {

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
			return "Invalid Json Object";
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
			return "Seats Reserved";
		}
		else if(result.getFirst()==ReservationStatus.SEATS_TAKEN)
		{
			return "Seats Taken";
		}

		return "Screening not found";
	}

	@RequestMapping(
			value = "/AllScreenings",
			method = RequestMethod.GET)
	public ReturnPayload<List<Screening>> allScreeningsGet()
			throws Exception {
		List<Screening> screenings = screeningRepo.findAll();

		return new ReturnPayload<>("query success",screenings);
	}

	@RequestMapping(
			value = "/AllScreeningByMovie",
			method = RequestMethod.POST)
	public ReturnPayload<List<Screening>> allScreeningsByMoviePost(@RequestBody Map<String, Object> payload)
			throws Exception {

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
	public ReturnPayload<Screening> screeningsByIdPost(@RequestBody Map<String, Object> payload)
			throws Exception {

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
			value = "/AllMovies",
			method = RequestMethod.GET)
	public ReturnPayload<List<Movie>> allMoviesGet()
			throws Exception {

		List<Movie> movies = movieRepo.findAll();

		return new ReturnPayload<>("query success",movies);
	}

	@RequestMapping(
			value = "/MovieById",
			method = RequestMethod.POST)
	public ReturnPayload<Movie> movieByIdPost(@RequestBody Map<String, Object> payload)
			throws Exception {

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
				if(seatStatus[i] != SeatStatus.EMPTY)
				{
					seatsAvailable=false;
					break;
				}
				else
				{
					seatStatus[i] = SeatStatus.RESERVED;
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

		msg.setSubject("Movie Reservation Details");
		String body = "Movie: " + movie.getName() +
				"\nHall: " + hall.getName() +
				"\nDate and time: " +  dateSgFormat.get().format(screening.getStartTime()) +
				"\nSeats: " + Arrays.toString(reservation.getSeats());

		msg.setText(body);

		javaMailSender.send(msg);
	}
}
