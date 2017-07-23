package myretail.product.command.api;

import myretail.jest.client.api.ElasticsearchApiCommand;
import myretail.product.command.request.ProductRetrievalApiRequest;
import myretail.product.command.response.ProductRetrievalApiResponse;

public interface ProductRetrievalCommand
		extends ElasticsearchApiCommand<ProductRetrievalApiRequest, ProductRetrievalApiResponse> {

}
