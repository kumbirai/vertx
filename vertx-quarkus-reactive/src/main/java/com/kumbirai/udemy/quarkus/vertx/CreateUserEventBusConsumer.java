package com.kumbirai.udemy.quarkus.vertx;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

import static com.kumbirai.udemy.quarkus.vertx.PeriodicUserCreator.ADDRESS;

@ApplicationScoped
public class CreateUserEventBusConsumer extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(CreateUserEventBusConsumer.class);

	@Override
	public Uni<Void> asyncStart()
	{
		var client = WebClient.create(vertx, new WebClientOptions().setDefaultHost("localhost")
				.setDefaultPort(8080));
		vertx.eventBus()
				.<String>consumer(ADDRESS, message ->
				{
					String body = message.body();
					LOG.info("Consumed from Event Bus: {}", body);
					client.get(body)
							.send()
							.subscribe()
							.with(response ->
								  {
									  LOG.info("-->{}", response.body());
								  });
				});
		return Uni.createFrom()
				.voidItem();
	}
}
