package myretail.product.api.model;

import org.joda.money.Money;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import myretail.product.converter.jackson.MoneyDeserializer;
import myretail.product.converter.jackson.MoneySerializer;

@Getter
@Setter
@Builder
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	Long id;
	String name;
	@JsonDeserialize(using = MoneyDeserializer.class)
	@JsonSerialize(using = MoneySerializer.class)
	Money price;
}
