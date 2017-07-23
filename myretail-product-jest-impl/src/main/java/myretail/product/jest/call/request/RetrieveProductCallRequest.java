package myretail.product.jest.call.request;

import io.searchbox.action.Action;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.jest.client.call.request.ElasticsearchCallRequest;

@Getter
@Setter
@Builder
public class RetrieveProductCallRequest implements ElasticsearchCallRequest {

	Action<?> jestAction;
	
	Operation operation;

	public enum Operation {
		READ, UPDATE
	};
}
