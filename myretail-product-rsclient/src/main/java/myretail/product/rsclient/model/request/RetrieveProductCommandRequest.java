package myretail.product.rsclient.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RetrieveProductCommandRequest {

	Long productId;
}
