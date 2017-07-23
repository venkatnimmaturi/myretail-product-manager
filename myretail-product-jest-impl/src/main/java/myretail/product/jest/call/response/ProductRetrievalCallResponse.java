package myretail.product.jest.call.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.call.response.ElasticsearchCallResponse;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.product.api.model.Product;

@Getter
@Setter
@Builder
public class ProductRetrievalCallResponse implements ElasticsearchCallResponse {

	ElasticStatus status;
	Product result;
}
