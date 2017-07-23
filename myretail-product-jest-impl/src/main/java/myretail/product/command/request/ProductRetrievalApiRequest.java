package myretail.product.command.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.api.request.ApiRequest;

@Getter
@Setter
@Builder
public class ProductRetrievalApiRequest implements ApiRequest {

	Long id;

}
