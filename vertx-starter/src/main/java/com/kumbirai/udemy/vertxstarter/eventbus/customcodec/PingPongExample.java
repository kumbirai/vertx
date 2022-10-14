package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPongExample
{
	private static final Logger LOG = LoggerFactory.getLogger(PingPongExample.class);

	public static void main(String[] args)
	{
		var vertx = Vertx.vertx();
		vertx.deployVerticle(new PingVerticle(), logOnError());
		vertx.deployVerticle(new PongVerticle(), logOnError());
	}

	private static Handler<AsyncResult<String>> logOnError()
	{
		return ar ->
		{
			if (ar.failed())
			{
				LOG.error("err", ar.cause());
			}
		};
	}
}
