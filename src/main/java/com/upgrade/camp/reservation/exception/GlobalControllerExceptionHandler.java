package com.upgrade.camp.reservation.exception;

import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.upgrade.camp.reservation.exception.AbstractReservationServiceException.ApiError;
import com.upgrade.camp.reservation.ws.ErrorResponseWS;

@Slf4j
@NoArgsConstructor
@ControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> defaultExceptionHandler(Exception e) {
		ApiError apiError = AbstractReservationServiceException.ApiError.builder()
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(e.getMessage())
				.build();
		return logAndBuildErrorResponse(apiError, e);
	}

	@ExceptionHandler(value = IllegalArgumentException.class)
	public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException e) {
		ApiError apiError = AbstractReservationServiceException.ApiError.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(e.getMessage())
				.build();
		return logAndBuildErrorResponse(apiError, e);
	}

	@ExceptionHandler(value = AbstractReservationServiceException.class)
	public ResponseEntity<Object> abstractReservationException(AbstractReservationServiceException e) {
		ApiError apiError = e.getApiError();
		return logAndBuildErrorResponse(apiError, e);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		for(ObjectError error : ex.getBindingResult().getAllErrors()) {
			if(error instanceof FieldError) {
				String fieldName = ((FieldError) error).getField();
				String errorMessage = error.getDefaultMessage();
				details.add(fieldName + " " + errorMessage);
			}else {
				details.add(error.getDefaultMessage());
			}
		}
		ApiError apiError = AbstractReservationServiceException.ApiError.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(String.join(",", details))
				.build();
		return logAndBuildErrorResponse(apiError, ex);
	}

	private ResponseEntity<Object> logAndBuildErrorResponse(ApiError apiError, Exception e) {
		logger.error("Unexpected exception occurred: ", e);
		ErrorResponseWS errorResponseWS = ErrorResponseWS.builder()
				.status(apiError.getStatus().value())
				.message(apiError.getMessage())
				.build();

		return ResponseEntity
				.status(apiError.getStatus())
				.body(errorResponseWS);
	}
}
