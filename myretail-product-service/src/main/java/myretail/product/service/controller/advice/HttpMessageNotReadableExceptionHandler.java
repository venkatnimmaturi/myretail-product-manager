package myretail.product.service.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import myretail.product.api.model.response.ErrorResponse;
import myretail.product.api.model.response.ErrorResponse.ErrorResponseBuilder;

@ControllerAdvice(annotations = RestController.class)
public class HttpMessageNotReadableExceptionHandler {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorResponse handleHttpMessageNotReadable(HttpMessageNotReadableException e) {

		ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
		responseBuilder.message(e.getMostSpecificCause().getMessage());
		return responseBuilder.build();
	}
}
