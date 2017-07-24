package myretail.product.rsclient.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import myretail.product.api.model.Product;
import myretail.product.rsclient.config.spring.RestServiceEndpointProperties;
import myretail.product.rsclient.hystrix.exception.HystrixCommandRuntimeException;
import myretail.product.rsclient.model.Status;
import myretail.product.rsclient.model.Status.ResultStatus;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse.RetrieveProductCommandResponseBuilder;

@Component("retrieveProductHystrixCommand")
public class RetrieveProductHystrixCommand {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestServiceEndpointProperties endPointConfig;

	@HystrixCommand(fallbackMethod = "defaultResponse", commandProperties = {
			@HystrixProperty(name = "requestCache.enabled", value = "false"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "15000") })
	public RetrieveProductCommandResponse makeRestCall(RetrieveProductCommandRequest request) {

		Map<String, String> uriParams = new HashMap<>();
		RetrieveProductCommandResponse response = null;

		try {
			uriParams.put("id", request.getProductId().toString());
			JsonNode node = restTemplate.getForObject(endPointConfig.getUrl(), JsonNode.class, uriParams);

			JsonNode title = node.path("product").path("item").path("product_description").path("title");
			if (title instanceof MissingNode) {
				throw new HystrixCommandRuntimeException("Problem finding title node in the Json Response", null);
			} else {
				Product product = Product.builder().id(request.getProductId()).name(title.asText()).build();
				response = RetrieveProductCommandResponse.builder().product(product)
						.status(Status.builder().resultStatus(ResultStatus.SUCCESS).build()).build();
			}

		} catch (Exception e) {
			// client cannot do anything about it.. throw RTE
			throw new HystrixCommandRuntimeException("Problem make a webservice call in hystrix", e.getCause());
		}
		return response;
	}

	public RetrieveProductCommandResponse defaultResponse(RetrieveProductCommandRequest callRequest, Throwable t) {
		RetrieveProductCommandResponseBuilder responseBuilder = RetrieveProductCommandResponse.builder();
		responseBuilder.status(Status.builder().resultStatus(ResultStatus.FAIL).build())
				.product(Product.builder().name("Not Available").id(callRequest.getProductId()).build());
		return responseBuilder.build();
	}

}
