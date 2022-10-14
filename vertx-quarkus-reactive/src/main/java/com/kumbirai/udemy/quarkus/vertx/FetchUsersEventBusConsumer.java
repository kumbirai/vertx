package com.kumbirai.udemy.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import static com.kumbirai.udemy.quarkus.vertx.PeriodicUserFetcher.ADDRESS;

@ApplicationScoped
public class FetchUsersEventBusConsumer extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(FetchUsersEventBusConsumer.class);

	@Override
	public Uni<Void> asyncStart()
	{
		vertx.eventBus()
				.<JsonArray>consumer(ADDRESS, message ->
				{
					LOG.info("Consumed from Event Bus: {}", message.body());
				});
		return Uni.createFrom()
				.voidItem();
	}
}
