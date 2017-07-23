package myretail.product.command.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.api.response.ApiResponse;
import myretail.jest.client.model.common.ElasticStatus;
import myretail.product.api.model.Product;

@Getter
@Setter
@Builder
public class ProductApiResponse implements ApiResponse {

	Product product;
	ElasticStatus status;

}
