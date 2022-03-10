package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class PongVerticle extends AbstractVerticle
{
	public static final AtomicInteger id = new AtomicInteger(0);
	private static final Logger LOG = LoggerFactory.getLogger(PongVerticle.class);

	@Override
	public void start(final Promise<Void> startPromise)
	{
		// Register only once
		vertx.eventBus()
				.registerDefaultCodec(Pong.class,
						new LocalMessageCodec<>(Pong.class));
		vertx.eventBus()
				.<Ping>consumer(PingVerticle.ADDRESS,
						message ->
						{
							LOG.debug("Received Message: {}",
									message.body());
							message.reply(Pong.builder()
									.id(id.incrementAndGet())
									.payload(message.body())
									.build());
						})
				.exceptionHandler(error ->
				{
					LOG.error("Error: ",
							error);
				});
		startPromise.complete();
	}
}
