package myretail.product.command.api;

import myretail.jest.client.api.ElasticsearchApiCommand;
import myretail.product.command.request.ProductApiRequest;
import myretail.product.command.response.ProductApiResponse;

public interface ProductEsCommand
		extends ElasticsearchApiCommand<ProductApiRequest, ProductApiResponse> {

}
