package myretail.product.converter.jackson;

import java.io.IOException;
import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MoneyDeserializer extends StdDeserializer<Money> {

	private static final long serialVersionUID = 7885417944191782452L;

	protected MoneyDeserializer() {
		super(Money.class);
	}

	@Override
	public Money deserialize(JsonParser parser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		final ObjectCodec codec = parser.getCodec();
		final JsonNode node = codec.readTree(parser);
		Assert.notNull(node.get("currency_code"), "Currency code cannot be null");
		Assert.notNull(node.get("value"), "Price cannot be null");
		CurrencyUnit currency = CurrencyUnit.of(node.get("currency_code").asText());
		BigDecimal value = node.get("value").decimalValue();
		return Money.of(currency, value);
	}

}
