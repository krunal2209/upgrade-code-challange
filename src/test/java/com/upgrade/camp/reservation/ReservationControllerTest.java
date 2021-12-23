package com.upgrade.camp.reservation;

import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.arrivalDateAfterDepartureDate;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.arrivalDateIsNull;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.bookingAlreadyExist;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.departureDateIsNull;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.emailIsNull;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.fullNameIsNull;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.maxOneMonthInAdvance;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.minimumOneDayBeforeArrival;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.minimumOneDayRequired;
import static com.upgrade.camp.reservation.fixture.ErrorResponseFixture.moreThanThreeDaysReservation;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestForMoreThan1MonthInAdvance;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestForToday;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestWS;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestWithArrivalDateIsAfterDepartureDate;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestWithLessThan1Day;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.reservationRequestWithMoreThan3Days;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestForMoreThan1MonthInAdvance;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestForToday;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestWS;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestWithArrivalDateIsAfterDepartureDate;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestWithLessThan1Day;
import static com.upgrade.camp.reservation.fixture.ReservationFixture.updateReservationRequestWithMoreThan3Days;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.upgrade.camp.reservation.repository.ReservationsRepository;
import com.upgrade.camp.reservation.repository.ReservedDatesRepository;
import com.upgrade.camp.reservation.ws.ErrorResponseWS;
import com.upgrade.camp.reservation.ws.ReservationRequestWS;
import com.upgrade.camp.reservation.ws.ReservationResponseWS;
import com.upgrade.camp.reservation.ws.UpdateReservationRequestWS;

