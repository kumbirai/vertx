package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import com.github.javafaker.Faker;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingVerticle extends AbstractVerticle
{
	static final String ADDRESS = PingVerticle.class.getName();
	private static final Logger LOG = LoggerFactory.getLogger(PingVerticle.class);

	@Override
	public void start(final Promise<Void> startPromise)
	{
		var eventBus = vertx.eventBus();
		final Ping message = new Ping("Hello", true);
		LOG.debug("Sending: {}", message);
		// Register only once
		eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
		eventBus.<Pong>request(ADDRESS, message, reply ->
		{
			if (reply.failed())
			{
				LOG.error("Failed: ", reply.cause());
				return;
			}
			LOG.debug("Response: {}", reply.result()
					.body());
		});
		Faker faker = new Faker();
		vertx.setPeriodic(1000, id ->
		{
			Ping ping = Ping.builder()
					.message(faker.chuckNorris()
									 .fact())
					.enabled(faker.bool()
									 .bool())
					.build();
			LOG.debug("Sending: {}", ping);
			vertx.eventBus()
					.<Pong>request(ADDRESS, ping, reply ->
					{
						if (reply.failed())
						{
							LOG.error("Failed: ", reply.cause());
							return;
						}
						LOG.debug("Response: {}{}", reply.result()
								.body(), System.lineSeparator());
					});
		});
		startPromise.complete();
	}
}
