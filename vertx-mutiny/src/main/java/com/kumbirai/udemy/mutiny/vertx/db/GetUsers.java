package com.kumbirai.udemy.mutiny.vertx.db;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUsers extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(GetUsers.class);

	@Override
	public Uni<Void> asyncStart()
	{
		vertx.periodicStream(15000L)
				.toMulti()
				.subscribe()
				.with(item ->
				{
					LOG.info("Calling GET localhost users");
					vertx.createHttpClient()
							.request(new RequestOptions().setMethod(HttpMethod.GET)
									.setHost("localhost")
									.setPort(Integer.valueOf(VertxMutinyReactiveSQL.HTTP_SERVER_PORT))
									.setURI("/users"))
							.onItem()
							.transform(httpClientRequest -> httpClientRequest.connect()
									.onItem()
									.transform(httpClientResponse -> httpClientResponse.body()
											.onItem()
											.transform(buffer -> buffer.toString())
											.subscribe()
											.with(str -> LOG.info("-->{}",
													str)))
									.subscribe()
									.with(response -> LOG.debug("Response processed")))
							.subscribe()
							.with(items -> LOG.debug("HTTP Client done"));
				});

		return Uni.createFrom()
				.voidItem();
	}

}
