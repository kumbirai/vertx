package com.kumbirai.udemy.vertxstarter.eventbus.customcodec;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PingPong extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(PingPong.class);

	private static final HttpClient httpClient = HttpClient.newBuilder()
			.version(HttpClient.Version.HTTP_1_1)
			.connectTimeout(Duration.ofSeconds(10))
			.build();

	@Override
	public void start(Promise<Void> startPromise) throws Exception
	{
		vertx.deployVerticle(new PingVerticle(), logOnError());
		vertx.deployVerticle(new PongVerticle(), logOnError());

		vertx.setPeriodic(1000, id ->
		{
			try
			{
				var response = httpClient.send(HttpRequest.newBuilder()
													   .uri(URI.create("http://localhost:8888/"))
													   .GET()
													   .build(), HttpResponse.BodyHandlers.ofString());
				LOG.info(response.body());
			}
			catch (IOException | InterruptedException e)
			{
				LOG.error("err", e);
			}
		});
		startPromise.complete();
	}

	private Handler<AsyncResult<String>> logOnError()
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
