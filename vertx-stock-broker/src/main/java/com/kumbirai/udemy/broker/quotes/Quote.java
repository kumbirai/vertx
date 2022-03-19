package com.kumbirai.udemy.broker.quotes;

import com.kumbirai.udemy.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote
{
	Asset asset;
	BigDecimal bid;
	BigDecimal ask;
	BigDecimal lastPrice;
	BigDecimal volume;

	public JsonObject toJsonObject()
	{
		return JsonObject.mapFrom(this);
	}
}
