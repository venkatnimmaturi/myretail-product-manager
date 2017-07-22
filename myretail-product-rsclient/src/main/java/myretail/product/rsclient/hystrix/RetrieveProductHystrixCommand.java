package myretail.product.rsclient.hystrix;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import myretail.product.rsclient.model.request.RetrieveProductCommandRequest;
import myretail.product.rsclient.model.response.RetrieveProductCommandResponse;

@Component("retrieveProductHystrixCommand")
public class RetrieveProductHystrixCommand {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private URI rootUri;

	public RetrieveProductCommandResponse makeRestCall(RetrieveProductCommandRequest request) {

		Map<String, String> uriParams = new HashMap<>();

		uriParams.put("id", request.getProductId().toString());
		JsonNode node = restTemplate.getForObject(rootUri.toString(), JsonNode.class, uriParams);

		System.out.println(node);
		return null;
	}

}
