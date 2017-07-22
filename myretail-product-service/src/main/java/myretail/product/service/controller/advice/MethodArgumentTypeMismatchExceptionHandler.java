package myretail.product.service.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import myretail.product.api.model.response.ErrorResponse;
import myretail.product.api.model.response.ErrorResponse.ErrorResponseBuilder;

@ControllerAdvice(annotations = RestController.class)
public class MethodArgumentTypeMismatchExceptionHandler {

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorResponse handleHttpMessageNotReadable(MethodArgumentTypeMismatchException e) {

		ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
		responseBuilder.message(e.getMostSpecificCause().getMessage());
		return responseBuilder.build();
	}
}
