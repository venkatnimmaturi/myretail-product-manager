package myretail.product.converter.jackson;

import java.io.IOException;

import org.joda.money.Money;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class MoneySerializer extends StdSerializer<Money> {

	private static final long serialVersionUID = -615908247498974703L;

	protected MoneySerializer() {
		super(Money.class);
	}

	@Override
	public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		jgen.writeStartObject();
		jgen.writeNumberField("value", value.getAmount());
		jgen.writeStringField("currency_code", value.getCurrencyUnit().toString());
		jgen.writeEndObject();
	}
}
