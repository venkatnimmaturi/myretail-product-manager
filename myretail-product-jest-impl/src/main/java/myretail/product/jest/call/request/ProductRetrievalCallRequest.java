package myretail.product.jest.call.request;

import io.searchbox.core.Get;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.call.request.ElasticsearchCallRequest;

@Getter
@Setter
@Builder
public class ProductRetrievalCallRequest implements ElasticsearchCallRequest {

	Get search;
}
