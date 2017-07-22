package myretail.product.rsclient.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(Include.NON_EMPTY)
public class Status {

	private String message;

	private ResultStatus resultStatus;

	public enum ResultStatus {
		UNKNOWN, SUCCESS, FAIL, RETRYO
	}
}
