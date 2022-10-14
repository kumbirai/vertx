package com.kumbirai.udemy.mutiny.vertx.db;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUsers extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(GetUsers.class);

	@Override
	public Uni<Void> asyncStart()
	{
		var client = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost")
				.setDefaultPort(VertxMutinyReactiveSQL.HTTP_SERVER_PORT));

		vertx.periodicStream(15000L)
				.toMulti()
				.subscribe()
				.with(item ->
					  {
						  LOG.info("Calling GET localhost users");
						  client.get("/users")
								  .send()
								  .onItem()
								  .transform(HttpResponse::bodyAsJsonArray)
								  .subscribe()
								  .with(users -> LOG.debug("Users: {}", users));
					  });

		return Uni.createFrom()
				.voidItem();
	}
}
