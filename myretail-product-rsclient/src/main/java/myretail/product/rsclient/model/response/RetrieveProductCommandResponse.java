package myretail.product.rsclient.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.product.api.model.Product;
import myretail.product.rsclient.model.Status;

@Getter
@Setter
@Builder
public class RetrieveProductCommandResponse {

	Product product;
	Status status;
}
