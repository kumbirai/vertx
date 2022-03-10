package com.kumbirai.udemy.vertxstarter.eventbus;

import com.github.javafaker.Faker;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class RequestResponseExample
{
	public static void main(String[] args)
	{
		var vertx = Vertx.vertx();
		vertx.deployVerticle(new RequestVerticle());
		vertx.deployVerticle(new ResponseVerticle());
	}

	public static class RequestVerticle extends AbstractVerticle
	{
		static final String ADDRESS = "my.request.address";
		private static final Logger LOG = LoggerFactory.getLogger(RequestVerticle.class);

		@Override
		public void start(final Promise<Void> startPromise) throws Exception
		{
			startPromise.complete();
			var eventBus = vertx.eventBus();
			final String message = "Hello World!";
			LOG.debug("Sending: {}",
					message);
			eventBus.<String>request(ADDRESS,
					message,
					reply ->
					{
						LOG.debug("Response: {}",
								reply.result()
										.body());
					});
			Faker faker = new Faker();
			vertx.setPeriodic(Duration.ofSeconds(4)
							.toMillis(),
					id -> vertx.eventBus()
							.<String>request(ADDRESS,
									faker.chuckNorris()
											.fact(),
									reply ->
									{
										LOG.debug("Response: {}",
												reply.result()
														.body());
									}));
		}
	}

	public static class ResponseVerticle extends AbstractVerticle
	{
		private static final Logger LOG = LoggerFactory.getLogger(ResponseVerticle.class);

		@Override
		public void start(final Promise<Void> startPromise) throws Exception
		{
			startPromise.complete();
			vertx.eventBus()
					.<String>consumer(RequestVerticle.ADDRESS,
							message ->
							{
								LOG.debug("Received Message: {}",
										message.body());
								message.reply("Received your message. Thanks!");
							});
		}
	}
}
