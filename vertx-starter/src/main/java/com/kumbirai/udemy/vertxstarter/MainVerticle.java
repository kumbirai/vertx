package com.kumbirai.udemy.vertxstarter;

import com.kumbirai.udemy.vertxstarter.eventbus.PointToPointExample;
import com.kumbirai.udemy.vertxstarter.eventbus.PublishSubscribeExample;
import com.kumbirai.udemy.vertxstarter.eventbus.customcodec.PingPong;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle
{
	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

	public static void main(String[] args)
	{
		var vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception
	{
		deployVerticles();

		vertx.createHttpServer()
				.requestHandler(req ->
				{
					req.response()
							.putHeader("content-type",
									"text/plain")
							.end("Hello from Vert.x!")
							.onSuccess(id ->
							{
								LOG.debug("Server Responded...");
							});

				})
				.listen(8888,
						http ->
						{
							if (http.succeeded())
							{
								startPromise.complete();
								LOG.debug("HTTP server started on port 8888");
							}
							else
							{
								startPromise.fail(http.cause());
							}
						});
	}

	private void deployVerticles()
	{
		vertx.deployVerticle(new PingPong(),
				logOnError());
		vertx.deployVerticle(new PointToPointExample.Sender(),
				logOnError());
		vertx.deployVerticle(new PointToPointExample.Receiver(),
				logOnError());
		vertx.deployVerticle(new PublishSubscribeExample.Publish(),
				logOnError());
		vertx.deployVerticle(new PublishSubscribeExample.Subscriber1(),
				logOnError());
		vertx.deployVerticle(PublishSubscribeExample.Subscriber2.class.getName(),
				new DeploymentOptions().setInstances(2),
				logOnError());
	}

	private Handler<AsyncResult<String>> logOnError()
	{
		return ar ->
		{
			if (ar.failed())
			{
				LOG.error("err",
						ar.cause());
			}
		};
	}
}
