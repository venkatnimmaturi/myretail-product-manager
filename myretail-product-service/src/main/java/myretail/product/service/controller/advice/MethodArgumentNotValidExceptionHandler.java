package myretail.product.service.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import myretail.product.api.model.response.ErrorResponse;
import myretail.product.api.model.response.ErrorResponse.ErrorResponseBuilder;

@ControllerAdvice(annotations = RestController.class)
public class MethodArgumentNotValidExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	ErrorResponse handleHttpMessageNotReadable(MethodArgumentNotValidException e) {

		ErrorResponseBuilder responseBuilder = ErrorResponse.builder();
		StringBuilder stringBuilder = new StringBuilder();
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			stringBuilder.append("Field: ").append(fieldError.getField()).append("; Hint: ")
					.append(fieldError.getDefaultMessage()).append(";");
		}
		responseBuilder.message(stringBuilder.toString());

		return responseBuilder.build();
	}
}
