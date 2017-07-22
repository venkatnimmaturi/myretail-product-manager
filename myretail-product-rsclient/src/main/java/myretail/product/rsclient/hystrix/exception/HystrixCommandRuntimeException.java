package myretail.product.rsclient.hystrix.exception;

public class HystrixCommandRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -9002835060197304010L;

	public HystrixCommandRuntimeException(String message, Throwable arg1) {
		super(message, arg1);
	}

}