@Profile("test")
@AutoConfigureWebTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ReservationControllerTest {

	private static final String PATH_RESERVATION = "/api/v1/reservation";
	private static final String PATH_RESERVATION_WITH_ID = "/api/v1/reservation/%s";
	private static final String PATH_RESERVATION_AVAILABLE_DATES = "/api/v1/reservation/availableDates";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ReservationsRepository reservationsRepository;

	@Autowired
	private ReservedDatesRepository repositoryDate;

	String reservationId;

	@AfterEach
	public void tearDown() {
		reservationsRepository.deleteAll();
		repositoryDate.deleteAll();
	}

	@Nested
	@DisplayName("Create Reservation Validation Tests")
	class CreateReservationValidationTests {

		@Test
		@DisplayName("Create reservation with arrival date is after departure date.")
		public void testCreateReservationWithArrivalDateIsAfterDepartureDate() {
			sendCreateRequestAndAssert(arrivalDateAfterDepartureDate(), reservationRequestWithArrivalDateIsAfterDepartureDate());
		}

		@Test
		@DisplayName("Create reservation for less than 1 day.")
		public void testCreateReservationForLessThanOneDay() {
			sendCreateRequestAndAssert(minimumOneDayRequired(), reservationRequestWithLessThan1Day());
		}

		@Test
		@DisplayName("Create reservation for more than 3 days.")
		public void testCreateReservationForMoreThanThreeDays() {
			sendCreateRequestAndAssert(moreThanThreeDaysReservation(), reservationRequestWithMoreThan3Days());
		}

		@Test
		@DisplayName("Create reservation for today.")
		public void testCreateReservationForToday() {
			sendCreateRequestAndAssert(minimumOneDayBeforeArrival(), reservationRequestForToday());
		}

		@Test
		@DisplayName("Create reservation for more than 1 month in advance.")
		public void testCreateReservationForMoreThanOneMonthInAdvance() {
			sendCreateRequestAndAssert(maxOneMonthInAdvance(), reservationRequestForMoreThan1MonthInAdvance());
		}

		@Test
		@DisplayName("Create reservation without full name.")
		public void testCreateReservationWithoutFullName() {
			ReservationRequestWS request = reservationRequestWS();
			request.setFullName(null);
			sendCreateRequestAndAssert(fullNameIsNull(), request);
		}

		@Test
		@DisplayName("Create reservation without email address.")
		public void testCreateReservationWithoutEmailAddress() {
			ReservationRequestWS request = reservationRequestWS();
			request.setEmailAddress(null);
			sendCreateRequestAndAssert(emailIsNull(), request);
		}

		@Test
		@DisplayName("Create reservation without arrival date.")
		public void testCreateReservationWithoutArrivalDate() {
			ReservationRequestWS request = reservationRequestWS();
			request.setArrivalDate(null);
			sendCreateRequestAndAssert(arrivalDateIsNull(), request);
		}

		@Test
		@DisplayName("Create reservation without departure date.")
		public void testCreateReservationWithoutDepartureDate() {
			ReservationRequestWS request = reservationRequestWS();
			request.setDepartureDate(null);
			sendCreateRequestAndAssert(departureDateIsNull(), request);
		}

		private void sendCreateRequestAndAssert(ErrorResponseWS expectedResponse, ReservationRequestWS requestWS) {
			webTestClient
					.post()
					.uri("/api/v1/reservation")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(requestWS))
					.exchange()
					.expectStatus()
					.isBadRequest()
					.expectBody(ErrorResponseWS.class)
					.value(response -> {
						assertThat(response).isNotNull();
						assertThat(response).isEqualTo(expectedResponse);
					});
		}
	}

	@Nested
	@DisplayName("Update Reservation Validation Tests")
	class UpdateReservationValidationTests {

		@BeforeEach
		public void setup() {
			webTestClient
					.post()
					.uri("/api/v1/reservation")
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(reservationRequestWS()))
					.exchange()
					.expectStatus()
					.isOk()
					.expectBody(ReservationResponseWS.class)
					.value(response -> reservationId = response.getId());
		}

		@AfterEach
		public void tearDown() {
			reservationsRepository.deleteAll();
			repositoryDate.deleteAll();
		}

		@Test
		@DisplayName("Update reservation with arrival date is after departure date.")
		public void testUpdateReservationWithArrivalDateIsAfterDepartureDate() {
			sendUpdateRequestAndAssert(reservationId, arrivalDateAfterDepartureDate(), updateReservationRequestWithArrivalDateIsAfterDepartureDate());
		}

		@Test
		@DisplayName("Update reservation for less than 1 day.")
		public void testUpdateReservationForLessThanOneDay() {
			sendUpdateRequestAndAssert(reservationId, minimumOneDayRequired(), updateReservationRequestWithLessThan1Day());
		}

		@Test
		@DisplayName("Update reservation for more than 3 days.")
		public void testUpdateReservationForMoreThanThreeDays() {
			sendUpdateRequestAndAssert(reservationId, moreThanThreeDaysReservation(), updateReservationRequestWithMoreThan3Days());
		}

		@Test
		@DisplayName("Update reservation for today.")
		public void testUpdateReservationForToday() {
			sendUpdateRequestAndAssert(reservationId, minimumOneDayBeforeArrival(), updateReservationRequestForToday());
		}

		@Test
		@DisplayName("Update reservation for more than 1 month in advance.")
		public void testUpdateReservationForMoreThanOneMonthInAdvance() {
			sendUpdateRequestAndAssert(reservationId, maxOneMonthInAdvance(), updateReservationRequestForMoreThan1MonthInAdvance());
		}

		@Test
		@DisplayName("Update reservation without arrival date.")
		public void testUpdateReservationWithoutArrivalDate() {
			UpdateReservationRequestWS request = updateReservationRequestWS();
			request.setArrivalDate(null);
			sendUpdateRequestAndAssert(reservationId, arrivalDateIsNull(), request);
		}

		@Test
		@DisplayName("Update reservation without departure date.")
		public void testUpdateReservationWithoutDepartureDate() {
			UpdateReservationRequestWS request = updateReservationRequestWS();
			request.setDepartureDate(null);
			sendUpdateRequestAndAssert(reservationId, departureDateIsNull(), request);
		}

		private void sendUpdateRequestAndAssert(String reservationId, ErrorResponseWS expectedResponse, UpdateReservationRequestWS updateReservationRequestWS) {
			webTestClient
					.patch()
					.uri(format(PATH_RESERVATION_WITH_ID, reservationId))
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.body(BodyInserters.fromValue(updateReservationRequestWS))
					.exchange()
					.expectStatus()
					.isBadRequest()
					.expectBody(ErrorResponseWS.class)
					.value(response -> {
						assertThat(response).isNotNull();
						assertThat(response).isEqualTo(expectedResponse);
					});
		}
	}

	@Test
	public void testGetAvailableDates() {
		webTestClient
				.post()
				.uri(PATH_RESERVATION)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(reservationRequestWS()))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(ReservationResponseWS.class)
				.returnResult().getResponseBody();

		List<LocalDate> availableDates = LocalDate.now().datesUntil(LocalDate.now().plusDays(32))
				.collect(Collectors.toList());
		availableDates.remove(LocalDate.now().plusDays(2));
		availableDates.remove(LocalDate.now().plusDays(3));

		webTestClient
				.get()
				.uri(PATH_RESERVATION_AVAILABLE_DATES)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.is2xxSuccessful()
				.expectBody(new ParameterizedTypeReference<List<LocalDate>>() {
				}).value(response -> {
					assertThat(response).isNotNull();
					assertThat(response).isEqualTo(availableDates);
				});
	}

	@Test
	public void testCancelReservation() {
		webTestClient
				.post()
				.uri(PATH_RESERVATION)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(reservationRequestWS()))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(ReservationResponseWS.class)
				.value(response -> reservationId = response.getId());

		webTestClient
				.delete()
				.uri(format(PATH_RESERVATION_WITH_ID, reservationId))
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus()
				.isNoContent();
	}

	@Test
	public void testConcurrentCreateReservationForSameDate() throws InterruptedException, ExecutionException {
		ReservationRequestWS reservationRequestWS = reservationRequestWS();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<ReservationResponseWS> createReservationResponse1 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(reservationRequestWS))
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody(ReservationResponseWS.class)
						.returnResult().getResponseBody());

		TimeUnit.SECONDS.sleep(1);

		Future<ErrorResponseWS> createReservationResponse2 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(reservationRequestWS))
						.exchange()
						.expectStatus()
						.is4xxClientError()
						.expectBody(ErrorResponseWS.class)
						.returnResult().getResponseBody());

		executorService.shutdown();

		ReservationResponseWS reservationResponseWS = createReservationResponse1.get();
		assertThat(reservationResponseWS.getId()).isNotNull();
		assertThat(reservationResponseWS.getFullName()).isEqualTo(reservationRequestWS.getFullName());
		assertThat(reservationResponseWS.getEmailAddress()).isEqualTo(reservationRequestWS.getEmailAddress());
		assertThat(reservationResponseWS.getArrivalDate()).isEqualTo(reservationRequestWS.getArrivalDate());
		assertThat(reservationResponseWS.getDepartureDate()).isEqualTo(reservationRequestWS.getDepartureDate());
		assertThat(createReservationResponse2.get()).isEqualTo(bookingAlreadyExist());
	}

	@Test
	public void testConcurrentCreateReservationForOverlappingDates() throws InterruptedException, ExecutionException {
		ReservationRequestWS reservationRequestWS = reservationRequestWS();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<ReservationResponseWS> createReservationResponse1 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(reservationRequestWS))
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody(ReservationResponseWS.class)
						.returnResult().getResponseBody());

		TimeUnit.SECONDS.sleep(1);

		ReservationRequestWS overlappingDatesRequest = reservationRequestWS();
		overlappingDatesRequest.setArrivalDate(LocalDate.now().plusDays(2));
		overlappingDatesRequest.setDepartureDate(LocalDate.now().plusDays(3));


		Future<ErrorResponseWS> createReservationResponse2 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(overlappingDatesRequest))
						.exchange()
						.expectStatus()
						.is4xxClientError()
						.expectBody(ErrorResponseWS.class)
						.returnResult().getResponseBody());

		executorService.shutdown();

		ReservationResponseWS reservationResponseWS = createReservationResponse1.get();
		assertThat(reservationResponseWS.getId()).isNotNull();
		assertThat(reservationResponseWS.getFullName()).isEqualTo(reservationRequestWS.getFullName());
		assertThat(reservationResponseWS.getEmailAddress()).isEqualTo(reservationRequestWS.getEmailAddress());
		assertThat(reservationResponseWS.getArrivalDate()).isEqualTo(reservationRequestWS.getArrivalDate());
		assertThat(reservationResponseWS.getDepartureDate()).isEqualTo(reservationRequestWS.getDepartureDate());
		assertThat(createReservationResponse2.get()).isEqualTo(bookingAlreadyExist());
	}

	@Test
	public void testConcurrentCreateReservationForDifferentDate() throws InterruptedException, ExecutionException {
		ReservationRequestWS request1 = reservationRequestWS();
		ReservationRequestWS request2 = reservationRequestWS();
		request2.setArrivalDate(LocalDate.now().plusDays(5));
		request2.setDepartureDate(LocalDate.now().plusDays(7));

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<ReservationResponseWS> createReservationResponse1 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(request1))
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody(ReservationResponseWS.class)
						.returnResult().getResponseBody());

		Future<ReservationResponseWS> createReservationResponse2 = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(request2))
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody(ReservationResponseWS.class)
						.returnResult().getResponseBody());

		executorService.shutdown();
		ReservationResponseWS reservationResponse1WS = createReservationResponse1.get();
		assertThat(reservationResponse1WS.getId()).isNotNull();
		assertThat(reservationResponse1WS.getFullName()).isEqualTo(request1.getFullName());
		assertThat(reservationResponse1WS.getEmailAddress()).isEqualTo(request1.getEmailAddress());
		assertThat(reservationResponse1WS.getArrivalDate()).isEqualTo(request1.getArrivalDate());
		assertThat(reservationResponse1WS.getDepartureDate()).isEqualTo(request1.getDepartureDate());

		ReservationResponseWS reservationResponse2WS = createReservationResponse2.get();
		assertThat(reservationResponse2WS.getId()).isNotNull();
		assertThat(reservationResponse2WS.getFullName()).isEqualTo(request2.getFullName());
		assertThat(reservationResponse2WS.getEmailAddress()).isEqualTo(request2.getEmailAddress());
		assertThat(reservationResponse2WS.getArrivalDate()).isEqualTo(request2.getArrivalDate());
		assertThat(reservationResponse2WS.getDepartureDate()).isEqualTo(request2.getDepartureDate());
	}

	@Test
	public void testConcurrentUpdateReservationAndCreateReservationForSameDate() throws InterruptedException, ExecutionException {
		ReservationRequestWS createReservation = reservationRequestWS();

		webTestClient
				.post()
				.uri(PATH_RESERVATION)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(createReservation))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(ReservationResponseWS.class)
				.value(response -> reservationId = response.getId());

		UpdateReservationRequestWS updateRequest = updateReservationRequestWS();
		updateRequest.setArrivalDate(LocalDate.now().plusDays(5));
		updateRequest.setDepartureDate(LocalDate.now().plusDays(7));

		ReservationRequestWS createReservation1 = reservationRequestWS();
		createReservation1.setArrivalDate(LocalDate.now().plusDays(5));
		createReservation1.setDepartureDate(LocalDate.now().plusDays(7));

		ExecutorService executorService = Executors.newFixedThreadPool(2);
		Future<ReservationResponseWS> createReservationResponse = executorService.submit(() ->
				webTestClient
						.post()
						.uri(PATH_RESERVATION)
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(createReservation1))
						.exchange()
						.expectStatus()
						.isOk()
						.expectBody(ReservationResponseWS.class)
						.returnResult().getResponseBody());

		TimeUnit.SECONDS.sleep(1);

		Future<ErrorResponseWS> updateReservationResponse = executorService.submit(() ->
				webTestClient
						.patch()
						.uri(format(PATH_RESERVATION_WITH_ID, reservationId))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.body(BodyInserters.fromValue(updateRequest))
						.exchange()
						.expectStatus()
						.is4xxClientError()
						.expectBody(ErrorResponseWS.class)
						.returnResult().getResponseBody());

		executorService.shutdown();

		ReservationResponseWS reservationResponseWS = createReservationResponse.get();
		assertThat(reservationResponseWS.getId()).isNotNull();
		assertThat(reservationResponseWS.getFullName()).isEqualTo(createReservation1.getFullName());
		assertThat(reservationResponseWS.getEmailAddress()).isEqualTo(createReservation1.getEmailAddress());
		assertThat(reservationResponseWS.getArrivalDate()).isEqualTo(createReservation1.getArrivalDate());
		assertThat(reservationResponseWS.getDepartureDate()).isEqualTo(createReservation1.getDepartureDate());
		assertThat(updateReservationResponse.get()).isEqualTo(bookingAlreadyExist());
	}
}
