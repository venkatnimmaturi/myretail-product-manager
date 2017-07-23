package myretail.product.command.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.api.request.ApiRequest;
import myretail.product.api.model.Product;

@Getter
@Setter
@Builder
public class ProductApiRequest implements ApiRequest {

	Product product;

	Action action;

	public enum Action {
		READ, UPDATE
	};

}
