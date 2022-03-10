package com.kumbirai.udemy.vertxstarter.eventbus;

import com.github.javafaker.Faker;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PointToPointExample
{
	public static void main(String[] args)
	{
		var vertx = Vertx.vertx();
		vertx.deployVerticle(new Sender());
		vertx.deployVerticle(new Receiver());
	}

	public static class Sender extends AbstractVerticle
	{
		@Override
		public void start(final Promise<Void> startPromise) throws Exception
		{
			Faker faker = new Faker();
			vertx.setPeriodic(2000,
					id ->
					{
						vertx.eventBus()
								.send(Sender.class.getName(),
										faker.elderScrolls()
												.quote());
					});
			startPromise.complete();
		}
	}

	public static class Receiver extends AbstractVerticle
	{
		private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

		@Override
		public void start(final Promise<Void> startPromise) throws Exception
		{
			vertx.eventBus()
					.<String>consumer(Sender.class.getName(),
							message ->
							{
								LOG.debug("Received: {}",
										message.body());
							});
			startPromise.complete();
		}
	}
}
