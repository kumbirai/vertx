package com.kumbirai.udemy.broker;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class TestMainVerticle extends AbstractRestApiTest
{
	@Test
	void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable
	{
		testContext.completeNow();
	}
}
