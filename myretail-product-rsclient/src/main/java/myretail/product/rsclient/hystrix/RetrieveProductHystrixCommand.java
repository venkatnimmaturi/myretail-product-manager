package myretail.product.rsclient.hystrix;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.MissingNode;

import myretail.product.api.model.Product;
import myretail.product.rsclient.config.spring.RestServiceEndpointProperties;
import myretail.product.rsclient.model.Status;
import myretail.product.rsclient.model.Status.ResultStatus;
import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component("retrieveProductHystrixCommand")
public class RetrieveProductHystrixCommand {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestServiceEndpointProperties endPointConfig;

	public RetrieveProductCommandResponse makeRestCall(RetrieveProductCommandRequest request) {

		Map<String, String> uriParams = new HashMap<>();
		RetrieveProductCommandResponse response = null;

		uriParams.put("id", request.getProductId().toString());
		JsonNode node = restTemplate.getForObject(endPointConfig.getUrl(), JsonNode.class, uriParams);

		JsonNode title = node.path("product").path("item").path("product_description").path("title");
		if (title instanceof MissingNode) {
			// TODO: deal later
		} else {
			Product product = Product.builder().productId(request.getProductId()).productName(title.asText()).build();
			response = RetrieveProductCommandResponse.builder().product(product)
					.status(Status.builder().resultStatus(ResultStatus.SUCCESS).build()).build();
		}
		return response;
	}

}
